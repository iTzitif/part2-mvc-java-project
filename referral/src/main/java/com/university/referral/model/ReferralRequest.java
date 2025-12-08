package com.university.referral.model;

public class ReferralRequest {

    private String referralId;
    private String patientId;
    private String referringClinicianId;
    private String referredFacilityId;
    private String date;
    private String urgency;
    private String clinicalSummary;

    public ReferralRequest(String referralId, String patientId, String referringClinicianId,
                           String referredFacilityId, String date, String urgency, String clinicalSummary) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.referringClinicianId = referringClinicianId;
        this.referredFacilityId = referredFacilityId;
        this.date = date;
        this.urgency = urgency;
        this.clinicalSummary = clinicalSummary;
    }

    public String getReferralId() { return referralId; }
    public String getPatientId() { return patientId; }
    public String getReferringClinicianId() { return referringClinicianId; }
    public String getReferredFacilityId() { return referredFacilityId; }
    public String getDate() { return date; }
    public String getUrgency() { return urgency; }
    public String getClinicalSummary() { return clinicalSummary; }
}
