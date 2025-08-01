package com.watersidegym.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.User;

public class UserServiceTest {

	@BeforeEach
	public void setUp() {
		// Clear users between tests
		UserService.getUsers().clear();
	}

	@Test
	public void testAddUser_Success() {
		User user = new User();
		user.setEmail("test@example.com");

		boolean result = UserService.addUser(user);

		assertTrue(result);
		assertEquals(1, UserService.getUsers().size());
	}
	@Test
    public void testAddUser_DuplicateEmail_Fails() {
        User user1 = new User();
        user1.setEmail("test@example.com");
        UserService.addUser(user1);

        User user2 = new User();
        user2.setEmail("test@example.com");

        boolean result = UserService.addUser(user2);

        assertFalse(result);
        assertEquals(1, UserService.getUsers().size());
    }
	
	@Test
    public void testFindUserByEmail_FindsCorrectUser() {
        User user = new User();
        user.setEmail("findme@example.com");
        UserService.addUser(user);

        User found = UserService.findUserByEmail("findme@example.com");

        assertNotNull(found);
        assertEquals("findme@example.com", found.getEmail());
    }
	
	@Test
    public void testFindUserByEmail_NotFound() {
        User found = UserService.findUserByEmail("notfound@example.com");

        assertNull(found);
    }
}
