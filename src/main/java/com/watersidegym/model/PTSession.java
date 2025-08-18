package com.watersidegym.model;

import java.util.Date;

public class PTSession {
    private User member;    // person booking
    private User trainer;   // has role = "PT"
    private Date sessionDate;
    private Date startTime;
    private int durationMinutes; // 30 or 60 mins
    public PTSession() {}
    public PTSession(User member, User trainer, Date sessionDate, Date startTime, int durationMinutes) {
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

    public Date getStartTime() {
        return startTime;
    }

    public int getDurationMinutes() {
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
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}
}
