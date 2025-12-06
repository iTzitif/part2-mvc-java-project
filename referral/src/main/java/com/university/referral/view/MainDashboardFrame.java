package com.university.referral.view;

import com.university.referral.controller.PatientRecordController;
import com.university.referral.model.ApplicationDataStore;

import javax.swing.*;
import java.awt.*;

public class MainDashboardFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private ApplicationDataStore dataStore;
    private PatientRecordController patientRecordController;

    public MainDashboardFrame() {

        // USE SINGLETON
        dataStore = ApplicationDataStore.getInstance();
        dataStore.loadApplicationData();

        // CREATE CONTROLLER
        patientRecordController = new PatientRecordController(dataStore);

        setTitle("University Referral System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // ADD PATIENT TAB — NOW IT'S CORRECT
        tabbedPane.addTab(
                "Patient Records",
                new PatientRecordPanel(this, patientRecordController, dataStore.getPatients())
        );

        add(tabbedPane, BorderLayout.CENTER);
    }
}
