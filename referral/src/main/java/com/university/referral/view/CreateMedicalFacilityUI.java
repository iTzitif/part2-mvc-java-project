package com.university.referral.view;

import com.university.referral.controller.MedicalFacilityController;
import com.university.referral.model.MedicalFacility;

import javax.swing.*;
import java.awt.*;

public class CreateMedicalFacilityUI extends JFrame {

    private final MedicalFacilityController controller = new MedicalFacilityController();

    public CreateMedicalFacilityUI() {
        setTitle("Create New Medical Facility");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));

        JTextField facilityIdField = new JTextField();
        JTextField facilityNameField = new JTextField();
        JTextField facilityTypeField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField postcodeField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField openingHoursField = new JTextField();
        JTextField managerNameField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField specialitiesField = new JTextField();

        panel.add(new JLabel("Facility ID:")); panel.add(facilityIdField);
        panel.add(new JLabel("Facility Name:")); panel.add(facilityNameField);
        panel.add(new JLabel("Facility Type:")); panel.add(facilityTypeField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("Postcode:")); panel.add(postcodeField);
        panel.add(new JLabel("Phone Number:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Opening Hours:")); panel.add(openingHoursField);
        panel.add(new JLabel("Manager Name:")); panel.add(managerNameField);
        panel.add(new JLabel("Capacity:")); panel.add(capacityField);
        panel.add(new JLabel("Specialities Offered:")); panel.add(specialitiesField);

        JButton createBtn = new JButton("Create Medical Facility");
        createBtn.addActionListener(e -> {
            try {
                int capacity = Integer.parseInt(capacityField.getText());

                MedicalFacility facility = new MedicalFacility(
                        facilityIdField.getText(),
                        facilityNameField.getText(),
                        facilityTypeField.getText(),
                        addressField.getText(),
                        postcodeField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        openingHoursField.getText(),
                        managerNameField.getText(),
                        capacity,
                        specialitiesField.getText()
                );

                boolean success = controller.addFacility(facility);

                JOptionPane.showMessageDialog(this,
                        success ? "Medical facility created successfully!" : "Failed to create medical facility.");

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Capacity must be a number.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        panel.add(createBtn);
        add(panel);
        setVisible(true);
    }
}
