package com.university.referral.controller;

import com.university.referral.model.Prescription;
import com.university.referral.service.PrescriptionService;

import java.time.LocalDate;
import java.util.List;

public class PrescriptionController {

    private PrescriptionService prescriptionService;

    public PrescriptionController() {
        prescriptionService = new PrescriptionService();
    }

    public boolean createPrescription(String patientId,
                                      String clinicianId,
                                      String appointmentId,
                                      LocalDate prescriptionDate,
                                      String medicationName,
                                      String dosage,
                                      String frequency,
                                      int durationDays,
                                      String quantity,
                                      String instructions,
                                      String pharmacyName,
                                      String status,
                                      LocalDate issueDate,
                                      LocalDate collectionDate) {

        String prescriptionId = prescriptionService.generatePrescriptionID();

        Prescription prescription = new Prescription(
                prescriptionId,
                patientId,
                clinicianId,
                appointmentId,
                prescriptionDate,
                medicationName,
                dosage,
                frequency,
                durationDays,
                quantity,
                instructions,
                pharmacyName,
                status,
                issueDate,
                collectionDate
        );

        return prescriptionService.createPrescription(prescription);
    }

    public List<Prescription> getPatientPrescriptions(String patientId) {
        return prescriptionService.getPrescriptionsByPatient(patientId);
    }
    public List<Prescription> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }
    public Prescription findPrescription(String prescriptionId) {
        return prescriptionService.getPrescriptionByID(prescriptionId);
    }
    public boolean deletePrescription(String prescriptionId) {
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            return false;
        }
        return prescriptionService.deletePrescription(prescriptionId);
    }
}
