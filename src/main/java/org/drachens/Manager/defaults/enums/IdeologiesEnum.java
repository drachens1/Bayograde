package org.drachens.Manager.defaults.enums;

import org.drachens.dataClasses.Countries.IdeologyTypes;

public enum IdeologiesEnum {
    ww2_fascist,
    ww2_neutral,
    ww2_anarchist,
    ww2_conservatist,
    ww2_socialist,
    ww2_liberalist,
    ww2_imperialist,
    ww2_capitalist,
    ww2_nationalist;

    private IdeologyTypes ideologyTypes;

    public IdeologyTypes getIdeologyTypes() {
        return ideologyTypes;
    }

    public void setIdeologyTypes(IdeologyTypes ideologyTypes) {
        this.ideologyTypes = ideologyTypes;
    }
}
