package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class TestUtilsTest {

    @Test
    void testMultipleMethodsEmpty() {
        assertThrowsExactly(IllegalArgumentException.class, () -> TestUtils.testMultipleMethods(
                new MockExecutorImpl(), m -> false, a -> {}, new Object[0],
                new MockExecutorImpl(), "throwException"
        ));
    }

    @Test
    void testResetFailure() {
        try (MockedStatic<TestUtils> ignored = mockStatic(TestUtils.class)) {
            when(TestUtils.testSingleMethod(any(), any(), any(), any(), any())).thenAnswer(a -> null);
            when(new Refl<>(TestUtils.class).invokeMethod("testMultipleMethods",
                    new Class[]{Object.class, Predicate.class, Consumer.class, Object[].class, Object.class, String.class, Class[].class},
                    any(), any(), any(), any(), any(), any(), any())).thenCallRealMethod();

            TestUtils.testMultipleMethods(
                    new MockExecutorImpl(), m -> true, a -> {}, new Object[0],
                    new MockExecutorImpl(), "throwException"
            );
        }
    }

    @Test
    void testTestSingleMethod() {
        MockExecutorImpl executor = new MockExecutorImpl();
        assertThrowsExactly(MockExecutorImpl.MockException.class, () ->
                TestUtils.testSingleMethod(executor, MockExecutorImpl.class.getDeclaredMethod("throwException"),
                        new Object[0], executor, "throwException"));
    }

    @Test
    void mockNoExceptionThrownForJacocoCoverage() throws NoSuchMethodException {
        try (MockedStatic<ExceptionUtils> ignored = mockStatic(ExceptionUtils.class)) {
            when(new Refl<>(ExceptionUtils.class).invokeMethod("throwException", any(Exception.class))).then(a -> null);
            MockExecutorImpl executor = new MockExecutorImpl();
            Object object = TestUtils.testSingleMethod(executor,
                    MockExecutorImpl.class.getDeclaredMethod("throwException"),
                    new Object[0], executor, "throwException");
            assertNull(object);
        }
    }

    @Test
    void testTestReturnTypeIllegalArgumentException() {
        assertThrowsExactly(AssertionFailedError.class, () -> TestUtils.testReturnType(
                new MockExecutorImpl(), MockExecutorImpl.class, String.class,
                m -> !m.getName().equals("getThis")));
    }

    @Test
    void testTestReturnTypeGeneralException() {
        try (MockedStatic<ExceptionUtils> exceptionUtils = mockStatic(ExceptionUtils.class)) {
            when(new Refl<>(ExceptionUtils.class).invokeMethod("throwException", any(Exception.class))).then(a -> null);
            TestUtils.testReturnType(
                    new MockExecutorImpl(), MockExecutorImpl.class, MockExecutorImpl.class,
                    m -> !m.getName().equals("throwException"));
            exceptionUtils.verify(() -> ExceptionUtils.throwException(any(InvocationTargetException.class)));
        }
    }

}