package com.university.referral.view;

import com.university.referral.model.PatientRecord;

import javax.swing.*;
import java.awt.*;

public class AddPatientDialog extends JDialog {

    private JTextField idField, nameField, dobField, phoneField, emailField;
    private boolean saved = false;
    private PatientRecord newRecord;

    public AddPatientDialog(JFrame parent) {
        super(parent, "Add New Patient", true);
        setLayout(new GridLayout(6, 2, 5, 5));

        idField = new JTextField();
        nameField = new JTextField();
        dobField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        add(new JLabel("Patient ID:"));
        add(idField);

        add(new JLabel("Full Name:"));
        add(nameField);

        add(new JLabel("Date of Birth:"));
        add(dobField);

        add(new JLabel("Phone Number:"));
        add(phoneField);

        add(new JLabel("Email Address:"));
        add(emailField);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            newRecord = new PatientRecord(
                    idField.getText().trim(),
                    nameField.getText().trim(),
                    dobField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            saved = true;
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        add(saveBtn);
        add(cancelBtn);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    public boolean isSaved() {
        return saved;
    }

    public PatientRecord getNewRecord() {
        return newRecord;
    }
}

