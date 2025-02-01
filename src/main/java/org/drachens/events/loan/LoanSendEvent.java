package org.drachens.events.loan;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Loan;

public record LoanSendEvent(Instance instance, Country from, Country to, Loan loan) implements Event { }
