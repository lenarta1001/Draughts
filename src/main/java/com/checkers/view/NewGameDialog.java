package com.checkers.view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class NewGameDialog extends JDialog {
    private JComboBox<String> colorCombo;
    private JComboBox<String> opponentCombo;
    private JCheckBox importCheck;
    private JTextField filePathField;
    
    private boolean confirmed = false;
    private File importedFile;

    public NewGameDialog(Frame owner) {
        super(owner, "New Game Settings", true);
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JLabel colorLabel = new JLabel("Your Colour:");
        String[] colors = {"Black (Starts)", "White"};
        colorCombo = new JComboBox<>(colors);

        JLabel opponentLabel = new JLabel("Opponent:");
        String[] opponents = {"Player vs Player", "Player vs Computer"};
        opponentCombo = new JComboBox<>(opponents);
        
        importCheck = new JCheckBox("Import game from file");
        
        filePathField = new JTextField(20);
        filePathField.setEditable(false);
        filePathField.setEnabled(false);

        JButton browseBtn = new JButton("Browse...");
        browseBtn.setEnabled(false);
        browseBtn.addActionListener(e -> chooseFile());

  
        importCheck.addActionListener(e -> {
            boolean isImport = importCheck.isSelected();
            colorCombo.setEnabled(!isImport);
            opponentCombo.setEnabled(!isImport);
            
            filePathField.setEnabled(isImport);
            browseBtn.setEnabled(isImport);
        });

        JButton startBtn = new JButton("Start Game");
        startBtn.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(colorLabel)
                    .addComponent(opponentLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(colorCombo)
                    .addComponent(opponentCombo)))
            .addComponent(importCheck)
            .addGroup(layout.createSequentialGroup()
                .addComponent(filePathField)
                .addComponent(browseBtn))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(startBtn)
                .addComponent(cancelBtn))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(colorLabel)
                .addComponent(colorCombo))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(opponentLabel)
                .addComponent(opponentCombo))
            .addGap(10)
            .addComponent(importCheck)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(filePathField)
                .addComponent(browseBtn))
            .addGap(10)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(startBtn)
                .addComponent(cancelBtn))
        );
        add(panel);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Game File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            importedFile = fileChooser.getSelectedFile();
            filePathField.setText(importedFile.getAbsolutePath());
        }
    }

    private boolean validateInput() {
        if (importCheck.isSelected()) {
            if (importedFile == null || !importedFile.exists()) {
                JOptionPane.showMessageDialog(this, "Please select a valid file to import.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean isConfirmed() { return confirmed; }
    public boolean isBlack() { return colorCombo.getSelectedIndex() == 0; }
    public boolean isVsComputer() { return opponentCombo.getSelectedIndex() == 1; }
    public boolean isImport() { return importCheck.isSelected(); }
    public File getSelectedFile() { return importedFile; }
}