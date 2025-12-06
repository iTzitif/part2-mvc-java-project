package com.university.referral.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {

    public static void writeCsv(String filePath, List<String[]> rows) {

        try (FileWriter writer = new FileWriter(filePath)) {

            for (String[] row : rows) {
                writer.write(String.join(",", row) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
