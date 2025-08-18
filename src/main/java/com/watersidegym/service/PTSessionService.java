package com.watersidegym.service;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PTSessionService {

    private List<PTSession> sessions = new ArrayList<>();

    public PTSessionService() {
        initSampleData();
    }

    // --- Sample Data for Trainers ---
    private void initSampleData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        User member = UserService.findUserByEmail("member@example.com");
        User trainer = UserService.findUserByEmail("PT@example.com");

        try {
            sessions.add(new PTSession(member, trainer,
                    dateFormat.parse("15.08.2025"),
                    timeFormat.parse("09:00"), 60));

            sessions.add(new PTSession(member, trainer,
                    dateFormat.parse("16.08.2025"),
                    timeFormat.parse("11:00"), 45));

            sessions.add(new PTSession(member, trainer,
                    dateFormat.parse("17.08.2025"),
                    timeFormat.parse("15:30"), 30));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // --- Trainer-facing: retrieve sessions ---
    public List<PTSession> getSessionsForTrainer(User trainer) {
        List<PTSession> result = new ArrayList<>();
        for (PTSession s : sessions) {
            if (s.getTrainer() != null && s.getTrainer().equals(trainer)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<PTSession> getAllSessions() {
        return sessions;
    }

    // --- Member-facing: booking logic ---
    public boolean bookSession(PTSession ptSession, User member) {
        if (member == null || ptSession == null) {
            return false;
        }

        if (member.getRemainingPTSessions() <= 0) {
            return false;
        }

        ptSession.setMember(member);

        // prevent duplicate booking of same trainer/date/time
        for (PTSession s : member.getBookedPTSession()) {
            if (s.getTrainer() != null && s.getTrainer().equals(ptSession.getTrainer())
                    && s.getSessionDate().equals(ptSession.getSessionDate())
                    && s.getStartTime().equals(ptSession.getStartTime())) {
                return false;
            }
        }

        member.addBookedPTSession(ptSession);
        member.setRemainingPTSessions(member.getRemainingPTSessions() - 1);
        return true;
    }

    public List<PTSession> getBookedSessionsForMember(User member) {
        List<PTSession> result = new ArrayList<>();
        for (PTSession s : member.getBookedPTSession()) {
            if (s.getMember() != null && s.getMember().equals(member)) {
                result.add(s);
            }
        }
        return result;
    }
}
