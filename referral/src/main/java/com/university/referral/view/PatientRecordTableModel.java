package com.university.referral.view;

import com.university.referral.model.PatientRecord;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PatientRecordTableModel extends AbstractTableModel {

    private List<PatientRecord> patientRecords;

    private final String[] columnNames = {
            "Patient ID", "First Name", "Last Name", "Date of Birth",
            "NHS Number", "Gender", "Phone", "Email", "Address",
            "Postcode", "Emergency Contact", "Emergency Phone",
            "Registration Date", "GP Surgery ID"
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
        PatientRecord p = patientRecords.get(row);
        return switch (col) {
            case 0 -> p.getPatientId();
            case 1 -> p.getFirstName();
            case 2 -> p.getLastName();
            case 3 -> p.getDateOfBirth();
            case 4 -> p.getNhsNumber();
            case 5 -> p.getGender();
            case 6 -> p.getPhoneNumber();
            case 7 -> p.getEmail();
            case 8 -> p.getAddress();
            case 9 -> p.getPostcode();
            case 10 -> p.getEmergencyContactName();
            case 11 -> p.getEmergencyContactPhone();
            case 12 -> p.getRegistrationDate();
            case 13 -> p.getGpSurgeryId();
            default -> null;
        };
    }

    public void refresh() {
        fireTableDataChanged();
    }
}
