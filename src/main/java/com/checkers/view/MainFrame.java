package com.checkers.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class MainFrame extends JFrame {
    
    public MainFrame(BoardPanel boardPanel) {
        super("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(boardPanel, BorderLayout.CENTER);
        setJMenuBar(createJMenuBar());
        setPreferredSize(new Dimension(500, 500));
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem newGameItem = new JMenuItem("New Game");
        fileMenu.add(newGameItem);
        fileMenu.addSeparator();

        JMenuItem importItem = new JMenuItem("Import");
        fileMenu.add(importItem);
        fileMenu.addSeparator();

        JMenuItem exportItem = new JMenuItem("Export");
        fileMenu.add(exportItem);
        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(e -> { System.exit(0); });
        fileMenu.addSeparator();

        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Draughts v1.0. For the prevailing roles look at the specification."));
        helpMenu.add(aboutItem);
        
        menuBar.add(helpMenu);

        return menuBar;
    }
}
