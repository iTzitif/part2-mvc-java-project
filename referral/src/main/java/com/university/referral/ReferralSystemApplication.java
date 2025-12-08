package com.university.referral;

import com.university.referral.view.MainDashboardFrame;

import javax.swing.*;

public class ReferralSystemApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainDashboardFrame main = new MainDashboardFrame();
            main.setVisible(true);
        });
    }
}
