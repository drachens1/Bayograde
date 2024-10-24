package org.drachens.dataClasses.Economics.factory;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.animation.Animation;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.dataClasses.other.TextDisplay;

import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.KyoriUtil.compBuild;

public class PlaceableFactory implements PlaceableBuilds {
    private final ItemDisplay itemDisplay;
    private final TextDisplay textDisplay;
    private final FactoryType factoryType;
    private final Province province;
    private final int max;
    private final Country occupier;
    private int current;
    int[] constructionFrames = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
    int[] smokeFrames = {31,32,33};
    private final Animation constructionAnimation = new Animation(1, Material.CYAN_DYE,constructionFrames);
    private Animation smokeAnimation = new Animation(1, Material.CYAN_DYE,smokeFrames);
    public PlaceableFactory(FactoryType factoryType, Province province) {
        this.factoryType = factoryType;
        this.province = province;
        this.current = 1;
        this.occupier = province.getOccupier();
        province.setBuildType(this);
        occupier.addPlaceableFactory(this);
        Pos pos = province.getPos().add(0.5, 1.5, 0.5);
        System.out.println(current);
        itemDisplay = new ItemDisplay(itemBuilder(factoryType.getItem(), factoryType.getModelData()[current - 1]), pos, ItemDisplay.DisplayType.GROUND, province.getInstance(), true);
        textDisplay = new TextDisplay(pos.add(0, 1, 0), province.getInstance(), compBuild(current + "", NamedTextColor.BLUE), 0, 0, "a".getBytes()[0], "b".getBytes()[0], true);
        for (Player p : province.getInstance().getPlayers()) {
            itemDisplay.addViewer(p);
            textDisplay.addViewer(p);
        }
        this.max = Math.round(factoryType.getMax(province.getMaterial())*province.getOccupier().getMaxBoost());
        smokeAnimation.start(itemDisplay,true);
    }

    private void updateDisplay() {
        itemDisplay.setItem(itemBuilder(factoryType.getItem(), factoryType.getModelData()[current - 1]));
        textDisplay.setText(compBuild(current + "", NamedTextColor.BLUE));
    }

    public boolean canAddFactory(int add) {
        return current + add <= max*province.getOccupier().getMaxBoost();
    }

    @Override
    public void onCaptured(Country capturer) {
        occupier.removePlaceableFactory(this);
        capturer.addPlaceableFactory(this);
    }

    @Override
    public void onBombed(float dmg) {

    }

    @Override
    public void onDestroy() {
        itemDisplay.delete();
        province.setBuildType(null);
        occupier.removePlaceableFactory(this);
    }

    @Override
    public void onUpgrade(int amount) {
        if (!canAddFactory(amount)) {
            return;
        }
        current += amount;
        if (current <= 0) {
            itemDisplay.delete();
            textDisplay.dispose();
        }
        updateDisplay();
    }

    public int lvl() {
        return current;
    }

    public FactoryType getFactoryType() {
        return factoryType;
    }

    public HashMap<CurrencyTypes, Float> onGenerate() {
        HashMap<CurrencyTypes, Float> currencyTypes = new HashMap<>();
        for (int i = 0; i < factoryType.getCurrency().size(); i++)
            currencyTypes.put(factoryType.getCurrency().get(i), factoryType.getProduction().get(i));

        return currencyTypes;
    }
}
