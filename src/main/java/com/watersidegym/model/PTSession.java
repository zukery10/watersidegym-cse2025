package com.watersidegym.model;

import java.util.Date;

public class PTSession {
    
    private String memberName;
    private String trainerName;
    private String trainerEmail;   
	private Date sessionDate;
    private String sessionTime;
    private String duration; 

    public PTSession() {
    }

   
	public PTSession(String memberName, String trainerName, String trainerEmail, Date sessionDate, String sessionTime,
			String duration) {
		super();
		this.memberName = memberName;
		this.trainerName = trainerName;
		this.trainerEmail = trainerEmail;
		this.sessionDate = sessionDate;
		this.sessionTime = sessionTime;
		this.duration = duration;
	}


	public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTrainerName() {
        return trainerName;
    }
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public Date getSessionDate() {
        return sessionDate;
    }
    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }
    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getTrainerEmail() {
		return trainerEmail;
	}

	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
	}


	@Override
	public String toString() {
		return "PTSession [memberName=" + memberName + ", trainerName=" + trainerName + ", trainerEmail=" + trainerEmail
				+ ", sessionDate=" + sessionDate + ", sessionTime=" + sessionTime + ", duration=" + duration + "]";
	}
	
	
}
