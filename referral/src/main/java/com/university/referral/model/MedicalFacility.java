package com.university.referral.model;

public class MedicalFacility {

    private String facilityId;
    private String facilityName;
    private String facilityType;
    private String address;
    private String postcode;
    private String phoneNumber;
    private String email;
    private String openingHours;
    private String managerName;
    private int capacity;
    private String specialitiesOffered;

    public MedicalFacility() { }

    public MedicalFacility(String facilityId, String facilityName, String facilityType,
                           String address, String postcode, String phoneNumber,
                           String email, String openingHours, String managerName,
                           int capacity, String specialitiesOffered) {

        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.address = address;
        this.postcode = postcode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.openingHours = openingHours;
        this.managerName = managerName;
        this.capacity = capacity;
        this.specialitiesOffered = specialitiesOffered;
    }

    public String getFacilityId() { return facilityId; }
    public String getFacilityName() { return facilityName; }
    public String getFacilityType() { return facilityType; }
    public String getAddress() { return address; }
    public String getPostcode() { return postcode; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getOpeningHours() { return openingHours; }
    public String getManagerName() { return managerName; }
    public int getCapacity() { return capacity; }
    public String getSpecialitiesOffered() { return specialitiesOffered; }

    public void setFacilityId(String facilityId) { this.facilityId = facilityId; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
    public void setAddress(String address) { this.address = address; }
    public void setPostcode(String postcode) { this.postcode = postcode; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setSpecialitiesOffered(String specialitiesOffered) { this.specialitiesOffered = specialitiesOffered; }
}
