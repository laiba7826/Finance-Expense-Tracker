package com.expense.tracker.ui;

import com.expense.tracker.service.FinanceService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private FinanceService financeService;

    public MainFrame() {
        financeService = new FinanceService();

        setTitle("Smart Expense Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize Panels
        LoginPanel loginPanel = new LoginPanel(this);
        DashboardPanel dashboardPanel = new DashboardPanel(this);
        StatsPanel statsPanel = new StatsPanel(this);
        AddExpensePanel addExpensePanel = new AddExpensePanel(this);
        SetBudgetPanel setBudgetPanel = new SetBudgetPanel(this);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(statsPanel, "STATS");
        mainPanel.add(addExpensePanel, "ADD_EXPENSE");
        mainPanel.add(setBudgetPanel, "SET_BUDGET");

        add(mainPanel);

        // Logic to check if user already exists
        if (financeService.getCurrentUser().getUsername().equals("New User")) {
            cardLayout.show(mainPanel, "LOGIN");
        } else {
            // If user exists, maybe still show login or go straight to dashboard?
            // "first we log in to it that we see welcome message"
            // Let's always show login for simplicity or "Welcome Back"
            cardLayout.show(mainPanel, "LOGIN");
        }
    }

    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }

    public FinanceService getService() {
        return financeService;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}
