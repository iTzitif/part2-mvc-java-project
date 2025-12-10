package com.university.referral.service;

import com.university.referral.util.CSVDataStore;
import com.university.referral.model.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class PatientService {
    private CSVDataStore dataStore;
    private List<Patient> patients;

    public PatientService() {
        dataStore = CSVDataStore.getInstance();
        patients = new ArrayList<>();
        loadPatients();
    }

    private void loadPatients() {
        List<String[]> data = dataStore.loadData("data/patients.csv");
        for (String[] row : data) {
            if (row.length >= 7) {
                Patient patient = new Patient(
                        row[0], // patientID
                        row[1], // name
                        parseDate(row[2]), // dateOfBirth
                        row[3], // address
                        row[4], // phone
                        row[5], // email
                        row[6]  // emergencyContact
                );
                patients.add(patient);
            }
        }
    }

    public boolean createPatient(Patient patient) {
        patients.add(patient);
        return savePatientToFile(patient);
    }

    public Patient getPatientByID(String patientID) {
        for (Patient patient : patients) {
            if (patient.getPatientID().equals(patientID)) {
                return patient;
            }
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    public List<Patient> searchPatients(String searchTerm) {
        List<Patient> results = new ArrayList<>();
        for (Patient patient : patients) {
            if (patient.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    patient.getPatientID().contains(searchTerm)) {
                results.add(patient);
            }
        }
        return results;
    }

    private boolean savePatientToFile(Patient patient) {
        String[] data = {
                patient.getPatientID(),
                patient.getName(),
                patient.getDateOfBirth().toString(),
                patient.getAddress(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getEmergencyContact()
        };
        return dataStore.appendData("data/patients.csv", data);
    }

    private Date parseDate(String dateStr) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }

    public String generatePatientID() {
        return "PAT" + System.currentTimeMillis();
    }
}


