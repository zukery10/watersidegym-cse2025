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

		if (ptSession.getTrainerName() == null || ptSession.getTrainerName().isEmpty()) {
		    context.addMessage("ptForm:trainer",
		        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please choose a trainer.", null));
		    hasError = true;
		}


		if (ptSession.getSessionDate() == null) {
			context.addMessage("ptForm:date", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please select a date", null));
			hasError = true;

		}

		if (ptSession.getSessionTime() == null || ptSession.getSessionTime().isEmpty()) {
			context.addMessage("ptForm:time", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please select a time.", null));
			hasError = true;

		}

		if (ptSession.getDuration() == null || ptSession.getDuration().isEmpty()) {
			context.addMessage("ptForm:dur", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please select a duration.", null));
			hasError = true;

		}

		if (hasError) {
			return null; // if error - stay on the same page
		}

		// get LoginBean and logged-in user
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser();

		try {
		boolean sessionOk = ptSessionService.book(ptSession, loggedInUser);

		if (sessionOk) {
			 ptSession = new PTSession();
			context.addMessage(null, new FacesMessage(
					"Booking confirmed. Remaining 1:1 sessions: " + loggedInUser.getRemainingPTSessions()));
			return "member-dashboard?faces-redirect=true";
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Booking failed: no sessions left or invalid details.", null));
			return null;
		}
		}catch (Exception e) {
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "System error: booking failed. Please try again later.", null));
	        return null;
	    }

	}
	
	public List<PTSession> getMemberBookedSessions() {
		FacesContext ctx = FacesContext.getCurrentInstance();
	    LoginBean loginBean = ctx.getApplication()
	            .evaluateExpressionGet(ctx, "#{loginBean}", LoginBean.class);

	    User member = (loginBean != null) ? loginBean.getLoggedInUser() : null;
	    if (member == null) {
	        return new ArrayList<>();
	    }

	    String fullName = member.getFirstName() + " " + member.getSecondName();
	    return ptSessionService.getBookedSessionsForMember(fullName);
	}


}
