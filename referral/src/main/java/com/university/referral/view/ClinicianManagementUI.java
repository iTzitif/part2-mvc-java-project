package com.university.referral.view;

import com.university.referral.controller.ClinicianController;
import com.university.referral.model.Clinician;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClinicianManagementUI extends JFrame {
    private ClinicianController clinicianController;
    private JTable clinicianTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, addButton, refreshButton, updateButton, deleteButton;
    private String loggedInClinicianID;

    private String userRole; // e.g., "Admin", "Clinician", "Patient"

    public ClinicianManagementUI(String userRole) {
        this.userRole = userRole;
        clinicianController = new ClinicianController();

        setTitle("Clinician Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);

        initComponents();
        loadClinicians();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(41, 128, 185));

        JLabel title = new JLabel("Clinician Management");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Add New Clinician");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showAddClinicianDialog());
        topButtons.add(addButton);

        updateButton = new JButton("Update Selected");
        updateButton.setBackground(new Color(241, 196, 15));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> showUpdateClinicianDialog());
        topButtons.add(updateButton);

        deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteSelectedClinician());
        topButtons.add(deleteButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topButtons, BorderLayout.NORTH);

        String[] columns = {
                "Clinician ID", "First Name", "Last Name", "Title", "Speciality",
                "GMC Number", "Phone", "Email", "Workplace ID", "Workplace Type",
                "Employment Status", "Start Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        clinicianTable = new JTable(tableModel);
        clinicianTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clinicianTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(clinicianTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchClinicians());
        searchPanel.add(searchButton);

        refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> loadClinicians());
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.SOUTH);

        if (loggedInClinicianID != null) {
            addButton.setVisible(false);
            deleteButton.setVisible(false);
            searchButton.setVisible(false);
            searchField.setVisible(false);
            refreshButton.setVisible(false);

            updateButton.setText("Update My Profile");
        }
        if ("Patient".equalsIgnoreCase(userRole)) {
             title = new JLabel("View");
             title.setFont(new Font("Arial", Font.BOLD, 20));
             title.setForeground(Color.WHITE);
             header.add(title, BorderLayout.WEST);

            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }

    }

    private void loadClinicians() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (loggedInClinicianID == null) {
            java.util.List<Clinician> list = clinicianController.getAllClinicians();

            for (Clinician c : list) {
                Object[] row = {
                        c.getClinicianID(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getTitle(),
                        c.getSpeciality(),
                        c.getGmcNumber(),
                        c.getPhoneNumber(),
                        c.getEmail(),
                        c.getWorkplaceID(),
                        c.getWorkplaceType(),
                        c.getEmploymentStatus(),
                        sdf.format(c.getStartDate())
                };
                tableModel.addRow(row);
            }

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No clinicians found.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        Clinician c = clinicianController.findClinician(loggedInClinicianID);

        if (c == null) {
            JOptionPane.showMessageDialog(this, "Clinician profile not found!");
            return;
        }

        tableModel.addRow(new Object[]{
                c.getClinicianID(),
                c.getFirstName(),
                c.getLastName(),
                c.getTitle(),
                c.getSpeciality(),
                c.getGmcNumber(),
                c.getPhoneNumber(),
                c.getEmail(),
                c.getWorkplaceID(),
                c.getWorkplaceType(),
                c.getEmploymentStatus(),
                sdf.format(c.getStartDate())
        });
    }

    private void searchClinicians() {
        String search = searchField.getText().trim();
        tableModel.setRowCount(0);

        List<Clinician> list = clinicianController.searchClinicians(search);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Clinician c : list) {
            tableModel.addRow(new Object[]{
                    c.getClinicianID(),
                    c.getFirstName(),
                    c.getLastName(),
                    c.getTitle(),
                    c.getSpeciality(),
                    c.getGmcNumber(),
                    c.getPhoneNumber(),
                    c.getEmail(),
                    c.getWorkplaceID(),
                    c.getWorkplaceType(),
                    c.getEmploymentStatus(),
                    sdf.format(c.getStartDate())
            });
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No clinicians match: " + search);
        }
    }

    private void showAddClinicianDialog() {
        JDialog dialog = createClinicianEditorDialog(null);
        dialog.setTitle("Add New Clinician");
        dialog.setVisible(true);
    }

    private void showUpdateClinicianDialog() {
        int row = clinicianTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a clinician to update");
            return;
        }

        String clinicianID = (String) tableModel.getValueAt(row, 0);
        Clinician clinician = clinicianController.findClinician(clinicianID);

        if (clinician == null) {
            JOptionPane.showMessageDialog(this, "Clinician not found!");
            return;
        }

        JDialog dialog = createClinicianEditorDialog(clinician);
        dialog.setTitle("Update Clinician");
        dialog.setVisible(true);
    }

    private JDialog createClinicianEditorDialog(Clinician existing) {
        JDialog dialog = new JDialog(this, true);
        dialog.setSize(500, 700);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // --- Form Fields ---
        int y = 0;

        JTextField fName = addField(panel, gbc, y++, "First Name: *", existing == null ? "" : existing.getFirstName());
        JTextField lName = addField(panel, gbc, y++, "Last Name: *", existing == null ? "" : existing.getLastName());
        JTextField title = addField(panel, gbc, y++, "Title:", existing == null ? "" : existing.getTitle());
        JTextField spec = addField(panel, gbc, y++, "Speciality:", existing == null ? "" : existing.getSpeciality());
        JTextField gmc = addField(panel, gbc, y++, "GMC Number:", existing == null ? "" : existing.getGmcNumber());
        JTextField phone = addField(panel, gbc, y++, "Phone: *", existing == null ? "" : existing.getPhoneNumber());
        JTextField email = addField(panel, gbc, y++, "Email:", existing == null ? "" : existing.getEmail());
        JTextField workID = addField(panel, gbc, y++, "Workplace ID:", existing == null ? "" : existing.getWorkplaceID());
        JTextField workType = addField(panel, gbc, y++, "Workplace Type:", existing == null ? "" : existing.getWorkplaceType());
        JTextField empStatus = addField(panel, gbc, y++, "Employment Status:", existing == null ? "" : existing.getEmploymentStatus());
        JTextField start = addField(panel, gbc, y++, "Start Date (yyyy-mm-dd): *",
                existing == null ? sdf.format(new Date()) : sdf.format(existing.getStartDate())
        );

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;

        JButton save = new JButton(existing == null ? "Add Clinician" : "Update");
        save.setBackground(new Color(46, 204, 113));
        save.setForeground(Color.WHITE);

        JButton cancel = new JButton("Cancel");

        JPanel btns = new JPanel();
        btns.add(save);
        btns.add(cancel);
        panel.add(btns, gbc);

        save.addActionListener(e -> {
            try {
                if (fName.getText().trim().isEmpty() || lName.getText().trim().isEmpty() || phone.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "First name, last name and phone are required!");
                    return;
                }

                Date startDate = sdf.parse(start.getText().trim());

                boolean success;

                if (existing == null) {
                    success = clinicianController.registerNewClinician(
                            fName.getText(), lName.getText(), title.getText(), spec.getText(),
                            gmc.getText(), phone.getText(), email.getText(),
                            workID.getText(), workType.getText(), empStatus.getText(), startDate
                    );
                } else {
                    success = clinicianController.updateClinician(
                            existing.getClinicianID(),
                            fName.getText(), lName.getText(), title.getText(), spec.getText(),
                            gmc.getText(), phone.getText(), email.getText(),
                            workID.getText(), workType.getText(), empStatus.getText(), startDate
                    );
                }

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Saved successfully!");
                    dialog.dispose();
                    loadClinicians();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to save clinician.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        cancel.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        return dialog;
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, int y, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField(20);
        field.setText(value);
        panel.add(field, gbc);

        return field;
    }

    private void deleteSelectedClinician() {
        int row = clinicianTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a clinician to delete");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        String name = tableModel.getValueAt(row, 1) + " " + tableModel.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this clinician?\n\nID: " + id + "\nName: " + name,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (clinicianController.deleteClinician(id)) {
                JOptionPane.showMessageDialog(this, "Clinician deleted successfully");
                loadClinicians();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete clinician");
            }
        }
    }
}
