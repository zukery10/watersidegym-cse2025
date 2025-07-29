package com.watersidegym.controller;

import static org.junit.jupiter.api.Assertions.*;

import javax.faces.application.FacesMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.User;

class TestLoginBean {
	
	LoginBean loginbean;
	User user;
	@BeforeEach
	void setUp(){
		loginbean = new LoginBean();  //create an instance before each test
	}
	

	@Test
	void testEmptyFields() {
		loginbean.setEmail("");  // sets empty fields 
		loginbean.setPassword("");
		assertTrue(loginbean.emptyFields());
	}
	@Test
	void testLoginSuccess() {
		
	}
	@Test
	void testNoEmailFound() {
		assertTrue(loginbean.noEmailFound(user));
		
	}
	
	@Test
	void testPasswordMismatch() {
		
		assertTrue(loginbean.passwordMismatch(user));
		
	}

}
