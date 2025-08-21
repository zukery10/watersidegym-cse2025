package com.watersidegym.controller;

import com.watersidegym.model.PTSession;
import com.watersidegym.model.User;
import com.watersidegym.service.PTSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PTSessionBeanTest {

    private PTSessionBean bean;
    private PTSessionService service;
    private User trainer;

    @BeforeEach
    void setUp() {
        service = new PTSessionService();
        bean = new PTSessionBean();
        bean.setPtSessionService(service);

        trainer = new User();
        trainer.setFirstName("Alice");
        trainer.setSecondName("Trainer");
        trainer.setRole("PT");
    }

    @Test
    void testGetFilteredSessionsWhenNoUser() {
        assertTrue(bean.getFilteredSessions().isEmpty());
    }
    @Test
    void returnsNullWhenRequiredFieldsMissing() {
        User loggedIn = new User();
        loggedIn.setFirstName("Alex");
        loggedIn.setSecondName("Byrne");
        loggedIn.setRemainingPTSessions(2);

        bean.init(); 

        String outcome = bean.bookSession(); // no session details

        assertNull(outcome);
    }

    @Test
    void testFilterSessionsByDateWhenDatesMatch() {
        PTSession s = new PTSession();
        s.setTrainer(trainer);

        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.AUGUST, 18, 10, 0, 0);
        Date sessionDate = cal.getTime();
        s.setSessionDate(sessionDate);

        s.setStartTime("09:00");
        s.setDurationMinutes(60);

        List<PTSession> list = new ArrayList<>();
        list.add(s);
        bean.setSessionList(list);

        bean.setSelectedDate(sessionDate);
        bean.filterSessionsByDate();

        assertEquals(1, bean.getFilteredSessions().size());
    }

    @Test
    void testFilterSessionsByDateWhenDatesDoNotMatch() {
        PTSession s = new PTSession();
        s.setTrainer(trainer);

        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.AUGUST, 18, 10, 0, 0);
        s.setSessionDate(cal.getTime());

        s.setStartTime("09:00");
        s.setDurationMinutes(60);

        // make a list and add the session
        List<PTSession> list = new ArrayList<>();
        list.add(s);
        bean.setSessionList(list);

        // pick a different day
        Calendar other = Calendar.getInstance();
        other.set(2025, Calendar.AUGUST, 19);
        bean.setSelectedDate(other.getTime());

        bean.filterSessionsByDate();

        assertTrue(bean.getFilteredSessions().isEmpty());
    }


    @Test
    void testGetTodayIso_notNull() {
        String today = bean.getTodayIso();
        assertNotNull(today);
        //System.out.println(today);
        assertTrue(today.matches("2025-08-21"));
    }
    //0 sessions check
    @Test
    void returnsNullWhenNoSessionsLeft() {
        User loggedIn = new User();
        loggedIn.setFirstName("Alex");
        loggedIn.setSecondName("Byrne");
        loggedIn.setRemainingPTSessions(0);

        PTSession form = bean.getPtSession();
        form.setTrainer(loggedIn);
        form.setSessionDate(new Date());
        form.setStartTime("8:00");
        form.setDurationMinutes(30);

        String outcome = bean.bookSession();

        assertNull(outcome);
        assertEquals(0, loggedIn.getRemainingPTSessions());
    }


}

