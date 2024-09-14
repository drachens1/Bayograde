package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.drachens.Manager.ConfigFileManager.getLogCmds;
import static org.drachens.Manager.ConfigFileManager.getLogMsg;

public class Messages {

    public static void globalBroadcast(String msg){
        System.out.println(msg);
        logMsg("server",msg,null);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()){
            p.sendMessage(msg);
        }
    }

    public static void globalBroadcast(Component msg){
        System.out.println(msg);
        logMsg("server", String.valueOf(msg),null);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()){
            p.sendMessage(msg);
        }
    }

    public static void playerSendMessage(Component msg){
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()){
            p.sendMessage(msg);
        }
    }

    public static void broadcast(String msg, Instance world){
        System.out.println(msg);
        logMsg("server",msg,world);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()){
            if (p.getInstance()==world){
                p.sendMessage(msg);
            }
        }
    }
    public static void broadcast(Component msg, Instance world){
        System.out.println(msg);
        logMsg("server",msg.insertion(),world);
        for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()){
            if (p.getInstance()==world){
                p.sendMessage(msg);
            }
        }
    }
    public static void logCmd(String playerName,String cmd, Instance w){
        System.out.println(playerName+" : " +cmd);
        try {
            FileWriter f = new FileWriter(getLogCmds(),true);
            try {
                if (w!=null){
                    f.write(w.getDimensionName()+" "+getTime()+" "+playerName+":"+cmd);
                }else {
                    f.write(getTime()+" "+playerName+":"+cmd);
                }
                f.write("\n");
                f.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logMsg(String playerName, String msg, Instance w){
        try {
            FileWriter f = new FileWriter(getLogMsg(),true);
            try {
                if (w!=null){
                    f.write(w.getDimensionName()+" "+getTime()+" "+playerName+":"+msg);
                }else {
                    f.write(getTime()+" "+playerName+":"+msg);
                }
                f.write("\n");
                f.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTime(){
        return new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date());
    }
    public static void Console(String plugin,String log){
        System.out.println(plugin+": "+log);
    }

}
