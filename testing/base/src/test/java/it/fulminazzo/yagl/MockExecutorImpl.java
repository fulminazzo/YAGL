package it.fulminazzo.yagl;

/**
 * A class used by {@link TestUtilsTest}
 */
class MockExecutorImpl {

    MockExecutorImpl throwException() {
        throw new MockException();
    }

    MockExecutorImpl getThis() {
        return this;
    }

    public static class MockException extends RuntimeException {

    }

}
