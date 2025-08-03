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
    public void testGetPriceFor_validPlans() {
        assertEquals(50.0, bean.getPriceFor("PLAN3MONTH"));
        assertEquals(90.0, bean.getPriceFor("PLAN6MONTH"));
        assertEquals(150.0, bean.getPriceFor("PLAN12MONTH"));
    }

    @Test
    public void testGetPriceFor_invalidPlan_returnsZero() {
        assertEquals(0.0, bean.getPriceFor("UNKNOWN_PLAN"));
    }

    @Test
    public void testCardAndPaypalSettersAndGetters() {
        bean.setCardholderName("Alice");
        bean.setCardNumber("4111111111111111");
        bean.setExpiry("12/30");
        bean.setCvv("123");
        bean.setPaypalEmail("user@example.com");
        bean.setPaypalPassword("password");

        assertEquals("Alice", bean.getCardholderName());
        assertEquals("4111111111111111", bean.getCardNumber());
        assertEquals("12/30", bean.getExpiry());
        assertEquals("123", bean.getCvv());
        assertEquals("user@example.com", bean.getPaypalEmail());
        assertEquals("password", bean.getPaypalPassword());
    }

    @Test
    public void testValidCardDetails_returnsTrue() {
        bean.setCardholderName("Jane");
        bean.setCardNumber("4111111111111111");
        bean.setCvv("123");
        assertTrue(bean.isValidCardDetails());
    }

    @Test
    public void testInvalidCardDetails_missingFields_returnsFalse() {
        bean.setCardholderName("");
        bean.setCardNumber("123"); // too short number for card
        bean.setCvv("1");          // too short cvv
        assertFalse(bean.isValidCardDetails());
    }

    @Test
    public void testCardNotExpired_returnsFalse() {
        bean.setExpiry("12/30");  // future date which is allowed
        assertFalse(bean.isCardExpired());
    }

    @Test
    public void testCardExpired_returnsTrue() {
        bean.setExpiry("01/20");  // past date, itis not allowed
        assertTrue(bean.isCardExpired());
    }

    @Test
    public void testCardInvalidFormat_returnsTrue() {
        bean.setExpiry("2025-12"); // invalid format as it should be MM/yy
        assertTrue(bean.isCardExpired());
    }


    @Test
    public void testValidPayPalCredentials_returnsTrue() {
        bean.setPaypalEmail("user@example.com");
        bean.setPaypalPassword("password");
        assertTrue(bean.isValidPayPal());
    }

    @Test
    public void testInvalidPayPalCredentials_returnsFalse() {
        bean.setPaypalEmail("wrong@example.com");
        bean.setPaypalPassword("wrongpass");
        assertFalse(bean.isValidPayPal());
    }
}
