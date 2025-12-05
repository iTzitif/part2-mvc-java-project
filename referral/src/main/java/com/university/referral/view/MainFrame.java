package com.university.referral.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("University Referral System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Clinicians", new ClinicianPanel());
        tabbedPane.addTab("Appointments", new AppointmentPanel());
        tabbedPane.addTab("Prescriptions", new PrescriptionPanel());
        tabbedPane.addTab("Referrals", new ReferralPanel());
        tabbedPane.addTab("Staff", new StaffPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
