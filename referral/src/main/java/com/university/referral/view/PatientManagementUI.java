package com.university.referral.view;

import com.university.referral.controller.PatientController;
import com.university.referral.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientManagementUI extends JFrame {
    private PatientController patientController;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, addButton, refreshButton, updateButton, deleteButton;
    private String loggedInPatientID;
    private String userRole;

    public PatientManagementUI(String userRole) {
        this.userRole=userRole;
        patientController = new PatientController();
        setTitle("Patient Management");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        initComponents();
        loadPatients();
    }

    public PatientManagementUI(String userRole,String patientID ) {
        this.loggedInPatientID = patientID;
        this.userRole=userRole;
        patientController = new PatientController();
        setTitle(loggedInPatientID == null ? "Patient Management" : "My Profile");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        initComponents();
        loadPatients();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(41, 128, 185));

        JLabel titleLabel = new JLabel("Patient Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Top buttons
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Add New Patient");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddPatientDialog());
        topButtonPanel.add(addButton);

        updateButton = new JButton("Update Selected");
        updateButton.setBackground(new Color(241, 196, 15));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> showUpdatePatientDialog());
        topButtonPanel.add(updateButton);

        deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteSelectedPatient());
        topButtonPanel.add(deleteButton);

        // Center panel with table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topButtonPanel, BorderLayout.NORTH);

        String[] columns = {
                "Patient ID", "First Name", "Last Name", "DOB", "NHS Number",
                "Gender", "Phone", "Email", "Address", "Postcode",
                "Emergency Contact Name", "Emergency Contact Phone",
                "Registration Date", "GP Surgery ID"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchPatients());
        searchPanel.add(searchButton);

        refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> loadPatients());
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.SOUTH);

        if (loggedInPatientID != null) {
            addButton.setVisible(false);
            deleteButton.setVisible(false);
            searchButton.setVisible(false);
            searchField.setVisible(false);
            refreshButton.setVisible(false);
            updateButton.setText("Update My Profile");
        }
        if ("Receptionist".equalsIgnoreCase(userRole)){
            titleLabel = new JLabel("View Patient Basic Details");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);
            add(headerPanel, BorderLayout.NORTH);
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        if ("General Practitioner".equalsIgnoreCase(userRole)){
            titleLabel = new JLabel("View Patient List");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);
            add(headerPanel, BorderLayout.NORTH);
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }


    }

    private void loadPatients() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (loggedInPatientID == null) {
            List<Patient> patients = patientController.getAllPatients();
            for (Patient p : patients) {
                Object[] row = {
                        p.getPatientId(), p.getFirstName(), p.getLastName(),
                        sdf.format(p.getDateOfBirth()), p.getNhsNumber(), p.getGender(),
                        p.getPhoneNumber(), p.getEmail(), p.getAddress(), p.getPostcode(),
                        p.getEmergencyContactName(), p.getEmergencyContactPhone(),
                        sdf.format(p.getRegistrationDate()), p.getGpSurgeryId()
                };
                tableModel.addRow(row);
            }
            if (patients.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No patients found. Add a new patient to get started.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        // Patient self-profile
        Patient p = patientController.findPatient(loggedInPatientID);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Patient not found!");
            return;
        }

        Object[] row = {
                p.getPatientId(), p.getFirstName(), p.getLastName(),
                sdf.format(p.getDateOfBirth()), p.getNhsNumber(), p.getGender(),
                p.getPhoneNumber(), p.getEmail(), p.getAddress(), p.getPostcode(),
                p.getEmergencyContactName(), p.getEmergencyContactPhone(),
                sdf.format(p.getRegistrationDate()), p.getGpSurgeryId()
        };
        tableModel.addRow(row);
    }

    private void searchPatients() {
        String term = searchField.getText().trim();
        if (term.isEmpty()) { loadPatients(); return; }

        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Patient> patients = patientController.searchPatients(term);
        for (Patient p : patients) {
            Object[] row = {
                    p.getPatientId(), p.getFirstName(), p.getLastName(),
                    sdf.format(p.getDateOfBirth()), p.getNhsNumber(), p.getGender(),
                    p.getPhoneNumber(), p.getEmail(), p.getAddress(), p.getPostcode(),
                    p.getEmergencyContactName(), p.getEmergencyContactPhone(),
                    sdf.format(p.getRegistrationDate()), p.getGpSurgeryId()
            };
            tableModel.addRow(row);
        }
        if (patients.isEmpty())
            JOptionPane.showMessageDialog(this, "No patients found matching: " + term);
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog(this, "Add New Patient", true);
        dialog.setSize(500, 800);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {
                "First Name:*", "Last Name:*", "DOB (yyyy-mm-dd):*", "NHS Number:*", "Gender:*",
                "Phone:*", "Email", "Address", "Postcode",
                "Emergency Contact Name", "Emergency Contact Phone", "Registration Date:*", "GP Surgery ID"
        };
        JTextField[] fields = new JTextField[13];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i=0;i<labels.length;i++){
            gbc.gridx=0; gbc.gridy=i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx=1;
            fields[i]=new JTextField(20);
            if(i==2 || i==11) fields[i].setText(sdf.format(new Date()));
            panel.add(fields[i], gbc);
        }

        gbc.gridx=0; gbc.gridy=13; gbc.gridwidth=2;
        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        panel.add(noteLabel, gbc);

        gbc.gridy=14;
        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(46,204,113));
        saveBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e->{
            try{
                String firstName = fields[0].getText().trim();
                String lastName = fields[1].getText().trim();
                Date dob = sdf.parse(fields[2].getText().trim());
                String nhsNumber = fields[3].getText().trim();
                String gender = fields[4].getText().trim();
                String phone = fields[5].getText().trim();
                String email = fields[6].getText().trim();
                String address = fields[7].getText().trim();
                String postcode = fields[8].getText().trim();
                String emergencyName = fields[9].getText().trim();
                String emergencyPhone = fields[10].getText().trim();
                Date regDate = sdf.parse(fields[11].getText().trim());
                String gpId = fields[12].getText().trim();

                if(firstName.isEmpty()||lastName.isEmpty()||gender.isEmpty()||phone.isEmpty()){
                    JOptionPane.showMessageDialog(dialog,"First Name, Last Name, Gender, and Phone are required!");
                    return;
                }

                boolean success = patientController.registerNewPatient(
                        firstName,lastName,dob,nhsNumber,gender,phone,email,address,postcode,
                        emergencyName,emergencyPhone,regDate,gpId
                );
                if(success){
                    JOptionPane.showMessageDialog(dialog,"Patient registered successfully!");
                    dialog.dispose(); loadPatients();
                } else {
                    JOptionPane.showMessageDialog(dialog,"Failed to register patient!");
                }

            }catch(Exception ex){
                JOptionPane.showMessageDialog(dialog,"Error: "+ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e->dialog.dispose());
        buttonPanel.add(saveBtn); buttonPanel.add(cancelBtn);
        panel.add(buttonPanel, gbc);
        dialog.add(panel); dialog.setVisible(true);
    }

    private void showUpdatePatientDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if(selectedRow==-1){ JOptionPane.showMessageDialog(this,"Select a patient to update"); return; }

        String patientId = (String) tableModel.getValueAt(selectedRow,0);
        Patient p = patientController.findPatient(patientId);
        if(p==null){ JOptionPane.showMessageDialog(this,"Patient not found!"); return; }

        JDialog dialog = new JDialog(this,"Update Patient",true);
        dialog.setSize(500,800); dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        gbc.gridx=0; gbc.gridy=0;
        panel.add(new JLabel("Patient ID:"),gbc);
        gbc.gridx=1;
        JLabel idLabel = new JLabel(p.getPatientId());
        panel.add(idLabel,gbc);

        String[] labels = {
                "First Name:*", "Last Name:*", "DOB (yyyy-mm-dd):*", "NHS Number:*", "Gender:*",
                "Phone:*", "Email", "Address", "Postcode",
                "Emergency Contact Name", "Emergency Contact Phone", "Registration Date:*", "GP Surgery ID"
        };
        JTextField[] fields = new JTextField[13];
        String[] values = {
                p.getFirstName(),p.getLastName(),sdf.format(p.getDateOfBirth()),p.getNhsNumber(),p.getGender(),
                p.getPhoneNumber(),p.getEmail(),p.getAddress(),p.getPostcode(),
                p.getEmergencyContactName(),p.getEmergencyContactPhone(),sdf.format(p.getRegistrationDate()),p.getGpSurgeryId()
        };
        for(int i=0;i<labels.length;i++){
            gbc.gridx=0; gbc.gridy=i+1;
            panel.add(new JLabel(labels[i]),gbc);
            gbc.gridx=1;
            fields[i]=new JTextField(20); fields[i].setText(values[i]);
            panel.add(fields[i],gbc);
        }

        gbc.gridx=0; gbc.gridy=14; gbc.gridwidth=2;
        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("Update");
        updateBtn.setBackground(new Color(241,196,15)); updateBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("Cancel");

        updateBtn.addActionListener(e->{
            try{
                String firstName = fields[0].getText().trim();
                String lastName = fields[1].getText().trim();
                Date dob = sdf.parse(fields[2].getText().trim());
                String nhsNumber = fields[3].getText().trim();
                String gender = fields[4].getText().trim();
                String phone = fields[5].getText().trim();
                String email = fields[6].getText().trim();
                String address = fields[7].getText().trim();
                String postcode = fields[8].getText().trim();
                String emergencyName = fields[9].getText().trim();
                String emergencyPhone = fields[10].getText().trim();
                Date regDate = sdf.parse(fields[11].getText().trim());
                String gpId = fields[12].getText().trim();

                if(firstName.isEmpty()||lastName.isEmpty()||gender.isEmpty()||phone.isEmpty()){
                    JOptionPane.showMessageDialog(dialog,"First Name, Last Name, Gender, and Phone are required!");
                    return;
                }

                boolean success = patientController.updatePatient(
                        patientId,firstName,lastName,dob,nhsNumber,gender,phone,email,address,postcode,
                        emergencyName,emergencyPhone,regDate,gpId
                );
                if(success){
                    JOptionPane.showMessageDialog(dialog,"Patient updated successfully!");
                    dialog.dispose(); loadPatients();
                } else {
                    JOptionPane.showMessageDialog(dialog,"Failed to update patient!");
                }

            }catch(Exception ex){
                JOptionPane.showMessageDialog(dialog,"Error: "+ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e->dialog.dispose());
        buttonPanel.add(updateBtn); buttonPanel.add(cancelBtn);
        panel.add(buttonPanel,gbc);
        dialog.add(panel); dialog.setVisible(true);
    }

    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if(selectedRow==-1){ JOptionPane.showMessageDialog(this,"Select a patient to delete"); return; }

        String patientId = (String) tableModel.getValueAt(selectedRow,0);
        String patientName = (String) tableModel.getValueAt(selectedRow,1)+" "+(String)tableModel.getValueAt(selectedRow,2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this patient?\nPatient ID: "+patientId+"\nName: "+patientName,
                "Confirm Deletion",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

        if(confirm==JOptionPane.YES_OPTION){
            boolean success = patientController.deletePatient(patientId);
            if(success){
                JOptionPane.showMessageDialog(this,"Patient deleted successfully!");
                loadPatients();
            } else {
                JOptionPane.showMessageDialog(this,"Failed to delete patient!");
            }
        }
    }
}
