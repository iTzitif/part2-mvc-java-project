package com.university.referral.view;

import com.university.referral.controller.StaffMemberController;
import com.university.referral.model.StaffMember;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class CreateStaffUI extends JFrame {

    private final StaffMemberController controller = new StaffMemberController();

    public CreateStaffUI() {
        setTitle("Create New Staff Member");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(13, 2, 5, 5));

        // Input fields
        JTextField staffIdField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField facilityField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField employmentStatusField = new JTextField();
        JTextField startDateField = new JTextField("yyyy-MM-dd");
        JTextField lineManagerField = new JTextField();
        JTextField accessLevelField = new JTextField();

        panel.add(new JLabel("Staff ID:")); panel.add(staffIdField);
        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
        panel.add(new JLabel("Role:")); panel.add(roleField);
        panel.add(new JLabel("Department:")); panel.add(departmentField);
        panel.add(new JLabel("Facility ID:")); panel.add(facilityField);
        panel.add(new JLabel("Phone Number:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Employment Status:")); panel.add(employmentStatusField);
        panel.add(new JLabel("Start Date:")); panel.add(startDateField);
        panel.add(new JLabel("Line Manager:")); panel.add(lineManagerField);
        panel.add(new JLabel("Access Level:")); panel.add(accessLevelField);

        JButton createBtn = new JButton("Create Staff Member");
        createBtn.addActionListener(e -> {
            try {
                StaffMember staff = new StaffMember(
                        staffIdField.getText(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        roleField.getText(),
                        departmentField.getText(),
                        facilityField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        employmentStatusField.getText(),
                        LocalDate.parse(startDateField.getText()),
                        lineManagerField.getText(),
                        accessLevelField.getText()
                );

                boolean success = controller.addStaffMember(staff);

                JOptionPane.showMessageDialog(this,
                        success ? "Staff member created successfully!" : "Failed to create staff member.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        panel.add(createBtn);
        add(panel);
        setVisible(true);
    }
}
