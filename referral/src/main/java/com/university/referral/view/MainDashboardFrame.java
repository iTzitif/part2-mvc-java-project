package com.university.referral.view;

import com.university.referral.controller.*;
import com.university.referral.model.ApplicationDataStore;
import javax.swing.*;
import java.awt.*;

public class MainDashboardFrame extends JFrame {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final ApplicationDataStore dataStore;  // Regular instance

    public MainDashboardFrame() {
        setTitle("University Referral System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create regular instance (NOT singleton)
        dataStore = new ApplicationDataStore();
        dataStore.loadApplicationData();

        // Create controllers
        var patientController = new PatientRecordController(dataStore);


        // Add tabs
        tabbedPane.addTab("Patients", new PatientRecordPanel(this, patientController, dataStore.getPatients()));

        add(tabbedPane, BorderLayout.CENTER);
    }
}