package com.university.referral.view;

import com.university.referral.controller.AppointmentController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentAddUI extends JFrame {

    private final AppointmentController controller = new AppointmentController();

    public AppointmentAddUI() {
        setTitle("Create New Staff Member");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));

        JTextField patientIdField = new JTextField();
        JTextField clinicianIdField = new JTextField();
        JTextField dateField = new JTextField("yyyy-MM-dd");
        JTextField timeField = new JTextField("HH:mm");
        JTextField durationField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField facilityField = new JTextField();
        JTextField reasonField = new JTextField();
        JTextField notesField = new JTextField();

        panel.add(new JLabel("Patient ID:")); panel.add(patientIdField);
        panel.add(new JLabel("Clinician ID:")); panel.add(clinicianIdField);
        panel.add(new JLabel("Date:")); panel.add(dateField);
        panel.add(new JLabel("Time:")); panel.add(timeField);
        panel.add(new JLabel("Duration (min):")); panel.add(durationField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Facility:")); panel.add(facilityField);
        panel.add(new JLabel("Reason:")); panel.add(reasonField);
        panel.add(new JLabel("Notes:")); panel.add(notesField);

        JButton bookBtn = new JButton("Book Appointment");
        bookBtn.addActionListener(e -> {
            try {
                boolean success = controller.bookAppointment(
                        patientIdField.getText(),
                        clinicianIdField.getText(),
                        LocalDate.parse(dateField.getText()),
                        LocalTime.parse(timeField.getText()),
                        Integer.parseInt(durationField.getText()),
                        typeField.getText(),
                        facilityField.getText(),
                        reasonField.getText(),
                        notesField.getText()
                );

                JOptionPane.showMessageDialog(this,
                        success ? "Appointment booked successfully!" : "Failed to book appointment.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        panel.add(bookBtn);
        add(panel);
        setVisible(true);
    }
}
