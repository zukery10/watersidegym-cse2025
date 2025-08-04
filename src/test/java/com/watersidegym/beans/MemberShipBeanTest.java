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

    
}
