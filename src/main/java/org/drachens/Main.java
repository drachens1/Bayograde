package org.drachens;

import dev.ng5m.CPlayer;
import dev.ng5m.Constants;
import dev.ng5m.Util;
import dev.ng5m.events.EventHandlerProviderManager;
import dev.ng5m.greet.GreetEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.BuildingTypes;
import org.drachens.Manager.defaults.defaultsStorer.Elections;
import org.drachens.Manager.defaults.defaultsStorer.Ideologies;
import org.drachens.Manager.defaults.defaultsStorer.Modifiers;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.VotingWinner;
import org.drachens.advancement.Advancement;
import org.drachens.advancement.AdvancementManager;
import org.drachens.advancement.AdvancementSection;
import org.drachens.dataClasses.*;
import org.drachens.dataClasses.Countries.ElectionTypes;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Research.ResearchCategoryEnum;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.Research.tree.ResearchCategory;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.drachens.fileManagement.databases.DataTypeEum;
import org.drachens.fileManagement.databases.Database;
import org.drachens.fileManagement.databases.Table;
import org.drachens.interfaces.inventories.BuildItem;
import org.drachens.interfaces.inventories.ChangeInventoryButton;
import org.drachens.interfaces.items.HotbarItemButton;
import org.drachens.store.StoreCategory;
import org.drachens.store.items.Hat;
import org.drachens.temporary.*;
import org.drachens.temporary.demand.DemandInventory;
import org.drachens.temporary.inventories.ExitItem;
import org.drachens.temporary.research.ResearchLab;
import org.drachens.temporary.research.ResearchLibrary;
import org.drachens.temporary.research.ResearchUniversity;
import org.drachens.temporary.scoreboards.items.ShowDiplomacy;
import org.drachens.temporary.scoreboards.items.ShowEconomy;
import org.drachens.temporary.scoreboards.items.ShowGeneralInfo;
import org.drachens.temporary.scoreboards.items.ShowIdeology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.KyoriUtil.setupPrefixes;
import static org.drachens.util.ServerUtil.initSrv;
import static org.drachens.util.ServerUtil.setupAll;

public class Main {
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
        setupPrefixes();
        initSrv();
        createWW2VotingOption();

        new Factory();

        createAdvancements();

        EventHandlerProviderManager.hook();
        ContinentalManagers.inventoryManager.registerInventory(InventoryEnum.defaultInv, new NoneCustomisableInventory(new HotbarItemButton[]{new BuildItem(10, itemBuilder(Material.CYAN_DYE, 10),BuildingEnum.factory), new TroopMover(),new ChangeInventoryButton(0,itemBuilder(Material.BOOK),InventoryEnum.scoreboardInv),new ChangeInventoryButton(1,itemBuilder(Material.BROWN_DYE),InventoryEnum.research)}));

        ContinentalManagers.inventoryManager.registerInventory(InventoryEnum.scoreboardInv, new NoneCustomisableInventory(new HotbarItemButton[]{new ShowDiplomacy(),new ShowEconomy(),new ShowIdeology(),new ShowGeneralInfo(),new ExitItem()}));

        ContinentalManagers.inventoryManager.registerInventory(InventoryEnum.research,new NoneCustomisableInventory(new HotbarItemButton[]{new BuildItem(1,itemBuilder(Material.BROWN_DYE,1), BuildingEnum.library),new BuildItem(2,itemBuilder(Material.BROWN_DYE,2),BuildingEnum.university),new BuildItem(3,itemBuilder(Material.BROWN_DYE,3),BuildingEnum.researchCenter),new BuildItem(4,itemBuilder(Material.BROWN_DYE,4),BuildingEnum.researchLab),new ExitItem()}));

        ContinentalManagers.cosmeticsManager.register(new StoreCategory("Something",
                Component.text("example", NamedTextColor.AQUA),
                itemBuilder(Material.LAPIS_BLOCK, Component.text("sm", NamedTextColor.AQUA)),
                new Hat("1", 1, Material.PURPLE_DYE, Component.text("item", NamedTextColor.AQUA), 1)
        ));

        setupAll(new ArrayList<>(), ContinentalManagers.scoreboardManager);

        ServerPropertiesFile spf = ContinentalManagers.configFileManager.getServerPropertiesFile();

        ContinentalManagers.database = new Database("server",spf.getDatabaseHost(),spf.getPort(),spf.getDatabaseUser(),spf.getDatabasePassword());
        ContinentalManagers.database.createTable(new Table.Create("player_info")
                .addColumn("uuid", DataTypeEum.STRING,true,true)
                .addColumn("name", DataTypeEum.STRING)
                .addColumn("last_online", DataTypeEum.STRING)
                .addColumn("first_joined", DataTypeEum.STRING)
                .addColumn("playtime", DataTypeEum.LONG)
                .addColumn("gold", DataTypeEum.INTEGER)
                .addColumn("permissions", DataTypeEum.STRING)
                .addColumn("cosmetics", DataTypeEum.STRING)
                .addColumn("achievements", DataTypeEum.STRING)
                .build());
    }

    private static void createAdvancements(){
        AdvancementManager advancementManager = ContinentalManagers.advancementManager;
        advancementManager.register(new AdvancementSection.Create("magic",Material.BROWN_DYE, FrameType.TASK,Component.text("WW2 inspired",NamedTextColor.GOLD),Component.text("The advancement tree for the ww2 inspired mode",NamedTextColor.GRAY))
                        .addAdvancement(new Advancement("facs_built1", Material.OAK_BOAT, FrameType.GOAL, new int[]{1, 0}, Component.text("10 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), null, 10f, "factoryBuilt"))
                        .addAdvancement(new Advancement("facs_built2", Material.OAK_BOAT, FrameType.GOAL, new int[]{2, 0}, Component.text("30 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "facs_built1", 30f,"factoryBuilt"))
                        .addAdvancement(new Advancement("facs_built3", Material.OAK_BOAT, FrameType.GOAL, new int[]{3, 0}, Component.text("50 Factories built", NamedTextColor.GOLD), Component.text("", NamedTextColor.GRAY, TextDecoration.ITALIC), "facs_built2",50f,"factoryBuilt"))

                .build());
    }

    private static void createWW2VotingOption() {
        BuildingTypes buildingTypes = ContinentalManagers.defaultsStorer.buildingTypes;
        buildingTypes.register(new ResearchUniversity());
        buildingTypes.register(new ResearchLibrary());
        buildingTypes.register(new ResearchLab());
        buildingTypes.register(new ResearchCenter());

        Modifier exampleModifier = new Modifier.create(Component.text("Example", NamedTextColor.GOLD))
                .setDescription(Component.text("description", NamedTextColor.BLUE))
                .addBoost(BoostEnum.production,0.1f)
                .addBoost(BoostEnum.capitulation,2f)
                .addBoost(BoostEnum.stabilityBase,3f)
                .addBoost(BoostEnum.stabilityGain,4f)
                .addBoost(BoostEnum.relations,6f)
                .addBoost(BoostEnum.buildingSlotBoost,2f)
                .build();

        Modifier fascistModifier = new Modifier.create(Component.text("War", TextColor.color(204, 0, 0), TextDecoration.BOLD))
                .addBoost(BoostEnum.production,0.1f)
                .addBoost(BoostEnum.stabilityBase,10f)
                .addBoost(BoostEnum.stabilityGain,-0.1f)
                .addBoost(BoostEnum.capitulation,5f)
                .build();

        Modifier centristModifier = new Modifier.create(Component.text("Centrist", TextColor.color(96, 96, 96), TextDecoration.BOLD))
                .addBoost(BoostEnum.stabilityBase,50f)
                .addBoost(BoostEnum.capitulation,-5f)
                .addBoost(BoostEnum.production,0.2f)
                .build();

        Modifier anarchistModifier = new Modifier.create(Component.text("Anarchist", TextColor.color(7, 154, 12)))
                .addBoost(BoostEnum.stabilityBase,-100f)
                .addBoost(BoostEnum.stabilityGain,-5f)
                .addBoost(BoostEnum.capitulation,0.5f)
                .addBoost(BoostEnum.production,5f)
                .build();

        Modifier conservatistModifer = new Modifier.create(Component.text("Conservatism", TextColor.color(204, 0, 0)))
                .addBoost(BoostEnum.stabilityBase,1f)
                .build();

        Modifier socialistModifier = new Modifier.create(Component.text("Socialist", TextColor.color(255, 0, 0)))
                .addBoost(BoostEnum.stabilityBase,40f)
                .addBoost(BoostEnum.stabilityGain,0.2f)
                .addBoost(BoostEnum.production,0.2f)
                .build();

        Modifier liberalModifier = new Modifier.create(Component.text("Liberalist", TextColor.color(51, 253, 255)))
                .build();

        Modifier capitalistModifier = new Modifier.create(Component.text("Capitalist", TextColor.color(0, 153, 0)))
                .addBoost(BoostEnum.stabilityBase,-10f)
                .addBoost(BoostEnum.production,0.5f)
                .build();

        String votingName = "ww2-";
        Modifiers modifiers = ContinentalManagers.defaultsStorer.modifier;//Name system = votingName-modifier

        modifiers.register(fascistModifier, votingName + "fascistModifier");
        modifiers.register(centristModifier, votingName + "centristModifier");
        modifiers.register(anarchistModifier, votingName + "anarchistModifier");
        modifiers.register(conservatistModifer, votingName + "conservatistModifier");
        modifiers.register(socialistModifier, votingName + "socialistModifier");
        modifiers.register(liberalModifier, votingName + "liberalModifier");
        modifiers.register(capitalistModifier, votingName + "capitalistModifier");
        modifiers.register(exampleModifier, votingName + "example");

        IdeologyTypes fascist = new IdeologyTypes(TextColor.color(0, 0, 0), "F", "Fascist", getLeaders(fascistModifier,TextColor.color(0, 0, 0)), fascistModifier);
        IdeologyTypes neutral = new IdeologyTypes(TextColor.color(165, 157, 157), "N", "Centrist", getLeaders(centristModifier,TextColor.color(165, 157, 157)), centristModifier);
        IdeologyTypes anarchist = new IdeologyTypes(TextColor.color(7, 154, 12), "A", "Anarchism", getLeaders(anarchistModifier,TextColor.color(7, 154, 12)), anarchistModifier);
        IdeologyTypes conservatism = new IdeologyTypes(TextColor.color(204, 0, 0), "C", "Conservative", getLeaders(conservatistModifer,TextColor.color(204, 0, 0)), conservatistModifer);
        IdeologyTypes socialist = new IdeologyTypes(TextColor.color(255, 0, 0), "S", "Socialist", getLeaders(socialistModifier,TextColor.color(255, 0, 0)), socialistModifier);
        IdeologyTypes liberalist = new IdeologyTypes(TextColor.color(51, 253, 255), "L", "Liberalist", getLeaders(liberalModifier,TextColor.color(51, 253, 255)), liberalModifier);
        IdeologyTypes capitalist = new IdeologyTypes(TextColor.color(0, 153, 0), "C", "Capitalist", getLeaders(capitalistModifier,TextColor.color(0, 153, 0)), capitalistModifier);

        Ideologies ideologies = ContinentalManagers.defaultsStorer.ideologies;

        ideologies.register(fascist);
        ideologies.register(neutral);
        ideologies.register(anarchist);
        ideologies.register(conservatism);
        ideologies.register(socialist);
        ideologies.register(liberalist);
        ideologies.register(capitalist);

        List<IdeologyTypes> ideologyTypesList = new ArrayList<>();

        ideologyTypesList.add(fascist);
        ideologyTypesList.add(anarchist);
        ideologyTypesList.add(conservatism);
        ideologyTypesList.add(liberalist);
        ideologyTypesList.add(capitalist);

        ElectionTypes democratic = new ElectionTypes(TextColor.color(0, 0, 255), "Democratic");
        ElectionTypes authorotarian = new ElectionTypes(TextColor.color(64, 64, 64), "Authoritarian");
        ElectionTypes totalitarian = new ElectionTypes(TextColor.color(0, 0, 0), "Totalitarian");
        ElectionTypes republic = new ElectionTypes(TextColor.color(0, 102, 204), "Republic");

        Elections elections = ContinentalManagers.defaultsStorer.elections;

        elections.register(democratic);
        elections.register(authorotarian);
        elections.register(totalitarian);
        elections.register(republic);

        List<ElectionTypes> electionTypes = new ArrayList<>();

        electionTypes.add(democratic);
        electionTypes.add(authorotarian);
        electionTypes.add(totalitarian);
        electionTypes.add(republic);

        HashMap<CurrencyTypes, Currencies> c = new HashMap<>();
        CurrencyTypes production = CurrencyEnum.production.getCurrencyType();
        c.put(production, new Currencies(production, 10f));

        Modifier superPower = new Modifier.create(Component.text("Super Power", NamedTextColor.GOLD, TextDecoration.BOLD))
                .addBoost(BoostEnum.stabilityBase,10f)
                .addBoost(BoostEnum.production,0.3f)
                .setDescription(Component.text()
                        .append(Component.text("The nation is the only super power meaning they are by far the strongest at the start.", TextColor.color(128, 128, 128), TextDecoration.ITALIC))
                        .build())
                .build();

        Modifier major = new Modifier.create(Component.text("Major", NamedTextColor.GOLD, TextDecoration.BOLD))
                .addBoost(BoostEnum.stabilityBase,5f)
                .addBoost(BoostEnum.production,0.1f)
                .setDescription(Component.text()
                        .append(Component.text("This nation is a major power its below super power but above minor.", TextColor.color(128, 128, 128), TextDecoration.ITALIC))
                        .build())
                .build();

        Modifier minor = new Modifier.create(Component.text("Minor", NamedTextColor.GOLD, TextDecoration.BOLD))
                .setDescription(Component.text()
                        .append(Component.text("Just a wee lil nation.", TextColor.color(128, 128, 128), TextDecoration.ITALIC))
                        .build())
                .build();

        modifiers.register(superPower, votingName + "superpower");
        modifiers.register(major, votingName + "major");
        modifiers.register(minor, votingName + "minor");

        Material capMaterial = Material.CYAN_DYE;
        Material effMaterial = Material.CYAN_DYE;
        Material antiBiotics = Material.CYAN_DYE;
        Material gunMaterial = Material.ORANGE_DYE;
        Material radMaterial = Material.SADDLE;
        Material planMaterial = Material.BROWN_DYE;

        ContinentalManagers.defaultsStorer.voting.register(new VotingOption.create(1936, 1937, 1000L, "ww2_clicks")
                .setMapGenerator(new MapGeneratorManager())
                .setWar(new ClickWarSystem())
                .setCountries(90)
                .setDefaultCurrencies(c)
                .setIdeologyTypes(ideologyTypesList)
                .setElections(electionTypes)
                        .setTechTree(new TechTree.Create(Component.text("Tech",NamedTextColor.BLUE))
                                .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.factory_efficiency, Component.text("Increases production",NamedTextColor.DARK_PURPLE), Component.text("Factories efficiency", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                        .addResearchOption(new ResearchOption.Create("ww2_eff1",itemBuilder(effMaterial),100f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Streamlines production"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.production,0.1f)
                                                        .build())
                                                .setComparedToLast(0, 1)
                                        .build())
                                        .addResearchOption(new ResearchOption.Create("ww2_eff2",itemBuilder(effMaterial),1000f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Streamlines production"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.production,0.1f)
                                                        .build())
                                                .addRequires("ww2_eff1")
                                                .setComparedToLast(0, 1)
                                        .build())
                                        .addResearchOption(new ResearchOption.Create("ww2_eff3",itemBuilder(effMaterial),2000f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Streamlines production"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.production,0.1f)
                                                        .build())
                                                .addRequires("ww2_eff2")
                                                .setComparedToLast(0, 1)
                                        .build())
                                        .addResearchOption(new ResearchOption.Create("ww2_eff4",itemBuilder(effMaterial),3000f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Streamlines production"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.production,0.1f)
                                                        .build())
                                                .addRequires("ww2_eff3")
                                                .setComparedToLast(0, 1)
                                        .build())
                                .build())
                                .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.factory_capacity, Component.text("Increases factory capacity",NamedTextColor.DARK_PURPLE), Component.text("Factory capacity", NamedTextColor.GRAY, TextDecoration.ITALIC))
                                        .addResearchOption(new ResearchOption.Create("ww2_cap1",itemBuilder(capMaterial,Component.text("Capacity increase", NamedTextColor.GOLD)),20f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Increases the max number of factories by 10%"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.buildingSlotBoost,0.1f)
                                                        .build())
                                        .build())
                                        .addResearchOption(new ResearchOption.Create("ww2_cap2",itemBuilder(capMaterial,Component.text("Capacity increase", NamedTextColor.GOLD)),100f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Increases the max number of factories by 10%"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.buildingSlotBoost,0.1f)
                                                        .build())
                                                .addRequires("ww2_cap1")
                                                .setComparedToLast(0, 1)
                                        .build())
                                        .addResearchOption(new ResearchOption.Create("ww2_cap3",itemBuilder(capMaterial,Component.text("Capacity increase", NamedTextColor.GOLD)),150f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Increases the max number of factories by 10%"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.buildingSlotBoost,0.1f)
                                                        .build())
                                                .addRequires("ww2_cap2")
                                                .setComparedToLast(0, 1)
                                        .build())
                                        .addResearchOption(new ResearchOption.Create("ww2_cap4",itemBuilder(capMaterial,Component.text("Capacity increase", NamedTextColor.GOLD)),200f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text()
                                                                .append(Component.text("Increases the max number of factories by 10%"))
                                                                .build())
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.buildingSlotBoost,0.1f)
                                                        .build())
                                                .addRequires("ww2_cap3")
                                                .setComparedToLast(0, 1)
                                        .build())
                                .build())
                                .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.radar, Component.text("Radar tech tree increases coordination massively",NamedTextColor.DARK_PURPLE),Component.text("Radar", NamedTextColor.GRAY,TextDecoration.ITALIC))
                                        .addResearchOption(new ResearchOption.Create("ww2_radar1", itemBuilder(radMaterial), 30f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text("Increases fighter attack by 10% and troops damage by 5%", NamedTextColor.GRAY))
                                                        .build())//todo Create an airport and plane system that can then have boosts 
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.planes, 0.1f)
                                                        .build())
                                        .build())
                                .build())
                                .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.penicillin, Component.text("Antibiotics research means less people die", NamedTextColor.DARK_PURPLE), Component.text("Antibiotics", NamedTextColor.GRAY,TextDecoration.ITALIC))
                                        .addResearchOption(new ResearchOption.Create("ww2_antibiotics1", itemBuilder(antiBiotics), 30f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text("10% less people die! - eventually", NamedTextColor.AQUA))//todo add MANPOWER
                                                        .build())
                                        .build())
                                .build())
                                .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.airoplane, Component.text("Airoplane research!", NamedTextColor.DARK_PURPLE),Component.text("Airoplane", NamedTextColor.GRAY,TextDecoration.ITALIC))
                                        .addResearchOption(new ResearchOption.Create("ww2_air1", itemBuilder(planMaterial), 30f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text("Airoplanes are good at murdering people", NamedTextColor.AQUA))
                                                        .build())
                                        .build())
                                .build())
                                .addCategory(new ResearchCategory.Create(ResearchCategoryEnum.guns, Component.text("Better guns", NamedTextColor.DARK_PURPLE), Component.text("GUNS", NamedTextColor.GRAY,TextDecoration.ITALIC))
                                        .addResearchOption(new ResearchOption.Create("ww2_guns1",itemBuilder(gunMaterial),30f)
                                                .setDescription(new ComponentListBuilder()
                                                        .addComponent(Component.text("something", NamedTextColor.AQUA))
                                                        .build())
                                                .setModifier(new Modifier.create(null)
                                                        .addBoost(BoostEnum.gunAccuracy, 30f)
                                                .build())
                                        .build())
                                        .addResearchOption(new ResearchOption.Create(votingName, itemBuilder(gunMaterial), 0)
                                        .build())
                                .build())
                        .build())
                .build(), VotingWinner.ww2_clicks);

        ContinentalManagers.defaultsStorer.voting.register(new VotingOption.create(1936, 1937, 1000L, "ww2_troops")
                .setMapGenerator(new MapGeneratorManager())
                .setWar(new TroopWarSystem())
                .setCountries(90)
                .setDefaultCurrencies(c)
                .setIdeologyTypes(ideologyTypesList)
                .setElections(electionTypes)
                .build(), VotingWinner.ww2_troops);

        ContinentalManagers.inventoryManager.registerInventory(InventoryEnum.demand, new DemandInventory());
    }

    private static List<Leader> getLeaders(Modifier modifier, TextColor color) {
        List<Leader> leaders = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            leaders.add(new Leader.create(Component.text(getName(), color)).addModifier(modifier).build());
        }
        return leaders;
    }

    private static String getName() {
        return firstName[new Random().nextInt(0, firstName.length)] + " " + lastName[new Random().nextInt(0, lastName.length)];
    }

    public static void initHooks() {
        MinecraftServer.getConnectionManager().setPlayerProvider(CPlayer::new);

        new GreetEvents().hook(
                new GreetEvents.GreetSettings(
                        event ->
                                Util.colored("[", Constants.Colors.LIGHT_GRAY).append(
                                        Util.colored("+", Constants.Colors.LIME).append(
                                                Util.colored("] " + event.getPlayer().getUsername(), Constants.Colors.LIGHT_GRAY)
                                        )
                                ),
                        event ->
                                Util.colored("[", Constants.Colors.LIGHT_GRAY).append(
                                        Util.colored("-", Constants.Colors.RED).append(
                                                Util.colored("]" + event.getPlayer().getUsername(), Constants.Colors.LIGHT_GRAY)
                                        )
                                ))
        );

    }
}

