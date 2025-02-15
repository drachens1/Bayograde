package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.enums.PathingEnum;
import org.drachens.Manager.defaults.enums.TroopTypeEnum;
import org.drachens.dataClasses.Armys.DivisionTrainingQueue;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.player_types.CPlayer;

public class TroopSummonCMD extends Command {
    public TroopSummonCMD() {
        super("troop-summon");

        setDefaultExecutor((sender,context)->{
            CPlayer p = (CPlayer) sender;
            TroopCountry country = (TroopCountry) p.getCountry();
            Troop troop = new Troop(country.getCapital(),new DivisionTrainingQueue.TrainedTroop(TroopTypeEnum.ww2.getTroopTye(), country.getDivisionDesigns().getFirst(),0f), PathingEnum.ww2.getaStarPathfinderVoids());
            troop.getTroopType().getShootingAnimation().start(troop.getTroop(),true);
        });
    }
}
