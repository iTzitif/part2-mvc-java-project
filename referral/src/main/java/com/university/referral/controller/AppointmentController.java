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

    public boolean bookAppointment(String patientId, String clinicianId, LocalDate date,
                                   LocalTime time, int durationMinutes, String type,
                                   String facilityId, String reason, String notes) {

        String appointmentId = appointmentService.generateAppointmentID();
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

        return appointmentService.createAppointment(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    public List<Appointment> getPatientAppointments(String patientID) {
        return appointmentService.getAppointmentsByPatient(patientID);
    }

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
