package com.university.referral.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVDataStore {

    private static CSVDataStore instance;

    private CSVDataStore() {}

    public static synchronized CSVDataStore getInstance() {
        if (instance == null) {
            instance = new CSVDataStore();
        }
        return instance;
    }

    // Load CSV data (skips header)
    public List<String[]> loadData(String filepath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                data.add(splitCSVLine(line));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return data;
    }

    // Append a row
    public boolean appendData(String filepath, String[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true))) {
            bw.write(String.join(",", data));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            return false;
        }
    }

    // Create / overwrite file with headers
    public boolean createFileWithHeaders(String filepath, String[] headers) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write(String.join(",", headers));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
            return false;
        }
    }

    // CSV-safe split (handles quotes)
    private String[] splitCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }
}
