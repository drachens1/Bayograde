package org.drachens.dataClasses.other;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

@Getter
@Setter
public class CompletionBarTextDisplay {
    private final TextDisplay textDisplay;
    private final String fullBar = "||||||||||||||||||||||||||";
    private Component additional;
    private final TextColor colour;
    private float progress = 1.0f;

    public CompletionBarTextDisplay(Pos pos, Instance instance, TextColor colour, Component additional) {
        pos = pos.add(0.5, 0, 0.5);
        textDisplay = TextDisplay.create(instance, pos, Component.text(fullBar, colour))
                .followPlayer(true)
                .build();
        this.colour = colour;
        this.additional=additional;
    }

    public void addProgress(float amount) {
        progress += amount;
    }

    public void setProgress(float progress) {
        if (1.0f < progress) progress = 1.0f;
        if (0.0f > progress) {
            return;
        }
        this.progress = progress;
        int end = Math.round(fullBar.length() * progress);
        String bar = fullBar.substring(0, end);
        String second = fullBar.substring(end);
        textDisplay.setText(Component.text()
                .append(Component.text(bar, colour))
                .append(Component.text(second, TextColor.color(0, 0, 0)))
                .append(additional)
                .build());
    }
}
