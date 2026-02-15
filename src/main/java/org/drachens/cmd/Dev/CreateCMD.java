package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class CreateCMD extends Command {
    private final HashMap<Province, Clientside> clientsideHashMap = new HashMap<>();
    public CreateCMD() {
        super("build");

        ArgumentInteger modelData = ArgumentType.Integer("ModelData");
        ArgumentItemStack item = ArgumentType.ItemStack("Item");
        ArgumentFloat rotation = ArgumentType.Float("rotation");

        setDefaultExecutor(((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            clientsideHashMap.get(ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(p.getPosition().withY(0))).dispose();
        }));

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            ItemStack itemStack = context.get(item);
            int modelDatas = context.get(modelData);
            if (null == itemStack) return;

            Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(p.getPosition().withY(0));
            ItemDisplay i = new ItemDisplay(itemBuilder(itemStack.material(), modelDatas),
                    province,
                    ItemDisplay.DisplayType.GROUND);
            if (clientsideHashMap.containsKey(province)){
                clientsideHashMap.get(province).dispose();
            }
            clientsideHashMap.put(province,i);
            i.addViewer(p);
        }, item, modelData);

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            ItemStack itemStack = context.get(item);
            int modelDatas = context.get(modelData);
            if (null == itemStack) return;

            Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(p.getPosition().withY(0));
            ItemDisplay i = new ItemDisplay(itemBuilder(itemStack.material(), modelDatas),
                    province,
                    ItemDisplay.DisplayType.GROUND);
            i.addYaw(context.get(rotation));
            if (clientsideHashMap.containsKey(province)){
                clientsideHashMap.get(province).dispose();
            }
            clientsideHashMap.put(province,i);
            i.addViewer(p);
        }, item, modelData, rotation);

    }
}
