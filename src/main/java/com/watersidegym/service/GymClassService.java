package com.watersidegym.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.watersidegym.model.GymClass;

public class GymClassService {

    private static List<GymClass> gymclassList = new ArrayList<>();

    public GymClassService() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            // Format: Date → Time → Name → Slots
            gymclassList.add(new GymClass(dateFormat.parse("26.08.25"), timeFormat.parse("08:00"), "Yoga", 5));
            gymclassList.add(new GymClass(dateFormat.parse("26.08.25"), timeFormat.parse("10:00"), "Pilates", 8));
            gymclassList.add(new GymClass(dateFormat.parse("26.08.25"), timeFormat.parse("14:00"), "Tai chi", 10));
            gymclassList.add(new GymClass(dateFormat.parse("27.08.25"), timeFormat.parse("09:00"), "Zumba", 3));
            gymclassList.add(new GymClass(dateFormat.parse("27.08.25"), timeFormat.parse("11:00"), "Yoga", 7));
            gymclassList.add(new GymClass(dateFormat.parse("28.08.25"), timeFormat.parse("15:00"), "Pilates", 9));
            gymclassList.add(new GymClass(dateFormat.parse("28.08.25"), timeFormat.parse("17:00"), "Kickboxing", 5));
            gymclassList.add(new GymClass(dateFormat.parse("29.08.25"), timeFormat.parse("08:30"), "Spinning", 6));
            gymclassList.add(new GymClass(dateFormat.parse("29.08.25"), timeFormat.parse("10:30"), "HIIT", 8));
            gymclassList.add(new GymClass(dateFormat.parse("30.08.25"), timeFormat.parse("12:00"), "CrossFit", 2));
            gymclassList.add(new GymClass(dateFormat.parse("30.08.25"), timeFormat.parse("14:00"), "Aqua Aerobics", 4));
            gymclassList.add(new GymClass(dateFormat.parse("31.08.25"), timeFormat.parse("16:00"), "Dance", 10));
            gymclassList.add(new GymClass(dateFormat.parse("31.08.25"), timeFormat.parse("18:00"), "Strength Training", 1));
            gymclassList.add(new GymClass(dateFormat.parse("31.08.25"), timeFormat.parse("09:30"), "Tabata", 6));
            gymclassList.add(new GymClass(dateFormat.parse("31.08.25"), timeFormat.parse("11:30"), "Step Aerobics", 7));
            gymclassList.add(new GymClass(dateFormat.parse("31.08.25"), timeFormat.parse("13:30"), "Core Blast", 3));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<GymClass> getAllGymClasses() {
        return gymclassList;
    }
}
