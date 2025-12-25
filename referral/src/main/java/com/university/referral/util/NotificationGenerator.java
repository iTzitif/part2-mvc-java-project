package com.university.referral.util;

import com.university.referral.model.Prescription;
import com.university.referral.model.Referral;
import com.university.referral.service.ClinicianService;
import com.university.referral.service.PatientService;

import java.text.SimpleDateFormat;

public class NotificationGenerator {
    private static NotificationGenerator instance;
    private SimpleDateFormat dateFormat;
    private PatientService patientService;
    private ClinicianService clinicianService;

    private NotificationGenerator() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        patientService = new PatientService();
        clinicianService = new ClinicianService();

    }

    public static synchronized NotificationGenerator getInstance() {
        if (instance == null) {
            instance = new NotificationGenerator();
        }
        return instance;
    }

    public String generateReferralEmail(Referral referral) {
        StringBuilder email = new StringBuilder();
        email.append("===============================================\n");
        email.append("          REFERRAL NOTIFICATION EMAIL          \n");
        email.append("===============================================\n\n");

        email.append("Referral ID: ").append(referral.getReferral_id()).append("\n\n");

        email.append("TO: Dr. ").append(referral.getReferral_id()).append("\n");
        email.append("Specialty: ").append(referral.getReferring_clinician_id()).append("\n");
        email.append("FROM: Dr. ").append(referral.getReferred_to_clinician_id()).append("\n");
        email.append("PATIENT INFORMATION:\n");
        email.append("Name: ").append(referral.getPatient_id()).append("\n");
        email.append("Patient ID: ").append(referral.getPatient_id()).append("\n");
        email.append("REFERRAL REASON:\n");
        email.append(referral.getNotes()).append("\n\n");

        email.append("CLINICAL NOTES:\n");
        email.append(referral.getNotes()).append("\n\n");

        email.append("Status: ").append(referral.getStatus()).append("\n");
        email.append("===============================================\n");

        return email.toString();
    }

    public String generatePrescriptionDocument(Prescription prescription) {
        StringBuilder doc = new StringBuilder();
        doc.append("===============================================\n");
        doc.append("               PRESCRIPTION                    \n");
        doc.append("===============================================\n\n");

        doc.append("Prescription ID: ").append(prescription.getPrescriptionId()).append("\n\n");
        doc.append("PATIENT INFORMATION:\n");
        doc.append("Name: ").append(patientService.getPatientByID(prescription.getPatientId())).append("\n");
        doc.append("Patient ID: ").append(prescription.getPatientId()).append("\n");

        doc.append("PRESCRIBER INFORMATION:\n");
        doc.append("Dr. ").append(prescription.getClinicianId()).append("\n");
        doc.append("Practice: ").append(clinicianService.getClinicianByID(prescription.getClinicianId())).append("\n\n");

        doc.append("MEDICATION DETAILS:\n");
        doc.append("Drug Name: ").append(prescription.getMedicationName()).append("\n");
        doc.append("Dosage: ").append(prescription.getDosage()).append("\n");
        doc.append("Quantity: ").append(prescription.getQuantity()).append("\n");
        doc.append("Duration: ").append(prescription.getDurationDays()).append("\n\n");

        doc.append("DIRECTIONS:\n");
        doc.append(prescription.getInstructions()).append("\n\n");

        doc.append("===============================================\n");

        return doc.toString();
    }

    public void saveReferralToFile(Referral referral) {
        String content = generateReferralEmail(referral);
        String filename = "referral_" + referral.getReferral_id() + ".txt";
        FileIOHandler.writeToFile("output/referrals/" + filename, content);
    }

    public void savePrescriptionToFile(Prescription prescription) {
        String content = generatePrescriptionDocument(prescription);
        String filename = "prescription_" + prescription.getPrescriptionId() + ".txt";
        FileIOHandler.writeToFile("output/prescriptions/" + filename, content);
    }
}
