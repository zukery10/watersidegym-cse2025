package com.watersidegym.controller;

import com.watersidegym.model.User;
import com.watersidegym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.faces.application.FacesMessage;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountBeanTest {

    private CreateAccountBean createAccBean;

    @BeforeEach
    public void init() {
        UserService.getUsers().clear();

        // we need to override this so FacesContext doesn't crash
        createAccBean = new CreateAccountBean() {
            protected void addMessage(String msg, FacesMessage.Severity severity) {
                // skipping messages for now
            }
        };
    }

    @Test
    public void shouldRegisterNewUser() {
        User u = new User();
        u.setEmail("myemail@example.com");
        createAccBean.setUser(u);

        String page = createAccBean.register();

        assertEquals("index", page); // should go to index
        assertEquals(1, UserService.getUsers().size()); // only one user should be there
    }

    @Test
    public void shouldNotRegisterIfEmailTaken() {
        User u1 = new User();
        u1.setEmail("taken@example.com");
        UserService.addUser(u1); // already added

        User u2 = new User();
        u2.setEmail("taken@example.com"); // same email
        createAccBean.setUser(u2);

        String page = createAccBean.register();

        assertNull(page); // registration failed
        assertEquals(1, UserService.getUsers().size()); // should still be just 1
    }

    @Test
    public void canSetAndGetUser() {
        User tempUser = new User();
        tempUser.setEmail("hello@abc.com");

        createAccBean.setUser(tempUser);

        assertEquals("hello@abc.com", createAccBean.getUser().getEmail());
    }

    @Test
    public void afterRegisterUserShouldBeReset() {
        User person = new User();
        person.setEmail("someone@example.com");
        createAccBean.setUser(person);

        createAccBean.register();

        assertNotSame(person, createAccBean.getUser());
        assertNull(createAccBean.getUser().getEmail()); // new empty user
    }

    @Test
    public void ifRegisterFailsUserStaysTheSame() {
        User existing = new User();
        existing.setEmail("a@a.com");
        UserService.addUser(existing); // add first

        User trying = new User();
        trying.setEmail("a@a.com"); // same email
        createAccBean.setUser(trying);

        createAccBean.register();

        assertSame(trying, createAccBean.getUser()); // same object
        assertEquals("a@a.com", createAccBean.getUser().getEmail());
    }
}
