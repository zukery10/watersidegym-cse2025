package com.watersidegym.service;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import java.util.ArrayList;
import java.util.List;

public class PTSessionService {

    private List<PTSession> sessions = new ArrayList<>();

    public PTSessionService() {
    }

    public List<PTSession> getAllSessions() {
        // returns all sessions stored in memory
        return sessions;
    }

    // retrieve all sessions for a specific trainer
    public List<PTSession> getSessionsForTrainer(User trainer) {
        List<PTSession> result = new ArrayList<>();
        for (PTSession s : sessions) {
            // check if session's trainer matches the requested trainer
            if (s.getTrainer() != null && s.getTrainer().equals(trainer)) {
                result.add(s);
            }
        }
        return result;
    }

    // book a session for a member
    public boolean bookSession(PTSession ptSession, User member) {
        // if either member or session is null, cannot book
        if (member == null || ptSession == null) {
            return false;
        }

        // check if member has remaining sessions
        if (member.getRemainingPTSessions() <= 0) {
            return false;
        }

        ptSession.setMember(member);

        // prevent duplicate booking of same trainer/date/time
        for (PTSession s : sessions) {
            if (s.getTrainer() != null && s.getTrainer().equals(ptSession.getTrainer())
                    && s.getSessionDate().equals(ptSession.getSessionDate())
                    && s.getStartTime().equals(ptSession.getStartTime())) {
                return false;
            }
        }

        // add session to member and global list, reduce remaining sessions
        member.addBookedPTSession(ptSession);
        member.setRemainingPTSessions(member.getRemainingPTSessions() - 1);
        sessions.add(ptSession);
        return true;
    }

    // get all sessions booked by a specific member
    public List<PTSession> getBookedSessionsForMember(User member) {
        List<PTSession> result = new ArrayList<>();
        for (PTSession s : member.getBookedPTSession()) {
            // check if the session belongs to this member
            if (s.getMember() != null && s.getMember().equals(member)) {
                result.add(s);
            }
        }
        return result;
    }

    // retrieve all users with role "PT" (all trainers)
    public List<User> getAllTrainers() {
        List<User> trainers = new ArrayList<>();
        for (User u : UserService.getUsers()) {
            // check if user role is PT
            if ("PT".equals(u.getRole())) {
                trainers.add(u);
            }
        }
        return trainers;
    }

}
