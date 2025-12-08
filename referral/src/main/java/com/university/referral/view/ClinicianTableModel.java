package com.university.referral.view;

import com.university.referral.model.ClinicianProfile;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ClinicianTableModel extends AbstractTableModel {

    private final List<ClinicianProfile> list;
    private final String[] cols = { "ID", "Full Name", "Role", "Contact" };

    public ClinicianTableModel(List<ClinicianProfile> list) { this.list = list; }
    @Override public int getRowCount() { return list.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        ClinicianProfile c = list.get(row);
        return switch (col) {
            case 0 -> c.getClinicianId();
            case 1 -> c.getFullName();
            case 2 -> c.getRole();
            case 3 -> c.getContactNumber();
            default -> null;
        };
    }
}
