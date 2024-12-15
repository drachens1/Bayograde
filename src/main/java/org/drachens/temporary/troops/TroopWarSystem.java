package org.drachens.temporary.troops;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.animation.Animation;
import org.drachens.animation.DynamicAnimation;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Armys.TrainedTroop;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Armys.TroopType;
import org.drachens.dataClasses.territories.Province;
import org.drachens.interfaces.War;

import java.util.HashMap;

import static org.drachens.util.ServerUtil.blockVecToPos;

public class TroopWarSystem implements War {


    public TroopWarSystem() {

    }

    @Override
    public void onClick(PlayerBlockInteractEvent e) {

    }

    @Override
    public void onClick(PlayerUseItemEvent e) {

    }

    @Override
    public void onClick(PlayerStartDiggingEvent e) {

    }
}
