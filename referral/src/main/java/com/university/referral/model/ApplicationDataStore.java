package com.university.referral.model;

import com.university.referral.util.CsvFileReader;

import java.util.ArrayList;
import java.util.List;

public class ApplicationDataStore {

    private static ApplicationDataStore instance;

    public static final String DATA_DIR = "data";
    public static final String PATIENT_CSV = DATA_DIR + "/patients.csv";
    public static final String APPOINTMENT_CSV = DATA_DIR + "/appointments.csv";

    private final List<PatientRecord> patientRecords = new ArrayList<>();
    private final List<AppointmentRecord> appointmentRecords = new ArrayList<>();

    private ApplicationDataStore() { }

    public static ApplicationDataStore getInstance() {
        if (instance == null) instance = new ApplicationDataStore();
        return instance;
    }

    // getters
    public List<PatientRecord> getPatients() { return patientRecords; }
    public List<AppointmentRecord> getAppointments() { return appointmentRecords; }

    // load all datasets
    public void loadApplicationData() {
        loadPatientRecords();   // assume already implemented for patient module
        loadAppointmentRecords();

    }

    private void loadPatientRecords() {
        List<String[]> rows = CsvFileReader.readCsvFile(PATIENT_CSV);
        for (String[] c : rows) {
            if (c.length < 5) continue;
            patientRecords.add(new PatientRecord(c[0], c[1], c[2], c[3], c[4]));
        }
    }



    private void loadAppointmentRecords() {
        List<String[]> rows = CsvFileReader.readCsvFile(APPOINTMENT_CSV);
        for (String[] c : rows) {
            if (c.length < 6) continue;
            appointmentRecords.add(new AppointmentRecord(c[0], c[1], c[2], c[3], c[4], c[5]));
        }
    }


}
