package dev.ng5m;

import java.io.File;

public class ModerationManager extends JSONManager<Integer> {
    public ModerationManager(File file) {
        super(file);
    }

    @Override
    public boolean is(Integer o) {
        return false;
    }
}
