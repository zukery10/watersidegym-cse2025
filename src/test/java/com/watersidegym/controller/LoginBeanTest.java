package com.watersidegym.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import javax.faces.application.FacesMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.User;

class LoginBeanTest {
	
	LoginBean loginbean;
	User user;
	@BeforeEach
	public void setUp(){
		loginbean = new LoginBean();  //create an instance before each test
	}
	

	@Test
	public void testEmptyFields() {
		loginbean.setEmail("");  // sets empty fields 
		loginbean.setPassword("");
		assertTrue(loginbean.emptyFields());
	}

	@Test
	public void testNoEmailFound() {
		assertTrue(loginbean.noEmailFound(user));
		
	}
	
	@Test
	public void testPasswordMismatch() {
		User user2 = new User();
       
        user2.setPassword("Member123");
        
		assertTrue(loginbean.passwordMismatch(user2));
	}
	@Test
	public void testRedirectDashboard() {
		User user1 = new User();
        user1.setRole("Member");
		assertEquals("member-dashboard",loginbean.redirect(user1));
		User user2 = new User();
        user2.setRole("Non-Member");		
		assertEquals("nonmember-dashboard",loginbean.redirect(user2));
		User user3 = new User();
        user3.setRole("PT");
		assertEquals("personal-trainer-dashboard",loginbean.redirect(user3));
	}
	@Test 
	public void testLogout() {	
		assertEquals("index", loginbean.logout());		
	}


}
