package org.drachens.fileManagement.customTypes.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.OtherUtil.replaceWith;

public record CustomLoginRecord(String join, String changeJoin, String changeLeave, String leave) {
    public Component getComponent() {
        Component joinComponent = (join != null)
                ? MiniMessage.miniMessage().deserialize(join)
                : Component.text("Unset");

        Component changeJoinComponent = (changeJoin != null)
                ? MiniMessage.miniMessage().deserialize(changeJoin)
                : Component.text("Unset");

        Component changeLeaveComponent = (changeLeave != null)
                ? MiniMessage.miniMessage().deserialize(changeLeave)
                : Component.text("Unset");

        Component leaveComponent = (leave != null)
                ? MiniMessage.miniMessage().deserialize(leave)
                : Component.text("Unset");

        return Component.text()
                .append(Component.text("Join Message: "))
                .append(joinComponent)
                .appendNewline()
                .append(Component.text("Change world join Message: "))
                .append(changeJoinComponent)
                .appendNewline()
                .append(Component.text("Change world leave Message: "))
                .append(changeLeaveComponent)
                .appendNewline()
                .append(Component.text("Leave Message: "))
                .append(leaveComponent)
                .appendNewline()
                .build();
    }

    public Component toMessage(String msg, CPlayer p){
        return Component.text()
                .append(replaceWith(MiniMessage.miniMessage().deserialize(msg),"%player%",p.getUsername()))
                .build();
    }
}
