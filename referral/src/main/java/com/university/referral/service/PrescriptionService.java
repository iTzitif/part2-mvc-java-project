package com.university.referral.service;

import com.university.referral.model.Prescription;
import com.university.referral.util.CSVDataStore;
import com.university.referral.util.NotificationGenerator;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionService {

    private static final String PRESCRIPTION_FILE = "data/prescriptions.csv";

    private static final String[] HEADERS = {
            "prescription_id", "patient_id", "clinician_id", "appointment_id",
            "prescription_date", "medication_name", "dosage", "frequency",
            "duration_days", "quantity", "instructions", "pharmacy_name",
            "status", "issue_date", "collection_date"
    };

    private CSVDataStore dataStore;
    private List<Prescription> prescriptions;
    private NotificationGenerator notificationGenerator;

    public PrescriptionService() {
        dataStore = CSVDataStore.getInstance();
        prescriptions = new ArrayList<>();
        notificationGenerator = NotificationGenerator.getInstance();

        File file = new File(PRESCRIPTION_FILE);
        if (!file.exists()) {
            dataStore.createFileWithHeaders(PRESCRIPTION_FILE, HEADERS);
        }

        loadPrescriptionsFromFile();
    }

    // ===================== LOAD =====================
    private void loadPrescriptionsFromFile() {
        List<String[]> rows = dataStore.loadData(PRESCRIPTION_FILE);

        for (String[] v : rows) {
            if (v.length < 15) {
                System.err.println("Invalid prescription entry");
                continue;
            }

            try {
                Prescription prescription = new Prescription(
                        v[0],
                        v[1],
                        v[2],
                        v[3],
                        parseDate(v[4]),
                        v[5],
                        v[6],
                        v[7],
                        Integer.parseInt(v[8]),
                        v[9],
                        v[10],
                        v[11],
                        v[12],
                        parseDate(v[13]),
                        parseDate(v[14])
                );

                prescriptions.add(prescription);

            } catch (Exception e) {
                System.err.println("Error parsing prescription row");
                e.printStackTrace();
            }
        }
    }

    // ===================== CREATE =====================
    public boolean createPrescription(Prescription prescription) {
        prescriptions.add(prescription);
        notificationGenerator.savePrescriptionToFile(prescription);
        return dataStore.appendData(PRESCRIPTION_FILE, toCSVArray(prescription));
    }

    // ===================== READ =====================
    public List<Prescription> getPrescriptionsByPatient(String patientId) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription p : prescriptions) {
            if (p.getPatientId().equals(patientId)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions);
    }

    public Prescription getPrescriptionByID(String prescriptionId) {
        for (Prescription p : prescriptions) {
            if (p.getPrescriptionId().equals(prescriptionId)) {
                return p;
            }
        }
        return null;
    }

    // ===================== DELETE =====================
    public boolean deletePrescription(String prescriptionId) {
        boolean removed = prescriptions.removeIf(
                p -> p.getPrescriptionId().equals(prescriptionId)
        );
        return removed && rewriteFile();
    }

    // ===================== FILE REWRITE =====================
    private boolean rewriteFile() {
        if (!dataStore.createFileWithHeaders(PRESCRIPTION_FILE, HEADERS)) {
            return false;
        }

        for (Prescription p : prescriptions) {
            if (!dataStore.appendData(PRESCRIPTION_FILE, toCSVArray(p))) {
                return false;
            }
        }
        return true;
    }

    // ===================== HELPERS =====================
    private String[] toCSVArray(Prescription p) {
        return new String[] {
                p.getPrescriptionId(),
                p.getPatientId(),
                p.getClinicianId(),
                p.getAppointmentId(),
                safeDate(p.getPrescriptionDate()),
                p.getMedicationName(),
                p.getDosage(),
                p.getFrequency(),
                String.valueOf(p.getDurationDays()),
                p.getQuantity(),
                p.getInstructions(),
                p.getPharmacyName(),
                p.getStatus(),
                safeDate(p.getIssueDate()),
                safeDate(p.getCollectionDate())
        };
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("null")) {
            return null;
        }
        return LocalDate.parse(value);
    }

    private String safeDate(LocalDate d) {
        return d == null ? "" : d.toString();
    }

    public String generatePrescriptionID() {
        return "PRX" + System.currentTimeMillis();
    }

    public void refreshPrescriptions() {
        prescriptions.clear();
        loadPrescriptionsFromFile();
    }
}
