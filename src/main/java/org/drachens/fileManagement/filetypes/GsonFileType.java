package org.drachens.fileManagement.filetypes;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class GsonFileType extends GsonStringMaker {
    private final File file;

    protected GsonFileType(String path) {
        super(readFile(path));
        this.file = new File(path);
    }

    private static String readFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "{}";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            StringBuilder json = new StringBuilder();
            String line;
            while (null != (line = reader.readLine())) {
                json.append(line);
            }
            return json.toString();
        } catch (IOException e) {
            System.err.println("Error reading file: " + path + " - " + e.getMessage());
            return "{}";
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(saveToJson());
        } catch (IOException e) {
            System.err.println("Error saving file: " + file.getAbsolutePath() + " - " + e.getMessage());
        }
    }
}
