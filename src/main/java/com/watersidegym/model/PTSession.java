package com.watersidegym.model;

import java.util.Date;

public class PTSession {
	
	 	private String memberName;
	    private String trainerName;
	    private Date sessionDate;
	    private String sessionTime;
		
	    
	    public PTSession() {
			
		}


		public PTSession(String memberName, String trainerName, Date sessionDate, String sessionTime) {
			super();
			this.memberName = memberName;
			this.trainerName = trainerName;
			this.sessionDate = sessionDate;
			this.sessionTime = sessionTime;
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
	    
	    
	    
	    

}
