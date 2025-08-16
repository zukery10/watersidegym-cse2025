package com.watersidegym.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.User;
import com.watersidegym.service.UserService;

import javax.faces.application.FacesMessage;

class LoginBeanTest {

    private LoginBean loginbean;

    @BeforeEach
    public void setUp() {
        // Clear users before each test
        UserService.getUsers().clear();

        // Override addMessage so FacesContext is not used
        loginbean = new LoginBean() {
            @Override
            protected void addMessage(String msg, FacesMessage.Severity severity) {
                // Do nothing in tests
            }
        };
    }

    @Test
    public void loginFailsWhenFieldsEmpty() {
        loginbean.setEmail("");
        loginbean.setPassword("");

        String page = loginbean.login();

        assertNull(page); // login should fail
    }

    @Test
    public void loginFailsWhenEmailNotFound() {
        loginbean.setEmail("notfound@example.com");
        loginbean.setPassword("password123");

        String page = loginbean.login();

        assertNull(page); // login fails because user does not exist
    }

    @Test
    public void loginFailsWhenPasswordIncorrect() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("rightpassword");
        user.setRole("Member");
        UserService.addUser(user);

        loginbean.setEmail("user@example.com");
        loginbean.setPassword("wrongpassword");

        String page = loginbean.login();

        assertNull(page); // login fails because password is wrong
    }

    @Test
    public void loginSucceedsAndRedirectsMember() {
        // Clear users
        UserService.getUsers().clear();

        // Create a user with Member role
        User member = new User();
        member.setEmail("member@example.com");
        member.setPassword("password");
        UserService.addUser(member);
        member.setRole("Member");


        // Set login input
        loginbean.setEmail("member@example.com");
        loginbean.setPassword("password");

        // Call login
        String page = loginbean.login();

        // Check redirect
        assertEquals("member-dashboard", page);
        assertEquals(member, loginbean.getLoggedInUser());
    }

    @Test
    public void loginSucceedsAndRedirectsNonMember() {
        User user = new User();
        user.setEmail("nonmember@example.com");
        user.setPassword("password");
        UserService.addUser(user);

        loginbean.setEmail("nonmember@example.com");
        loginbean.setPassword("password");

        String page = loginbean.login();

        assertEquals("nonmember-dashboard", page);
        assertEquals(user, loginbean.getLoggedInUser());
    }

    @Test
    public void loginSucceedsAndRedirectsPT() {
        User user = new User();
        user.setEmail("pt@example.com");
        user.setPassword("password");
        UserService.addUser(user);
        user.setRole("PT");

        loginbean.setEmail("pt@example.com");
        loginbean.setPassword("password");

        String page = loginbean.login();

        assertEquals("personal-trainer-dashboard", page);
        assertEquals(user, loginbean.getLoggedInUser());
    }
    
    @Test
    public void logoutClearsUserAndFields() {
        User user = new User();
        user.setEmail("member@example.com");
        user.setPassword("password");
        UserService.addUser(user);
        user.setRole("Member");

        loginbean.setEmail("member@example.com");
        loginbean.setPassword("password");
        loginbean.login(); // log in first

        String page = loginbean.logout();

        assertEquals("index", page);
        assertNull(loginbean.getLoggedInUser());
        assertNull(loginbean.getEmail());
        assertNull(loginbean.getPassword());
    }
    
    @Test
    public void testIsLoggedIn() {
        assertFalse(loginbean.isLoggedIn()); // not logged in yet

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");
        UserService.addUser(user);
        user.setRole("Member");

        loginbean.setEmail("user@example.com");
        loginbean.setPassword("password");
        loginbean.login();

        assertTrue(loginbean.isLoggedIn()); // logged in
    }


}
