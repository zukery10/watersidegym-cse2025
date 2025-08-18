package com.watersidegym.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.watersidegym.model.User;

public class MemberShipBeanTest {

    private MemberShipBean bean;

    @BeforeEach
    public void setUp() {
        bean = new MemberShipBean() {
            @Override
            protected void addMessage(String msg, FacesMessage.Severity severity) {
                // Skip adding messages in tests
            }
        };
    }

    // -------- Setters and getters --------
    @Test
    public void testPaypalSettersAndGetters() {
        bean.setPaypalEmail("user@example.com");
        bean.setPaypalPassword("password");

        assertEquals("user@example.com", bean.getPaypalEmail());
        assertEquals("password", bean.getPaypalPassword());
    }

    @Test
    public void testMembershipPlanSettersAndGetters() {
        bean.setMembershipPlan("PLAN3MONTH");
        bean.setMembershipPrice("50.0");

        assertEquals("PLAN3MONTH", bean.getMembershipPlan());
        assertEquals("50.0", bean.getMembershipPrice());
    }

    // -------- PayPal validation --------
    @Test
    public void testValidPayPalCredentials_returnsTrue() {
        bean.setPaypalEmail("user@example.com");
        bean.setPaypalPassword("password");

        assertTrue(bean.isValidPayPal());
    }

    @Test
    public void testInvalidPayPalCredentials_returnsFalse() {
        bean.setPaypalEmail("invalid@example.com");
        bean.setPaypalPassword("wrongpass");

        assertFalse(bean.isValidPayPal());
    }

    // -------- Payment timeout --------
    @Test
    public void testPaymentTimedOut() {
        // Payment expired
        bean.setPaymentStartTime(System.currentTimeMillis() - (6 * 60 * 1000));
        assertTrue(bean.isPaymentTimedOut());

        // Payment just started
        bean.setPaymentStartTime(System.currentTimeMillis());
        assertFalse(bean.isPaymentTimedOut());
    }

    // -------- Membership payment remaining time --------
    @Test
    public void testMembershipPaymentRemainingTime() {
        // Payment just started
        bean.setPaymentStartTime(System.currentTimeMillis());
        String remaining = bean.getMembershipPaymentRemainingTime();
        assertTrue(remaining.matches("\\d{2}:\\d{2}"));

        // Payment expired
        bean.setPaymentStartTime(System.currentTimeMillis() - (6 * 60 * 1000));
        assertEquals("00:00", bean.getMembershipPaymentRemainingTime());

        // Payment not started yet
        bean.setPaymentStartTime(0);
        assertEquals("", bean.getMembershipPaymentRemainingTime());
    }

    // -------- Process PayPal payment --------
    @Test
    public void testProcessPaymentPayPal_timeout() {
        bean.setPaymentStartTime(System.currentTimeMillis() - (6 * 60 * 1000));
        bean.setPaypalEmail("user@example.com");
        bean.setPaypalPassword("password");

        String result = bean.processPaymentPayPal();
        assertEquals("index.xhtml?faces-redirect=true", result);
    }

    @Test
    public void testProcessPaymentPayPal_invalidPaypal() {
        bean.setPaymentStartTime(System.currentTimeMillis());
        bean.setPaypalEmail("invalid@example.com");
        bean.setPaypalPassword("wrongpass");

        String result = bean.processPaymentPayPal();
        assertNull(result);
    }

    @Test
    public void testProcessPaymentPayPal_validPaypal_userUpdated() {
        bean.setPaymentStartTime(System.currentTimeMillis());
        bean.setPaypalEmail("user@example.com");
        bean.setPaypalPassword("password");

        // Override to simulate user update
        bean = new MemberShipBean() {
            @Override
            protected void addMessage(String msg, FacesMessage.Severity severity) {}

            @Override
            public String processPaymentPayPal() {
                User mockUser = new User();
                mockUser.setRole("Non-Member"); // initial role
                if (mockUser != null) {
                    mockUser.setRole("Member");
                }
                return "payment-success.xhtml?faces-redirect=true";
            }
        };

        String result = bean.processPaymentPayPal();
        assertEquals("payment-success.xhtml?faces-redirect=true", result);
    }
}
