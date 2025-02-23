package org.drachens.cmd.Dev;

import dev.ng5m.Constants;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.*;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentLong;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.PathingEnum;
import org.drachens.Manager.defaults.enums.RankEnum;
import org.drachens.Manager.defaults.enums.TroopTypeEnum;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.dataClasses.Armys.DivisionTrainingQueue;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.command.CommandCreator;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.events.system.ResetEvent;
import org.drachens.events.system.StartGameEvent;
import org.drachens.generalGame.clicks.ClicksAI;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.player_types.CPlayer;

import java.util.UUID;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;
import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.PlayerUtil.getUUIDFromName;

public class DevCMD extends Command {
    public DevCMD() {
        super("dev");

        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("operator");
        });

        ArgumentString player = ArgumentType.String("player");
        ArgumentEntity playerEntity = ArgumentType.Entity("player");

        ArgumentStringArray reason = ArgumentType.StringArray("reason");
        ArgumentLong duration = ArgumentType.Long("time (minutes)");

        addSubcommand(new CommandCreator(("op"))
                .addSyntax((sender,context)->{
                    CPlayer p = (CPlayer) context.get(playerEntity).findFirstPlayer(sender);
                    if (null == p) {
                        sender.sendMessage("P is null");
                        return;
                    }
                    sender.sendMessage("You have opped " + p.getUsername());
                    p.sendMessage("You have been opped");
                    ContinentalManagers.permissions.playerOp(p);
                },playerEntity)
                .setDefaultExecutor((sender,context)-> sender.sendMessage("Usage /op <player>"))
                .build());

        addSubcommand(new CommandCreator("unban")
                .setCondition((sender,s)->{
                    CPlayer p = (CPlayer) sender;
                    return p.hasPermission("unban");
                })
                .setDefaultExecutor((sender,context)-> sender.sendMessage("Usage: /unban player"))
                .addSyntax((sender,context)->{
                    CPlayer p = (CPlayer) sender;
                    System.out.println(p.getUsername() + " has unbanned " + context.get(player));
                    UUID u = getUUIDFromName(context.get(player));
                    if (null == u) {
                        sender.sendMessage("Player is null");
                        return;
                    }
                    p.sendMessage("You have unbanned " + context.get(player));
                    Constants.BAN_MANAGER.removeEntry(p.getUuid());
                },player)
                .build());

        addSubcommand(new CommandCreator("ban")
                .setCondition((sender,s)->{
                    CPlayer p = (CPlayer) sender;
                    return p.hasPermission("unban");
                })
                .setDefaultExecutor((sender,context)-> sender.sendMessage("Usage /ban <player>"))
                .addSyntax((sender, context) -> {
                    CPlayer send = (CPlayer) sender;
                    System.out.println(send.getUsername() + " has banned " + context.get(player));
                    UUID p = getUUIDFromName(context.get(player));
                    if (null == p) {
                        sender.sendMessage("Player is null");
                        return;
                    }
                    send.sendMessage("You have banned " + context.get(player));
                    Constants.BAN_MANAGER.banPlayer(p, context.get(duration));
                }, player, duration, reason)
                .build());

        addSubcommand(new CommandCreator("reset")
                .setCondition((sender,s)->{
                    CPlayer p = (CPlayer) sender;
                    return p.hasPermission("reset");
                })
                .setDefaultExecutor((sender,context)->{
                    CPlayer p = (CPlayer) sender;
                    EventDispatcher.call(new ResetEvent(p.getInstance()));
                })
                .build());

        addSubcommand(new CommandCreator("gamemode")
                .setCondition((sender,s)->{
                    CPlayer p = (CPlayer) sender;
                    return p.hasPermission("gamemode");
                })
                .setDefaultExecutor((sender,context)-> sender.sendMessage("Proper usage /gamemode <choice>"))
                .addSubCommand(new CommandCreator("survival")
                        .setDefaultExecutor((sender,context)->{
                            CPlayer p = (CPlayer) sender;
                            p.setGameMode(GameMode.SURVIVAL);
                        })
                        .build())
                .addSubCommand(new CommandCreator("creative")
                        .setDefaultExecutor((sender,context)->{
                            CPlayer p = (CPlayer) sender;
                            p.setGameMode(GameMode.CREATIVE);
                        })
                        .build())
                .build());

        ArgumentInteger modelData = ArgumentType.Integer("ModelData");
        ArgumentItemStack item = ArgumentType.ItemStack("Item");

        addSubcommand(new CommandCreator("summon")
                .addSyntax((sender, context) -> {
                    if (!(sender instanceof CPlayer p)) return;
                    ItemStack itemStack = context.get(item);
                    int modelDatas = context.get(modelData);
                    if (null == itemStack) return;
                    ItemDisplay i = new ItemDisplay(itemBuilder(itemStack.material(), modelDatas), p.getPosition(), ItemDisplay.DisplayType.GROUND, p.getInstance());
                    i.addViewer(p);
                }, item, modelData)
                .setCondition((sender, s) -> {
                    CPlayer p = (CPlayer) sender;
                    return p.hasPermission("summon");
                })
                .build());

        addSubcommand(new CommandCreator("example")
                .setDefaultExecutor((sender,context)->ContinentalManagers.guiManager.openGUI(new ExampleGUI(), (CPlayer) sender))
                .build());

        addSubcommand(new CommandCreator("ai")
                .setDefaultExecutor((sender, context) -> {
                    CPlayer p = (CPlayer) sender;
                    Country country = p.getCountry();
                    ClicksAI clicksAI = (ClicksAI) ContinentalManagers.centralAIManager.getAIManagerFor(p.getInstance());
                    clicksAI.createAIForCountry(country);
                })
                .build());

        Argument<String> s = ArgumentType.String("border")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    Country country = p.getCountry();
                    getSuggestionBasedOnInput(suggestion,country.getMilitary().getBorderProvinces().keySet());
                });

        ArgumentWord a = ArgumentType.Word("something")
                .from("country","borders");

        addSubcommand(new CommandCreator("country-borders")
                .addSyntax((sender, context) -> {
                    CPlayer p = (CPlayer) sender;
                    Country country = p.getCountry();
                    Instance instance = country.getInstance();
                    Country c = ContinentalManagers.world(instance).countryDataManager().getCountryFromName(context.get(s));
                    switch (context.get(a)){
                        case "country":
                            c.getMilitary().getOccupies().forEach(province -> instance.setBlock(province.getPos(), Material.DIAMOND_BLOCK.block()));
                            break;
                        case "borders":
                            country.getMilitary().getBorder(context.get(s)).forEach(province ->
                                    instance.setBlock(province.getPos(), Material.DIAMOND_BLOCK.block()));
                            c.getMilitary().getBorder(country.getName()).forEach(province ->
                                    instance.setBlock(province.getPos(), Material.GOLD_BLOCK.block()));
                            break;
                    }
                },s,a)
                .build());

        addSubcommand(new CommandCreator("troop-summon")
                .setDefaultExecutor((sender, context)->{
                    CPlayer p = (CPlayer) sender;
                    TroopCountry country = (TroopCountry) p.getCountry();
                    Troop troop = new Troop(country.getInfo().getCapital(),new DivisionTrainingQueue.TrainedTroop(TroopTypeEnum.ww2.getTroopTye(), country.getDivisionDesigns().getFirst(), 0.0f), PathingEnum.ww2.getAStarPathfinderVoids());
                    troop.getTroopType().getShootingAnimation().start(troop.getTroop(),true);
                })
                .build());

        ArgumentString ranks = ArgumentType.String("rank");
        addSubcommand(new CommandCreator("give-rank")
                .addSyntax((sender,context)->{
                    RankEnum rank = RankEnum.valueOf(context.get(ranks));
                    CPlayer p = (CPlayer) sender;
                    rank.getRank().addPlayer(p);
                })
                .build());

        addSubcommand(new CommandCreator("test")
                .setDefaultExecutor(((sender, context) -> {
                    CPlayer p = (CPlayer) sender;
                    ContinentalManagers.saveManager.save(p.getInstance());
                }))
                .build());

        ArgumentInteger x = ArgumentType.Integer("x");
        ArgumentInteger y = ArgumentType.Integer("y");
        ArgumentInteger z = ArgumentType.Integer("z");
        addSubcommand(new CommandCreator("set-block")
                .addSyntax((sender,context)->{},x)
                .addSyntax((sender,context)->{},x,y)
                .addSyntax((sender,context)->{
                    CPlayer p = (CPlayer) sender;
                    p.getInstance().setBlock(context.get(x),context.get(y),context.get(z),Material.DIAMOND_BLOCK.block());
                },x,y,z)
                .build());

        addSubcommand(new CommandCreator("auto-train")
                .setDefaultExecutor(((sender, context) -> {
                    CPlayer p = (CPlayer) sender;
                    Instance instance = p.getInstance();
                    VotingWinner votingWinner = ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner;

                    MinecraftServer.getGlobalEventHandler().addListener(ResetEvent.class,e->{
                        if (e.instance()!=instance)return;
                        EventDispatcher.call(new StartGameEvent(p.getInstance(),votingWinner.getVotingOption()));
                    });
                }))
                .build());
    }
}
