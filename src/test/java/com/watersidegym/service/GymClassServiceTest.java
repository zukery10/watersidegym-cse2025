package com.watersidegym.service;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.GymClass;
import com.watersidegym.model.User;

public class GymClassServiceTest {

    private GymClassService gymClassService;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    @BeforeEach
    public void setup() {        
        dateFormat = new SimpleDateFormat("dd.MM.yy");
        timeFormat = new SimpleDateFormat("HH:mm");
        gymClassService = new GymClassService();
    }

    @Test
    public void testClassListNotEmpty() {
        // Ensure the service returns a non-empty list of gym classes
        List<GymClass> list = gymClassService.getAllGymClasses();
        assertNotNull(list, "Class list should not be null");
        assertFalse(list.isEmpty(), "Class list should not be empty");
    }

    @Test
    public void testEachClassHasValidNameAndDate() {
        // Loop through all classes and verify that necessary fields are set
        for (GymClass cls : gymClassService.getAllGymClasses()) {
            assertNotNull(cls.getNameOfClass(), "Class name should not be null");
            assertNotNull(cls.getClassDate(), "Class date should not be null");
            assertNotNull(cls.getClassTime(), "Class time should not be null");
        }
    }

    @Test
    public void testNumberOfClassesLoaded() {
        // Confirm that 18 gym classes are loaded as hardcoded in the service
        assertEquals(18, gymClassService.getAllGymClasses().size(), "Expected 18 classes loaded");
    }
    
    @Test
    public void testGymClassValues() throws ParseException {
    	GymClass gymClass = gymClassService.getAllGymClasses().get(0);

        assertEquals("Yoga", gymClass.getNameOfClass());

        assertEquals(dateFormat.parse("07.08.25"), gymClass.getClassDate());

        assertEquals(timeFormat.parse("08:00"), gymClass.getClassTime());
        assertEquals("1 hr", gymClass.getClassDuration());
        assertEquals(5, gymClass.getNoOfSlots());
    }
    
    @Test
    public void testNumberOfSlotsReducedBy1() throws ParseException {
        GymClass gymclass1 = new GymClass("Yoga", dateFormat.parse("07.08.25"), timeFormat.parse("08:00"), 5, "1 hr",10);
        User testUser = new User(); // create test user

        boolean booked = gymClassService.bookClass(testUser, gymclass1);

        assertTrue(booked, "Booking should be successful");
        assertEquals(4, gymclass1.getNoOfSlots(), "Slots should be reduced by 1");
        assertTrue(testUser.getEnrolledClasses().contains(gymclass1), "User should be enrolled in class");
    }
    
    @Test 
    public void testSameClassEnrollment() {
		GymClass yogaClass = new GymClass("Yoga Basics", new Date(125, 7, 10), // August 10, 2025 (year since 1900)
				new Date(0, 0, 0, 9, 30), // 9:30 AM
				20, "1 hour",1);
		GymClass yogaClass1 = new GymClass("Yoga Basics", new Date(125, 7, 10), // August 10, 2025 (year since 1900)
				new Date(0, 0, 0, 9, 30), // 9:30 AM
				20, "1 hour",1);
		User u = new User();
		u.enrollInClass(yogaClass1);
		assertTrue(u.isSameClass(yogaClass, yogaClass1));
    }
    @Test
	public void enrolledClass() {
		GymClass yogaClass = new GymClass("Yoga Basics", new Date(125, 7, 10), // August 10, 2025 (year since 1900)
				new Date(0, 0, 0, 9, 30), // 9:30 AM
				20, "1 hour",1);
		User u = new User();
		u.enrollInClass(yogaClass);
		assertTrue(u.getEnrolledClasses().contains(yogaClass));

	}
	@Test
	public void isnotenrolledClass() {
		GymClass yogaClass = new GymClass("Yoga Basics", new Date(125, 7, 10), // August 10, 2025 (year since 1900)
				new Date(0, 0, 0, 9, 30), // 9:30 AM
				20, "1 hour",1);
		GymClass yogaClass1 = new GymClass("Kick Boxing", new Date(125, 7, 10), // August 10, 2025 (year since 1900)
				new Date(0, 0, 0, 9, 30), // 9:30 AM
				20, "1 hour",1);
		User u = new User();
		u.enrollInClass(yogaClass);
		assertFalse(u.getEnrolledClasses().contains(yogaClass1));
	}
}

