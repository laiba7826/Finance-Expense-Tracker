package com.expense.tracker.model;

import com.expense.tracker.util.CustomLinkedList;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private double currentBalance;
    private double monthlySalary;
    private double totalSavings;

    // Hidden savings for charity logic
    private double hiddenCharityPool;

    private CustomLinkedList<Transaction> transactions;
    private CustomLinkedList<Debt> debts;
    private CustomLinkedList<Investment> investments;
    private Map<String, Double> categoryBudgets; // Category -> Limit

    public User(String username) {
        this.username = username;
        this.currentBalance = 0.0;
        this.transactions = new CustomLinkedList<>();
        this.debts = new CustomLinkedList<>();
        this.investments = new CustomLinkedList<>();
        this.categoryBudgets = new HashMap<>();
    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public void addDebt(Debt d) {
        this.debts.add(d);
    }

    public void addInvestment(Investment i) {
        this.investments.add(i);
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public double getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(double totalSavings) {
        this.totalSavings = totalSavings;
    }

    public double getHiddenCharityPool() {
        return hiddenCharityPool;
    }

    public void addToHiddenCharity(double amount) {
        this.hiddenCharityPool += amount;
    }

    public CustomLinkedList<Transaction> getTransactions() {
        return transactions;
    }

    public CustomLinkedList<Debt> getDebts() {
        return debts;
    }

    public CustomLinkedList<Investment> getInvestments() {
        return investments;
    }

    public Map<String, Double> getCategoryBudgets() {
        return categoryBudgets;
    }

    public void setBudget(String category, double limit) {
        categoryBudgets.put(category, limit);
    }
}
