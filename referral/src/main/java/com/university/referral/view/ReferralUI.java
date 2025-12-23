package com.university.referral.view;

import com.university.referral.controller.ReferralController;
import com.university.referral.util.SingletonReferralManager;
import com.university.referral.model.Referral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReferralUI extends JFrame {

    private ReferralController referralController;
    private SingletonReferralManager referralManager;

    private JTextField patientIDField, referringClinicianIDField, referredToClinicianIDField;
    private JTextField referringFacilityIDField, referredToFacilityIDField, specialtyField, urgencyField;
    private JTextArea reasonArea, clinicalSummaryArea, requestedInvestigationsArea, notesArea;

    private String userRole, loggedInUserID, type;
    private boolean notesEditable = false;

    private JButton createButton, viewQueueButton, clearButton, saveNotesButton;
    private JTable referralTable;
    private DefaultTableModel tableModel;

    public ReferralUI() {
        this("", "", "");
    }

    public ReferralUI(String userRole, String loggedInUserID, String type) {
        this.userRole = userRole;
        this.loggedInUserID = loggedInUserID;
        this.type = type;

        referralController = new ReferralController();
        referralManager = SingletonReferralManager.getInstance();

        setTitle("Referral Management");
        setSize(1000, 750);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("Referral Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        if ("createReferral".equalsIgnoreCase(type)) {
            add(createFormPanel(), BorderLayout.CENTER);
        } else {
            add(createTablePanel(), BorderLayout.CENTER);
            loadReferrals();
        }

        JPanel infoPanel = new JPanel();
        JLabel infoLabel = new JLabel("Referrals are managed by SingletonReferralManager");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoPanel.add(infoLabel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Referral"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        y = addLabelAndField(formPanel, gbc, y, "Patient ID:", patientIDField = new JTextField(20));
        y = addLabelAndField(formPanel, gbc, y, "Referring Clinician ID:", referringClinicianIDField = new JTextField(20));
        y = addLabelAndField(formPanel, gbc, y, "Referred To Clinician ID:", referredToClinicianIDField = new JTextField(20));
        y = addLabelAndField(formPanel, gbc, y, "Referring Facility ID:", referringFacilityIDField = new JTextField(20));
        y = addLabelAndField(formPanel, gbc, y, "Referred To Facility ID:", referredToFacilityIDField = new JTextField(20));
        y = addLabelAndField(formPanel, gbc, y, "Specialty:", specialtyField = new JTextField(20));
        y = addLabelAndField(formPanel, gbc, y, "Urgency Level:", urgencyField = new JTextField(20));
        y = addLabelAndArea(formPanel, gbc, y, "Referral Reason:", reasonArea = new JTextArea(3, 20));
        y = addLabelAndArea(formPanel, gbc, y, "Clinical Summary:", clinicalSummaryArea = new JTextArea(3, 20));
        y = addLabelAndArea(formPanel, gbc, y, "Requested Investigations:", requestedInvestigationsArea = new JTextArea(3, 20));
        y = addLabelAndArea(formPanel, gbc, y, "Notes:", notesArea = new JTextArea(3, 20));

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createButton = new JButton("Create Referral");
        createButton.addActionListener(e -> createReferral());
        viewQueueButton = new JButton("View Queue Status");
        viewQueueButton.addActionListener(e -> showQueueStatus());
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(createButton);
        buttonPanel.add(viewQueueButton);
        buttonPanel.add(clearButton);

        formPanel.add(buttonPanel, gbc);
        return formPanel;
    }

    private int addLabelAndField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JTextField field) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        return y + 1;
    }

    private int addLabelAndArea(JPanel panel, GridBagConstraints gbc, int y, String labelText, JTextArea area) {
        area.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(area);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(scroll, gbc);
        return y + 1;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("All Referrals"));

        String[] columns = {
                "Referral ID", "Patient ID", "Referring Clinician", "Referred To Clinician",
                "Referring Facility", "Referred Facility", "Referral Date", "Urgency", "Reason",
                "Clinical Summary", "Investigations", "Status", "Appointment ID", "Notes",
                "Created Date", "Last Updated"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return notesEditable && referralTable.getSelectedRow() == row && column == 13;
            }
        };

        referralTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (notesEditable && column == 13 && referralTable.getSelectedRow() == row) {
                    comp.setBackground(new Color(255, 255, 200)); // Highlight editable cell
                } else {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        };

        referralTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referralTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(referralTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadReferrals());
        JButton editNotesButton = new JButton("Edit Notes");
        editNotesButton.addActionListener(e -> enableNotesEditing());
        saveNotesButton = new JButton("Save Notes");
        saveNotesButton.addActionListener(e -> saveSelectedNotes());

        btnPanel.add(refreshButton);
        btnPanel.add(editNotesButton);
        btnPanel.add(saveNotesButton);
        tablePanel.add(btnPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private void createReferral() {
        try {
            String patientId = patientIDField.getText().trim();
            String specialty = specialtyField.getText().trim();
            String reason = reasonArea.getText().trim();

            if (patientId.isEmpty() || specialty.isEmpty() || reason.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Patient ID, Specialty, and Reason are required!");
                return;
            }

            boolean success = referralController.createReferral(
                    patientIDField.getText().trim(),
                    referringClinicianIDField.getText().trim(),
                    referredToClinicianIDField.getText().trim(),
                    referringFacilityIDField.getText().trim(),
                    referredToFacilityIDField.getText().trim(),
                    LocalDate.now(),
                    urgencyField.getText().trim(),
                    reasonArea.getText().trim(),
                    clinicalSummaryArea.getText().trim(),
                    requestedInvestigationsArea.getText().trim(),
                    "Pending",
                    null,
                    notesArea.getText().trim()
            );

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Referral created successfully!\nQueue size: " + referralManager.getQueueSize());
                clearFields();
                loadReferrals();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create referral!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadReferrals() {
        tableModel.setRowCount(0);
        List<Referral> referrals;

        if ("Specialist".equalsIgnoreCase(userRole)) {
            referrals = "receiveReferral".equalsIgnoreCase(type) ?
                    referralController.getClinicianReceiveReferral(loggedInUserID) :
                    referralController.getClinicianReferrals(loggedInUserID);
        } else {
            referrals = referralController.getAllReferrals();
        }

        for (Referral r : referrals) {
            Object[] row = {
                    r.getReferral_id(),
                    r.getPatient_id(),
                    r.getReferring_clinician_id(),
                    r.getReferred_to_clinician_id(),
                    r.getReferring_facility_id(),
                    r.getReferred_to_facility_id(),
                    r.getReferral_date(),
                    r.getUrgency_level(),
                    r.getReferral_reason(),
                    r.getClinical_summary(),
                    r.getRequested_investigations(),
                    r.getStatus(),
                    r.getAppointment_id(),
                    r.getNotes(),
                    r.getCreated_date(),
                    r.getLast_updated()
            };
            tableModel.addRow(row);
        }
    }

    private void showQueueStatus() {
        int queueSize = referralManager.getQueueSize();
        int totalReferrals = referralManager.getAllReferrals().size();
        String message = String.format(
                "Singleton Referral Manager Status:\n\nTotal Referrals: %d\nPending in Queue: %d\nProcessed: %d",
                totalReferrals, queueSize, totalReferrals - queueSize
        );
        JOptionPane.showMessageDialog(this, message, "Queue Status", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        patientIDField.setText("");
        referringClinicianIDField.setText("");
        referredToClinicianIDField.setText("");
        referringFacilityIDField.setText("");
        referredToFacilityIDField.setText("");
        specialtyField.setText("");
        urgencyField.setText("");
        reasonArea.setText("");
        clinicalSummaryArea.setText("");
        requestedInvestigationsArea.setText("");
        notesArea.setText("");
    }

    private void enableNotesEditing() {
        int selectedRow = referralTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a referral from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        notesEditable = true;
        tableModel.fireTableDataChanged();
        JOptionPane.showMessageDialog(this, "Edit the Notes cell (highlighted) and click 'Save Notes' to save.", "Edit Enabled", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveSelectedNotes() {
        int selectedRow = referralTable.getSelectedRow();
        if (selectedRow == -1) return;

        String referralId = tableModel.getValueAt(selectedRow, 0).toString();
        String newNotes = tableModel.getValueAt(selectedRow, 13).toString().trim();

        if (newNotes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Notes cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = referralController.updateReferralNotes(referralId, newNotes);
        if (success) {
            JOptionPane.showMessageDialog(this, "Referral notes updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            notesEditable = false;
            loadReferrals();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update referral notes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReferralUI("Admin", "admin01", "createReferral").setVisible(true));
    }
}
