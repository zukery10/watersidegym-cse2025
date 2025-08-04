package com.watersidegym.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemberShipBeanTest {

    private MemberShipBean bean;

    @BeforeEach
    public void setUp() {
        bean = new MemberShipBean();
    }

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

    @Test
    public void testChoosePlan_setsPlanAndConfirmationMessage() {
        String result = bean.choosePlan("PLAN6MONTH", "90.0");

        assertEquals("PLAN6MONTH", bean.getMembershipPlan());
        assertEquals("90.0", bean.getMembershipPrice());
        assertTrue(bean.getConfirmationMessage().contains("PLAN6MONTH"));
        assertEquals("payment.xhtml?faces-redirect=true", result);
    }

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

    @Test
    public void testProcessPaymentPayPal_withValidCredentials_returnsSuccessPage() {
        bean.setMembershipPlan("PLAN12MONTH");
        bean.setPaypalEmail("user@example.com");
        bean.setPaypalPassword("password");

        String result = bean.processPaymentPayPal();

        assertEquals("payment-success.xhtml?faces-redirect=true", result);
    }

    @Test
    public void testProcessPaymentPayPal_withInvalidCredentials_returnsNull() {
        bean.setMembershipPlan("PLAN12MONTH");
        bean.setPaypalEmail("invalid@example.com");
        bean.setPaypalPassword("wrongpass");

        String result = bean.processPaymentPayPal();

        assertNull(result);
    }

    
}
