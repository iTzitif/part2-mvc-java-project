package com.university.referral.service;

import com.university.referral.model.Clinician;
import com.university.referral.util.CSVDataStore;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClinicianService {

    private CSVDataStore dataStore;
    private List<Clinician> clinicians;
    private SimpleDateFormat dateFormat;


    public ClinicianService() {
        dataStore = CSVDataStore.getInstance();
        clinicians = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        loadCliniciansFromFile();
    }

    private void loadCliniciansFromFile() {
        File file = new File("data/clinicians.csv");
        if (!file.exists()) {
            System.out.println("Clinicians file not found. Creating new.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }

                if (line.trim().isEmpty()) continue;

                String[] v = line.split(",");

                if (v.length >= 12) {
                    try {
                        String id = v[0].trim();
                        String first = v[1].trim();
                        String last = v[2].trim();
                        String title = v[3].trim();
                        String speciality = v[4].trim();
                        String gmc = v[5].trim();
                        String phone = v[6].trim();
                        String email = v[7].trim();
                        String workplaceID = v[8].trim();
                        String workplaceType = v[9].trim();
                        String status = v[10].trim();
                        Date startDate = parseDate(v[11].trim());

                        Clinician c = new Clinician(
                                id, first, last, title, speciality, gmc,
                                phone, email, workplaceID, workplaceType,
                                status, startDate
                        );

                        clinicians.add(c);

                    } catch (Exception e) {
                        System.err.println("Error parsing clinician line: " + line);
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Loaded " + clinicians.size() + " clinicians.");

        } catch (Exception e) {
            System.err.println("Error loading clinicians file: " + e.getMessage());
        }
    }

    public boolean createClinician(Clinician clinician) {
        clinicians.add(clinician);
        return saveClinicianToFile(clinician);
    }

    private boolean saveClinicianToFile(Clinician c) {
        String[] data = {
                c.getClinicianID(),
                c.getFirstName(),
                c.getLastName(),
                c.getTitle(),
                c.getSpeciality(),
                c.getGmcNumber(),
                c.getPhoneNumber(),
                c.getEmail(),
                c.getWorkplaceID(),
                c.getWorkplaceType(),
                c.getEmploymentStatus(),
                dateFormat.format(c.getStartDate())
        };

        return dataStore.appendData("data/clinicians.csv", data);
    }

    public boolean updateClinician(Clinician updated) {
        for (int i = 0; i < clinicians.size(); i++) {
            if (clinicians.get(i).getClinicianID().equals(updated.getClinicianID())) {
                clinicians.set(i, updated);
                updateClinicianInFile(updated);
                return true;
            }
        }
        return false;
    }

    private void updateClinicianInFile(Clinician updated) {
        File input = new File("data/clinicians.csv");
        File temp = new File("data/clinicians_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(input));
             BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {

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

                String[] v = line.split(",");

                if (v.length >= 12) {
                    String id = v[0].trim();

                    if (id.equals(updated.getClinicianID())) {
                        // Write updated data
                        String[] updatedData = {
                                updated.getClinicianID(),
                                updated.getFirstName(),
                                updated.getLastName(),
                                updated.getTitle(),
                                updated.getSpeciality(),
                                updated.getGmcNumber(),
                                updated.getPhoneNumber(),
                                updated.getEmail(),
                                updated.getWorkplaceID(),
                                updated.getWorkplaceType(),
                                updated.getEmploymentStatus(),
                                dateFormat.format(updated.getStartDate())
                        };
                        writer.write(String.join(",", updatedData));
                        writer.newLine();
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        input.delete();
        temp.renameTo(input);
    }

    public boolean deleteClinician(String clinicianID) {
        Clinician c = getClinicianByID(clinicianID);

        if (c != null) {
            clinicians.remove(c);
            deleteClinicianFromFile(clinicianID);
            return true;
        }
        return false;
    }

    private void deleteClinicianFromFile(String idToDelete) {
        File input = new File("data/clinicians.csv");
        File temp = new File("data/clinicians_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(input));
             BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {

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

                String[] v = line.split(",");
                if (!v[0].trim().equals(idToDelete)) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        input.delete();
        temp.renameTo(input);
    }

    public List<Clinician> searchClinicians(String term) {
        List<Clinician> results = new ArrayList<>();
        String t = term.toLowerCase();

        for (Clinician c : clinicians) {
            if (c.getFirstName().toLowerCase().contains(t) ||
                    c.getLastName().toLowerCase().contains(t) ||
                    c.getClinicianID().toLowerCase().contains(t) ||
                    c.getSpeciality().toLowerCase().contains(t) ||
                    c.getWorkplaceID().toLowerCase().contains(t)) {
                results.add(c);
            }
        }
        return results;
    }

    public Clinician getClinicianByID(String id) {
        for (Clinician c : clinicians) {
            if (c.getClinicianID().equals(id)) return c;
        }
        return null;
    }

    public List<Clinician> getAllClinicians() {
        return new ArrayList<>(clinicians);
    }

    public String generateClinicianID() {
        return "CLN" + System.currentTimeMillis();
    }

    private Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }
}
