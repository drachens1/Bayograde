package org.drachens.util;

import net.minestom.server.coordinate.Pos;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OtherUtil {
    public static String posToString(Pos pos) {
        return pos.x() + ", " + pos.z();
    }

    public static float[] yawToQuat(double yaw) {
        float rad = (float) Math.toRadians(yaw);
        float half = rad / 2;

        return new float[]{
                0f, (float) Math.sin(half),
                0f, (float) Math.cos(half)
        };
    }

    public static String formatPlaytime(long seconds) {
        long months = seconds / (30L * 24 * 60 * 60);
        seconds %= (30L * 24 * 60 * 60);
        long days = seconds / (24 * 60 * 60);
        seconds %= (24 * 60 * 60);
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder formattedTime = new StringBuilder();

        if (months > 0) {
            formattedTime.append(months).append(" Month").append(months > 1 ? "s" : "").append(" ");
        }

        if (days > 0) {
            formattedTime.append(days).append(" Day").append(days > 1 ? "s" : "").append(" ");
        }

        if (hours > 0) {
            formattedTime.append(hours).append(" Hour").append(hours > 1 ? "s" : "").append(" ");
        }

        if (minutes > 0) {
            formattedTime.append(minutes).append(" Minute").append(minutes > 1 ? "s" : "").append(" ");
        }

        if (seconds > 0) {
            formattedTime.append(seconds).append(" Second").append(seconds != 1 ? "s" : "");
        }

        return formattedTime.toString().trim();
    }

    public static float bound(float upper, float lower, float value) {
        if (value > upper) {
            return upper;
        }
        return Math.max(value, lower);
    }

    public static void runThread(Runnable runnable) {
        new Thread(runnable).start();
    }
}
