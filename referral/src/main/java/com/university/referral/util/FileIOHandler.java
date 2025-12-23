package com.university.referral.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileIOHandler {

    public static boolean writeToFile(String filepath, String content) {
        try {
            Path path = Paths.get(filepath);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
                writer.write(content);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    public static String readFromFile(String filepath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return content.toString();
    }
}
