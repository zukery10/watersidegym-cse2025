package com.watersidegym.controller;

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
	
	//filter for matching dates -> selectedDate
	
	

}
