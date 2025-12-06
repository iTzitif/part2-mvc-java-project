package com.university.referral.model;

public class PatientRecord {

    private String patientId;
    private String name;
    private String dob;
    private String phone;
    private String email;

    public PatientRecord() { }

    public PatientRecord(String patientId, String name, String dob, String phone, String email) {
        this.patientId = patientId;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    public String getPatientId() { return patientId; }
    public String getName() { return name; }
    public String getDob() { return dob; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
