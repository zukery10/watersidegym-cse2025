package com.watersidegym.controller;

	import com.watersidegym.model.PTSession;
	import com.watersidegym.model.User;
	import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

	import static org.junit.jupiter.api.Assertions.*;

	class PTSessionBeanTest {

		// Creates a PTSessionBean for tests - 
		//so we don't need JSF/EL for loginbean and skips FacesContext - for Faces messages 
		private PTSessionBean newBeanFor(final User u) {
		    return new PTSessionBean() {

		    	// Return - gives back the test user.(fake loginbean)
		        @Override
		        protected LoginBean currentLoginBean() {
		            return new LoginBean() {
		                @Override
		                public User getLoggedInUser() {
		                    return u;
		                }
		            };
		        }
		        //Ignores Faces messages 
		        @Override
		        protected void addMessage(String clientId,
		                                  javax.faces.application.FacesMessage.Severity severity,
		                                  String text) {
		         }
		    };
		}

		//null case - missing field
	    @Test
	    void returnsNullWhenRequiredFieldsMissing() {
	        User loggedIn = new User();
	        loggedIn.setFirstName("Alex");
	        loggedIn.setSecondName("Byrne");
	        loggedIn.setRemainingPTSessions(2);

	        PTSessionBean bean = newBeanFor(loggedIn);
	        bean.init(); 

	        String outcome = bean.bookSession(); // no session details

	        assertNull(outcome);
	    }

	    //success case - decrement, form reset and redirect
	    @Test
	    void returnsRedirectAndResetsFormAndDecrementsCount() {
	        User loggedIn = new User();
	        loggedIn.setFirstName("Alex");
	        loggedIn.setSecondName("Byrne");
	        loggedIn.setRemainingPTSessions(2); //2 sessions

	        PTSessionBean bean = newBeanFor(loggedIn);
	        bean.init();

	        PTSession form = bean.getPtSession();
	        form.setTrainerName("Priya Shah");
	        form.setSessionDate(new Date());
	        form.setSessionTime("17:00");
	        form.setDuration("60 min");

	        String outcome = bean.bookSession();

	        assertEquals("member-dashboard?faces-redirect=true", outcome); //redirect
	        assertNull(bean.getPtSession().getTrainerName());   // form reset
	        assertEquals(1, loggedIn.getRemainingPTSessions()); // decremented to 1
	    }

	    //0 sessions check
	    @Test
	    void returnsNullWhenNoSessionsLeft() {
	        User loggedIn = new User();
	        loggedIn.setFirstName("Alex");
	        loggedIn.setSecondName("Byrne");
	        loggedIn.setRemainingPTSessions(0);

	        PTSessionBean bean = newBeanFor(loggedIn);
	        bean.init();

	        PTSession form = bean.getPtSession();
	        form.setTrainerName("Joe");
	        form.setSessionDate(new Date());
	        form.setSessionTime("08:00");
	        form.setDuration("30 min");

	        String outcome = bean.bookSession();

	        assertNull(outcome);
	        assertEquals(0, loggedIn.getRemainingPTSessions());
	    }
	    
	    //date check
	    @Test
	    void getTodayIso_returnsTodayInIsoFormat() {
	        PTSessionBean bean = newBeanFor(null);
	       
	        String actual = bean.getTodayIso();

	        String expected = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        
	        assertEquals(expected, actual);
	    }
	}

