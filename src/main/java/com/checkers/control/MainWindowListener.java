package com.checkers.control;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.checkers.view.MainFrame;

public class MainWindowListener extends WindowAdapter {
    private MainFrame mainFrame;

    public MainWindowListener(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        mainFrame.handleExit();
    }
}
