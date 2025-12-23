package com.university.referral.service;

import com.university.referral.model.MedicalFacility;
import com.university.referral.util.CSVDataStore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalFacilityService {

    private List<MedicalFacility> facilities;
    private CSVDataStore dataStore;

    public MedicalFacilityService() {
        facilities = new ArrayList<>();
        dataStore = CSVDataStore.getInstance();
        loadFacilitiesFromFile();
    }

    private void loadFacilitiesFromFile() {
        File file = new File("data/facilities.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] values = splitCSVLine(line);
                if (values.length >= 11) {
                    MedicalFacility f = new MedicalFacility();
                    f.setFacilityId(values[0]);
                    f.setFacilityName(values[1]);
                    f.setFacilityType(values[2]);
                    f.setAddress(values[3]);
                    f.setPostcode(values[4]);
                    f.setPhoneNumber(values[5]);
                    f.setEmail(values[6]);
                    f.setOpeningHours(values[7]);
                    f.setManagerName(values[8]);
                    f.setCapacity(Integer.parseInt(values[9]));
                    f.setSpecialitiesOffered(values[10]);
                    facilities.add(f);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MedicalFacility> getAllFacilities() {
        return new ArrayList<>(facilities);
    }

    public MedicalFacility getFacilityById(String facilityId) {
        return facilities.stream()
                .filter(f -> f.getFacilityId().equals(facilityId))
                .findFirst()
                .orElse(null);
    }

    public boolean addFacility(MedicalFacility facility) {
        facilities.add(facility);
        return saveFacilityToFile(facility);
    }

    public boolean updateFacility(MedicalFacility updatedFacility) {
        for (int i = 0; i < facilities.size(); i++) {
            if (facilities.get(i).getFacilityId().equals(updatedFacility.getFacilityId())) {
                facilities.set(i, updatedFacility);
                return updateFacilityInFile(updatedFacility);
            }
        }
        return false;
    }

    public boolean deleteFacility(String facilityId) {
        boolean removed = facilities.removeIf(f -> f.getFacilityId().equals(facilityId));
        if (removed) {
            return deleteFacilityFromFile(facilityId);
        }
        return false;
    }

    public String generateFacilityID() {
        return "FAC" + System.currentTimeMillis();
    }

    private boolean saveFacilityToFile(MedicalFacility f) {
        File file = new File("data/facilities.csv");
        boolean writeHeader = !file.exists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (writeHeader) {
                bw.write("FacilityID,Name,Type,Address,Postcode,Phone,Email,OpeningHours,Manager,Capacity,Specialities");
                bw.newLine();
            }
            bw.write(toCSVLine(f));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateFacilityInFile(MedicalFacility updated) {
        File inputFile = new File("data/facilities.csv");
        File tempFile = new File("data/facilities_temp.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { bw.write(line); bw.newLine(); firstLine = false; continue; }
                String[] values = splitCSVLine(line);
                if (values.length > 0 && values[0].equals(updated.getFacilityId())) {
                    bw.write(toCSVLine(updated));
                    bw.newLine();
                } else {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return inputFile.delete() && tempFile.renameTo(inputFile);
    }

    private boolean deleteFacilityFromFile(String facilityId) {
        File inputFile = new File("data/facilities.csv");
        File tempFile = new File("data/facilities_temp.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { bw.write(line); bw.newLine(); firstLine = false; continue; }
                String[] values = splitCSVLine(line);
                if (values.length > 0 && values[0].equals(facilityId)) continue; // skip
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return inputFile.delete() && tempFile.renameTo(inputFile);
    }

    private String toCSVLine(MedicalFacility f) {
        return String.join(",",
                f.getFacilityId(),
                f.getFacilityName(),
                f.getFacilityType(),
                f.getAddress(),
                f.getPostcode(),
                f.getPhoneNumber(),
                f.getEmail(),
                f.getOpeningHours(),
                f.getManagerName(),
                String.valueOf(f.getCapacity()),
                f.getSpecialitiesOffered()
        );
    }
    private String[] splitCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else sb.append(c);
        }
        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }
}


