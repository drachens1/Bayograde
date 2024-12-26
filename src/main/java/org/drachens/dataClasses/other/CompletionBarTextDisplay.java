package org.drachens.dataClasses.other;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

public class CompletionBarTextDisplay  {
    private final TextDisplay textDisplay;
    private float progress = 1f;
    private final String fullBar = "||||||||||||||||||||||||||";
    private final TextColor colour;

    public CompletionBarTextDisplay(Pos pos, Instance instance, TextColor colour){
        pos = pos.add(0.5,0,0.5);
        textDisplay = new TextDisplay.create(pos,instance, Component.text(fullBar,colour))
                .setFollowPlayer(true)
                .build();
        this.colour=colour;
    }

    public TextDisplay getTextDisplay(){
        return textDisplay;
    }

    public void setProgress(float progress){
        if (progress>1f)progress=1f;
        this.progress=progress;
        int end = Math.round(fullBar.length()*progress);
        String bar = fullBar.substring(0,end);
        String second = fullBar.substring(end);
        textDisplay.setText(Component.text()
                .append(Component.text(bar,colour))
                        .append(Component.text(second,TextColor.color(0,0,0)))
                .build());
    }

    public void addProgress(float amount){
        progress+=amount;
    }

    public float getProgress(){
        return progress;
    }
}
