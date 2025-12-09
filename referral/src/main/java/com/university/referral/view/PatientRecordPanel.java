package com.university.referral.view;

import com.university.referral.controller.PatientRecordController;
import com.university.referral.model.PatientRecord;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class PatientRecordPanel extends JPanel {

    private JTable table;
    private PatientRecordTableModel tableModel;

    private PatientRecordController controller;
    private JFrame parentFrame;

    private JTextField searchField;
    private List<PatientRecord> originalList;

    private JButton addButton, editButton, deleteButton;

    public PatientRecordPanel(JFrame parentFrame,
                              PatientRecordController controller,
                              List<PatientRecord> patientRecords) {

        this.parentFrame = parentFrame;
        this.controller = controller;
        this.originalList = patientRecords;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        topPanel.add(new JLabel("Search: "));
        topPanel.add(searchField);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new PatientRecordTableModel(patientRecords);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterTable(); }

            @Override
            public void removeUpdate(DocumentEvent e) { filterTable(); }

            @Override
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        initializeButtonActions();
    }

    private void initializeButtonActions() {

        // ADD PATIENT
        addButton.addActionListener(e -> {
            AddPatientDialog dialog = new AddPatientDialog(parentFrame);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                controller.addPatient(dialog.getNewRecord());
                refreshTable();
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a record to edit.");
                return;
            }

            PatientRecord existing = originalList.get(selectedRow);
            EditPatientDialog dialog = new EditPatientDialog(parentFrame, existing);
            dialog.setVisible(true);

            if (dialog.isUpdated()) {
                controller.updatePatient(selectedRow, dialog.getUpdatedRecord());
                refreshTable();
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a record to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this patient?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                controller.deletePatient(selectedRow);
                refreshTable();
            }
        });
    }

    private void filterTable() {
        String keyword = searchField.getText().toLowerCase();

        List<PatientRecord> filtered = originalList.stream()
                .filter(p -> p.getFirstName().toLowerCase().contains(keyword)
                        || p.getLastName().toLowerCase().contains(keyword)
                        || p.getPatientId().toLowerCase().contains(keyword)
                        || p.getEmail().toLowerCase().contains(keyword))
                .toList();

        tableModel = new PatientRecordTableModel(filtered);
        table.setModel(tableModel);
    }

    private void refreshTable() {
        tableModel = new PatientRecordTableModel(originalList);
        table.setModel(tableModel);
    }
}
