package com.expense.tracker.ui;

import com.expense.tracker.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

public class StatsPanel extends JPanel {
    private MainFrame mainFrame;
    private JButton backButton;

    public StatsPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(60, 63, 65)); // Dark header
        backButton = new JButton("<< Back");
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(75, 110, 175));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainFrame.showCard("DASHBOARD"));

        JLabel title = new JLabel("Analytics & Reports");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        header.add(backButton);
        header.add(Box.createHorizontalStrut(20));
        header.add(title);

        add(header, BorderLayout.NORTH);

        // Tabbed Pane for Charts and Reports
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Expense Chart", new BarChartPanel());
        tabbedPane.addTab("Detailed Report", new ReportPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private class BarChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Refetch data every paint to ensure it's fresh
            Map<String, Double> categoryTotals = new HashMap<>();
            double maxVal = 0;

            if (mainFrame.getService() != null) {
                for (Transaction t : mainFrame.getService().getCurrentUser().getTransactions()) {
                    if (t instanceof Expense) {
                        String cat = t.getCategory();
                        double amount = t.getAmount();
                        categoryTotals.put(cat, categoryTotals.getOrDefault(cat, 0.0) + amount);
                        maxVal = Math.max(maxVal, categoryTotals.get(cat));
                    }
                }
            }

            if (categoryTotals.isEmpty()) {
                g.setColor(Color.GRAY);
                g.drawString("No expense data available to display.", 50, 50);
                return;
            }

            // Draw Chart
            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int barWidth = (width - 2 * padding) / Math.max(1, categoryTotals.size()) - 20;

            // Axes
            g.setColor(Color.BLACK);
            g.drawLine(padding, height - padding, width - padding, height - padding); // X
            g.drawLine(padding, height - padding, padding, padding); // Y

            int x = padding + 10;
            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                double val = entry.getValue();
                int barHeight = (int) ((val / maxVal) * (height - 2 * padding));

                // Bar
                g.setColor(new Color(100, 150, 200));
                g.fillRect(x, height - padding - barHeight, barWidth, barHeight);

                // Border
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, height - padding - barHeight, barWidth, barHeight);

                // Label
                g.setColor(Color.BLACK);
                String label = entry.getKey();
                // Truncate if too long
                if (label.length() > 5)
                    label = label.substring(0, 5) + "..";
                g.drawString(label, x, height - padding + 15);

                // Value
                String valStr = String.format("$%.0f", val);
                g.drawString(valStr, x + (barWidth - g.getFontMetrics().stringWidth(valStr)) / 2,
                        height - padding - barHeight - 5);

                x += barWidth + 20;
            }
        }
    }

    private class ReportPanel extends JPanel {
        private JTextArea reportArea;

        public ReportPanel() {
            setLayout(new BorderLayout());

            reportArea = new JTextArea();
            reportArea.setEditable(false);
            reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(reportArea);
            add(scrollPane, BorderLayout.CENTER);

            JButton refreshBtn = new JButton("Refresh Report");
            refreshBtn.setForeground(Color.BLACK);
            refreshBtn.addActionListener(e -> generateReport());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(refreshBtn);
            add(buttonPanel, BorderLayout.SOUTH);

            // Generate initial report
            generateReport();
        }

        private void generateReport() {
            StringBuilder report = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            if (mainFrame.getService() == null) {
                reportArea.setText("No data available.");
                return;
            }

            User user = mainFrame.getService().getCurrentUser();

            // Header
            report.append("═══════════════════════════════════════════════════════════════\n");
            report.append("           SMART EXPENSE TRACKER - FINANCIAL REPORT\n");
            report.append("═══════════════════════════════════════════════════════════════\n\n");

            // Summary
            report.append("┌─ FINANCIAL SUMMARY ─────────────────────────────────────────┐\n");
            report.append(String.format("│ User:             %-42s │\n", user.getUsername()));
            report.append(String.format("│ Current Balance:  $%-41.2f │\n", user.getCurrentBalance()));
            report.append(String.format("│ Monthly Salary:   $%-41.2f │\n", user.getMonthlySalary()));
            report.append(String.format("│ Total Savings:    $%-41.2f │\n", user.getTotalSavings()));
            report.append("└─────────────────────────────────────────────────────────────┘\n\n");

            // Transactions
            report.append("┌─ TRANSACTION HISTORY ───────────────────────────────────────┐\n");
            if (user.getTransactions().size() == 0) {
                report.append("│ No transactions recorded.                                   │\n");
            } else {
                for (Transaction t : user.getTransactions()) {
                    String type = t instanceof Income ? "INCOME" : t instanceof Donation ? "DONATE" : "EXPENSE";
                    String date = dateFormat.format(t.getDate());
                    report.append(String.format("│ [%s] %s\n", type, date));
                    report.append(String.format("│   Category: %-20s Amount: $%-15.2f │\n",
                            t.getCategory(), t.getAmount()));
                    report.append(String.format("│   Desc: %-50s │\n", t.getDescription()));
                    report.append("│                                                             │\n");
                }
            }
            report.append("└─────────────────────────────────────────────────────────────┘\n\n");

            // Expense by Category
            report.append("┌─ EXPENSES BY CATEGORY ──────────────────────────────────────┐\n");
            Map<String, Double> categoryTotals = new HashMap<>();
            for (Transaction t : user.getTransactions()) {
                if (t instanceof Expense) {
                    categoryTotals.put(t.getCategory(),
                            categoryTotals.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                }
            }
            if (categoryTotals.isEmpty()) {
                report.append("│ No expenses recorded.                                       │\n");
            } else {
                for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                    report.append(String.format("│ %-30s $%-28.2f │\n",
                            entry.getKey(), entry.getValue()));
                }
            }
            report.append("└─────────────────────────────────────────────────────────────┘\n\n");

            // Budgets
            report.append("┌─ BUDGET LIMITS ─────────────────────────────────────────────┐\n");
            if (user.getCategoryBudgets().isEmpty()) {
                report.append("│ No budgets set.                                             │\n");
            } else {
                for (Map.Entry<String, Double> entry : user.getCategoryBudgets().entrySet()) {
                    double spent = categoryTotals.getOrDefault(entry.getKey(), 0.0);
                    double limit = entry.getValue();
                    double remaining = limit - spent;
                    String status = remaining >= 0 ? "OK" : "OVER";
                    report.append(String.format("│ %-20s Limit: $%-10.2f Spent: $%-10.2f │\n",
                            entry.getKey(), limit, spent));
                    report.append(String.format("│   Remaining: $%-15.2f [%s]\n",
                            remaining, status));
                }
            }
            report.append("└─────────────────────────────────────────────────────────────┘\n\n");

            // Debts
            report.append("┌─ DEBTS TRACKING ────────────────────────────────────────────┐\n");
            if (user.getDebts().size() == 0) {
                report.append("│ No debts recorded.                                          │\n");
            } else {
                for (Debt d : user.getDebts()) {
                    report.append(String.format("│ %-30s Total: $%-18.2f │\n",
                            d.getPersonName(), d.getTotalAmount()));
                    report.append(String.format("│   Paid: $%-15.2f Remaining: $%-18.2f │\n",
                            d.getPaidAmount(), d.getRemainingAmount()));
                }
            }
            report.append("└─────────────────────────────────────────────────────────────┘\n\n");

            // Investments
            report.append("┌─ INVESTMENTS ───────────────────────────────────────────────┐\n");
            if (user.getInvestments().size() == 0) {
                report.append("│ No investments recorded.                                    │\n");
            } else {
                for (Investment inv : user.getInvestments()) {
                    report.append(String.format("│ %-30s Value: $%-18.2f │\n",
                            inv.getName(), inv.getTotalValue()));
                    report.append(String.format("│   Installments: %d/%d (Remaining: %d)\n",
                            inv.getInstallmentsPaid(), inv.getTotalInstallments(),
                            inv.getRemainingInstallments()));
                }
            }
            report.append("└─────────────────────────────────────────────────────────────┘\n");

            reportArea.setText(report.toString());
            reportArea.setCaretPosition(0); // Scroll to top
        }
    }
}
