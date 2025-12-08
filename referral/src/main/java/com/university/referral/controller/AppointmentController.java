package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.AppointmentRecord;
import com.university.referral.util.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class AppointmentController {

    private final ApplicationDataStore dataStore;

    public AppointmentController(ApplicationDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<AppointmentRecord> getAll() { return dataStore.getAppointments(); }

    public void addAppointment(AppointmentRecord a) {
        dataStore.getAppointments().add(a);
        saveAll();
    }

    public void updateAppointment(int idx, AppointmentRecord updated) {
        dataStore.getAppointments().set(idx, updated);
        saveAll();
    }

    public void deleteAppointment(int idx) {
        dataStore.getAppointments().remove(idx);
        saveAll();
    }

    private void saveAll() {
        List<String[]> rows = new ArrayList<>();
        for (AppointmentRecord a : dataStore.getAppointments()) {
            rows.add(new String[] {
                    a.getAppointmentId(), a.getPatientId(), a.getClinicianId(),
                    a.getAppointmentDate(), a.getAppointmentTime(), a.getAppointmentType()
            });
        }
        CsvFileWriter.writeCsv(ApplicationDataStore.APPOINTMENT_CSV, rows);
    }
}
