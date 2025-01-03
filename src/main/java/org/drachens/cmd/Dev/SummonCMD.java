package org.drachens.cmd.Dev;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.item.ItemStack;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class SummonCMD extends Command {
    public SummonCMD() {
        super("summon");

        var modelData = ArgumentType.Integer("ModelData");
        var item = ArgumentType.ItemStack("Item");

        setCondition(((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("summon");
        }));
        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            if (!p.hasPermission("summon")) return;
            ItemStack itemStack = context.get(item);
            int modelDatas = context.get(modelData);
            if (itemStack == null) return;
            ItemDisplay i = new ItemDisplay(itemBuilder(itemStack.material(), modelDatas), p.getPosition(), ItemDisplay.DisplayType.GROUND, p.getInstance(), true);
            i.addViewer(p);
        }, item, modelData);
    }
}
