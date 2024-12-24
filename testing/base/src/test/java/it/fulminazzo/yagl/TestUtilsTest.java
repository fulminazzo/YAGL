package it.fulminazzo.yagl;

import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class TestUtilsTest {

    @Test
    void testMultipleMethodsEmpty() {
        assertThrowsExactly(IllegalArgumentException.class, () -> TestUtils.testMultipleMethods(
                new MockExecutor(), m -> false, a -> {}, new Object[0],
                new MockExecutor(), "throwException"
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
                    new MockExecutor(), m -> true, a -> {}, new Object[0],
                    new MockExecutor(), "throwException"
            );
        }
    }

    @Test
    void testTestSingleMethod() {
        MockExecutor executor = new MockExecutor();
        assertThrowsExactly(MockExecutor.MockException.class, () ->
                TestUtils.testSingleMethod(executor, MockExecutor.class.getDeclaredMethod("throwException"),
                        new Object[0], executor, "throwException"));
    }

    @Test
    void mockNoExceptionThrownForJacocoCoverage() throws NoSuchMethodException {
        try (MockedStatic<ExceptionUtils> ignored = mockStatic(ExceptionUtils.class)) {
            when(new Refl<>(ExceptionUtils.class).invokeMethod("throwException", any(Exception.class))).then(a -> null);
            MockExecutor executor = new MockExecutor();
            Object object = TestUtils.testSingleMethod(executor,
                    MockExecutor.class.getDeclaredMethod("throwException"),
                    new Object[0], executor, "throwException");
            assertNull(object);
        }
    }

}