package com.university.referral;

import com.university.referral.view.LoginUI;
import javax.swing.*;

public class ReferralSystemApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}
