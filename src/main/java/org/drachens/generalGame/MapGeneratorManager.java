package org.drachens.generalGame;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import de.articdive.jnoise.pipeline.JNoise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ColoursEnum;
import org.drachens.Manager.defaults.enums.IdeologiesEnum;
import org.drachens.Manager.defaults.enums.ModifiersEnum;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.FlatPos;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.additional.Purges;
import org.drachens.dataClasses.additional.greatDepression.GreatDepressionEventsRunner;
import org.drachens.dataClasses.laws.Law;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.generalGame.clicks.ClicksCountry;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.interfaces.MapGen;
import org.drachens.interfaces.ai.AIManager;

import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.drachens.util.Messages.logMsg;

public class MapGeneratorManager extends MapGen {
    private final HashMap<String, LawCategory> laws = new HashMap<>();
    private final List<CountryType> countryTypes;
    final HashMap<String, Integer> gMax = new HashMap<>(); //og = 4
    final HashMap<String, Integer> rgMax = new HashMap<>(); //og = 2
    final HashMap<String, Integer> yMax = new HashMap<>(); //og = 2
    final HashMap<String, Integer> lMax = new HashMap<>(); //og = 2
    final HashMap<String, Integer> grMax = new HashMap<>(); //og = 2
    final int[][] directions = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
    private ProvinceManager provinceManager;
    private List<CountryType> countryNames;
    private HashMap<CurrencyTypes, Currencies> currenciesHashMap;
    private CountryDataManager countryDataManager;
    private Instance instance;
    private Ideology defIdeology;
    private int countries;
    private VotingOption votingOption;
    private List<FlatPos> land;
    private HashMap<FlatPos, Country> countryHashMap;
    private HashMap<FlatPos, Country> capitals;

    public MapGeneratorManager() {
        super(100, 100);
        List<LawCategory> lawCategories = new ArrayList<>();
        lawCategories.add(new LawCategory.Create("Conscription")
                .setDefault("volunteer")
                .addLaw(new Law("high", new Modifier.create(Component.text("Everyone serves"), "everyone_serves").addBoost(BoostEnum.recruitablePop, 1.0f).build(), null))
                .addLaw(new Law("all-adults", new Modifier.create(Component.text("All adults serve"), "all_adults_served").addBoost(BoostEnum.recruitablePop, 0.5f).build(), null))
                .addLaw(new Law("all-men", new Modifier.create(Component.text("All men serve"), "all_men_serve").addBoost(BoostEnum.recruitablePop, 0.3f).build(), null))
                .addLaw(new Law("mass", new Modifier.create(Component.text("Mass conscription"), "mass_conscription").addBoost(BoostEnum.recruitablePop, 0.1f).build(), null))
                .addLaw(new Law("low", new Modifier.create(Component.text("Limited conscription"), "limited_conscription").addBoost(BoostEnum.recruitablePop, 0.05f).build(), null))
                .addLaw(new Law("volunteer", new Modifier.create(Component.text("Volunteer only"), "volunteer_only").addBoost(BoostEnum.recruitablePop, 0.01f).build(), null))
                .build());
        lawCategories.add(new LawCategory.Create("Womens-Rights")
                .setDefault("no-rights")
                .addLaw(new Law("no-rights", new Modifier.create(Component.text("No rights"), "no_rights").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_liberalist.getIdeologyTypes()))
                .addLaw(new Law("workplace", new Modifier.create(Component.text("Allowed in the workplace"), "allowed_in_the_workplace").build(), null))
                .addLaw(new Law("right-to-vote-and-work", new Modifier.create(Component.text("The right to vote and work"), "the_right_to_vote_and_work").build(),null))
                .build());
        lawCategories.add(new LawCategory.Create("Media-Policy")
                .setDefault("limited-censoring-laws")
                .addLaw(new Law("complete-control-of-media", new Modifier.create(Component.text("Complete control"), "complete_control").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_liberalist.getIdeologyTypes()))
                .addLaw(new Law("limited-censoring-laws", new Modifier.create(Component.text("Limited censoring"), "limited_censoring").build(), null))
                .addLaw(new Law("free-media", new Modifier.create(Component.text("Free media"), "free_media").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_fascist.getIdeologyTypes()))
                .build());
        lawCategories.add(new LawCategory.Create("Immigration-policy")
                .setDefault("closed-borders")
                .addLaw(new Law("open-borders", new Modifier.create(Component.text("Open borders"), "open_borders").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_fascist.getIdeologyTypes()))
                .addLaw(new Law("application-process", new Modifier.create(Component.text("Application process"), "application_process").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_fascist.getIdeologyTypes()))
                .addLaw(new Law("closed-borders", new Modifier.create(Component.text("Closed borders"), "closed_borders").build(), null))
                .addLaw(new Law("thorough-application-process", new Modifier.create(Component.text("Thorough application process"), "thorough_application_process").build(), null))
                .build());
        lawCategories.add(new LawCategory.Create("Equality")
                .setDefault("some-equality")
                .addLaw(new Law("concentration-camps", new Modifier.create(Component.text()
                        .append(Component.text("Concentration camps")).hoverEvent(HoverEvent.showText(Component.text("We should never forget"))).build(), "concentration-camps").build(), country -> country.getIdeology().getCurrentIdeology() == IdeologiesEnum.ww2_fascist.getIdeologyTypes()))
                .addLaw(new Law("oppression", new Modifier.create(Component.text("Oppression"), "oppression").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_fascist.getIdeologyTypes()))
                .addLaw(new Law("segregation", new Modifier.create(Component.text("Segregation"), "segregation").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_fascist.getIdeologyTypes()))
                .addLaw(new Law("some-equality", new Modifier.create(Component.text("Some equality"), "some_equality").build(), null))
                .addLaw(new Law("complete-equality", new Modifier.create(Component.text("Complete equality"), "complete_equality").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_fascist.getIdeologyTypes()))
                .build());
        lawCategories.add(new LawCategory.Create("Companies-policies")
                .setDefault("capitalism")
                .addLaw(new Law("nationalization", new Modifier.create(Component.text("Nationalization"), "nationalization").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_capitalist.getIdeologyTypes()))
                .addLaw(new Law("state-capitalism", new Modifier.create(Component.text("State Capitalism"), "state_capitalism").build(), country -> country.getIdeology().getCurrentIdeology() == IdeologiesEnum.ww2_socialist.getIdeologyTypes()))
                .addLaw(new Law("capitalism", new Modifier.create(Component.text("Capitalism"), "capitalism").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_socialist.getIdeologyTypes()))
                .addLaw(new Law("free-market", new Modifier.create(Component.text("Free Market"), "free_market").build(), country -> country.getIdeology().getCurrentIdeology() != IdeologiesEnum.ww2_socialist.getIdeologyTypes()))
                .build());

        lawCategories.forEach(lawCategory -> laws.put(lawCategory.getIdentifier(), lawCategory));

        Leader georgeV = new Leader.create(Component.text("George V")).setDescription(Component.text("\nKing of the United Kingdom, British Dominions and the Emperor of India", NamedTextColor.GRAY)).build();

        CountryType[] countryTypes = {//Those with puppets MUST be first
                //Europe
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Austria", ColoursEnum.WHITE.getTextColor()), "Austria", Material.SNOW_BLOCK, Material.WHITE_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Kurt Schuschnigg")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Albania", ColoursEnum.PURPLE.getTextColor()), "Albania", Material.PURPLE_WOOL, Material.PURPLE_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Zog I")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Belgium", ColoursEnum.YELLOW.getTextColor()), "Belgium", Material.YELLOW_CONCRETE_POWDER, Material.YELLOW_CONCRETE, CityNum.minor, null, new Leader.create(Component.text("Paul van Zeeland")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Bulgaria", ColoursEnum.BROWN.getTextColor()), "Bulgaria", Material.DIRT, Material.BROWN_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Boris III")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Czechoslovakia", ColoursEnum.CYAN.getTextColor()), "Czechoslovakia", Material.CYAN_WOOL, Material.LIGHT_BLUE_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Edvard Beneš")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Denmark", ColoursEnum.BROWN.getTextColor()), "Denmark", Material.SOUL_SOIL, Material.BROWN_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Thorvald Stauning")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Estonia", ColoursEnum.CYAN.getTextColor()), "Estonia", Material.WARPED_WART_BLOCK, Material.CYAN_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Konstantin Päts")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Finland", ColoursEnum.WHITE.getTextColor()), "Finland", Material.WHITE_CONCRETE, Material.WHITE_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Pehr Evind Svinhufvud")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("France", ColoursEnum.CYAN.getTextColor()), "France", Material.LIGHT_BLUE_TERRACOTTA, Material.PURPLE_CONCRETE_POWDER, CityNum.major, null, new Leader.create(Component.text("Albert Lebrun")).build(), null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("German Reich", ColoursEnum.GRAY.getTextColor()), "GermanReich", Material.CYAN_TERRACOTTA, Material.GRAY_CONCRETE_POWDER, CityNum.superPower, null, new Leader.create(Component.text("Adolf Hitler")).addModifier(new Modifier.create(Component.text("Hitler"), "hitler").addBoost(BoostEnum.stabilityBase, 5.0f).addBoost(BoostEnum.production, 0.05f).build()).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Greece", ColoursEnum.BLUE.getTextColor()), "Greece", Material.LIGHT_BLUE_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE, CityNum.superPower, null, new Leader.create(Component.text("George II")).build(), null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("Hungary", ColoursEnum.GRAY.getTextColor()), "Hungary", Material.SCULK, Material.BLACK_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Miklós Horthy")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Ireland", ColoursEnum.LIME.getTextColor()), "Ireland", Material.LIME_CONCRETE_POWDER, Material.LIME_CONCRETE, CityNum.minor, null, new Leader.create(Component.text("Domhnall Ua Buachalla")).build(), null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("Italy", ColoursEnum.GREEN.getTextColor()), "Italy", Material.LIME_TERRACOTTA, Material.LIME_CONCRETE_POWDER, CityNum.major, null, new Leader.create(Component.text("Benito Mussolini")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Latvia", ColoursEnum.CYAN.getTextColor()), "Latvia", Material.CRIMSON_PLANKS, Material.PURPLE_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Alberts Kviesis")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Lithuania", ColoursEnum.YELLOW.getTextColor()), "Lithuania", Material.SPONGE, Material.YELLOW_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Antanas Smetona")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Luxembourg", ColoursEnum.GREEN.getTextColor()), "Luxembourg", Material.GREEN_CONCRETE, Material.GREEN_CONCRETE_POWDER, CityNum.irrelevant, null, new Leader.create(Component.text("Charlotte Adelgonde Elisabeth Marie Wilhelmine")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Netherlands", ColoursEnum.ORANGE.getTextColor()), "Netherlands", Material.ORANGE_CONCRETE, Material.ORANGE_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Hendrikus Colijn")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Norway", ColoursEnum.BROWN.getTextColor()), "Norway", Material.MUD_BRICKS, Material.BROWN_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Johan Nygaardsvold")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Poland", ColoursEnum.MAGENTA.getTextColor()), "Poland", Material.PINK_CONCRETE, Material.PINK_CONCRETE_POWDER, CityNum.almostMajor, null, new Leader.create(Component.text("Ignacy Mościcki")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Portugal", ColoursEnum.LIME.getTextColor()), "Portugal", Material.LIME_WOOL, Material.LIME_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Óscar Carmona")).build(), null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("Romania", ColoursEnum.YELLOW.getTextColor()), "Romania", Material.YELLOW_TERRACOTTA, Material.YELLOW_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Milan Stojadinović")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Spain", ColoursEnum.YELLOW.getTextColor()), "Spain", Material.YELLOW_CONCRETE, Material.YELLOW_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Manuel Portela")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Sweden", ColoursEnum.CYAN.getTextColor()), "Sweden", Material.PRISMARINE_BRICKS, Material.PRISMARINE, CityNum.minor, null, new Leader.create(Component.text("Per Albin Hansson")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral,  Component.text("Switzerland", ColoursEnum.RED.getTextColor()), "Switzerland", Material.RED_CONCRETE, Material.RED_CONCRETE_POWDER, CityNum.almostMajor, null, new Leader.create(Component.text("The People")).setDescription(Component.text("I tried to find something more concrete but switzerland is so confusing but a direct democracy is very good")).build(), new Modifier[]{ModifiersEnum.ww2_neutral.getModifier()}),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("United Kingdom", ColoursEnum.PINK.getTextColor()), "United-Kingdom", Material.PINK_TERRACOTTA, Material.RED_CONCRETE_POWDER, CityNum.major, null, georgeV, null),
                new CountryType(IdeologiesEnum.ww2_socialist, Component.text("Soviet Union", ColoursEnum.RED.getTextColor()), "Soviet-Union", Material.NETHERRACK, Material.RED_TERRACOTTA, CityNum.superPower, null, new Leader.create(Component.text("Stalin")).addModifier(new Modifier.create(Component.text("Purges", NamedTextColor.RED), "purges").addEventsRunner(new Purges()).build()).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Yugoslavia", ColoursEnum.BLUE.getTextColor()), "Yugoslavia", Material.BLUE_CONCRETE_POWDER, Material.BLUE_CONCRETE, CityNum.almostMajor, null, new Leader.create(Component.text("Paul Karađorđević")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Turkey", ColoursEnum.WHITE.getTextColor()), "Turkey", Material.WHITE_TERRACOTTA, Material.WHITE_CONCRETE_POWDER, CityNum.almostMajor, null, new Leader.create(Component.text("Mustafa Kemal Atatürk")).build(), null),
                //Asia
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Afghanistan", ColoursEnum.CYAN.getTextColor()), "Afghanistan", Material.WAXED_WEATHERED_COPPER, Material.OXIDIZED_CUT_COPPER, CityNum.minor, null, new Leader.create(Component.text("Mohammad Zahir Shah")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Bhutan", ColoursEnum.BROWN.getTextColor()), "Bhutan", Material.SPRUCE_WOOD, Material.SPRUCE_LOG, CityNum.minor, null, new Leader.create(Component.text("Jigme Wangchuk")).build(), null),
                new CountryType(IdeologiesEnum.ww2_imperialist, Component.text("British Raj", ColoursEnum.PINK.getTextColor()), "British-Raj", Material.PINK_TERRACOTTA, Material.RED_CONCRETE_POWDER, CityNum.minor, "United-Kingdom", georgeV, null),
                new CountryType(IdeologiesEnum.ww2_nationalist,  Component.text("China", ColoursEnum.WHITE.getTextColor()), "China", Material.WHITE_WOOL, Material.WHITE_TERRACOTTA, CityNum.minor, null, new Leader.create(Component.text("Lin Sen")).build(), null),
                new CountryType(IdeologiesEnum.ww2_imperialist, Component.text("Dutch East Indies", ColoursEnum.ORANGE.getTextColor()), "Dutch-East-Indies", Material.ORANGE_CONCRETE, Material.ORANGE_CONCRETE_POWDER, CityNum.minor, "Netherlands", new Leader.create(Component.text("Bonifacius Cornelis de Jonge")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Persia", ColoursEnum.GRAY.getTextColor()), "Persia", Material.POLISHED_BLACKSTONE, Material.BLACKSTONE, CityNum.minor, null, new Leader.create(Component.text("Reza Shah Pahlavi")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Iraq", ColoursEnum.BROWN.getTextColor()), "Iraq", Material.STRIPPED_JUNGLE_WOOD, Material.LIGHT_GRAY_TERRACOTTA, CityNum.minor, null, new Leader.create(Component.text("Jamil Al Midfai")).build(), null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("Japan", ColoursEnum.ORANGE.getTextColor()), "Japan", Material.ORANGE_TERRACOTTA, Material.ORANGE_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Emperor Shōwa")).build(), null),
                new CountryType(IdeologiesEnum.ww2_imperialist, Component.text("Malaya", ColoursEnum.PINK.getTextColor()), "Malaya", Material.MAGENTA_WOOL, Material.RED_CONCRETE_POWDER, CityNum.minor, "United-Kingdom", georgeV, null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("Manchukuo", ColoursEnum.ORANGE.getTextColor()), "Manchukuo", Material.ORANGE_TERRACOTTA, Material.ORANGE_CONCRETE_POWDER, CityNum.minor, "Japan", new Leader.create(Component.text("Puyi")).build(), null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("Mengkukuo", ColoursEnum.ORANGE.getTextColor()), "Mengkukuo", Material.ORANGE_TERRACOTTA, Material.ORANGE_CONCRETE_POWDER, CityNum.minor, "Japan", new Leader.create(Component.text("Puyi")).build(), null),
                new CountryType(IdeologiesEnum.ww2_socialist,  Component.text("Mongolia", ColoursEnum.CYAN.getTextColor()), "Mongolia", Material.STRIPPED_WARPED_STEM, Material.CYAN_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Anandyn Amar")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Nepal", ColoursEnum.BROWN.getTextColor()), "Nepal", Material.OAK_WOOD, Material.BROWN_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Tribhuvan Bir Bikram Shah Dev")).build(), null),
                new CountryType(IdeologiesEnum.ww2_imperialist, Component.text("Oman", ColoursEnum.PINK.getTextColor()), "Oman", Material.PINK_TERRACOTTA, Material.RED_CONCRETE_POWDER, CityNum.minor, "United-Kingdom", georgeV, null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Philippines", ColoursEnum.BROWN.getTextColor()), "Philippines", Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_MANGROVE_WOOD, CityNum.minor, null, new Leader.create(Component.text("Manuel L. Quezon")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Saudi Arabia", ColoursEnum.LIGHT_GRAY.getTextColor()), "Saudi-Arabia", Material.LIGHT_GRAY_TERRACOTTA, Material.CYAN_CONCRETE_POWDER, CityNum.minor, null, new Leader.create(Component.text("Ibn Saud")).build(), null),
                new CountryType(IdeologiesEnum.ww2_fascist,  Component.text("Siam", ColoursEnum.BLUE.getTextColor()), "Siam", Material.PACKED_ICE, Material.ICE, CityNum.minor, null, new Leader.create(Component.text("Ananda Mahidol")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Tibet", ColoursEnum.GREEN.getTextColor()), "Tibet", Material.BAMBOO_BLOCK, Material.STRIPPED_BAMBOO_BLOCK, CityNum.minor, null, new Leader.create(Component.text("Thubten Jamphel Yeshe Gyaltsen")).build(), null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("Yemen", ColoursEnum.PINK.getTextColor()), "Yemen", Material.PINK_TERRACOTTA, Material.RED_CONCRETE_POWDER, CityNum.minor, "United-Kingdom", new Leader.create(Component.text("Imam Yahya Hamiduddin ")).build(), null),
                //Oceania
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Australia", ColoursEnum.PINK.getTextColor()), "Australia", Material.PINK_TERRACOTTA, Material.RED_CONCRETE_POWDER, CityNum.minor, "United-Kingdom", georgeV, null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("New Zealand", ColoursEnum.PINK.getTextColor()), "New-Zealand", Material.PINK_TERRACOTTA, Material.RED_CONCRETE_POWDER, CityNum.minor, "United-Kingdom", georgeV, null),
                //North America
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Canada", ColoursEnum.PINK.getTextColor()), "Canada", Material.PINK_TERRACOTTA, Material.RED_CONCRETE_POWDER, CityNum.minor, "United-Kingdom", georgeV, null),
                new CountryType(IdeologiesEnum.ww2_neutral, Component.text("United States", NamedTextColor.BLUE), "United-States", Material.BLUE_CONCRETE, Material.BLUE_TERRACOTTA, CityNum.superPower, null, new Leader.create(Component.text("Teddy Roosevelt")).build(), null),
                new CountryType(IdeologiesEnum.ww2_nationalist, Component.text("Mexico", NamedTextColor.RED), "Mexico", Material.RED_CONCRETE, Material.RED_TERRACOTTA, CityNum.minor, null, new Leader.create(Component.text("")).build(), null),
                //South America
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Argentina", ColoursEnum.BLUE.getTextColor()), "Argentina", Material.BLUE_ICE, Material.ICE, CityNum.minor, null, new Leader.create(Component.text("Agustín Pedro Justo")).build(), null),
                new CountryType(IdeologiesEnum.ww2_socialist,  Component.text("Bolivia", ColoursEnum.BROWN.getTextColor()), "Bolivia", Material.MANGROVE_LOG, Material.MANGROVE_WOOD, CityNum.minor, null, new Leader.create(Component.text("José Luis Tejada Sorzano")).build(), null),
                new CountryType(IdeologiesEnum.ww2_nationalist, Component.text("Brazil", ColoursEnum.GREEN.getTextColor()), "Brazil", Material.MELON, Material.GRASS_BLOCK, CityNum.minor, null, new Leader.create(Component.text("Getúlio Vargas")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist, Component.text("Chile", ColoursEnum.PINK.getTextColor()), "Chile", Material.PURPUR_PILLAR, Material.PURPUR_BLOCK, CityNum.minor, null, new Leader.create(Component.text("Arturo Alessandri")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Colombia", ColoursEnum.BROWN.getTextColor()), "Colombia", Material.ACACIA_WOOD, Material.ACACIA_LOG, CityNum.minor, null, new Leader.create(Component.text("Alfonso López Pumarejo")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Ecuador", ColoursEnum.WHITE.getTextColor()), "Ecuador", Material.BIRCH_LOG, Material.BIRCH_WOOD, CityNum.minor, null, new Leader.create(Component.text("Federico Páez")).build(), null),
                new CountryType(IdeologiesEnum.ww2_nationalist,  Component.text("Paraguay", ColoursEnum.BROWN.getTextColor()), "Paraguay", Material.WAXED_OXIDIZED_CUT_COPPER, Material.COPPER_BLOCK, CityNum.minor, null, new Leader.create(Component.text("Eusebio Ayala")).build(), null),
                new CountryType(IdeologiesEnum.ww2_nationalist,  Component.text("Peru", ColoursEnum.GRAY.getTextColor()), "Peru", Material.COBBLESTONE, Material.STONE, CityNum.minor, null, new Leader.create(Component.text("Óscar Raymundo Benavides Larrea")).build(), null),
                new CountryType(IdeologiesEnum.ww2_liberalist, Component.text("Uruguay", ColoursEnum.YELLOW.getTextColor()), "Uruguay", Material.END_STONE, Material.END_STONE_BRICKS, CityNum.minor, null, new Leader.create(Component.text("Gabriel Terra")).build(), null),
                new CountryType(IdeologiesEnum.ww2_conservatist,  Component.text("Venezuela", ColoursEnum.BLACK.getTextColor()), "Venezuela", Material.POLISHED_BASALT, Material.BASALT, CityNum.minor, null, new Leader.create(Component.text("Eleazar López Contreras")).build(), null),
        };
        System.out.println("Country types length : " + countryTypes.length);
        this.countryTypes = Arrays.stream(countryTypes).toList();
        this.countryTypes.forEach(countryType -> {
            gMax.put(countryType.identifier(), countryType.cityNum.g());
            gMax.put(countryType.identifier(), countryType.cityNum.g());
            rgMax.put(countryType.identifier(), countryType.cityNum.rg());
            yMax.put(countryType.identifier(), countryType.cityNum.y());
            lMax.put(countryType.identifier(), countryType.cityNum.l());
            grMax.put(countryType.identifier(), countryType.cityNum.gr());
        });
    }

    @Override
    public void onGenerate(Instance instance, VotingOption votingOption) {
        if (isGenerating(instance)) {
            logMsg("server", "Tried to generate a new map when a map was generating", instance);
            return;
        }

        addGenerating(instance);
        initializeGeneration(instance, votingOption);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            removeGenerating(instance);
            countryDataManager.getCountries().forEach(Country::createInfo);
        }).delay(2, ChronoUnit.SECONDS).schedule();

        MinecraftServer.getSchedulerManager().buildTask(this::start).schedule();
    }

    private void initializeGeneration(Instance instance, VotingOption votingOption) {
        this.votingOption = votingOption;
        this.instance = instance;
        this.provinceManager = ContinentalManagers.world(instance).provinceManager();
        provinceManager.reset();
        this.countryDataManager = ContinentalManagers.world(instance).countryDataManager();
        this.countries = votingOption.getCountries();
        this.currenciesHashMap = new HashMap<>(votingOption.getDefaultCurrencies());
        this.countryNames = new ArrayList<>(countryTypes);
        this.defIdeology = new Ideology(votingOption);
        this.land = new ArrayList<>();
        this.countryHashMap = new HashMap<>();
        this.capitals = new HashMap<>();
    }

    public void start() {
        Random random = new Random();
        JNoise noisePipeline = JNoise.newBuilder()
                .setNoiseSource(PerlinNoiseGenerator.newBuilder()
                        .setSeed(random.nextLong())
                        .setInterpolation(Interpolation.LINEAR)
                        .build())
                .clamp(0, 1)
                .scale(random.nextDouble(0.03, 0.04))
                .build();

        instance.enableAutoChunkLoad(true);

        for (int x = -getSizeX(); x < getSizeX(); x++) {
            for (int y = -getSizeY(); y < getSizeY(); y++) {
                double noiseValue = noisePipeline.evaluateNoise(x, y);
                Pos pos = new Pos(x, 0, y);
                if (noiseValue < 0.01) {
                    instance.setBlock(pos, Material.BLUE_STAINED_GLASS.block());
                } else {
                    land.add(new FlatPos(x, y));
                }
            }
        }

        createCountries();
    }

    private void createCountries() {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < this.countries; i++) {
            Country newCountry = createCountry(i);
            if (newCountry != null) {
                countries.add(newCountry);
                countryDataManager.addCountry(newCountry);
            }
        }

        assignIdeologiesAndOverlords(countries);
        floodFill(countries);
    }

    private void assignIdeologiesAndOverlords(List<Country> countries) {
        Random random = new Random();
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            country.getIdeology().addIdeology(
                    votingOption.getIdeologyTypes().get(random.nextInt(votingOption.getIdeologyTypes().size())),
                    random.nextFloat(0, 15.0f)
            );

            String overlord = countryNames.get(i).overlord();
            if (overlord != null) {
                Country overlordCountry = countryDataManager.getCountryFromName(overlord);
                country.setOverlord(overlordCountry);
                overlordCountry.addPuppet(country);
            }
        }
    }

    private Country createCountry(int count) {
        if (countryNames.isEmpty()) {
            System.err.println("Not enough countries, exiting early");
            return null;
        }

        CountryType countryName = countryNames.get(count);
        HashMap<CurrencyTypes, Currencies> newCurrencies = cloneCurrencies();

        Country country = switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
            case ww2_clicks -> new ClicksCountry(newCurrencies, countryName.identifier(), countryName.name(), countryName.block(), countryName.border(), defIdeology, instance, laws);
            case ww2_troops -> new TroopCountry(newCurrencies, countryName.identifier(), countryName.name(), countryName.block(), countryName.border(), defIdeology, instance, laws);
            default -> throw new IllegalArgumentException("Unexpected voting winner");
        };

        country.getIdeology().setCurrentIdeology(countryName.ideologiesEnum().getIdeologyTypes());
        country.getIdeology().addIdeology(countryName.ideologiesEnum().getIdeologyTypes(), new Random().nextFloat(40.0f, 80.0f));
        country.getEconomy().getVault().setCountry(country);

        Leader leader = countryName.leader();
        leader.setIdeologyTypes(countryName.ideologiesEnum());
        country.setLeader(leader);

        if (countryName.extra() != null) {
            Arrays.stream(countryName.extra()).forEach(country::addModifier);
        }

        if (countryName.overlord() != null) {
            country.setOverlord(countryDataManager.getCountryFromName(countryName.overlord()));
        }

        if (!"Soviet-Union".equals(countryName.identifier)) {
            Modifier m = ModifiersEnum.great_depression.getModifier().independantClone();
            m.addEventsRunner(new GreatDepressionEventsRunner(country, m));
            country.addModifier(m);
        }

        return country;
    }

    private HashMap<CurrencyTypes, Currencies> cloneCurrencies() {
        HashMap<CurrencyTypes, Currencies> clonedCurrencies = new HashMap<>();
        for (var entry : currenciesHashMap.entrySet()) {
            clonedCurrencies.put(entry.getKey(), entry.getValue().clone());
        }
        return clonedCurrencies;
    }

    public void floodFill(List<Country> countries) {
        Queue<FlatPos>[] countryQueues = new Queue[countries.size()];

        for (int i = 0; i < countries.size(); i++) {
            countryQueues[i] = new LinkedList<>();
            startCountry(i, countryQueues, countries);
        }

        boolean anyQueueHadExpansion;
        do {
            anyQueueHadExpansion = false;
            for (int i = 0; i < countries.size(); i++) {
                if (!countryQueues[i].isEmpty()) {
                    FlatPos currentPos = countryQueues[i].poll();
                    for (FlatPos neighbor : getNeighbours(currentPos)) {
                        if (land.contains(neighbor) && !countryHashMap.containsKey(neighbor)) {
                            countryQueues[i].add(neighbor);
                            countryHashMap.put(neighbor, countries.get(i));
                            anyQueueHadExpansion = true;
                            land.remove(neighbor);
                        }
                    }
                }
            }
        } while (anyQueueHadExpansion);

        land.forEach(z -> instance.setBlock(new Pos(z.x(), 0, z.z()), Material.WHITE_TERRACOTTA.block()));

        borders(countries);
    }

    private void startCountry(int i, Queue<FlatPos>[] countryQueues, List<Country> countries) {
        while (true) {
            countryQueues[i] = new LinkedList<>();
            final FlatPos seedPos = this.chooseStartingPos();
            countryQueues[i].add(seedPos);
            if (!this.countryHashMap.containsKey(seedPos)) {
                this.land.remove(seedPos);
                this.countryHashMap.put(seedPos, countries.get(i));
                this.capitals.put(seedPos, countries.get(i));
            } else continue;
            return;
        }
    }

    private void borders(List<Country> countries) {
        countryHashMap.forEach((flatPos, country) -> {
            Province province = new Province(new Pos(flatPos.x(), 0, flatPos.z()), instance, country, new ArrayList<>());
            province.initialOccupier(country);
            province.setCore(country);
            provinceManager.registerProvince(flatPos.x(), flatPos.z(), province);
        });
        countryHashMap.forEach((flatPos, country) -> {
            List<FlatPos> f = getNeighbours(flatPos);
            List<Province> p = new ArrayList<>();
            f.forEach(flatPos1 -> {
                Province province = ContinentalManagers.world(instance).provinceManager().getProvince(flatPos1);
                if (null == province) return;
                p.add(province);
            });
            Province province = provinceManager.getProvince(flatPos);
            province.setNeighbours(p);
            check(province);
        });
        capitals.forEach((flatPos, country) -> {
            Province province = provinceManager.getProvince(flatPos);
            province.setCity(6);
            country.setCapital(province);
            country.getMilitary().addMajorCityBlock(province, Material.EMERALD_BLOCK);
        });
        generateCities(countries);
    }

    private void check(Province province) {
        Country country = province.getOccupier();
        for (Province neighbour : province.getNeighbours()) {
            if (neighbour.getOccupier() != country) {
                province.setBorder();
                province.getNeighbours().forEach(province1 -> {
                    if (province1.getOccupier() != country) {
                        province1.getOccupier().addBorder(province, country.getName());
                    }
                });
                return;
            }
        }
        province.setBlock();
    }

    private List<FlatPos> getNeighbours(FlatPos e) {
        List<FlatPos> f = new ArrayList<>();
        for (int[] i : directions) {
            f.add(new FlatPos(e.x() + i[0], e.z() + i[2]));
        }
        return f;
    }

    private FlatPos chooseStartingPos() {
        return land.get(new Random().nextInt(land.size()));
    }

    private void generateCities(List<Country> countries) {
        switch (ContinentalManagers.world(instance).dataStorer().votingWinner) {
            case ww2_clicks -> {
                for (Country c : countries) {
                    ClicksCountry country = (ClicksCountry) c;
                    cityTypeGen(80, 5, gMax.get(country.getName()), country);
                    cityTypeGen(90, 4, rgMax.get(country.getName()), country);
                    cityTypeGen(100, 3, yMax.get(country.getName()), country);
                    cityTypeGen(110, 2, lMax.get(country.getName()), country);
                    cityTypeGen(120, 1, grMax.get(country.getName()), country);
                    country.calculateCapitulationPercentage();
                }
            }
            case ww2_troops -> {
                for (Country c : countries) {
                    TroopCountry country = (TroopCountry) c;
                    cityTypeGen(80, 5, gMax.get(country.getName()), country);
                    cityTypeGen(90, 4, rgMax.get(country.getName()), country);
                    cityTypeGen(100, 3, yMax.get(country.getName()), country);
                    cityTypeGen(110, 2, lMax.get(country.getName()), country);
                    cityTypeGen(120, 1, grMax.get(country.getName()), country);
                    country.calculateCapitulationPercentage();
                }
            }
            default ->
                    throw new IllegalArgumentException("Unexpected value: " + ContinentalManagers.world(instance).dataStorer().votingWinner);
        }
        finalLoop(countries);
    }

    private void cityTypeGen(int amount, int index, int max, Country country) {
        int t = country.getMilitary().getOccupies().size() / amount;
        if (!(1 > t)) for (int i = 0; i < t; i++) {
            if (t > max) break;
            Province p = country.getMilitary().getOccupies().get(new Random().nextInt(country.getMilitary().getOccupies().size()));
            if (p.isCity()) {
                i--;
            } else {
                p.setCity(index);
                country.getMilitary().addMajorCityBlock(p, p.getMaterial());
            }
        }
    }

    private void finalLoop(List<Country> countries) {
        AIManager aiManager = ContinentalManagers.centralAIManager.getAIManagerFor(instance);
        countries.forEach(country -> {
            country.init();
            //aiManager.createAIForCountry(country);
        });
    }

    private enum CityNum {
        superPower(4, 3, 2, 2, 2),
        major(3, 3, 2, 2, 4),
        almostMajor(2, 2, 2, 3, 2),
        minor(1, 1, 1, 1, 3),
        irrelevant(1, 0, 0, 1, 2);

        private final int g;
        private final int rg;
        private final int y;
        private final int l;
        private final int gr;

        CityNum(int g, int rg, int y, int l, int gr) {
            this.g = g;
            this.rg = rg;
            this.y = y;
            this.l = l;
            this.gr = gr;
        }

        public int g() {
            return g;
        }

        public int rg() {
            return rg;
        }

        public int y() {
            return y;
        }

        public int l() {
            return l;
        }

        public int gr() {
            return gr;
        }
    }

    private record CountryType(IdeologiesEnum ideologiesEnum, Component name,
                               String identifier, Material block, Material border, CityNum cityNum, String overlord,
                               Leader leader, Modifier[] extra) {
    }
}
