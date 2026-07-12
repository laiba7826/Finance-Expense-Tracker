package com.expense.tracker.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JLabel savingsLabel;

    public DashboardPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        balanceLabel = new JLabel("Current Balance: $0.00", SwingConstants.CENTER);
        savingsLabel = new JLabel("Total Savings: $0.00", SwingConstants.CENTER);

        headerPanel.add(welcomeLabel);
        headerPanel.add(balanceLabel);
        headerPanel.add(savingsLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center - Actions
        JPanel actionsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JButton salaryBtn = new JButton("Set Salary / Income");
        JButton budgetBtn = new JButton("Set Budget Limit");
        JButton expenseBtn = new JButton("Add Expense");
        JButton savingsBtn = new JButton("Manage Savings / Charity");
        JButton debtBtn = new JButton("Track Debts");
        JButton investBtn = new JButton("Track Investments");
        JButton statsBtn = new JButton("Analytics / Graphs");
        statsBtn.setBackground(new Color(200, 200, 255));

        actionsPanel.add(salaryBtn);
        actionsPanel.add(budgetBtn);
        actionsPanel.add(expenseBtn);
        actionsPanel.add(savingsBtn);
        actionsPanel.add(debtBtn);
        actionsPanel.add(investBtn);
        actionsPanel.add(investBtn);
        actionsPanel.add(statsBtn);

        // Set Text Color to Black
        salaryBtn.setForeground(Color.BLACK);
        budgetBtn.setForeground(Color.BLACK);
        expenseBtn.setForeground(Color.BLACK);
        savingsBtn.setForeground(Color.BLACK);
        debtBtn.setForeground(Color.BLACK);
        investBtn.setForeground(Color.BLACK);
        statsBtn.setForeground(Color.BLACK);

        actionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(actionsPanel, BorderLayout.CENTER);

        // Listeners
        salaryBtn.addActionListener(e -> updateSalary());
        budgetBtn.addActionListener(e -> setBudget());
        expenseBtn.addActionListener(e -> addExpense());
        savingsBtn.addActionListener(e -> manageSavings());
        debtBtn.addActionListener(e -> manageDebts());
        investBtn.addActionListener(e -> manageInvestments());
        statsBtn.addActionListener(e -> showStats());

        // Refresh data whenever shown
        this.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                refreshData();
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });
    }

    private void refreshData() {
        if (mainFrame.getService() != null) {
            com.expense.tracker.model.User user = mainFrame.getService().getCurrentUser();
            welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
            balanceLabel.setText(String.format("Current Balance: $%.2f", user.getCurrentBalance()));
            savingsLabel.setText(String.format("Total Savings: $%.2f", user.getTotalSavings()));
            // Hidden charity pool is not shown, as requested.
        }
    }

    private void updateSalary() {
        String input = JOptionPane.showInputDialog(this, "Enter Monthly Salary:");
        if (input != null && !input.isEmpty()) {
            try {
                double salary = Double.parseDouble(input);
                com.expense.tracker.model.User user = mainFrame.getService().getCurrentUser();
                user.setMonthlySalary(salary);
                // Also add to balance? Usually salary implies income.
                int choice = JOptionPane.showConfirmDialog(this, "Add this salary to current balance?", "Income",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    mainFrame.getService().addIncome(salary, "Salary", "Monthly Salary");
                }
                refreshData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        }
    }

    private void setBudget() {
        mainFrame.showCard("SET_BUDGET");
    }

    private void addExpense() {
        mainFrame.showCard("ADD_EXPENSE");
    }

    private void showStats() {
        mainFrame.showCard("STATS");
    }

    private void manageSavings() {
        com.expense.tracker.model.User user = mainFrame.getService().getCurrentUser();
        String message = String.format("Current Savings: $%.2f", user.getTotalSavings());
        String[] options = { "Add Savings", "Donate to Charity", "Close" };

        int choice = JOptionPane.showOptionDialog(this, message, "Savings & Charity",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) { // Add Savings
            String input = JOptionPane.showInputDialog(this, "Amount to transfer to savings:");
            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);
                    mainFrame.getService().addSavings(amount);
                    refreshData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid amount");
                }
            }
        } else if (choice == 1) { // Donate
            String input = JOptionPane.showInputDialog(this,
                    "Amount to donate (deducted from savings):");
            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);
                    mainFrame.getService().donateToCharity(amount, "Charity Donation");
                    JOptionPane.showMessageDialog(this,
                            "Thank you! Donation successful.\n\nMotivational Message: You are making a difference!");
                    refreshData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            }
        }
    }

    private void manageDebts() {
        // Simple list view
        com.expense.tracker.util.CustomLinkedList<com.expense.tracker.model.Debt> debts = mainFrame.getService()
                .getCurrentUser().getDebts();
        StringBuilder sb = new StringBuilder("Current Debts:\n");
        for (com.expense.tracker.model.Debt d : debts) {
            sb.append(d.toString()).append("\n");
        }

        String[] options = { "Add Debt", "Make Payment", "View Details", "Close" };
        int choice = JOptionPane.showOptionDialog(this, new JScrollPane(new JTextArea(sb.toString(), 10, 30)), "Debts",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == 0) { // Add Debt
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Person/Entity Name:"));
            JTextField nameField = new JTextField();
            panel.add(nameField);
            panel.add(new JLabel("Total Amount:"));
            JTextField amountField = new JTextField();
            panel.add(amountField);

            int res = JOptionPane.showConfirmDialog(this, panel, "Add Debt", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText();
                    double amount = Double.parseDouble(amountField.getText());
                    mainFrame.getService().addDebt(new com.expense.tracker.model.Debt(name, amount));
                    JOptionPane.showMessageDialog(this, "Debt added successfully!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid input");
                }
            }
        } else if (choice == 1) { // Make Payment
            if (debts.size() == 0) {
                JOptionPane.showMessageDialog(this, "No debts to pay.");
                return;
            }

            // Select debt
            String[] debtNames = new String[debts.size()];
            int idx = 0;
            for (com.expense.tracker.model.Debt d : debts) {
                debtNames[idx++] = d.getPersonName() + " ($" + String.format("%.2f", d.getRemainingAmount())
                        + " remaining)";
            }

            String selected = (String) JOptionPane.showInputDialog(this, "Select Debt:",
                    "Make Payment", JOptionPane.QUESTION_MESSAGE, null, debtNames, debtNames[0]);

            if (selected != null) {
                // Extract person name from selection
                String personName = selected.substring(0, selected.indexOf(" ($"));
                com.expense.tracker.model.Debt selectedDebt = null;
                for (com.expense.tracker.model.Debt d : debts) {
                    if (d.getPersonName().equals(personName)) {
                        selectedDebt = d;
                        break;
                    }
                }

                if (selectedDebt != null && selectedDebt.getRemainingAmount() > 0) {
                    String amountStr = JOptionPane.showInputDialog(this,
                            String.format("Make payment to %s\nRemaining: $%.2f\n\nEnter payment amount:",
                                    selectedDebt.getPersonName(), selectedDebt.getRemainingAmount()));

                    if (amountStr != null) {
                        try {
                            double amount = Double.parseDouble(amountStr);
                            if (amount > 0) {
                                selectedDebt.pay(amount);
                                String status = selectedDebt.isCleared() ? "Debt cleared!"
                                        : String.format("Remaining: $%.2f", selectedDebt.getRemainingAmount());
                                JOptionPane.showMessageDialog(this,
                                        String.format("Payment recorded!\n%s", status));
                            } else {
                                JOptionPane.showMessageDialog(this, "Amount must be greater than 0");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid amount");
                        }
                    }
                } else if (selectedDebt != null) {
                    JOptionPane.showMessageDialog(this, "This debt is already cleared!");
                }
            }
        } else if (choice == 2) { // View Details
            if (debts.size() == 0) {
                JOptionPane.showMessageDialog(this, "No debts to view.");
                return;
            }

            String[] debtNames = new String[debts.size()];
            int idx = 0;
            for (com.expense.tracker.model.Debt d : debts) {
                debtNames[idx++] = d.getPersonName();
            }

            String selected = (String) JOptionPane.showInputDialog(this, "Select Debt:",
                    "View Details", JOptionPane.QUESTION_MESSAGE, null, debtNames, debtNames[0]);

            if (selected != null) {
                for (com.expense.tracker.model.Debt d : debts) {
                    if (d.getPersonName().equals(selected)) {
                        StringBuilder details = new StringBuilder();
                        details.append("Debt to: ").append(d.getPersonName()).append("\n");
                        details.append("Total Amount: $").append(String.format("%.2f", d.getTotalAmount()))
                                .append("\n");
                        details.append("Paid: $").append(String.format("%.2f", d.getPaidAmount())).append("\n");
                        details.append("Remaining: $").append(String.format("%.2f", d.getRemainingAmount()))
                                .append("\n");
                        details.append("Status: ").append(d.isCleared() ? "CLEARED" : "PENDING").append("\n\n");
                        details.append("Payment History:\n");

                        if (d.getPaymentHistory().isEmpty()) {
                            details.append("No payments yet.");
                        } else {
                            int paymentNum = 1;
                            for (com.expense.tracker.model.Debt.DebtPayment p : d.getPaymentHistory()) {
                                details.append(String.format("  Payment #%d: $%.2f\n",
                                        paymentNum++, p.getAmount()));
                            }
                        }

                        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(details.toString(), 15, 40)),
                                "Debt Details", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }
        }
    }

    private void manageInvestments() {
        com.expense.tracker.util.CustomLinkedList<com.expense.tracker.model.Investment> invs = mainFrame.getService()
                .getCurrentUser().getInvestments();
        StringBuilder sb = new StringBuilder("Investments:\n");
        for (com.expense.tracker.model.Investment i : invs) {
            sb.append(i.toString()).append("\n");
        }

        String[] options = { "Add Investment", "Pay Installment", "View Details", "Close" };
        int choice = JOptionPane.showOptionDialog(this, new JScrollPane(new JTextArea(sb.toString(), 10, 30)),
                "Investments",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == 0) { // Add Investment
            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Property/Stock Name:"));
            JTextField nameField = new JTextField();
            panel.add(nameField);
            panel.add(new JLabel("Total Value:"));
            JTextField valField = new JTextField();
            panel.add(valField);
            panel.add(new JLabel("Total Installments:"));
            JTextField instField = new JTextField();
            panel.add(instField);

            int res = JOptionPane.showConfirmDialog(this, panel, "Add Investment", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText();
                    double val = Double.parseDouble(valField.getText());
                    int inst = Integer.parseInt(instField.getText());
                    mainFrame.getService().addInvestment(new com.expense.tracker.model.Investment(name, val, inst));
                    JOptionPane.showMessageDialog(this, "Investment added successfully!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid input");
                }
            }
        } else if (choice == 1) { // Pay Installment
            if (invs.size() == 0) {
                JOptionPane.showMessageDialog(this, "No investments to pay.");
                return;
            }

            // Select investment
            String[] investmentNames = new String[invs.size()];
            int idx = 0;
            for (com.expense.tracker.model.Investment i : invs) {
                investmentNames[idx++] = i.getName();
            }

            String selected = (String) JOptionPane.showInputDialog(this, "Select Investment:",
                    "Pay Installment", JOptionPane.QUESTION_MESSAGE, null, investmentNames, investmentNames[0]);

            if (selected != null) {
                com.expense.tracker.model.Investment selectedInv = null;
                for (com.expense.tracker.model.Investment i : invs) {
                    if (i.getName().equals(selected)) {
                        selectedInv = i;
                        break;
                    }
                }

                if (selectedInv != null && selectedInv.getRemainingInstallments() > 0) {
                    String amountStr = JOptionPane.showInputDialog(this,
                            String.format("Pay installment for %s\nSuggested amount: $%.2f\n\nEnter amount:",
                                    selectedInv.getName(), selectedInv.getAmountPerInstallment()));

                    if (amountStr != null) {
                        try {
                            double amount = Double.parseDouble(amountStr);
                            selectedInv.payInstallment(amount);
                            mainFrame.getService().getCurrentUser(); // Trigger save via next transaction
                            JOptionPane.showMessageDialog(this,
                                    String.format("Installment paid! Remaining: %d installments, $%.2f",
                                            selectedInv.getRemainingInstallments(), selectedInv.getRemainingAmount()));
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid amount");
                        }
                    }
                } else if (selectedInv != null) {
                    JOptionPane.showMessageDialog(this, "All installments already paid!");
                }
            }
        } else if (choice == 2) { // View Details
            if (invs.size() == 0) {
                JOptionPane.showMessageDialog(this, "No investments to view.");
                return;
            }

            String[] investmentNames = new String[invs.size()];
            int idx = 0;
            for (com.expense.tracker.model.Investment i : invs) {
                investmentNames[idx++] = i.getName();
            }

            String selected = (String) JOptionPane.showInputDialog(this, "Select Investment:",
                    "View Details", JOptionPane.QUESTION_MESSAGE, null, investmentNames, investmentNames[0]);

            if (selected != null) {
                for (com.expense.tracker.model.Investment i : invs) {
                    if (i.getName().equals(selected)) {
                        StringBuilder details = new StringBuilder();
                        details.append("Investment: ").append(i.getName()).append("\n");
                        details.append("Total Value: $").append(String.format("%.2f", i.getTotalValue())).append("\n");
                        details.append("Total Paid: $").append(String.format("%.2f", i.getTotalPaid())).append("\n");
                        details.append("Remaining: $").append(String.format("%.2f", i.getRemainingAmount()))
                                .append("\n");
                        details.append("Installments: ").append(i.getInstallmentsPaid()).append("/")
                                .append(i.getTotalInstallments()).append("\n\n");
                        details.append("Payment History:\n");

                        if (i.getPaymentHistory().isEmpty()) {
                            details.append("No payments yet.");
                        } else {
                            for (com.expense.tracker.model.Investment.InstallmentPayment p : i.getPaymentHistory()) {
                                details.append(String.format("  Installment #%d: $%.2f\n",
                                        p.getInstallmentNumber(), p.getAmount()));
                            }
                        }

                        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(details.toString(), 15, 40)),
                                "Investment Details", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }
        }
    }
}
