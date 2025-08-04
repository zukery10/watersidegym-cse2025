package com.watersidegym.service;

import com.watersidegym.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserService {
    private static List<User> users = new ArrayList<>();

    // Static block to initialize users once when the class loads
    static { // member example
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
        user2.setRole("Non-Member");
        users.add(user2);
        
        //PT Example
        User user3 = new User();
        user3.setEmail("PT@example.com");
        user3.setPassword("PT123456");
        user3.setFirstName("Joe");
        user3.setSecondName("Soundman");
        user3.setAddress1("65 Elm Road");
        user3.setCounty("Wicklow");
        user3.setEircode("T12ABC1");
        user3.setPhoneNumber("0859876543");
        user3.setDateOfBirth(new Date(95, 6, 20));
        user3.setGender("Male");
        user3.setRole("PT");
        users.add(user3);
        
    }
    
    
    // Static method to get all users
    public static List<User> getUsers() {
        return users;
    }

    // Static method to add user, returns false if email exists
    public static boolean addUser(User user) {
        if (findUserByEmail(user.getEmail()) != null) {
            return false;
        }
        users.add(user);
        user.setRole("Non-Member");
        return true;
    }

    // Static method to find user by email
    public static User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
}
