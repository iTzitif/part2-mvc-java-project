package com.university.referral.view;

import com.university.referral.model.PatientRecord;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PatientRecordTableModel extends AbstractTableModel {

    private List<PatientRecord> patientRecords;

    private final String[] columnNames = {
            "Patient ID", "Full Name", "Date of Birth", "Phone Number", "Email"
    };

    public PatientRecordTableModel(List<PatientRecord> patientRecords) {
        this.patientRecords = patientRecords;
    }

    @Override
    public int getRowCount() {
        return patientRecords.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        PatientRecord patient = patientRecords.get(row);

        return switch (col) {
            case 0 -> patient.getPatientId();
            case 1 -> patient.getName();
            case 2 -> patient.getDob();
            case 3 -> patient.getPhone();
            case 4 -> patient.getEmail();
            default -> null;
        };
    }

    public void refresh() {
        fireTableDataChanged();
    }
}
