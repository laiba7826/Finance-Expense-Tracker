package com.expense.tracker.model;

public class Donation extends Transaction {
    private static final long serialVersionUID = 1L;

    public Donation(double amount, String description) {
        super(amount, "Charity", description);
    }
}
