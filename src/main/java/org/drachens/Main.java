package org.drachens;

import dev.ng5m.Constants;
import dev.ng5m.Util;
import dev.ng5m.events.EventHandlerProviderManager;
import dev.ng5m.greet.GreetEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.Elections;
import org.drachens.Manager.defaults.defaultsStorer.Ideologies;
import org.drachens.Manager.defaults.defaultsStorer.Modifiers;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.Manager.scoreboards.temp.DefaultScoreboard;
import org.drachens.dataClasses.Countries.ElectionTypes;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyBoost;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.HotbarInventory;
import org.drachens.dataClasses.Modifier;
import org.drachens.interfaces.Voting.VotingOption;
import org.drachens.interfaces.items.HotbarItemButton;
import org.drachens.temporary.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.drachens.util.KyoriUtil.compBuild;
import static org.drachens.util.ServerUtil.initSrv;
import static org.drachens.util.ServerUtil.setupAll;

public class Main {
    public static void main(String[] args) {
        completeStartup();
    }
    public static void completeStartup(){
        initSrv();

        CurrencyTypes production = new CurrencyTypes(compBuild("production", TextColor.color(255, 165, 0)),compBuild("\uD83D\uDC35",TextColor.color(255, 165, 0)),"production");

        ContinentalManagers.defaultsStorer.currencies.register(production);

        createWW2VotingOption();


        HashMap<String, ContinentalScoreboards> continentalScoreboards = new HashMap<>();
        ScoreboardManager scoreboardManager = new ScoreboardManager();
        continentalScoreboards.put("default",new DefaultScoreboard(scoreboardManager));
        scoreboardManager.setScoreboards(continentalScoreboards);

        new Factory();

        EventHandlerProviderManager.hook();
        ContinentalManagers.defaultsStorer.currencies.register(production);
        HotbarItemButton[] items = {new BuildItem(), new BuildItem2(), new TroopMover()};
        ContinentalManagers.inventoryManager.registerInventory("default",new HotbarInventory(items));

        setupAll(new ArrayList<>(),scoreboardManager);
    }

    private static void createWW2VotingOption(){
        CurrencyTypes production = ContinentalManagers.defaultsStorer.currencies.getCurrencyType("production");

        Modifier exampleModifier = new Modifier.create(compBuild("Example",NamedTextColor.GOLD))
                .setDescription(compBuild("description",NamedTextColor.BLUE))
                .addCurrencyBoost(new CurrencyBoost(production,10f))
                .addMaxBoost(1f)
                .addCapitulationBoostPercentage(2f)
                .addStabilityBaseBoost(3f)
                .addStabilityGainBoost(4f)
                .setRelationsBoost(5f)
                .setBaseRelationsBoost(6f)
                .setProductionBoost(7f)
                .setMaxBuildingSlotBoost(2f)
                .build();

        Modifier fascistModifier = new Modifier.create(compBuild("War", TextColor.color(204,0,0), TextDecoration.BOLD))
                .addCurrencyBoost(new CurrencyBoost(production,0.1f))
                .addStabilityBaseBoost(10f)
                .addStabilityGainBoost(-0.1f)
                .addCapitulationBoostPercentage(5f)
                .setTextColour(204,0,0)
                .build();

        Modifier centristModifier = new Modifier.create(compBuild("Centrist",TextColor.color(96,96,96),TextDecoration.BOLD))
                .addStabilityBaseBoost(50f)
                .addCapitulationBoostPercentage(-5f)
                .addCurrencyBoost(new CurrencyBoost(production,0.2f))
                .setTextColour(96,96,96)
                .build();

        Modifier anarchistModifier = new Modifier.create(compBuild("Anarchist",TextColor.color(7,154,12)))
                .addStabilityBaseBoost(-100f)
                .addStabilityGainBoost(-5f)
                .addCapitulationBoostPercentage(0.5f)
                .addCurrencyBoost(new CurrencyBoost(production,5f))
                .setTextColour(7,154,12)
                .build();

        Modifier conservatistModifer = new Modifier.create(compBuild("Conservatism",TextColor.color(204,0,0)))
                .addStabilityBaseBoost(1)
                .setTextColour(204,0,0)
                .build();

        Modifier socialistModifier = new Modifier.create(compBuild("Socialist",TextColor.color(255,0,0)))
                .addStabilityGainBoost(0.2f)
                .addStabilityBaseBoost(40f)
                .addCurrencyBoost(new CurrencyBoost(production,0.2f))
                .setTextColour(255,0,0)
                .build();

        Modifier liberalModifier = new Modifier.create(compBuild("Liberalist",TextColor.color(51,253,255)))
                .setTextColour(51,253,255)
                .build();

        Modifier capitalistModifier = new Modifier.create(compBuild("Capitalist",TextColor.color(0,153,0)))
                .addStabilityBaseBoost(-10f)
                .addCurrencyBoost(new CurrencyBoost(production,0.5f))
                .setTextColour(0,153,0)
                .build();

        String votingName = "ww2-";
        Modifiers modifiers = ContinentalManagers.defaultsStorer.modifier;//Name system = votingName-modifier

        modifiers.register(fascistModifier,votingName+"fascistModifier");
        modifiers.register(centristModifier,votingName+"centristModifier");
        modifiers.register(anarchistModifier,votingName+"anarchistModifier");
        modifiers.register(conservatistModifer,votingName+"conservatistModifier");
        modifiers.register(socialistModifier,votingName+"socialistModifier");
        modifiers.register(liberalModifier,votingName+"liberalModifier");
        modifiers.register(capitalistModifier,votingName+"capitalistModifier");
        modifiers.register(exampleModifier,votingName+"example");

        IdeologyTypes fascist = new IdeologyTypes(TextColor.color(0,0,0),"F","Fascist",getLeaders(fascistModifier),fascistModifier);
        IdeologyTypes neutral = new IdeologyTypes(TextColor.color(165,157,157),"N","Centrist", getLeaders(centristModifier),centristModifier);
        IdeologyTypes anarchist = new IdeologyTypes(TextColor.color(7,154,12),"A","Anarchism",getLeaders(anarchistModifier),anarchistModifier);
        IdeologyTypes conservatism = new IdeologyTypes(TextColor.color(204,0,0),"C","Conservative",getLeaders(conservatistModifer),conservatistModifer);
        IdeologyTypes socialist = new IdeologyTypes(TextColor.color(255,0,0),"S","Socialist",getLeaders(socialistModifier),socialistModifier);
        IdeologyTypes liberalist = new IdeologyTypes(TextColor.color(51,253,255),"L","Liberalist", getLeaders(liberalModifier),liberalModifier);
        IdeologyTypes capitalist = new IdeologyTypes(TextColor.color(0,153,0),"C","Capitalist", getLeaders(capitalistModifier),capitalistModifier);

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

        ElectionTypes democratic = new ElectionTypes(TextColor.color(0,0,255),"Democratic");
        ElectionTypes authorotarian = new ElectionTypes(TextColor.color(64,64,64),"Authoritarian");
        ElectionTypes totalitarian = new ElectionTypes(TextColor.color(0,0,0),"Totalitarian");
        ElectionTypes republic = new ElectionTypes(TextColor.color(0,102,204),"Republic");

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
        c.put(production,new Currencies(production,10f));

        Modifier superPower = new Modifier.create(compBuild("Super Power",NamedTextColor.GOLD,TextDecoration.BOLD))
                .addStabilityBaseBoost(10f)
                .addCurrencyBoost(new CurrencyBoost(production,0.3f))
                .setDescription(Component.text()
                        .append(Component.text("The nation is the only super power meaning they are by far the strongest at the start.",TextColor.color(128,128,128),TextDecoration.ITALIC))
                        .build())
                .build();

        Modifier major = new Modifier.create(compBuild("Major",NamedTextColor.GOLD,TextDecoration.BOLD))
                .addStabilityBaseBoost(5f)
                .addCurrencyBoost(new CurrencyBoost(production,0.1f))
                .setDescription(Component.text()
                        .append(Component.text("This nation is a major power its below super power but above minor.",TextColor.color(128,128,128),TextDecoration.ITALIC))
                        .build())
                .build();

        Modifier minor = new Modifier.create(compBuild("Minor",NamedTextColor.GOLD,TextDecoration.BOLD))
                .setDescription(Component.text()
                        .append(Component.text("Just a wee lil nation.",TextColor.color(128,128,128),TextDecoration.ITALIC))
                        .build())
                .build();

        modifiers.register(superPower,votingName+"superpower");
        modifiers.register(major,votingName+"major");
        modifiers.register(minor,votingName+"minor");

        System.out.println("voting");
        ContinentalManagers.defaultsStorer.voting.register(new VotingOption.create(1936, 1937, 1000L,"ww2_clicks")
                .setMapGenerator(new MapGeneratorManager())
                .setWar(new ClickWarSystem())
                .setCountries(90)
                .setDefaultCurrencies(c)
                .setIdeologyTypes(ideologyTypesList)
                .setElections(electionTypes)
                .build(),"ww2_clicks");

        ContinentalManagers.defaultsStorer.voting.register(new VotingOption.create(1936, 1937, 1000L,"ww2_troops")
                .setMapGenerator(new MapGeneratorManager())
                .setWar(new TroopWarSystem())
                .setCountries(90)
                .setDefaultCurrencies(c)
                .setIdeologyTypes(ideologyTypesList)
                .setElections(electionTypes)
                .build(),"ww2_troops");
    }

    private static List<Leader> getLeaders(Modifier modifier){
        List<Leader> leaders = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            leaders.add(new Leader.create(compBuild(getName(),modifier.getTextColor())).addModifier(modifier).build());
        }
        return leaders;
    }
    private static String getName(){
        return firstName[new Random().nextInt(0,firstName.length)]+" "+lastName[new Random().nextInt(0,lastName.length)];
    }

    private static String[] firstName = {
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
            "Billy", "Julia", "Bruce", "Judy", "Albert", "Grace", "Willie", "Denise", "Gabriel", "Amber",
            "Logan", "Marilyn", "Alan", "Beverly", "Juan", "Danielle", "Wayne", "Theresa", "Roy", "Sophia",
            "Ralph", "Marie", "Randy", "Diana", "Eugene", "Brittany", "Vincent", "Natalie", "Russell", "Isabella",
            "Elijah", "Charlotte", "Louis", "Rose", "Bobby", "Alexis", "Philip", "Kayla", "Johnny", "Lillian",
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
    private static String[] lastName = {
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
            "Cherry", "Hayden", "Shaffer", "Mann", "Riddle", "Alexander", "Buck", "Clemons", "Blackwell", "McNeill",
    };

    public static void initHooks() {
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
                                        Util.colored("]", Constants.Colors.LIGHT_GRAY)
                                )
                        ))
        );

    }
}

