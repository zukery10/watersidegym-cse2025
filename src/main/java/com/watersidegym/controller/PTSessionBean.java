package com.watersidegym.controller;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import com.watersidegym.service.PTSessionService;

@ManagedBean(name = "ptSessionBean")
@SessionScoped
public class PTSessionBean {

	PTSession ptSession = new PTSession();
	PTSessionService ptSessionService;

	@PostConstruct
	void init() {
		ptSessionService = new PTSessionService();

	}

	public PTSession getPtSession() {
		return ptSession;
	}

	public void setPtSession(PTSession ptSession) {
		this.ptSession = ptSession;
	}

	//handles the book button and calls service to book
	public String bookSession() {

		boolean hasError = false;

		//validation messages
		if (ptSession.getTrainerName() == null || ptSession.getTrainerName().isEmpty()) {
		    addMessage("ptForm:trainer", FacesMessage.SEVERITY_ERROR, "Please choose a trainer.");
		    hasError = true;
		}

		if (ptSession.getSessionDate() == null) {
		    addMessage("ptForm:date", FacesMessage.SEVERITY_ERROR, "Please select a date");
		    hasError = true;
		}

		if (ptSession.getSessionTime() == null || ptSession.getSessionTime().isEmpty()) {
		    addMessage("ptForm:time", FacesMessage.SEVERITY_ERROR, "Please select a time.");
		    hasError = true;
		}

		if (ptSession.getDuration() == null || ptSession.getDuration().isEmpty()) {
		    addMessage("ptForm:dur", FacesMessage.SEVERITY_ERROR, "Please select a duration.");
		    hasError = true;
		}


		if (hasError) {
			return null; // if error - stay on the same page
		}

		// get LoginBean and logged-in user
		LoginBean loginBean = currentLoginBean();
		User loggedInUser = loginBean.getLoggedInUser();

		//calls servise class book method and failure check.
		try {
		    boolean sessionOk = ptSessionService.bookSession(ptSession, loggedInUser);

		    if (sessionOk) {
		        ptSession = new PTSession();
		        addMessage(null, FacesMessage.SEVERITY_INFO,
		                "Booking confirmed. Remaining 1:1 sessions: " + loggedInUser.getRemainingPTSessions());
		        return "member-dashboard?faces-redirect=true";
		    } else {
		        addMessage(null, FacesMessage.SEVERITY_ERROR,
		                "Booking failed: no sessions left or invalid details.");
		        return null;
		    }
		} catch (Exception e) {
		    addMessage(null, FacesMessage.SEVERITY_ERROR,
		            "System error: booking failed. Please try again later.");
		    return null;
		}

	}
	//returns member’s booked sessions- linked to service	
	public List<PTSession> getMemberBookedSessions() {

		LoginBean loginBean = currentLoginBean();

		User member = (loginBean != null) ? loginBean.getLoggedInUser() : null;
		if (member == null) {
			return new ArrayList<>();
		}

		String fullName = member.getFirstName() + " " + member.getSecondName();
		return ptSessionService.getBookedSessionsForMember(fullName);
	}

	//calling login bean- whenever need to use loginBean object
	protected LoginBean currentLoginBean() {
		javax.faces.context.FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
		if (context == null) {
			return null;
		} else {
			javax.faces.application.Application app = context.getApplication();
			LoginBean loginBean = app.evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);

			return loginBean;
		}
	}

	//Faces context - reusable 
	protected void addMessage(String clientId,
			javax.faces.application.FacesMessage.Severity severity,
			String text) {
		javax.faces.context.FacesContext ctx = javax.faces.context.FacesContext.getCurrentInstance();
		if (ctx != null) { 
			ctx.addMessage(clientId, new javax.faces.application.FacesMessage(severity, text, null));
		}

	}
}
