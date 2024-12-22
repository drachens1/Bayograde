package org.drachens.dataClasses.Research;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.temporary.research.ResearchCountry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.drachens.util.InventoryUtil.addExitButton;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ResearchGUI extends InventoryGUI {
    private final int startX;
    private final int startY;

    public ResearchGUI(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, "Research");
    }

    @Override
    public void decorate(@NotNull CPlayer p) {
        TechTree tree = ContinentalManagers.world(p.getInstance()).dataStorer().votingOption.getTree();
        int max = p.getInventory().getSize();
        for (Map.Entry<Integer[], ResearchOption> e : tree.getResearchOptionMap().entrySet()) {
            int x = e.getKey()[1];
            if (x < startX || x >= 8 + startX) continue;
            int y = e.getKey()[0];
            if (y < startY || y >= 8 + startY) continue;
            int coords = (y - startY) * 9 + x - startX;
            if (coords > max) {
                continue;
            }
            ResearchOption researchOption = e.getValue();
            addButton(coords, apple(researchOption.getItem(), researchOption, researchOption.getIdentifier(), researchOption.getRequires(), researchOption.getOr()));
        }
        if (startX >= 0)
            addButton(36, prevX(itemBuilder(Material.GREEN_CONCRETE, Component.text("Previous", NamedTextColor.GREEN))));
        if (startY >= 0)
            addButton(5, prevY(itemBuilder(Material.GREEN_CONCRETE, Component.text("Previous", NamedTextColor.GREEN))));
        addButton(44, nextX(itemBuilder(Material.GREEN_CONCRETE, Component.text("Next", NamedTextColor.GREEN))));
        addButton(6, nextY(itemBuilder(Material.GREEN_CONCRETE, Component.text("Next", NamedTextColor.GREEN))));
        addExitButton(this);
        super.decorate(p);
    }

    private InventoryButton nextX(ItemStack i) {
        return new InventoryButton()
                .creator(player -> i)
                .consumer(e -> {
                    CPlayer p = (CPlayer) e.getPlayer();
                    ContinentalManagers.guiManager.openGUI(new ResearchGUI(startX + 3, startY), p);
                });
    }

    private InventoryButton prevX(ItemStack i) {
        return new InventoryButton()
                .creator(player -> i)
                .consumer(e -> {
                    CPlayer p = (CPlayer) e.getPlayer();
                    ContinentalManagers.guiManager.openGUI(new ResearchGUI(startX - 3, startY), p);
                });
    }

    private InventoryButton nextY(ItemStack i) {
        return new InventoryButton()
                .creator(player -> i)
                .consumer(e -> {
                    CPlayer p = (CPlayer) e.getPlayer();
                    ContinentalManagers.guiManager.openGUI(new ResearchGUI(startX, startY + 3), p);
                });
    }

    private InventoryButton prevY(ItemStack i) {
        return new InventoryButton()
                .creator(player -> i)
                .consumer(e -> {
                    CPlayer p = (CPlayer) e.getPlayer();
                    ContinentalManagers.guiManager.openGUI(new ResearchGUI(startX, startY - 3), p);
                });
    }

    private InventoryButton apple(ItemStack i, ResearchOption researchOption, String identifier, List<String> prev, List<String> or) {
        return new InventoryButton()
                .creator(player -> {
                    ResearchCountry country = (ResearchCountry) player.getCountry();
                    List<Component> description = researchOption.createLore(country);
                    if (country.isResearching()) {
                        if (Objects.equals(country.getCurrentResearch().getIdentifier(), identifier)) {
                            description.add(Component.text("Already researching this", NamedTextColor.RED));
                        } else {
                            description.add(Component.text("Already researching", NamedTextColor.RED));
                        }
                    } else if (!country.hasResearchedAll(prev)) {
                        description.add(Component.text("Has not researched all the prequisites", NamedTextColor.GREEN));
                    } else if (country.hasResearchedAny(or)) {
                        description.add(Component.text("Has not researched one of the or's", NamedTextColor.GREEN));
                    } else if (country.hasResearched(identifier)) {
                        description.add(Component.text("Has already been researched", NamedTextColor.GREEN));
                    } else {
                        description.add(Component.text("Can research", NamedTextColor.GREEN));
                    }
                    return i.withLore(description);
                })
                .consumer(e -> {
                    CPlayer player = (CPlayer) e.getPlayer();
                    ResearchCountry country = (ResearchCountry) player.getCountry();
                    if (country.isResearching() || !country.hasResearchedAll(prev) || country.hasResearchedAny(or) || country.hasResearched(identifier))
                        return;
                    country.startResearching(researchOption);
                });
    }
}
