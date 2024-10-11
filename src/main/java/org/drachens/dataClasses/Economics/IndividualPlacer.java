package org.drachens.dataClasses.Economics;

import net.minestom.server.item.ItemStack;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;

import java.util.ArrayList;
import java.util.List;

public class IndividualPlacer {
    private final ItemStack item;
    private PlaceableFactory builds;
    private List<Payment> payment;
    private IndividualPlacer(create create){
        this.item = create.item;
        if (create.builds!=null)this.builds = create.builds;
        if (create.costs!=null)this.payment = create.costs;
    }
    public ItemStack getItem(){
        return item;
    }
    public PlaceableFactory getBuilds(){
        return builds;
    }
    public List<Payment> getPayment(){
        return payment;
    }
    public static class create{
        ItemStack item;
        PlaceableFactory builds;
        List<Payment> costs = new ArrayList<>();
        public create create(ItemStack item){
            this.item = item;
            return this;
        }
        public create addPayment(Payment payment){
            costs.add(payment);
            return this;
        }
        public create setBuilds(PlaceableFactory builds){
            this.builds = builds;
            return this;
        }
        public IndividualPlacer build(){
            return new IndividualPlacer(this);
        }
    }
}
