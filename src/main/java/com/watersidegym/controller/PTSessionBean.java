package com.watersidegym.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import com.watersidegym.service.PTSessionService;

@ManagedBean(name = "ptSessionBean")
@SessionScoped
public class PTSessionBean {

	private PTSessionService ptSessionService;
	private List<PTSession> sessionList;
	private Date selectedDate;
	private List<PTSession> filteredSessions = new ArrayList<>();

	public PTSessionBean() {
	}

	@PostConstruct
	public void init() {
		ptSessionService = new PTSessionService();

		// get logged-in trainer from LoginBean
		FacesContext context = FacesContext.getCurrentInstance();
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser();

		if (loggedInUser != null) {
			sessionList = ptSessionService.getSessionsForTrainer(loggedInUser);
			filteredSessions = new ArrayList<>(sessionList);
		} else {
			sessionList = new ArrayList<>();
			filteredSessions = new ArrayList<>();
		}
	}

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
			return sessionList; // default: show all
		}
		return filteredSessions;
	}

	public String filterSessionsByDate() {
		filteredSessions = new ArrayList<>();

		if (selectedDate == null) {
			return null; // no filter applied
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
		return null; // stay on page
	}
}
