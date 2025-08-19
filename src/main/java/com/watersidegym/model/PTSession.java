package com.watersidegym.model;

import java.util.Date;

public class PTSession {
	private User member; // person booking
	private User trainer; // has role = "PT"
	private Date sessionDate;
	private String startTime;
	private Integer durationMinutes;

	public PTSession() {
	}

	public PTSession(User member, User trainer, Date sessionDate, String startTime, int durationMinutes) {
		this.member = member;
		this.trainer = trainer;
		this.sessionDate = sessionDate;
		this.startTime = startTime;
		this.durationMinutes = durationMinutes;
	}

	public User getMember() {
		return member;
	}

	public User getTrainer() {
		return trainer;
	}

	public Date getSessionDate() {
		return sessionDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public Integer getDurationMinutes() {
	    return durationMinutes;
	}

	public void setMember(User member) {
		this.member = member;
	}

	public void setTrainer(User trainer) {
		this.trainer = trainer;
	}

	public void setSessionDate(Date sessionDate) {
		this.sessionDate = sessionDate;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setDurationMinutes(Integer durationMinutes) {
	    this.durationMinutes = durationMinutes;
	}
}
