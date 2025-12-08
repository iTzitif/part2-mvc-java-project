package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.PrescriptionEntry;
import com.university.referral.util.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionController {

    private final ApplicationDataStore dataStore;

    public PrescriptionController(ApplicationDataStore dataStore) { this.dataStore = dataStore; }

    public List<PrescriptionEntry> getAll() { return dataStore.getPrescriptions(); }

    public void addPrescription(PrescriptionEntry p) {
        dataStore.getPrescriptions().add(p);
        saveAll();
    }

    public void updatePrescription(int idx, PrescriptionEntry p) {
        dataStore.getPrescriptions().set(idx, p);
        saveAll();
    }

    public void deletePrescription(int idx) {
        dataStore.getPrescriptions().remove(idx);
        saveAll();
    }

    private void saveAll() {
        List<String[]> rows = new ArrayList<>();
        for (PrescriptionEntry p : dataStore.getPrescriptions()) {
            rows.add(new String[] {
                    p.getPrescriptionId(), p.getAppointmentId(), p.getPatientId(),
                    p.getClinicianId(), p.getMedicationDetails(), p.getDosage(), p.getInstructions()
            });
        }
        CsvFileWriter.writeCsv(ApplicationDataStore.PRESCRIPTION_CSV, rows);
    }
}
