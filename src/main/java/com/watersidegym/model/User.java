
package com.watersidegym.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
	private String email;
	private String password;
	private String firstName;
	private String secondName;
	private String address1;
	private String address2;
	private String county;
	private String eircode;
	private String phoneNumber;
	private Date dateOfBirth;
	private String gender; // "Male", "Female", "Other"
	private String role; // "Non-Member", "Member", "PT"
	private List<GymClass> enrolledClasses = new ArrayList<>();
	private int remainingPTSessions;
	private List<PTSession> bookedPTSession = new ArrayList<>();
	private List<GymClass> cart = new ArrayList<>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getEircode() {
		return eircode;
	}

	public void setEircode(String eircode) {
		this.eircode = eircode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public List<GymClass> getEnrolledClasses() {
		return enrolledClasses;
	}
	
	public int getRemainingPTSessions() {
		return remainingPTSessions;
	}

	public void setRemainingPTSessions(int remainingPTSessions) {
		this.remainingPTSessions = remainingPTSessions;
	}
	
	public List<PTSession> getBookedPTSession() {
		return bookedPTSession;
	}

	public void setBookedPTSession(List<PTSession> bookedPTSession) {
		this.bookedPTSession = bookedPTSession;
	}
	
	public void addBookedPTSession(PTSession ptSession) {
		
		bookedPTSession.add(ptSession);		
	}

	public void enrollInClass(GymClass gymClass) {
	    for (GymClass enrolled : enrolledClasses) {
	        if (isSameClass(enrolled, gymClass)) {
	            return; // already enrolled do nothing
	        }
	    }
	    enrolledClasses.add(gymClass);
	}
	
	public boolean isEnrolledInClass(GymClass gymClass) {
	    for (GymClass enrolled : enrolledClasses) {
	        if (isSameClass(enrolled, gymClass)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public boolean isSameClass(GymClass c1, GymClass c2) {
	    return c1.getNameOfClass().equals(c2.getNameOfClass()) &&
	           c1.getClassDate().equals(c2.getClassDate()) &&
	           c1.getClassTime().equals(c2.getClassTime());
	}
}

