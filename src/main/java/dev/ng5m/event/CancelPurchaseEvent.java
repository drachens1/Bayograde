package dev.ng5m.event;

import dev.ng5m.shop.PurchaseGson;
import net.minestom.server.event.Event;

public record CancelPurchaseEvent(PurchaseGson request, boolean success) implements Event { }