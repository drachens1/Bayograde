package org.drachens.dataClasses.customgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.World;
import org.drachens.dataClasses.datastorage.DataStorer;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.Messages.broadcast;

public class IntermissionGameWorld extends World {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    private final CPlayer opener;
    private final List<CPlayer> players = new ArrayList<>();
    private final Component welcomeMessage;
    private final List<Clientside> clientsides = new ArrayList<>();
    private boolean aiEnabled = true;
    private boolean factionsEnabled = true;
    private boolean instaBuild = false;
    private boolean researchEnabled = true;
    private int speedChoice = 5;
    private int progressionRateChoice = 5;
    private double speed = 1;
    private double progressionRate = 1;
    private int votingChoice = 1;
    private VotingWinner votingWinner;
    private boolean creation = false;

    public IntermissionGameWorld(CPlayer p, String name) {
        super(MinecraftServer.getInstanceManager().createInstanceContainer(), new Pos(0, 1, 0));
        fill(new Pos(-10,0,-10),new Pos(10,0,10),Block.GRAY_CONCRETE);
        this.opener=p;
        ContinentalManagers.worldManager.registerWorld(this);
        welcomeMessage =Component.text()
                .append(Component.text("| Welcome to a custom game.\n| These are basically the same as the global one but\n| the owner: "+p.getUsername()+" can activate certain DLC's \n| Also there is no voting period\n|\n| You are currently in the waiting period you can do anything but time wont advance\n| Until "+p.getUsername()+" starts the game", NamedTextColor.GREEN))
                .build();
        p.sendMessage(Component.text()
                .append(Component.text("| Welcome to a custom game.\n| You can activate any DLC's if you have any\n| Commands:\n| /manage creation complete #Starts the game\n| /manage creation cancel #Cancels the waiting period\n| /manage invite <player> #Invites a player\n| /manage kick <player> #Kicks a player\n| /manage options #Shows the options/clickable options for commands to make it easier\n| You need to start the game for time to advance\n| That's all! have fun!",NamedTextColor.GREEN))
                .build());
        ContinentalManagers.putWorldClass(getInstance(),new CustomGameWorldClass(new CountryDataManager(getInstance(), new ArrayList<>()),
                new ClientEntsToLoad(),
                new ProvinceManager(),
                new DataStorer()
        ));

        TextDisplay aiEnabledDisplay = new TextDisplay.create(new Pos(1,1,10).withYaw(180),getInstance(),Component.text().append(Component.text("AI enabled  ")).append(Component.text("✔",NamedTextColor.GREEN)).build())
                .build();
        aiEnabledDisplay.addBoundingBox(0.4f,0.4f, new Pos(-0.8,0,0),player -> {
            aiEnabled=!aiEnabled;
                    if (aiEnabled){
                        aiEnabledDisplay.setText(Component.text().append(Component.text("AI enabled  ")).append(Component.text("✔",NamedTextColor.GREEN)).build());
                    }else {
                        aiEnabledDisplay.setText(Component.text().append(Component.text("AI enabled  ")).append(Component.text("❌",NamedTextColor.RED)).build());
                    }
                });

        TextDisplay factionsEnabledDisplay = new TextDisplay.create(new Pos(1,1.5,10).withYaw(180),getInstance(),Component.text().append(Component.text("Factions enabled  ")).append(Component.text("✔",NamedTextColor.GREEN)).build())
                .build();
        factionsEnabledDisplay.addBoundingBox(0.4f,0.4f, new Pos(-1.2,0,0),player -> {
            factionsEnabled=!factionsEnabled;
            if (factionsEnabled){
                factionsEnabledDisplay.setText(Component.text().append(Component.text("Factions enabled  ")).append(Component.text("✔",NamedTextColor.GREEN)).build());
            }else {
                factionsEnabledDisplay.setText(Component.text().append(Component.text("Factions enabled  ")).append(Component.text("❌",NamedTextColor.RED)).build());
            }
        });

        TextDisplay instaBuildDisplay = new TextDisplay.create(new Pos(1,3,10).withYaw(180),getInstance(),Component.text().append(Component.text("Instant build  ")).append(Component.text("❌",NamedTextColor.RED)).build())
                .build();
        instaBuildDisplay.addBoundingBox(0.4f,0.4f, new Pos(-0.9,0,0),player -> {
            instaBuild=!instaBuild;
            if (instaBuild){
                instaBuildDisplay.setText(Component.text().append(Component.text("Instant build  ")).append(Component.text("✔",NamedTextColor.GREEN)).build());
            }else {
                instaBuildDisplay.setText(Component.text().append(Component.text("Instant build  ")).append(Component.text("❌",NamedTextColor.RED)).build());
            }
        });

        TextDisplay researchDisplay = new TextDisplay.create(new Pos(1,2.5,10).withYaw(180),getInstance(),Component.text().append(Component.text("Research enabled  ")).append(Component.text("✔",NamedTextColor.GREEN)).build())
                .build();
        researchDisplay.addBoundingBox(0.4f,0.4f, new Pos(-1.3,-0.1,0),player -> {
            researchEnabled=!researchEnabled;
            if (researchEnabled){
                researchDisplay.setText(Component.text().append(Component.text("Research enabled  ")).append(Component.text("✔",NamedTextColor.GREEN)).build());
            }else {
                researchDisplay.setText(Component.text().append(Component.text("Research enabled  ")).append(Component.text("❌",NamedTextColor.RED)).build());
            }
        });

        double[] options = new double[]{0.1,0.25,0.5,0.75, 1.0, 1.5, 2d, 3d, 4d, 5d};
        //Speed
        TextDisplay speedDisplay = new TextDisplay.create(new Pos(4.5,1.5,8.5).withYaw(145),getInstance(),Component.text("<  Speed : 1.0  >"))
                .build();
        //Add
        speedDisplay.addBoundingBox(0.4f,0.4f, new Pos(-0.75,0,0.55),player -> {
            if (speedChoice>=options.length-1){
                return;
            }
            speedChoice++;
            speed=options[speedChoice];
            speedDisplay.setText(Component.text().append(Component.text("<  Speed : ")).append(Component.text(speed)).append(Component.text("  >")).build());
        });
        //Minus
        speedDisplay.addBoundingBox(0.4f,0.4f, new Pos(0.75,0,-0.55),player -> {
            if (speedChoice<=0){
                return;
            }
            speedChoice--;
            speed=options[speedChoice];
            speedDisplay.setText(Component.text().append(Component.text("<   Speed : ")).append(Component.text(speed)).append(Component.text("  >")).build());
        });

        double[] optionsProgression = new double[]{0.1,0.25,0.5,0.75, 1.0, 1.5, 2d, 3d, 4d, 5d};
        TextDisplay progressionDisplay = new TextDisplay.create(new Pos(4.5,2,8.5).withYaw(145),getInstance(),Component.text("<  Progression rate : 1.0  >"))
                .build();
        //Add
        progressionDisplay.addBoundingBox(0.4f,0.4f, new Pos(-1.3,0,0.9),player -> {
            if (progressionRateChoice>=optionsProgression.length-1){
                return;
            }
            progressionRateChoice++;
            progressionRate=optionsProgression[progressionRateChoice];
            progressionDisplay.setText(Component.text().append(Component.text("<  Progression rate : ")).append(Component.text(progressionRate)).append(Component.text("  >")).build());
        });
        //Minus
        progressionDisplay.addBoundingBox(0.4f,0.4f, new Pos(1.3,0,-0.9),player -> {
            if (progressionRateChoice<=0){
                return;
            }
            progressionRateChoice--;
            progressionRate=optionsProgression[progressionRateChoice];
            progressionDisplay.setText(Component.text().append(Component.text("<   Progression rate : ")).append(Component.text(progressionRate)).append(Component.text("  >")).build());
        });

        VotingWinner[] votingsOptions = new VotingWinner[]{VotingWinner.ww2_clicks, VotingWinner.ww2_troops};
        TextDisplay votingDisplay = new TextDisplay.create(new Pos(-2.5,1.5,8.5).withYaw(215),getInstance(),Component.text("<  Option : ww2_clicks  >"))
                .build();
        votingWinner=votingsOptions[0];
        votingDisplay.addBoundingBox(0.4f,0.4f, new Pos(1.2,0,0.9),player -> {
            if (votingChoice>=votingsOptions.length-1){
                return;
            }
            votingChoice++;
            votingWinner=votingsOptions[votingChoice];
            votingDisplay.setText(Component.text().append(Component.text("<  Option : ")).append(Component.text(votingWinner.name())).append(Component.text("  >")).build());
        });

        votingDisplay.addBoundingBox(0.4f,0.4f, new Pos(-1.2,0,-0.85),player -> {
            if (votingChoice<=0){
                return;
            }
            votingChoice--;
            votingWinner=votingsOptions[votingChoice];
            votingDisplay.setText(Component.text().append(Component.text("<   Option : ")).append(Component.text(votingWinner.name())).append(Component.text("  >")).build());
        });

        TextDisplay ownerName = new TextDisplay.create(new Pos(1.1, 4.5, 10).withYaw(180),getInstance(),Component.text().append(Component.text("Opener: ", NamedTextColor.WHITE,TextDecoration.BOLD,TextDecoration.UNDERLINED)).append(p.getCPlayerName().decorate(TextDecoration.BOLD,TextDecoration.UNDERLINED)).build()).build();

        TextDisplay displayName = new TextDisplay.create(new Pos(1.1, 5, 10).withYaw(180),getInstance(),Component.text("Name: "+name, NamedTextColor.WHITE,TextDecoration.BOLD,TextDecoration.UNDERLINED))
                .build();

        TextDisplay start = new TextDisplay.create(new Pos(1.1,2,10).withYaw(180),getInstance(),Component.text("START",NamedTextColor.DARK_GRAY,TextDecoration.BOLD)).build();

        start.addBoundingBox(1f,0.2f,new Pos(0,0,0),player -> {
            if (player!=opener){
                return;
            }
            complete();
        });

        clientsides.add(aiEnabledDisplay);
        clientsides.add(factionsEnabledDisplay);
        clientsides.add(instaBuildDisplay);
        clientsides.add(researchDisplay);
        clientsides.add(speedDisplay);
        clientsides.add(progressionDisplay);
        clientsides.add(votingDisplay);
        clientsides.add(ownerName);
        clientsides.add(start);
        clientsides.add(displayName);

        p.setLeaderOfOwnGame(true);
        p.setInstance(getInstance());
    }

    private void complete(){
        creation = true;
        broadcast(Component.text()
                .append(MessageEnum.system.getComponent())
                .append(Component.text("Loading...",NamedTextColor.GREEN))
                .build(),getInstance());

        new CustomGameWorld(players, opener, VotingOption.create(votingWinner.getVotingOption())
                .factionsEnabled(factionsEnabled)
                .instaBuild(instaBuild)
                .researchEnabled(researchEnabled)
                .speed(speed)
                .progressionRate(progressionRateChoice)
                .AIEnabled(aiEnabled)
                .build());
    }

    @Override
    public void addPlayer(CPlayer p) {
        p.teleport(getSpawnPoint());
        players.add(p);
        p.setInOwnGame(true);
        p.setInIntermission(true);
        if (!p.isLeaderOfOwnGame()) p.sendMessage(welcomeMessage);
        p.refreshCommands();
        scoreboardManager.getScoreboard(p).getSidebar().removeViewer(p);
        clientsides.forEach(clientside -> clientside.addViewer(p));
    }

    @Override
    public void removePlayer(CPlayer p) {
        players.remove(p);
        if (players.isEmpty()){
            clientsides.forEach(Clientside::dispose);
            ContinentalManagers.worldManager.unregisterWorld(this);
        }
        if (creation)return;
        p.setInIntermission(false);
        p.setLeaderOfOwnGame(false);
        p.setInOwnGame(false);
        p.refreshCommands();
        p.addPlayTime(LocalTime.now());
    }
}