package com.expense.tracker.model;

public class Expense extends Transaction {
    private static final long serialVersionUID = 1L;

    public Expense(double amount, String category, String description) {
        super(amount, category, description);
    }
}
