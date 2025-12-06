package com.university.referral.model;

import com.university.referral.util.CsvFileReader;

import java.util.ArrayList;
import java.util.List;

public class ApplicationDataStore {

    private static ApplicationDataStore instance;

    public static final String PATIENT_CSV = "data/patients.csv";

    private final List<PatientRecord> patientRecord = new ArrayList<>();

    private ApplicationDataStore() { }

    public static ApplicationDataStore getInstance() {
        if (instance == null) {
            instance = new ApplicationDataStore();
        }
        return instance;
    }

    public List<PatientRecord> getPatients() {
        return patientRecord;
    }

    public void loadApplicationData() {
        loadPatientRecords();
    }

    private void loadPatientRecords() {

        List<String[]> rows = CsvFileReader.readCsvFile(PATIENT_CSV);

        for (String[] col : rows) {
            patientRecord.add(new PatientRecord(
                    col[0], col[1], col[2], col[3], col[4]
            ));
        }
    }
}
