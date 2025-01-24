package dev.ng5m.event;

import dev.ng5m.shop.PurchaseGson;
import net.minestom.server.event.Event;

public class PurchaseEvent implements Event {
    public final PurchaseGson request;
    public final boolean success;

    public PurchaseEvent(PurchaseGson request, boolean success) {
        this.request = request;
        this.success = success;
    }

}
