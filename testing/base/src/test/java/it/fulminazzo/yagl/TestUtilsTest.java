package it.fulminazzo.yagl;

import it.fulminazzo.yagl.reflect.Reflect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class TestUtilsTest {

    @Test
    void testMultipleMethodsEmpty() {
        assertThrowsExactly(IllegalArgumentException.class, () -> TestUtils.testMultipleMethods(
                new MockExecutorImpl(), m -> false, a -> {
                }, new Object[0],
                new MockExecutorImpl(), "throwException"
        ));
    }

    @Test
    void testResetFailure() {
        try (MockedStatic<TestUtils> ignored = mockStatic(TestUtils.class)) {
            when(TestUtils.testSingleMethod(any(), any(), any(), any(), any())).thenAnswer(a -> null);
            when(Reflect.on(TestUtils.class).invoke("testMultipleMethods",
                    any(), any(), any(), any(), any(), any(), any())).thenCallRealMethod();

            TestUtils.testMultipleMethods(
                    new MockExecutorImpl(), m -> true, a -> {
                    }, new Object[0],
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
        try (MockedStatic<TestUtils> testUtils = mockStatic(TestUtils.class, CALLS_REAL_METHODS)) {
            testUtils.when(() -> TestUtils.throwException(any(Exception.class))).then(a -> null);
            MockExecutorImpl executor = new MockExecutorImpl();
            Object object = TestUtils.testSingleMethod(executor,
                    MockExecutorImpl.class.getDeclaredMethod("throwException"),
                    new Object[0], executor, "throwException");
            assertNull(object);
        }
    }

    @Test
    void testTestReturnTypeIllegalArgumentException() {
        try (MockedStatic<Assertions> assertions = mockStatic(Assertions.class)) {
            TestUtils.testReturnType(new MockExecutorImpl(), MockExecutorImpl.class, String.class,
                    m -> !m.getName().equals("getThis"));
            assertions.verify(() -> Assertions.fail("Method 'getThis()' of class 'MockExecutorImpl' did not have return type of 'MockExecutorImpl'"));
        }
    }

    @Test
    void testTestReturnTypeGeneralException() {
        try (MockedStatic<TestUtils> testUtils = mockStatic(TestUtils.class, CALLS_REAL_METHODS)) {
            testUtils.when(() -> TestUtils.throwException(any(Exception.class))).then(a -> null);
            TestUtils.testReturnType(
                    new MockExecutorImpl(), MockExecutorImpl.class, MockExecutorImpl.class,
                    m -> !m.getName().equals("throwException"));
            testUtils.verify(() -> TestUtils.throwException(any(InvocationTargetException.class)));
        }
    }

}
