package com.university.referral.model;

import java.io.Serializable;
import java.util.Date;

public class Clinician implements Serializable {

    private String clinicianID;
    private String firstName;
    private String lastName;
    private String title;
    private String speciality;
    private String gmcNumber;
    private String phoneNumber;
    private String email;
    private String workplaceID;
    private String workplaceType;
    private String employmentStatus;
    private Date startDate;

    public Clinician(String clinicianID, String firstName, String lastName, String title,
                     String speciality, String gmcNumber, String phoneNumber,
                     String email, String workplaceID, String workplaceType,
                     String employmentStatus, Date startDate) {

        this.clinicianID = clinicianID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.speciality = speciality;
        this.gmcNumber = gmcNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.workplaceID = workplaceID;
        this.workplaceType = workplaceType;
        this.employmentStatus = employmentStatus;
        this.startDate = startDate;
    }

    public String getClinicianID() { return clinicianID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getTitle() { return title; }
    public String getSpeciality() { return speciality; }
    public String getGmcNumber() { return gmcNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getWorkplaceID() { return workplaceID; }
    public String getWorkplaceType() { return workplaceType; }
    public String getEmploymentStatus() { return employmentStatus; }
    public Date getStartDate() { return startDate; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setTitle(String title) { this.title = title; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }
    public void setGmcNumber(String gmcNumber) { this.gmcNumber = gmcNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setWorkplaceID(String workplaceID) { this.workplaceID = workplaceID; }
    public void setWorkplaceType(String workplaceType) { this.workplaceType = workplaceType; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
}
