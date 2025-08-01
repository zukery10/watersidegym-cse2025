package com.watersidegym.model;

public enum MembershipPlan {
    PLAN3MONTH("3 Month", 50.0),
    PLAN6MONTH("6 Month", 90.0),
    PLAN12MONTH("12 Month", 150.0); // fixed price instead of €50 typo

    private final String label;
    private final double price;

    MembershipPlan(String label, double price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public double getPrice() {
        return price;
    }
    
}