package com.watersidegym.service;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PTSessionServiceTest {

    private PTSessionService service;
    private User trainer;
    private User member;

    @BeforeEach
    void setUp() {
        service = new PTSessionService();

        trainer = new User();
        trainer.setFirstName("Alice");
        trainer.setSecondName("Trainer");
        trainer.setRole("PT");

        member = new User();
        member.setFirstName("Bob");
        member.setSecondName("Member");
        member.setRole("Member");
        member.setRemainingPTSessions(3);
    }

    @Test
    void testGetSessionsForTrainerWhenNoneExist() {
        List<PTSession> sessions = service.getSessionsForTrainer(trainer);
        assertTrue(sessions.isEmpty());
    }

    @Test
    void testBookSessionFailsIfMemberOrSessionIsNull() {
        User u = new User();
        u.setRemainingPTSessions(1);

        assertFalse(service.bookSession(null, u));
        assertFalse(service.bookSession(new PTSession(), null));
        assertEquals(0, u.getBookedPTSession().size());
    }

    @Test
    void testBookSessionFailsIfNoRemainingSessions() {
        User noCredits = new User();
        noCredits.setRemainingPTSessions(0);

        PTSession s = new PTSession();
        s.setTrainer(trainer);
        s.setSessionDate(new Date());
        s.setStartTime("08:00");
        s.setDurationMinutes(30);

        assertFalse(service.bookSession(s, noCredits));
        assertEquals(0, noCredits.getBookedPTSession().size());
    }

    @Test
    void testBookSessionWorksForValidMemberAndTrainer() {
        PTSession s = new PTSession();
        s.setTrainer(trainer);
        s.setSessionDate(new Date());
        s.setStartTime("10:00");
        s.setDurationMinutes(60);

        boolean result = service.bookSession(s, member);

        assertTrue(result);
        assertEquals(2, member.getRemainingPTSessions()); // went down by 1
        assertEquals(1, member.getBookedPTSession().size());
        assertEquals(1, service.getAllSessions().size());
    }

    @Test
    void testBookSessionUpdatesMemberAndStoresSession() {
        User m = new User();
        m.setFirstName("Sam");
        m.setSecondName("Lee");
        m.setRemainingPTSessions(2);

        User t = new User();
        t.setFirstName("Priya");
        t.setSecondName("Shah");

        PTSession s = new PTSession();
        s.setTrainer(t);
        s.setSessionDate(new Date());
        s.setStartTime("08:00");
        s.setDurationMinutes(45);

        assertTrue(service.bookSession(s, m));
        assertEquals(1, m.getBookedPTSession().size());
        assertEquals(m, s.getMember());
        assertEquals(1, m.getRemainingPTSessions());

        List<PTSession> mine = service.getBookedSessionsForMember(m);
        assertEquals(1, mine.size());
        assertEquals("Priya", mine.get(0).getTrainer().getFirstName());
    }

    @Test
    void testBookSessionFailsForDuplicate() {
        Date date = new Date();

        PTSession s1 = new PTSession(member, trainer, date, "10:00", 60);
        service.bookSession(s1, member);

        PTSession s2 = new PTSession();
        s2.setTrainer(trainer);
        s2.setSessionDate(date);
        s2.setStartTime("10:00");
        s2.setDurationMinutes(60);

        boolean result = service.bookSession(s2, member);

        assertFalse(result);
        assertEquals(1, service.getAllSessions().size());
    }

    @Test
    void testGetBookedSessionsForMemberReturnsCorrectList() {
        PTSession s = new PTSession();
        s.setTrainer(trainer);
        s.setSessionDate(new Date());
        s.setStartTime("11:00");
        s.setDurationMinutes(30);

        service.bookSession(s, member);

        List<PTSession> booked = service.getBookedSessionsForMember(member);
        assertEquals(1, booked.size());
        assertEquals("11:00", booked.get(0).getStartTime());
    }
}