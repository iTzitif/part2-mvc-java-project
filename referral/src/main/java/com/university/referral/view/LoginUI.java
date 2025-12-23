package com.university.referral.view;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleComboBox;

    public LoginUI() {
        setTitle("Healthcare Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Healthcare Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("👤 Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("🔒 Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("👨‍⚕️ Role:"), gbc);

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
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        gbc.fill = GridBagConstraints.NONE;          // Do NOT stretch
        gbc.anchor = GridBagConstraints.CENTER;      // Center the button
        gbc.insets = new Insets(10, 0, 0, 0);        // Small top spacing

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30)); // Smaller size
        loginButton.addActionListener(e -> handleLogin());

        mainPanel.add(loginButton, gbc);


        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        this.dispose();
        new MainMenuUI(role, username).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
