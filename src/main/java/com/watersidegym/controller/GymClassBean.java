package com.watersidegym.controller;

import java.util.List;

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import com.watersidegym.model.GymClass;
import com.watersidegym.service.GymClassService;

@ManagedBean
@SessionScoped
public class GymClassBean {
	
	private GymClassService service = new GymClassService();
	
	public List<GymClass> getGymClassList(){
		
		return service.getAllGymClasses();
		
	}

}
