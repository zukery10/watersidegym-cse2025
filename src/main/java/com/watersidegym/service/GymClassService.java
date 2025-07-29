package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import com.watersidegym.model.GymClass;

public class GymClassService {
	
	private List<GymClass> gymclassList = new ArrayList<>();
	
	
	public GymClassService() {
		 // Dummy data initialisation
		gymclassList.add(new GymClass("Yoga", 5));
		gymclassList.add(new GymClass("Pilates", 8));
		gymclassList.add(new GymClass("Tai chi", 10));
		gymclassList.add(new GymClass("Tai chi", 10));
		gymclassList.add(new GymClass("Yoga", 7));
		gymclassList.add(new GymClass("Zumba", 3));
		gymclassList.add(new GymClass("Pilates", 9));
		gymclassList.add(new GymClass("Kickboxing", 5));
		gymclassList.add(new GymClass("Spinning", 6));
		gymclassList.add(new GymClass("HIIT", 8));
		gymclassList.add(new GymClass("CrossFit", 2));
		gymclassList.add(new GymClass("Aqua Aerobics", 4));
		gymclassList.add(new GymClass("Dance", 10));
		gymclassList.add(new GymClass("Strength Training", 1));
		gymclassList.add(new GymClass("Tabata", 6));
		gymclassList.add(new GymClass("Step Aerobics", 7));
		gymclassList.add(new GymClass("Core Blast", 3));

	}
	
	public List<GymClass> getAllGymClasses(){
		
		return gymclassList;
	}

}
