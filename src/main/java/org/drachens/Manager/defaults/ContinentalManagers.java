package org.drachens.Manager.defaults;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.AchievementsManager;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.YearManager;
import org.drachens.Manager.defaults.defaultsStorer.DefaultsStorer;
import org.drachens.dataClasses.WorldClasses;

import static org.drachens.util.ServerUtil.getWorldClasses;

public class ContinentalManagers {
    public static final AchievementsManager achievementsManager = new AchievementsManager();
    public static final ConfigFileManager configFileManager = new ConfigFileManager();
    public static final PermissionsUtil permissions = new PermissionsUtil();
    public static final YearManager yearManager = new YearManager(MinecraftServer.getInstanceManager().getInstances().stream().toList());
    public static final DefaultsStorer defaultsStorer = new DefaultsStorer();
    public static final InventoryManager inventoryManager = new InventoryManager();
    public static WorldClasses world(Instance instance){
        return getWorldClasses(instance);
    }
}
