package org.drachens.Manager.defaults;

import net.minestom.server.instance.Instance;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.*;
import org.drachens.Manager.defaults.scheduler.ContinentalSchedulerManager;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.advancement.AdvancementManager;
import org.drachens.dataClasses.WorldClasses;
import org.drachens.fileManagement.ConfigFileManager;
import org.drachens.fileManagement.databases.Database;
import org.drachens.store.CosmeticsManager;

import static org.drachens.util.ServerUtil.getWorldClasses;

public class ContinentalManagers {
    public static final PlayerModsManager playerModsManager = new PlayerModsManager();
    public static final ConfigFileManager configFileManager = new ConfigFileManager();
    public static final PermissionsUtil permissions = new PermissionsUtil();
    public static final YearManager yearManager = new YearManager();
    public static final InventoryManager inventoryManager = new InventoryManager();
    public static final GUIManager guiManager = new GUIManager();
    public static final DemandManager demandManager = new DemandManager();
    public static final CosmeticsManager cosmeticsManager = new CosmeticsManager();
    public static final ContinentalSchedulerManager schedulerManager = new ContinentalSchedulerManager();
    public static final ScoreboardManager scoreboardManager = new ScoreboardManager();
    public static final AdvancementManager advancementManager = new AdvancementManager();
    public static final WorldManager worldManager = new WorldManager();
    public static final ImaginaryWorldManager imaginaryWorldManager = new ImaginaryWorldManager();
    public static final CombatManager combatManager = new CombatManager();
    public static final ChannelManager channelManager = new ChannelManager();
    public static final ServerHealthManager serverHealthManager = new ServerHealthManager();
    public static final CentralAIManager centralAIManager = new CentralAIManager();
    public static final PlayerSettingsManager playerSettingsManager = new PlayerSettingsManager();
    public static Database database;

    public static WorldClasses world(Instance instance) {
        return getWorldClasses(instance);
    }
}
