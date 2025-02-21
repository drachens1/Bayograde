package org.drachens.advancement;

import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class AdvancementSection {
    private final String identifier;
    private final Material item;
    private final List<Advancement> advancements;
    private final Component title;
    private final Component description;
    private final FrameType frameType;

    private AdvancementSection(Create create) {
        identifier = create.identifier;
        item = create.item;
        advancements = create.advancements;
        title = create.title;
        description = create.description;
        frameType = create.frameType;
    }

    public static class Create {
        private final String identifier; //has to be lower case
        private final Material item;
        private final Component title;
        private final Component description;
        private final List<Advancement> advancements = new ArrayList<>();
        private final FrameType frameType;

        public Create(String identifier, Material item, FrameType frameType, Component title, Component description) {
            this.identifier = identifier;
            this.item = item;
            this.frameType = frameType;
            this.description = description;
            this.title = title;
        }

        public Create addAdvancement(Advancement advancement) {
            advancements.add(advancement);
            return this;
        }

        public AdvancementSection build() {
            return new AdvancementSection(this);
        }
    }
}
