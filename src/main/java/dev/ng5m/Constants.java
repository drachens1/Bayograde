package dev.ng5m;

import dev.ng5m.bansystem.BanManager;

import java.io.File;

public class Constants {
    public static final BanManager BAN_MANAGER = new BanManager(new File("bans.json"));
}
