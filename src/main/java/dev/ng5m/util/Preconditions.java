package dev.ng5m.util;

public enum Preconditions {
    ;

    public static void assertTrue(boolean condition, String message) {
        if (!condition) throw new RuntimeException(message);
    }

    public static void assertNotNull(Object o, String message) {
        assertTrue(null != o, message);
    }


}
