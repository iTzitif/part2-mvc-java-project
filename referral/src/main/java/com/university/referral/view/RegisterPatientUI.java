package com.university.referral.view;

import com.university.referral.controller.PatientController;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterPatientUI extends JFrame {

    private JTextField firstNameField, lastNameField, dobField, nhsNumberField, genderField,
            phoneField, emailField, addressField, postcodeField, emergencyNameField, emergencyPhoneField,
            gpSurgeryIdField;
    private JButton registerButton, clearButton;
    private JLabel statusLabel;

    private PatientController patientController;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public RegisterPatientUI() {
        patientController = new PatientController();

        setTitle("Register New Patient");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int y = 0;

        gbc.gridy = y++;
        firstNameField = addField(panel, gbc, "First Name:");

        gbc.gridy = y++;
        lastNameField = addField(panel, gbc, "Last Name:");

        gbc.gridy = y++;
        dobField = addField(panel, gbc, "Date of Birth (yyyy-MM-dd):");

        gbc.gridy = y++;
        nhsNumberField = addField(panel, gbc, "NHS Number:");

        gbc.gridy = y++;
        genderField = addField(panel, gbc, "Gender:");

        gbc.gridy = y++;
        phoneField = addField(panel, gbc, "Phone Number:");

        gbc.gridy = y++;
        emailField = addField(panel, gbc, "Email:");

        gbc.gridy = y++;
        addressField = addField(panel, gbc, "Address:");

        gbc.gridy = y++;
        postcodeField = addField(panel, gbc, "Postcode:");

        gbc.gridy = y++;
        emergencyNameField = addField(panel, gbc, "Emergency Contact Name:");

        gbc.gridy = y++;
        emergencyPhoneField = addField(panel, gbc, "Emergency Contact Phone:");

        gbc.gridy = y++;
        gpSurgeryIdField = addField(panel, gbc, "GP Surgery ID:");

        gbc.gridy = y++;
        registerButton = new JButton("Register Patient");
        registerButton.addActionListener(e -> registerPatient());
        panel.add(registerButton, gbc);

        gbc.gridy = y++;
        clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton, gbc);

        gbc.gridy = y++;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel, gbc);

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, String labelText) {
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        panel.add(label, gbc);

        gbc.gridx = 1;
        JTextField textField = new JTextField(20);
        panel.add(textField, gbc);

        return textField;
    }

    private void registerPatient() {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            Date dob = dateFormat.parse(dobField.getText().trim());
            String nhsNumber = nhsNumberField.getText().trim();
            String gender = genderField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String postcode = postcodeField.getText().trim();
            String emergencyName = emergencyNameField.getText().trim();
            String emergencyPhone = emergencyPhoneField.getText().trim();
            Date registrationDate = new Date();
            String gpSurgeryId = gpSurgeryIdField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || nhsNumber.isEmpty()) {
                statusLabel.setText("First Name, Last Name, and NHS Number are required!");
                return;
            }

            boolean success = patientController.registerNewPatient(
                    firstName, lastName, dob, nhsNumber, gender, phone,
                    email, address, postcode, emergencyName, emergencyPhone,
                    registrationDate, gpSurgeryId
            );

            if (success) {
                statusLabel.setForeground(new Color(0, 128, 0));
                statusLabel.setText("Patient registered successfully!");
                clearFields();
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Failed to register patient.");
            }

        } catch (ParseException e) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        dobField.setText("");
        nhsNumberField.setText("");
        genderField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        postcodeField.setText("");
        emergencyNameField.setText("");
        emergencyPhoneField.setText("");
        gpSurgeryIdField.setText("");
        statusLabel.setText(" ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterPatientUI().setVisible(true));
    }
}
