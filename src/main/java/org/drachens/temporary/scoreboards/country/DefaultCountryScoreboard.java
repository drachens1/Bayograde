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

import static org.drachens.util.KyoriUtil.compBuild;

public class DefaultCountryScoreboard extends ContinentalScoreboards {
    @Override
    protected Sidebar createSidebar(CPlayer p) {
        return new ScoreboardBuilder.builder(compBuild("Country",NamedTextColor.GOLD, TextDecoration.BOLD))
                .addLine("generalInfo",compBuild("0-General-Info--v", TextColor.color(65,131,237)),60)
                .addLine("country1",compBuild("Country : "+p.getCountry().getName(),NamedTextColor.BLUE),50)
                .addLine("economy",compBuild("0-Economy--^",TextColor.color(65,131,237)),40)
                .addLine("ideologies",compBuild("0-Ideologies--^",TextColor.color(65,131,237)),30)
                .addLine("diplomacy",compBuild("0-Diplomacy--^",TextColor.color(65,131,237)),20)
                .build();
    }

    public void openEconomy(){
        CPlayer p = getPlayer();
        Country country = p.getCountry();
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("economy",compBuild("0-Economy--v",TextColor.color(65,131,237)));
        List<Currencies> currenciesList = country.getVault().getCurrencies();
        int i = 29;
        for (Currencies currencies : currenciesList){
            sidebar.createLine(new Sidebar.ScoreboardLine("economy"+ i, Component.text()
                    .append(Component.text(currencies.getAmount()))
                    .append(currencies.getCurrencyType().getSymbol())
                    .build(),i));
            i--;
        }
    }
    public void closeEconomy(){
        Sidebar sidebar = getSidebar();
        sidebar.updateLineContent("economy",compBuild("0-Economy--^",TextColor.color(65,131,237)));
        for (int i = 29; i > 20; i--){
            sidebar.removeLine("economy"+i);
        }
    }
    public void openIdeologies(){
        CPlayer p = getPlayer();
        Country country = p.getCountry();
        Sidebar sidebar = getSidebar();
        int i = 29;
        for (Map.Entry<IdeologyTypes,Float> e : country.getIdeology().getIdeologies().entrySet()){
            float amount = e.getValue();
            if (amount<5f)continue;
            IdeologyTypes ideologyTypes = e.getKey();
            sidebar.createLine(new Sidebar.ScoreboardLine("ideology"+i,Component.text()
                    .append(Component.text(amount))
                    .append(ideologyTypes.getPrefix())
                    .build(),i));
            i--;
        }
    }
    public void closeIdeologies(){
        Sidebar sidebar = getSidebar();
        for (int i = 29; i > 10; i--){
            sidebar.removeLine("ideology"+i);
        }
    }
    public void openDiplomacy(){
        CPlayer p = getPlayer();
        Country country = p.getCountry();
        Sidebar sidebar = getSidebar();
        Map<Country, Float> pos = country.getRelations().getPositiveRelations();
        Map<Country, Float> neg = country.getRelations().getNegativeRelations();
        int i = 19;
        for (Map.Entry<Country,Float> e : pos.entrySet()){
            sidebar.createLine(new Sidebar.ScoreboardLine("diplomacy"+i,Component.text()
                    .append(e.getKey().getNameComponent())
                    .append(Component.text(" +"+e.getValue(),NamedTextColor.GREEN))
                    .build(),i));
        }
        for (Map.Entry<Country,Float> e : neg.entrySet()){
            sidebar.createLine(new Sidebar.ScoreboardLine("diplomacy"+i,Component.text()
                    .append(e.getKey().getNameComponent())
                    .append(Component.text(" "+e.getValue(),NamedTextColor.GREEN))
                    .build(),i));
        }
    }
    public void closeDiplomacy(){
        Sidebar sidebar = getSidebar();
        for (int i = 19; i > 0; i--){
            sidebar.removeLine("diplomacy"+i);
        }
    }
}
