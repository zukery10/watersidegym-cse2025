package com.watersidegym.controller;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import com.watersidegym.model.GymClass;
import com.watersidegym.service.GymClassService;

@ManagedBean
@RequestScoped
public class GymClassBean {
	
	private GymClassService service = new GymClassService();
	
	public List<GymClass> getClasses(){
		
		return service.getAllGymClasses();
		
	}

}
