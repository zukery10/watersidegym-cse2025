package com.watersidegym.controller;
import com.watersidegym.model.User;
import com.watersidegym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountBeanTest {

    private CreateAccountBean bean;

    @BeforeEach
    public void setUp() {
        UserService.getUsers().clear(); // Ensure a clean state before each test
        bean = new CreateAccountBean();
    }

    @Test
    public void testRegister_Successful() {
        User user = new User();
        user.setEmail("newuser@example.com");
        bean.setUser(user);

        String result = bean.register();

        assertEquals("index.jsf", result);
        assertEquals(1, UserService.getUsers().size());
    }

    @Test
    public void testRegister_DuplicateEmail() {
        // Add an existing user first
        User existingUser = new User();
        existingUser.setEmail("duplicate@example.com");
        UserService.addUser(existingUser);

        // Try to register the same email
        User duplicateUser = new User();
        duplicateUser.setEmail("duplicate@example.com");
        bean.setUser(duplicateUser);

        String result = bean.register();

        assertNull(result);
        assertEquals(1, UserService.getUsers().size()); // Still only the original user
    }

    @Test
    public void testSetAndGetUser() {
        User testUser = new User();
        testUser.setEmail("test@example.com");

        bean.setUser(testUser);

        assertEquals("test@example.com", bean.getUser().getEmail());
    }
}
