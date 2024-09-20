package dev.ng5m;

import dev.ng5m.bansystem.BanManager;
import dev.ng5m.bansystem.BanSystemEvents;
import dev.ng5m.events.EventHandlerProviderManager;

import java.io.File;

public class Constants {

    public static final BanManager BAN_MANAGER = new BanManager(new File("playerData" + File.separator + "bans.json"));

}
