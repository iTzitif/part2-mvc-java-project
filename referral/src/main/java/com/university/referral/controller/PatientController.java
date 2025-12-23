package com.university.referral.controller;

import com.university.referral.model.*;
import com.university.referral.service.PatientService;

import java.util.Date;
import java.util.List;

public class PatientController {
    private PatientService patientService;

    public PatientController() {
        patientService = new PatientService();
    }

    public boolean registerNewPatient(String firstName, String lastName, Date dateOfBirth,
                                      String nhsNumber, String gender, String phoneNumber,
                                      String email, String address, String postcode,
                                      String emergencyContactName, String emergencyContactPhone,
                                      Date registrationDate, String gpSurgeryId) {
        String patientId = patientService.generatePatientID();
        Patient patient = new Patient(patientId, firstName, lastName, dateOfBirth,
                nhsNumber, gender, phoneNumber, email, address, postcode,
                emergencyContactName, emergencyContactPhone, registrationDate, gpSurgeryId);
        return patientService.createPatient(patient);
    }

    public boolean updatePatient(String patientId, String firstName, String lastName, Date dateOfBirth,
                                 String nhsNumber, String gender, String phoneNumber,
                                 String email, String address, String postcode,
                                 String emergencyContactName, String emergencyContactPhone,
                                 Date registrationDate, String gpSurgeryId) {
        Patient patient = new Patient(patientId, firstName, lastName, dateOfBirth,
                nhsNumber, gender, phoneNumber, email, address, postcode,
                emergencyContactName, emergencyContactPhone, registrationDate, gpSurgeryId);
        return patientService.updatePatient(patient);
    }

    public boolean deletePatient(String patientId) {
        return patientService.deletePatient(patientId);
    }

    public Patient findPatient(String patientId) {
        return patientService.getPatientByID(patientId);
    }

    public List<Patient> searchPatients(String searchTerm) {
        return patientService.searchPatients(searchTerm);
    }

    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    public int getPatientCount() {
        return patientService.getPatientCount();
    }
}
