package com.university.referral.view;

import com.university.referral.model.AppointmentRecord;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentTableModel extends AbstractTableModel {

    private final List<AppointmentRecord> list;
    private final String[] columns = { "ID", "Patient ID", "Clinician ID", "Facility ID", "Date", "Time", "Duration", "Type", "Status", "Reason", "Notes" };
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

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
            case 3 -> a.getFacilityId();
            case 4 -> a.getAppointmentDate().format(dateFormatter);
            case 5 -> a.getAppointmentTime().format(timeFormatter);
            case 6 -> a.getDurationMinutes();
            case 7 -> a.getAppointmentType();
            case 8 -> a.getStatus();
            case 9 -> a.getReasonForVisit();
            case 10 -> a.getNotes();
            default -> null;
        };
    }
}
