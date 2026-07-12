package com.expense.tracker.model;

public class Income extends Transaction {
    private static final long serialVersionUID = 1L;

    public Income(double amount, String category, String description) {
        super(amount, category, description);
    }
}
