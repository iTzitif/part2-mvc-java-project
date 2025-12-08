package com.university.referral.view;

import com.university.referral.controller.StaffController;
import com.university.referral.model.StaffMember;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {

    private final JTable table;
    private StaffTableModel tableModel;
    private final StaffController controller;
    private final List<StaffMember> originalList;
    private final JTextField searchField;
    private final JFrame parent;

    public StaffPanel(JFrame parentFrame, StaffController controller, List<StaffMember> staff) {
        this.parent = parentFrame;
        this.controller = controller;
        this.originalList = staff;

        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        top.add(new JLabel("Search (name/id):")); top.add(searchField);
        add(top, BorderLayout.NORTH);

        tableModel = new StaffTableModel(staff);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton add = new JButton("Add"), edit = new JButton("Edit"), del = new JButton("Delete");
        buttons.add(add); buttons.add(edit); buttons.add(del);
        add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(parent, "Enter: ID,FullName,Role,Contact");
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 4) {
                    controller.addStaff(new StaffMember(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()));
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select a row"); return; }
            StaffMember ex = originalList.get(r);
            String pre = String.join(",", ex.getStaffId(), ex.getFullName(), ex.getRole(), ex.getContactNumber());
            String input = JOptionPane.showInputDialog(parent, "Edit staff:", pre);
            if (input != null) {
                String[] p = input.split(",", -1);
                if (p.length >= 4) {
                    controller.updateStaff(r, new StaffMember(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()));
                    refresh();
                } else JOptionPane.showMessageDialog(parent, "Invalid input");
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(parent, "Select"); return; }
            if (JOptionPane.showConfirmDialog(parent, "Confirm delete?") == JOptionPane.YES_OPTION) {
                controller.deleteStaff(r); refresh();
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
                .filter(s -> s.getFullName().toLowerCase().contains(kw) || s.getStaffId().toLowerCase().contains(kw))
                .toList();
        tableModel = new StaffTableModel(filtered);
        table.setModel(tableModel);
    }

    private void refresh() {
        tableModel = new StaffTableModel(originalList);
        table.setModel(tableModel);
    }
}
