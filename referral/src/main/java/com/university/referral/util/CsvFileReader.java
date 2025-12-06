package com.university.referral.util;

import java.io.*;
import java.util.*;

public class CsvFileReader {

    public static List<String[]> readCsvFile(String filePath) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line.split(","));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rows;
    }
}
