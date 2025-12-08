package com.university.referral.model;

public class ClinicianProfile {

    private String clinicianId;
    private String fullName;
    private String role;
    private String contactNumber;

    public ClinicianProfile(String clinicianId, String fullName, String role, String contactNumber) {
        this.clinicianId = clinicianId;
        this.fullName = fullName;
        this.role = role;
        this.contactNumber = contactNumber;
    }

    public String getClinicianId() { return clinicianId; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getContactNumber() { return contactNumber; }

    public void setClinicianId(String clinicianId) { this.clinicianId = clinicianId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
