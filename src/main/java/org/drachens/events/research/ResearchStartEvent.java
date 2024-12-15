package org.drachens.events.research;

import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.interfaces.Event;

public class ResearchStartEvent extends Event {
    private final Country country;
    private final ResearchOption researchOption;

    public ResearchStartEvent(Instance instance, Country country, ResearchOption researchOption) {
        super(instance);
        this.country = country;
        this.researchOption = researchOption;
    }

    public Country getCountry() {
        return country;
    }

    public ResearchOption getResearchOption() {
        return researchOption;
    }
}
