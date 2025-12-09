package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.AppointmentRecord;
import com.university.referral.util.CsvFileWriter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {

    private final ApplicationDataStore dataStore;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AppointmentController(ApplicationDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<AppointmentRecord> getAll() {
        return dataStore.getAppointments();
    }

    public void addAppointment(AppointmentRecord record) {
        dataStore.getAppointments().add(record);
        saveAll();
    }

    public void updateAppointment(int index, AppointmentRecord updatedRecord) {
        if (index >= 0 && index < dataStore.getAppointments().size()) {
            dataStore.getAppointments().set(index, updatedRecord);
            saveAll();
        }
    }

    public void deleteAppointment(int index) {
        if (index >= 0 && index < dataStore.getAppointments().size()) {
            dataStore.getAppointments().remove(index);
            saveAll();
        }
    }

    private void saveAll() {
        List<String[]> rows = new ArrayList<>();
        for (AppointmentRecord a : dataStore.getAppointments()) {
            rows.add(new String[]{
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getClinicianId(),
                    a.getFacilityId(),
                    a.getAppointmentDate().format(dateFormatter),
                    a.getAppointmentTime().format(timeFormatter),
                    String.valueOf(a.getDurationMinutes()),
                    a.getAppointmentType(),
                    a.getStatus(),
                    a.getReasonForVisit(),
                    a.getNotes(),
                    a.getCreatedDate().format(dateTimeFormatter),
                    a.getLastModified().format(dateTimeFormatter)
            });
        }
        CsvFileWriter.writeCsv(ApplicationDataStore.APPOINTMENT_CSV, rows);
    }
}
