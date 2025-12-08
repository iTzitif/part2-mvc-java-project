package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.StaffMember;
import com.university.referral.util.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class StaffController {

    private final ApplicationDataStore dataStore;

    public StaffController(ApplicationDataStore dataStore) { this.dataStore = dataStore; }

    public List<StaffMember> getAll() { return dataStore.getStaff(); }

    public void addStaff(StaffMember s) {
        dataStore.getStaff().add(s);
        saveAll();
    }

    public void updateStaff(int idx, StaffMember s) {
        dataStore.getStaff().set(idx, s);
        saveAll();
    }

    public void deleteStaff(int idx) {
        dataStore.getStaff().remove(idx);
        saveAll();
    }

    private void saveAll() {
        List<String[]> rows = new ArrayList<>();
        for (StaffMember s : dataStore.getStaff()) {
            rows.add(new String[] { s.getStaffId(), s.getFullName(), s.getRole(), s.getContactNumber() });
        }
        CsvFileWriter.writeCsv(ApplicationDataStore.STAFF_CSV, rows);
    }
}
