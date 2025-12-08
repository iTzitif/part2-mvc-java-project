package com.university.referral.model;

public class StaffMember {

    private String staffId;
    private String fullName;
    private String role;
    private String contactNumber;

    public StaffMember(String staffId, String fullName, String role, String contactNumber) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.role = role;
        this.contactNumber = contactNumber;
    }

    public String getStaffId() { return staffId; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getContactNumber() { return contactNumber; }

    public void setStaffId(String staffId) { this.staffId = staffId; }
}
