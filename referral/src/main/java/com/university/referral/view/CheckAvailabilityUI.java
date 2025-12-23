package com.university.referral.view;

import com.university.referral.controller.ClinicianController;
import com.university.referral.model.Appointment;
import com.university.referral.model.Clinician;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class CheckAvailabilityUI extends JFrame {
    private ClinicianController clinicianController;
    private JTable clinicianTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, checkAvailabilityButton, refreshButton;
    private JLabel statusLabel;
    private String loggedInClinicianID;
    private String userRole;

    public CheckAvailabilityUI(String userRole) {
        this.userRole = userRole;
        clinicianController = new ClinicianController();

        setTitle("Clinician Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadClinicians();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(41, 128, 185));
        JLabel title = new JLabel("Clinician Management");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by ID, Name, or Speciality");
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchClinicians());
        searchPanel.add(searchButton);

        topPanel.add(searchPanel);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkAvailabilityButton = new JButton("Check Availability");
        checkAvailabilityButton.setBackground(new Color(41, 128, 185));
        checkAvailabilityButton.setForeground(Color.WHITE);
        checkAvailabilityButton.setFocusPainted(false);
        checkAvailabilityButton.setToolTipText("Check selected clinician's available appointments");
        checkAvailabilityButton.addActionListener(e -> showAvailabilityDialog());

        refreshButton = new JButton("Refresh All");
        refreshButton.setToolTipText("Reload all clinicians");
        refreshButton.addActionListener(e -> loadClinicians());

        topButtons.add(checkAvailabilityButton);
        topButtons.add(Box.createRigidArea(new Dimension(10, 0)));
        topButtons.add(refreshButton);

        topPanel.add(topButtons);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        String[] columns = {
                "Clinician ID", "First Name", "Last Name", "Title", "Speciality",
                "GMC Number", "Phone", "Email", "Workplace ID", "Workplace Type",
                "Employment Status", "Start Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        clinicianTable = new JTable(tableModel);
        clinicianTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clinicianTable.setRowHeight(25);
        clinicianTable.setAutoCreateRowSorter(true);

        clinicianTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(clinicianTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        searchField.addActionListener(e -> searchClinicians());
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { liveSearch(); }
            public void removeUpdate(DocumentEvent e) { liveSearch(); }
            public void insertUpdate(DocumentEvent e) { liveSearch(); }
            private void liveSearch() {
                if (searchField.getText().trim().isEmpty()) {
                    loadClinicians();
                }
            }
        });
    }

    private void loadClinicians() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        statusLabel.setText(" ");

        if (loggedInClinicianID == null) {
            List<Clinician> list = clinicianController.getAllClinicians();

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
                statusLabel.setText("No clinicians found.");
            }
            return;
        }

        Clinician c = clinicianController.findClinician(loggedInClinicianID);
        if (c == null) {
            statusLabel.setText("Clinician profile not found!");
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
        statusLabel.setText(" ");

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
            statusLabel.setText("No clinicians match: " + search);
        }
    }

    private void showAvailabilityDialog() {
        int row = clinicianTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a clinician first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clinicianID = (String) tableModel.getValueAt(row, 0);
        String name = tableModel.getValueAt(row, 1) + " " + tableModel.getValueAt(row, 2);

        JDialog dialog = new JDialog(this, true);
        dialog.setTitle("Availability for: " + name);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Appointment> availableAppointments = clinicianController.getAvailableClinicians(clinicianID);

        String clinicianAppointments = availableAppointments.isEmpty()
                ? "No available appointments"
                : availableAppointments.stream()
                .map(a -> "Appointment on " + a.getAppointmentDate() + " at " + a.getAppointmentTime())
                .distinct()
                .collect(Collectors.joining("\n"));

        JTextArea textArea = new JTextArea(clinicianAppointments);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dialog.dispose());
        panel.add(close, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckAvailabilityUI("admin").setVisible(true));
    }
}



