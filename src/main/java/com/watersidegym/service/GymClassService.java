package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import com.watersidegym.model.GymClass;

public class GymClassService {
	
	private List<GymClass> gymclassList = new ArrayList<>();
	
	
	public GymClassService() {
		 // Dummy data initialization
		gymclassList.add(new GymClass("Yoga", 5));
		gymclassList.add(new GymClass("Pilates", 8));
		gymclassList.add(new GymClass("Tai chi", 10));

	}
	
	public List<GymClass> getAllGymClasses(){
		
		return gymclassList;
	}

}
