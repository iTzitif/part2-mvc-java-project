package com.university.referral.view;

import com.university.referral.controller.PrescriptionController;
import com.university.referral.model.PrescriptionEntry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class PrescriptionPanel extends JPanel {

    private final JTable table;
    private PrescriptionTableModel tableModel;
    private final PrescriptionController controller;
    private final List<PrescriptionEntry> originalList;
    private final JTextField searchField;
    private final JFrame parent;

    public PrescriptionPanel(JFrame parentFrame, PrescriptionController controller, List<PrescriptionEntry> prescriptions) {
        this.parent = parentFrame;
        this.controller = controller;
        this.originalList = prescriptions;

        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        top.add(new JLabel("Search (patient/id):")); top.add(searchField);
        add(top, BorderLayout.NORTH);

        tableModel = new PrescriptionTableModel(prescriptions);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton add = new JButton("Add"), edit = new JButton("Edit"), del = new JButton("Delete");
        buttons.add(add); buttons.add(edit); buttons.add(del);
        add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(parent, "Enter: ID,AppointmentID,PatientID,ClinicianID,Medication,Dosage,Instructions");
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 7) {
                    controller.addPrescription(new PrescriptionEntry(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                            p[4].trim(), p[5].trim(), p[6].trim()));
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select a row"); return; }
            PrescriptionEntry ex = originalList.get(r);
            String pre = String.join(",", ex.getPrescriptionId(), ex.getAppointmentId(), ex.getPatientId(), ex.getClinicianId(),
                    ex.getMedicationDetails(), ex.getDosage(), ex.getInstructions());
            String input = JOptionPane.showInputDialog(parent, "Edit prescription:", pre);
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 7) {
                    controller.updatePrescription(r, new PrescriptionEntry(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                            p[4].trim(), p[5].trim(), p[6].trim()));
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select"); return; }
            if (JOptionPane.showConfirmDialog(parent, "Confirm delete?") == JOptionPane.YES_OPTION) {
                controller.deletePrescription(r); refresh();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
        });
    }

    private void filter() {
        String kw = searchField.getText().toLowerCase();
        var filtered = originalList.stream()
                .filter(p -> p.getPatientId().toLowerCase().contains(kw) || p.getPrescriptionId().toLowerCase().contains(kw))
                .toList();
        tableModel = new PrescriptionTableModel(filtered);
        table.setModel(tableModel);
    }

    private void refresh() {
        tableModel = new PrescriptionTableModel(originalList);
        table.setModel(tableModel);
    }
}
