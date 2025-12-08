package com.university.referral.view;

import com.university.referral.controller.FacilityController;
import com.university.referral.model.MedicalFacility;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class FacilityPanel extends JPanel {

    private final JTable table;
    private FacilityTableModel tableModel;
    private final FacilityController controller;
    private final List<MedicalFacility> originalList;
    private final JTextField searchField;
    private final JFrame parent;

    public FacilityPanel(JFrame parentFrame, FacilityController controller, List<MedicalFacility> facilities) {
        this.parent = parentFrame;
        this.controller = controller;
        this.originalList = facilities;

        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        top.add(new JLabel("Search (name/id):")); top.add(searchField);
        add(top, BorderLayout.NORTH);

        tableModel = new FacilityTableModel(facilities);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton add = new JButton("Add"), edit = new JButton("Edit"), del = new JButton("Delete");
        buttons.add(add); buttons.add(edit); buttons.add(del);
        add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(parent, "Enter: ID,Name,Address");
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 3) {
                    controller.addFacility(new MedicalFacility(p[0].trim(), p[1].trim(), p[2].trim()));
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select"); return; }
            MedicalFacility ex = originalList.get(r);
            String pre = String.join(",", ex.getFacilityId(), ex.getName(), ex.getAddress());
            String input = JOptionPane.showInputDialog(parent, "Edit facility:", pre);
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 3) {
                    controller.updateFacility(r, new MedicalFacility(p[0].trim(), p[1].trim(), p[2].trim()));
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select"); return; }
            if (JOptionPane.showConfirmDialog(parent, "Confirm delete?") == JOptionPane.YES_OPTION) {
                controller.deleteFacility(r); refresh();
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
                .filter(f -> f.getName().toLowerCase().contains(kw) || f.getFacilityId().toLowerCase().contains(kw))
                .toList();
        tableModel = new FacilityTableModel(filtered);
        table.setModel(tableModel);
    }

    private void refresh() {
        tableModel = new FacilityTableModel(originalList);
        table.setModel(tableModel);
    }
}
