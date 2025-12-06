package com.university.referral.view;

import com.university.referral.model.PatientRecord;

import javax.swing.*;
import java.awt.*;

public class EditPatientDialog extends JDialog {

    private JTextField idField, nameField, dobField, phoneField, emailField;
    private boolean updated = false;
    private PatientRecord updatedRecord;

    public EditPatientDialog(JFrame parent, PatientRecord existing) {
        super(parent, "Edit Patient Record", true);
        setLayout(new GridLayout(6, 2, 5, 5));

        idField = new JTextField(existing.getPatientId());
        nameField = new JTextField(existing.getName());
        dobField = new JTextField(existing.getDob());
        phoneField = new JTextField(existing.getPhone());
        emailField = new JTextField(existing.getEmail());

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

        JButton saveBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            updatedRecord = new PatientRecord(
                    idField.getText().trim(),
                    nameField.getText().trim(),
                    dobField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
            );
            updated = true;
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        add(saveBtn);
        add(cancelBtn);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    public boolean isUpdated() {
        return updated;
    }

    public PatientRecord getUpdatedRecord() {
        return updatedRecord;
    }
}
