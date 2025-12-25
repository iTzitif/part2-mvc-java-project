package com.university.referral.controller;

import com.university.referral.model.StaffMember;
import com.university.referral.service.StaffMemberService;

import java.util.List;

public class StaffMemberController {
    private StaffMemberService staffService;

    public StaffMemberController() {
        staffService = new StaffMemberService();
    }

    public List<StaffMember> getAllStaffMembers() {
        return staffService.getAllStaffMembers();
    }

    public StaffMember findStaffMember(String staffId) {
        return staffService.getStaffMemberById(staffId);
    }

    public boolean addStaffMember(StaffMember staffMember) {
        return staffService.addStaffMember(staffMember);
    }

    public boolean updateStaffMember(StaffMember staffMember) {
        return staffService.updateStaffMember(staffMember);
    }

    public boolean deleteStaffMember(String staffId) {
        return staffService.deleteStaffMember(staffId);
    }

    public String generateStaffID() {
        return staffService.generateStaffID();
    }

    public List<StaffMember> searchStaffMembers(String keyword) {
        return staffService.searchStaffMembers(keyword);
    }


}
