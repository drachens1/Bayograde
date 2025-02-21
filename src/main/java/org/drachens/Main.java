package org.drachens;

import dev.ng5m.NG5M;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.*;
import org.drachens.advancement.Advancement;
import org.drachens.advancement.AdvancementManager;
import org.drachens.advancement.AdvancementSection;
import org.drachens.cmd.ConfirmCMD;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Research.ResearchCategoryEnum;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.Research.tree.ResearchCategory;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.drachens.fileManagement.databases.DataTypeEum;
import org.drachens.fileManagement.databases.Database;
import org.drachens.fileManagement.databases.Table;
import org.drachens.generalGame.MapGeneratorManager;
import org.drachens.generalGame.clicks.ClickWarSystem;
import org.drachens.generalGame.clicks.ClicksAI;
import org.drachens.generalGame.factory.Factory;
import org.drachens.generalGame.research.ResearchLab;
import org.drachens.generalGame.research.ResearchLibrary;
import org.drachens.generalGame.research.ResearchUniversity;
import org.drachens.generalGame.troops.TroopAI;
import org.drachens.generalGame.troops.TroopWarSystem;
import org.drachens.generalGame.troops.buildings.Barracks;
import org.drachens.store.StoreCategory;
import org.drachens.store.items.Hat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.ServerUtil.initSrv;
import static org.drachens.util.ServerUtil.setupAll;

public enum Main {
    ;
    private static final String[] firstName = {
            "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth",
            "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen",
            "Christopher", "Nancy", "Daniel", "Lisa", "Matthew", "Margaret", "Anthony", "Betty", "Mark", "Sandra",
            "Donald", "Ashley", "Steven", "Dorothy", "Paul", "Kimberly", "Andrew", "Emily", "Joshua", "Donna",
            "Kenneth", "Michelle", "Kevin", "Carol", "Brian", "Amanda", "George", "Melissa", "Edward", "Deborah",
            "Ronald", "Stephanie", "Timothy", "Rebecca", "Jason", "Sharon", "Jeffrey", "Laura", "Ryan", "Cynthia",
            "Jacob", "Kathleen", "Gary", "Amy", "Nicholas", "Shirley", "Eric", "Angela", "Jonathan", "Helen",
            "Stephen", "Anna", "Larry", "Brenda", "Justin", "Pamela", "Scott", "Nicole", "Brandon", "Emma",
            "Benjamin", "Samantha", "Samuel", "Katherine", "Gregory", "Christine", "Frank", "Debra", "Alexander", "Rachel",
            "Raymond", "Catherine", "Patrick", "Carolyn", "Jack", "Janet", "Dennis", "Ruth", "Jerry", "Maria",
            "Tyler", "Heather", "Aaron", "Diane", "Jose", "Virginia", "Adam", "Julie", "Henry", "Joyce",
            "Nathan", "Victoria", "Douglas", "Olivia", "Zachary", "Kelly", "Peter", "Christina", "Kyle", "Lauren",
            "Walter", "Joan", "Ethan", "Evelyn", "Jeremy", "Judith", "Harold", "Megan", "Keith", "Cheryl",
            "Christian", "Andrea", "Roger", "Hannah", "Noah", "Martha", "Gerald", "Jacqueline", "Carl", "Frances",
            "Terry", "Gloria", "Sean", "Ann", "Austin", "Teresa", "Arthur", "Sara", "Lawrence", "Janice",
            "Jesse", "Jean", "Dylan", "Alice", "Bryan", "Madison", "Joe", "Doris", "Jordan", "Abigail",
            "Billy", "Julia", "Bruce", "Judty", "Albert", "Grace", "Willie", "Denise", "Gabriel", "Amber",
            "Logan", "Marilyn", "Alan", "Beverly", "Juan", "Danielle", "Wayne", "Theresa", "Roy", "Sophia",
            "Ralph", "Marie", "Randy", "Diana", "Eugene", "Brittany", "Vincent", "Natalie", "Russell", "Isabella",
            "Elijah", "Charlotte", "Louis", "Rose", "Bobby", "Alexis", "Philipt", "Kayla", "Johnny", "Lillian",
            "Howard", "Clara", "Mary", "Jacob", "Landon", "Savannah", "Oliver", "Liam", "Mason", "Sofia",
            "Jayden", "Ella", "Logan", "Ava", "Isabella", "Zoe", "Caden", "Aiden", "Jack", "Brooklyn",
            "Elena", "Henry", "Zachary", "Mia", "Joshua", "Matthew", "Samuel", "Levi", "Nathan", "Lucas",
            "Miles", "Emmett", "Oliver", "Lily", "Eva", "Hazel", "Violet", "Nathaniel", "Jeremiah", "Jameson",
            "Milo", "Aurora", "Isabelle", "Juliet", "Josephine", "Sienna", "Sophie", "Stella", "Molly", "Jasmine",
            "Alice", "Penelope", "Savannah", "Rosalie", "Ada", "Clara", "Delilah", "Brielle", "Paisley", "Emilia",
            "Adeline", "Serenity", "Liliana", "Scarlett", "Victoria", "Samantha", "Valentina", "Aria", "Amelia", "Chloe",
            "Emily", "Elizabeth", "Grace", "Harper", "Layla", "Madeline", "Olivia", "Sophia", "Zoe", "Avery",
            "Isabella", "Ella", "Lillian", "Eleanor", "Evelyn", "Ariana", "Gabriella", "Lucy", "Aubrey", "Brooklyn",
            "Kennedy", "Hannah", "Bella", "Audrey", "Skylar", "Savannah", "Riley", "Mila", "Ruby", "Aurora",
            "Sadie", "Sienna", "Leah", "Maya", "Nova", "Quinn", "Everly", "Jade", "Piper", "Nora",
            "Willow", "Sydney", "Melody", "Blake", "Luna", "Riley", "Alexandra", "Kylie", "Zara", "Amaya",
            "Addison", "Maya", "Alyssa", "Faith", "Zara", "Serena", "Madison", "Adeline", "Alexa", "Arianna",
            "Ella", "Naomi", "Isla", "Valeria", "Raegan", "Caitlyn", "Brianna", "Everleigh", "Giselle", "Selena",
    };
    private static final String[] lastName = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
            "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
            "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
            "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
            "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
            "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes",
            "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper",
            "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson",
            "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes",
            "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez",
            "Powell", "Jenkins", "Perry", "Russell", "Sullivan", "Bell", "Coleman", "Butler", "Henderson", "Barnes",
            "Gonzales", "Fisher", "Vasquez", "Simmons", "Romero", "Jordan", "Patterson", "Alexander", "Hamilton", "Graham",
            "Reynolds", "Griffin", "Wallace", "Moreno", "West", "Cole", "Hayes", "Bryant", "Herrera", "Gibson",
            "Ellis", "Tran", "Medina", "Aguilar", "Stevens", "Murray", "Ford", "Castro", "Marshall", "Owens",
            "Harrison", "Fernandez", "McDonald", "Woods", "Washington", "Kennedy", "Wells", "Vargas", "Henry", "Chen",
            "Freeman", "Webb", "Tucker", "Guzman", "Burns", "Crawford", "Olson", "Simpson", "Porter", "Hunter",
            "Gordon", "Mendez", "Silva", "Shaw", "Snyder", "Mason", "Dixon", "Munoz", "Hunt", "Hicks",
            "Holmes", "Palmer", "Wagner", "Black", "Robertson", "Boyd", "Rose", "Stone", "Salazar", "Fox",
            "Warren", "Mills", "Meyer", "Rice", "Schmidt", "Garza", "Daniels", "Ferguson", "Nichols", "Stephens",
            "Soto", "Weaver", "Ryan", "Gardner", "Payne", "Grant", "Dunn", "Kelley", "Spencer", "Hawkins",
            "Arnold", "Pierce", "Vargas", "Hansen", "Peters", "Santos", "Hart", "Bradley", "Knight", "Elliott",
            "Cunningham", "Duncan", "Armstrong", "Hudson", "Carroll", "Lane", "Riley", "Andrews", "Alvarado", "Ray",
            "Delgado", "Berry", "Perkins", "Hoffman", "Johnston", "Matthews", "Pena", "Richards", "Contreras", "Willis",
            "Carpenter", "Lawrence", "Sandoval", "Guerrero", "George", "Chapman", "Rios", "Estrada", "Ortega", "Watkins",
            "Greene", "Nunez", "Wheeler", "Valdez", "Harper", "Burke", "Larson", "Santiago", "Maldonado", "Morrison",
            "Franklin", "Carlson", "Austin", "Dominguez", "Carr", "Lawson", "Jacobs", "O'Brien", "Lynch", "Singh",
            "Vega", "Bishop", "Montgomery", "Oliver", "Jensen", "Harvey", "Williamson", "Gilbert", "Dean", "Sims",
            "Espinoza", "Howell", "Li", "Wong", "Reid", "Hanson", "Le", "McCoy", "Garrett", "Burton",
            "Fuller", "Wang", "Weber", "Welch", "Rojas", "Lucas", "Marquez", "Fields", "Park", "Yang",
            "Little", "Banks", "Padilla", "Day", "Walsh", "Bowman", "Schultz", "Luna", "Fowler", "Mejia",
            "Davidson", "Acosta", "Brewer", "May", "Holland", "Juarez", "Newman", "Pearson", "Curtis", "Cortez",
            "Douglas", "Schneider", "Joseph", "Barrett", "Navarro", "Figueroa", "Keller", "Avila", "Wade", "Molina",
            "Stanley", "Hopkins", "Campos", "Barnett", "Bates", "Chambers", "Caldwell", "Beck", "Lambert", "Miranda",
            "Byrd", "Craig", "Ayala", "Lowe", "Frazier", "Powers", "Neal", "Leonard", "Gregory", "Carrillo",
            "Sutton", "Fleming", "Rhodes", "Shelton", "Schwartz", "Norris", "Jennings", "Watts", "Duran", "Walters",
            "Cohen", "McDaniel", "Moran", "Parks", "Steele", "Vaughn", "Becker", "Holt", "DeLeon", "Barker",
            "Terry", "Hale", "Leon", "Hail", "Baldwin", "Jordan", "Cameron", "Bishop", "Summers", "Phelps",
            "McCarthy", "Salinas", "Ramsey", "Gentry", "Paul", "Parsons", "Ferrell", "Hodge", "Pace", "Sheppard",
            "Cherry", "Hayden", "Shaffer", "Mann", "Riddle", "Aletander", "Buck", "Clemons", "Blackwell", "McNeill",
    };

    public static void main(String[] args) {
        completeStartup();
    }

    public static void completeStartup() {
        NG5M ng5m = new NG5M();
        ng5m.preHook();

        initSrv();

        MinecraftServer.getCommandManager().register(new ConfirmCMD());

        ContinentalManagers.centralAIManager.registerEventManager(new ClicksAI(VotingWinner.ww2_clicks));
        ContinentalManagers.centralAIManager.registerEventManager(new TroopAI(VotingWinner.ww2_troops));

        createWW2VotingOption();

        BuildingEnum.university.setBuildType(new ResearchUniversity());
        BuildingEnum.researchLab.setBuildType(new ResearchLab());
        BuildingEnum.library.setBuildType(new ResearchLibrary());
        BuildingEnum.researchCenter.setBuildType(new ResearchCenter());
        BuildingEnum.factory.setBuildType(new Factory());
        BuildingEnum.barracks.setBuildType(new Barracks());

        createAdvancements();

        ContinentalManagers.cosmeticsManager.register(new StoreCategory("Something",
                Component.text("example", NamedTextColor.AQUA),
                itemBuilder(Material.LAPIS_BLOCK, Component.text("sm", NamedTextColor.AQUA)),
                new Hat("1", 1, Material.PURPLE_DYE, Component.text("item", NamedTextColor.AQUA), 1)
        ));

        ServerPropertiesFile spf = ContinentalManagers.configFileManager.getServerPropertiesFile();

        ContinentalManagers.database = new Database("server", spf.getDatabaseHost(), spf.getDatabasePort(), spf.getDatabaseUser(), spf.getDatabasePassword());
        ContinentalManagers.database.createTable(new Table.Create("player_info")
                .addColumn("uuid", DataTypeEum.STRING, true, true)
                .addColumn("data", DataTypeEum.BIGSTRING)
                .build());

        setupAll(new ArrayList<>(), ContinentalManagers.scoreboardManager);
        ng5m.hook();
    }

    private static void createAdvancements() {
        AdvancementManager advancementManager = ContinentalManagers.advancementManager;
        advancementManager.register(new AdvancementSection.Create("magic", Material.BROWN_DYE, FrameType.TASK, Component.text("WW2 inspired", NamedTextColor.WHITE), Component.text("The advancement tree for the ww2 inspired mode", NamedTextColor.GRAY))
                .addAdvancement(new Advancement("facs_built1", Material.OAK_BOAT, FrameType.GOAL, 1, 0, Component.text("10 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), null, 10.0f, "factoryBuilt"))
                .addAdvancement(new Advancement("facs_built2", Material.OAK_BOAT, FrameType.GOAL, 2, 0, Component.text("30 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "facs_built1", 30.0f, "factoryBuilt"))
                .addAdvancement(new Advancement("facs_built3", Material.OAK_BOAT, FrameType.GOAL, 3, 0, Component.text("50 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "facs_built2", 50.0f, "factoryBuilt"))
                .addAdvancement(new Advancement("facs_built4", Material.OAK_BOAT, FrameType.GOAL, 4, 0, Component.text("100 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "facs_build3", 100.0f, "factoryBuilt"))
                .addAdvancement(new Advancement("facs_built5", Material.OAK_BOAT, FrameType.GOAL, 5, 0, Component.text("1000 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "facs_build4", 1000.0f, "factoryBuilt"))
                //War
                .addAdvancement(new Advancement("conquer", Material.IRON_SWORD, FrameType.GOAL, 0, 2, Component.text("Conquer", NamedTextColor.GOLD), Component.text("Attack a country to complete", NamedTextColor.GRAY, TextDecoration.ITALIC), null, 1.0f, "captureBlock"))
                .addAdvancement(new Advancement("speedrun", Material.IRON_SWORD, FrameType.GOAL, 0, 3, Component.text("Speedrun", NamedTextColor.GOLD), Component.text("Conquer the world in one year", NamedTextColor.GRAY, TextDecoration.ITALIC), "conquer", 1.0f, "capturedWorldInOneYear"))
                .addAdvancement(new Advancement("nowarconquer", Material.IRON_SWORD, FrameType.GOAL, 1, 2, Component.text("Not a drop", NamedTextColor.GOLD), Component.text("Conquer the world without starting a war", NamedTextColor.GRAY, TextDecoration.ITALIC), "conquer", 1.0f, "nowarconq"))
                .addAdvancement(new Advancement("properconquest", Material.IRON_SWORD, FrameType.GOAL, -1, 2, Component.text("Victory in the face of foe", NamedTextColor.GOLD), Component.text("Conquer the world when there is 5 people online", NamedTextColor.GRAY, TextDecoration.ITALIC), "conquer", 1.0f, "conquerwhenothers"))
                //diplomacy
                .addAdvancement(new Advancement("diplomacy", Material.BOOK, FrameType.GOAL, 1, 3, Component.text("Diplomacy", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), null, 1.0f, "diplomacy"))
                .addAdvancement(new Advancement("factioncreation", Material.BOOK, FrameType.GOAL, 2, 3, Component.text("Created a faction", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "diplomacy", 1.0f, "factionCreate"))
                .addAdvancement(new Advancement("merger", Material.BOOK, FrameType.GOAL, 2, 3, Component.text("Merged into a country", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "diplomacy", 1.0f, "mergeCountries"))
                .addAdvancement(new Advancement("coop", Material.BOOK, FrameType.GOAL, 3, 2, Component.text("Win as a co-op", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "diplomacy", 1.0f, "coopwin"))
                //research
                .addAdvancement(new Advancement("research", Material.BROWN_DYE, FrameType.GOAL, 2, 5, Component.text("Research", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), null, 1.0f, "research"))
                .addAdvancement(new Advancement("allofthem", Material.BROWN_DYE, FrameType.GOAL, 3, 5, Component.text("ALL OF THEM?!", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "research", 1.0f, "allofthem"))
                .addAdvancement(new Advancement("gunmaster", Material.BROWN_DYE, FrameType.GOAL, 0, 5, Component.text("Research the whole gun tree", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "research", 1.0f, "guntreeall"))
                .build());
    }

    private static void createWW2VotingOption() {
        Modifier fascistModifier = ModifiersEnum.ww2_fascist.getModifier();
        Modifier centristModifier = ModifiersEnum.ww2_centrist.getModifier();
        Modifier anarchistModifier = ModifiersEnum.ww2_anarchist.getModifier();
        Modifier conservatistModifer = ModifiersEnum.ww2_conservatist.getModifier();
        Modifier socialistModifier = ModifiersEnum.ww2_socialist.getModifier();
        Modifier liberalModifier = ModifiersEnum.ww2_liberalist.getModifier();
        Modifier capitalistModifier = ModifiersEnum.ww2_capitalist.getModifier();
        Modifier imperialistModifier = ModifiersEnum.ww2_imperialist.getModifier();
        Modifier nationalistModifier = ModifiersEnum.ww2_nationalist.getModifier();


        IdeologiesEnum.ww2_fascist.setIdeologyTypes(new IdeologyTypes(TextColor.color(0, 0, 0), "F", "Fascist", getLeaders(fascistModifier, TextColor.color(0, 0, 0)), fascistModifier));
        IdeologiesEnum.ww2_neutral.setIdeologyTypes(new IdeologyTypes(TextColor.color(165, 157, 157), "N", "Centrist", getLeaders(centristModifier, TextColor.color(165, 157, 157)), centristModifier));
        IdeologiesEnum.ww2_anarchist.setIdeologyTypes(new IdeologyTypes(TextColor.color(7, 154, 12), "A", "Anarchism", getLeaders(anarchistModifier, TextColor.color(7, 154, 12)), anarchistModifier));
        IdeologiesEnum.ww2_conservatist.setIdeologyTypes(new IdeologyTypes(TextColor.color(204, 0, 0), "C", "Conservative", getLeaders(conservatistModifer, TextColor.color(204, 0, 0)), conservatistModifer));
        IdeologiesEnum.ww2_socialist.setIdeologyTypes(new IdeologyTypes(TextColor.color(255, 0, 0), "S", "Socialist", getLeaders(socialistModifier, TextColor.color(255, 0, 0)), socialistModifier));
        IdeologiesEnum.ww2_liberalist.setIdeologyTypes(new IdeologyTypes(TextColor.color(51, 253, 255), "L", "Liberalist", getLeaders(liberalModifier, TextColor.color(51, 253, 255)), liberalModifier));
        IdeologiesEnum.ww2_capitalist.setIdeologyTypes(new IdeologyTypes(TextColor.color(0, 153, 0), "C", "Capitalist", getLeaders(capitalistModifier, TextColor.color(0, 153, 0)), capitalistModifier));
        IdeologiesEnum.ww2_nationalist.setIdeologyTypes(new IdeologyTypes(TextColor.color(0, 0, 0), "N", "Nationalist", getLeaders(nationalistModifier, TextColor.color(255, 204, 0)), nationalistModifier));
        IdeologiesEnum.ww2_imperialist.setIdeologyTypes(new IdeologyTypes(TextColor.color(255, 204, 0), "I", "Imperialist", getLeaders(imperialistModifier, TextColor.color(255, 204, 0)), imperialistModifier));
        List<IdeologyTypes> ideologyTypesList = new ArrayList<>();

        ideologyTypesList.add(IdeologiesEnum.ww2_fascist.getIdeologyTypes());
        ideologyTypesList.add(IdeologiesEnum.ww2_anarchist.getIdeologyTypes());
        ideologyTypesList.add(IdeologiesEnum.ww2_conservatist.getIdeologyTypes());
        ideologyTypesList.add(IdeologiesEnum.ww2_liberalist.getIdeologyTypes());
        ideologyTypesList.add(IdeologiesEnum.ww2_capitalist.getIdeologyTypes());
        ideologyTypesList.add(IdeologiesEnum.ww2_nationalist.getIdeologyTypes());
        ideologyTypesList.add(IdeologiesEnum.ww2_imperialist.getIdeologyTypes());

        HashMap<CurrencyTypes, Currencies> c = new HashMap<>();
        CurrencyTypes production = CurrencyEnum.production.getCurrencyType();
        c.put(production, new Currencies(production, 1000.0f));

        Material capMaterial = Material.CYAN_DYE;
        Material effMaterial = Material.CYAN_DYE;
        Material antiBiotics = Material.CYAN_DYE;
        Material gunMaterial = Material.ORANGE_DYE;
        Material radMaterial = Material.SADDLE;
        Material planMaterial = Material.BROWN_DYE;

        VotingWinner.ww2_clicks.setVotingOption(VotingOption.create(1936, 1960, 1000L, "ww2_clicks")
                .mapGenerator(new MapGeneratorManager())
                .war(new ClickWarSystem())
                .countries(63)
                .defaultCurrencies(c)
                .ideologyTypes(ideologyTypesList)
                .tree(new TechTree.Create(Component.text("Tech", NamedTextColor.BLUE))
                        .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.factory_efficiency, Component.text("Increases production", NamedTextColor.DARK_PURPLE), Component.text("Factories efficiency", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                .addResearchOption(new ResearchOption.Create("ww2_eff1", itemBuilder(effMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.production, 0.1f)
                                                .build())
                                        .setName(Component.text("Streamline Production 1"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_eff2", itemBuilder(effMaterial), 1000.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.production, 0.1f)
                                                .build())
                                        .addRequires("ww2_eff1")
                                        .setName(Component.text("Streamline Production 2"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_eff3", itemBuilder(effMaterial), 2000.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.production, 0.1f)
                                                .build())
                                        .addRequires("ww2_eff2")
                                        .setName(Component.text("Streamline Production 3"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_eff4", itemBuilder(effMaterial), 3000.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.production, 0.1f)
                                                .build())
                                        .addRequires("ww2_eff3")
                                        .setName(Component.text("Streamline Production 4"))
                                        .build())
                                .build())
                        .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.factory_capacity, Component.text("Increases factory capacity", NamedTextColor.DARK_PURPLE), Component.text("Factory capacity", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                .addResearchOption(new ResearchOption.Create("ww2_cap1", itemBuilder(capMaterial, Component.text("Capacity increase", NamedTextColor.GOLD)), 20.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.buildingSlotBoost, 0.1f)
                                                .build())
                                        .setName(Component.text("Capacity increase 1"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_cap2", itemBuilder(capMaterial, Component.text("Capacity increase", NamedTextColor.GOLD)), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.buildingSlotBoost, 0.1f)
                                                .build())
                                        .addRequires("ww2_cap1")
                                        .setName(Component.text("Capacity increase 2"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_cap3", itemBuilder(capMaterial, Component.text("Capacity increase", NamedTextColor.GOLD)), 150.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.buildingSlotBoost, 0.1f)
                                                .build())
                                        .addRequires("ww2_cap2")
                                        .setName(Component.text("Capacity increase 3"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_cap4", itemBuilder(capMaterial, Component.text("Capacity increase", NamedTextColor.GOLD)), 200.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.buildingSlotBoost, 0.1f)
                                                .build())
                                        .addRequires("ww2_cap3")
                                        .setName(Component.text("Capacity increase 4"))
                                        .build())
                                .build())
                        .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.radar, Component.text("Radar tech tree increases coordination massively", NamedTextColor.DARK_PURPLE), Component.text("Radar", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                .addResearchOption(new ResearchOption.Create("ww2_radar1", itemBuilder(radMaterial), 30.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.planes, 0.1f)
                                                .build())
                                        .setName(Component.text("Radar"))
                                        .build())
                                .build())
                        .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.penicillin, Component.text("Antibiotics research means less people die", NamedTextColor.DARK_PURPLE), Component.text("Antibiotics", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                .addResearchOption(new ResearchOption.Create("ww2_antibiotics1", itemBuilder(antiBiotics), 30.0f)
                                        .setName(Component.text("Antibiotics"))
                                        .build())
                                .build())
                        .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.airoplane, Component.text("Airoplane research!", NamedTextColor.DARK_PURPLE), Component.text("Airoplane", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                .addResearchOption(new ResearchOption.Create("ww2_air1", itemBuilder(planMaterial), 30.0f)
                                        .setName(Component.text("Don't research this"))
                                        .build())
                                .build())
                        .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.guns, Component.text("Better guns", NamedTextColor.DARK_PURPLE), Component.text("GUNS", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                .addResearchOption(new ResearchOption.Create("ww2_guns1", itemBuilder(gunMaterial), 30.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunAccuracy, 0.3f)
                                                .build())
                                        .setName(Component.text("Enhanced accuracy"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_guns2", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, -0.1f)
                                                .build())
                                        .addRequires("ww2_guns1")
                                        .setName(Component.text("Streamlined production"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_guns3", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, -0.1f)
                                                .build())
                                        .addRequires("ww2_guns2")
                                        .setName(Component.text("Streamlined production 2"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_semi_auto", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, 0.1f)
                                                .build())
                                        .addRequires("ww2_guns3")
                                        .setName(Component.text("Research semi automatic weapons"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_semi_gas", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, 0.1f)
                                                .addBoost(BoostEnum.gunAccuracy, 0.08f)
                                                .build())
                                        .addRequires("ww2_semi_auto")
                                        .setName(Component.text("Make the semi automatic weapons gas operated"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_semi_bigger_magazine", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, 0.1f)
                                                .addBoost(BoostEnum.gunAccuracy, 0.08f)
                                                .build())
                                        .addRequires("ww2_semi_gas")
                                        .setName(Component.text("Develop bigger magazines"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_submachine", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, 0.1f)
                                                .build())
                                        .addRequires("ww2_guns3")
                                        .setName(Component.text("Create a submachine gun"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_folding_stacks", itemBuilder(gunMaterial), 200.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, 0.1f)
                                                .addBoost(BoostEnum.gunAccuracy, 0.1f)
                                                .build())
                                        .addRequires("ww2_submachine")
                                        .setName(Component.text("Create folding stacks"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_full_auto", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, 0.1f)
                                                .addBoost(BoostEnum.gunAccuracy, 0.1f)
                                                .build())
                                        .addRequires("ww2_guns3")
                                        .setName(Component.text("Begin fully automatic guns development"))
                                        .build())
                                .addResearchOption(new ResearchOption.Create("ww2_cooling", itemBuilder(gunMaterial), 100.0f)
                                        .setModifier(new Modifier.create(null)
                                                .addBoost(BoostEnum.gunCost, 0.1f)
                                                .addBoost(BoostEnum.gunAccuracy, 0.05f)
                                                .build())
                                        .addRequires("ww2_full_auto")
                                        .setName(Component.text("Better cooling"))
                                        .build())
                                .build())
                        .build())
                .defaultInventory(InventoryEnum.defaultInv)
                .build());

        VotingWinner.ww2_troops.setVotingOption(VotingOption.create(1936, 1960, 1000L, "ww2_troops")
                .mapGenerator(new MapGeneratorManager())
                .war(new TroopWarSystem())
                .countries(63)
                .defaultCurrencies(c)
                .ideologyTypes(ideologyTypesList)
                .defaultInventory(InventoryEnum.troops_default)
                .build());
    }

    private static List<Leader> getLeaders(Modifier modifier, TextColor color) {
        List<Leader> leaders = new ArrayList<>();
        for (int i = 0; 100 > i; i++) {
            leaders.add(new Leader.create(Component.text(getName(), color)).addModifier(modifier).build());
        }
        return leaders;
    }

    private static String getName() {
        return firstName[new Random().nextInt(0, firstName.length)] + ' ' + lastName[new Random().nextInt(0, lastName.length)];
    }
}