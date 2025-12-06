package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.PatientRecord;
import com.university.referral.util.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class PatientRecordController {

    private ApplicationDataStore dataStore;

    public PatientRecordController(ApplicationDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addPatient(PatientRecord record) {
        dataStore.getPatients().add(record);
        saveAll();
    }

    public void updatePatient(int index, PatientRecord updated) {
        dataStore.getPatients().set(index, updated);
        saveAll();
    }

    public void deletePatient(int index) {
        dataStore.getPatients().remove(index);
        saveAll();
    }

    private void saveAll() {
        List<String[]> csvRows = new ArrayList<>();

        for (PatientRecord p : dataStore.getPatients()) {
            csvRows.add(new String[]{
                    p.getPatientId(),
                    p.getName(),
                    p.getDob(),
                    p.getPhone(),
                    p.getEmail()
            });
        }

        CsvFileWriter.writeCsv(ApplicationDataStore.PATIENT_CSV, csvRows);
    }
}
