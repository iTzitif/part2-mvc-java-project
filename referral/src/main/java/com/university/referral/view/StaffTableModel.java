package com.university.referral.view;

import com.university.referral.model.StaffMember;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StaffTableModel extends AbstractTableModel {

    private final List<StaffMember> list;
    private final String[] cols = { "Staff ID", "Full Name", "Role", "Contact" };

    public StaffTableModel(List<StaffMember> list) { this.list = list; }
    @Override public int getRowCount() { return list.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        StaffMember s = list.get(row);
        return switch (col) {
            case 0 -> s.getStaffId();
            case 1 -> s.getFullName();
            case 2 -> s.getRole();
            case 3 -> s.getContactNumber();
            default -> null;
        };
    }
}
