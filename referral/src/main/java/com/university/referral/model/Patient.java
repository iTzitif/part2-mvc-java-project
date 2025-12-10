package com.university.referral.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
    private String patientID;
    private String name;
    private Date dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private String emergencyContact;
    private List<Appointment> appointments;
    private List<MedicalRecord> medicalRecords;

    public Patient(String patientID, String name, Date dateOfBirth,
                   String address, String phone, String email,
                   String emergencyContact) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.appointments = new ArrayList<>();
        this.medicalRecords = new ArrayList<>();
    }

    public void bookAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void cancelAppointment(String appointmentID) {
        appointments.removeIf(apt -> apt.getAppointmentID().equals(appointmentID));
    }

    public void viewMedicalRecord() {
        // Implementation for viewing records
    }

    // Getters and setters
    public String getPatientID() { return patientID; }
    public String getName() { return name; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getEmergencyContact() { return emergencyContact; }
    public List<Appointment> getAppointments() { return appointments; }
    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
}