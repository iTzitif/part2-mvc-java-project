package com.university.referral.view;

import com.university.referral.controller.*;
import com.university.referral.model.ApplicationDataStore;

import javax.swing.*;
import java.awt.*;

public class MainDashboardFrame extends JFrame {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final ApplicationDataStore dataStore = ApplicationDataStore.getInstance();

    public MainDashboardFrame() {
        setTitle("University Referral System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        dataStore.loadApplicationData();

        var patientController = new PatientRecordController(dataStore);
        var appointmentController = new AppointmentController(dataStore);
        var clinicianController = new ClinicianProfileController(dataStore);
        var prescriptionController = new PrescriptionController(dataStore);
        var referralController = new ReferralRequestController(dataStore);

        tabbedPane.addTab("Patient Records", new PatientRecordPanel(this, patientController, dataStore.getPatients()));
        tabbedPane.addTab("Appointments", new AppointmentPanel(this, appointmentController, dataStore.getAppointments()));
        tabbedPane.addTab("Clinicians", new ClinicianPanel(this, clinicianController, dataStore.getClinicians()));
        tabbedPane.addTab("Prescriptions", new PrescriptionPanel(this, prescriptionController, dataStore.getPrescriptions()));
        tabbedPane.addTab("Referrals", new ReferralPanel(this, referralController, dataStore.getReferrals()));
        add(tabbedPane, BorderLayout.CENTER);
    }
}
