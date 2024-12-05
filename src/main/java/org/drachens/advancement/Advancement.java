package org.drachens.advancement;

import net.kyori.adventure.text.Component;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.item.Material;
import org.drachens.interfaces.Event;

public class Advancement {
    private final String identifier;
    private final Class<? extends Event> event;
    private final Material item;
    private final FrameType frameType;
    private final Component title;
    private final Component description;
    private final String parent;
    private final int[] coords;
    public Advancement(String identifier, Class<? extends Event> event, Material item, FrameType frameType, int[] coords, Component title, Component description, String parent){
        this.identifier=identifier;
        this.event=event;
        this.item=item;
        this.frameType=frameType;
        this.coords=coords;
        this.title=title;
        this.description=description;
        this.parent=parent;
    }
    public String getIdentifier(){
        return identifier;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }

    public Material getItem(){
        return item;
    }

    public FrameType getFrameType(){
        return frameType;
    }

    public Component getTitle() {
        return title;
    }

    public Component getDescription() {
        return description;
    }

    public String getParent(){
        return parent;
    }

    public int[] getCoords(){
        return coords;
    }
}
