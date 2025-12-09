package com.university.referral.view;

import com.university.referral.model.PatientRecord;

import javax.swing.*;
import java.awt.*;

public class EditPatientDialog extends JDialog {

    private JTextField idField, firstNameField, lastNameField, dobField,
            nhsField, genderField, phoneField, emailField, addressField,
            postcodeField, emergencyNameField, emergencyPhoneField,
            registrationField, gpSurgeryField;

    private boolean updated = false;
    private PatientRecord updatedRecord;

    public EditPatientDialog(JFrame parent, PatientRecord existing) {
        super(parent, "Edit Patient Record", true);
        setLayout(new GridLayout(15, 2, 5, 5));

        idField = new JTextField(existing.getPatientId());
        firstNameField = new JTextField(existing.getFirstName());
        lastNameField = new JTextField(existing.getLastName());
        dobField = new JTextField(existing.getDateOfBirth());
        nhsField = new JTextField(existing.getNhsNumber());
        genderField = new JTextField(existing.getGender());
        phoneField = new JTextField(existing.getPhoneNumber());
        emailField = new JTextField(existing.getEmail());
        addressField = new JTextField(existing.getAddress());
        postcodeField = new JTextField(existing.getPostcode());
        emergencyNameField = new JTextField(existing.getEmergencyContactName());
        emergencyPhoneField = new JTextField(existing.getEmergencyContactPhone());
        registrationField = new JTextField(existing.getRegistrationDate());
        gpSurgeryField = new JTextField(existing.getGpSurgeryId());

        add(new JLabel("Patient ID:")); add(idField);
        add(new JLabel("First Name:")); add(firstNameField);
        add(new JLabel("Last Name:")); add(lastNameField);
        add(new JLabel("Date of Birth:")); add(dobField);
        add(new JLabel("NHS Number:")); add(nhsField);
        add(new JLabel("Gender:")); add(genderField);
        add(new JLabel("Phone Number:")); add(phoneField);
        add(new JLabel("Email Address:")); add(emailField);
        add(new JLabel("Address:")); add(addressField);
        add(new JLabel("Postcode:")); add(postcodeField);
        add(new JLabel("Emergency Contact Name:")); add(emergencyNameField);
        add(new JLabel("Emergency Contact Phone:")); add(emergencyPhoneField);
        add(new JLabel("Registration Date:")); add(registrationField);
        add(new JLabel("GP Surgery ID:")); add(gpSurgeryField);

        JButton saveBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            updatedRecord = new PatientRecord(
                    idField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    dobField.getText(),
                    nhsField.getText(),
                    genderField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    postcodeField.getText(),
                    emergencyNameField.getText(),
                    emergencyPhoneField.getText(),
                    registrationField.getText(),
                    gpSurgeryField.getText()
            );
            updated = true;
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        add(saveBtn); add(cancelBtn);

        setSize(500, 600);
        setLocationRelativeTo(parent);
    }

    public boolean isUpdated() { return updated; }
    public PatientRecord getUpdatedRecord() { return updatedRecord; }
}
