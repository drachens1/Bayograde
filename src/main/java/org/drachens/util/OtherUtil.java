package org.drachens.util;

public class OtherUtil {
    public static float[] yawToQuat(double yaw) {
        float rad = (float) Math.toRadians(yaw);
        float half = rad / 2;

        return new float[]{
                0f, (float) Math.sin(half),
                0f, (float) Math.cos(half)
        };
    }
}
