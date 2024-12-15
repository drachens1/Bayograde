package org.drachens.events.loan;

import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Loan;
import org.drachens.interfaces.Event;

public class LoanSendEvent extends Event {
    private final Country from;
    private final Country to;
    private final Loan loan;

    public LoanSendEvent(Instance instance, Country from, Country to, Loan loan) {
        super(instance);
        this.from = from;
        this.to = to;
        this.loan = loan;
    }

    public Country getFrom() {
        return from;
    }

    public Loan getLoan() {
        return loan;
    }

    public Country getTo() {
        return to;
    }
}
