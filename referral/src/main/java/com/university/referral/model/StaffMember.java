package com.university.referral.model;

import java.time.LocalDate;

public class StaffMember {

    private String staffId;
    private String firstName;
    private String lastName;
    private String role;
    private String department;
    private String facilityId;
    private String phoneNumber;
    private String email;
    private String employmentStatus;
    private LocalDate startDate;
    private String lineManager;
    private String accessLevel;

    public StaffMember(String staffId, String firstName, String lastName, String role,
                       String department, String facilityId, String phoneNumber, String email,
                       String employmentStatus, LocalDate startDate, String lineManager,
                       String accessLevel) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.facilityId = facilityId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.employmentStatus = employmentStatus;
        this.startDate = startDate;
        this.lineManager = lineManager;
        this.accessLevel = accessLevel;
    }

    public StaffMember(String userID, String username, String password, String name, String email, String phone, String staffID, String specialty, String qualifications) {
    }

    // Getters
    public String getStaffId() { return staffId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }
    public String getFacilityId() { return facilityId; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getEmploymentStatus() { return employmentStatus; }
    public LocalDate getStartDate() { return startDate; }
    public String getLineManager() { return lineManager; }
    public String getAccessLevel() { return accessLevel; }

    // Setters
    public void setStaffId(String staffId) { this.staffId = staffId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRole(String role) { this.role = role; }
    public void setDepartment(String department) { this.department = department; }
    public void setFacilityId(String facilityId) { this.facilityId = facilityId; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setLineManager(String lineManager) { this.lineManager = lineManager; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
}
