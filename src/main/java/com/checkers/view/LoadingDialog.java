package com.checkers.view;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog {
    
    public LoadingDialog(Frame owner) {
        super(owner, "Processing", true);
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JProgressBar progressBar = new JProgressBar();

        progressBar.setIndeterminate(true); 
        progressBar.setString("Reading file...");
        progressBar.setStringPainted(true);
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("Importing game data, please wait...", JLabel.CENTER);
        panel.add(messageLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        
        add(panel);
        pack();
        setLocationRelativeTo(owner);
    }
}