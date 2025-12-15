package com.university.referral.service;

import com.university.referral.model.MedicalFacility;
import com.university.referral.model.Referral;
import com.university.referral.model.SingletonReferralManager;
import com.university.referral.util.CSVDataStore;
import com.university.referral.util.NotificationGenerator;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ReferralService {
    private SingletonReferralManager referralManager;
    private NotificationGenerator notificationGenerator;
    private CSVDataStore dataStore;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public ReferralService() {
        referralManager = SingletonReferralManager.getInstance();
        notificationGenerator = NotificationGenerator.getInstance();
        dataStore = CSVDataStore.getInstance();
        loadReferralsFromFile();
    }

    private void loadReferralsFromFile() {
        File file = new File("data/referrals.csv");
        if (!file.exists()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // adjust as needed

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] values = line.split(",", -1); // include empty fields
                if (values.length >= 16) {
                    try {
                        LocalDate referralDate = LocalDate.parse(values[6], DATE_FORMAT);
//                        LocalDate createdDate = LocalDate.parse(values[16], DATE_FORMAT);
//                        LocalDate lastUpdated = LocalDate.parse(values[15], DATE_FORMAT);
                        Referral referral = new Referral(
                                values[0], // referral_id
                                values[1], // patient_id
                                values[2], // referring_clinician_id
                                values[3], // referred_to_clinician_id
                                values[4], // referring_facility_id
                                values[5], // referred_to_facility_id
                                referralDate,
                                values[7], // urgency_level
                                values[8], // referral_reason
                                values[9], // clinical_summary
                                values[10], // requested_investigations
                                values[11], // status
                                values[12], // appointment_id
                                values[13]  // notes
                        );

                        // Override createdDate and lastUpdated if needed
                        referralManager.addReferral(referral);

                    } catch (NumberFormatException e) {
                        System.err.println("Date parsing error in line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading referrals file: " + e.getMessage());
        }
    }

    public boolean createReferral(Referral referral) {
        referralManager.addReferral(referral);
        notificationGenerator.saveReferralToFile(referral);
        return saveReferralToFile(referral);
    }

    public List<Referral> getAllReferrals() {
        return referralManager.getAllReferrals();
    }

    public List<Referral> getReferralsByPatient(String patientId) {
        return referralManager.getReferralsByPatient(patientId);
    }
    public List<Referral> getReferralsByClinician(String clinicianId) {
        return referralManager.getReferralsByClinician(clinicianId);
    }
    public List<Referral> getReferralsByReferredToClinicianId(String referredToClinicianId) {

        return referralManager.getReferralsByReferredToClinicianId(referredToClinicianId);
    }


    public Referral getReferralByID(String referralId) {
        return referralManager.getReferralByID(referralId);
    }

    public Referral processNextReferral() {
        return referralManager.processNextReferral();
    }

    public boolean updateReferralStatus(String referralId, String newStatus) {
        Referral referral = referralManager.getReferralByID(referralId);
        if (referral != null) {
            referral.setStatus(newStatus);
            updateReferralInFile(referral);
            return true;
        }
        return false;
    }

    private void updateReferralInFile(Referral updatedReferral) {
        File inputFile = new File("data/referrals.csv");
        File tempFile = new File("data/referrals_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) { writer.write(line); writer.newLine(); firstLine = false; continue; }
                String[] values = line.split(",", -1);
                if (values.length >= 16) {
                    if (values[0].equals(updatedReferral.getReferral_id())) {
                        String[] updatedData = {
                                updatedReferral.getReferral_id(),
                                updatedReferral.getPatient_id(),
                                updatedReferral.getReferring_clinician_id(),
                                updatedReferral.getReferred_to_clinician_id(),
                                updatedReferral.getReferring_facility_id(),
                                updatedReferral.getReferred_to_facility_id(),
                                String.valueOf(updatedReferral.getReferral_date()),
                                updatedReferral.getUrgency_level(),
                                updatedReferral.getReferral_reason(),
                                updatedReferral.getClinical_summary(),
                                updatedReferral.getRequested_investigations(),
                                updatedReferral.getStatus(),
                                updatedReferral.getAppointment_id() != null ? updatedReferral.getAppointment_id() : "",
                                updatedReferral.getNotes(),
                                String.valueOf(updatedReferral.getCreated_date()),
                                String.valueOf(updatedReferral.getLast_updated())
                        };
                        writer.write(String.join(",", updatedData));
                        writer.newLine();
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                } else {
                    writer.write(line); writer.newLine();
                }
            }

        } catch (IOException e) { e.printStackTrace(); return; }

        if (inputFile.delete()) tempFile.renameTo(inputFile);
    }

    private boolean saveReferralToFile(Referral referral) {
        String[] data = {
                referral.getReferral_id(),
                referral.getPatient_id(),
                referral.getReferring_clinician_id(),
                referral.getReferred_to_clinician_id(),
                referral.getReferring_facility_id(),
                referral.getReferred_to_facility_id(),
                String.valueOf(referral.getReferral_date()),
                referral.getUrgency_level(),
                referral.getReferral_reason(),
                referral.getClinical_summary(),
                referral.getRequested_investigations(),
                referral.getStatus(),
                referral.getAppointment_id() != null ? referral.getAppointment_id() : "",
                referral.getNotes(),
                String.valueOf(referral.getCreated_date()),
                String.valueOf(referral.getLast_updated())
        };
        return dataStore.appendData("data/referrals.csv", data);
    }

    public String generateReferralID() {
        return "REF" + System.currentTimeMillis();
    }
}

