package com.watersidegym.model;

public enum MembershipPlan {
    PLAN3MONTH("3 Month"),
    PLAN6MONTH("6 Month"),
    PLAN12MONTH("12 Month");

    private final String label;

    MembershipPlan(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
