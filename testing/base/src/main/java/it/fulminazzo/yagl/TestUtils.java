package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.progress.MockingProgress;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

/**
 * The type Test utils.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

    /**
     * Mocks {@link ReflectionUtils} and executes the given function.
     *
     * @param runnable the function
     */
    public static void mockReflectionUtils(final @NotNull Runnable runnable) {
        try (MockedStatic<ReflectionUtils> ignored = mockStatic(ReflectionUtils.class, CALLS_REAL_METHODS)) {
            runnable.run();
        }
    }

    /**
     * Allows testing all the given <i>executor</i> methods that match the <i>methodFinder</i> predicate.
     * Each one of them is first invoked using {@link #testSingleMethod(Object, Method, Object[], Object, String, Class[])}.
     * Then, it uses <i>captorsValidator</i> to validate the passed parameters.
     *
     * @param executor                the executor
     * @param methodFinder            the method finder
     * @param captorsValidator        the captors validator
     * @param staticObjects           the objects that will be used for the creation of the parameters of <i>targetMethod</i>. If the required class is present among these objects, then the one provided will be used.                                Otherwise, {@link #mockParameter(Class)} will be called.
     * @param target                  the target
     * @param invokedMethod           the invoked method
     * @param invokedMethodParamTypes the type of the parameters when invoking <i>invokedMethod</i>. These will also be the types of the returned captors
     */
    public static void testMultipleMethods(final @NotNull Object executor, final @NotNull Predicate<Method> methodFinder,
                                           final @NotNull Consumer<ArgumentCaptor<?>[]> captorsValidator,
                                           final Object @NotNull [] staticObjects,
                                           final @NotNull Object target, final String invokedMethod,
                                           final Class<?> @NotNull ... invokedMethodParamTypes) {
        @NotNull List<Method> methods = new Refl<>(executor).getMethods(methodFinder);
        if (methods.isEmpty()) throw new IllegalArgumentException("Could not find any method matching the given arguments.");

        for (final Method method : methods) {
            ArgumentCaptor<?> @NotNull [] captors = testSingleMethod(executor, method, staticObjects, target, invokedMethod, invokedMethodParamTypes);
            captorsValidator.accept(captors);
            // Clean up
            try {
                MockingProgress mockingProgress = mockingProgress();
                mockingProgress.validateState();
                mockingProgress.reset();
                mockingProgress.resetOngoingStubbing();
                reset(target);
            } catch (NotAMockException ignored) {}
        }
    }

    /**
     * Allows testing the given <i>targetMethod</i>.
     * It first invokes <i>targetMethod</i> from <i>executor</i>
     * with the given static objects as parameters.
     * Then, it verifies using <b>Mock</b> that the <i>target</i> object
     * invoked <i>invokedMethod</i> during the execution of the prior method.
     *
     * @param executor                the executor
     * @param targetMethod            the target method
     * @param staticObjects           the objects that will be used for the creation of the parameters of <i>targetMethod</i>. If the required class is present among these objects, then the one provided will be used.                                Otherwise, {@link #mockParameter(Class)} will be called.
     * @param target                  the target
     * @param invokedMethod           the invoked method
     * @param invokedMethodParamTypes the type of the parameters when invoking <i>invokedMethod</i>.                                These will also be the types of the returned captors
     * @return the argument captors of the invoked parameters
     */
    public static ArgumentCaptor<?> @NotNull [] testSingleMethod(final @NotNull Object executor, final @NotNull Method targetMethod,
                                                                 final Object @NotNull [] staticObjects,
                                                                 final @NotNull Object target, final String invokedMethod,
                                                                 final Class<?> @NotNull ... invokedMethodParamTypes) {
        // Prepare argument captors
        final ArgumentCaptor<?>[] captors = initializeCaptors(invokedMethodParamTypes);
        try {
            // Execute target method
            final Object[] parameters = initializeParameters(targetMethod.getParameterTypes(), staticObjects);
            ReflectionUtils.setAccessibleOrThrow(targetMethod).invoke(executor, parameters);

            // Verify execution with mock
            Method method = target.getClass().getDeclaredMethod(invokedMethod, invokedMethodParamTypes);
            ReflectionUtils.setAccessibleOrThrow(method).invoke(verify(target),
                    Arrays.stream(captors).map(ArgumentCaptor::capture).toArray(Object[]::new));

            return captors;
        } catch (Exception e) {
            System.err.printf("An exception occurred while testing method '%s'%n", methodToString(targetMethod));
            System.err.printf("target: '%s'%n", target.getClass().getCanonicalName());
            System.err.printf("method: '%s'%n", ReflectionUtils.getMethod(target.getClass(), null, invokedMethod,
                    invokedMethodParamTypes));
            System.err.printf("Invoked method parameter types: '%s'%n", Arrays.toString(invokedMethodParamTypes));
            System.err.printf("Captors: '%s'%n", Arrays.toString(captors));
            ExceptionUtils.throwException(e);
            return null;
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
     * This function allows checking each one to verify that the return type is consistent with the original object.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @param clazz  the class of interest. If there are more implementations of the object, here there should be the most abstract one.
     * @param filter if there are some methods that return a copy or a clone of the object, they should be filtered here.
     */
    public static <T> void testReturnType(final @NotNull T object, final @NotNull Class<? super T> clazz,
                                          final @Nullable Predicate<Method> filter) {
        testReturnType(object, clazz, object.getClass(), filter);
    }

    /**
     * Many objects have setter, adder or remover methods which return the object itself,
     * to allow method chaining.
     * This function allows checking each one to verify that the return type is consistent with the original object.
     *
     * @param <T>                the type parameter
     * @param object             the object
     * @param clazz              the class of interest. If there are more implementations of the object, here there should be the most abstract one.
     * @param expectedReturnType the expected return type of the methods.                           For example, if the object is a hidden implementation,                           the corresponding abstract class (or interface) should be passed.
     * @param filter             if there are some methods that return a copy or a clone of the object, they should be filtered here.
     */
    public static <T> void testReturnType(final @NotNull T object, final @NotNull Class<? super T> clazz,
                                          @NotNull Class<?> expectedReturnType,
                                          final @Nullable Predicate<Method> filter) {
        if (expectedReturnType.getSimpleName().endsWith("Impl"))
            try {
                String name = expectedReturnType.getCanonicalName();
                expectedReturnType = ReflectionUtils.getClass(name.substring(0, name.length() - "Impl".length()));
            } catch (IllegalArgumentException ignored) {}
        for (Method method : clazz.getDeclaredMethods()) {
            final Class<?>[] parameters = method.getParameterTypes();
            final String methodString = methodToString(method);
            try {
                final Class<?> returnType = method.getReturnType();
                if (Modifier.isStatic(method.getModifiers())) continue;
                if (!clazz.isAssignableFrom(returnType)) continue;
                if (filter != null && filter.test(method)) continue;

                final Class<?> objectClass = object.getClass();
                final String objectClassName = objectClass.getSimpleName();
                String errorMessage = String.format("Method '%s' of class '%s' did not return itself",
                        methodString, objectClassName);

                Object[] mockParameters = Arrays.stream(parameters).map(TestUtils::mockParameter).toArray(Object[]::new);
                Object o = ReflectionUtils.setAccessibleOrThrow(method).invoke(object, mockParameters);

                if (method.getName().equals("copy"))
                    assertInstanceOf(objectClass, o, String.format("Returned object from %s call should have been %s but was %s",
                            methodString, objectClass, o.getClass()));
                else {
                    try {
                        ReflectionUtils.getMethod(objectClass, expectedReturnType, method.getName(), method.getParameterTypes());
                    } catch (IllegalArgumentException e) {
                        fail(String.format("Method '%s' of class '%s' did not have return type of '%s'",
                                methodString, objectClassName, objectClassName));
                    }
                    assertEquals(object.hashCode(), o.hashCode(), errorMessage);
                }
            } catch (Exception e) {
                System.err.printf("An exception occurred while testing method '%s'%n", methodString);
                ExceptionUtils.throwException(e);
            }
        }
    }

    /**
     * Mocks the given class to an object.
     *
     * @param clazz the clazz
     * @return the object
     */
    public static Object mockParameter(Class<?> clazz) {
        clazz = ReflectionUtils.getWrapperClass(clazz);
        if (Number.class.isAssignableFrom(clazz)) return 1;
        if (String.class.isAssignableFrom(clazz)) return "STONE";
        if (Boolean.class.isAssignableFrom(clazz)) return false;
        if (clazz.isEnum()) {
            Enum<?>[] enums = new Refl<>(clazz).invokeMethod("values");
            return enums[0];
        }
        if (clazz.isArray()) return Array.newInstance(clazz.getComponentType(), 0);
        if (Collection.class.isAssignableFrom(clazz)) return new ArrayList<>();
        Object object = mock(clazz);
        if (clazz.getPackage().getName().endsWith("guis"))
            when(new Refl<>(object).invokeMethod("size")).thenReturn(45);
        return object;
    }

    private static String methodToString(final @NotNull Method method) {
        return String.format("%s(%s)", method.getName(), Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName).collect(Collectors.joining(", ")));
    }
}
