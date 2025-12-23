package com.university.referral.service;

import com.university.referral.model.StaffMember;
import com.university.referral.util.CSVDataStore;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffMemberService {

    private static final String STAFF_FILE = "data/staff.csv";

    private static final String[] HEADERS = {
            "StaffID","FirstName","LastName","Role","Department",
            "FacilityID","Phone","Email","EmploymentStatus",
            "StartDate","LineManager","AccessLevel"
    };

    private List<StaffMember> staffMembers;
    private CSVDataStore dataStore;

    public StaffMemberService() {
        staffMembers = new ArrayList<>();
        dataStore = CSVDataStore.getInstance();

        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            dataStore.createFileWithHeaders(STAFF_FILE, HEADERS);
        }

        loadStaffFromFile();
    }

    private void loadStaffFromFile() {
        List<String[]> rows = dataStore.loadData(STAFF_FILE);

        for (String[] v : rows) {
            if (v.length >= 12) {
                staffMembers.add(new StaffMember(
                        v[0],
                        v[1],
                        v[2],
                        v[3],
                        v[4],
                        v[5],
                        v[6],
                        v[7],
                        v[8],
                        LocalDate.parse(v[9]),
                        v[10],
                        v[11]
                ));
            }
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
        return dataStore.appendData(STAFF_FILE, toCSVArray(staff));
    }

    public boolean updateStaffMember(StaffMember updatedStaff) {
        for (int i = 0; i < staffMembers.size(); i++) {
            if (staffMembers.get(i).getStaffId().equals(updatedStaff.getStaffId())) {
                staffMembers.set(i, updatedStaff);
                return rewriteFile();
            }
        }
        return false;
    }

    public boolean deleteStaffMember(String staffId) {
        boolean removed = staffMembers.removeIf(
                s -> s.getStaffId().equals(staffId)
        );
        return removed && rewriteFile();
    }

    public List<StaffMember> searchStaffMembers(String keyword) {
        List<StaffMember> results = new ArrayList<>();
        if (keyword == null || keyword.isEmpty()) return results;

        String lower = keyword.toLowerCase();
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

    private boolean rewriteFile() {
        if (!dataStore.createFileWithHeaders(STAFF_FILE, HEADERS)) {
            return false;
        }

        for (StaffMember s : staffMembers) {
            if (!dataStore.appendData(STAFF_FILE, toCSVArray(s))) {
                return false;
            }
        }
        return true;
    }

    private String[] toCSVArray(StaffMember s) {
        return new String[] {
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
        };
    }
}
