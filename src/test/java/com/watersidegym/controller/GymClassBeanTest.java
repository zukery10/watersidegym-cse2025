package com.watersidegym.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.watersidegym.model.GymClass;
import com.watersidegym.model.User;
import com.watersidegym.service.GymClassService;

public class GymClassBeanTest {

	private GymClassBean bean;
	private SimpleDateFormat dateFormat;
	private GymClass yoga;
	private User user;
	private MemberShipBean msb;

	@BeforeEach
	public void setUp() throws Exception {
		dateFormat = new SimpleDateFormat("dd.MM.yy");
		bean = new GymClassBean() {
			@Override
			protected void addMessage(String msg, FacesMessage.Severity severity) {
				// skip messages for testing
			}
			@Override
			public boolean isMember() {
				return false; // pretend always non-member for testing
			}
		};
		bean.setGymClassService(new GymClassService());
		bean.setGymClassList(bean.getGymClassService().getAllGymClasses());

		yoga = bean.getGymClassService().getAllGymClasses().get(0);
		user = new User();
	};




	// Matching date
	@Test
	public void testFilterWithMatchingDate() throws Exception {
		Date matchDate = new Date();
		matchDate = dateFormat.parse("07.08.25");
		bean.setSelectedDate(matchDate);

		bean.getFilteredClassesAction();  // triggers filtering

		List<GymClass> result = bean.getFilteredClasses();
		assertFalse(result.isEmpty(), "Expected matching classes for date");
	}

	// Non-matching date
	@Test
	public void testFilterWithNonMatchingDate() throws Exception {
		List<GymClass> result = new ArrayList<>();
		Date nonMatchDate = dateFormat.parse("01.09.25"); // no matching classes
		bean.setSelectedDate(nonMatchDate);

		bean.getFilteredClassesAction();

		result = bean.getFilteredClasses();
		assertTrue(result.isEmpty());
	}

	// Null date
	@Test
	public void testFilterWhenDateIsNull() {
		List<GymClass> result = new ArrayList<>();
		bean.setSelectedDate(null);  //sets date to null 

		result = bean.getFilteredClasses(); //gets the size of the filtered class (18)
		int x = result.size(); //set the int
		assertEquals(18,x);
		
	}


	// Data retrieval test
	@Test
	public void testConstructorHandlesDataLoading() {
		assertNotNull(bean.getGymClassList()); //check it is not null
		assertTrue(bean.getGymClassList().size() > 0); //checks theres gym class's
	}

	@Test
	public void testAddAndRemoveFromCart() {
		assertTrue(bean.getCart().isEmpty()); 
		bean.addToCart(yoga);
		assertEquals(1, bean.getCart().size());
		assertTrue(bean.buttonDisabled(yoga));
		assertEquals(yoga.getPrice(), bean.getTotalPrice());

		bean.removeItemFromCart(yoga);
		assertTrue(bean.getCart().isEmpty());
		assertEquals(0, bean.getTotalPrice());
		assertFalse(bean.buttonDisabled(yoga));
	}

	@Test
	public void testPaymentTimerAndTimeout() {
		bean.startClassPaymentTimer();
		assertFalse(bean.isClassPaymentTimedOut());

		// force timeout
		bean.startClassPaymentTimer();
		bean.setClassPaymentDeadline(System.currentTimeMillis() - 1000);
		assertTrue(bean.isClassPaymentTimedOut());
	}

	@Test
	public void testGetClassPaymentRemainingTime() {
		bean.startClassPaymentTimer();
		String remaining = bean.getClassPaymentRemainingTime();
		assertNotNull(remaining);
		assertTrue(remaining.contains(":"));
	}

	@Test
	public void testResetNonMemberCartAndTimer() {
		bean.addToCart(yoga);
		assertFalse(bean.getCart().isEmpty());

		bean.resetNonMemberCartAndTimer();
		assertTrue(bean.getCart().isEmpty());
		assertEquals(0, bean.getTotalPrice());
	}

	@Test
	public void testResetNonMemberCartAndTimerAndRedirect() {
		bean.addToCart(yoga);
		String page = bean.resetNonMemberCartAndTimerAndRedirect();
		assertEquals("nonmember-dashboard.xhtml?faces-redirect=true", page);
	}

	@Test
	public void testToPaymentAndRedirectPage() {
		String page = bean.toPayment();
		assertEquals("payment.xhtml?faces-redirect=true", page);
		assertTrue(bean.isClassPayment());

		// redirect page depends on flag
		assertEquals("nonmember-dashboard.xhtml", bean.getRedirectPage());

		bean.setClassPayment(false);
		assertEquals("member-dashboard.xhtml", bean.getRedirectPage());
	}
	
	@Test
	public void testIsCartEmpty () {
		int size = bean.getCart().size();
		assertEquals(0,size);
		assertTrue(bean.isCartEmpty());
	}
	
	@Test 
	public void testShowPice() {
		user.setRole("Non-Member");
		assertTrue(bean.showPrice());
		
	}
	@Test
	public void testTimerNotStarted() {
		assertEquals("",bean.getClassPaymentRemainingTime() );
		bean.startClassPaymentTimer();
		int size = bean.getCart().size();
		assertEquals(0,size);
	}
	@Test
	public void testTimerStarted() {
		bean.startClassPaymentTimer();
		assertTrue(!bean.getClassPaymentRemainingTime().equals("") );
		
		bean.addToCart(yoga);
		int size = bean.getCart().size();
		assertEquals(1,size);
	}

	@Test
	public void testInit() {
		assertNotNull(bean.getGymClassService());
		assertNotNull(bean.getCart());
	}
	@Test
	public void testisClassPaymentTimedOut() {
		bean.setClassPaymentDeadline(-1);
		assertFalse(bean.isClassPayment());	
	}
//	@Test
//	public void testIsValidPayPalCredentials() {
//		msb = new MemberShipBean();
//		String username = "user@example.com";
//		String password = "password";
//		msb.setPaypalEmail(username);
//		msb.setPaypalPassword(password);
//		bean.isValidPayPalCredentials(msb);
//	}
	@Test
	public void testBackToCart() {
	bean.setClassPayment(true);	
	String result = bean.returnToCart();
	assertEquals("browseClasses.xhtml",result);	
	bean.addToCart(yoga);
	int size = bean.getCart().size();
	assertEquals(1,size);	
	}
			
}

