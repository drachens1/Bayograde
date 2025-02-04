package org.drachens.interfaces.inventories;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.drachens.player_types.CPlayer;

public record OnUse(CPlayer player, boolean rightClick, boolean onBlock, Pos pos, Instance instance) {
}
