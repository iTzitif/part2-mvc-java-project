package com.university.referral.view;

import com.university.referral.controller.AppointmentController;
import com.university.referral.model.AppointmentRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentPanel extends JPanel {

    private AppointmentController controller;
    private JTable table;
    private DefaultTableModel model;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentPanel(AppointmentController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"ID", "Patient", "Clinician", "Facility", "Date", "Time", "Duration", "Type", "Status", "Reason", "Notes"}, 0
        );

        table = new JTable(model);
        loadData();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(delBtn);

        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addAppointment());
        editBtn.addActionListener(e -> editAppointment());
        delBtn.addActionListener(e -> deleteAppointment());
    }

    private void loadData() {
        model.setRowCount(0);
        List<AppointmentRecord> list = controller.getAll();

        for (AppointmentRecord a : list) {
            model.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getClinicianId(),
                    a.getFacilityId(),
                    a.getAppointmentDate().format(dateFormatter),
                    a.getAppointmentTime().format(timeFormatter),
                    a.getDurationMinutes(),
                    a.getAppointmentType(),
                    a.getStatus(),
                    a.getReasonForVisit(),
                    a.getNotes()
            });
        }
    }

    private void addAppointment() {
        AppointmentRecord a = showForm(null);
        if (a != null) {
            controller.addAppointment(a);
            loadData();
        }
    }

    private void editAppointment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to edit.");
            return;
        }

        AppointmentRecord old = controller.getAll().get(row);
        AppointmentRecord updated = showForm(old);

        if (updated != null) {
            controller.updateAppointment(row, updated);
            loadData();
        }
    }

    private void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        controller.deleteAppointment(row);
        loadData();
    }

    private AppointmentRecord showForm(AppointmentRecord existing) {

        JTextField idField = new JTextField(existing == null ? "" : existing.getAppointmentId());
        JTextField pField = new JTextField(existing == null ? "" : existing.getPatientId());
        JTextField cField = new JTextField(existing == null ? "" : existing.getClinicianId());
        JTextField fField = new JTextField(existing == null ? "" : existing.getFacilityId());
        JTextField dField = new JTextField(existing == null ? "" : existing.getAppointmentDate().format(dateFormatter));
        JTextField tField = new JTextField(existing == null ? "" : existing.getAppointmentTime().format(timeFormatter));
        JTextField durationField = new JTextField(existing == null ? "" : String.valueOf(existing.getDurationMinutes()));
        JTextField typeField = new JTextField(existing == null ? "" : existing.getAppointmentType());
        JTextField statusField = new JTextField(existing == null ? "" : existing.getStatus());
        JTextField reasonField = new JTextField(existing == null ? "" : existing.getReasonForVisit());
        JTextField notesField = new JTextField(existing == null ? "" : existing.getNotes());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Appointment ID:")); panel.add(idField);
        panel.add(new JLabel("Patient ID:")); panel.add(pField);
        panel.add(new JLabel("Clinician ID:")); panel.add(cField);
        panel.add(new JLabel("Facility ID:")); panel.add(fField);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(dField);
        panel.add(new JLabel("Time (HH:MM):")); panel.add(tField);
        panel.add(new JLabel("Duration (minutes):")); panel.add(durationField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Status:")); panel.add(statusField);
        panel.add(new JLabel("Reason for Visit:")); panel.add(reasonField);
        panel.add(new JLabel("Notes:")); panel.add(notesField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Appointment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            LocalDate date = LocalDate.parse(dField.getText(), dateFormatter);
            LocalTime time = LocalTime.parse(tField.getText(), timeFormatter);
            int duration = Integer.parseInt(durationField.getText());
            LocalDateTime now = LocalDateTime.now();

            return new AppointmentRecord(
                    idField.getText(),
                    pField.getText(),
                    cField.getText(),
                    fField.getText(),
                    date,
                    time,
                    duration,
                    typeField.getText(),
                    statusField.getText(),
                    reasonField.getText(),
                    notesField.getText(),
                    existing == null ? now : existing.getCreatedDate(),
                    now
            );
        }

        return null;
    }
}
