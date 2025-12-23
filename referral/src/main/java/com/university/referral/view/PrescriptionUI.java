package com.university.referral.view;

import com.university.referral.controller.AppointmentController;
import com.university.referral.controller.ClinicianController;
import com.university.referral.controller.PatientController;
import com.university.referral.controller.PrescriptionController;
import com.university.referral.model.Patient;
import com.university.referral.util.SingletonReferralManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class PrescriptionUI extends JFrame {

    private PrescriptionController prescriptionController;
    private PatientController patientController;
    private ClinicianController clinicianController;
    private AppointmentController appointmentController;
    private SingletonReferralManager singletonReferralManager;

    private JTextField patientIDField;
    private JTextField clinicianIDField;
    private JTextField appointmentIDField;
    private JTextField medicationNameField;
    private JTextField dosageField;
    private JTextField frequencyField;
    private JTextField durationDaysField;
    private JTextField quantityField;
    private JTextArea instructionsArea;
    private JTextField pharmacyNameField;
    private JTextField statusField;
    private JTextField prescriptionDateField;
    private JTextField issueDateField;
    private JTextField collectionDateField;
    private String loggedInUserID;
    private String userRole;

    private JButton createButton, clearButton;

    private boolean isPatientMode = false;

    public PrescriptionUI() {
        this.isPatientMode = false;
        initCommon();
    }

    public PrescriptionUI(String loggedInUserID) {
        this.loggedInUserID = loggedInUserID;
        this.isPatientMode = true;
        initCommon();
    }

    public PrescriptionUI(String loggedInUserID, String userRole) {
        this.loggedInUserID = loggedInUserID;
        this.userRole = userRole;
        this.isPatientMode = false;
        initCommon();
    }

    private void initCommon() {
        prescriptionController = new PrescriptionController();
        patientController = new PatientController();
        clinicianController = new ClinicianController();
        appointmentController = new AppointmentController();

        setTitle("Prescription");
        setSize(900, 650);
        setLocationRelativeTo(null);

        if (isPatientMode) {
            initPatientViewOnly();
        } else if ("General Practitioner".equalsIgnoreCase(userRole)) {
            initGPView();
        } else {
            initComponents();
        }
    }

    private void initGPView() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("All Prescriptions");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        var prescriptions = prescriptionController.getAllPrescriptions();

        if (prescriptions.isEmpty()) {
            add(new JLabel("No prescriptions found.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        String[] columns = {
                "ID", "Patient", "Clinician", "Medication", "Dosage", "Frequency",
                "Duration", "Qty", "Pharmacy", "Status", "Prescription Date", "Issue Date", "Collection Date",
                "Edit", "Delete"
        };

        Object[][] data = new Object[prescriptions.size()][columns.length];

        for (int i = 0; i < prescriptions.size(); i++) {
            var p = prescriptions.get(i);
            data[i][0] = p.getPrescriptionId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getClinicianId();
            data[i][3] = p.getMedicationName();
            data[i][4] = p.getDosage();
            data[i][5] = p.getFrequency();
            data[i][6] = p.getDurationDays();
            data[i][7] = p.getQuantity();
            data[i][8] = p.getPharmacyName();
            data[i][9] = p.getStatus();
            data[i][10] = p.getPrescriptionDate();
            data[i][11] = p.getIssueDate();
            data[i][12] = p.getCollectionDate();
            data[i][13] = "Edit";
            data[i][14] = "Delete";
        }

        JTable table = new JTable(data, columns);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 13) {
                    String prescriptionId = (String) table.getValueAt(row, 0);
                    editPrescription(prescriptionId);
                } else if (col == 14) {
                    String prescriptionId = (String) table.getValueAt(row, 0);
                    deletePrescription(prescriptionId);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    private void initPatientViewOnly() {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Prescriptions");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        add(title, BorderLayout.NORTH);

        var prescriptions = prescriptionController.getPatientPrescriptions(loggedInUserID);

        if (prescriptions.isEmpty()) {
            add(new JLabel("No prescriptions found.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        String[] columns = {
                "ID", "Clinician", "Appointment", "Date",
                "Medication", "Dosage", "Freq", "Days", "Qty",
                "Pharmacy", "Status", "Issue", "Collected"
        };

        String[][] data = new String[prescriptions.size()][columns.length];

        for (int i = 0; i < prescriptions.size(); i++) {
            var p = prescriptions.get(i);
            data[i][0] = p.getPrescriptionId();
            data[i][1] = p.getClinicianId();
            data[i][2] = p.getAppointmentId();
            data[i][3] = String.valueOf(p.getPrescriptionDate());
            data[i][4] = p.getMedicationName();
            data[i][5] = p.getDosage();
            data[i][6] = p.getFrequency();
            data[i][7] = String.valueOf(p.getDurationDays());
            data[i][8] = p.getQuantity();
            data[i][9] = p.getPharmacyName();
            data[i][10] = p.getStatus();
            data[i][11] = String.valueOf(p.getIssueDate());
            data[i][12] = String.valueOf(p.getCollectionDate());
        }

        JTable table = new JTable(data, columns);
        table.setEnabled(false);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    private void initComponents() {
        if (isPatientMode) {
            initPatientViewOnly();
            return;
        }

        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Create Prescription");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> patientCombo = new JComboBox<>();
        patientController.getAllPatients().forEach(p -> patientCombo.addItem(p.getPatientId() + " - " + p.getFirstName()));
        formPanel.add(patientCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Clinician:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> clinicianCombo = new JComboBox<>();
        clinicianController.getAllClinicians().forEach(p->clinicianCombo.addItem(p.getClinicianID() + " - " + p.getFirstName()));
        formPanel.add(clinicianCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Appointment:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> appointmentCombo = new JComboBox<>();
        appointmentController.getAllAppointments().forEach(p->appointmentCombo.addItem(p.getAppointmentId() + " - "  ));
        formPanel.add(appointmentCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Medication:"), gbc);
        gbc.gridx = 1;
        medicationNameField = new JTextField(20);
        medicationNameField.setToolTipText("Enter the medication name");
        formPanel.add(medicationNameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Dosage:"), gbc);
        gbc.gridx = 1;
        dosageField = new JTextField(20);
        dosageField.setToolTipText("e.g., 500mg");
        formPanel.add(dosageField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Frequency:"), gbc);
        gbc.gridx = 1;
        frequencyField = new JTextField(20);
        frequencyField.setToolTipText("e.g., 3 times a day");
        formPanel.add(frequencyField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Duration (Days):"), gbc);
        gbc.gridx = 1;
        durationDaysField = new JTextField(20);
        durationDaysField.setToolTipText("Number of days to take medication");
        formPanel.add(durationDaysField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(20);
        formPanel.add(quantityField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Instructions:"), gbc);
        gbc.gridx = 1;
        instructionsArea = new JTextArea(5, 20);
        instructionsArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(instructionsArea);
        formPanel.add(scroll, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Prescription Date:"), gbc);
        gbc.gridx = 1;
        JSpinner prescriptionDateSpinner = new JSpinner(new SpinnerDateModel());
        prescriptionDateSpinner.setEditor(new JSpinner.DateEditor(prescriptionDateSpinner, "yyyy-MM-dd"));
        formPanel.add(prescriptionDateSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Issue Date:"), gbc);
        gbc.gridx = 1;
        JSpinner issueDateSpinner = new JSpinner(new SpinnerDateModel());
        issueDateSpinner.setEditor(new JSpinner.DateEditor(issueDateSpinner, "yyyy-MM-dd"));
        formPanel.add(issueDateSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Collection Date:"), gbc);
        gbc.gridx = 1;
        JSpinner collectionDateSpinner = new JSpinner(new SpinnerDateModel());
        collectionDateSpinner.setEditor(new JSpinner.DateEditor(collectionDateSpinner, "yyyy-MM-dd"));
        formPanel.add(collectionDateSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Pharmacy:"), gbc);
        gbc.gridx = 1;
        pharmacyNameField = new JTextField(20);
        formPanel.add(pharmacyNameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "Issued", "Collected"});
        formPanel.add(statusCombo, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton createBtn = new JButton("Create Prescription");
        createBtn.addActionListener(e -> createPrescription());
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearFields());
        buttonPanel.add(createBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void viewPatientPrescriptions() {

        String patientId = JOptionPane.showInputDialog(this, "Enter Patient ID:");

        if (patientId == null || patientId.trim().isEmpty()) {
            return;
        }

        var prescriptions = prescriptionController.getPatientPrescriptions(patientId.trim());

        if (prescriptions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No prescriptions found for patient: " + patientId,
                    "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columns = {
                "ID", "Clinician", "Appointment", "Date",
                "Medication", "Dosage", "Freq", "Days", "Qty",
                "Pharmacy", "Status", "Issue", "Collected"
        };

        String[][] data = new String[prescriptions.size()][columns.length];

        for (int i = 0; i < prescriptions.size(); i++) {
            var p = prescriptions.get(i);
            data[i][0] = p.getPrescriptionId();
            data[i][1] = p.getClinicianId();
            data[i][2] = p.getAppointmentId();
            data[i][3] = String.valueOf(p.getPrescriptionDate());
            data[i][4] = p.getMedicationName();
            data[i][5] = p.getDosage();
            data[i][6] = p.getFrequency();
            data[i][7] = String.valueOf(p.getDurationDays());
            data[i][8] = p.getQuantity();
            data[i][9] = p.getPharmacyName();
            data[i][10] = p.getStatus();
            data[i][11] = String.valueOf(p.getIssueDate());
            data[i][12] = String.valueOf(p.getCollectionDate());
        }

        JTable table = new JTable(data, columns);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);

        JOptionPane.showMessageDialog(this, scrollPane,
                "Prescriptions for Patient: " + patientId,
                JOptionPane.PLAIN_MESSAGE);
    }

    private void createPrescription() {

        try {
            String patientId = patientIDField.getText().trim();
            Patient patient = patientController.findPatient(patientId);

            if (patient == null) {
                JOptionPane.showMessageDialog(this, "Patient not found!");
                return;
            }

            String clinicianId = clinicianIDField.getText().trim();
            String appointmentId = appointmentIDField.getText().trim();

            LocalDate prescriptionDate = LocalDate.parse(prescriptionDateField.getText().trim());
            String medicationName = medicationNameField.getText().trim();
            String dosage = dosageField.getText().trim();
            String frequency = frequencyField.getText().trim();
            int durationDays = Integer.parseInt(durationDaysField.getText().trim());
            String quantity = quantityField.getText().trim();
            String instructions = instructionsArea.getText().trim();
            String pharmacyName = pharmacyNameField.getText().trim();
            String status = statusField.getText().trim();
            LocalDate issueDate = LocalDate.parse(issueDateField.getText().trim());
            LocalDate collectionDate = LocalDate.parse(collectionDateField.getText().trim());

            boolean success = prescriptionController.createPrescription(
                    patientId,
                    clinicianId,
                    appointmentId,
                    prescriptionDate,
                    medicationName,
                    dosage,
                    frequency,
                    durationDays,
                    quantity,
                    instructions,
                    pharmacyName,
                    status,
                    issueDate,
                    collectionDate
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Prescription created successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create prescription.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        patientIDField.setText("");
        clinicianIDField.setText("");
        appointmentIDField.setText("");
        prescriptionDateField.setText("");
        medicationNameField.setText("");
        dosageField.setText("");
        frequencyField.setText("");
        durationDaysField.setText("");
        quantityField.setText("");
        instructionsArea.setText("");
        pharmacyNameField.setText("");
        statusField.setText("");
        issueDateField.setText("");
        collectionDateField.setText("");
    }

    private void deletePrescription(String prescriptionId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete prescription " + prescriptionId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = prescriptionController.deletePrescription(prescriptionId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Prescription deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete prescription.");
            }
        }
    }
    private void editPrescription(String prescriptionId) {
        var p = prescriptionController.findPrescription(prescriptionId);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Prescription not found!");
            return;
        }

        patientIDField.setText(p.getPatientId());
        clinicianIDField.setText(p.getClinicianId());
        appointmentIDField.setText(p.getAppointmentId());
        medicationNameField.setText(p.getMedicationName());
        dosageField.setText(p.getDosage());
        frequencyField.setText(p.getFrequency());
        durationDaysField.setText(String.valueOf(p.getDurationDays()));
        quantityField.setText(p.getQuantity());
        instructionsArea.setText(p.getInstructions());
        pharmacyNameField.setText(p.getPharmacyName());
        statusField.setText(p.getStatus());
        prescriptionDateField.setText(String.valueOf(p.getPrescriptionDate()));
        issueDateField.setText(String.valueOf(p.getIssueDate()));
        collectionDateField.setText(String.valueOf(p.getCollectionDate()));
    }

}
