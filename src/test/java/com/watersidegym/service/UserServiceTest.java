package com.watersidegym.service;

import com.watersidegym.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {

	@BeforeEach
	public void reset() {
		UserService.getUsers().clear();
	}

	@Test
	public void addUserWorksIfNew() {
		User u = new User();
		u.setEmail("new@example.com");

		boolean added = UserService.addUser(u);

		assertTrue(added);
		assertNotNull(UserService.findUserByEmail("new@example.com"));
	}

	@Test
	public void addUserFailsIfEmailAlreadyExists() {
		User u1 = new User();
		u1.setEmail("dupe@email.com");
		UserService.addUser(u1);

		User u2 = new User();
		u2.setEmail("dupe@email.com");

		boolean result = UserService.addUser(u2);

		assertFalse(result);
	}

	@Test
	public void findByEmailReturnsNullIfNotThere() {
		User result = UserService.findUserByEmail("noone@here.com");
		assertNull(result);
	}

	@Test
	public void findByEmailIgnoresCase() {
		User u = new User();
		u.setEmail("Capital@Email.com");
		UserService.addUser(u);

		User found = UserService.findUserByEmail("capital@email.com");

		assertNotNull(found);
	}
}