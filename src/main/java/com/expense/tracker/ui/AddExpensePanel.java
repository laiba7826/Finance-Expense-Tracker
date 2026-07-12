package com.expense.tracker.ui;

import javax.swing.*;
import java.awt.*;

public class AddExpensePanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField amountField;
    private JTextField descField;
    private JComboBox<String> categoryBox;

    public AddExpensePanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(200, 60, 60)); // Reddish for expense
        JButton backButton = new JButton("<< Cancel");
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> mainFrame.showCard("DASHBOARD"));
        JLabel title = new JLabel("Add New Expense");
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

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        amountField = new JTextField(15);
        formPanel.add(amountField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        String[] categories = { "Food", "Rent", "Utilities", "Entertainment", "Transport", "Health", "Other" };
        categoryBox = new JComboBox<>(categories);
        categoryBox.setEditable(true);
        formPanel.add(categoryBox, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descField = new JTextField(15);
        formPanel.add(descField, gbc);

        // Save Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton saveBtn = new JButton("Add Expense");
        saveBtn.setBackground(new Color(200, 60, 60));
        saveBtn.setForeground(Color.BLACK);
        saveBtn.addActionListener(e -> saveExpense());
        formPanel.add(saveBtn, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String category = (String) categoryBox.getSelectedItem();
            String desc = descField.getText();

            String warning = mainFrame.getService().addExpense(amount, category, desc);

            if (warning != null) {
                JOptionPane.showMessageDialog(this, warning, "Budget Limit Alert!", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Expense Added Successfully.");
            }

            // Clear and go back
            amountField.setText("");
            descField.setText("");
            mainFrame.showCard("DASHBOARD");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Amount. Please enter a number.");
        }
    }
}
