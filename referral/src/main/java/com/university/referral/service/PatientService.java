package com.university.referral.service;

import com.university.referral.model.Patient;
import com.university.referral.util.CSVDataStore;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientService {
    private CSVDataStore dataStore;
    private List<Patient> patients;
    private SimpleDateFormat dateFormat;

    public PatientService() {
        dataStore = CSVDataStore.getInstance();
        patients = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        loadPatientsFromFile();
    }

    private void loadPatientsFromFile() {
        File file = new File("data/patients.csv");
        if (!file.exists()) {
            System.out.println("Patients file does not exist yet. Starting fresh.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 14) { // Updated number of fields
                    try {
                        String patientId = values[0].trim();
                        String firstName = values[1].trim();
                        String lastName = values[2].trim();
                        Date dateOfBirth = parseDate(values[3].trim());
                        String nhsNumber = values[4].trim();
                        String gender = values[5].trim();
                        String phoneNumber = values[6].trim();
                        String email = values[7].trim();
                        String address = values[8].trim();
                        String postcode = values[9].trim();
                        String emergencyContactName = values[10].trim();
                        String emergencyContactPhone = values[11].trim();
                        Date registrationDate = parseDate(values[12].trim());
                        String gpSurgeryId = values[13].trim();

                        Patient patient = new Patient(
                                patientId, firstName, lastName, dateOfBirth, nhsNumber, gender,
                                phoneNumber, email, address, postcode,
                                emergencyContactName, emergencyContactPhone, registrationDate, gpSurgeryId
                        );
                        patients.add(patient);
                    } catch (Exception e) {
                        System.err.println("Error parsing patient line: " + line);
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Loaded " + patients.size() + " patients from file.");

        } catch (IOException e) {
            System.err.println("Error reading patients file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updatePatientInFile(Patient updatedPatient) {
        File inputFile = new File("data/patients.csv");
        File tempFile = new File("data/patients_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    writer.write(line);
                    writer.newLine();
                    firstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) continue;

                String[] values = line.split(",");
                if (values.length >= 14) {
                    String patientId = values[0].trim();

                    if (patientId.equals(updatedPatient.getPatientId())) {
                        String[] updatedData = {
                                updatedPatient.getPatientId(),
                                updatedPatient.getFirstName(),
                                updatedPatient.getLastName(),
                                dateFormat.format(updatedPatient.getDateOfBirth()),
                                updatedPatient.getNhsNumber(),
                                updatedPatient.getGender(),
                                updatedPatient.getPhoneNumber(),
                                updatedPatient.getEmail(),
                                updatedPatient.getAddress(),
                                updatedPatient.getPostcode(),
                                updatedPatient.getEmergencyContactName(),
                                updatedPatient.getEmergencyContactPhone(),
                                dateFormat.format(updatedPatient.getRegistrationDate()),
                                updatedPatient.getGpSurgeryId()
                        };
                        writer.write(String.join(",", updatedData));
                        writer.newLine();
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error updating patient in file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Could not rename temp file to original file");
            }
        } else {
            System.err.println("Could not delete original file");
        }
    }

    public boolean createPatient(Patient patient) {
        patients.add(patient);
        return savePatientToFile(patient);
    }

    public boolean updatePatient(Patient patient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientId().equals(patient.getPatientId())) {
                patients.set(i, patient);
                updatePatientInFile(patient);
                return true;
            }
        }
        return false;
    }

    public boolean deletePatient(String patientId) {
        Patient patient = getPatientByID(patientId);
        if (patient != null) {
            patients.remove(patient);
            deletePatientFromFile(patientId);
            return true;
        }
        return false;
    }

    private void deletePatientFromFile(String patientId) {
        File inputFile = new File("data/patients.csv");
        File tempFile = new File("data/patients_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    writer.write(line);
                    writer.newLine();
                    firstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) continue;

                String[] values = line.split(",");
                if (values.length >= 14) {
                    String currentPatientId = values[0].trim();
                    if (!currentPatientId.equals(patientId)) {
                        writer.write(line);
                        writer.newLine();
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error deleting patient from file: " + e.getMessage());
            return;
        }

        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Could not rename temp file");
            }
        } else {
            System.err.println("Could not delete original file");
        }
    }

    public Patient getPatientByID(String patientId) {
        for (Patient patient : patients) {
            if (patient.getPatientId().equals(patientId)) {
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
        String lowerSearchTerm = searchTerm.toLowerCase();

        for (Patient patient : patients) {
            if (patient.getFirstName().toLowerCase().contains(lowerSearchTerm) ||
                    patient.getLastName().toLowerCase().contains(lowerSearchTerm) ||
                    patient.getPatientId().toLowerCase().contains(lowerSearchTerm) ||
                    patient.getPhoneNumber().contains(searchTerm) ||
                    patient.getEmail().toLowerCase().contains(lowerSearchTerm)) {
                results.add(patient);
            }
        }
        return results;
    }

    private boolean savePatientToFile(Patient patient) {
        String[] data = {
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                dateFormat.format(patient.getDateOfBirth()),
                patient.getNhsNumber(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getPostcode(),
                patient.getEmergencyContactName(),
                patient.getEmergencyContactPhone(),
                dateFormat.format(patient.getRegistrationDate()),
                patient.getGpSurgeryId()
        };
        return dataStore.appendData("data/patients.csv", data);
    }

    private Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateStr);
            return new Date(); // fallback
        }
    }

    public String generatePatientID() {
        return "PAT" + System.currentTimeMillis();
    }
}
