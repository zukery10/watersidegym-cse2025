package com.watersidegym.controller;

import com.watersidegym.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PTAddVideoTest {

    private PTAddVideo bean;

    @BeforeEach
    public void setUp1() {
        bean = new PTAddVideo();
        bean.preload();
    }

    @Test
    public void testPreload_addsVideos1() {
        List<Video> videos = bean.getVideos();
        assertNotNull(videos);
        assertFalse(videos.isEmpty(), "Preload should add sample videos");
    }

    @Test
    public void testGetYogaOnly_returnsYogaVideos() {
        List<Video> yoga = bean.getYogaOnly();
        assertFalse(yoga.isEmpty(), "Yoga list should not be empty");
        for (Video v : yoga) {
            assertEquals("Yoga", v.getCategory());
        }
    }

    @Test
    public void testGetRecentlyAdded_returnsRecentlyAddedVideos() {
        List<Video> recent = bean.getRecentlyAdded();
        assertFalse(recent.isEmpty());
        for (Video v : recent) {
            assertEquals("Recently Added", v.getCategory());
        }
    }

    @Test
    public void testGetTabataOnly_returnsTabataVideos() {
        List<Video> tabata = bean.getTabataOnly();
        assertFalse(tabata.isEmpty());
        for (Video v : tabata) {
            assertEquals("Tabata", v.getCategory());
        }
    }

    @Test
    public void testFormFieldSettersAndGetters1() {
        bean.setFormTitle("My Video");
        bean.setFormDate("2025-08-18");
        bean.setFormDescription("Test video");
        bean.setFormRule("Member");
        bean.setFormCategory("Yoga");

        assertEquals("My Video", bean.getFormTitle());
        assertEquals("2025-08-18", bean.getFormDate());
        assertEquals("Test video", bean.getFormDescription());
        assertEquals("Member", bean.getFormRule());
        assertEquals("Yoga", bean.getFormCategory());
    }

    @Test
    public void testByCategory_isSortedNewestFirst1() {
        List<Video> tabata = bean.getTabataOnly();
        assertFalse(tabata.isEmpty());
        String firstDate = tabata.get(0).getDate();
        String lastDate = tabata.get(tabata.size()-1).getDate();
        assertTrue(firstDate.compareTo(lastDate) >= 0,
                "Videos should be sorted newest-first by date string");
    }
    
    
    
  
        @Test
        void testPreload_addsVideos() {
            assertNotNull(bean.getVideos());
            assertTrue(bean.getVideos().size() >= 10, "preload should add many videos");
        }

        @Test
        void testFormFieldSettersAndGetters() {
            bean.setFormTitle("My Video");
            bean.setFormDate("2025-08-18");
            bean.setFormDescription("Test video");
            bean.setFormRule("Member");
            bean.setFormCategory("Yoga");

            assertEquals("My Video", bean.getFormTitle());
            assertEquals("2025-08-18", bean.getFormDate());
            assertEquals("Test video", bean.getFormDescription());
            assertEquals("Member", bean.getFormRule());
            assertEquals("Yoga", bean.getFormCategory());
        }

       

        // --------------------

        @Test void testGetYogaOnly_returnsYoga() {
            for (Video v : bean.getYogaOnly())   assertEquals("Yoga", v.getCategory());
        }

        @Test void testGetRecentlyAdded_returnsRA() {
            for (Video v : bean.getRecentlyAdded()) assertEquals("Recently Added", v.getCategory());
        }

        @Test void testGetTabataOnly_returnsTabata() {
            for (Video v : bean.getTabataOnly()) assertEquals("Tabata", v.getCategory());
        }

        @Test void testGetTaichiOnly_returnsTaiChi() {
            for (Video v : bean.getTaichiOnly()) assertEquals("Tai Chi", v.getCategory());
        }

        @Test void testGetZumbaOnly_returnsZumba() {
            for (Video v : bean.getZumbaOnly()) assertEquals("Zumba", v.getCategory());
        }

        @Test void testGetCoreOnly_returnsCore() {
            for (Video v : bean.getCoreOnly()) assertEquals("Core Strength", v.getCategory());
        }

        @Test void testGetKickboxingOnly_returnsKickboxing() {
            for (Video v : bean.getKickboxingOnly()) assertEquals("Kickboxing", v.getCategory());
        }

        @Test void testGetCrossfitOnly_returnsCrossfit() {
            for (Video v : bean.getCrossfitOnly()) assertEquals("CrossFit", v.getCategory());
        }

        @Test void testGetAquaAerobicsOnly_returnsAqua() {
            for (Video v : bean.getAquaaerobicsOnly()) assertEquals("Aqua Aerobics", v.getCategory());
        }

        @Test void testGetPilatesOnly_returnsPilates() {
            for (Video v : bean.getPilatesOnly()) assertEquals("Pilates", v.getCategory());
        }

}

