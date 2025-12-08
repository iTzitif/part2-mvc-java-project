package com.university.referral.view;

import com.university.referral.model.PrescriptionEntry;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PrescriptionTableModel extends AbstractTableModel {

    private final List<PrescriptionEntry> list;
    private final String[] cols = { "Prescription ID", "Appointment ID", "Patient ID", "Clinician ID", "Medication", "Dosage", "Instructions" };

    public PrescriptionTableModel(List<PrescriptionEntry> list) { this.list = list; }
    @Override public int getRowCount() { return list.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        PrescriptionEntry p = list.get(row);
        return switch (col) {
            case 0 -> p.getPrescriptionId();
            case 1 -> p.getAppointmentId();
            case 2 -> p.getPatientId();
            case 3 -> p.getClinicianId();
            case 4 -> p.getMedicationDetails();
            case 5 -> p.getDosage();
            case 6 -> p.getInstructions();
            default -> null;
        };
    }
}
