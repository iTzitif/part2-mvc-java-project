package com.university.referral.service;

import com.university.referral.model.Referral;
import com.university.referral.util.SingletonReferralManager;
import com.university.referral.util.CSVDataStore;
import com.university.referral.util.NotificationGenerator;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReferralService {

    private static final String REFERRAL_FILE = "data/referrals.csv";

    private static final String[] HEADERS = {
            "ReferralID","PatientID","ReferringClinicianID","ReferredToClinicianID",
            "ReferringFacilityID","ReferredToFacilityID","ReferralDate",
            "UrgencyLevel","ReferralReason","ClinicalSummary",
            "RequestedInvestigations","Status","AppointmentID",
            "Notes","CreatedDate","LastUpdated"
    };

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private SingletonReferralManager referralManager;
    private NotificationGenerator notificationGenerator;
    private CSVDataStore dataStore;

    public ReferralService() {
        referralManager = SingletonReferralManager.getInstance();
        notificationGenerator = NotificationGenerator.getInstance();
        dataStore = CSVDataStore.getInstance();

        File file = new File(REFERRAL_FILE);
        if (!file.exists()) {
            dataStore.createFileWithHeaders(REFERRAL_FILE, HEADERS);
        }

        loadReferralsFromFile();
    }

    private void loadReferralsFromFile() {
        List<String[]> rows = dataStore.loadData(REFERRAL_FILE);

        for (String[] v : rows) {
            if (v.length >= 16) {
                Referral referral = new Referral(
                        v[0],
                        v[1],
                        v[2],
                        v[3],
                        v[4],
                        v[5],
                        LocalDate.parse(v[6], DATE_FORMAT),
                        v[7],
                        v[8],
                        v[9],
                        v[10],
                        v[11],
                        v[12],
                        v[13]
                );
                referralManager.addReferral(referral);
            }
        }
    }

    public boolean createReferral(Referral referral) {
        referralManager.addReferral(referral);
        notificationGenerator.saveReferralToFile(referral);
        return dataStore.appendData(REFERRAL_FILE, toCSVArray(referral));
    }

    public List<Referral> getAllReferrals() {
        return referralManager.getAllReferrals();
    }

    public List<Referral> getReferralsByPatient(String patientId) {
        return referralManager.getReferralsByPatient(patientId);
    }

    public List<Referral> getReferralsByClinician(String clinicianId) {
        return referralManager.getReferralsByClinician(clinicianId);
    }

    public List<Referral> getReferralsByReferredToClinicianId(String id) {
        return referralManager.getReferralsByReferredToClinicianId(id);
    }

    public boolean updateReferralNotes(String referralId, String notes) {
        Referral referral = referralManager.getReferralByID(referralId);
        if (referral == null) return false;

        referral.setNotes(notes);
        referral.setStatus("Updated");
        return rewriteFile();
    }

    private boolean rewriteFile() {
        if (!dataStore.createFileWithHeaders(REFERRAL_FILE, HEADERS)) {
            return false;
        }

        for (Referral r : referralManager.getAllReferrals()) {
            if (!dataStore.appendData(REFERRAL_FILE, toCSVArray(r))) {
                return false;
            }
        }
        return true;
    }

    private String[] toCSVArray(Referral r) {
        return new String[] {
                r.getReferral_id(),
                r.getPatient_id(),
                r.getReferring_clinician_id(),
                r.getReferred_to_clinician_id(),
                r.getReferring_facility_id(),
                r.getReferred_to_facility_id(),
                r.getReferral_date().toString(),
                r.getUrgency_level(),
                r.getReferral_reason(),
                r.getClinical_summary(),
                r.getRequested_investigations(),
                r.getStatus(),
                r.getAppointment_id() != null ? r.getAppointment_id() : "",
                r.getNotes(),
                r.getCreated_date().toString(),
                r.getLast_updated().toString()
        };
    }
    public String generateReferralID() {
        return "REF" + System.currentTimeMillis();
    }
}
