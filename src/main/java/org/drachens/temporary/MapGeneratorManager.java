package org.drachens.temporary;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.articdive.jnoise.pipeline.JNoise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.VotingWinner;
import org.drachens.Manager.defaults.enums.ElectionsEnum;
import org.drachens.Manager.defaults.enums.IdeologiesEnum;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.BoostEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.Election;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.FlatPos;
import org.drachens.dataClasses.Modifier;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.territories.Province;
import org.drachens.interfaces.MapGen;
import org.drachens.temporary.clicks.ClicksCountry;
import org.drachens.temporary.troops.TroopCountry;

import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.drachens.util.Messages.logMsg;

public class MapGeneratorManager extends MapGen {
    HashMap<String, Integer> gMax = new HashMap<>(); //og = 4
    HashMap<String, Integer> rgMax = new HashMap<>(); //og = 2
    HashMap<String, Integer> yMax = new HashMap<>(); //og = 2
    HashMap<String, Integer> lMax = new HashMap<>(); //og = 2
    HashMap<String, Integer> grMax = new HashMap<>(); //og = 2
    int[][] directions = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
    private ProvinceManager provinceManager;
    private List<CountryType> countryNames;
    private HashMap<CurrencyTypes, Currencies> currenciesHashMap;
    private CountryDataManager countryDataManager;
    private Instance instance;
    private Ideology defIdeology;
    private Election defElection;
    private int countries;
    private VotingOption votingOption;
    private final List<CountryType> countryTypes;

    private enum CityNum {
        superPower(4,3,2,2,2),
        major(3,3,2,2,4),
        almostMajor(2,2,2,3,2),
        minor(1,1,1,1,3),
        irrelevant(1,0,0,1,2);

        private final int g;
        private final int rg;
        private final int y;
        private final int l;
        private final int gr;
        CityNum(int g, int rg, int y, int l, int gr){
            this.g=g;
            this.rg=rg;
            this.y=y;
            this.l=l;
            this.gr=gr;
        }
        public int g(){
            return g;
        }
        public int rg(){
            return rg;
        }
        public int y(){
            return y;
        }
        public int l(){
            return l;
        }
        public int gr(){
            return gr;
        }
    }
    private record CountryType(IdeologiesEnum ideologiesEnum, ElectionsEnum electionsEnum, Component name, String identifier, Material block, Material border, CityNum cityNum, String overlord, Leader leader, Modifier[] extra){}

    public MapGeneratorManager() {
        super(200, 200);
        CountryType[] countryTypes = new CountryType[]{
                //Europe
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Hungary", NamedTextColor.BLACK), "Hungary", Material.SCULK, Material.BLACK_CONCRETE_POWDER,CityNum.minor,null, new Leader.create(Component.text("Miklós Horthy")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("German Reich", NamedTextColor.GRAY), "GermanReich", Material.CYAN_TERRACOTTA, Material.GRAY_CONCRETE_POWDER,CityNum.superPower,null,new Leader.create(Component.text("Adolf Hitler")).addModifier(new Modifier.create(Component.text("Hitler")).addBoost(BoostEnum.stabilityBase,5f).addBoost(BoostEnum.production,0.05f).build()).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Italy", NamedTextColor.GREEN), "Italy", Material.LIME_TERRACOTTA, Material.LIME_CONCRETE_POWDER,CityNum.major,null,new Leader.create(Component.text("Benito Mussolini")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Romania", NamedTextColor.YELLOW), "Romania", Material.YELLOW_TERRACOTTA, Material.YELLOW_CONCRETE_POWDER,CityNum.minor,null,new Leader.create(Component.text("Milan Stojadinović")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Yugoslavia", NamedTextColor.BLUE), "Yugoslavia", Material.BLUE_CONCRETE_POWDER, Material.BLUE_CONCRETE,CityNum.almostMajor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Bulgaria", TextColor.color(762911)), "Bulgaria", Material.DIRT, Material.BROWN_CONCRETE_POWDER,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Finland", NamedTextColor.BLUE), "Finland", Material.WHITE_CONCRETE, Material.WHITE_CONCRETE_POWDER,CityNum.almostMajor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Albania", NamedTextColor.GREEN), "Albania", Material.PURPLE_WOOL, Material.PURPLE_CONCRETE_POWDER,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_socialist, ElectionsEnum.democratic, Component.text("Soviet Union", NamedTextColor.RED), "Soviet-Union", Material.NETHERRACK, Material.RED_TERRACOTTA,CityNum.superPower,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Belgium", NamedTextColor.GREEN), "Belgium", Material.YELLOW_CONCRETE_POWDER, Material.YELLOW_CONCRETE,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("France", NamedTextColor.BLUE), "France", Material.LIGHT_BLUE_TERRACOTTA, Material.PURPLE_CONCRETE_POWDER,CityNum.major,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Czechoslovakia", NamedTextColor.RED), "Czechoslovakia", Material.CYAN_WOOL, Material.LIGHT_BLUE_CONCRETE_POWDER,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.democratic, Component.text("Austria", NamedTextColor.WHITE), "Austria", Material.SNOW_BLOCK, Material.WHITE_CONCRETE_POWDER,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_conservatist, ElectionsEnum.democratic, Component.text("United Kingdom", TextColor.color(175, 122, 197)), "United-Kingdom", Material.PINK_TERRACOTTA, Material.MAGENTA_STAINED_GLASS,CityNum.major,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_conservatist, ElectionsEnum.authoritarian, Component.text("Poland", TextColor.color(175, 122, 197)), "Poland", Material.PINK_CONCRETE, Material.PINK_CONCRETE_POWDER,CityNum.almostMajor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_conservatist, ElectionsEnum.authoritarian, Component.text("Turkey", NamedTextColor.WHITE), "Turkey", Material.WHITE_TERRACOTTA, Material.WHITE_CONCRETE_POWDER,CityNum.almostMajor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_conservatist, ElectionsEnum.authoritarian, Component.text("Switzerland", NamedTextColor.RED), "Switzerland", Material.RED_CONCRETE, Material.RED_CONCRETE_POWDER,CityNum.almostMajor,null,new Leader.create(Component.text("")).build(),null),
                //Asia
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Manchukuo", NamedTextColor.GRAY), "Manchukuo", Material.ORANGE_CONCRETE, Material.ORANGE_CONCRETE_POWDER,CityNum.minor,"Japan",new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Mengjiang", NamedTextColor.GRAY), "Mengjiang", Material.ORANGE_CONCRETE, Material.ORANGE_CONCRETE_POWDER,CityNum.irrelevant,"Japan",new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Japan", TextColor.color(196, 98, 16)), "Japan", Material.ORANGE_CONCRETE, Material.ORANGE_CONCRETE_POWDER,CityNum.major,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_fascist, ElectionsEnum.authoritarian, Component.text("Iraq", NamedTextColor.RED), "Iraq", Material.RED_CONCRETE, Material.RED_TERRACOTTA,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_socialist, ElectionsEnum.democratic, Component.text("Mongolia", NamedTextColor.GREEN), "Mongolia", Material.LIME_CONCRETE, Material.LIME_TERRACOTTA,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("China", NamedTextColor.YELLOW), "China", Material.YELLOW_CONCRETE, Material.YELLOW_TERRACOTTA,CityNum.almostMajor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Nepal", NamedTextColor.GREEN), "Nepal", Material.LIME_CONCRETE, Material.LIME_TERRACOTTA,CityNum.irrelevant,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Saudi Arabia", NamedTextColor.YELLOW), "Saudi-Arabia", Material.YELLOW_CONCRETE, Material.YELLOW_TERRACOTTA,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Philippines", NamedTextColor.BLUE), "Philippines", Material.BLUE_CONCRETE, Material.BLUE_TERRACOTTA,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("British Raj", TextColor.color(954026)), "BritishRaj", Material.LIME_CONCRETE, Material.LIME_TERRACOTTA,CityNum.minor,"United-Kingdom",new Leader.create(Component.text("")).build(),null),
                //Oceania
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Australia", NamedTextColor.YELLOW), "Australia", Material.YELLOW_CONCRETE, Material.YELLOW_TERRACOTTA,CityNum.minor,"United-Kingdom",new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("New Zealand", NamedTextColor.GRAY), "New-Zealand", Material.GRAY_CONCRETE, Material.GRAY_TERRACOTTA,CityNum.minor,"United-Kingdom",new Leader.create(Component.text("")).build(),null),
                //North America
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("United States", NamedTextColor.BLUE), "United-States", Material.BLUE_CONCRETE, Material.BLUE_TERRACOTTA,CityNum.superPower,null,new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Canada", NamedTextColor.GRAY), "Canada", Material.GRAY_CONCRETE, Material.GRAY_TERRACOTTA,CityNum.minor,"United-Kingdom",new Leader.create(Component.text("")).build(),null),
                new CountryType(IdeologiesEnum.ww2_liberalist, ElectionsEnum.democratic, Component.text("Mexico", NamedTextColor.RED), "Mexico", Material.RED_CONCRETE, Material.RED_TERRACOTTA,CityNum.minor,null,new Leader.create(Component.text("")).build(),null),
        };
        this.countryTypes= Arrays.stream(countryTypes).toList();
        this.countryTypes.forEach(countryType -> {
            gMax.put(countryType.identifier(),countryType.cityNum.g());
            rgMax.put(countryType.identifier(),countryType.cityNum.rg());
            yMax.put(countryType.identifier(),countryType.cityNum.y());
            lMax.put(countryType.identifier(),countryType.cityNum.l());
            grMax.put(countryType.identifier(),countryType.cityNum.gr());
        });
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
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            removeGenerating(instance);
            countryDataManager.getCountries().forEach(Country::createInfo);
        }).delay(2, ChronoUnit.SECONDS).schedule();
        this.countries = votingOption.getCountries();
        this.currenciesHashMap = new HashMap<>(votingOption.getDefaultCurrencies());
        countryNames = new ArrayList<>(countryTypes);
        this.defIdeology = new Ideology(votingOption);
        this.defElection = new Election(votingOption);
        land = new ArrayList<>();
        countryHashMap = new HashMap<>();
        capitals=new HashMap<>();
        start();
    }

    private List<FlatPos> land;
    private HashMap<FlatPos, Country> countryHashMap;
    private HashMap<FlatPos, Country> capitals;

    public void start() {
        instance.enableAutoChunkLoad(true);

        JNoise noisePipeline = JNoise.newBuilder()
                .setNoiseSource(PerlinNoiseGenerator.newBuilder()
                        .setSeed(new Random().nextLong())
                        .setInterpolation(Interpolation.LINEAR)
                        .build())
                .clamp(0, 1)
                .scale(0.031)
                .build();

        for (int x = 0; x < getSizeX(); x++) {
            for (int y = 0; y < getSizeY(); y++) {
                double noiseValue = noisePipeline.evaluateNoise(x, y);
                if (noiseValue < 0.02) {
                    instance.setBlock(new Pos(x,0,y),Material.BLUE_STAINED_GLASS.block());
                } else {
                    land.add(new FlatPos(x,y));
                }
            }
        }

        MinecraftServer.getSchedulerManager().buildTask(this::createCountries).delay(10, ChronoUnit.MILLIS).schedule();
    }

    private void createCountries() {
        List<Country> countries = new ArrayList<>();

        for (int i = 0; i < this.countries; i++) {
            Country newCount = createCountry();
            countries.add(newCount);
            countryDataManager.addCountry(newCount);
        }
        countries.forEach((country) -> country.getIdeology().addIdeology(votingOption.getIdeologyTypes().get(new Random().nextInt(votingOption.getIdeologyTypes().size())), new Random().nextFloat(0, 15f)));
        floodFill(countries);
    }

    private Country createCountry() {
        CountryType countryName = countryNames.get(new Random().nextInt(countryNames.size()));
        countryNames.remove(countryName);
        HashMap<CurrencyTypes, Currencies> newCurrencies = new HashMap<>();
        for (Map.Entry<CurrencyTypes, Currencies> e : currenciesHashMap.entrySet()) {
            newCurrencies.put(e.getKey(), e.getValue().clone());
        }
        Country country;
        switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
                    case VotingWinner.ww2_clicks -> country = new ClicksCountry(newCurrencies, countryName.identifier(), countryName.name(), countryName.block(), countryName.border(), defIdeology, defElection, instance);
                    case VotingWinner.ww2_troops -> country = new TroopCountry(newCurrencies, countryName.identifier(), countryName.name(), countryName.block(), countryName.border(), defIdeology, defElection, instance);
                    default -> throw new IllegalArgumentException("Unexpected value: " + ContinentalManagers.world(instance).dataStorer().votingWinner);
        }
        country.getIdeology().addIdeology(countryName.ideologiesEnum().getIdeologyTypes() ,new Random().nextFloat(30f,60f));
        country.getElections().addElection(countryName.electionsEnum(),new Random().nextFloat(30f,60f));
        return country;
    }

    public void floodFill(List<Country> countries) {
        Queue<FlatPos>[] countryQueues = new Queue[countries.size()];

        for (int i = 0; i < countries.size(); i++) {
            countryQueues[i] = new LinkedList<>();
            FlatPos seedPos = chooseStartingPos();
            countryQueues[i].add(seedPos);
            if (!countryHashMap.containsKey(seedPos)) {
                land.remove(seedPos);
                countryHashMap.put(seedPos,countries.get(i));
                capitals.put(seedPos,countries.get(i));
            }
        }

        boolean anyQueueHadExpansion;
        do {
            anyQueueHadExpansion = false;

            for (int i = 0; i < countries.size(); i++) {
                if (!countryQueues[i].isEmpty()) {
                    for (FlatPos neighbor : getNeighbours(countryQueues[i].poll())) {
                        if (land.contains(neighbor)&&!countryHashMap.containsKey(neighbor)) {
                            countryQueues[i].add(neighbor);
                            countryHashMap.put(neighbor,countries.get(i));
                            anyQueueHadExpansion = true;
                            land.remove(neighbor);
                        }
                    }
                }
            }
        } while (anyQueueHadExpansion);
        land.forEach(z -> instance.setBlock(new Pos(z.x(),0,z.z()),Material.WHITE_TERRACOTTA.block()));

        borders(countries);
    }

    private void borders(List<Country> countries){
        countryHashMap.forEach(((flatPos, country) -> {
            Province province = new Province(new Pos(flatPos.x(),0,flatPos.z()),instance,country, new ArrayList<>());
            country.addOccupied(province);
            province.setCore(country);
            country.addCore(province);
            provinceManager.registerProvince(flatPos.x(),flatPos.z(),province);
        }));
        countryHashMap.forEach(((flatPos, country) -> {
            List<FlatPos> f = getNeighbours(flatPos);
            List<Province> p = new ArrayList<>();
            f.forEach(flatPos1 -> {
                Province province = ContinentalManagers.world(instance).provinceManager().getProvince(flatPos1);
                if (province==null)return;
                p.add(province);
            });
            Province province = provinceManager.getProvince(flatPos);
            province.setNeighbours(p);
            check(province);
        }));
        capitals.forEach(((flatPos, country) -> {
            Province province = provinceManager.getProvince(flatPos);
            province.setCity(6);
            country.setCapital(province);
        }));
        generateCities(countries);
    }

    private void check(Province province){
        Country country = province.getOccupier();
        for (Province neighbour : province.getNeighbours()){
            if (neighbour.getOccupier()!=country){
                province.setBorder();
                return;
            }
        }
        province.setBlock();
    }

    private List<FlatPos> getNeighbours(FlatPos e) {
        List<FlatPos> f = new ArrayList<>();
        for (int[] i : directions){
            f.add(new FlatPos(e.x()+i[0],e.z()+i[2]));
        }
        return f;
    }

    private FlatPos chooseStartingPos() {
        return land.get(new Random().nextInt(land.size()));
    }

    private void generateCities(List<Country> countries) {
        switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
                    case VotingWinner.ww2_clicks -> {
                        System.out.println("1");
                        for (Country c : countries) {
                            ClicksCountry country = (ClicksCountry) c;
                            cityTypeGen(160, 5, gMax.get(country.getName()), country);
                            cityTypeGen(180, 4, rgMax.get(country.getName()), country);
                            cityTypeGen(200, 3, yMax.get(country.getName()), country);
                            cityTypeGen(220, 2, lMax.get(country.getName()), country);
                            cityTypeGen(240, 1, grMax.get(country.getName()), country);
                            country.calculateCapitulationPercentage();
                        }
                    }
                    case VotingWinner.ww2_troops -> {
                        for (Country c : countries) {
                            TroopCountry country = (TroopCountry) c;
                            cityTypeGen(160, 5, gMax.get(country.getName()), country);
                            cityTypeGen(180, 4, rgMax.get(country.getName()), country);
                            cityTypeGen(200, 3, yMax.get(country.getName()), country);
                            cityTypeGen(220, 2, lMax.get(country.getName()), country);
                            cityTypeGen(240, 1, grMax.get(country.getName()), country);
                            country.calculateCapitulationPercentage();
                        }
                    }
                    default -> throw new IllegalArgumentException("Unexpected value: " + ContinentalManagers.world(instance).dataStorer().votingWinner);
        }
        finalLoop(countries);
    }

    private void cityTypeGen(int amount, int index, int max, Country country) {
        int t = country.getOccupies().size() / amount;
        System.out.println("T: "+t+" = "+country.getOccupies().size()+" / "+amount);
        if (!(t < 1)) for (int i = 0; i < t; i++) {
            if (t > max) break;
            Province p = country.getOccupies().get(new Random().nextInt(country.getOccupies().size()));
            if (p.isCity()) {
                System.out.println("1.5");
                i--;
            } else {
                p.setCity(index);
                country.addMajorCity(p, p.getMaterial());
                System.out.println("2");
            }
        }
    }

    private void finalLoop(List<Country> countries){
        countries.forEach(Country::init);
    }
}
