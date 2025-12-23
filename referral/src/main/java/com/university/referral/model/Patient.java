package com.university.referral.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
    private String patientId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String nhsNumber;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private String postcode;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private Date registrationDate;
    private String gpSurgeryId;

    private List<Appointment> appointments;

    public Patient(String patientId, String firstName, String lastName, Date dateOfBirth,
                   String nhsNumber, String gender, String phoneNumber, String email,
                   String address, String postcode, String emergencyContactName,
                   String emergencyContactPhone, Date registrationDate, String gpSurgeryId) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.registrationDate = registrationDate;
        this.gpSurgeryId = gpSurgeryId;
        this.appointments = new ArrayList<>();
    }

    public String getPatientId() { return patientId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public String getNhsNumber() { return nhsNumber; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPostcode() { return postcode; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public Date getRegistrationDate() { return registrationDate; }
    public String getGpSurgeryId() { return gpSurgeryId; }
    public List<Appointment> getAppointments() { return appointments; }
}
