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

	public String book() {

		boolean hasError = false;
		FacesContext context = FacesContext.getCurrentInstance();

		if (ptSession.getTrainerEmail() == null || ptSession.getTrainerEmail().isEmpty()) {
			context.addMessage("ptForm:trainer", new FacesMessage("Please choose a trainer."));
			hasError = true;

		}

		if (ptSession.getSessionDate() == null) {
			context.addMessage("ptForm:date", new FacesMessage("Please select a date"));
			hasError = true;

		}

		if (ptSession.getSessionTime() == null || ptSession.getSessionTime().isEmpty()) {
			context.addMessage("ptForm:time", new FacesMessage("Please select a time."));
			hasError = true;

		}

		if (ptSession.getDuration() == null || ptSession.getDuration().isEmpty()) {
			context.addMessage("ptForm:dur", new FacesMessage("Please select a duration."));
			hasError = true;

		}

		if (hasError) {
			return null; // if error - stay on the same page
		}

		// get LoginBean and logged-in user
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser();

		boolean sessionOk = ptSessionService.book(ptSession, loggedInUser);

		if (sessionOk) {
			context.addMessage(null, new FacesMessage(
					"Booking confirmed. Remaining 1:1 sessions: " + loggedInUser.getRemainingPTSessions()));
			return "member-dashboard?faces-redirect=true";
		} else {
			context.addMessage(null, new FacesMessage("Booking failed: no sessions left or invalid details."));
			return null;
		}

	}
	
	public List<PTSession> getMemberBookedSessions() {
	    FacesContext ctx = FacesContext.getCurrentInstance();
	    User member = (User) ctx.getExternalContext().getSessionMap().get("loggedInUser");

	    if (member == null) {
	        return new ArrayList<>();
	    }

	    String fullName = member.getFirstName() + " " + member.getSecondName();
	    return ptSessionService.getBookedSessionsForMember(fullName);
	}


}
