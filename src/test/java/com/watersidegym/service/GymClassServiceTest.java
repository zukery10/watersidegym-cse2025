package com.watersidegym.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.GymClass;

public class GymClassServiceTest {

    private GymClassService service;

    @BeforeEach
    public void setup() {
        // Create new service instance before each test
        service = new GymClassService();
    }

    @Test
    public void testClassListNotEmpty() {
        // Ensure the service returns a non-empty list of gym classes
        List<GymClass> list = service.getAllGymClasses();
        assertNotNull(list, "Class list should not be null");
        assertFalse(list.isEmpty(), "Class list should not be empty");
    }

    @Test
    public void testEachClassHasValidNameAndDate() {
        // Loop through all classes and verify that necessary fields are set
        for (GymClass cls : service.getAllGymClasses()) {
            assertNotNull(cls.getNameOfClass(), "Class name should not be null");
            assertNotNull(cls.getClassDate(), "Class date should not be null");
            assertNotNull(cls.getClassTime(), "Class time should not be null");
        }
    }

    @Test
    public void testNumberOfClassesLoaded() {
        // Confirm that 16 gym classes are loaded as hardcoded in the service
        assertEquals(16, service.getAllGymClasses().size(), "Expected 16 classes loaded");
    }
}

