package com.watersidegym.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import com.watersidegym.model.GymClass;
import com.watersidegym.service.GymClassService;

@ManagedBean(name="gymClassBean")
@RequestScoped
public class GymClassBean {

	private GymClassService service = new GymClassService();
	private List<GymClass> gymClassList ;
	private Date selectedDate;
	private List<GymClass> filteredClasses = new ArrayList<>();

	
	public GymClassBean() {
		gymClassList = new GymClassService().getAllGymClasses();
	}

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

	public List<GymClass> getFilteredClasses() {
		return filteredClasses;
	}
	
	//filter for matching dates -> selectedDate


	public void setFilteredClasses(List<GymClass> filteredClasses) {
		this.filteredClasses = filteredClasses;
	}

	public String getFilteredClassesAction(){

		if (selectedDate == null) {

			filteredClasses=		new ArrayList<>();
			return null;
		}

		filteredClasses = new ArrayList<>();
		Calendar sel = Calendar.getInstance();
		sel.setTime(selectedDate);

		for(GymClass cls : gymClassList) {

			Calendar clsDate = Calendar.getInstance();
			clsDate.setTime(cls.getClassDate());

			if(clsDate.get(Calendar.YEAR) == sel.get(Calendar.YEAR) &&
					clsDate.get(Calendar.MONTH) == sel.get(Calendar.MONTH) &&
					clsDate.get(Calendar.DAY_OF_MONTH) == sel.get(Calendar.DAY_OF_MONTH)){
				filteredClasses.add(cls);

			}
		}

		return null;

	}

}





















