package com.expense.tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Debt implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String personName; // Who we owe or who owes us
    private double totalAmount;
    private double paidAmount;

    @SuppressWarnings("unused")
    private boolean isPc; // "Payable" or "Receivable"

    private List<DebtPayment> paymentHistory; // Track each payment

    public Debt(String personName, double totalAmount) {
        this.id = UUID.randomUUID().toString();
        this.personName = personName;
        this.totalAmount = totalAmount;
        this.paidAmount = 0.0;
        this.paymentHistory = new ArrayList<>();
    }

    public void pay(double amount) {
        this.paidAmount += amount;
        this.paymentHistory.add(new DebtPayment(amount));
    }

    public double getRemainingAmount() {
        return totalAmount - paidAmount;
    }

    public boolean isCleared() {
        return getRemainingAmount() <= 0;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getPersonName() {
        return personName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public List<DebtPayment> getPaymentHistory() {
        return paymentHistory;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f / $%.2f (Remaining: $%.2f)",
                personName, paidAmount, totalAmount, getRemainingAmount());
    }

    // Handle backward compatibility for old serialized objects
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Initialize paymentHistory if it's null (old data)
        if (paymentHistory == null) {
            paymentHistory = new ArrayList<>();
        }
    }

    // Inner class for tracking individual payments
    public static class DebtPayment implements Serializable {
        private static final long serialVersionUID = 1L;
        private double amount;
        private long timestamp;

        public DebtPayment(double amount) {
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
        }

        public double getAmount() {
            return amount;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
