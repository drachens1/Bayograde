package org.drachens.temporary.invasions;

import dev.ng5m.CPlayer;
import kotlin.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.cmd.ConfirmCMD;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.OtherUtil.posToString;

public class NavalInvasionClicksItem extends HotbarItemButton {
    private final Component selectedAttackPoint = Component.text("Selected the attack point",NamedTextColor.GREEN);
    private final Component selectedInvasionPoint = Component.text("Selected initial launch point",NamedTextColor.GREEN);
    private final Component provinceNoExist = Component.text("That province doesn't exist",NamedTextColor.RED);
    private final Component error = Component.text("You need to join a country first", NamedTextColor.RED);
    private final Component pos1 = Component.text("You need to launch the attack from  an ally or your own land or a puppets", NamedTextColor.RED);
    private final Component pos2 = Component.text("You need a war justification to attack",NamedTextColor.RED);
    private final Component noWater = Component.text("It needs to be adjacent to water", NamedTextColor.RED);
    private final HashMap<CPlayer, Pair<Province, Province>> playerPairHashMap = new HashMap<>();
    private final ConfirmCMD confirm;
    int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1},
            {0, 0}
    };
    public NavalInvasionClicksItem() {
        super(1, itemBuilder(Material.SPRUCE_BOAT,1));
        confirm = (ConfirmCMD) MinecraftServer.getCommandManager().getCommand("confirm");
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Country country = p.getCountry();
        if (country==null){
            p.sendMessage(error);
            return;
        }
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getBlockPosition());
        if (province==null){
            p.sendMessage(provinceNoExist);
            return;
        }
        if (!country.isMilitaryFriend(province.getOccupier())){
            p.sendMessage(pos1);
            return;
        }
        if (notAdjacentWater(province.getPos(), province.getInstance())){
            p.sendMessage(noWater);
            return;
        }
        Pair<Province,Province> a = playerPairHashMap.getOrDefault(p,new Pair<>(null,null));
        playerPairHashMap.put(p,new Pair<>(province,a.component2()));
        p.sendMessage(selectedInvasionPoint);
        if (a.component2()!=null){
            p.sendMessage(Component.text()
                            .append(Component.text("Naval invasion:",NamedTextColor.BLUE))
                            .appendNewline()
                            .append(Component.text("Launch point: ",NamedTextColor.BLUE))
                            .append(Component.text(posToString(province.getPos())))
                            .appendNewline()
                            .append(Component.text("Attack point: ",NamedTextColor.BLUE))
                            .append(Component.text(posToString(a.component2().getPos())))
                            .appendNewline()
                            .append(Component.text()
                                    .append(Component.text("[LAUNCH]",NamedTextColor.GOLD, TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to confirm the naval invasion", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/confirm"))
                            )
                    .build());
            confirm.putPlayerRunnable(p, () -> navalInvade(p,province,a.component2()));
        }
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Country country = p.getCountry();
        if (country==null){
            p.sendMessage(error);
            return;
        }
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getPosition());
        if (province==null){
            p.sendMessage(provinceNoExist);
            return;
        }
        if (!country.canFight(province.getOccupier())){
            p.sendMessage(pos2);
            return;
        }
        if (notAdjacentWater(province.getPos(), province.getInstance())){
            p.sendMessage(noWater);
            return;
        }
        Pair<Province,Province> a = playerPairHashMap.getOrDefault(p,new Pair<>(null,null));
        playerPairHashMap.put(p,new Pair<>(a.component1(),province));
        p.sendMessage(selectedAttackPoint);
        if (a.component1()!=null){
            p.sendMessage(Component.text()
                    .append(Component.text("Naval invasion:",NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Launch point: ",NamedTextColor.BLUE))
                    .append(Component.text(posToString(a.component1().getPos())))
                    .appendNewline()
                    .append(Component.text("Attack point: ",NamedTextColor.BLUE))
                    .append(Component.text(posToString(province.getPos())))
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text("[LAUNCH]",NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to confirm the naval invasion", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/confirm"))
                    )
                    .build());
            confirm.putPlayerRunnable(p, () -> navalInvade(p,a.component1(),province));
        }
    }

    int[][] directions2 = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private boolean notAdjacentWater(@NotNull Pos position, Instance instance) {
        for (int[] direction : directions2) {
            int offsetX = direction[0];
            int offsetY = direction[1];

            if (instance.getBlock(position.add(offsetX, 0, offsetY)).compare(Block.BLUE_STAINED_GLASS)) {
                return false;
            }
        }
        return true;
    }

    public void navalInvade(CPlayer p, Province province1, Province province2){
        Country country = p.getCountry();
        if (country==null){
            p.sendMessage(error);
            return;
        }
        if (!country.isMilitaryFriend(province1.getOccupier())){
            p.sendMessage(pos1);
            return;
        }
        if (!country.canFight(province2.getOccupier())){
            p.sendMessage(pos2);
            return;
        }
        Country target = province2.getOccupier();
        for (int[] d : directions){
            Province province = province2.add(d[0],d[1]);
            if (province != null  && province.getOccupier()!=null && country.canFight(target)){
                province.capture(country);
            }
        }
    }
}
