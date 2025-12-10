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

    public boolean registerNewPatient(String name, Date dateOfBirth, String address,
                                      String phone, String email, String emergencyContact) {
        String patientID = patientService.generatePatientID();
        Patient patient = new Patient(patientID, name, dateOfBirth, address,
                phone, email, emergencyContact);
        return patientService.createPatient(patient);
    }

    public Patient findPatient(String patientID) {
        return patientService.getPatientByID(patientID);
    }

    public List<Patient> searchPatients(String searchTerm) {
        return patientService.searchPatients(searchTerm);
    }

    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }
}


