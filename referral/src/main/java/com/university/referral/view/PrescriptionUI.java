package com.university.referral.view;

import com.university.referral.controller.*;
import com.university.referral.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PrescriptionUI extends JFrame {

    private PrescriptionController prescriptionController;
    private PatientController patientController;
    private ClinicianController clinicianController;
    private AppointmentController appointmentController;

    private JComboBox<String> patientCombo;
    private JComboBox<String> clinicianCombo;
    private JComboBox<String> appointmentCombo;
    private JComboBox<String> statusCombo;

    private JTextField medicationNameField;
    private JTextField dosageField;
    private JTextField frequencyField;
    private JTextField durationDaysField;
    private JTextField quantityField;
    private JTextArea instructionsArea;
    private JTextField pharmacyNameField;

    private JSpinner prescriptionDateSpinner;
    private JSpinner issueDateSpinner;
    private JSpinner collectionDateSpinner;

    private String loggedInUserID;
    private String userRole;
    private boolean isPatientMode;

    public PrescriptionUI() {
        isPatientMode = false;
        init();
    }

    public PrescriptionUI(String loggedInUserID) {
        this.loggedInUserID = loggedInUserID;
        isPatientMode = true;
        init();
    }

    public PrescriptionUI(String loggedInUserID, String userRole) {
        this.loggedInUserID = loggedInUserID;
        this.userRole = userRole;
        isPatientMode = false;
        init();
    }

    private void init() {
        prescriptionController = new PrescriptionController();
        patientController = new PatientController();
        clinicianController = new ClinicianController();
        appointmentController = new AppointmentController();

        setTitle("Prescription");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (isPatientMode) {
            initPatientViewOnly();
        } else if ("General Practitioner".equalsIgnoreCase(userRole)) {
            initGPView();
        } else {
            initCreateView();
        }
    }

    /* ===================== PATIENT VIEW ===================== */

    private void initPatientViewOnly() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Prescriptions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        var prescriptions = prescriptionController.getPatientPrescriptions(loggedInUserID);

        if (prescriptions.isEmpty()) {
            add(new JLabel("No prescriptions found.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        String[] cols = {
                "ID", "Clinician", "Appointment", "Date",
                "Medication", "Dosage", "Frequency",
                "Days", "Qty", "Pharmacy", "Status",
                "Issue", "Collected"
        };

        Object[][] data = new Object[prescriptions.size()][cols.length];

        for (int i = 0; i < prescriptions.size(); i++) {
            var p = prescriptions.get(i);
            data[i] = new Object[]{
                    p.getPrescriptionId(),
                    p.getClinicianId(),
                    p.getAppointmentId(),
                    p.getPrescriptionDate(),
                    p.getMedicationName(),
                    p.getDosage(),
                    p.getFrequency(),
                    p.getDurationDays(),
                    p.getQuantity(),
                    p.getPharmacyName(),
                    p.getStatus(),
                    p.getIssueDate(),
                    p.getCollectionDate()
            };
        }

        JTable table = new JTable(data, cols);
        table.setEnabled(false);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /* ===================== GP VIEW ===================== */

    private void initGPView() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("All Prescriptions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        var prescriptions = prescriptionController.getAllPrescriptions();

        String[] cols = {
                "ID", "Patient", "Clinician", "Medication",
                "Dosage", "Status", "Edit", "Delete"
        };

        Object[][] data = new Object[prescriptions.size()][cols.length];

        for (int i = 0; i < prescriptions.size(); i++) {
            var p = prescriptions.get(i);
            data[i] = new Object[]{
                    p.getPrescriptionId(),
                    p.getPatientId(),
                    p.getClinicianId(),
                    p.getMedicationName(),
                    p.getDosage(),
                    p.getStatus(),
                    "Edit",
                    "Delete"
            };
        }

        JTable table = new JTable(data, cols);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                String id = table.getValueAt(row, 0).toString();
                if (col == 6) editPrescription(id);
                if (col == 7) deletePrescription(id);
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /* ===================== CREATE VIEW ===================== */

    private void initCreateView() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Create Prescription", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        patientCombo = new JComboBox<>();
        patientController.getAllPatients()
                .forEach(p -> patientCombo.addItem(p.getPatientId() + " - " + p.getFirstName()));

        clinicianCombo = new JComboBox<>();
        clinicianController.getAllClinicians()
                .forEach(c -> clinicianCombo.addItem(c.getClinicianID() + " - " + c.getFirstName()));

        appointmentCombo = new JComboBox<>();
        appointmentController.getAllAppointments()
                .forEach(a -> appointmentCombo.addItem(a.getAppointmentId()));

        medicationNameField = new JTextField(20);
        dosageField = new JTextField(20);
        frequencyField = new JTextField(20);
        durationDaysField = new JTextField(20);
        quantityField = new JTextField(20);
        pharmacyNameField = new JTextField(20);
        instructionsArea = new JTextArea(4, 20);

        prescriptionDateSpinner = createDateSpinner();
        issueDateSpinner = createDateSpinner();
        collectionDateSpinner = createDateSpinner();

        statusCombo = new JComboBox<>(new String[]{"Pending", "Issued", "Collected"});

        addRow(form, gbc, row++, "Patient", patientCombo);
        addRow(form, gbc, row++, "Clinician", clinicianCombo);
        addRow(form, gbc, row++, "Appointment", appointmentCombo);
        addRow(form, gbc, row++, "Medication", medicationNameField);
        addRow(form, gbc, row++, "Dosage", dosageField);
        addRow(form, gbc, row++, "Frequency", frequencyField);
        addRow(form, gbc, row++, "Duration Days", durationDaysField);
        addRow(form, gbc, row++, "Quantity", quantityField);
        addRow(form, gbc, row++, "Instructions", new JScrollPane(instructionsArea));
        addRow(form, gbc, row++, "Prescription Date", prescriptionDateSpinner);
        addRow(form, gbc, row++, "Issue Date", issueDateSpinner);
        addRow(form, gbc, row++, "Collection Date", collectionDateSpinner);
        addRow(form, gbc, row++, "Pharmacy", pharmacyNameField);
        addRow(form, gbc, row++, "Status", statusCombo);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton create = new JButton("Create");
        JButton clear = new JButton("Clear");

        create.addActionListener(e -> createPrescription());
        clear.addActionListener(e -> clearFields());

        buttons.add(create);
        buttons.add(clear);
        add(buttons, BorderLayout.SOUTH);
    }

    /* ===================== LOGIC ===================== */

    private void createPrescription() {
        try {
            String patientId = patientCombo.getSelectedItem().toString().split(" - ")[0];
            Patient patient = patientController.findPatient(patientId);
            if (patient == null) {
                JOptionPane.showMessageDialog(this, "Patient not found!");
                return;
            }

            boolean success = prescriptionController.createPrescription(
                    patientId,
                    clinicianCombo.getSelectedItem().toString().split(" - ")[0],
                    appointmentCombo.getSelectedItem().toString(),
                    toLocalDate(prescriptionDateSpinner),
                    medicationNameField.getText(),
                    dosageField.getText(),
                    frequencyField.getText(),
                    Integer.parseInt(durationDaysField.getText()),
                    quantityField.getText(),
                    instructionsArea.getText(),
                    pharmacyNameField.getText(),
                    statusCombo.getSelectedItem().toString(),
                    toLocalDate(issueDateSpinner),
                    toLocalDate(collectionDateSpinner)
            );

            JOptionPane.showMessageDialog(this,
                    success ? "Prescription created successfully!" : "Failed to create prescription.");

            if (success) clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPrescription(String id) {
        JOptionPane.showMessageDialog(this,
                "Edit handled via create screen.\n(ID: " + id + ")");
    }

    private void deletePrescription(String id) {
        if (JOptionPane.showConfirmDialog(this,
                "Delete prescription " + id + "?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            prescriptionController.deletePrescription(id);
            JOptionPane.showMessageDialog(this, "Prescription deleted.");
        }
    }

    private void clearFields() {
        medicationNameField.setText("");
        dosageField.setText("");
        frequencyField.setText("");
        durationDaysField.setText("");
        quantityField.setText("");
        instructionsArea.setText("");
        pharmacyNameField.setText("");
        statusCombo.setSelectedIndex(0);
    }

    private JSpinner createDateSpinner() {
        JSpinner s = new JSpinner(new SpinnerDateModel());
        s.setEditor(new JSpinner.DateEditor(s, "yyyy-MM-dd"));
        return s;
    }

    private LocalDate toLocalDate(JSpinner spinner) {
        Date d = (Date) spinner.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, String label, Component c) {
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(c, gbc);
    }
}
