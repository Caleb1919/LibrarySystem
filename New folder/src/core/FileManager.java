package core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_FOLDER = "data";

    /**
     * Ensure the data folder exists
     */
    private static void ensureDataFolderExists() {
        File dir = new File(DATA_FOLDER);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("📂 'data' folder created.");
            }
        }
    }

    /**
     * Ensure a specific file exists
     */
    private static void ensureFileExists(String filePath) {
        ensureDataFolderExists();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("📄 File created: " + filePath);
                }
            } catch (IOException e) {
                System.out.println("❌ Error creating file: " + e.getMessage());
            }
        }
    }

    /**
     * Write data to a file (overwrites existing content)
     */
    public static void writeToFile(String filePath, List<String> lines) {
        ensureFileExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("💾 Data saved to " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Error saving to file: " + e.getMessage());
        }
    }

    /**
     * Read all lines from a file
     */
    public static List<String> readFromFile(String filePath) {
        ensureFileExists(filePath);
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            if (!lines.isEmpty()) {
                System.out.println("📥 Data loaded from " + filePath);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading from file: " + e.getMessage());
        }
        return lines;
    }
}
