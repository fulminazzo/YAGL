package it.angrybear.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

/**
 * The type Test utils.
 */
public class TestUtils {

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
