package org.drachens.Manager.scoreboards;

import net.kyori.adventure.text.Component;
import net.minestom.server.scoreboard.Sidebar;

public class ScoreboardBuilder {
    public ScoreboardBuilder() {

    }

    public static class builder {
        private final Sidebar sidebar;
        int lineNum = 0;

        public builder(Component title) {
            sidebar = new Sidebar(title);
        }

        public builder addLine(Sidebar.ScoreboardLine scoreboardLine) {
            sidebar.createLine(scoreboardLine);
            lineNum--;
            return this;
        }

        public builder addLine(String id, Component context, int line) {
            sidebar.createLine(new Sidebar.ScoreboardLine(id, context, line));
            lineNum--;
            return this;
        }

        public builder addLine(String id, Component context) {
            sidebar.createLine(new Sidebar.ScoreboardLine(id, context, lineNum));
            lineNum--;
            return this;
        }

        public Sidebar build() {
            return sidebar;
        }
    }
}
