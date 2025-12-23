package com.university.referral.view;

import com.university.referral.controller.PatientController;
import com.university.referral.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class PatientProfileUI extends JFrame {

    private final PatientController controller = new PatientController();
    private final String patientId;

    public PatientProfileUI(String patientId) {
        this.patientId = patientId;

        setTitle("My Profile");
        setSize(500, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadProfile();
    }

    private void loadProfile() {
        Patient p = controller.findPatient(patientId);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Patient not found!");
            dispose();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(14, 2, 8, 8));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        JTextField[] f = {
                new JTextField(p.getFirstName()),
                new JTextField(p.getLastName()),
                new JTextField(sdf.format(p.getDateOfBirth())),
                new JTextField(p.getNhsNumber()),
                new JTextField(p.getGender()),
                new JTextField(p.getPhoneNumber()),
                new JTextField(p.getEmail()),
                new JTextField(p.getAddress()),
                new JTextField(p.getPostcode()),
                new JTextField(p.getEmergencyContactName()),
                new JTextField(p.getEmergencyContactPhone()),
                new JTextField(sdf.format(p.getRegistrationDate())),
                new JTextField(p.getGpSurgeryId())
        };

        String[] labels = {
                "First Name","Last Name","DOB","NHS Number","Gender","Phone",
                "Email","Address","Postcode","Emergency Name",
                "Emergency Phone","Registration Date","GP Surgery ID"
        };

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            panel.add(f[i]);
        }

        JButton update = new JButton("Update");
        update.addActionListener(e -> updateProfile(p, f));

        panel.add(update);
        panel.add(new JButton("Close") {{
            addActionListener(e -> dispose());
        }});

        add(panel);
        setVisible(true);
    }

    private void updateProfile(Patient p, JTextField[] f) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            controller.updatePatient(
                    p.getPatientId(),
                    f[0].getText(), f[1].getText(),
                    sdf.parse(f[2].getText()),
                    f[3].getText(), f[4].getText(),
                    f[5].getText(), f[6].getText(),
                    f[7].getText(), f[8].getText(),
                    f[9].getText(), f[10].getText(),
                    sdf.parse(f[11].getText()),
                    f[12].getText()
            );

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
