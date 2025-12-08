package com.university.referral.model;

public class AppointmentRecord {

    private String appointmentId;
    private String patientId;
    private String clinicianId;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentType;

    public AppointmentRecord(String appointmentId, String patientId, String clinicianId,
                             String appointmentDate, String appointmentTime, String appointmentType) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentType = appointmentType;
    }

    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getClinicianId() { return clinicianId; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getAppointmentType() { return appointmentType; }

    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setClinicianId(String clinicianId) { this.clinicianId = clinicianId; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
}
