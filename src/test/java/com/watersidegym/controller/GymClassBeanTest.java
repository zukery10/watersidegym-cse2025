package com.watersidegym.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.model.GymClass;

public class GymClassBeanTest {

	
	private GymClassBean bean;
	private SimpleDateFormat dateFormat;
	
	
	@BeforeEach
	public void setUp() {
		
		bean =new GymClassBean();
		dateFormat = new SimpleDateFormat("dd.MM.yy");
	}
	
	//matching date
	@Test
	public void testFilterWithMatchingDate() throws Exception{
			Date matchDate = dateFormat.parse("26.08.25");
	        bean.setSelectedDate(matchDate);

	        bean.getFilteredClassesAction();  // triggers the filtering

	        List<GymClass> result = bean.getFilteredClasses();
	        assertFalse(result.isEmpty());
			
	}
	//not matching
	@Test
    public void testFilterWithNonMatchingDate() throws Exception {
        Date nonMatchDate = dateFormat.parse("01.09.25"); // mismatch date
        bean.setSelectedDate(nonMatchDate);

        bean.getFilteredClassesAction();

        List<GymClass> result = bean.getFilteredClasses();
        assertTrue(result.isEmpty(), "Expected no classes but got some.");
    }
	
	//null date
	 @Test
	    public void testFilterWhenDateIsNull() {
	        bean.setSelectedDate(null);

	        List<GymClass> result = bean.getFilteredClasses();
	        assertFalse(result.isEmpty(), "Expected full list when no date is selected.");
	    }
	
	 //data retrieval
	 @Test
	    public void testConstructorHandlesDataLoading() {
	        assertNotNull(bean.getGymClassList(), "Gym class list should be initialized.");
	        assertTrue(bean.getGymClassList().size() > 0, "Expected preloaded class list.");
	    }
}
