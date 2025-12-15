package com.university.referral.service;

import com.university.referral.model.StaffMember;
import com.university.referral.util.CSVDataStore;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffMemberService {

    private List<StaffMember> staffMembers;
    private CSVDataStore dataStore;

    public StaffMemberService() {
        staffMembers = new ArrayList<>();
        dataStore = CSVDataStore.getInstance();
        loadStaffFromFile();
    }

    // Load CSV data
    private void loadStaffFromFile() {
        File file = new File("data/staff.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                String[] values = splitCSVLine(line);
                if (values.length >= 12) {
                    StaffMember s = new StaffMember(
                            values[0], // staffId
                            values[1], // firstName
                            values[2], // lastName
                            values[3], // role
                            values[4], // department
                            values[5], // facilityId
                            values[6], // phoneNumber
                            values[7], // email
                            values[8], // employmentStatus
                            LocalDate.parse(values[9]), // startDate
                            values[10], // lineManager
                            values[11]  // accessLevel
                    );
                    staffMembers.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<StaffMember> getAllStaffMembers() {
        return new ArrayList<>(staffMembers);
    }

    public StaffMember getStaffMemberById(String staffId) {
        return staffMembers.stream()
                .filter(s -> s.getStaffId().equals(staffId))
                .findFirst()
                .orElse(null);
    }

    public boolean addStaffMember(StaffMember staff) {
        staffMembers.add(staff);
        return saveStaffToFile(staff);
    }

    public boolean updateStaffMember(StaffMember updatedStaff) {
        for (int i = 0; i < staffMembers.size(); i++) {
            if (staffMembers.get(i).getStaffId().equals(updatedStaff.getStaffId())) {
                staffMembers.set(i, updatedStaff);
                return updateStaffInFile(updatedStaff);
            }
        }
        return false;
    }

    public boolean deleteStaffMember(String staffId) {
        boolean removed = staffMembers.removeIf(s -> s.getStaffId().equals(staffId));
        if (removed) {
            return deleteStaffFromFile(staffId);
        }
        return false;
    }

    public List<StaffMember> searchStaffMembers(String keyword) {
        if (keyword == null || keyword.isEmpty()) return new ArrayList<>();
        String lower = keyword.toLowerCase();
        List<StaffMember> results = new ArrayList<>();
        for (StaffMember s : staffMembers) {
            if ((s.getStaffId() != null && s.getStaffId().toLowerCase().contains(lower)) ||
                    (s.getFirstName() != null && s.getFirstName().toLowerCase().contains(lower)) ||
                    (s.getLastName() != null && s.getLastName().toLowerCase().contains(lower)) ||
                    (s.getRole() != null && s.getRole().toLowerCase().contains(lower)) ||
                    (s.getDepartment() != null && s.getDepartment().toLowerCase().contains(lower))) {
                results.add(s);
            }
        }
        return results;
    }

    public String generateStaffID() {
        return "STAFF" + System.currentTimeMillis();
    }

    // Save new staff member to CSV
    private boolean saveStaffToFile(StaffMember s) {
        File file = new File("data/staff.csv");
        boolean writeHeader = !file.exists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (writeHeader) {
                bw.write("StaffID,FirstName,LastName,Role,Department,FacilityID,Phone,Email,EmploymentStatus,StartDate,LineManager,AccessLevel");
                bw.newLine();
            }
            bw.write(toCSVLine(s));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update existing staff in CSV
    private boolean updateStaffInFile(StaffMember updated) {
        File inputFile = new File("data/staff.csv");
        File tempFile = new File("data/staff_temp.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { bw.write(line); bw.newLine(); firstLine = false; continue; }
                String[] values = splitCSVLine(line);
                if (values.length > 0 && values[0].equals(updated.getStaffId())) {
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

    // Delete staff from CSV
    private boolean deleteStaffFromFile(String staffId) {
        File inputFile = new File("data/staff.csv");
        File tempFile = new File("data/staff_temp.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { bw.write(line); bw.newLine(); firstLine = false; continue; }
                String[] values = splitCSVLine(line);
                if (values.length > 0 && values[0].equals(staffId)) continue; // skip
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return inputFile.delete() && tempFile.renameTo(inputFile);
    }

    // Convert staff member to CSV line
    private String toCSVLine(StaffMember s) {
        return String.join(",",
                s.getStaffId(),
                s.getFirstName(),
                s.getLastName(),
                s.getRole(),
                s.getDepartment(),
                s.getFacilityId(),
                s.getPhoneNumber(),
                s.getEmail(),
                s.getEmploymentStatus(),
                s.getStartDate().toString(),
                s.getLineManager(),
                s.getAccessLevel()
        );
    }

    // Split CSV respecting quotes
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
