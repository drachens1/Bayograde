package org.drachens.store;

import dev.ng5m.event.CancelPurchaseEvent;
import dev.ng5m.event.PurchaseEvent;
import dev.ng5m.shop.PurchaseGson;
import net.minestom.server.MinecraftServer;

public class StoreManager {
    public StoreManager(){
        MinecraftServer.getGlobalEventHandler().addListener(PurchaseEvent.class,e->{
            PurchaseGson purchaseGson = e.request();

        });
        MinecraftServer.getGlobalEventHandler().addListener(CancelPurchaseEvent.class,e->{
            PurchaseGson purchaseGson = e.request();

        });
    }
}
