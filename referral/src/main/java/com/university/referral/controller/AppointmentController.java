package com.university.referral.controller;

import com.university.referral.model.Appointment;
import com.university.referral.service.AppointmentService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentController {
    private AppointmentService appointmentService;

    public AppointmentController() {
        appointmentService = new AppointmentService();
    }

    // Book appointment using the new fields expected by AppointmentUI
    public boolean bookAppointment(String patientID, String clinicianID, LocalDate date,
                                   LocalTime time, int durationMinutes, String type,
                                   String location, String reason, String notes) {
        String appointmentID = appointmentService.generateAppointmentID();
        Appointment appointment = new Appointment();
        appointment.getAppointmentId();
        appointment.getPatientId();
        appointment.getClinicianId();
        appointment.getAppointmentDate();
        appointment.getAppointmentTime();
        appointment.getDurationMinutes();
        appointment.getAppointmentType();
        appointment.getFacilityId();
        appointment.getReasonForVisit();
        appointment.setNotes(notes);
        appointment.setStatus("Scheduled"); // Default status

        return appointmentService.createAppointment(appointment);
    }

    // Return all appointments for UI table
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // Return appointments filtered by patient
    public List<Appointment> getPatientAppointments(String patientID) {
        return appointmentService.getAppointmentsByPatient(patientID);
    }

    // Cancel an appointment
    public boolean cancelAppointment(String appointmentID) {
        return appointmentService.cancelAppointment(appointmentID);
    }
    public boolean updateAppointment(String appointmentId, String patientId, String clinicianId,
                                     LocalDate date, LocalTime time, int durationMinutes,
                                     String type, String facilityId, String reason, String notes) {
        Appointment appointment = new Appointment(
                appointmentId,
                patientId,
                clinicianId,
                facilityId,
                date,
                time,
                durationMinutes,
                type,
                reason,
                notes
        );
        appointment.setStatus("Scheduled"); // Or keep existing status if you prefer
        return appointmentService.rescheduleAppointment(appointment);
    }

}
