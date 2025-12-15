package com.university.referral.model;

import java.time.LocalDate;

public class Referral {
    private String referral_id;
    private String patient_id;
    private String referring_clinician_id;
    private String referred_to_clinician_id;
    private String referring_facility_id;
    private String referred_to_facility_id;
    private LocalDate referral_date;
    private String urgency_level;
    private String referral_reason;
    private String clinical_summary;
    private String requested_investigations;
    private String status;
    private String appointment_id;
    private String notes;
    private LocalDate created_date;
    private LocalDate last_updated;

    public Referral(String referral_id,
                    String patient_id,
                    String referring_clinician_id,
                    String referred_to_clinician_id,
                    String referring_facility_id,
                    String referred_to_facility_id,
                    LocalDate referral_date,
                    String urgency_level,
                    String referral_reason,
                    String clinical_summary,
                    String requested_investigations,
                    String status,
                    String appointment_id,
                    String notes) {
        this.referral_id = referral_id;
        this.patient_id = patient_id;
        this.referring_clinician_id = referring_clinician_id;
        this.referred_to_clinician_id = referred_to_clinician_id;
        this.referring_facility_id = referring_facility_id;
        this.referred_to_facility_id = referred_to_facility_id;
        this.referral_date = referral_date != null ? referral_date : LocalDate.now();
        this.urgency_level = urgency_level;
        this.referral_reason = referral_reason;
        this.clinical_summary = clinical_summary;
        this.requested_investigations = requested_investigations;
        this.status = status != null ? status : "Pending";
        this.appointment_id = appointment_id;
        this.notes = notes;
        this.created_date = LocalDate.now();
        this.last_updated = LocalDate.now();
    }

    // Getters
    public String getReferral_id() { return referral_id; }
    public String getPatient_id() { return patient_id; }
    public String getReferring_clinician_id() { return referring_clinician_id; }
    public String getReferred_to_clinician_id() { return referred_to_clinician_id; }
    public String getReferring_facility_id() { return referring_facility_id; }
    public String getReferred_to_facility_id() { return referred_to_facility_id; }
    public LocalDate getReferral_date() { return referral_date; }
    public String getUrgency_level() { return urgency_level; }
    public String getReferral_reason() { return referral_reason; }
    public String getClinical_summary() { return clinical_summary; }
    public String getRequested_investigations() { return requested_investigations; }
    public String getStatus() { return status; }
    public String getAppointment_id() { return appointment_id; }
    public String getNotes() { return notes; }
    public LocalDate getCreated_date() { return created_date; }
    public LocalDate getLast_updated() { return last_updated; }

    // Setters
    public void setStatus(String status) {
        this.status = status;
        this.last_updated = LocalDate.now();
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
        this.last_updated = LocalDate.now();
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.last_updated = LocalDate.now();
    }

    // Optional setters for created_date and last_updated
    public void setCreated_date(LocalDate created_date) {
        this.created_date = created_date;
    }

    public void setLast_updated(LocalDate last_updated) {
        this.last_updated = last_updated;
    }
}
