package com.watersidegym.model;

import java.util.Date;

public class GymClass  {

	private String nameOfClass;
	private int noOfSlots;
	private Date classDate; // Stores date only
	private Date classTime; // Stores time only
	private String classDuration;
	private int price;

	public GymClass(String nameOfClass, Date classDate, Date classTime, int noOfSlots, String classDuration, int price) {
		this.nameOfClass = nameOfClass;
		this.noOfSlots = noOfSlots;
		this.classDate = classDate;
		this.classTime = classTime;
		this.classDuration = classDuration;
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getNameOfClass() {
		return nameOfClass;
	}

	public void setNameOfClass(String nameOfClass) {
		this.nameOfClass = nameOfClass;
	}

	public int getNoOfSlots() {
		return noOfSlots;
	}

	public void setNoOfSlots(int noOfSlots) {
		this.noOfSlots = noOfSlots;
	}

	public Date getClassDate() {
		return classDate;
	}

	public void setClassDate(Date classDate) {
		this.classDate = classDate;
	}

	public Date getClassTime() {
		return classTime;
	}

	public void setClassTime(Date classTime) {
		this.classTime = classTime;
	}

	public String getClassDuration() {
		return classDuration;
	}

	public void setClassDuration(String classDuration) {
		this.classDuration = classDuration;
	}
}
