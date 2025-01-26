package dev.ng5m;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import dev.ng5m.event.PurchaseEvent;
import dev.ng5m.events.EventHandlerProviderManager;
import dev.ng5m.shop.PurchaseGson;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventDispatcher;
import org.drachens.miniGameSystem.minigames.FlappyBird;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class NG5M {

    public static void hook() {
        MinecraftServer.getConnectionManager().setPlayerProvider(CPlayer::new);

        EventHandlerProviderManager.registerProvider(FlappyBird.class);

        try {

            if (!FlappyBird.db.exists()) {
                FlappyBird.db.createNewFile();

                Util.writeString(FlappyBird.db, "{}");
            }

        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    private static void initPurchaseHandlerHTTPServer() {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(9802), 0);

            httpServer.createContext("/purchase_success", exchange -> {
                if (!("POST".equals(exchange.getRequestMethod()))) {
                    OutputStream os = exchange.getResponseBody();
                    String response = "Bad method: " + exchange.getRequestMethod();
                    exchange.sendResponseHeaders(405, response.length());
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    os.close();
                    return;
                }

                InputStream req = exchange.getRequestBody();

                String json = new String(req.readAllBytes());

                PurchaseGson purchaseGson = new Gson().fromJson(json, PurchaseGson.class);
                boolean success = purchaseGson.verify();

                EventDispatcher.call(new PurchaseEvent(purchaseGson, success));
            });

            httpServer.setExecutor(Executors.newFixedThreadPool(10));
            httpServer.start();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    public static void preServerHook() {
        initPurchaseHandlerHTTPServer();

//        try {
//            // add packet to registry
//
//            ClassPool pool = ClassPool.getDefault();
//
//            CtClass ctClass = pool.get("net.minestom.server.network.packet.PacketRegistry$ClientHandshake");
//            CtConstructor ctConstructor = ctClass.getDeclaredConstructor(new CtClass[]{});
//            ctConstructor.setBody("{ super(new net.minestom.server.network.packet.PacketRegistry$PacketRegistryTemplate$Entry[]{" +
//                    "entry(net.minestom.server.network.packet.client.handshake.ClientHandshakePacket.class, net.minestom.server.network.packet.client.handshake.ClientHandshakePacket.SERIALIZER), " +
//                    "entry(dev.ng5m.protocol.ClientPurchaseSuccessPacket.class, dev.ng5m.protocol.ClientPurchaseSuccessPacket.SERIALIZER)" +
//                    "}); }");
//
//            ctClass.toClass();
//
//            // add packet to immediate processing list
//            CtClass playerSocketConnection = pool.get("net.minestom.server.network.player.PlayerSocketConnection");
//            CtField field = playerSocketConnection.getDeclaredField("IMMEDIATE_PROCESS_PACKETS");
//            field.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
//
//            playerSocketConnection.toClass();
//
//            Field packetsField = PlayerSocketConnection.class.getDeclaredField("IMMEDIATE_PROCESS_PACKETS");
//            packetsField.setAccessible(true);
//            Set<Class<? extends ClientPacket>> set = (Set<Class<? extends ClientPacket>>) packetsField.get(null);
//            Set<Class<? extends ClientPacket>> modified = new HashSet<>(set);
//            modified.add(ClientPurchaseSuccessPacket.class);
//            packetsField.set(null, modified);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


    }

}
