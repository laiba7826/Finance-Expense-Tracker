package com.expense.tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Investment implements Serializable {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private String id;
    private String name; // Property name or Stock name
    private double totalValue;
    private int totalInstallments;
    private int installmentsPaid;

    private double amountPerInstallment; // Amount per installment
    private List<InstallmentPayment> paymentHistory; // Track each payment

    public Investment(String name, double totalValue, int totalInstallments) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.totalValue = totalValue;
        this.totalInstallments = totalInstallments;
        this.installmentsPaid = 0;
        this.amountPerInstallment = totalValue / totalInstallments;
        this.paymentHistory = new ArrayList<>();
    }

    public void payInstallment(double amount) {
        if (installmentsPaid < totalInstallments) {
            installmentsPaid++;
            paymentHistory.add(new InstallmentPayment(installmentsPaid, amount));
        }
    }

    public int getRemainingInstallments() {
        return totalInstallments - installmentsPaid;
    }

    public double getTotalPaid() {
        double total = 0;
        for (InstallmentPayment payment : paymentHistory) {
            total += payment.getAmount();
        }
        return total;
    }

    public double getRemainingAmount() {
        return totalValue - getTotalPaid();
    }

    public String getName() {
        return name;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public int getInstallmentsPaid() {
        return installmentsPaid;
    }

    public int getTotalInstallments() {
        return totalInstallments;
    }

    public double getAmountPerInstallment() {
        return amountPerInstallment;
    }

    public List<InstallmentPayment> getPaymentHistory() {
        return paymentHistory;
    }

    @Override
    public String toString() {
        return String.format("%s: Installments %d/%d (Paid: $%.2f, Remaining: $%.2f)",
                name, installmentsPaid, totalInstallments, getTotalPaid(), getRemainingAmount());
    }

    // Handle backward compatibility for old serialized objects
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Initialize paymentHistory if it's null (old data)
        if (paymentHistory == null) {
            paymentHistory = new ArrayList<>();
        }
        // Initialize amountPerInstallment if it's 0 (old data)
        if (amountPerInstallment == 0 && totalInstallments > 0) {
            amountPerInstallment = totalValue / totalInstallments;
        }
    }

    // Inner class for tracking individual payments
    public static class InstallmentPayment implements Serializable {
        private static final long serialVersionUID = 1L;
        private int installmentNumber;
        private double amount;
        private long timestamp;

        public InstallmentPayment(int installmentNumber, double amount) {
            this.installmentNumber = installmentNumber;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
        }

        public int getInstallmentNumber() {
            return installmentNumber;
        }

        public double getAmount() {
            return amount;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
