package com.watersidegym.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.watersidegym.model.GymClass;
import com.watersidegym.model.User;
import com.watersidegym.service.GymClassService;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

@ManagedBean
@SessionScoped
public class GymClassBean {

	private GymClassService gymClassService;
	private List<GymClass> gymClassList;
	private Date selectedDate;
	private String errorMessage;
	private List<GymClass> filteredClasses = new ArrayList<>();

	public GymClassBean() {
	}

	// Catch any unexpected errors during initialization to prevent app crashes
	// and show a user-friendly error message if loading classes fails.
	@PostConstruct
	public void init() {
		gymClassService = new GymClassService(); // Initialize service here
		try {
			gymClassList = gymClassService.getAllGymClasses();
			filteredClasses = new ArrayList<>(gymClassList); // Start with all classes shown
		} catch (Exception e) {
			gymClassList = new ArrayList<>();
			filteredClasses = new ArrayList<>();
			errorMessage = "Currently unable to load classes. Please try again later.";
		}
	}

	// getter setters

	public GymClassService getGymClassService() {
		return gymClassService;
	}

	public void setGymClassService(GymClassService gymClassService) {
		this.gymClassService = gymClassService;
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public void setGymClassList(List<GymClass> gymClassList) {
		this.gymClassList = gymClassList;
	}

	public List<GymClass> getGymClassList() {
		return gymClassService.getAllGymClasses();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setFilteredClasses(List<GymClass> filteredClasses) {
		this.filteredClasses = filteredClasses;
	}

	public List<GymClass> getFilteredClasses() {
		if (selectedDate == null) {
			return gymClassList; // show all classes by default
		}
		return filteredClasses; // show only filtered results after selection
	}

	// filter for matching dates -> selectedDate

	public String getFilteredClassesAction() {
		filteredClasses = new ArrayList<>();

		if (selectedDate == null) {
			return null; // nothing to filter
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedDate);

		// Reset time part to midnight - mismatches due to time zones
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		for (GymClass gymClass : gymClassList) {

			Calendar classDate = Calendar.getInstance();
			classDate.setTime(gymClass.getClassDate());

			// Reset time part of class date to midnight too
			classDate.set(Calendar.HOUR_OF_DAY, 0);
			classDate.set(Calendar.MINUTE, 0);
			classDate.set(Calendar.SECOND, 0);
			classDate.set(Calendar.MILLISECOND, 0);

			if (classDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
					&& classDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
					&& classDate.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {

				// Match found - add to filtered list
				filteredClasses.add(gymClass);

				// System.out.println("Selected Date: " + selectedDate);
				// System.out.println("Class Date: " + cls.getClassDate());
			}
		}
		return null; // stay on same page
	}

	// Booking logic
	public String bookClass(GymClass cls) {
		FacesContext context = FacesContext.getCurrentInstance();

		// Get LoginBean and logged-in user
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser();

		// manually check if the user already booked this class
		if (loggedInUser.isEnrolledInClass(cls)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Already Booked",
					"You have already booked this class."));
			return null;

		}

		// Check if the class has available slots
		if (cls.getNoOfSlots() <= 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Class Full", "No slots available."));
			return null;
		}

		// Book the class
		cls.setNoOfSlots(cls.getNoOfSlots() - 1); // Reduce available slots
		loggedInUser.enrollInClass(cls);

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Class Booked",
				"You have successfully booked the class."));

		return null; // Stay on the same page
	}

}
