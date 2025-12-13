package com.university.referral.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class Appointment {
    private String appointmentId;
    private String patientId;
    private String clinicianId;
    private String facilityId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private int durationMinutes;
    private String appointmentType;
    private String status;
    private String reasonForVisit;
    private String notes;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    // Constructor
    public Appointment(String appointmentId, String patientId, String clinicianId,
                       String facilityId, LocalDate appointmentDate, LocalTime appointmentTime,
                       int durationMinutes, String appointmentType, String reasonForVisit, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.facilityId = facilityId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.appointmentType = appointmentType;
        this.reasonForVisit = reasonForVisit;
        this.notes = notes;
        this.status = "Scheduled";
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    public Appointment() {

    }

    // Methods to update appointment
    public void reschedule(LocalDate newDate, LocalTime newTime) {
        this.appointmentDate = newDate;
        this.appointmentTime = newTime;
        this.lastModified = LocalDateTime.now();
    }

    public void cancel() {
        this.status = "Cancelled";
        this.lastModified = LocalDateTime.now();
    }

    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getClinicianId() { return clinicianId; }
    public String getFacilityId() { return facilityId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getAppointmentType() { return appointmentType; }
    public String getStatus() { return status; }
    public String getReasonForVisit() { return reasonForVisit; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getLastModified() { return lastModified; }

    public void setStatus(String status) {
        this.status = status;
        this.lastModified = LocalDateTime.now();
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.lastModified = LocalDateTime.now();
    }
}
