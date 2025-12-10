package com.university.referral.view;

import com.university.referral.controller.PatientController;
import com.university.referral.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientManagementUI extends JFrame {
    private PatientController patientController;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, addButton, refreshButton;

    public PatientManagementUI() {
        patientController = new PatientController();

        setTitle("Patient Management");
        setSize(900, 600);
        setLocationRelativeTo(null);

        initComponents();
        loadPatients();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Patient Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchPatients());
        searchPanel.add(searchButton);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadPatients());
        searchPanel.add(refreshButton);

        addButton = new JButton("Add New Patient");
        addButton.addActionListener(e -> showAddPatientDialog());
        searchPanel.add(addButton);

        add(searchPanel, BorderLayout.SOUTH);

        // Table Panel
        String[] columns = {"Patient ID", "Name", "DOB", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPatients() {
        tableModel.setRowCount(0);
        java.util.List<Patient> patients = patientController.getAllPatients();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Patient patient : patients) {
            Object[] row = {
                    patient.getPatientID(),
                    patient.getName(),
                    dateFormat.format(patient.getDateOfBirth()),
                    patient.getPhone(),
                    patient.getEmail(),
                    patient.getAddress()
            };
            tableModel.addRow(row);
        }
    }

    private void searchPatients() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadPatients();
            return;
        }

        tableModel.setRowCount(0);
        List<Patient> patients = patientController.searchPatients(searchTerm);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Patient patient : patients) {
            Object[] row = {
                    patient.getPatientID(),
                    patient.getName(),
                    dateFormat.format(patient.getDateOfBirth()),
                    patient.getPhone(),
                    patient.getEmail(),
                    patient.getAddress()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog(this, "Add New Patient", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Date of Birth
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("DOB (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        JTextField dobField = new JTextField(20);
        panel.add(dobField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        JTextField addressField = new JTextField(20);
        panel.add(addressField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Emergency Contact
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Emergency Contact:"), gbc);
        gbc.gridx = 1;
        JTextField emergencyField = new JTextField(20);
        panel.add(emergencyField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dob = sdf.parse(dobField.getText());
                String address = addressField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String emergency = emergencyField.getText();

                if (name.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name and Phone are required!");
                    return;
                }

                boolean success = patientController.registerNewPatient(
                        name, dob, address, phone, email, emergency
                );

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Patient registered successfully!");
                    dialog.dispose();
                    loadPatients();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to register patient!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
