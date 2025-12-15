package com.university.referral.service;

import com.university.referral.model.Prescription;
import com.university.referral.util.CSVDataStore;
import com.university.referral.util.NotificationGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionService {

    private CSVDataStore dataStore;
    private List<Prescription> prescriptions;
    private NotificationGenerator notificationGenerator;

    public PrescriptionService() {
        dataStore = CSVDataStore.getInstance();
        prescriptions = new ArrayList<>();
        notificationGenerator = NotificationGenerator.getInstance();
        loadPrescriptionsFromFile();
    }

    private void loadPrescriptionsFromFile() {

        File file = new File("data/prescriptions.csv");
        if (!file.exists()) {
            System.out.println("Prescription file does not exist yet. Starting fresh.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header row
                }

                String[] v = line.split(",");

                if (v.length < 15) {
                    System.err.println("Invalid prescription entry: " + line);
                    continue;
                }

                try {

                    Prescription prescription = new Prescription(
                            v[0],                                   // prescription_id
                            v[1],                                   // patient_id
                            v[2],                                   // clinician_id
                            v[3],                                   // appointment_id
                            parseDate(v[4]),                        // prescription_date
                            v[5],                                   // medication_name
                            v[6],                                   // dosage
                            v[7],                                   // frequency
                            Integer.parseInt(v[8]),                 // duration_days
                            v[9],                                   // quantity
                            v[10],                                  // instructions
                            v[11],                                  // pharmacy_name
                            v[12],                                  // status
                            parseDate(v[13]),                       // issue_date
                            parseDate(v[14])                        // collection_date
                    );

                    prescriptions.add(prescription);

                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    e.printStackTrace();
                }
            }

            System.out.println("Loaded " + prescriptions.size() + " prescriptions from file.");

        } catch (IOException e) {
            System.err.println("Error reading prescription file: " + e.getMessage());
        }
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.equals("") || value.equals("null"))
            return null;
        return LocalDate.parse(value);
    }

    public boolean createPrescription(Prescription prescription) {
        prescriptions.add(prescription);

        // store notification, if any
        notificationGenerator.savePrescriptionToFile(prescription);

        return savePrescriptionToFile(prescription);
    }

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

    private boolean savePrescriptionToFile(Prescription p) {

        String[] data = {
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

        return dataStore.appendData("data/prescriptions.csv", data);
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
    public boolean deletePrescription(String prescriptionId) {
        // Find the prescription in the in-memory list
        Prescription toDelete = null;
        for (Prescription p : prescriptions) {
            if (p.getPrescriptionId().equals(prescriptionId)) {
                toDelete = p;
                break;
            }
        }

        if (toDelete == null) {
            return false; // not found
        }

        // Remove from list
        prescriptions.remove(toDelete);

        // Rewrite the CSV file to reflect deletion
        return saveAllPrescriptionsToFile();
    }
    private boolean saveAllPrescriptionsToFile() {
        // First, overwrite the CSV file with the header
        String[] header = {
                "prescription_id", "patient_id", "clinician_id", "appointment_id",
                "prescription_date", "medication_name", "dosage", "frequency",
                "duration_days", "quantity", "instructions", "pharmacy_name",
                "status", "issue_date", "collection_date"
        };

        if (!dataStore.createFileWithHeaders("data/prescriptions.csv", header)) {
            return false;
        }

        // Append all current prescriptions
        boolean success = true;
        for (Prescription p : prescriptions) {
            success &= savePrescriptionToFile(p); // reuse existing method
        }
        return success;
    }


}
