package com.university.referral.view;

import com.university.referral.model.PatientRecord;

import javax.swing.*;
import java.awt.*;

public class AddPatientDialog extends JDialog {

    private JTextField idField, firstNameField, lastNameField, dobField,
            nhsField, genderField, phoneField, emailField, addressField,
            postcodeField, emergencyNameField, emergencyPhoneField,
            registrationField, gpSurgeryField;

    private boolean saved = false;
    private PatientRecord newRecord;

    public AddPatientDialog(JFrame parent) {
        super(parent, "Add New Patient", true);
        setLayout(new GridLayout(15, 2, 5, 5));

        idField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        dobField = new JTextField();
        nhsField = new JTextField();
        genderField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();
        postcodeField = new JTextField();
        emergencyNameField = new JTextField();
        emergencyPhoneField = new JTextField();
        registrationField = new JTextField();
        gpSurgeryField = new JTextField();

        add(new JLabel("Patient ID:")); add(idField);
        add(new JLabel("First Name:")); add(firstNameField);
        add(new JLabel("Last Name:")); add(lastNameField);
        add(new JLabel("Date of Birth:")); add(dobField);
        add(new JLabel("NHS Number:")); add(nhsField);
        add(new JLabel("Gender:")); add(genderField);
        add(new JLabel("Phone Number:")); add(phoneField);
        add(new JLabel("Email:")); add(emailField);
        add(new JLabel("Address:")); add(addressField);
        add(new JLabel("Postcode:")); add(postcodeField);
        add(new JLabel("Emergency Contact Name:")); add(emergencyNameField);
        add(new JLabel("Emergency Contact Phone:")); add(emergencyPhoneField);
        add(new JLabel("Registration Date:")); add(registrationField);
        add(new JLabel("GP Surgery ID:")); add(gpSurgeryField);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            newRecord = new PatientRecord(
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
            saved = true;
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        add(saveBtn); add(cancelBtn);

        setSize(500, 600);
        setLocationRelativeTo(parent);
    }

    public boolean isSaved() { return saved; }
    public PatientRecord getNewRecord() { return newRecord; }
}
