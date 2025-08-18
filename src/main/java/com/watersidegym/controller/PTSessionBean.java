package com.watersidegym.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import com.watersidegym.service.PTSessionService;

@ManagedBean(name = "ptSessionBean")
@SessionScoped
public class PTSessionBean {

	private PTSession ptSession = new PTSession();
	private PTSessionService ptSessionService;
	private List<PTSession> sessionList = new ArrayList<>();
	private Date selectedDate;
	private List<PTSession> filteredSessions = new ArrayList<>();

	public PTSessionBean() {
	}

	@PostConstruct
	public void init() {
		ptSessionService = new PTSessionService();

		// get logged-in trainer from LoginBean
		LoginBean loginBean = currentLoginBean();
		User loggedInUser = (loginBean != null) ? loginBean.getLoggedInUser() : null;

		if (loggedInUser != null) {
			sessionList = ptSessionService.getSessionsForTrainer(loggedInUser);
			filteredSessions = new ArrayList<>(sessionList);
		} else {
			sessionList = new ArrayList<>();
			filteredSessions = new ArrayList<>();
		}
	}

	// --- Booking logic ---
	public String bookSession() {
		boolean hasError = false;

		if (ptSession.getTrainer() == null) {
			addMessage("ptForm:trainer", FacesMessage.SEVERITY_ERROR, "Please choose a trainer.");
			hasError = true;
		}
		if (ptSession.getSessionDate() == null) {
			addMessage("ptForm:date", FacesMessage.SEVERITY_ERROR, "Please select a date");
			hasError = true;
		}
		if (ptSession.getStartTime() == null) {
			addMessage("ptForm:time", FacesMessage.SEVERITY_ERROR, "Please select a time.");
			hasError = true;
		}
		if (ptSession.getDurationMinutes() == 0) {
			addMessage("ptForm:dur", FacesMessage.SEVERITY_ERROR, "Please select a duration.");
			hasError = true;
		}

		if (hasError) {
			return null;
		}

		LoginBean loginBean = currentLoginBean();
		User loggedInUser = (loginBean != null) ? loginBean.getLoggedInUser() : null;

		try {
			boolean sessionOk = ptSessionService.bookSession(ptSession, loggedInUser);

			if (sessionOk) {
				ptSession = new PTSession();
				addMessage(null, FacesMessage.SEVERITY_INFO,
						"Booking confirmed. Remaining 1:1 sessions: " + loggedInUser.getRemainingPTSessions());
				return "member-dashboard?faces-redirect=true";
			} else {
				addMessage(null, FacesMessage.SEVERITY_ERROR,
						"This session has been already booked. Try another.");
				return null;
			}
		} catch (Exception e) {
			addMessage(null, FacesMessage.SEVERITY_ERROR,
					"System error: booking failed. Please try again later.");
			return null;
		}
	}

	public List<PTSession> getMemberBookedSessions() {
		LoginBean loginBean = currentLoginBean();
		User member = (loginBean != null) ? loginBean.getLoggedInUser() : null;
		if (member == null) {
			return new ArrayList<>();
		}
		return ptSessionService.getBookedSessionsForMember(member);
	}

	// --- Filtering logic ---
	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public List<PTSession> getSessionList() {
		return sessionList;
	}

	public List<PTSession> getFilteredSessions() {
		if (selectedDate == null) {
			return sessionList;
		}
		return filteredSessions;
	}

	public String filterSessionsByDate() {
		filteredSessions = new ArrayList<>();

		if (selectedDate == null) {
			return null;
		}

		Calendar calSelected = Calendar.getInstance();
		calSelected.setTime(selectedDate);
		calSelected.set(Calendar.HOUR_OF_DAY, 0);
		calSelected.set(Calendar.MINUTE, 0);
		calSelected.set(Calendar.SECOND, 0);
		calSelected.set(Calendar.MILLISECOND, 0);

		for (PTSession session : sessionList) {
			Calendar calSession = Calendar.getInstance();
			calSession.setTime(session.getSessionDate());
			calSession.set(Calendar.HOUR_OF_DAY, 0);
			calSession.set(Calendar.MINUTE, 0);
			calSession.set(Calendar.SECOND, 0);
			calSession.set(Calendar.MILLISECOND, 0);

			if (calSession.get(Calendar.YEAR) == calSelected.get(Calendar.YEAR)
					&& calSession.get(Calendar.MONTH) == calSelected.get(Calendar.MONTH)
					&& calSession.get(Calendar.DAY_OF_MONTH) == calSelected.get(Calendar.DAY_OF_MONTH)) {
				filteredSessions.add(session);
			}
		}
		return null;
	}

	// --- Utility methods ---
	protected LoginBean currentLoginBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null) {
			return null;
		}
		return context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
	}

	protected void addMessage(String clientId, FacesMessage.Severity severity, String text) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ctx != null) {
			ctx.addMessage(clientId, new FacesMessage(severity, text, null));
		}
	}

	public String getTodayIso() {
		return new java.text.SimpleDateFormat("yyyy-MM-dd")
				.format(new java.util.Date());
	}

	public PTSession getPtSession() {
		return ptSession;
	}

	public void setPtSession(PTSession ptSession) {
		this.ptSession = ptSession;
	}
}
