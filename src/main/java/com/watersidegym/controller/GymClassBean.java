package com.watersidegym.controller;

import java.io.Serializable;  
import java.util.*;           

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.watersidegym.model.GymClass;
import com.watersidegym.service.GymClassService;

@ManagedBean(name = "gymClassBean")
@SessionScoped
public class GymClassBean implements Serializable {

	private static final long serialVersionUID = 1L;


	private GymClassService service = new GymClassService();
	private List<GymClass> gymClassList ;
	private Date selectedDate;
	private String errorMessage;
	private List<GymClass> filteredClasses = new ArrayList<>();


	public GymClassBean() {
		try {
			gymClassList = new GymClassService().getAllGymClasses();
		} catch (Exception e) {
			gymClassList = new ArrayList<>(); // fallback to empty
			System.err.println("Failed to load classes: " + e.getMessage());
			errorMessage = "Currently unable to load classes. Please try again later.";
		}}

	//getter setters
	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public void setGymClassList(List<GymClass> gymClassList) {
		this.gymClassList = gymClassList;
	}

	public List<GymClass> getGymClassList(){
		return service.getAllGymClasses();
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
		return filteredClasses;  // show only filtered results after selection
	}

	//filter for matching dates -> selectedDate

	public String getFilteredClassesAction() {
		filteredClasses = new ArrayList<>();

		if (selectedDate == null) {
			return null; // nothing to filter
		}

		Calendar sel = Calendar.getInstance();
		sel.setTime(selectedDate);

		// Reset time part to midnight - mismatches due to time zones
		sel.set(Calendar.HOUR_OF_DAY, 0);
		sel.set(Calendar.MINUTE, 0);
		sel.set(Calendar.SECOND, 0);
		sel.set(Calendar.MILLISECOND, 0);


		for (GymClass cls : gymClassList) {

			Calendar clsDate = Calendar.getInstance();
			clsDate.setTime(cls.getClassDate());

			// Reset time part of class date to midnight too
			clsDate.set(Calendar.HOUR_OF_DAY, 0);
			clsDate.set(Calendar.MINUTE, 0);
			clsDate.set(Calendar.SECOND, 0);
			clsDate.set(Calendar.MILLISECOND, 0);

			if (clsDate.get(Calendar.YEAR) == sel.get(Calendar.YEAR) &&
					clsDate.get(Calendar.MONTH) == sel.get(Calendar.MONTH) &&
					clsDate.get(Calendar.DAY_OF_MONTH) == sel.get(Calendar.DAY_OF_MONTH)) {

				// Match found - add to filtered list
				filteredClasses.add(cls);

				//	 System.out.println("Selected Date: " + selectedDate);
				//   System.out.println("Class Date: " + cls.getClassDate());
			}


		}




		return null; // stay on same page

	}


}


















