package org.drachens.events.research;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.temporary.research.ResearchCountry;

public record ResearchStartEvent(Instance instance, ResearchCountry country, ResearchOption researchOption) implements Event { }