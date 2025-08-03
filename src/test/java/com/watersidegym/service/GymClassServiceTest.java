package com.watersidegym.service;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.GymClass;

public class GymClassServiceTest {

    private GymClassService gymClassService;

    private GymClassService service;
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
        // Confirm that 16 gym classes are loaded as hardcoded in the service
        assertEquals(16, gymClassService.getAllGymClasses().size(), "Expected 16 classes loaded");
    }
    
    @Test
    public void tesGymClassValues() throws ParseException {
        GymClass gymClass = service.getAllGymClasses().get(0);
        assertEquals("Yoga", gymClass.getNameOfClass());
        assertEquals(dateFormat.parse("26.08.25"), gymClass.getClassDate());
        assertEquals(timeFormat.parse("08:00"), gymClass.getClassTime());
        assertEquals("1 hr", gymClass.getClassDuration());
        assertEquals(5, gymClass.getNoOfSlots());
    }
}

