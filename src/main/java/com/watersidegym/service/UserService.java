package com.watersidegym.service;

import com.watersidegym.model.User;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@ApplicationScoped //@ApplicationScoped means there is only ONE instance of UserService for the whole application, shared across all users and sessions.
public class UserService {
    private List<User> users;

    @PostConstruct
    public void init() {
        users = new ArrayList<>();

        User user2 = new User();
        user2.setEmail("member@example.com");
        user2.setPassword("Member123");
        user2.setFirstName("Jane");
        user2.setSecondName("Smith");
        user2.setAddress1("45 Elm Road");
        user2.setCounty("Cork");
        user2.setEircode("T12ABC1");
        user2.setPhoneNumber("0859876543");
        user2.setDateOfBirth(new Date(95, 6, 20));
        user2.setGender("Female");
        user2.setRole("Member");

        users.add(user2);
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean addUser(User user) {
        if (findUserByEmail(user.getEmail()) != null) {
            return false;
        }
        users.add(user);
        return true;
    }

    public User findUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
