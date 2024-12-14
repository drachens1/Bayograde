package dev.ng5m.util;

public final class Preconditions {

    public static void assertTrue(boolean condition, String message) {
        if (!condition) throw new RuntimeException(message);
    }

    public static void assertNotNull(Object o, String message) {
        assertTrue(o != null, message);
    }


}
