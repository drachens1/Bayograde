package org.drachens.Manager;

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator;
import de.articdive.jnoise.pipeline.JNoise;
import it.unimi.dsi.fastutil.Pair;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.Provinces.ProvinceManager;

import java.util.*;

import static org.drachens.util.ServerUtil.addChunk;

public class MapGeneratorManager {
    private final Map<Pos, Province> landHashmap = new HashMap<>();
    private final ProvinceManager provinceManager;
    private final List<Material> BLOCKS = new ArrayList<>();
    private final List<String> countryNames = new ArrayList<>();
    private final List<Pair<Material, Material>> borderPlusBlock = new ArrayList<>();
    private final HashMap<CurrencyTypes, Currencies> currenciesHashMap;
    private final CountryDataManager countryDataManager;
    int gMax = 4;
    int rgMax = 2;
    int yMax = 2;
    int lMax = 2;
    int grMax = 2;

    public MapGeneratorManager(Instance instance, ProvinceManager provinceManager, CountryDataManager countryDataManager, int countries, HashMap<CurrencyTypes, Currencies> defaultCurrencies) {
        this.countryDataManager = countryDataManager;
        currenciesHashMap = defaultCurrencies;
        JNoise baseNoise = JNoise.newBuilder()
                .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(new Random().nextInt()).build())
                .build();

        JNoise detailNoise = JNoise.newBuilder()
                .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(new Random().nextInt()).build())
                .build();
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
                "Antiguaand_Barbuda", "Argentina", "Armenia", "Aruba", "Ashmoreand_Cartier_Islands", "Australia", "Austria", "Azerbaijan",
                "Bahamas,The", "Bahrain", "Bangladesh", "Barbados", "Bassasda_India", "Belarus", "Belgium", "Belize", "Benin", "Bermuda",
                "Bhutan", "Bolivia", "Bosniaand_Herzegovina", "Botswana", "BouvetIsland", "Brazil", "BritishIndian_Ocean_Territory",
                "BritishVirgin_Islands", "Brunei", "Bulgaria", "BurkinaFaso", "Burma", "Burundi", "Cambodia", "Cameroon", "Canada", "CapeVerde",
                "CaymanIslands", "CentralAfrican_Republic", "Chad", "Chile", "China", "ChristmasIsland", "ClippertonIsland", "Cocos(Keeling)_Islands",
                "Colombia", "Comoros", "Democratic_Republic_of_the_Congo", "Republic_of_the_Congo", "CookIslands", "CoralSea_Islands", "CostaRica",
                "Coted'Ivoire", "Croatia", "Cuba", "Cyprus", "CzechRepublic", "Denmark", "Dhekelia", "Djibouti", "Dominica", "DominicanRepublic", "Ecuador",
                "Egypt", "ElSalvador", "EquatorialGuinea", "Eritrea", "Estonia", "Ethiopia", "EuropaIsland", "FalklandIslands_(Islas_Malvinas)", "FaroeIslands",
                "Fiji", "Finland", "France", "FrenchGuiana", "FrenchPolynesia", "FrenchSouthern_and_Antarctic_Lands", "Gabon", "Gambia,The", "GazaStrip",
                "Georgia", "Germany", "Ghana", "Gibraltar", "GloriosoIslands", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey",
                "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "HeardIsland_and_McDonald_Islands", "HolySee_(Vatican_City)", "Honduras", "HongKong", "Hungary",
                "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isleof_Man", "Israel", "Italy", "Jamaica", "JanMayen", "Japan", "Jersey", "Jordan",
                "Juande_Nova_Island", "Kazakhstan", "Kenya", "Kiribati", "Korea,North", "Korea,South", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon",
                "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives",
                "Mali", "Malta", "MarshallIslands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia,Federated_States_of", "Moldova",
                "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Namibia", "Nauru", "NavassaIsland", "Nepal", "Netherlands", "NetherlandsAntilles",
                "NewCaledonia", "NewZealand", "Nicaragua", "Niger", "Nigeria", "Niue", "NorfolkIsland", "NorthernMariana_Islands", "Norway", "Oman", "Pakistan",
                "Palau", "Panama", "PapuaNew_Guinea", "ParacelIslands", "Paraguay", "Peru", "Philippines", "PitcairnIslands", "Poland", "Portugal", "PuertoRico",
                "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "SaintHelena", "SaintKitts_and_Nevis", "SaintLucia", "SaintPierre_and_Miquelon",
                "SaintVincent_and_the_Grenadines", "Samoa", "SanMarino", "SaoTome_and_Principe", "SaudiArabia", "Senegal", "Serbiaand_Montenegro", "Seychelles",
                "SierraLeone", "Singapore", "Slovakia", "Slovenia", "SolomonIslands", "Somalia", "SouthAfrica", "SouthGeorgia_and_the_South_Sandwich_Islands",
                "Spain", "SpratlyIslands", "SriLanka", "Sudan", "Suriname", "Svalbard", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
                "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidadand_Tobago", "TromelinIsland", "Tunisia", "Turkey", "Turkmenistan",
                "Turksand_Caicos_Islands", "Tuvalu", "Uganda", "Ukraine", "UnitedArab_Emirates", "UnitedKingdom", "UnitedStates", "Uruguay", "Uzbekistan", "Vanuatu",
                "Venezuela", "Vietnam", "VirginIslands", "WakeIsland", "Wallisand_Futuna", "WestBank", "WesternSahara", "Yemen", "Zambia", "Zimbabwe",
                "Ingsoc", "Atlantis", "Greater_London", "Imperuim", "Wakanda", "Panem", "Narnia", "Oz", "Mordor", "The_Romulan_Star_Empire"
        };
        Collections.addAll(BLOCKS, block);
        Collections.addAll(countryNames, countryName);

        double baseScale = 0.01;
        double detailScale = 0.1;
        double threshold = 0.5;
        for (int x = -96; x < 96; x++) {
            for (int z = -96; z < 96; z++) {
                double baseNoiseValue = baseNoise.evaluateNoise(x * baseScale, z * baseScale);
                double detailNoiseValue = detailNoise.evaluateNoise(x * detailScale, z * detailScale);

                double combinedValue = baseNoiseValue + detailNoiseValue * 0.5;
                combinedValue = Math.max(-1, Math.min(1, combinedValue));
                double normalizedValue = (combinedValue + 1) / 2.0;
                Pos pos = new Pos(x, 0, z);
                instance.loadChunk(pos);
                Province province = new Province(pos, instance);
                if (normalizedValue > threshold) {
                    province.setCapturable(false);
                    province.setBlock(Material.BLUE_STAINED_GLASS);
                    provinceManager.registerProvince(pos, province);
                } else {
                    province.setBlock(Material.WHITE_TERRACOTTA);
                    provinceManager.registerProvince(pos, province);
                    landHashmap.put(pos, province);
                }
                addChunk(province.getChunk());
            }
        }
        this.provinceManager = provinceManager;
        createCountries(countries);
    }

    public void createCountries(int num) {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Country newCount = createCountry();
            countries.add(newCount);
            countryDataManager.addCountry(newCount);
            MinecraftServer.getTeamManager().getTeams().add(newCount);
        }
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
        return new Country(currenciesHashMap, countryName, block, border);
    }

    public void floodFill(List<Country> countries) {
        Queue<Pos>[] factionQueues = new Queue[countries.size()];
        Set<Pos> visited = new HashSet<>();

        for (int i = 0; i < countries.size(); i++) {
            factionQueues[i] = new LinkedList<>();
            Pos seedPos = chooseStartingPos();
            factionQueues[i].add(seedPos);
            Province seedProvince = provinceManager.getProvince(seedPos);
            if (seedProvince != null && seedProvince.getOccupier() == null) {
                seedProvince.setOccupier(countries.get(i));
                seedProvince.setCity(6);
                visited.add(seedPos);
                countries.get(i).setCapital(seedProvince);
            }
        }

        boolean anyQueueHadExpansion;
        do {
            anyQueueHadExpansion = false;

            for (int i = 0; i < countries.size(); i++) {
                if (!factionQueues[i].isEmpty()) {
                    Pos currentPos = factionQueues[i].poll();
                    List<Pos> neighbors = getNeighbors(currentPos);

                    for (Pos neighbor : neighbors) {
                        if (!visited.contains(neighbor) && landHashmap.containsKey(neighbor)) {
                            Province neighborProvince = provinceManager.getProvince(neighbor);
                            if (neighborProvince != null && neighborProvince.getOccupier() == null) {
                                neighborProvince.setOccupier(countries.get(i));
                                visited.add(neighbor);
                                factionQueues[i].add(neighbor);
                                anyQueueHadExpansion = true;
                            }
                        }
                    }
                }
            }
        } while (anyQueueHadExpansion);
        borderScan();
        generateCities(countries);
    }

    private List<Pos> getNeighbors(Pos pos) {
        List<Pos> neighbors = new ArrayList<>();
        int[][] directions = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
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
        Pos[] directions = {
                new Pos(-1, 0, 0), // West
                new Pos(1, 0, 0),  // East
                new Pos(0, 0, -1), // North
                new Pos(0, 0, 1),   // South
                new Pos(0, 0, 0) // current
        };
        for (Pos direction : directions) {
            Pos newLoc = loc.add(direction);
            change(newLoc);
        }
    }

    void change(Pos loc) {
        Province province = provinceManager.getProvince(loc);
        if (province == null || province.isCity()) return;
        Country country = province.getOccupier();
        if (country != null) {
            int[][] directions2 = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            };

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
        for (Country country : countries) {
            cityTypeGen(160, 5, gMax, country);
            cityTypeGen(180, 4, rgMax, country);
            cityTypeGen(200, 3, yMax, country);
            cityTypeGen(220, 2, lMax, country);
            cityTypeGen(240, 1, grMax, country);
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
            }
        }
    }
}
