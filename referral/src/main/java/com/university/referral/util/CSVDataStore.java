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

    public List<String[]> loadData(String filepath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filepath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return data;
    }

    public boolean appendData(String filepath, String[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true))) {
            bw.write(String.join(",", data));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

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
}
