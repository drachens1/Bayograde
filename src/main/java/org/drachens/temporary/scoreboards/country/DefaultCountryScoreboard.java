package org.drachens.temporary.scoreboards.country;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.scoreboard.Sidebar;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardBuilder;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Economics.currency.Currencies;

import java.util.List;
import java.util.Map;

public class DefaultCountryScoreboard extends ContinentalScoreboards {
    private boolean generalInfo = true;
    private boolean economy = false;
    private boolean ideologies = false;
    private boolean diplomacy = false;

    @Override
    protected Sidebar createSidebar(CPlayer p) {
        Country country = p.getCountry();
        return new ScoreboardBuilder(Component.text("Country", NamedTextColor.GOLD, TextDecoration.BOLD))
                .addLine("generalInfo", Component.text("0-General-Info--v", TextColor.color(65, 131, 237)), 50)
                .addLine("generalInfo1", Component.text("Country : " + country.getName(), NamedTextColor.BLUE), 49)
                .addLine("generalInfo2", Component.text("Stability : " + country.getStability().getStability(), NamedTextColor.BLUE), 50)
                .addLine("economy", Component.text("1-Economy--^", TextColor.color(65, 131, 237)), 40)
                .addLine("ideologies", Component.text("2-Ideologies--^", TextColor.color(65, 131, 237)), 30)
                .addLine("diplomacy", Component.text("3-Diplomacy--^", TextColor.color(65, 131, 237)), 20)
                .build();
    }

    public void openGeneralInfo() {
        generalInfo = true;
        CPlayer p = getPlayer();
        Sidebar sidebar = getSidebar();
        Country country = p.getCountry();
        sidebar.updateLineContent("generalInfo", Component.text("0-General-Info--v", TextColor.color(65, 131, 237)));
        sidebar.createLine(new Sidebar.ScoreboardLine("generalInfo1", Component.text("Country : " + country.getName(), NamedTextColor.BLUE), 49));
        sidebar.createLine(new Sidebar.ScoreboardLine("generalInfo2", Component.text("Stability : " + country.getStability().getStability(), NamedTextColor.BLUE), 50));
    }

    public void closeGeneralInfo() {
        generalInfo = false;
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("generalInfo", Component.text("0-General-Info--^", TextColor.color(65, 131, 237)));
        sidebar.removeLine("generalInfo1");
        sidebar.removeLine("generalInfo2");
    }

    public void openEconomy() {
        economy = true;
        CPlayer p = getPlayer();
        Country country = p.getCountry();
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("economy", Component.text("1-Economy--v", TextColor.color(65, 131, 237)));
        List<Currencies> currenciesList = country.getVault().getCurrencies();
        int i = 39;
        for (Currencies currencies : currenciesList) {
            sidebar.createLine(new Sidebar.ScoreboardLine("economy" + i, Component.text()
                    .append(Component.text(currencies.getAmount()))
                    .append(currencies.getCurrencyType().getSymbol())
                    .build(), i));
            i--;
        }
    }

    public void closeEconomy() {
        economy = false;
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("economy", Component.text("1-Economy--^", TextColor.color(65, 131, 237)));
        for (int i = 39; i > 20; i--) {
            sidebar.removeLine("economy" + i);
        }
    }

    public void openIdeologies() {
        ideologies = true;
        CPlayer p = getPlayer();
        Country country = p.getCountry();
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("ideologies", Component.text("2-Ideologies--v", TextColor.color(65, 131, 237)));
        int i = 29;
        for (Map.Entry<IdeologyTypes, Float> e : country.getIdeology().getIdeologies().entrySet()) {
            float amount = e.getValue();
            if (amount < 1f) continue;
            IdeologyTypes ideologyTypes = e.getKey();
            sidebar.createLine(new Sidebar.ScoreboardLine("ideology" + i, Component.text()
                    .append(Component.text(Math.round(amount)))
                    .append(ideologyTypes.getPrefix())
                    .build(), i));
            i--;
        }
    }

    public void closeIdeologies() {
        ideologies = false;
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("ideologies", Component.text("2-Ideologies--^", TextColor.color(65, 131, 237)));
        for (int i = 29; i > 10; i--) {
            sidebar.removeLine("ideology" + i);
        }
    }

    public void openDiplomacy() {
        diplomacy = true;
        CPlayer p = getPlayer();
        Country country = p.getCountry();
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("diplomacy", Component.text("3-Diplomacy--v", TextColor.color(65, 131, 237)));
        Map<Country, Float> pos = country.getRelations().getPositiveRelations();
        Map<Country, Float> neg = country.getRelations().getNegativeRelations();
        int i = 19;
        for (Map.Entry<Country, Float> e : pos.entrySet()) {
            sidebar.createLine(new Sidebar.ScoreboardLine("diplomacy" + i, Component.text()
                    .append(e.getKey().getNameComponent())
                    .append(Component.text(" +" + e.getValue(), NamedTextColor.GREEN))
                    .build(), i));
        }
        for (Map.Entry<Country, Float> e : neg.entrySet()) {
            sidebar.createLine(new Sidebar.ScoreboardLine("diplomacy" + i, Component.text()
                    .append(e.getKey().getNameComponent())
                    .append(Component.text(" " + e.getValue(), NamedTextColor.GREEN))
                    .build(), i));
        }
    }

    public void closeDiplomacy() {
        diplomacy = false;
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("diplomacy", Component.text("3-Diplomacy--^", TextColor.color(65, 131, 237)));
        for (int i = 19; i > 0; i--) {
            sidebar.removeLine("diplomacy" + i);
        }
    }

    public void updateAll() {
        updateGeneralInfo();
        updateDiplomacy();
        updateIdeologies();
        updateEconomy();
    }

    public void updateGeneralInfo() {
        if (!generalInfo) return;
        CPlayer p = getPlayer();
        Sidebar sidebar = getSidebar();
        Country country = p.getCountry();
        sidebar.updateLineContent("generalInfo2", Component.text("Stability : " + country.getStability().getStability(), NamedTextColor.BLUE));
    }

    public void updateDiplomacy() {
        if (!diplomacy) return;
        CPlayer p = getPlayer();
        Sidebar sidebar = getSidebar();
        Country country = p.getCountry();
        Map<Country, Float> pos = country.getRelations().getPositiveRelations();
        Map<Country, Float> neg = country.getRelations().getNegativeRelations();
        int i = 19;
        for (Map.Entry<Country, Float> e : pos.entrySet()) {
            updateLine(sidebar, "diplomacy" + i, Component.text()
                    .append(e.getKey().getNameComponent())
                    .append(Component.text(" +" + e.getValue(), NamedTextColor.GREEN))
                    .build(), i);
        }
        for (Map.Entry<Country, Float> e : neg.entrySet()) {
            updateLine(sidebar, "diplomacy" + i, Component.text()
                    .append(e.getKey().getNameComponent())
                    .append(Component.text(" " + e.getValue(), NamedTextColor.GREEN))
                    .build(), i);
        }
    }

    public void updateEconomy() {
        if (!economy) return;
        CPlayer p = getPlayer();
        Sidebar sidebar = getSidebar();
        Country country = p.getCountry();
        List<Currencies> currenciesList = country.getVault().getCurrencies();
        int i = 39;
        for (Currencies currencies : currenciesList) {
            updateLine(sidebar, "economy" + i, Component.text()
                    .append(Component.text(currencies.getAmount()))
                    .append(currencies.getCurrencyType().getSymbol())
                    .build(), i);
            i--;
        }
    }

    public void updateIdeologies() {
        if (!ideologies) return;
        CPlayer p = getPlayer();
        Sidebar sidebar = getSidebar();
        Country country = p.getCountry();
        int i = 29;
        for (Map.Entry<IdeologyTypes, Float> e : country.getIdeology().getIdeologies().entrySet()) {
            float amount = e.getValue();
            if (amount < 5f) continue;
            IdeologyTypes ideologyTypes = e.getKey();
            updateLine(sidebar, "ideology" + i, Component.text()
                    .append(Component.text(Math.round(amount)))
                    .append(ideologyTypes.getPrefix())
                    .build(), i);
            i--;
        }
    }

    private void updateLine(Sidebar sidebar, String id, Component content, int lineNum) {
        if (sidebar.getLine(id) == null) {
            sidebar.createLine(new Sidebar.ScoreboardLine(id, content, lineNum));
        } else {
            sidebar.updateLineContent(id, content);
        }
    }
}
