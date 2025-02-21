package dev.ng5m;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import dev.ng5m.bansystem.BanSystemEvents;
import dev.ng5m.event.CancelPurchaseEvent;
import dev.ng5m.event.PurchaseEvent;
import dev.ng5m.shop.PurchaseGson;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventDispatcher;
import org.drachens.player_types.CPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class NG5M {
    private final HttpServer httpServer;
    public NG5M(){
        try {
            httpServer = HttpServer.create(new InetSocketAddress(9802), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void preHook(){
        setupHTTP();
    }
    public void hook() {
        MinecraftServer.getConnectionManager().setPlayerProvider(CPlayer::new);

        new BanSystemEvents();
    }

    private void setupHTTP(){
        try {
            httpServer.createContext("/purchase_success", exchange -> {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    OutputStream os = exchange.getResponseBody();
                    String response = "Bad method: " + exchange.getRequestMethod();
                    exchange.sendResponseHeaders(405, response.length());
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    os.close();
                    return;
                }

                InputStream req = exchange.getRequestBody();

                String json = new String(req.readAllBytes(), StandardCharsets.UTF_8);

                PurchaseGson purchaseGson = new Gson().fromJson(json, PurchaseGson.class);
                boolean success = purchaseGson.verify();

                EventDispatcher.call(new PurchaseEvent(purchaseGson, success));
            });

            httpServer.createContext("/purchase_cancel", exchange -> {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    OutputStream os = exchange.getResponseBody();
                    String response = "Bad method: " + exchange.getRequestMethod();
                    exchange.sendResponseHeaders(405, response.length());
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    os.close();
                    return;
                }

                InputStream req = exchange.getRequestBody();

                String json = new String(req.readAllBytes(), StandardCharsets.UTF_8);

                PurchaseGson purchaseGson = new Gson().fromJson(json, PurchaseGson.class);
                boolean success = purchaseGson.verify();

                EventDispatcher.call(new CancelPurchaseEvent(purchaseGson, success));
            });

            httpServer.setExecutor(Executors.newFixedThreadPool(10));
            httpServer.start();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }
}
