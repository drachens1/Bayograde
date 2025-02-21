package org.drachens.fileManagement;

import lombok.Getter;
import org.drachens.fileManagement.customTypes.ServerPropertiesFile;
import org.drachens.fileManagement.customTypes.WhitelistFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class ConfigFileManager {
    private String logMsg;
    private String logCmds;
    private final ServerPropertiesFile serverPropertiesFile = new ServerPropertiesFile();
    private final WhitelistFile whitelistFile = new WhitelistFile();

    public void startup()  {
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
    }

    private void fileExists(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                //don't need anything here
            }
        }
    }

}
