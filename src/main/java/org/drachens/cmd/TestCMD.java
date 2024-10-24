package org.drachens.cmd;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.command.builder.Command;

import static org.drachens.util.KyoriUtil.compBuild;

public class TestCMD extends Command {
    int[] troopWalk = {1,2,3,4};
    int[] attack = {1,2,3};
    public TestCMD() {
        super("test");
        setDefaultExecutor((sender,context)->{
            System.out.println("1");
            System.out.println(compBuild("Appleeee", NamedTextColor.GOLD));
            System.out.println("2");
            System.out.println(PlainTextComponentSerializer.plainText().serialize(compBuild("Appleeee", NamedTextColor.GOLD)));
        });
    }
}
