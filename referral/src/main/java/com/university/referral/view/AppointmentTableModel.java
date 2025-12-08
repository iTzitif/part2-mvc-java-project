package com.university.referral.view;

import com.university.referral.model.AppointmentRecord;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class AppointmentTableModel extends AbstractTableModel {

    private final List<AppointmentRecord> list;
    private final String[] columns = { "ID", "Patient ID", "Clinician ID", "Date", "Time", "Type" };

    public AppointmentTableModel(List<AppointmentRecord> list) { this.list = list; }

    @Override public int getRowCount() { return list.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int col) { return columns[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        AppointmentRecord a = list.get(row);
        return switch (col) {
            case 0 -> a.getAppointmentId();
            case 1 -> a.getPatientId();
            case 2 -> a.getClinicianId();
            case 3 -> a.getAppointmentDate();
            case 4 -> a.getAppointmentTime();
            case 5 -> a.getAppointmentType();
            default -> null;
        };
    }
}


