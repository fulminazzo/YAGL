package it.fulminazzo.yagl;

/**
 * A class used by {@link TestUtilsTest}
 */
class MockExecutorImpl {

    void throwException() {
        throw new MockException();
    }

    MockExecutorImpl getThis() {
        return this;
    }

    public static class MockException extends RuntimeException {

    }

}
