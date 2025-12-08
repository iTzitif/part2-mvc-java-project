package com.university.referral.view;

import com.university.referral.controller.ReferralRequestController;
import com.university.referral.model.ReferralRequest;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class ReferralPanel extends JPanel {

    private final JTable table;
    private final ReferralRequestController controller;
    private ReferralTableModel tableModel;
    private final List<ReferralRequest> originalList;
    private final JTextField searchField;
    private final JFrame parent;

    public ReferralPanel(JFrame parentFrame, ReferralRequestController controller, List<ReferralRequest> referrals) {
        this.parent = parentFrame;
        this.controller = controller;
        this.originalList = referrals;

        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        top.add(new JLabel("Search (patient/referral id):")); top.add(searchField);
        add(top, BorderLayout.NORTH);

        tableModel = new ReferralTableModel(referrals);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton add = new JButton("Add"), edit = new JButton("Edit"), del = new JButton("Delete");
        buttons.add(add); buttons.add(edit); buttons.add(del);
        add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(parent, "Enter: ReferralID,PatientID,ReferringClinicianID,FacilityID,Date,Urgency,Summary");
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 7) {
                    ReferralRequest r = new ReferralRequest(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                            p[4].trim(), p[5].trim(), p[6].trim());
                    controller.addReferral(r);
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select row"); return; }
            ReferralRequest ex = originalList.get(r);
            String pre = String.join(",", ex.getReferralId(), ex.getPatientId(), ex.getReferringClinicianId(),
                    ex.getReferredFacilityId(), ex.getDate(), ex.getUrgency(), ex.getClinicalSummary());
            String input = JOptionPane.showInputDialog(parent, "Edit referral:", pre);
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 7) {
                    controller.updateReferral(r, new ReferralRequest(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                            p[4].trim(), p[5].trim(), p[6].trim()));
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select"); return; }
            if (JOptionPane.showConfirmDialog(parent, "Confirm delete?") == JOptionPane.YES_OPTION) {
                controller.deleteReferral(r); refresh();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
        });
    }

    private void filter() {
        String kw = searchField.getText().toLowerCase();
        var filtered = originalList.stream()
                .filter(r -> r.getPatientId().toLowerCase().contains(kw) || r.getReferralId().toLowerCase().contains(kw))
                .toList();
        tableModel = new ReferralTableModel(filtered);
        table.setModel(tableModel);
    }

    private void refresh() {
        tableModel = new ReferralTableModel(originalList);
        table.setModel(tableModel);
    }
}
