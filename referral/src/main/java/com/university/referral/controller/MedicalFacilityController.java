package com.university.referral.controller;

import com.university.referral.model.MedicalFacility;
import com.university.referral.service.MedicalFacilityService;

import java.util.List;

public class MedicalFacilityController {
    private MedicalFacilityService facilityService;

    public MedicalFacilityController() {
        facilityService = new MedicalFacilityService();
    }

    // Get all facilities
    public List<MedicalFacility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    // Find a facility by ID
    public MedicalFacility findFacility(String facilityId) {
        return facilityService.getFacilityById(facilityId);
    }

    // Add new facility
    public boolean addFacility(MedicalFacility facility) {
        return facilityService.addFacility(facility);
    }

    // Update facility
    public boolean updateFacility(MedicalFacility facility) {
        return facilityService.updateFacility(facility);
    }

    // Delete facility
    public boolean deleteFacility(String facilityId) {
        return facilityService.deleteFacility(facilityId);
    }

    // Generate unique Facility ID
    public String generateFacilityID() {
        return facilityService.generateFacilityID();
    }

    // Search facilities by keyword
    public List<MedicalFacility> searchFacilities(String keyword) {
        return facilityService.searchFacilities(keyword);
    }
}



