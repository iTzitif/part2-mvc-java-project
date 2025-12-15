package com.university.referral.view;

import com.university.referral.model.MedicalFacility;

import javax.swing.*;
import java.awt.*;

public class MainMenuUI extends JFrame {
    private String userRole;
    private JPanel buttonPanel;
    private String loggedInUserID;

    public MainMenuUI(String role, String userID) {
        this.userRole = role;
        this.loggedInUserID = userID;
        setTitle("Healthcare Management System - Main Menu");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel headerLabel = new JLabel("Healthcare Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Logged in as: " + userRole));
        add(infoPanel, BorderLayout.SOUTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        createMenuButtons();

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void createMenuButtons() {
        // -------------------------------------------------------------
// PATIENT MENU — Patients can manage only their profile,
// search clinicians, check availability, book appointments,
// and view their prescriptions. No delete permissions.
// -------------------------------------------------------------
        if (userRole.equals("Patient")) {

            addMenuButton("Manage Profile", e -> manageProfile());             // Update personal info
            addMenuButton("Search / View Clinician", e -> searchClinician());  // View clinicians (read-only)
            addMenuButton("Check Availability", e -> checkAvailability());    // Check available appointment slots
            addMenuButton("Appointment", e -> openAppointmentBooking());      // Book/manage appointments
            addMenuButton("View Prescription", e -> viewPrescription());      // View prescriptions (read-only)
        }

// -------------------------------------------------------------
// SPECIALIST MENU — Specialists can view schedule, referrals,
// create referrals, and update referral notes. No delete rights.
// -------------------------------------------------------------
        if (userRole.equals("Specialist")) {
            addMenuButton("View Appointment Schedule", e -> openAppointmentBooking());
            addMenuButton("View Referrals", e -> viewReferrals());
            addMenuButton("Create Referrals", e -> createReferral());
            addMenuButton("Receive Referral", e -> receiveReferral());
        }
//

// -------------------------------------------------------------
// GENERAL PRACTITIONER MENU — Full clinical functionality:
// Prescriptions, referrals, patient list, medical records, notifications.
// CRUD access for prescriptions, read-only for facilities.
// -------------------------------------------------------------
        if (userRole.equals("General Practitioner")) {

            addMenuButton("View Appointment Schedule", e -> openAppointmentView());
            addMenuButton("View Patient List", e -> viewPatientList());
            addMenuButton("Create Prescription", e -> openPrescriptionUI());
            addMenuButton("View Prescription", e -> viewPrescription());
            addMenuButton("View Facility List", e -> viewFacilityList());
            addMenuButton("Create Referral", e -> createReferral());
            addMenuButton("Update Clinical Notes", e -> viewReferrals());

//            addMenuButton("Request Consent", e -> requestConsent());
//            addMenuButton("Send Referral", e -> sendReferral());

//            addMenuButton("Access Medical Record", e -> viewMedicalRecords());
//            addMenuButton("Receive Notifications", e -> receiveNotifications());
        }


// -------------------------------------------------------------
// RECEPTIONIST MENU — Administrative role. Can manage patients,
// appointments, staff, view facilities, and handle referred patients.
// Full CRUD for patients, appointments, and staff.
// -------------------------------------------------------------
        if (userRole.equals("Receptionist")) {

            // Patient management
            addMenuButton("Register Patient", e -> openPatientRegistration());  // Add
            addMenuButton("Edit Patient Details", e -> editPatient());          // Update
            addMenuButton("Delete Patient", e -> deletePatient());              // Delete
            addMenuButton("Access Patient Basic Details", e -> viewPatientBasicDetails()); // View

            // Appointment management
            addMenuButton("Book Appointment", e -> openAppointmentBooking());  // Add
            addMenuButton("Edit Appointment", e -> editAppointment());          // Update
            addMenuButton("Cancel Appointment", e -> cancelAppointment());      // Delete/Cancel

            // Staff management
            addMenuButton("Register Staff", e -> openStaffRegistration());      // Add staff
            addMenuButton("Edit Staff Details", e -> editStaff());              // Update
            addMenuButton("Delete Staff", e -> deleteStaff());                  // Delete
            addMenuButton("View Staff List", e -> viewStaffList());             // View

            // Facility viewing (read-only)
            addMenuButton("View Facility List", e -> viewFacilityList());

            // Referrals
            addMenuButton("View Referred Patients", e -> viewReferredPatients());
        }


// -------------------------------------------------------------
// NURSE MENU — Can record vitals and access medical records.
// -------------------------------------------------------------
        if (userRole.equals("Nurse")) {
            addMenuButton("Record Vitals", e -> recordVitals());               // Add vitals
            addMenuButton("Access Medical Record", e -> viewMedicalRecords()); // View only
        }


// -------------------------------------------------------------
// PRACTICE MANAGER MENU — Full CRUD access to staff and facilities.
// This menu assumes Practice Manager is separate from GP/Receptionist.
// -------------------------------------------------------------
        if (userRole.equals("Practice Manager")) {

            // Staff management
            addMenuButton("Register Staff", e -> openStaffRegistration());     // Add staff
            addMenuButton("Edit Staff Details", e -> editStaff());             // Update
            addMenuButton("Delete Staff", e -> deleteStaff());                 // Delete
            addMenuButton("View Staff List", e -> viewStaffList());            // View

            // Facility management
            addMenuButton("Register Facility", e -> openFacilityRegistration());// Add
            addMenuButton("Edit Facility Details", e -> editFacility());        // Update
            addMenuButton("Delete Facility", e -> deleteFacility());            // Delete
            addMenuButton("View Facility List", e -> viewFacilityList());       // View
        }


// -------------------------------------------------------------
// UNIVERSAL OPTIONS — Available for all roles.
// Load data, print documents, logout.
// -------------------------------------------------------------
        addMenuButton("Load Data Files", e -> loadDataFiles());    // Load all CSV files
        addMenuButton("Print Document", e -> printDocument());     // Print prescription/referral
        addMenuButton("Logout", e -> logout());                    // Exit system

    }

    private void cancelAppointment() {

    }

    private void openFacilityRegistration() {
    }

    private void viewStaffList() {
    }

    private void deleteStaff() {
    }

    private void editStaff() {
    }

    private void editFacility() {
    }

    private void deleteFacility() {
    }

    private void editAppointment() {
    }

    private void deletePatient() {
    }

    private void editPatient() {
    }

    private void openStaffRegistration() {
        new StaffMemberUI().setVisible(true);
    }

    private void viewFacilityList() {
        new MedicalFacilityUI().setVisible(true);
    }

    private void deletePrescription() {
    }

    private void editPrescription() {
    }

    private void viewPatientList() { new PatientManagementUI( userRole).setVisible(true);}


    private void addMenuButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.addActionListener(action);
        buttonPanel.add(button);
    }

    private void openPatientManagement() {
        new PatientManagementUI(userRole).setVisible(true);
    }



    private void openPrescriptionUI() {
        new PrescriptionUI().setVisible(true);
    }

    private void viewPrescription() {
        new PrescriptionUI(loggedInUserID,userRole).setVisible(true);
    }


    private void openReferralUI() {
        new ReferralUI().setVisible(true);
    }

    private void openPatientRegistration() {
        new PatientManagementUI(userRole).setVisible(true);
    }
    private void openAppointmentView() {
        new AppointmentUI(loggedInUserID,userRole).setVisible(true);
    }
    private void openAppointmentBooking() {
        new AppointmentUI(loggedInUserID,userRole).setVisible(true);
    }

    private void manageProfile() {
        new PatientManagementUI(userRole,loggedInUserID).setVisible(true);
    }


    private void searchClinician() {
        new ClinicianManagementUI(userRole).setVisible(true);
    }





    private void viewMedicalRecords() {
        new PatientManagementUI(userRole,loggedInUserID).setVisible(true);
    }
    private void viewReferrals() {
        new ReferralUI(userRole,loggedInUserID,"viewReferrals").setVisible(true);
    }
    private void receiveReferral() {  new ReferralUI(userRole,loggedInUserID,"receiveReferral").setVisible(true);}
    private void createReferral() {  new ReferralUI(userRole,loggedInUserID,"createReferral").setVisible(true);}

    private void recordVitals() {
        JOptionPane.showMessageDialog(this, "Record Vitals feature");
    }

    private void loadDataFiles() {
        int result = JOptionPane.showConfirmDialog(this,
                "Load data from CSV files?",
                "Load Data",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Data files initialized successfully!");
        }
    }

    private void checkAvailability() {
        new CheckAvailabilityUI(userRole).setVisible(true);
    }

    private void printDocument() { JOptionPane.showMessageDialog(this, "Print Feature"); }
    private void receiveNotifications() { JOptionPane.showMessageDialog(this, "Notifications"); }
    private void requestConsent() { JOptionPane.showMessageDialog(this, "Request Consent"); }
    private void sendReferral() { JOptionPane.showMessageDialog(this, "Send Referral"); }
    private void updateClinicalNotes() { JOptionPane.showMessageDialog(this, "Update Clinical Notes"); }
    private void manageWaitingList() { JOptionPane.showMessageDialog(this, "Waiting List"); }
    private void viewPatientBasicDetails() {
        new PatientManagementUI(userRole).setVisible(true);
    }
    private void viewReferredPatients() { JOptionPane.showMessageDialog(this, "Referred Patients"); }
    private void manageClinicSchedule() { JOptionPane.showMessageDialog(this, "Manage Clinic Schedule"); }
    private void generateReports() { JOptionPane.showMessageDialog(this, "Generate Reports"); }
    private void sendNotifications() { JOptionPane.showMessageDialog(this, "Send Notifications"); }

    private void logout() {
        this.dispose();
        new LoginUI().setVisible(true);
    }
}
