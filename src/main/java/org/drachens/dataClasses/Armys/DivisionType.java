package org.drachens.dataClasses.Armys;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.ItemStack;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.interfaces.Saveable;

@Getter
@Setter
public class DivisionType implements Saveable {
    private String name;
    private float hp;
    private float atk;
    private float def;
    private float org;
    private float speed;
    private final float trainingTime;
    private Payment payment;
    private final ItemStack icon;

    public DivisionType(String name, float atk, float def, float org, float hp, float speed, float trainingTime, Payment payment, ItemStack icon) {
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.org = org;
        this.speed = speed;
        this.payment = payment;
        this.icon = icon;
        this.trainingTime = trainingTime;
    }

    public Payment getCost() {
        return payment;
    }

    public void setCost(Payment payment) {
        this.payment = payment;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
