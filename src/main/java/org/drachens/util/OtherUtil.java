package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import org.intellij.lang.annotations.RegExp;

import java.util.ArrayList;
import java.util.List;

public enum OtherUtil {
    ;

    public static String posToString(Pos pos) {
        return pos.x() + ", " + pos.z();
    }

    public static float[] yawToQuat(double yaw) {
        float rad = (float) Math.toRadians(yaw);
        float half = rad / 2;

        return new float[]{
                0.0f, (float) Math.sin(half),
                0.0f, (float) Math.cos(half)
        };
    }

    public static String formatPlaytime(long seconds) {
        long months = seconds / (30L * 24 * 60 * 60);
        seconds %= 30L * 24 * 60 * 60;
        long days = seconds / (24 * 60 * 60);
        seconds %= 24 * 60 * 60;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder formattedTime = new StringBuilder();

        if (0 < months) {
            formattedTime.append(months).append(" Month").append(1 < months ? "s" : "").append(' ');
        }

        if (0 < days) {
            formattedTime.append(days).append(" Day").append(1 < days ? "s" : "").append(' ');
        }

        if (0 < hours) {
            formattedTime.append(hours).append(" Hour").append(1 < hours ? "s" : "").append(' ');
        }

        if (0 < minutes) {
            formattedTime.append(minutes).append(" Minute").append(1 < minutes ? "s" : "").append(' ');
        }

        if (0 < seconds) {
            formattedTime.append(seconds).append(" Second").append(1 != seconds ? "s" : "");
        }

        return formattedTime.toString().trim();
    }

    public static float bound(float upper, float lower, float value) {
        if (value > upper) return upper;
        return Math.max(value, lower);
    }

    public static void runThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static Component replaceWith(Component component, @RegExp String from, String to){
        return component.replaceText(builder -> builder
                .match(from)
                .replacement(to)
        );
    }

    public static <T> List<T> addToList(List<T> list, T value) {
        list = new ArrayList<>(list);
        list.add(value);
        return list;
    }
}
