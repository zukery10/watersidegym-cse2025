package com.watersidegym.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.watersidegym.model.GymClass;
import com.watersidegym.model.User;

public class GymClassService {

	private List<GymClass> gymclassList;
	
	public GymClassService() {
		gymclassList = new ArrayList<>();
		init();
	}

	public void init() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		// We use try-catch here because parsing dates with SimpleDateFormat.parse()
		// throws a checked ParseException that must be handled or declared.

		try {
			// Format: Date → Time → Name → Slots
			gymclassList.add(new GymClass("Yoga", dateFormat.parse("07.08.25"), timeFormat.parse("08:00"), 5, "1 hr", 10));
			gymclassList
			.add(new GymClass("Pilates", dateFormat.parse("07.08.25"), timeFormat.parse("10:00"), 8, "45 min",19));
			gymclassList.add(new GymClass("Tai chi", dateFormat.parse("07.08.25"), timeFormat.parse("14:00"), 10,
					"1 hr 15 min",11));

			gymclassList
			.add(new GymClass("Zumba", dateFormat.parse("08.08.25"), timeFormat.parse("09:00"), 3, "50 min",10));
			gymclassList.add(new GymClass("Yoga", dateFormat.parse("08.08.25"), timeFormat.parse("11:00"), 7, "1 hr",20));

			gymclassList
			.add(new GymClass("Pilates", dateFormat.parse("09.08.25"), timeFormat.parse("15:00"), 9, "40 min",12));
			gymclassList.add(
					new GymClass("Kickboxing", dateFormat.parse("09.08.25"), timeFormat.parse("17:00"), 5, "1 hr",4));

			gymclassList.add(
					new GymClass("Spinning", dateFormat.parse("10.08.25"), timeFormat.parse("08:30"), 6, "35 min",10));
			gymclassList
			.add(new GymClass("HIIT", dateFormat.parse("10.08.25"), timeFormat.parse("10:30"), 8, "45 min",10));

			gymclassList
			.add(new GymClass("CrossFit", dateFormat.parse("11.08.25"), timeFormat.parse("12:00"), 1, "1 hr",10));
			gymclassList.add(new GymClass("Aqua Aerobics", dateFormat.parse("11.08.25"), timeFormat.parse("14:00"), 4,
					"50 min",10));

			gymclassList.add(
					new GymClass("Dance", dateFormat.parse("12.08.25"), timeFormat.parse("16:00"), 0, "1 hr 30 min",10));
			gymclassList.add(new GymClass("Strength Training", dateFormat.parse("12.08.25"), timeFormat.parse("18:00"),
					1, "1 hr",10));

			gymclassList
			.add(new GymClass("Tabata", dateFormat.parse("13.08.25"), timeFormat.parse("09:30"), 6, "30 min",10));
			gymclassList.add(new GymClass("Step Aerobics", dateFormat.parse("13.08.25"), timeFormat.parse("11:30"), 7,
					"45 min",17));
			gymclassList.add(
					new GymClass("Core Blast", dateFormat.parse("13.08.25"), timeFormat.parse("13:30"), 3, "40 min",17));

			gymclassList
			.add(new GymClass("Zumba", dateFormat.parse("14.08.25"), timeFormat.parse("08:00"), 5, "50 min",6));
			gymclassList
			.add(new GymClass("HIIT", dateFormat.parse("14.08.25"), timeFormat.parse("10:00"), 6, "45 min",14));

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}


	public List<GymClass> getAllGymClasses() {
		return gymclassList;
	}



	//Book a class by reducing available slots. Returns true/false
	public boolean bookClass(User user, GymClass cls) {
		if (user == null || cls == null) {
			return false;
		}

		// Already enrolled
		if (user.isEnrolledInClass(cls)) {
			return false;
		}

		// Class full
		if (cls.getNoOfSlots() <= 0) {
			return false;
		}
		
		if(user.getRole().equals("Member")) {
		// Reduce slots and enroll
		cls.setNoOfSlots(cls.getNoOfSlots() - 1);
		user.enrollInClass(cls);
		return true;
		} 
		
		if(user.getRole().equals("Non-Member")) {
			return true;
		}
		

		return false;
		
	}
	



}
