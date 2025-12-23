package com.university.referral.model;

public class ReferralRequest {

    private String referralId;
    private String patientId;
    private String referringClinicianId;
    private String referredToClinicianId;
    private String date;
    private String urgency;
    private String clinicalSummary;
    private String status;

    public ReferralRequest() { }

    public ReferralRequest(String referralId, String patientId, String referringClinicianId,
                           String referredToClinicianId, String date, String urgency, String clinicalSummary,
                           String status) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.referringClinicianId = referringClinicianId;
        this.referredToClinicianId = referredToClinicianId;
        this.date = date;
        this.urgency = urgency;
        this.clinicalSummary = clinicalSummary;
        this.status = status;
    }

    public String getReferralId() { return referralId; }
    public String getPatientId() { return patientId; }
    public String getReferringClinicianId() { return referringClinicianId; }
    public String getReferredToClinicianId() { return referredToClinicianId; }
    public String getDate() { return date; }
    public String getUrgency() { return urgency; }
    public String getClinicalSummary() { return clinicalSummary; }
    public String getStatus() { return status; }

    public void setReferralId(String referralId) { this.referralId = referralId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setReferringClinicianId(String referringClinicianId) { this.referringClinicianId = referringClinicianId; }
    public void setReferredToClinicianId(String referredToClinicianId) { this.referredToClinicianId = referredToClinicianId; }
    public void setDate(String date) { this.date = date; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
    public void setClinicalSummary(String clinicalSummary) { this.clinicalSummary = clinicalSummary; }
    public void setStatus(String status) { this.status = status; }
}
