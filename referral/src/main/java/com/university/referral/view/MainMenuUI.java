package com.university.referral.view;

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
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Main container with white background
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Color.WHITE);

        // HEADER PANEL - Modern healthcare header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Left side: Welcome text
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBackground(new Color(0, 123, 255));
        JLabel welcomeLabel = new JLabel("Welcome, " + userRole);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel);
        headerPanel.add(welcomePanel, BorderLayout.WEST);

        // Right side: User ID and logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(0, 123, 255));
        JLabel userLabel = new JLabel("ID: " + loggedInUserID);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(new Color(220, 230, 255));

        JButton logoutBtn = new JButton("Logout");
        styleLogoutButton(logoutBtn);
        logoutBtn.addActionListener(e -> logout());

        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(logoutBtn);
        headerPanel.add(userPanel, BorderLayout.EAST);

        // TITLE PANEL
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        JLabel titleLabel = new JLabel("Healthcare Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        titlePanel.add(titleLabel);

        // MAIN CONTENT PANEL - Cards layout
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));

        // Create sections based on user role
        createMenuSections(contentPanel);

        // Add all panels to main container
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(titlePanel, BorderLayout.CENTER);
        mainContainer.add(contentPanel, BorderLayout.SOUTH);

        // Add scroll pane for many buttons
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Replace content panel with scrollable version
        mainContainer.remove(contentPanel);
        mainContainer.add(scrollPane, BorderLayout.CENTER);

        add(mainContainer);
    }

    private void createMenuSections(JPanel contentPanel) {
        // Create role-specific sections
        String sectionTitle = getRoleSectionTitle();
        contentPanel.add(createSectionHeader(sectionTitle));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create button grid
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

        createMenuButtons(gridPanel);

        contentPanel.add(gridPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Add universal options in a separate section
        contentPanel.add(createSectionHeader("System Tools"));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        toolsPanel.setBackground(Color.WHITE);

        JButton loadDataBtn = createToolButton("📁 Load Data Files", e -> loadDataFiles());
        JButton printBtn = createToolButton("🖨️ Print Document", e -> printDocument());

        toolsPanel.add(loadDataBtn);
        toolsPanel.add(printBtn);

        contentPanel.add(toolsPanel);
    }

    private String getRoleSectionTitle() {
        switch (userRole) {
            case "Patient": return "Patient Dashboard";
            case "Specialist": return "Specialist Panel";
            case "General Practitioner": return "GP Clinical Panel";
            case "Receptionist": return "Receptionist Administration";
            case "Nurse": return "Nurse Station";
            case "Practice Manager": return "Practice Management";
            default: return "Main Menu";
        }
    }

    private void createMenuButtons(JPanel gridPanel) {
        // PATIENT MENU
        if (userRole.equals("Patient")) {
            addMenuCard(gridPanel, "👤 Manage Profile", "Update personal information", e -> manageProfile());
            addMenuCard(gridPanel, "🔍 Search Clinician", "Find and view clinicians", e -> searchClinician());
            addMenuCard(gridPanel, "📅 Check Availability", "Check appointment availability", e -> checkAvailability());
            addMenuCard(gridPanel, "🏥 Book Appointment", "Schedule appointments", e ->openAppointment());
            addMenuCard(gridPanel, "🏥 View Appointment", "View appointments", e -> openAppointmentBooking());
            addMenuCard(gridPanel, "💊 View Prescription", "View prescriptions", e -> viewPrescriptionPatient());
        }

        // SPECIALIST MENU
        else if (userRole.equals("Specialist")) {
            addMenuCard(gridPanel, "📅 View Schedule", "Appointment schedule", e -> openAppointmentBooking());
            addMenuCard(gridPanel, "📋 View Referrals", "Review patient referrals", e -> viewReferrals());
            addMenuCard(gridPanel, "✏️ Create Referrals", "Create new referrals", e -> createReferral());
            addMenuCard(gridPanel, "📥 Receive Referral", "Accept incoming referrals", e -> receiveReferral());
        }

        // GENERAL PRACTITIONER MENU
        else if (userRole.equals("General Practitioner")) {
            addMenuCard(gridPanel, "📅 View Schedule", "Appointment schedule", e -> openAppointmentView());
            addMenuCard(gridPanel, "👥 Patient List", "View patient directory", e -> viewPatientList());
            addMenuCard(gridPanel, "💊 Create Prescription", "Issue prescriptions", e -> openPrescriptionUI());
            addMenuCard(gridPanel, "📋 View Prescription", "Review prescriptions", e -> viewPrescription());
            addMenuCard(gridPanel, "🏥 Facility List", "View medical facilities", e -> viewFacilityList());
            addMenuCard(gridPanel, "📝 Create Referral", "Refer to specialists", e -> createReferral());
            addMenuCard(gridPanel, "📋 Update Notes", "Update clinical notes", e -> viewReferrals());
        }

        // RECEPTIONIST MENU
        else if (userRole.equals("Receptionist")) {
            addMenuCard(gridPanel, "👤 Register Patient", "New patient registration", e -> openPatientRegistration());
            addMenuCard(gridPanel, "👁️ View Patients", "Access patient details", e -> viewPatientBasicDetails());
            addMenuCard(gridPanel, "🏥 Book Appointment", "Schedule appointments", e ->openAppointment());
            addMenuCard(gridPanel, "📅 View Appointment", "Schedule appointments", e -> openAppointmentBooking());
            addMenuCard(gridPanel, "👥 View Staff", "Staff directory", e -> openStaffRegistration());
            addMenuCard(gridPanel, "✏️ Create Staff", "Create new Staff", e -> createStaff());
            addMenuCard(gridPanel, "🏥 Facilities", "View facility list", e -> viewFacilityList());
            addMenuCard(gridPanel, "✏️ Create Facility", "Create new Facility", e -> createFacility());
        }

        // NURSE MENU
        else if (userRole.equals("Nurse")) {
            addMenuCard(gridPanel, "❤️ Record Vitals", "Record patient vitals", e -> recordVitals());
            addMenuCard(gridPanel, "📋 Medical Records", "Access patient records", e -> viewMedicalRecords());
        }
    }

    private void addMenuCard(JPanel panel, String title, String description, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(200, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Title with icon
        String[] parts = title.split(" ", 2);
        String icon = parts[0];
        String text = parts.length > 1 ? parts[1] : "";

        JLabel titleLabel = new JLabel("<html><b>" + text + "</b></html>");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setIcon(new ImageIcon(getIconText(icon)));

        // Description
        JLabel descLabel = new JLabel("<html><font color='#6c757d' size='2'>" + description + "</font></html>");
        descLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
                        BorderFactory.createEmptyBorder(19, 19, 19, 19)
                ));
                card.setBackground(new Color(248, 249, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                card.setBackground(Color.WHITE);
            }
        });

        // Add click listener
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.actionPerformed(new java.awt.event.ActionEvent(card, 0, ""));
            }
        });

        panel.add(card);
    }

    private JPanel createSectionHeader(String title) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(33, 37, 41));

        headerPanel.add(headerLabel);
        return headerPanel;
    }

    private JButton createToolButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(248, 249, 250));
        button.setForeground(new Color(73, 80, 87));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }

    private void styleLogoutButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(new Color(255, 255, 255, 150));
        button.setForeground(new Color(0, 123, 255));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
    }

    private String getIconText(String icon) {
        // For simplicity, using emoji icons. In production, use actual icons.
        return icon;
    }

    // All existing methods remain the same from here...
    private void cancelAppointment() {
        // Implementation
    }

    private void openFacilityRegistration() {
        // Implementation
    }

    private void viewStaffList() {
        // Implementation
    }

    private void deleteStaff() {
        // Implementation
    }

    private void editStaff() {
        // Implementation
    }

    private void editFacility() {
        // Implementation
    }

    private void deleteFacility() {
        // Implementation
    }

    private void editAppointment() {
        // Implementation
    }

    private void deletePatient() {
        // Implementation
    }

    private void editPatient() {
        // Implementation
    }

    private void openStaffRegistration() {
        new StaffMemberUI().setVisible(true);
    }
    private void createStaff() {
        new CreateStaffUI().setVisible(true);
    }
    private void createFacility() {
        new CreateMedicalFacilityUI().setVisible(true);
    }

    private void viewFacilityList() {
        new MedicalFacilityUI().setVisible(true);
    }

    private void deletePrescription() {
        // Implementation
    }

    private void editPrescription() {
        // Implementation
    }

    private void viewPatientList() {
        new PatientManagementUI(userRole).setVisible(true);
    }

    private void openPatientManagement() {
        new PatientManagementUI(userRole).setVisible(true);
    }

    private void openPrescriptionUI() {
        new PrescriptionUI().setVisible(true);
    }

    private void viewPrescription() {
        new PrescriptionUI(loggedInUserID, userRole).setVisible(true);
    }
    private void viewPrescriptionPatient() {
        new PrescriptionUI(loggedInUserID).setVisible(true);
    }


    private void openReferralUI() {
        new ReferralUI().setVisible(true);
    }

    private void openPatientRegistration() {
        new RegisterPatientUI().setVisible(true);
    }

    private void openAppointmentView() {
        new AppointmentUI(loggedInUserID, userRole).setVisible(true);
    }

    private void openAppointmentBooking() {
        new AppointmentUI(loggedInUserID, userRole).setVisible(true);
    }

    private void openAppointment() {
        new AppointmentAddUI().setVisible(true);
    }

    private void manageProfile() {
        new PatientProfileUI(loggedInUserID).setVisible(true);
    }

    private void searchClinician() {
        new ClinicianManagementUI(userRole).setVisible(true);
    }

    private void viewMedicalRecords() {
        new PatientManagementUI(userRole, loggedInUserID).setVisible(true);
    }

    private void viewReferrals() {
        new ReferralUI(userRole, loggedInUserID, "viewReferrals").setVisible(true);
    }

    private void receiveReferral() {
        new ReferralUI(userRole, loggedInUserID, "receiveReferral").setVisible(true);
    }

    private void createReferral() {
        new ReferralUI(userRole, loggedInUserID, "createReferral").setVisible(true);
    }

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

    private void printDocument() {
        JOptionPane.showMessageDialog(this, "Print Feature");
    }

    private void receiveNotifications() {
        JOptionPane.showMessageDialog(this, "Notifications");
    }

    private void requestConsent() {
        JOptionPane.showMessageDialog(this, "Request Consent");
    }

    private void sendReferral() {
        JOptionPane.showMessageDialog(this, "Send Referral");
    }

    private void updateClinicalNotes() {
        JOptionPane.showMessageDialog(this, "Update Clinical Notes");
    }

    private void manageWaitingList() {
        JOptionPane.showMessageDialog(this, "Waiting List");
    }

    private void viewPatientBasicDetails() {
        new PatientManagementUI(userRole).setVisible(true);
    }

    private void viewReferredPatients() {
        JOptionPane.showMessageDialog(this, "Referred Patients");
    }

    private void manageClinicSchedule() {
        JOptionPane.showMessageDialog(this, "Manage Clinic Schedule");
    }

    private void generateReports() {
        JOptionPane.showMessageDialog(this, "Generate Reports");
    }

    private void sendNotifications() {
        JOptionPane.showMessageDialog(this, "Send Notifications");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginUI().setVisible(true);
        }
    }
}