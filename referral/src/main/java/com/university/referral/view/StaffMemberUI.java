package com.university.referral.view;

import com.university.referral.controller.StaffMemberController;
import com.university.referral.model.StaffMember;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffMemberUI extends JFrame {
    private StaffMemberController staffController;
    private JTable staffTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, refreshButton, deleteButton, updateButton;
    private JLabel statusLabel;

    public StaffMemberUI() {
        staffController = new StaffMemberController();

        setTitle("Staff Member Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadStaffMembers();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(41, 128, 185));
        JLabel title = new JLabel("Staff Member Management");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // TOP BUTTONS
        JPanel topButtons = new JPanel();
        topButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        updateButton = new JButton("Update Staff");
        updateButton.setBackground(new Color(41, 128, 185));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> updateStaffMember());

        deleteButton = new JButton("Delete Staff");
        deleteButton.setBackground(new Color(192, 57, 43));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteStaffMember());

        refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> loadStaffMembers());

        JButton createButton = new JButton("Create Staff");
        createButton.setBackground(new Color(39, 174, 96));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.addActionListener(e -> createStaffMember());

        topButtons.add(createButton, 0);
        topButtons.add(updateButton);
        topButtons.add(deleteButton);
        topButtons.add(refreshButton);

        // CENTER PANEL - TABLE
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topButtons, BorderLayout.NORTH);

        String[] columns = {
                "Staff ID", "First Name", "Last Name", "Role", "Department",
                "Facility ID", "Phone", "Email", "Employment Status", "Start Date",
                "Line Manager", "Access Level"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable = new JTable(tableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.setRowHeight(25);
        staffTable.setAutoCreateRowSorter(true);

        // Row striping
        staffTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(staffTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // STATUS LABEL
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // SEARCH PANEL
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by ID, Name, Role, or Department");
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStaffMembers());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.SOUTH);

        // Search by Enter key
        searchField.addActionListener(e -> searchStaffMembers());

        // Optional: live search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { liveSearch(); }
            public void removeUpdate(DocumentEvent e) { liveSearch(); }
            public void insertUpdate(DocumentEvent e) { liveSearch(); }
            private void liveSearch() {
                if (searchField.getText().trim().isEmpty()) {
                    loadStaffMembers();
                }
            }
        });
    }

    private void loadStaffMembers() {
        tableModel.setRowCount(0);
        statusLabel.setText(" ");

        java.util.List<StaffMember> list = staffController.getAllStaffMembers();
        for (StaffMember s : list) {
            Object[] row = {
                    s.getStaffId(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getRole(),
                    s.getDepartment(),
                    s.getFacilityId(),
                    s.getPhoneNumber(),
                    s.getEmail(),
                    s.getEmploymentStatus(),
                    s.getStartDate(),
                    s.getLineManager(),
                    s.getAccessLevel()
            };
            tableModel.addRow(row);
        }

        if (list.isEmpty()) {
            statusLabel.setText("No staff members found.");
        }
    }

    private void searchStaffMembers() {
        String search = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        statusLabel.setText(" ");

        List<StaffMember> list = staffController.searchStaffMembers(search);
        for (StaffMember s : list) {
            tableModel.addRow(new Object[]{
                    s.getStaffId(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getRole(),
                    s.getDepartment(),
                    s.getFacilityId(),
                    s.getPhoneNumber(),
                    s.getEmail(),
                    s.getEmploymentStatus(),
                    s.getStartDate(),
                    s.getLineManager(),
                    s.getAccessLevel()
            });
        }

        if (tableModel.getRowCount() == 0) {
            statusLabel.setText("No staff match: " + search);
        }
    }

    private void deleteStaffMember() {
        int row = staffTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String staffId = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete staff ID: " + staffId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            staffController.deleteStaffMember(staffId);
            loadStaffMembers();
            JOptionPane.showMessageDialog(this, "Staff member deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateStaffMember() {
        int row = staffTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String staffId = (String) tableModel.getValueAt(row, 0);
        StaffMember staff = staffController.findStaffMember(staffId);

        if (staff == null) {
            JOptionPane.showMessageDialog(this, "Selected staff member not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField firstNameField = new JTextField(staff.getFirstName());
        JTextField lastNameField = new JTextField(staff.getLastName());
        JTextField roleField = new JTextField(staff.getRole());
        JTextField departmentField = new JTextField(staff.getDepartment());
        JTextField facilityIdField = new JTextField(staff.getFacilityId());
        JTextField phoneField = new JTextField(staff.getPhoneNumber());
        JTextField emailField = new JTextField(staff.getEmail());
        JTextField statusField = new JTextField(staff.getEmploymentStatus());
        JTextField startDateField = new JTextField(staff.getStartDate().toString());
        JTextField managerField = new JTextField(staff.getLineManager());
        JTextField accessField = new JTextField(staff.getAccessLevel());

        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
        panel.add(new JLabel("Role:")); panel.add(roleField);
        panel.add(new JLabel("Department:")); panel.add(departmentField);
        panel.add(new JLabel("Facility ID:")); panel.add(facilityIdField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Employment Status:")); panel.add(statusField);
        panel.add(new JLabel("Start Date (YYYY-MM-DD):")); panel.add(startDateField);
        panel.add(new JLabel("Line Manager:")); panel.add(managerField);
        panel.add(new JLabel("Access Level:")); panel.add(accessField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Update Staff ID: " + staffId, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                staff.setFirstName(firstNameField.getText().trim());
                staff.setLastName(lastNameField.getText().trim());
                staff.setRole(roleField.getText().trim());
                staff.setDepartment(departmentField.getText().trim());
                staff.setFacilityId(facilityIdField.getText().trim());
                staff.setPhoneNumber(phoneField.getText().trim());
                staff.setEmail(emailField.getText().trim());
                staff.setEmploymentStatus(statusField.getText().trim());
                staff.setStartDate(java.time.LocalDate.parse(startDateField.getText().trim()));
                staff.setLineManager(managerField.getText().trim());
                staff.setAccessLevel(accessField.getText().trim());

                staffController.updateStaffMember(staff);
                loadStaffMembers();
                JOptionPane.showMessageDialog(this, "Staff member updated successfully.", "Updated", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createStaffMember() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField facilityIdField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField managerField = new JTextField();
        JTextField accessField = new JTextField();

        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
        panel.add(new JLabel("Role:")); panel.add(roleField);
        panel.add(new JLabel("Department:")); panel.add(departmentField);
        panel.add(new JLabel("Facility ID:")); panel.add(facilityIdField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Employment Status:")); panel.add(statusField);
        panel.add(new JLabel("Start Date (YYYY-MM-DD):")); panel.add(startDateField);
        panel.add(new JLabel("Line Manager:")); panel.add(managerField);
        panel.add(new JLabel("Access Level:")); panel.add(accessField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Create New Staff Member", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                StaffMember staff = new StaffMember(
                        staffController.generateStaffID(),
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        roleField.getText().trim(),
                        departmentField.getText().trim(),
                        facilityIdField.getText().trim(),
                        phoneField.getText().trim(),
                        emailField.getText().trim(),
                        statusField.getText().trim(),
                        java.time.LocalDate.parse(startDateField.getText().trim()),
                        managerField.getText().trim(),
                        accessField.getText().trim()
                );

                boolean success = staffController.addStaffMember(staff);

                if (success) {
                    loadStaffMembers();
                    JOptionPane.showMessageDialog(this, "Staff member created successfully.", "Created", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create staff member.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StaffMemberUI().setVisible(true));
    }
}
