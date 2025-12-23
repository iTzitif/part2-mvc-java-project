package com.university.referral.view;

import com.university.referral.controller.MedicalFacilityController;
import com.university.referral.controller.StaffMemberController;
import com.university.referral.model.MedicalFacility;
import com.university.referral.model.StaffMember;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedicalFacilityUI extends JFrame {
    private MedicalFacilityController facilityController;
    private JTable facilityTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, refreshButton, deleteButton, updateButton;
    private JLabel statusLabel;

    public MedicalFacilityUI() {
        facilityController = new MedicalFacilityController();

        setTitle("Medical Facility Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadFacilities();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(41, 128, 185));
        JLabel title = new JLabel("Medical Facility Management");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel topButtons = new JPanel();
        topButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        updateButton = new JButton("Update Facility");
        updateButton.setBackground(new Color(41, 128, 185));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> updateFacility());

        deleteButton = new JButton("Delete Facility");
        deleteButton.setBackground(new Color(192, 57, 43));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteFacility());

        refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> loadFacilities());
        JButton createButton = new JButton("Create Facility");
        createButton.setBackground(new Color(39, 174, 96));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.addActionListener(e -> createFacility());

        topButtons.add(createButton, 0);

        topButtons.add(updateButton);
        topButtons.add(deleteButton);
        topButtons.add(refreshButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topButtons, BorderLayout.NORTH);

        String[] columns = {
                "Facility ID", "Name", "Type", "Address", "Postcode",
                "Phone", "Email", "Opening Hours", "Manager", "Capacity", "Specialities"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        facilityTable = new JTable(tableModel);
        facilityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        facilityTable.setRowHeight(25);
        facilityTable.setAutoCreateRowSorter(true);

        facilityTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(facilityTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by ID, Name, Type, or Specialities");
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchFacilities());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.SOUTH);

        searchField.addActionListener(e -> searchFacilities());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { liveSearch(); }
            public void removeUpdate(DocumentEvent e) { liveSearch(); }
            public void insertUpdate(DocumentEvent e) { liveSearch(); }
            private void liveSearch() {
                if (searchField.getText().trim().isEmpty()) {
                    loadFacilities();
                }
            }
        });
    }

    private void loadFacilities() {
        tableModel.setRowCount(0);
        statusLabel.setText(" ");

        java.util.List<MedicalFacility> list = facilityController.getAllFacilities();
        for (MedicalFacility f : list) {
            Object[] row = {
                    f.getFacilityId(),
                    f.getFacilityName(),
                    f.getFacilityType(),
                    f.getAddress(),
                    f.getPostcode(),
                    f.getPhoneNumber(),
                    f.getEmail(),
                    f.getOpeningHours(),
                    f.getManagerName(),
                    f.getCapacity(),
                    f.getSpecialitiesOffered()
            };
            tableModel.addRow(row);
        }

        if (list.isEmpty()) {
            statusLabel.setText("No medical facilities found.");
        }
    }

    private void searchFacilities() {
        String search = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        statusLabel.setText(" ");

        List<MedicalFacility> list = facilityController.getAllFacilities();
        list.stream()
                .filter(f -> f.getFacilityId().toLowerCase().contains(search) ||
                        f.getFacilityName().toLowerCase().contains(search) ||
                        f.getFacilityType().toLowerCase().contains(search) ||
                        f.getSpecialitiesOffered().toLowerCase().contains(search))
                .forEach(f -> tableModel.addRow(new Object[]{
                        f.getFacilityId(),
                        f.getFacilityName(),
                        f.getFacilityType(),
                        f.getAddress(),
                        f.getPostcode(),
                        f.getPhoneNumber(),
                        f.getEmail(),
                        f.getOpeningHours(),
                        f.getManagerName(),
                        f.getCapacity(),
                        f.getSpecialitiesOffered()
                }));

        if (tableModel.getRowCount() == 0) {
            statusLabel.setText("No facilities match: " + search);
        }
    }

    private void deleteFacility() {
        int row = facilityTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a facility to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String facilityId = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete facility ID: " + facilityId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            facilityController.deleteFacility(facilityId);
            loadFacilities();
            JOptionPane.showMessageDialog(this, "Facility deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateFacility() {
        int row = facilityTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a facility to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String facilityId = (String) tableModel.getValueAt(row, 0);
        MedicalFacility facility = facilityController.findFacility(facilityId);

        if (facility == null) {
            JOptionPane.showMessageDialog(this, "Selected facility not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField(facility.getFacilityName());
        JTextField typeField = new JTextField(facility.getFacilityType());
        JTextField addressField = new JTextField(facility.getAddress());
        JTextField postcodeField = new JTextField(facility.getPostcode());
        JTextField phoneField = new JTextField(facility.getPhoneNumber());
        JTextField emailField = new JTextField(facility.getEmail());
        JTextField hoursField = new JTextField(facility.getOpeningHours());
        JTextField managerField = new JTextField(facility.getManagerName());
        JTextField capacityField = new JTextField(String.valueOf(facility.getCapacity()));
        JTextField specialitiesField = new JTextField(facility.getSpecialitiesOffered());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("Postcode:")); panel.add(postcodeField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Opening Hours:")); panel.add(hoursField);
        panel.add(new JLabel("Manager Name:")); panel.add(managerField);
        panel.add(new JLabel("Capacity:")); panel.add(capacityField);
        panel.add(new JLabel("Specialities:")); panel.add(specialitiesField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Update Facility ID: " + facilityId, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            facility.setFacilityName(nameField.getText().trim());
            facility.setFacilityType(typeField.getText().trim());
            facility.setAddress(addressField.getText().trim());
            facility.setPostcode(postcodeField.getText().trim());
            facility.setPhoneNumber(phoneField.getText().trim());
            facility.setEmail(emailField.getText().trim());
            facility.setOpeningHours(hoursField.getText().trim());
            facility.setManagerName(managerField.getText().trim());
            facility.setCapacity(Integer.parseInt(capacityField.getText().trim()));
            facility.setSpecialitiesOffered(specialitiesField.getText().trim());

            facilityController.updateFacility(facility);
            loadFacilities();
            JOptionPane.showMessageDialog(this, "Facility updated successfully.", "Updated", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void createFacility() {
        JTextField nameField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField postcodeField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField hoursField = new JTextField();
        JTextField managerField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField specialitiesField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("Postcode:")); panel.add(postcodeField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Opening Hours:")); panel.add(hoursField);
        panel.add(new JLabel("Manager Name:")); panel.add(managerField);
        panel.add(new JLabel("Capacity:")); panel.add(capacityField);
        panel.add(new JLabel("Specialities:")); panel.add(specialitiesField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Create New Medical Facility", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                MedicalFacility facility = new MedicalFacility();
                facility.setFacilityId(facilityController.generateFacilityID());
                facility.setFacilityName(nameField.getText().trim());
                facility.setFacilityType(typeField.getText().trim());
                facility.setAddress(addressField.getText().trim());
                facility.setPostcode(postcodeField.getText().trim());
                facility.setPhoneNumber(phoneField.getText().trim());
                facility.setEmail(emailField.getText().trim());
                facility.setOpeningHours(hoursField.getText().trim());
                facility.setManagerName(managerField.getText().trim());
                facility.setCapacity(Integer.parseInt(capacityField.getText().trim()));
                facility.setSpecialitiesOffered(specialitiesField.getText().trim());

                boolean success = facilityController.addFacility(facility);

                if (success) {
                    loadFacilities();
                    JOptionPane.showMessageDialog(this, "Facility created successfully.", "Created", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create facility.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Capacity must be a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedicalFacilityUI().setVisible(true));
    }
}



