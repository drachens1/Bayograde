package org.drachens.dataClasses.Research.tree;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Modifier;
import org.drachens.temporary.research.ResearchCountry;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class ResearchOption {
    private final String identifier;
    private final List<String> requiresString;
    private final List<String> orString;
    private final ItemStack item;
    private final Modifier modifier;
    private final int[] comparedToLast; //[1]=x [0]=y
    private final Payment cost;
    private final List<Component> description;
    private ResearchCategory researchCategory;
    protected ResearchOption(Create create){
        identifier = create.identifier;
        requiresString = create.requires;
        item = create.item;
        modifier = create.modifier;
        comparedToLast = create.comparedToLast;
        orString = create.or;
        cost = create.cost;
        description=create.description;
    }

    public Payment getCost(){
        return cost.clone();
    }

    public Modifier getModifier(){
        return modifier;
    }

    public List<String> getOr(){
        return orString;
    }

    public List<String> getRequires(){
        return requiresString;
    }

    public ItemStack getItem(){
        return item;
    }

    public int[] getComparedToLast(){
        return comparedToLast;
    }

    public String getIdentifier(){
        return identifier;
    }

    public void setResearchCategory(ResearchCategory researchCategory){
        this.researchCategory=researchCategory;
    }

    public List<Component> createLore(ResearchCountry country){
        List<Component> base = new ArrayList<>();
        base.add(Component.text()
                .append(Component.text(cost.getAmount()))
                .append(cost.getCurrencyType().getSymbol())
                .build());
        if (!requiresString.isEmpty()){
            base.add(compBuild("Prequisites: ", NamedTextColor.BLUE));
            requiresString.forEach(require->{
                if (country.hasResearched(require)){
                    base.add(compBuild("- "+require,NamedTextColor.GREEN));
                }else {
                    base.add(compBuild("- "+require,NamedTextColor.RED));
                }
            });
        }
        if (!orString.isEmpty()){
            base.add(compBuild("Or: ", NamedTextColor.BLUE));
            orString.forEach(or->{
                if (country.hasResearched(or)){
                    base.add(compBuild("- "+or,NamedTextColor.RED));
                }else {
                    base.add(compBuild("- "+or,NamedTextColor.GREEN));
                }
            });
        }
        if (description!=null)
            base.addAll(description);
        if (researchCategory!=null)
            base.add(researchCategory.getType());
        return base;
    }

    public static class Create{
        private List<Component> description;
        private final String identifier;
        private final ItemStack item;
        private final List<String> requires = new ArrayList<>();
        private final List<String> or = new ArrayList<>();
        private final Payment cost;
        private Modifier modifier;
        private int[] comparedToLast;
        public Create(String identifier, ItemStack item, float cost){
            this.identifier=identifier;
            this.item=item;
            this.cost=new Payment(CurrencyEnum.research,cost);
        }
        public Create addRequires(String identifier){
            requires.add(identifier);
            return this;
        }
        public Create addOr(String identifier){
            or.add(identifier);
            return this;
        }
        public Create setModifier(Modifier modifier){
            this.modifier = modifier;
            return this;
        }
        public Create setComparedToLast(int y, int x){
            this.comparedToLast=new int[]{y,x};
            return this;
        }
        public Create setDescription(List<Component> description){
            this.description = description;
            return this;
        }
        public ResearchOption build(){
            return new ResearchOption(this);
        }
    }
}
