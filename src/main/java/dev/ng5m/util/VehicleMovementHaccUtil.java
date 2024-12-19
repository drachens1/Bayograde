package dev.ng5m.util;

public final class VehicleMovementHaccUtil {

    public static boolean jumping(byte flags) {
        return (flags & 0x01) != 0;
    }

    public static boolean sneaking(byte flags) {
        return (flags & 0x02) != 0;
    }


}
