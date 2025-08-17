package com.watersidegym.service;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import com.watersidegym.service.PTSessionService;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PTSessionServiceTest {

	//null member and ptsession check
    @Test
    void bookReturnsFalseWhenMemberOrSessionNull() {
        PTSessionService svc = new PTSessionService();
        User u = new User();
        u.setRemainingPTSessions(1);

        assertFalse(svc.bookSession(null, u));
        assertFalse(svc.bookSession(new PTSession(), null));
        assertEquals(0, svc.getAllBookedSession().size());
    }

    //0 remaining sessions check
    @Test
    void bookReturnsFalseWhenNoRemainingSessions() {
        PTSessionService sessionService = new PTSessionService();
        User u = new User();
        u.setFirstName("Sam");
        u.setSecondName("Lee");
        u.setRemainingPTSessions(0);

        PTSession s = new PTSession();
        s.setTrainerName("Alex");
        s.setSessionDate(new Date());
        s.setSessionTime("08:00");
        s.setDuration("30 min");

        assertFalse(sessionService.bookSession(s, u));
        assertEquals(0, sessionService.getAllBookedSession().size());
    }

    //success case 
    @Test
    void bookSuccessSetsMemberNameDecrementsAndStores() {
        PTSessionService svc = new PTSessionService();
        User u = new User();
        u.setFirstName("Sam");
        u.setSecondName("Lee");
        u.setRemainingPTSessions(2);

        PTSession s = new PTSession();
        s.setTrainerName("Priya Shah");
        s.setSessionDate(new Date());
        s.setSessionTime("09:00");
        s.setDuration("45 min");

        assertTrue(svc.bookSession(s, u));
        assertEquals(1, svc.getAllBookedSession().size());
        assertEquals("Sam Lee", s.getMemberName());
        assertEquals(1, u.getRemainingPTSessions());

        List<PTSession> mine = svc.getBookedSessionsForMember("sam lee");
        assertEquals(1, mine.size());
        assertEquals("Priya Shah", mine.get(0).getTrainerName());
    }
}
