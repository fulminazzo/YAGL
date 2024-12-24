package it.fulminazzo.yagl;

/**
 * A class used by {@link TestUtilsTest}
 */
class MockExecutor {

    void throwException() {
        throw new MockException();
    }

    public static class MockException extends RuntimeException {

    }

}
