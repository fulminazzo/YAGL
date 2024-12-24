package it.fulminazzo.yagl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestUtilsTest {

    @Test
    void testTestSingleMethod() {
        MockExecutor executor = new MockExecutor();
        assertThrowsExactly(MockExecutor.MockException.class, () ->
                TestUtils.testSingleMethod(executor, MockExecutor.class.getDeclaredMethod("throwException"),
                        new Object[0], executor, "throwException"));
    }

}