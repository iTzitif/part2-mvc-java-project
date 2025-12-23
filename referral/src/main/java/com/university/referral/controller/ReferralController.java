package com.university.referral.controller;

import com.university.referral.model.Referral;
import com.university.referral.service.ReferralService;
import java.time.LocalDate;
import java.util.List;

public class ReferralController {
    private ReferralService referralService;

    public ReferralController() {
        referralService = new ReferralService();
    }

    public boolean createReferral(String patientId,
                                  String referringClinicianId,
                                  String referredToClinicianId,
                                  String referringFacilityId,
                                  String referredToFacilityId,
                                  LocalDate referralDate,
                                  String urgencyLevel,
                                  String referralReason,
                                  String clinicalSummary,
                                  String requestedInvestigations,
                                  String status,
                                  String appointmentId,
                                  String notes) {
        String referralId = referralService.generateReferralID();

        Referral referral = new Referral(
                referralId,
                patientId,
                referringClinicianId,
                referredToClinicianId,
                referringFacilityId,
                referredToFacilityId,
                referralDate,
                urgencyLevel,
                referralReason,
                clinicalSummary,
                requestedInvestigations,
                status,
                appointmentId,
                notes
        );

        return referralService.createReferral(referral);
    }

    public List<Referral> getAllReferrals() {
        return referralService.getAllReferrals();
    }

    public List<Referral> getPatientReferrals(String patientId) {
        return referralService.getReferralsByPatient(patientId);
    }
    public List<Referral> getClinicianReferrals(String clinicianId) {
        return referralService.getReferralsByClinician(clinicianId);
    }
    public List<Referral> getClinicianReceiveReferral(String referredToClinicianId) {
        return referralService.getReferralsByReferredToClinicianId(referredToClinicianId);
    }
    public boolean updateReferralNotes(String referralId, String notes) {
        return referralService.updateReferralNotes(referralId, notes);
    }
}


