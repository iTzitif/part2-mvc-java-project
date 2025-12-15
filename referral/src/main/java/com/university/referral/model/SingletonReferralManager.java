package com.university.referral.model;

import java.util.*;

public class SingletonReferralManager {

    private static SingletonReferralManager instance;

    private Queue<Referral> referralQueue;
    private Map<String, Referral> allReferrals;

    private SingletonReferralManager() {
        referralQueue = new LinkedList<>();
        allReferrals = new HashMap<>();
    }

    public static synchronized SingletonReferralManager getInstance() {
        if (instance == null) {
            instance = new SingletonReferralManager();
        }
        return instance;
    }

    public void addReferral(Referral referral) {
        // Prevent duplicate referrals by ID
        if (!allReferrals.containsKey(referral.getReferral_id())) {
            allReferrals.put(referral.getReferral_id(), referral);
            referralQueue.offer(referral);
        }
    }

    public Referral processNextReferral() {
        return referralQueue.poll();
    }

    public List<Referral> getAllReferrals() {
        return new ArrayList<>(allReferrals.values());
    }

    public List<Referral> getReferralsByPatient(String patientID) {
        List<Referral> result = new ArrayList<>();
        for (Referral referral : allReferrals.values()) {
            if (referral.getPatient_id().equals(patientID)) {
                result.add(referral);
            }
        }
        return result;
    }

    public List<Referral> getReferralsByClinician(String clinicianId) {
        List<Referral> result = new ArrayList<>();
        for (Referral referral : allReferrals.values()) {
            if (referral.getReferring_clinician_id().equals(clinicianId)) {
                result.add(referral);
            }
        }
        return result;
    }

    public List<Referral> getReferralsByReferredToClinicianId(String referredToClinicianId) {
        List<Referral> result = new ArrayList<>();
        for (Referral referral : allReferrals.values()) {
            if (referral.getReferred_to_clinician_id().equals(referredToClinicianId)) {
                result.add(referral);
            }
        }
        return result;
    }

    public Referral getReferralByID(String referralID) {
        return allReferrals.get(referralID); // O(1)
    }

    public int getQueueSize() {
        return referralQueue.size();
    }
}
