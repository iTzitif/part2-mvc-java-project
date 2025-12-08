package com.university.referral.service;

import com.university.referral.model.ReferralRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReferralRequestService {

    private static ReferralRequestService instance;

    private ReferralRequestService() { }

    public static ReferralRequestService getInstance() {
        if (instance == null) instance = new ReferralRequestService();
        return instance;
    }

    public synchronized void createReferralText(ReferralRequest r) {
        try {
            Path out = Path.of("output");
            Files.createDirectories(out);
            Path file = out.resolve("referrals_out.txt");
            try (BufferedWriter bw = Files.newBufferedWriter(file, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
                bw.write("ReferralID: " + r.getReferralId()); bw.newLine();
                bw.write("Date: " + r.getDate()); bw.newLine();
                bw.write("PatientID: " + r.getPatientId()); bw.newLine();
                bw.write("ReferringClinicianID: " + r.getReferringClinicianId()); bw.newLine();
                bw.write("ReferredFacilityID: " + r.getReferredFacilityId()); bw.newLine();
                bw.write("Urgency: " + r.getUrgency()); bw.newLine();
                bw.write("Clinical Summary:"); bw.newLine();
                bw.write(r.getClinicalSummary()); bw.newLine();
                bw.write("--------------------------------------------------"); bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
