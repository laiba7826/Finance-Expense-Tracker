package com.expense.tracker.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected double amount;
    protected String category; // e.g., "Food", "Salary", "Charity"
    protected Date date;
    protected String description;

    public Transaction(double amount, String category, String description) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = new Date();
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public Date getDate() { return date; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("%s: %.2f [%s] - %s", date, amount, category, description);
    }
}
