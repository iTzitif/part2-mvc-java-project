package com.university.referral;

import com.university.referral.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
            System.out.println("Test");
        });
    }
}
