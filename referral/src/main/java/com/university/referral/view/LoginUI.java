package com.university.referral.view;

import com.university.referral.controller.PatientController;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private JTextField usernameField;
    private JButton loginButton;
    private JComboBox<String> roleComboBox;
    private PatientController patientController;

    public LoginUI() {
        setTitle("Healthcare Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        patientController = new PatientController();

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Healthcare Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // User ID Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("👤 UserId:"), gbc);

        // User ID Field
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        // Role Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("👨‍⚕️ Role:"), gbc);

        // Role ComboBox
        gbc.gridx = 1;
        String[] roles = {
                "Patient",
                "Specialist",
                "Receptionist",
                "General Practitioner",
                "Nurse"
        };
        roleComboBox = new JComboBox<>(roles);
        mainPanel.add(roleComboBox, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> handleLogin());

        mainPanel.add(loginButton, gbc);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean authenticated = patientController.authenticate(username, role);

        if (!authenticated) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Patient ID. Please enter a valid Patient ID.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.dispose();
        new MainMenuUI(role, username).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
