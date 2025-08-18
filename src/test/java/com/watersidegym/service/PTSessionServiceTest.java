package com.watersidegym.service;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PTSessionServiceTest {

    // null member and ptsession check
    @Test
    void bookReturnsFalseWhenMemberOrSessionNull() {
        PTSessionService service = new PTSessionService();
        User user = new User();
        user.setRemainingPTSessions(1);

        assertFalse(service.bookSession(null, user));
        assertFalse(service.bookSession(new PTSession(), null));
        assertEquals(0, user.getBookedPTSession().size());
    }

    // 0 remaining sessions check
    @Test
    void bookReturnsFalseWhenNoRemainingSessions() {
        PTSessionService sessionService = new PTSessionService();
        User member = new User();
        member.setFirstName("Sam");
        member.setSecondName("Lee");
        member.setRemainingPTSessions(0);

        User trainer = new User();
        trainer.setFirstName("Alex");
        trainer.setSecondName("Trainer");

        PTSession session = new PTSession();
        session.setTrainer(trainer);
        session.setSessionDate(new Date());
        session.setStartTime("8:00");
        session.setDurationMinutes(30);

        assertFalse(sessionService.bookSession(session, member));
        assertEquals(0, member.getBookedPTSession().size());
    }

    // success case
    @Test
    void bookSuccessSetsMemberDecrementsAndStores() {
        PTSessionService service = new PTSessionService();

        User member = new User();
        member.setFirstName("Sam");
        member.setSecondName("Lee");
        member.setRemainingPTSessions(2);

        User trainer = new User();
        trainer.setFirstName("Priya");
        trainer.setSecondName("Shah");

        PTSession session = new PTSession();
        session.setTrainer(trainer);
        session.setSessionDate(new Date());
        session.setStartTime("8:00");
        session.setDurationMinutes(45);

        assertTrue(service.bookSession(session, member));
        assertEquals(1, member.getBookedPTSession().size());
        assertEquals(member, session.getMember());
        assertEquals(1, member.getRemainingPTSessions());

        List<PTSession> mine = service.getBookedSessionsForMember(member);
        assertEquals(1, mine.size());
        assertEquals("Priya", mine.get(0).getTrainer().getFirstName());
        assertEquals("Shah", mine.get(0).getTrainer().getSecondName());
    }
}
