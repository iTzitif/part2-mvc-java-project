package com.university.referral;

import com.university.referral.model.ApplicationDataStore;
import com.university.referral.view.MainDashboardFrame;

public class ReferralSystemApplication {
    public static void main(String[] args) {

        ApplicationDataStore.getInstance();

        MainDashboardFrame dashboard = new MainDashboardFrame();
        dashboard.setVisible(true);

    }
}
