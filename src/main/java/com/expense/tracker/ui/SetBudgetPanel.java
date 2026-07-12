package com.expense.tracker.ui;

import javax.swing.*;
import java.awt.*;

public class SetBudgetPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField limitField;
    private JComboBox<String> categoryBox;

    public SetBudgetPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(60, 120, 60)); // Greenish for budget
        JButton backButton = new JButton("<< Cancel");
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> mainFrame.showCard("DASHBOARD"));
        JLabel title = new JLabel("Set Budget Limit");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(backButton);
        header.add(Box.createHorizontalStrut(20));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Category
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        String[] categories = { "Food", "Rent", "Utilities", "Entertainment", "Transport", "Health", "Other" };
        categoryBox = new JComboBox<>(categories);
        categoryBox.setEditable(true);
        formPanel.add(categoryBox, gbc);

        // Limit
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Monthly Limit ($):"), gbc);
        gbc.gridx = 1;
        limitField = new JTextField(15);
        formPanel.add(limitField, gbc);

        // Save Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton saveBtn = new JButton("Set Budget");
        saveBtn.setBackground(new Color(60, 120, 60));
        saveBtn.setForeground(Color.BLACK);
        saveBtn.addActionListener(e -> saveBudget());
        formPanel.add(saveBtn, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void saveBudget() {
        try {
            double limit = Double.parseDouble(limitField.getText());
            String category = (String) categoryBox.getSelectedItem();

            mainFrame.getService().setBudget(category, limit);
            JOptionPane.showMessageDialog(this, "Budget set for " + category);

            limitField.setText("");
            mainFrame.showCard("DASHBOARD");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Limit. Please enter a number.");
        }
    }
}
