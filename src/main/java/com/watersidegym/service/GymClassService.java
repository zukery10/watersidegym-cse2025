package com.watersidegym.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.watersidegym.model.GymClass;

public class GymClassService {

    private List<GymClass> gymclassList = new ArrayList<>();

    public GymClassService() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            // Format: Date → Time → Name → Slots
        	gymclassList.add(new GymClass("Yoga", dateFormat.parse("26.08.25"), timeFormat.parse("08:00"), 5));
        	gymclassList.add(new GymClass("Pilates", dateFormat.parse("26.08.25"), timeFormat.parse("10:00"), 8));
        	gymclassList.add(new GymClass("Tai chi", dateFormat.parse("26.08.25"), timeFormat.parse("14:00"), 10));
        	gymclassList.add(new GymClass("Zumba", dateFormat.parse("27.08.25"), timeFormat.parse("09:00"), 3));
        	gymclassList.add(new GymClass("Yoga", dateFormat.parse("27.08.25"), timeFormat.parse("11:00"), 7));
        	gymclassList.add(new GymClass("Pilates", dateFormat.parse("28.08.25"), timeFormat.parse("15:00"), 9));
        	gymclassList.add(new GymClass("Kickboxing", dateFormat.parse("28.08.25"), timeFormat.parse("17:00"), 5));
        	gymclassList.add(new GymClass("Spinning", dateFormat.parse("29.08.25"), timeFormat.parse("08:30"), 6));
        	gymclassList.add(new GymClass("HIIT", dateFormat.parse("29.08.25"), timeFormat.parse("10:30"), 8));
        	gymclassList.add(new GymClass("CrossFit", dateFormat.parse("30.08.25"), timeFormat.parse("12:00"), 2));
        	gymclassList.add(new GymClass("Aqua Aerobics", dateFormat.parse("30.08.25"), timeFormat.parse("14:00"), 4));
        	gymclassList.add(new GymClass("Dance", dateFormat.parse("31.08.25"), timeFormat.parse("16:00"), 10));
        	gymclassList.add(new GymClass("Strength Training", dateFormat.parse("31.08.25"), timeFormat.parse("18:00"), 1));
        	gymclassList.add(new GymClass("Tabata", dateFormat.parse("31.08.25"), timeFormat.parse("09:30"), 6));
        	gymclassList.add(new GymClass("Step Aerobics", dateFormat.parse("31.08.25"), timeFormat.parse("11:30"), 7));
        	gymclassList.add(new GymClass("Core Blast", dateFormat.parse("31.08.25"), timeFormat.parse("13:30"), 3));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public List<GymClass> getAllGymClasses() {
        return gymclassList;
    }
}
