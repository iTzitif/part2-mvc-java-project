package com.university.referral.model;

import com.university.referral.util.CsvFileReader;

import java.util.ArrayList;
import java.util.List;

public class ApplicationDataStore {

    private static ApplicationDataStore instance;

    public static final String DATA_DIR = "data";
    public static final String PATIENT_CSV = DATA_DIR + "/patients.csv";
    public static final String CLINICIAN_CSV = DATA_DIR + "/clinicians.csv";
    public static final String APPOINTMENT_CSV = DATA_DIR + "/appointments.csv";
    public static final String PRESCRIPTION_CSV = DATA_DIR + "/prescriptions.csv";
    public static final String REFERRAL_CSV = DATA_DIR + "/referrals.csv";

    private final List<PatientRecord> patientRecords = new ArrayList<>();
    private final List<ClinicianProfile> clinicianProfiles = new ArrayList<>();
    private final List<AppointmentRecord> appointmentRecords = new ArrayList<>();
    private final List<PrescriptionEntry> prescriptionEntries = new ArrayList<>();
    private final List<ReferralRequest> referralRequests = new ArrayList<>();

    private ApplicationDataStore() { }

    public static ApplicationDataStore getInstance() {
        if (instance == null) instance = new ApplicationDataStore();
        return instance;
    }

    // getters
    public List<PatientRecord> getPatients() { return patientRecords; }
    public List<ClinicianProfile> getClinicians() { return clinicianProfiles; }
    public List<AppointmentRecord> getAppointments() { return appointmentRecords; }
    public List<PrescriptionEntry> getPrescriptions() { return prescriptionEntries; }
    public List<ReferralRequest> getReferrals() { return referralRequests; }


    // load all datasets
    public void loadApplicationData() {
        loadPatientRecords();   // assume already implemented for patient module
        loadClinicianProfiles();
        loadAppointmentRecords();
        loadPrescriptionEntries();
        loadReferralRequests();
    }

    private void loadPatientRecords() {
        List<String[]> rows = CsvFileReader.readCsvFile(PATIENT_CSV);
        for (String[] c : rows) {
            if (c.length < 5) continue;
            patientRecords.add(new PatientRecord(c[0], c[1], c[2], c[3], c[4]));
        }
    }

    private void loadClinicianProfiles() {
        List<String[]> rows = CsvFileReader.readCsvFile(CLINICIAN_CSV);
        for (String[] c : rows) {
            if (c.length < 4) continue;
            clinicianProfiles.add(new ClinicianProfile(c[0], c[1], c[2], c[3]));
        }
    }

    private void loadAppointmentRecords() {
        List<String[]> rows = CsvFileReader.readCsvFile(APPOINTMENT_CSV);
        for (String[] c : rows) {
            if (c.length < 6) continue;
            appointmentRecords.add(new AppointmentRecord(c[0], c[1], c[2], c[3], c[4], c[5]));
        }
    }

    private void loadPrescriptionEntries() {
        List<String[]> rows = CsvFileReader.readCsvFile(PRESCRIPTION_CSV);
        for (String[] c : rows) {
            if (c.length < 7) continue;
            prescriptionEntries.add(new PrescriptionEntry(
                    c[0], c[1], c[2], c[3], c[4], c[5], c[6]
            ));
        }
    }

    private void loadReferralRequests() {
        List<String[]> rows = CsvFileReader.readCsvFile(REFERRAL_CSV);
        for (String[] c : rows) {
            if (c.length < 7) continue;
            referralRequests.add(new ReferralRequest(c[0], c[1], c[2], c[3], c[4], c[5], c[6]));
        }
    }

}
