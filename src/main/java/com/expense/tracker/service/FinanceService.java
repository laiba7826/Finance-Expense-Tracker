package com.expense.tracker.service;

import com.expense.tracker.model.*;
import java.io.*;
import java.util.Map;

public class FinanceService {

    private User currentUser;
    private static final String DATA_FILE = "user_data.dat";

    public FinanceService() {
        // Try to load existing data
        loadData();
        if (currentUser == null) {
            currentUser = new User("New User"); // Default user if none exists
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // --- Transaction Logic ---

    public void addIncome(double amount, String category, String description) {
        Income income = new Income(amount, category, description);
        currentUser.addTransaction(income);
        currentUser.setCurrentBalance(currentUser.getCurrentBalance() + amount);
        saveData(); // Auto-save
    }

    /**
     * @return null if successful, or a warning string if budget exceeded.
     */
    public String addExpense(double amount, String category, String description) {
        String warning = null;

        // Check Budget
        Map<String, Double> budgets = currentUser.getCategoryBudgets();
        if (budgets.containsKey(category)) {
            double limit = budgets.get(category);
            double spent = calculateTotalSpent(category);
            if (spent + amount > limit) {
                warning = "Warning: You have exceeded your budget for " + category + "!";
            }
        }

        Expense expense = new Expense(amount, category, description);
        currentUser.addTransaction(expense);
        currentUser.setCurrentBalance(currentUser.getCurrentBalance() - amount);

        saveData();
        return warning;
    }

    public void addDebt(Debt debt) {
        currentUser.addDebt(debt);
        saveData();
    }

    public void addInvestment(Investment investment) {
        currentUser.addInvestment(investment);
        saveData();
    }

    public void setBudget(String category, double limit) {
        currentUser.setBudget(category, limit);
        saveData();
    }

    // --- Logic for Savings and Charity ---

    public void addSavings(double amount) {
        currentUser.setCurrentBalance(currentUser.getCurrentBalance() - amount);
        currentUser.setTotalSavings(currentUser.getTotalSavings() + amount);
        saveData();
    }

    /**
     * Deducts from savings but moves strict amount to hidden charity pool.
     */
    public void donateToCharity(double amount, String cause) {
        if (currentUser.getTotalSavings() >= amount) {
            currentUser.setTotalSavings(currentUser.getTotalSavings() - amount);
            currentUser.addToHiddenCharity(amount);

            // Log as a donation transaction? Or keep it completely hidden?
            // The prompt says "doesnt be shwon on the app" regarding the remaining amount.
            // Let's add a Donation transaction but maybe filter it from normal view if
            // strict.
            // For now, let's track it in the hidden pool mainly.
            Donation donation = new Donation(amount, cause);
            currentUser.addTransaction(donation);

            saveData();
        } else {
            // Not enough savings
            throw new IllegalArgumentException("Insufficient savings for this donation.");
        }
    }

    // --- Helper Logic ---

    private double calculateTotalSpent(String category) {
        double total = 0;
        for (Transaction t : currentUser.getTransactions()) {
            if (t instanceof Expense && t.getCategory().equalsIgnoreCase(category)) {
                // Should check if it's in the current month? For simplicity, we assume global
                // or we check dates.
                // Let's do simple global check for now or basic month check if possible.
                // Keeping it simple: global total for category for now.
                total += t.getAmount();
            }
        }
        return total;
    }

    // --- Persistence ---

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                currentUser = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
