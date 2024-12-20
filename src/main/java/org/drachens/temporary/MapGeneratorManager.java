package org.drachens.temporary;

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator;
import de.articdive.jnoise.pipeline.JNoise;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Scheduler;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.Modifiers;
import org.drachens.Manager.defaults.defaultsStorer.enums.VotingWinner;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.Countries.*;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Modifier;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.territories.Continent;
import org.drachens.dataClasses.territories.Province;
import org.drachens.dataClasses.territories.Region;
import org.drachens.interfaces.MapGen;
import org.drachens.temporary.clicks.ClicksCountry;
import org.drachens.temporary.troops.TroopCountry;

import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.drachens.util.Messages.logMsg;
import static org.drachens.util.ServerUtil.addChunk;

public class MapGeneratorManager extends MapGen {
    private final List<Material> BLOCKS = new ArrayList<>();
    private final List<String> allCountryNames = new ArrayList<>();
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    public List<Continent> continents;
    HashMap<CountryEnums.Type, Integer> gMax = new HashMap<>(); //og = 4
    HashMap<CountryEnums.Type, Integer> rgMax = new HashMap<>(); //og = 2
    HashMap<CountryEnums.Type, Integer> yMax = new HashMap<>(); //og = 2
    HashMap<CountryEnums.Type, Integer> lMax = new HashMap<>(); //og = 2
    HashMap<CountryEnums.Type, Integer> grMax = new HashMap<>(); //og = 2
    int[][] directions = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
    Pos[] directions1 = {
            new Pos(-1, 0, 0), // West
            new Pos(1, 0, 0),  // East
            new Pos(0, 0, -1), // North
            new Pos(0, 0, 1),   // South
            new Pos(0, 0, 0) // current
    };
    int[][] directions2 = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
    };
    int maxCountries = 10;
    private Map<Pos, Province> landHashmap;
    private ProvinceManager provinceManager;
    private List<String> countryNames;
    private List<Pair<Material, Material>> borderPlusBlock;
    private HashMap<CurrencyTypes, Currencies> currenciesHashMap;
    private CountryDataManager countryDataManager;
    private Instance instance;
    private Ideology defIdeology;
    private Election defElection;
    private int countries;
    private List<ElectionTypes> electionTypes;
    private List<IdeologyTypes> ideologyTypes;
    private VotingOption votingOption;
    private final Modifiers modifiers = ContinentalManagers.defaultsStorer.modifier;
    private IdeologyTypes winnerOfThePrevWar;
    private IdeologyTypes upAndComingIdeology;
    private ElectionTypes upAndComingElection;
    private ElectionTypes electionWinnerPrevWar;

    public MapGeneratorManager() {
        super(111, 111);
        setupHashmaps();
        Material[] block = {
                //Terracotta's
                Material.ORANGE_TERRACOTTA, Material.MAGENTA_TERRACOTTA,
                Material.LIGHT_BLUE_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA,
                Material.PINK_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA,
                Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA,
                Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA,
                Material.BLACK_TERRACOTTA,
                //concrete
                Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE,
                Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE,
                Material.PINK_CONCRETE, Material.GRAY_CONCRETE, Material.LIGHT_GRAY_CONCRETE,
                Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE, Material.BLUE_CONCRETE,
                Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE,
                Material.BLACK_CONCRETE,
                //powdered
                Material.WHITE_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER,
                Material.LIGHT_BLUE_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER, Material.LIME_CONCRETE_POWDER,
                Material.PINK_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE_POWDER,
                Material.CYAN_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER, Material.BLUE_CONCRETE_POWDER,
                Material.BROWN_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER,
                Material.BLACK_CONCRETE_POWDER,
                //logs
                Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG,
                Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.MANGROVE_LOG, Material.CHERRY_LOG,
                // Planks
                Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.BIRCH_PLANKS, Material.JUNGLE_PLANKS,
                Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS, Material.MANGROVE_PLANKS, Material.CHERRY_PLANKS,
                //Wool
                Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
                Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
                Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
                Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
                Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
                Material.BLACK_WOOL
        };

        String[] countryName = {
                "Afghanistan", "Akrotiri", "Albania", "Algeria", "AmericanSamoa", "Andorra", "Angola", "Anguilla", "Antarctica",
                "Antiguaand_Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
                "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Bassasda_India", "Belarus", "Belgium", "Belize", "Benin", "Bermuda",
                "Bhutan", "Bolivia", "Bosniaand_Herzegovina", "Botswana", "BouvetIsland", "Brazil", "BritishIndian_Ocean_Territory",
                "BritishVirgin_Islands", "Brunei", "Bulgaria", "BurkinaFaso", "Burma", "Burundi", "Cambodia", "Cameroon", "Canada", "CapeVerde",
                "CaymanIslands", "CentralAfrican_Republic", "Chad", "Chile", "China", "ChristmasIsland", "ClippertonIsland", "Cocos(Keeling)_Islands",
                "Colombia", "Comoros", "Democratic_Republic_of_the_Congo", "Republic_of_the_Congo", "CookIslands", "CoralSea_Islands", "CostaRica",
                "CotedIvoire", "Croatia", "Cuba", "Cyprus", "CzechRepublic", "Denmark", "Dhekelia", "Djibouti", "Dominica", "DominicanRepublic", "Ecuador",
                "Egypt", "ElSalvador", "EquatorialGuinea", "Eritrea", "Estonia", "Ethiopia", "EuropaIsland", "FalklandIslands", "FaroeIslands",
                "Fiji", "Finland", "France", "FrenchGuiana", "FrenchPolynesia", "FrenchSouthern_and_Antarctic_Lands", "Gabon", "Gambia,The", "GazaStrip",
                "Georgia", "Germany", "Ghana", "Gibraltar", "GloriosoIslands", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey",
                "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "HolySee_(Vatican_City)", "Honduras", "HongKong", "Hungary",
                "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isleof_Man", "Israel", "Italy", "Jamaica", "JanMayen", "Japan", "Jersey", "Jordan",
                "Juande_Nova_Island", "Kazakhstan", "Kenya", "Kiribati", "Korea,North", "Korea,South", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon",
                "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives",
                "Mali", "Malta", "MarshallIslands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia,Federated_States_of", "Moldova",
                "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Namibia", "Nauru", "NavassaIsland", "Nepal", "Netherlands", "NetherlandsAntilles",
                "NewCaledonia", "NewZealand", "Nicaragua", "Niger", "Nigeria", "Niue", "NorfolkIsland", "NorthernMariana_Islands", "Norway", "Oman", "Pakistan",
                "Palau", "Panama", "PapuaNew_Guinea", "ParacelIslands", "Paraguay", "Peru", "Philippines", "PitcairnIslands", "Poland", "Portugal", "PuertoRico",
                "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "SaintHelena", "SaintKitts_and_Nevis", "SaintLucia", "SaintPierre_and_Miquelon",
                "SaintVincent_and_the_Grenadines", "Samoa", "SanMarino", "SaoTome_and_Principe", "SaudiArabia", "Senegal", "Serbiaand_Montenegro", "Seychelles",
                "SierraLeone", "Singapore", "Slovakia", "Slovenia", "SolomonIslands", "Somalia", "SouthAfrica",
                "Spain", "SpratlyIslands", "SriLanka", "Sudan", "Suriname", "Svalbard", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
                "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidadand_Tobago", "TromelinIsland", "Tunisia", "Turkey", "Turkmenistan",
                "Turksand_Caicos_Islands", "Tuvalu", "Uganda", "Ukraine", "UnitedArab_Emirates", "UnitedKingdom", "UnitedStates", "Uruguay", "Uzbekistan", "Vanuatu",
                "Venezuela", "Vietnam", "VirginIslands", "WakeIsland", "Wallisand_Futuna", "WestBank", "WesternSahara", "Yemen", "Zambia", "Zimbabwe",
                "Ingsoc", "Atlantis", "Greater_London", "Imperuim", "Wakanda", "Panem", "Narnia", "Oz", "Mordor", "The_Romulan_Star_Empire",
                "USSR", "Elementia"
        };
        Collections.addAll(BLOCKS, block);
        Collections.addAll(allCountryNames, countryName);
    }

    public static Integer calculateTopPercent(List<Integer> numbers, float top) {
        numbers.sort(Collections.reverseOrder());

        int count = (int) Math.ceil(numbers.size() * top);

        return numbers.get(count - 1);
    }

    private void setupHashmaps() {
        int goldMax = 3;
        int rgMax2 = 2;
        int yMax2 = 2;
        int lMax2 = 2;
        int grMax2 = 2;
        for (CountryEnums.Type e : CountryEnums.Type.values()) {
            System.out.println(e.name());
            gMax.put(e, goldMax);
            goldMax++;
            rgMax.put(e, rgMax2);
            yMax.put(e, yMax2);
            lMax.put(e, lMax2);
            grMax.put(e, grMax2);
        }
    }

    @Override
    public void generate(Instance instance, VotingOption votingOption) {
        if (isGenerating(instance)) {
            logMsg("server", "Tried to generate a new map when a map was generating", instance);
            return;
        }

        addGenerating(instance);
        this.votingOption = votingOption;
        this.instance = instance;
        this.provinceManager = ContinentalManagers.world(instance).provinceManager();
        provinceManager.reset();
        this.countryDataManager = ContinentalManagers.world(instance).countryDataManager();
        scheduler.buildTask(() -> {
            removeGenerating(instance);
            countryDataManager.getCountries().forEach(Country::createInfo);
        }).delay(2, ChronoUnit.SECONDS).schedule();
        this.countries = votingOption.getCountries();
        this.currenciesHashMap = new HashMap<>(votingOption.getDefaultCurrencies());
        continents = new ArrayList<>();
        landHashmap = new HashMap<>();
        borderPlusBlock = new ArrayList<>();
        countryNames = new ArrayList<>(allCountryNames);
        this.defIdeology = new Ideology(votingOption);
        this.defElection = new Election(votingOption);
        this.electionTypes = new ArrayList<>(votingOption.getElectionTypes());
        this.ideologyTypes = new ArrayList<>(votingOption.getIdeologyTypes());
        start();
    }

    public void start() {
        JNoise baseNoise = JNoise.newBuilder()
                .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(new Random().nextInt()).build())
                .build();

        JNoise detailNoise = JNoise.newBuilder()
                .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(new Random().nextInt()).build())
                .build();

        double baseScale = 0.01;
        double detailScale = 0.05;
        double threshold = 0.5;
        for (int x = -getSizeX(); x < getSizeX(); x++) {
            for (int z = -getSizeY(); z < getSizeY(); z++) {
                double baseNoiseValue = baseNoise.evaluateNoise(x * baseScale, z * baseScale);
                double detailNoiseValue = detailNoise.evaluateNoise(x * detailScale, z * detailScale);

                double combinedValue = baseNoiseValue + detailNoiseValue * 0.5;
                combinedValue = Math.max(-1, Math.min(1, combinedValue));
                double normalizedValue = (combinedValue + 1) / 2.0;
                Pos pos = new Pos(x, 0, z);
                instance.loadChunk(pos);
                Province province = new Province(pos, instance, new ArrayList<>());
                if (normalizedValue > threshold) {
                    province.setCapturable(false);
                    province.setBlock(Material.BLUE_STAINED_GLASS);
                    provinceManager.registerProvince(pos, province);
                } else {
                    provinceManager.registerProvince(pos, province);
                    landHashmap.put(pos, province);
                }
                addChunk(province.getChunk());
            }
        }
        scheduler.buildTask(() -> {
            setNeighbours();
            createCountries(countries);
        }).delay(10, ChronoUnit.MILLIS).schedule();

    }

    private void createCountries(int num) {
        List<Country> countries = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Country newCount = createCountry();
            countries.add(newCount);
            countryDataManager.addCountry(newCount);
        }
        countries.forEach((country) -> country.getIdeology().addIdeology(votingOption.getIdeologyTypes().get(new Random().nextInt(votingOption.getIdeologyTypes().size())), new Random().nextFloat(0, 15f)));
        floodFill(countries);
    }

    private Country createCountry() {
        Material block = BLOCKS.get(new Random().nextInt(BLOCKS.size()));
        Material border = BLOCKS.get(new Random().nextInt(BLOCKS.size()));
        Pair<Material, Material> bordersNblock = new Pair<>() {
            @Override
            public Material left() {
                return block;
            }

            @Override
            public Material right() {
                return border;
            }
        };
        if (borderPlusBlock.contains(bordersNblock)) {
            return createCountry();
        }
        borderPlusBlock.add(bordersNblock);
        String countryName = countryNames.get(new Random().nextInt(countryNames.size()));
        countryNames.remove(countryName);
        HashMap<CurrencyTypes, Currencies> newCurrencies = new HashMap<>();
        for (Map.Entry<CurrencyTypes, Currencies> e : currenciesHashMap.entrySet()) {
            newCurrencies.put(e.getKey(), e.getValue().clone());
        }
        switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
            case VotingWinner.ww2_clicks -> {
                return new ClicksCountry(newCurrencies, countryName, Component.text(countryName, NamedTextColor.BLUE), block, border, defIdeology, defElection, instance);
            }
            case VotingWinner.ww2_troops -> {
                return new TroopCountry(newCurrencies, countryName, Component.text(countryName, NamedTextColor.BLUE), block, border, defIdeology, defElection, instance);
            }
        }
        return null;
    }

    public void floodFill(List<Country> countries) {
        Queue<Pos>[] countryQueues = new Queue[countries.size()];
        Set<Pos> visited = new HashSet<>();

        for (int i = 0; i < countries.size(); i++) {
            countryQueues[i] = new LinkedList<>();
            Pos seedPos = chooseStartingPos();
            countryQueues[i].add(seedPos);
            Province seedProvince = provinceManager.getProvince(seedPos);
            if (seedProvince != null && seedProvince.getOccupier() == null) {
                seedProvince.initialOccupier(countries.get(i));
                seedProvince.setCity(6);
                visited.add(seedPos);
                landHashmap.remove(seedPos);
                countries.get(i).setCapital(seedProvince);
            }
        }

        boolean anyQueueHadExpansion;
        do {
            anyQueueHadExpansion = false;

            for (int i = 0; i < countries.size(); i++) {
                if (!countryQueues[i].isEmpty()) {
                    Pos currentPos = countryQueues[i].poll();
                    List<Pos> neighbors = getNeighbors(currentPos);

                    for (Pos neighbor : neighbors) {
                        if (!visited.contains(neighbor) && landHashmap.containsKey(neighbor)) {
                            Province neighborProvince = provinceManager.getProvince(neighbor);
                            if (neighborProvince != null && neighborProvince.getOccupier() == null) {
                                neighborProvince.initialOccupier(countries.get(i));
                                landHashmap.remove(neighbor);
                                visited.add(neighbor);
                                countryQueues[i].add(neighbor);
                                anyQueueHadExpansion = true;
                            }
                        }
                    }
                }
            }
        } while (anyQueueHadExpansion);
        landHashmap.forEach((pos, province) -> province.setBlock(Material.WHITE_TERRACOTTA));

        storiesGenerate(countries);
        borderScan();
        generateCities(countries);
    }

    private List<Pos> getNeighbors(Pos pos) {
        List<Pos> neighbors = new ArrayList<>();
        for (int[] dir : directions) {
            Pos neighbor = new Pos(pos.blockX() + dir[0], pos.blockY(), pos.blockZ() + dir[2]);
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    private Pos chooseStartingPos() {
        List<Pos> availablePositions = new ArrayList<>(landHashmap.keySet());
        Pos attempt = availablePositions.get(new Random().nextInt(availablePositions.size()));
        if (landHashmap.get(attempt).getOccupier() != null) {
            return chooseStartingPos();
        }
        return attempt;
    }

    private void borderScan() {
        Pos pos1 = new Pos(-100, 0, -100);
        Pos pos2 = new Pos(100, 0, 100);
        for (int x = Math.min((int) pos1.x(), (int) pos2.x()); x <= Math.max((int) pos1.x(), (int) pos2.x()); x++) {
            for (int z = Math.min((int) pos1.z(), (int) pos2.z()); z <= Math.max((int) pos1.z(), (int) pos2.z()); z++) {
                Pos newLoc = new Pos(x, 0, z);
                Province p = provinceManager.getProvince(newLoc);
                if (p != null && p.getOccupier() != null) {
                    updateBorders(newLoc);
                }
            }
        }
    }

    public void updateBorders(Pos loc) {
        for (Pos direction : directions1) {
            Pos newLoc = loc.add(direction);
            change(newLoc);
        }
    }

    void change(Pos loc) {
        Province province = provinceManager.getProvince(loc);
        if (province == null || province.isCity()) return;
        Country country = province.getOccupier();
        if (country != null) {
            for (int[] direction2 : directions2) {
                int offsetX2 = direction2[0];
                int offsetY2 = direction2[1];

                Pos neighborLocation = loc.add(offsetX2, 0, offsetY2);

                Province neighbourBlock = provinceManager.getProvince(neighborLocation);
                if (neighbourBlock != null && neighbourBlock.isCapturable() && neighbourBlock.getOccupier() != country) {
                    province.setBorder();
                    return;
                }
            }
            province.setBlock();
        }
    }

    private void generateCities(List<Country> countries) {
        switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
            case VotingWinner.ww2_clicks -> {
                for (Country c : countries) {
                    ClicksCountry country = (ClicksCountry) c;
                    cityTypeGen(160, 5, gMax.get(country.getType()), country);
                    cityTypeGen(180, 4, rgMax.get(country.getType()), country);
                    cityTypeGen(200, 3, yMax.get(country.getType()), country);
                    cityTypeGen(220, 2, lMax.get(country.getType()), country);
                    cityTypeGen(240, 1, grMax.get(country.getType()), country);
                    country.calculateCapitulationPercentage();
                }
            }
            case VotingWinner.ww2_troops -> {
                for (Country c : countries) {
                    TroopCountry country = (TroopCountry) c;
                    cityTypeGen(160, 5, gMax.get(country.getType()), country);
                    cityTypeGen(180, 4, rgMax.get(country.getType()), country);
                    cityTypeGen(200, 3, yMax.get(country.getType()), country);
                    cityTypeGen(220, 2, lMax.get(country.getType()), country);
                    cityTypeGen(240, 1, grMax.get(country.getType()), country);
                    country.calculateCapitulationPercentage();
                }
            }
        }

    }

    private void cityTypeGen(int amount, int index, int max, Country country) {
        int t = country.getOccupies().size() / amount;
        if (!(t < 1)) for (int i = 0; i < t; i++) {
            if (t > max) break;
            Province p = country.getOccupies().get(new Random().nextInt(country.getOccupies().size()));
            if (p.isCity()) {
                i--;
            } else {
                p.setCity(index);
                country.addCity(p);
                country.addMajorCity(p, p.getMaterial());
            }
        }
    }

    private void storiesGenerate(List<Country> countries) {
        List<Integer> size = new ArrayList<>();
        for (Country country : countries) {
            size.add(country.getOccupies().size());
        }
        float medianSize = calculateTopPercent(size, 0.2f);
        for (int b = 0; b < size.size(); b++) {
            switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
                case VotingWinner.ww2_clicks -> {
                    ClicksCountry country = (ClicksCountry) countries.get(b);
                    if (size.get(b) >= medianSize) {
                        country.setType(CountryEnums.Type.major);
                        country.addModifier(modifiers.getModifier("ww2-major"));
                    } else {
                        country.setType(CountryEnums.Type.minor);
                        country.addModifier(modifiers.getModifier("ww2-minor"));
                    }
                }
                case VotingWinner.ww2_troops -> {
                    TroopCountry country = (TroopCountry) countries.get(b);
                    if (size.get(b) >= medianSize) {
                        country.setType(CountryEnums.Type.major);
                        country.addModifier(modifiers.getModifier("ww2-major"));
                    } else {
                        country.setType(CountryEnums.Type.minor);
                        country.addModifier(modifiers.getModifier("ww2-minor"));
                    }
                }
            }

        }
        assignRegion(countries);
        createWinnersNUpAndComers();
        createContinent(countries, setSuperPower(countries));
    }

    private Country setSuperPower(List<Country> countries) {
        Country biggest = countries.getFirst();
        for (Country country : countries) {
            if (country.getOccupies().size() > biggest.getOccupies().size()) biggest = country;
        }
        switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
            case VotingWinner.ww2_clicks -> {
                ClicksCountry country = (ClicksCountry) biggest;
                country.setType(CountryEnums.Type.superPower);
            }
            case VotingWinner.ww2_troops -> {
                TroopCountry country = (TroopCountry) biggest;
                country.setType(CountryEnums.Type.superPower);
            }
        }

        return biggest;
    }

    private void createWinnersNUpAndComers() {
        List<IdeologyTypes> temp = votingOption.getIdeologyTypes();
        winnerOfThePrevWar = temp.get(new Random().nextInt(0, temp.size()));
        temp.remove(winnerOfThePrevWar);
        upAndComingIdeology = temp.get(new Random().nextInt(0, temp.size()));

        List<ElectionTypes> temp2 = votingOption.getElectionTypes();
        electionWinnerPrevWar = temp2.get(new Random().nextInt(0, temp2.size()));
        temp2.remove(electionWinnerPrevWar);
        upAndComingElection = temp2.get(new Random().nextInt(0, temp2.size()));
    }

    private void createContinent(List<Country> countries, Country superPower) {
        superPower.removeModifier(modifiers.getModifier("ww2-major"));
        superPower.addModifier(modifiers.getModifier("ww2-superpower"));
        Continent mainContinent = new Continent();
        continents.add(mainContinent);
        Queue<Pos> continentQueue = new LinkedList<>();
        Set<Pos> visited = new HashSet<>();

        Pos seedPos = superPower.getCapital().getPos();
        continentQueue.add(seedPos);
        Province seedProvince = provinceManager.getProvince(seedPos);
        mainContinent.addProvince(seedProvince);


        boolean anyQueueHadExpansion;
        int currentInCont = 0;
        do {
            anyQueueHadExpansion = false;

            for (int i = 0; i < countries.size(); i++) {
                if (!continentQueue.isEmpty()) {
                    Pos currentPos = continentQueue.poll();
                    List<Pos> neighbors = getNeighbors(currentPos);

                    for (Pos neighbor : neighbors) {
                        if (!visited.contains(neighbor) && landHashmap.containsKey(neighbor)) {
                            Province neighborProvince = provinceManager.getProvince(neighbor);
                            if (neighborProvince != null && neighborProvince.getOccupier() != null) {
                                mainContinent.addProvince(neighborProvince);
                                if (!mainContinent.getCountries().contains(neighborProvince.getOccupier())) {
                                    if (currentInCont >= maxCountries) break;
                                    mainContinent.addCountry(neighborProvince.getOccupier());
                                    currentInCont++;
                                }
                                visited.add(neighbor);
                                continentQueue.add(neighbor);
                                anyQueueHadExpansion = true;
                            }
                        }
                    }
                }
            }
        } while (anyQueueHadExpansion);

        Modifier modifier = modifiers.getModifier("ww2-example");
        for (Country c : countries) {
            switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
                case VotingWinner.ww2_clicks -> {
                    ClicksCountry country = (ClicksCountry) c;
                    country.getIdeology().changeLeadingIdeology();
                    if (country.getType().equals(CountryEnums.Type.major)) {
                        historyAssignMajor(country);
                    } else if (country.getType().equals(CountryEnums.Type.minor)) {
                        historyAssignMinors(country);
                    }
                    ideologyBoost(country);
                    country.addModifier(modifier);
                    historyAssignSuperPowers(country);
                }
                case VotingWinner.ww2_troops -> {
                    TroopCountry country = (TroopCountry) c;
                    country.getIdeology().changeLeadingIdeology();
                    if (country.getType().equals(CountryEnums.Type.major)) {
                        historyAssignMajor(country);
                    } else if (country.getType().equals(CountryEnums.Type.minor)) {
                        historyAssignMinors(country);
                    }
                    ideologyBoost(country);
                    country.addModifier(modifier);
                    historyAssignSuperPowers(country);
                }
            }

        }
        superPower.getIdeology().changeLeadingIdeology();
    }

    private void assignRegion(List<Country> countries) {

        Region north = new Region("north", getRandomIdeology(), getRandomElection());
        Region south = new Region("south", getRandomIdeology(), getRandomElection());
        Region east = new Region("east", getRandomIdeology(), getRandomElection());
        Region west = new Region("west", getRandomIdeology(), getRandomElection());

        Pos spawn = new Pos(0, 0, 0);
        for (Country country : countries) {
            int x = country.getCapital().getPos().blockX();
            int z = country.getCapital().getPos().blockZ();
            Ideology ideology = country.getIdeology();
            Election election = country.getElections();
            if (x > 0) {
                ideology.addIdeology(north.getLeadingIdeology(), new Random().nextFloat(0f, (float) Math.abs(country.getCapital().getPos().distance(spawn) * 2)));
                country.addRegion(north);
            } else {
                ideology.addIdeology(south.getLeadingIdeology(), new Random().nextFloat(0f, (float) Math.abs(country.getCapital().getPos().distance(spawn) * 2)));
                country.addRegion(south);
            }
            if (z > 0) {
                election.addElection(east.getLeadingElectionType(), new Random().nextFloat(0f, (float) Math.abs(country.getCapital().getPos().distance(spawn) * 2)));
                country.addRegion(east);
            } else {
                election.addElection(west.getLeadingElectionType(), new Random().nextFloat(0f, (float) Math.abs(country.getCapital().getPos().distance(spawn) * 2)));
                country.addRegion(west);
            }
        }
    }

    private IdeologyTypes getRandomIdeology() {
        return ideologyTypes.get(new Random().nextInt(ideologyTypes.size()));
    }

    private ElectionTypes getRandomElection() {
        return electionTypes.get(new Random().nextInt(electionTypes.size()));
    }

    private void historyAssignMajor(TroopCountry country) {
        if (new Random().nextBoolean()) {
            country.setHistory(CountryEnums.History.colonialPower);
        } else {
            country.setHistory(CountryEnums.History.upAndComing);
        }
        leadershipStyle(country);
        int num2 = new Random().nextInt(0, 3);
        switch (num2) {
            case 0:
                country.setPreviousWar(CountryEnums.PreviousWar.winner);
                country.getIdeology().addIdeology(winnerOfThePrevWar, new Random().nextFloat(0, 40f));
                country.getElections().addElection(electionWinnerPrevWar, new Random().nextFloat(0, 40f));
                break;
            case 1:
                country.setPreviousWar(CountryEnums.PreviousWar.loser);
                country.setFocuses(CountryEnums.Focuses.war);
                break;
            case 2:
                country.setPreviousWar(CountryEnums.PreviousWar.neutral);
                break;
        }
        if (country.getPreviousWar().equals(CountryEnums.PreviousWar.loser)) {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.unfriendly);
                country.getIdeology().addIdeology(upAndComingIdeology, new Random().nextFloat(0, 40f));
                country.getElections().addElection(upAndComingElection, new Random().nextFloat(0, 40f));
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        } else {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.friendly);
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        }
    }

    private void historyAssignMajor(ClicksCountry country) {
        if (new Random().nextBoolean()) {
            country.setHistory(CountryEnums.History.colonialPower);
        } else {
            country.setHistory(CountryEnums.History.upAndComing);
        }
        leadershipStyle(country);
        int num2 = new Random().nextInt(0, 3);
        switch (num2) {
            case 0:
                country.setPreviousWar(CountryEnums.PreviousWar.winner);
                country.getIdeology().addIdeology(winnerOfThePrevWar, new Random().nextFloat(0, 40f));
                country.getElections().addElection(electionWinnerPrevWar, new Random().nextFloat(0, 40f));
                break;
            case 1:
                country.setPreviousWar(CountryEnums.PreviousWar.loser);
                country.setFocuses(CountryEnums.Focuses.war);
                break;
            case 2:
                country.setPreviousWar(CountryEnums.PreviousWar.neutral);
                break;
        }
        if (country.getPreviousWar().equals(CountryEnums.PreviousWar.loser)) {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.unfriendly);
                country.getIdeology().addIdeology(upAndComingIdeology, new Random().nextFloat(0, 40f));
                country.getElections().addElection(upAndComingElection, new Random().nextFloat(0, 40f));
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        } else {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.friendly);
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        }
    }

    private void historyAssignMinors(TroopCountry country) {
        if (new Random().nextBoolean()) {
            country.setHistory(CountryEnums.History.colony);
        } else {
            country.setHistory(CountryEnums.History.previousColonies);
        }
        leadershipStyle(country);
        int num2 = new Random().nextInt(0, 3);
        switch (num2) {
            case 0:
                country.setPreviousWar(CountryEnums.PreviousWar.winner);
                country.getIdeology().addIdeology(winnerOfThePrevWar, new Random().nextFloat(0, 40f));
                country.getElections().addElection(electionWinnerPrevWar, new Random().nextFloat(0, 40f));
                break;
            case 1:
                country.setPreviousWar(CountryEnums.PreviousWar.loser);
                country.setFocuses(CountryEnums.Focuses.war);
                country.getIdeology().addIdeology(winnerOfThePrevWar, new Random().nextFloat(0, 40f));
                country.getElections().addElection(electionWinnerPrevWar, new Random().nextFloat(0, 40f));
                break;
            case 2:
                country.setPreviousWar(CountryEnums.PreviousWar.neutral);
                break;
        }
        //country.setElections(new Random().nextBoolean());
        if (country.getPreviousWar().equals(CountryEnums.PreviousWar.loser)) {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.unfriendly);
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        } else {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.friendly);
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        }
    }

    private void historyAssignMinors(ClicksCountry country) {
        if (new Random().nextBoolean()) {
            country.setHistory(CountryEnums.History.colony);
        } else {
            country.setHistory(CountryEnums.History.previousColonies);
        }
        leadershipStyle(country);
        int num2 = new Random().nextInt(0, 3);
        switch (num2) {
            case 0:
                country.setPreviousWar(CountryEnums.PreviousWar.winner);
                country.getIdeology().addIdeology(winnerOfThePrevWar, new Random().nextFloat(0, 40f));
                country.getElections().addElection(electionWinnerPrevWar, new Random().nextFloat(0, 40f));
                break;
            case 1:
                country.setPreviousWar(CountryEnums.PreviousWar.loser);
                country.setFocuses(CountryEnums.Focuses.war);
                country.getIdeology().addIdeology(winnerOfThePrevWar, new Random().nextFloat(0, 40f));
                country.getElections().addElection(electionWinnerPrevWar, new Random().nextFloat(0, 40f));
                break;
            case 2:
                country.setPreviousWar(CountryEnums.PreviousWar.neutral);
                break;
        }
        //country.setElections(new Random().nextBoolean());
        if (country.getPreviousWar().equals(CountryEnums.PreviousWar.loser)) {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.unfriendly);
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        } else {
            if (new Random().nextBoolean()) {
                country.setRelationsStyle(CountryEnums.RelationsStyle.friendly);
            } else {
                country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
            }
        }
    }

    private void historyAssignSuperPowers(ClicksCountry country) {
        leadershipStyle(country);
        country.setHistory(CountryEnums.History.colonialPower);
        country.setPreviousWar(CountryEnums.PreviousWar.winner);
        //country.setElections(new Random().nextBoolean());
        country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
    }

    private void historyAssignSuperPowers(TroopCountry country) {
        leadershipStyle(country);
        country.setHistory(CountryEnums.History.colonialPower);
        country.setPreviousWar(CountryEnums.PreviousWar.winner);
        //country.setElections(new Random().nextBoolean());
        country.setRelationsStyle(CountryEnums.RelationsStyle.issolationist);
    }

    private void leadershipStyle(TroopCountry country) {
        int num2 = new Random().nextInt(0, 5);
        switch (num2) {
            case 0:
                country.setFocuses(CountryEnums.Focuses.economy);
                break;
            case 1:
                country.setFocuses(CountryEnums.Focuses.diverse);
                break;
            case 2:
                country.setFocuses(CountryEnums.Focuses.military);
                break;
            case 3:
                country.setFocuses(CountryEnums.Focuses.stability);
                break;
            case 4:
                country.setFocuses(CountryEnums.Focuses.war);
                break;
        }
    }

    private void leadershipStyle(ClicksCountry country) {
        int num2 = new Random().nextInt(0, 5);
        switch (num2) {
            case 0:
                country.setFocuses(CountryEnums.Focuses.economy);
                break;
            case 1:
                country.setFocuses(CountryEnums.Focuses.diverse);
                break;
            case 2:
                country.setFocuses(CountryEnums.Focuses.military);
                break;
            case 3:
                country.setFocuses(CountryEnums.Focuses.stability);
                break;
            case 4:
                country.setFocuses(CountryEnums.Focuses.war);
                break;
        }
    }

    private void ideologyBoost(Country country) {
        country.getIdeology().addIdeology(winnerOfThePrevWar, new Random().nextFloat(0, 50f));
    }

    private List<Province> getNeighbours(Province province) {
        List<Province> neighbour = new ArrayList<>();
        for (int[] offset : directions2) {
            neighbour.add(provinceManager.getProvince(province.getPos().add(offset[0], 0, offset[1])));
        }
        return neighbour;
    }

    private void setNeighbours() {
        for (Map.Entry<Pos, Province> entry : provinceManager.getProvinceHashMap().entrySet()) {
            Province province = entry.getValue();
            province.setNeighbours(getNeighbours(province));
        }
    }
}
