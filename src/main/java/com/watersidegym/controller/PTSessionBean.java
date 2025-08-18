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

	private Date selectedDate;
	private PTSession ptSession = new PTSession();
	private PTSessionService ptSessionService;
	private List<PTSession> sessionList = new ArrayList<>();
	private List<PTSession> filteredSessions = new ArrayList<>();
	private List<User> trainers = new ArrayList<>();
	private String startTime;
	private String selectedTrainerName;

	public PTSessionBean() {
	}

	@PostConstruct
	public void init() {
		setPtSessionService(new PTSessionService());

		// load existing sessions for the logged-in trainer if needed
		User loggedInUser = getLoggedInUser();

		if (loggedInUser != null && "PT".equals(loggedInUser.getRole())) {
			setSessionList(getPtSessionService().getSessionsForTrainer(loggedInUser));
			filteredSessions = new ArrayList<>(getSessionList());
		}

		// populate trainers dropdown
		setTrainers(getPtSessionService().getAllTrainers());
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public PTSession getPtSession() {
		return ptSession;
	}

	public List<PTSession> getSessionList() {
		return sessionList;
	}

	public List<PTSession> getFilteredSessions() {
		if (filteredSessions == null || filteredSessions.isEmpty()) {
			User loggedInUser = getLoggedInUser();
			// if logged-in user is a trainer, load their sessions
			if (loggedInUser != null && "PT".equals(loggedInUser.getRole())) {
				setSessionList(getPtSessionService().getSessionsForTrainer(loggedInUser));
				filteredSessions = new ArrayList<>(getSessionList());
			} else {
				filteredSessions = new ArrayList<>();
			}
		}
		return filteredSessions;
	}

	public List<User> getTrainers() {
		return trainers;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getSelectedTrainerName() {
		return selectedTrainerName;
	}

	public void setSelectedTrainerName(String selectedTrainerName) {
		this.selectedTrainerName = selectedTrainerName;
	}

	// booking logic
	public String bookSession() {
		boolean hasError = false;

		// find the trainer object based on selected string
		if (selectedTrainerName != null && !selectedTrainerName.isEmpty()) {
			String[] parts = selectedTrainerName.split(" ", 2); // split into first and last
			String firstName = parts[0];
			String lastName = "";
			if (parts.length > 1) {
				lastName = parts[1]; // if last name exists, assign it
			}
			ptSession.setTrainer(null);
			for (User t : getTrainers()) {
				// check if first and last names match
				if (t.getFirstName().equals(firstName) && t.getSecondName().equals(lastName)) {
					ptSession.setTrainer(t);
					break;
				}
			}
		}

		// validation checks
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
			return null; // stop if validation fails
		}

		User loggedInUser = getLoggedInUser();

		try {
			boolean sessionOk = getPtSessionService().bookSession(ptSession, loggedInUser);

			if (sessionOk) {
				// reset form fields after successful booking
				ptSession = new PTSession();
				selectedTrainerName = null;

				addMessage(null, FacesMessage.SEVERITY_INFO,
						"Booking confirmed. Remaining 1:1 sessions: " + loggedInUser.getRemainingPTSessions());
				return "member-dashboard?faces-redirect=true";
			} else {
				addMessage(null, FacesMessage.SEVERITY_ERROR, "This session has been already booked. Try another.");
				return null;
			}
		} catch (Exception e) {
			addMessage(null, FacesMessage.SEVERITY_ERROR, "System error: booking failed. Please try again later.");
			return null;
		}
	}

	// logic to get member booked sessions
	public List<PTSession> getMemberBookedSessions() {
		User loggedInUser = getLoggedInUser();

		if (loggedInUser == null) {
			return new ArrayList<>(); // return empty if no user
		}
		return getPtSessionService().getBookedSessionsForMember(loggedInUser);
	}

	// filter logic
	public String filterSessionsByDate() {
		filteredSessions = new ArrayList<>();

		// if no date selected, show nothing
		if (selectedDate == null) {
			return null;
		}

		Calendar calSelected = Calendar.getInstance();
		calSelected.setTime(selectedDate);
		calSelected.set(Calendar.HOUR_OF_DAY, 0);
		calSelected.set(Calendar.MINUTE, 0);
		calSelected.set(Calendar.SECOND, 0);
		calSelected.set(Calendar.MILLISECOND, 0);

		for (PTSession session : getSessionList()) {
			Calendar calSession = Calendar.getInstance();
			calSession.setTime(session.getSessionDate());
			calSession.set(Calendar.HOUR_OF_DAY, 0);
			calSession.set(Calendar.MINUTE, 0);
			calSession.set(Calendar.SECOND, 0);
			calSession.set(Calendar.MILLISECOND, 0);

			// compare year, month, day to match date
			if (calSession.get(Calendar.YEAR) == calSelected.get(Calendar.YEAR)
					&& calSession.get(Calendar.MONTH) == calSelected.get(Calendar.MONTH)
					&& calSession.get(Calendar.DAY_OF_MONTH) == calSelected.get(Calendar.DAY_OF_MONTH)) {
				filteredSessions.add(session);
			}
		}
		return null;
	}

	// utility methods
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
		return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
	}

	private User getLoggedInUser() {
		LoginBean loginBean = currentLoginBean();
		if (loginBean != null) {
			return loginBean.getLoggedInUser();
		}
		return null;
	}

	public PTSessionService getPtSessionService() {
		return ptSessionService;
	}

	public void setPtSessionService(PTSessionService ptSessionService) {
		this.ptSessionService = ptSessionService;
	}

	public void setTrainers(List<User> trainers) {
		this.trainers = trainers;
	}

	public void setSessionList(List<PTSession> sessionList) {
		this.sessionList = sessionList;
	}
}
