package org.drachens.Manager.defaults.enums;

import lombok.Getter;
import lombok.Setter;
import org.drachens.dataClasses.Countries.IdeologyTypes;

@Getter
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

    @Setter
    private IdeologyTypes ideologyTypes;
}
