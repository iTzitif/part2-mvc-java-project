package com.university.referral.controller;

import com.university.referral.model.MedicalFacility;
import com.university.referral.service.MedicalFacilityService;
import java.util.List;

public class MedicalFacilityController {
    private MedicalFacilityService facilityService;

    public MedicalFacilityController() {
        facilityService = new MedicalFacilityService();
    }

    public List<MedicalFacility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    public MedicalFacility findFacility(String facilityId) {
        return facilityService.getFacilityById(facilityId);
    }

    public boolean addFacility(MedicalFacility facility) {
        return facilityService.addFacility(facility);
    }

    public boolean updateFacility(MedicalFacility facility) {
        return facilityService.updateFacility(facility);
    }

    public boolean deleteFacility(String facilityId) {
        return facilityService.deleteFacility(facilityId);
    }

    public String generateFacilityID() {
        return facilityService.generateFacilityID();
    }

}



