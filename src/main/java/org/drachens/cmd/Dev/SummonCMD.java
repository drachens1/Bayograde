package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.item.ItemStack;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class SummonCMD extends Command {
    public SummonCMD() {
        super("summon");

        ArgumentInteger modelData = ArgumentType.Integer("ModelData");
        ArgumentItemStack item = ArgumentType.ItemStack("Item");

        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("summon");
        });
        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            ItemStack itemStack = context.get(item);
            int modelDatas = context.get(modelData);
            if (null == itemStack) return;
            ItemDisplay i = new ItemDisplay(itemBuilder(itemStack.material(), modelDatas), p.getPosition(), ItemDisplay.DisplayType.GROUND, p.getInstance());
            i.addViewer(p);
        }, item, modelData);
    }
}
