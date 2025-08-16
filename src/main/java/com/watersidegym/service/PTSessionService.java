package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import com.watersidegym.model.GymClass;
import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;

public class PTSessionService {

	private List<PTSession> bookedSessionsList;

	public PTSessionService() {
		bookedSessionsList = new ArrayList<>();

	}

	public List<PTSession> getAllBookedSession() {
		return bookedSessionsList;
	}

	public boolean book(PTSession ptSession, User member) {

		if (member == null || ptSession == null) {
			return false;
		}

		if (member.getRemainingPTSessions() <= 0) {
			return false;
		}

		member.setRemainingPTSessions(member.getRemainingPTSessions() - 1);
		return true;
	}

	public List<PTSession> getBookedSessionsForMember(String memberName) {
	    List<PTSession> result = new ArrayList<>();
	    if (memberName == null) {
	        return result;
	    }

	    for (PTSession s : bookedSessionsList) {
	        if (s.getMemberName() != null && s.getMemberName().equalsIgnoreCase(memberName)) {
	            result.add(s);
	        }
	    }

	    return result;
	}

}
