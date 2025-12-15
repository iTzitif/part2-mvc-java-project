package com.university.referral.controller;

import com.university.referral.model.Appointment;
import com.university.referral.model.Clinician;
import com.university.referral.service.AppointmentService;
import com.university.referral.service.ClinicianService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ClinicianController {

    private ClinicianService clinicianService;
    private AppointmentService appointmentService;

    public ClinicianController() {
        clinicianService = new ClinicianService();
        appointmentService= new AppointmentService();
    }

    public boolean registerNewClinician(
            String firstName,
            String lastName,
            String title,
            String speciality,
            String gmcNumber,
            String phone,
            String email,
            String workplaceID,
            String workplaceType,
            String employmentStatus,
            Date startDate
    ) {
        String clinicianID = clinicianService.generateClinicianID();

        Clinician clinician = new Clinician(
                clinicianID,
                firstName,
                lastName,
                title,
                speciality,
                gmcNumber,
                phone,
                email,
                workplaceID,
                workplaceType,
                employmentStatus,
                startDate
        );

        return clinicianService.createClinician(clinician);
    }

    public boolean updateClinician(
            String clinicianID,
            String firstName,
            String lastName,
            String title,
            String speciality,
            String gmcNumber,
            String phone,
            String email,
            String workplaceID,
            String workplaceType,
            String employmentStatus,
            Date startDate
    ) {
        Clinician clinician = new Clinician(
                clinicianID,
                firstName,
                lastName,
                title,
                speciality,
                gmcNumber,
                phone,
                email,
                workplaceID,
                workplaceType,
                employmentStatus,
                startDate
        );

        return clinicianService.updateClinician(clinician);
    }

    public boolean deleteClinician(String clinicianID) {
        return clinicianService.deleteClinician(clinicianID);
    }

    public Clinician findClinician(String clinicianID) {
        return clinicianService.getClinicianByID(clinicianID);
    }

    public List<Clinician> searchClinicians(String searchTerm) {
        return clinicianService.searchClinicians(searchTerm);
    }

    public List<Clinician> getAllClinicians() {
        return clinicianService.getAllClinicians();
    }

    public int getClinicianCount() {
        return clinicianService.getClinicianCount();
    }

    public List<Appointment> getAvailableClinicians(String clinicianID) {
        List<Appointment> appointments = appointmentService.getAppointmentsByClinician(clinicianID);
        if (appointments.isEmpty()) {
            return Collections.emptyList();
        }
        return appointments;
    }

    public List<Appointment> getClinicianAppointments(String clinicianID) {
        return appointmentService.getAppointmentsByClinician(clinicianID);
    }


}
