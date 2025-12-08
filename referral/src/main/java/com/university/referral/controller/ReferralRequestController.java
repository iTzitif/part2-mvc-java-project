package com.university.referral.controller;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.model.ReferralRequest;
import com.university.referral.service.ReferralRequestService;
import com.university.referral.util.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class ReferralRequestController {

    private final ApplicationDataStore dataStore;

    public ReferralRequestController(ApplicationDataStore dataStore) { this.dataStore = dataStore; }

    public List<ReferralRequest> getAll() { return dataStore.getReferrals(); }

    public void addReferral(ReferralRequest r) {
        dataStore.getReferrals().add(r);
        ReferralRequestService.getInstance().createReferralText(r);
        saveAll();
    }

    public void updateReferral(int idx, ReferralRequest r) {
        dataStore.getReferrals().set(idx, r);
        saveAll();
    }

    public void deleteReferral(int idx) {
        dataStore.getReferrals().remove(idx);
        saveAll();
    }

    private void saveAll() {
        List<String[]> rows = new ArrayList<>();
        for (ReferralRequest r : dataStore.getReferrals()) {
            rows.add(new String[] {
                    r.getReferralId(), r.getPatientId(), r.getReferringClinicianId(),
                    r.getReferredFacilityId(), r.getDate(), r.getUrgency(), r.getClinicalSummary()
            });
        }
        CsvFileWriter.writeCsv(ApplicationDataStore.REFERRAL_CSV, rows);
    }
}
