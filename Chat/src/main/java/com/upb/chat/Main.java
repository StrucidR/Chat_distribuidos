package com.upb.chat;

import javax.swing.SwingUtilities;

import com.upb.chat.gui.LoginFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                LoginFrame::new
        );
    }
}