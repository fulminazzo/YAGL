package it.angrybear.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.reset;

/**
 * The type Test utils.
 */
public class TestUtils {

    /**
     * Allows to test the given <i>targetMethod</i>.
     * It verifies that the <i>executor</i> invokes <i>targetMethod</i> and that,
     * upon execution, <i>target</i> invokes <i>invokedMethod</i>.
     *
     * @param executor                the executor
     * @param targetMethod            the target method
     * @param staticObjects           the objects that will be used for the creation of the parameters of <i>targetMethod</i>. If the required class is present among these objects, then the one provided will be used.                                Otherwise, {@link #mockParameter(Class)} will be called.
     * @param target                  the target
     * @param invokedMethod           the invoked method
     * @param invokedMethodParamTypes the type of the parameters when invoking <i>invokedMethod</i>. These will also be the types of the returned captors
     * @return the argument captors of the invoked parameters
     */
    public static ArgumentCaptor<?> @NotNull [] testSingleMethod(final @NotNull Object executor, final @NotNull Method targetMethod,
                                                                 final Object @NotNull [] staticObjects,
                                                                 final @NotNull Object target, final String invokedMethod,
                                                                 final Class<?> @NotNull ... invokedMethodParamTypes) {
        try {
            // Execute target method
            final Object[] parameters = initializeParameters(targetMethod.getParameterTypes(), staticObjects);
            targetMethod.setAccessible(true);
            targetMethod.invoke(executor, parameters);

            // Prepare argument captors
            final ArgumentCaptor<?>[] captors = initializeCaptors(invokedMethodParamTypes);

            // Verify execution with mock
            new Refl<>(verify(target)).invokeMethod(invokedMethod, Arrays.stream(captors)
                    .map(ArgumentCaptor::capture).toArray(Object[]::new));

            return captors;
        } catch (Exception e) {
            System.err.printf("An exception occurred while testing method '%s'%n", methodToString(targetMethod));
            ExceptionUtils.throwException(e);
            throw new IllegalStateException("Unreachable code");
        }
    }

    private static Object[] initializeParameters(final Class<?> @NotNull [] classes, final Object @NotNull ... staticObjects) {
        return Arrays.stream(classes).map(c -> initializeSingle(c, staticObjects)).toArray(Object[]::new);
    }

    private static Object initializeSingle(final Class<?> clazz, final Object @NotNull ... staticObjects) {
        for (Object o : staticObjects) if (clazz.isAssignableFrom(o.getClass())) return o;
        return TestUtils.mockParameter(clazz);
    }

    private static ArgumentCaptor<?>[] initializeCaptors(final Class<?> @NotNull [] classes) {
        return Arrays.stream(classes).map(ArgumentCaptor::forClass).toArray(ArgumentCaptor[]::new);
    }

    /**
     * Many objects have setter, adder or remover methods which return the object itself,
     * to allow method chaining.
     * This function allows to check each one to verify that the return type is consistent with the original object.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @param clazz  the class of interest. If there are more implementations of the object, here there should be the most abstract one.
     * @param filter if there are some methods that return a copy or a clone of the object, they should be filtered here.
     */
    public static <T> void testReturnType(final @NotNull T object, final @NotNull Class<? super T> clazz,
                                          final @Nullable Predicate<Method> filter) {
        final Refl<?> refl = new Refl<>(object);

        for (Method method : refl.getNonStaticMethods()) {
            final Class<?>[] parameters = method.getParameterTypes();
            final String methodString = methodToString(method);
            try {
                final Class<?> returnType = method.getReturnType();
                if (!clazz.isAssignableFrom(returnType)) continue;
                if (filter != null && filter.test(method)) return;

                String errorMessage = String.format("Method '%s' of class '%s' did not return itself",
                        methodString, object.getClass().getSimpleName());

                Object[] mockParameters = Arrays.stream(parameters).map(TestUtils::mockParameter).toArray(Object[]::new);
                method.setAccessible(true);
                Object o = method.invoke(object, mockParameters);

                if (method.getName().equals("copy"))
                    assertInstanceOf(object.getClass(), o, String.format("Returned object from %s call should have been %s but was %s",
                            methodString, object.getClass(), o.getClass()));
                else assertEquals(object.hashCode(), o.hashCode(), errorMessage);
            } catch (Exception e) {
                System.err.printf("An exception occurred while testing method '%s'%n", methodString);
                ExceptionUtils.throwException(e);
            }
        }
    }

    private static Object mockParameter(Class<?> clazz) {
        clazz = ReflectionUtils.getWrapperClass(clazz);
        if (Number.class.isAssignableFrom(clazz)) return 1;
        if (String.class.isAssignableFrom(clazz)) return "STONE";
        if (Boolean.class.isAssignableFrom(clazz)) return false;
        if (clazz.isEnum()) {
            Enum<?>[] enums = new Refl<>(clazz).invokeMethod("values");
            if (enums == null) return null;
            return enums[0];
        }
        if (clazz.isArray()) return Array.newInstance(clazz.getComponentType(), 0);
        if (Collection.class.isAssignableFrom(clazz)) return new ArrayList<>();
        return mock(clazz);
    }

    private static String methodToString(final @NotNull Method method) {
        return String.format("%s(%s)", method.getName(), Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName).collect(Collectors.joining(", ")));
    }
}
