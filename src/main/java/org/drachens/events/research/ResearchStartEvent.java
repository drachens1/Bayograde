package org.drachens.events.research;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Research.tree.ResearchOption;

public record ResearchStartEvent(Instance instance, Country country, ResearchOption researchOption) implements Event { }