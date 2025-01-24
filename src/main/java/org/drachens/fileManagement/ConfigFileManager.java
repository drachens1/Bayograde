package org.drachens.fileManagement;

import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.drachens.fileManagement.customTypes.WhitelistFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigFileManager {
    private String logMsg;
    private String logCmds;
    private final ServerPropertiesFile serverPropertiesFile = new ServerPropertiesFile();
    private final WhitelistFile whitelistFile = new WhitelistFile();

    public void startup() {
        //To setup all the log stuff
        System.out.println("Creating logs");
        new File("logs").mkdir();
        new File("logs/cmds").mkdir();
        new File("logs/msg").mkdir();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy.HH.mm");
        logCmds = "logs/cmds/" + format.format(new Date()) + ".txt";
        File cmd = new File(logCmds);
        fileExists(cmd);
        logMsg = "logs/msg/" + format.format(new Date()) + ".txt";
        File msg = new File(logMsg);
        fileExists(msg);
        System.out.println("Logs finished");
    }

    private void fileExists(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
                System.out.println(f.getName() + " was created");
            } catch (IOException e) {
                //don't need anything here
            }
        }
    }

    public String getLogMsg() {
        return logMsg;
    }

    public String getLogCmds() {
        return logCmds;
    }

    public WhitelistFile getWhitelistFile() {
        return whitelistFile;
    }

    public ServerPropertiesFile getServerPropertiesFile() {
        return serverPropertiesFile;
    }
}
