package com.university.referral.controller;

import com.university.referral.model.StaffMember;
import com.university.referral.service.StaffMemberService;

import java.util.List;

public class StaffMemberController {
    private StaffMemberService staffService;

    public StaffMemberController() {
        staffService = new StaffMemberService();
    }

    // Get all staff members
    public List<StaffMember> getAllStaffMembers() {
        return staffService.getAllStaffMembers();
    }

    // Find a staff member by ID
    public StaffMember findStaffMember(String staffId) {
        return staffService.getStaffMemberById(staffId);
    }

    // Add new staff member
    public boolean addStaffMember(StaffMember staffMember) {
        return staffService.addStaffMember(staffMember);
    }

    // Update staff member
    public boolean updateStaffMember(StaffMember staffMember) {
        return staffService.updateStaffMember(staffMember);
    }

    // Delete staff member
    public boolean deleteStaffMember(String staffId) {
        return staffService.deleteStaffMember(staffId);
    }

    // Generate unique Staff ID
    public String generateStaffID() {
        return staffService.generateStaffID();
    }

    // Search staff members by keyword
    public List<StaffMember> searchStaffMembers(String keyword) {
        return staffService.searchStaffMembers(keyword);
    }
}
