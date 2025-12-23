package com.university.referral.view;

import com.university.referral.controller.AppointmentController;
import com.university.referral.controller.ClinicianController;
import com.university.referral.controller.PatientController;
import com.university.referral.model.Appointment;
import com.university.referral.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentUI extends JFrame {
    private AppointmentController appointmentController;
    private PatientController patientController;
    private ClinicianController clinicianController;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JButton bookButton, cancelButton, refreshButton,modifyButton;
    private JTextField patientSearchField;
    private String loggedInUserID;

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AppointmentUI() {
        appointmentController = new AppointmentController();
        patientController = new PatientController();

        setTitle("Appointment Management");
        setSize(950, 600);
        setLocationRelativeTo(null);

        initComponents();
        loadAppointments();
    }
    private String userRole;

    public AppointmentUI(String loggedInUserID, String userRole) {
        appointmentController = new AppointmentController();
        patientController = new PatientController();
        clinicianController = new ClinicianController();
        this.loggedInUserID = loggedInUserID;
        this.userRole = userRole;
        setTitle("Appointment Management");
        setSize(950, 600);
        setLocationRelativeTo(null);
        initComponents();
        loadAppointments();
    }


    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("Appointment Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // ===== TOP PANEL (SEARCH + BUTTONS) =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search row
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by Patient ID:"));
        patientSearchField = new JTextField(15);
        searchPanel.add(patientSearchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchAppointmentsByPatient());
        searchPanel.add(searchButton);

        topPanel.add(searchPanel);

        // Buttons row
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookButton = new JButton("Book Appointment");
        bookButton.addActionListener(e -> showBookAppointmentDialog());
        buttonPanel.add(bookButton);

        modifyButton = new JButton("Modify Appointment");
        modifyButton.addActionListener(e -> modifyAppointment());
        buttonPanel.add(modifyButton);

        cancelButton = new JButton("Cancel Selected");
        cancelButton.addActionListener(e -> cancelSelectedAppointment());
        buttonPanel.add(cancelButton);

        refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> loadAppointments());
        buttonPanel.add(refreshButton);

        topPanel.add(buttonPanel);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL (TABLE) =====
        JPanel centerPanel = new JPanel(new BorderLayout());

        String[] columns = {"Appointment ID", "Patient ID", "Patient Name", "Clinician ID",
                "Date", "Time", "Duration (min)", "Type", "Facility Id", "Reason", "Notes", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ===== ROLE-BASED VISIBILITY =====
        if ("patient".equalsIgnoreCase(userRole)) {
            bookButton.setEnabled(false);
            cancelButton.setEnabled(true);
            refreshButton.setEnabled(true);
            modifyButton.setVisible(false);
        } else if ("General Practitioner".equalsIgnoreCase(userRole)) {
            bookButton.setVisible(false);
            cancelButton.setVisible(false);
            refreshButton.setVisible(false);
            modifyButton.setVisible(false);
        } else if ("Specialist".equalsIgnoreCase(userRole)) {
            bookButton.setVisible(false);
        }
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);

        List<Appointment> allAppointments;

        if ("patient".equalsIgnoreCase(userRole)) {
            // Show only appointments for the logged-in patient
            allAppointments = appointmentController.getPatientAppointments(loggedInUserID);
        } else if("Specialist".equalsIgnoreCase(userRole)) {
            allAppointments = clinicianController.getClinicianAppointments(loggedInUserID);
        }
        else {
            allAppointments = appointmentController.getAllAppointments();
        }

        for (Appointment appointment : allAppointments) {
            Patient patient = patientController.findPatient(appointment.getPatientId());

            Object[] row = {
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    patient != null ? patient.getFirstName() : "Unknown",
                    appointment.getClinicianId() != null ? appointment.getClinicianId() : "Not Assigned",
                    appointment.getAppointmentDate().format(DateTimeFormatter.ISO_DATE),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    appointment.getDurationMinutes(),
                    appointment.getAppointmentType(),
                    appointment.getFacilityId(),
                    appointment.getReasonForVisit(),
                    appointment.getNotes(),
                    appointment.getStatus()
            };
            tableModel.addRow(row);
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No appointments found. Book a new appointment to get started.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchAppointmentsByPatient() {
        String patientID = patientSearchField.getText().trim();

        if (patientID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Patient ID");
            return;
        }

        Patient patient = patientController.findPatient(patientID);
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not found!");
            return;
        }

        tableModel.setRowCount(0);

        List<Appointment> appointments = appointmentController.getPatientAppointments(patientID);

        for (Appointment appointment : appointments) {
            Object[] row = {
                    appointment.getAppointmentId(),
                    patient.getPatientId(),
                    patient.getFirstName(),
                    appointment.getClinicianId() != null ? appointment.getClinicianId() : "Not Assigned",
                    appointment.getAppointmentDate().format(DateTimeFormatter.ISO_DATE),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    appointment.getDurationMinutes(),
                    appointment.getAppointmentType(),
                    appointment.getFacilityId(),
                    appointment.getReasonForVisit(),
                    appointment.getNotes(),
                    appointment.getStatus()
            };
            tableModel.addRow(row);
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No appointments found for Patient ID: " + patientID);
        }
    }

    void cancelSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel");
            return;
        }

        String appointmentID = (String) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 2);
        String date = (String) tableModel.getValueAt(selectedRow, 4);
        String time = (String) tableModel.getValueAt(selectedRow, 5);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this appointment?\n\n" +
                        "Patient: " + patientName + "\n" +
                        "Date/Time: " + date + " " + time,
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = appointmentController.cancelAppointment(appointmentID);
            if (success) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully!");
                loadAppointments();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel appointment!");
            }
        }
    }
// test
    private void showBookAppointmentDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField patientIDField = new JTextField();
        JTextField clinicianIDField = new JTextField();
        JTextField dateField = new JTextField("yyyy-MM-dd");
        JTextField timeField = new JTextField("HH:mm");
        JTextField durationField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField facilityField = new JTextField();
        JTextField reasonField = new JTextField();
        JTextField notesField = new JTextField();

        panel.add(new JLabel("Patient ID:")); panel.add(patientIDField);
        panel.add(new JLabel("Clinician ID:")); panel.add(clinicianIDField);
        panel.add(new JLabel("Date (yyyy-MM-dd):")); panel.add(dateField);
        panel.add(new JLabel("Time (HH:mm):")); panel.add(timeField);
        panel.add(new JLabel("Duration (minutes):")); panel.add(durationField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Facility ID:")); panel.add(facilityField);
        panel.add(new JLabel("Reason:")); panel.add(reasonField);
        panel.add(new JLabel("Notes:")); panel.add(notesField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Book New Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String patientID = patientIDField.getText().trim();
                String clinicianID = clinicianIDField.getText().trim();
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                LocalTime time = LocalTime.parse(timeField.getText().trim());
                int duration = Integer.parseInt(durationField.getText().trim());
                String type = typeField.getText().trim();
                String facility = facilityField.getText().trim();
                String reason = reasonField.getText().trim();
                String notes = notesField.getText().trim();

                boolean success = appointmentController.bookAppointment(
                        patientID, clinicianID, date, time, duration, type, facility, reason, notes
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
                    loadAppointments();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to book appointment!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void modifyAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to modify");
            return;
        }
        if ("patient".equalsIgnoreCase(userRole)) {
            String selectedPatientID = (String) tableModel.getValueAt(selectedRow, 1);
            if (!loggedInUserID.equals(selectedPatientID)) {
                JOptionPane.showMessageDialog(this, "You can only modify your own appointments!");
                return;
            }
        }
        // Open the same booking dialog prefilled with selected data
        showModifyAppointmentDialog(selectedRow);
    }
    private void showModifyAppointmentDialog(int row) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        String appointmentId = (String) tableModel.getValueAt(row, 0);
        String patientId = (String) tableModel.getValueAt(row, 1);
        String clinicianId = (String) tableModel.getValueAt(row, 3);
        String date = (String) tableModel.getValueAt(row, 4);
        String time = (String) tableModel.getValueAt(row, 5);
        String duration = String.valueOf(tableModel.getValueAt(row, 6));
        String type = (String) tableModel.getValueAt(row, 7);
        String facility = (String) tableModel.getValueAt(row, 8);
        String reason = (String) tableModel.getValueAt(row, 9);
        String notes = (String) tableModel.getValueAt(row, 10);

        JTextField patientIDField = new JTextField(patientId);
        JTextField clinicianIDField = new JTextField(clinicianId);
        JTextField dateField = new JTextField(date);
        JTextField timeField = new JTextField(time);
        JTextField durationField = new JTextField(duration);
        JTextField typeField = new JTextField(type);
        JTextField facilityField = new JTextField(facility);
        JTextField reasonField = new JTextField(reason);
        JTextField notesField = new JTextField(notes);

        panel.add(new JLabel("Patient ID:")); panel.add(patientIDField);
        panel.add(new JLabel("Clinician ID:")); panel.add(clinicianIDField);
        panel.add(new JLabel("Date:")); panel.add(dateField);
        panel.add(new JLabel("Time:")); panel.add(timeField);
        panel.add(new JLabel("Duration:")); panel.add(durationField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Facility ID:")); panel.add(facilityField);
        panel.add(new JLabel("Reason:")); panel.add(reasonField);
        panel.add(new JLabel("Notes:")); panel.add(notesField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Modify Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Update appointment using controller
            appointmentController.updateAppointment(
                    appointmentId,
                    patientIDField.getText().trim(),
                    clinicianIDField.getText().trim(),
                    LocalDate.parse(dateField.getText().trim()),
                    LocalTime.parse(timeField.getText().trim()),
                    Integer.parseInt(durationField.getText().trim()),
                    typeField.getText().trim(),
                    facilityField.getText().trim(),
                    reasonField.getText().trim(),
                    notesField.getText().trim()
            );
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Appointment modified successfully!");
        }
    }

}
