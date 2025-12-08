package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.MedicalFacility;
import com.university.referral.util.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class FacilityController {

    private final ApplicationDataStore dataStore;

    public FacilityController(ApplicationDataStore dataStore) { this.dataStore = dataStore; }

    public List<MedicalFacility> getAll() { return dataStore.getFacilities(); }

    public void addFacility(MedicalFacility f) {
        dataStore.getFacilities().add(f);
        saveAll();
    }

    public void updateFacility(int idx, MedicalFacility f) {
        dataStore.getFacilities().set(idx, f);
        saveAll();
    }

    public void deleteFacility(int idx) {
        dataStore.getFacilities().remove(idx);
        saveAll();
    }

    private void saveAll() {
        List<String[]> rows = new ArrayList<>();
        for (MedicalFacility f : dataStore.getFacilities()) {
            rows.add(new String[] { f.getFacilityId(), f.getName(), f.getAddress() });
        }
        CsvFileWriter.writeCsv(ApplicationDataStore.FACILITY_CSV, rows);
    }
}
