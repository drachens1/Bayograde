package org.drachens.events.research;

import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.interfaces.Event;
import org.drachens.temporary.research.ResearchCountry;

public class ResearchCompletionEvent extends Event {
    private final ResearchCountry country;
    private final ResearchOption researchOption;

    public ResearchCompletionEvent(Instance instance, ResearchCountry country, ResearchOption researchOption) {
        super(instance);
        this.country = country;
        this.researchOption = researchOption;
    }

    public ResearchCountry getCountry() {
        return country;
    }

    public ResearchOption getResearchOption() {
        return researchOption;
    }
}
