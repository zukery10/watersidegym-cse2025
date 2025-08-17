package com.watersidegym.service;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PTSessionServiceTest {

	//null member and ptsession check
    @Test
    void bookReturnsFalseWhenMemberOrSessionNull() {
        PTSessionService service = new PTSessionService();
        User user = new User();
        user.setRemainingPTSessions(1);

        assertFalse(service.bookSession(null, user));
        assertFalse(service.bookSession(new PTSession(), null));
        assertEquals(0, user.getBookedPTSession().size());
    }

    //0 remaining sessions check
    @Test
    void bookReturnsFalseWhenNoRemainingSessions() {
        PTSessionService sessionService = new PTSessionService();
        User user = new User();
        user.setFirstName("Sam");
        user.setSecondName("Lee");
        user.setRemainingPTSessions(0);

        PTSession session = new PTSession();
        session.setTrainerName("Alex");
        session.setSessionDate(new Date());
        session.setSessionTime("08:00");
        session.setDuration("30 min");

        assertFalse(sessionService.bookSession(session, user));
        assertEquals(0, user.getBookedPTSession().size());
    }

    //success case 
    @Test
    void bookSuccessSetsMemberNameDecrementsAndStores() {
        PTSessionService service = new PTSessionService();
        User user = new User();
        user.setFirstName("Sam");
        user.setSecondName("Lee");
        user.setRemainingPTSessions(2);

        PTSession session = new PTSession();
        session.setTrainerName("Priya Shah");
        session.setSessionDate(new Date());
        session.setSessionTime("09:00");
        session.setDuration("45 min");

        assertTrue(service.bookSession(session, user));
        assertEquals(1, user.getBookedPTSession().size());
        assertEquals("Sam Lee", session.getMemberName());
        assertEquals(1, user.getRemainingPTSessions());

        List<PTSession> mine = service.getBookedSessionsForMember(user);
        assertEquals(1, mine.size());
        assertEquals("Priya Shah", mine.get(0).getTrainerName());
    }
}
