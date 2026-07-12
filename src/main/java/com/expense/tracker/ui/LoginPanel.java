package com.expense.tracker.ui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Smart Expense Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel nameLabel = new JLabel("Enter Name:");
        nameField = new JTextField(15);
        JButton loginButton = new JButton("Login");

        loginButton.setForeground(Color.BLACK);

        loginButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                // Update username
                if (mainFrame.getService().getCurrentUser().getUsername().equals("New User") ||
                        !mainFrame.getService().getCurrentUser().getUsername().equals(name)) { // Or always update?
                    mainFrame.getService().getCurrentUser().setUsername(name);
                    // Trigger save? The service saves on transactions. We should probably force a
                    // save or just wait.
                    // Let's rely on next action saving, or maybe adding a simple save method to
                    // Service.
                    // For now, in-memory is fine until transaction.
                }
                mainFrame.showCard("DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter your name.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);
    }
}
