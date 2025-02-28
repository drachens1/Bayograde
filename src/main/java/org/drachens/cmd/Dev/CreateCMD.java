package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class CreateCMD extends Command {
    public CreateCMD() {
        super("create");

        ArgumentInteger modelData = ArgumentType.Integer("ModelData");
        ArgumentItemStack item = ArgumentType.ItemStack("Item");

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            ItemStack itemStack = context.get(item);
            int modelDatas = context.get(modelData);
            if (null == itemStack) return;

            ItemDisplay i = new ItemDisplay(itemBuilder(itemStack.material(), modelDatas), ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(p.getPosition()), ItemDisplay.DisplayType.GROUND);
            i.addViewer(p);
        }, item, modelData);

    }
}
