package com.university.referral.model;

public class PrescriptionEntry {

    private String prescriptionId;
    private String appointmentId;
    private String patientId;
    private String clinicianId;
    private String medicationDetails;
    private String dosage;
    private String instructions;

    public PrescriptionEntry(String prescriptionId, String appointmentId, String patientId,
                             String clinicianId, String medicationDetails, String dosage, String instructions) {
        this.prescriptionId = prescriptionId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.medicationDetails = medicationDetails;
        this.dosage = dosage;
        this.instructions = instructions;
    }

    public String getPrescriptionId() { return prescriptionId; }
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getClinicianId() { return clinicianId; }
    public String getMedicationDetails() { return medicationDetails; }
    public String getDosage() { return dosage; }
    public String getInstructions() { return instructions; }

    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }
}
