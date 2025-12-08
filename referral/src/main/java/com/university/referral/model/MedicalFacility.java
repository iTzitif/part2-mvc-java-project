package com.university.referral.model;

public class MedicalFacility {

    private String facilityId;
    private String name;
    private String address;

    public MedicalFacility(String facilityId, String name, String address) {
        this.facilityId = facilityId;
        this.name = name;
        this.address = address;
    }

    public String getFacilityId() { return facilityId; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    public void setFacilityId(String facilityId) { this.facilityId = facilityId; }
}
