package com.university.referral.view;

import com.university.referral.model.MedicalFacility;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FacilityTableModel extends AbstractTableModel {

    private final List<MedicalFacility> list;
    private final String[] cols = { "Facility ID", "Name", "Address" };

    public FacilityTableModel(List<MedicalFacility> list) { this.list = list; }
    @Override public int getRowCount() { return list.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        MedicalFacility f = list.get(row);
        return switch (col) {
            case 0 -> f.getFacilityId();
            case 1 -> f.getName();
            case 2 -> f.getAddress();
            default -> null;
        };
    }
}
