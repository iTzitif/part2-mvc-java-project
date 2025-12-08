package com.university.referral.view;

import com.university.referral.model.ReferralRequest;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ReferralTableModel extends AbstractTableModel {

    private final List<ReferralRequest> list;
    private final String[] cols = { "Referral ID", "Patient ID", "Referring Clinician", "Facility ID", "Date", "Urgency", "Summary" };

    public ReferralTableModel(List<ReferralRequest> list) { this.list = list; }
    @Override public int getRowCount() { return list.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        ReferralRequest r = list.get(row);
        return switch (col) {
            case 0 -> r.getReferralId();
            case 1 -> r.getPatientId();
            case 2 -> r.getReferringClinicianId();
            case 3 -> r.getReferredFacilityId();
            case 4 -> r.getDate();
            case 5 -> r.getUrgency();
            case 6 -> r.getClinicalSummary();
            default -> null;
        };
    }
}
