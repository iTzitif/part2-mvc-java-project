package com.university.referral.view;

import com.university.referral.controller.AppointmentController;
import com.university.referral.model.AppointmentRecord;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class AppointmentPanel extends JPanel {

    private final JTable table;
    private AppointmentTableModel tableModel;
    private final JTextField searchField;
    private final List<AppointmentRecord> originalList;
    private final AppointmentController controller;
    private final JFrame parentFrame;

    public AppointmentPanel(JFrame parentFrame, AppointmentController controller, List<AppointmentRecord> appointments) {
        this.parentFrame = parentFrame;
        this.controller = controller;
        this.originalList = appointments;

        setLayout(new BorderLayout());

        // Top search
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        top.add(new JLabel("Search (patient id/date):"));
        top.add(searchField);
        add(top, BorderLayout.NORTH);

        tableModel = new AppointmentTableModel(appointments);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        buttons.add(addBtn); buttons.add(editBtn); buttons.add(delBtn);
        add(buttons, BorderLayout.SOUTH);

        // Actions: simple input dialogs for quick functionality
        addBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(parentFrame,
                    "Enter appointment fields as comma-separated:\nID,PatientID,ClinicianID,Date(yyyy-MM-dd),Time(HH:mm),Type",
                    "A001," , JOptionPane.PLAIN_MESSAGE);
            if (input != null && !input.isBlank()) {
                String[] parts = input.split(",", -1);
                if (parts.length >= 6) {
                    AppointmentRecord a = new AppointmentRecord(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(), parts[5].trim());
                    controller.addAppointment(a);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid input. Need 6 comma-separated values.");
                }
            }
        });

        editBtn.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parentFrame, "Select a row to edit."); return; }
            AppointmentRecord existing = originalList.get(r);
            String pre = String.join(",", existing.getAppointmentId(), existing.getPatientId(),
                    existing.getClinicianId(), existing.getAppointmentDate(), existing.getAppointmentTime(),
                    existing.getAppointmentType());
            String input = JOptionPane.showInputDialog(parentFrame, "Edit appointment (comma-separated):", pre);
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 6) {
                    AppointmentRecord updated = new AppointmentRecord(p[0].trim(), p[1].trim(), p[2].trim(),
                            p[3].trim(), p[4].trim(), p[5].trim());
                    controller.updateAppointment(r, updated);
                    refreshTable();
                } else JOptionPane.showMessageDialog(parentFrame, "Invalid input.");
            }
        });

        delBtn.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parentFrame, "Select a row to delete."); return; }
            if (JOptionPane.showConfirmDialog(parentFrame, "Confirm delete?") == JOptionPane.YES_OPTION) {
                controller.deleteAppointment(r);
                refreshTable();
            }
        });

        // Search filtering
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
        });
    }

    private void filter() {
        String kw = searchField.getText().toLowerCase();
        var filtered = originalList.stream()
                .filter(a -> a.getPatientId().toLowerCase().contains(kw) || a.getAppointmentDate().contains(kw))
                .toList();
        tableModel = new AppointmentTableModel(filtered);
        table.setModel(tableModel);
    }

    private void refreshTable() {
        tableModel = new AppointmentTableModel(originalList);
        table.setModel(tableModel);
    }
}
