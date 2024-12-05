package org.drachens.dataClasses.Research;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.events.research.ResearchStartEvent;
import org.drachens.temporary.research.ResearchCountry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.drachens.util.InventoryUtil.addExitButton;
import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.KyoriUtil.compBuild;

public class ResearchGUI extends InventoryGUI {
    int start;
    public ResearchGUI(int i){
        start=i;
    }
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW,"Research");
    }

    @Override
    public void decorate(@NotNull CPlayer p) {
        TechTree tree = ContinentalManagers.world(p.getInstance()).dataStorer().votingOption.getTree();
        int max = p.getInventory().getSize();
        for (Map.Entry<Integer[], ResearchOption> e : tree.getResearchOptionMap().entrySet()){
            int x = e.getKey()[1];
            if (x<start || x>=8+start)continue;
            int coords = e.getKey()[0]*9+x-start;
            if (coords>max){
                continue;
            }
            ResearchOption researchOption = e.getValue();
            addButton(coords,apple(researchOption.getItem(), researchOption,researchOption.getIdentifier(),researchOption.getRequires(),researchOption.getOr()));
        }
        if (start>=0) addButton(36,prev(itemBuilder(Material.GREEN_CONCRETE,compBuild("Previous",NamedTextColor.GREEN))));
        addButton(44,next(itemBuilder(Material.GREEN_CONCRETE,compBuild("Next",NamedTextColor.GREEN))));
        addExitButton(this);
        super.decorate(p);
    }

    private InventoryButton next(ItemStack i){
        return new InventoryButton()
                .creator(player -> i)
                .consumer(e -> {
                    CPlayer p = (CPlayer) e.getPlayer();
                    ContinentalManagers.guiManager.openGUI(new ResearchGUI(start+=3),p);
                });
    }
    private InventoryButton prev(ItemStack i){
        return new InventoryButton()
                .creator(player -> i)
                .consumer(e -> {
                    CPlayer p = (CPlayer) e.getPlayer();
                    ContinentalManagers.guiManager.openGUI(new ResearchGUI(start-=3),p);
                });
    }

    private InventoryButton apple(ItemStack i, ResearchOption researchOption, String identifier, List<String> prev, List<String> or) {
        return new InventoryButton()
                .creator(player -> {
                    ResearchCountry country = (ResearchCountry) player.getCountry();
                    List<Component> description = researchOption.createLore(country);
                    if (country.isResearching()){
                        if (Objects.equals(country.getCurrentResearch().getIdentifier(), identifier)){
                            description.add(compBuild("Already researching this", NamedTextColor.RED));
                        }else {
                            description.add(compBuild("Already researching", NamedTextColor.RED));
                        }
                    } else if (!country.hasResearchedAll(prev)) {
                        description.add(compBuild("Has not researched all the prequisites", NamedTextColor.GREEN));
                    } else if (country.hasResearchedAny(or)) {
                        description.add(compBuild("Has not researched one of the or's", NamedTextColor.GREEN));
                    } else if (country.hasResearched(identifier)) {
                        description.add(compBuild("Has already been researched", NamedTextColor.GREEN));
                    } else {
                        description.add(compBuild("Can research", NamedTextColor.GREEN));
                    }
                    return i.withLore(description);
                })
                .consumer(e -> {
                    CPlayer player = (CPlayer) e.getPlayer();
                    ResearchCountry country = (ResearchCountry) player.getCountry();
                    if (country.isResearching()||!country.hasResearchedAll(prev)||country.hasResearchedAny(or)||country.hasResearched(identifier))return;
                    country.setCurrentResearch(researchOption);
                    EventDispatcher.call(new ResearchStartEvent(player.getInstance(),country,researchOption));
                });
    }
}
