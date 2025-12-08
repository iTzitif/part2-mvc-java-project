package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.ClinicianProfile;
import com.university.referral.util.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class ClinicianProfileController {

    private final ApplicationDataStore dataStore;

    public ClinicianProfileController(ApplicationDataStore dataStore) { this.dataStore = dataStore; }

    public List<ClinicianProfile> getAll() { return dataStore.getClinicians(); }

    public void addClinician(ClinicianProfile c) {
        dataStore.getClinicians().add(c);
        saveAll();
    }

    public void updateClinician(int idx, ClinicianProfile c) {
        dataStore.getClinicians().set(idx, c);
        saveAll();
    }

    public void deleteClinician(int idx) {
        dataStore.getClinicians().remove(idx);
        saveAll();
    }

    private void saveAll() {
        List<String[]> rows = new ArrayList<>();
        for (ClinicianProfile c : dataStore.getClinicians()) {
            rows.add(new String[]{ c.getClinicianId(), c.getFullName(), c.getRole(), c.getContactNumber() });
        }
        CsvFileWriter.writeCsv(ApplicationDataStore.CLINICIAN_CSV, rows);
    }
}
