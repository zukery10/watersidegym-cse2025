package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;

public class PTSessionService {


	//adds session to list and decrement session number
	public boolean bookSession(PTSession ptSession, User member) {

		
		
		if (member == null || ptSession == null) {
			return false;
		}

		if (member.getRemainingPTSessions() <= 0) {
			return false;
		}

		String fullName = member.getFirstName() + " " + member.getSecondName();
		ptSession.setMemberName(fullName);
	
		for (PTSession s : member.getBookedPTSession()) {
	        if (s.getMemberName().equalsIgnoreCase(fullName)
	                && s.getTrainerName().equalsIgnoreCase(ptSession.getTrainerName())
	                && s.getSessionDate().toString().equalsIgnoreCase(ptSession.getSessionDate().toString())
	                && s.getSessionTime().equalsIgnoreCase(ptSession.getSessionTime())) {
	            return false;
	        }
	    }

		member.addBookedPTSession(ptSession);

		member.setRemainingPTSessions(member.getRemainingPTSessions() - 1);
		return true;
	}
	
	
	
	//Returns all sessions booked by a specific member name
	public List<PTSession> getBookedSessionsForMember(User member) {
		List<PTSession> result = new ArrayList<>();
		
		String memberName = member.getFirstName() + " " + member.getSecondName();


		for (PTSession s : member.getBookedPTSession()) {
			if (s.getMemberName() != null && s.getMemberName().equalsIgnoreCase(memberName)) {
				result.add(s);
			}
		}

		return result;
	}

}
