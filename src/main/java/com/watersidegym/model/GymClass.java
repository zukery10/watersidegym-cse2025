package com.watersidegym.model;

import java.util.Date;

public class GymClass {

    private String nameOfClass;
    private int noOfSlots;
    private Date classDate;  // Stores date only
    private Date classTime;  // Stores time only

    public GymClass(String nameOfClass, Date classDate,Date classTime,int noOfSlots) {
        this.nameOfClass = nameOfClass;
        this.noOfSlots = noOfSlots;
        this.classDate = classDate;
        this.classTime = classTime;
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
}
