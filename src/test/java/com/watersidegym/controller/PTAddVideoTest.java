package com.watersidegym.controller;

import com.watersidegym.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

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

        @Test
        void testDeleteVideo_existingVideo() {
            int initialSize = bean.getVideos().size();
            
            int videoId = bean.getVideos().get(0).getId();
            
            bean.deleteVideo(videoId);
            
            assertEquals(initialSize - 1, bean.getVideos().size(), "Video list size should decrease by 1");
            
            boolean videoStillExists = bean.getVideos().stream()
                    .anyMatch(v -> v.getId() == videoId);
            assertFalse(videoStillExists, "Video should no longer exist in the list");
        }

        @Test
        void testDeleteVideo_nonExistentVideo() {
            int initialSize = bean.getVideos().size();
            
            bean.deleteVideo(-1);
            
            assertEquals(initialSize, bean.getVideos().size(), "Video list size should not change");
        }

        @Test
        void testDeleteVideo_multipleVideos() {
            int initialSize = bean.getVideos().size();
            
            int firstVideoId = bean.getVideos().get(0).getId();
            int secondVideoId = bean.getVideos().get(1).getId();
            
            bean.deleteVideo(firstVideoId);
            bean.deleteVideo(secondVideoId);
            
            assertEquals(initialSize - 2, bean.getVideos().size(), "Video list size should decrease by 2");
            
            assertFalse(bean.getVideos().stream().anyMatch(v -> v.getId() == firstVideoId), 
                    "First video should no longer exist");
            assertFalse(bean.getVideos().stream().anyMatch(v -> v.getId() == secondVideoId), 
                    "Second video should no longer exist");
        }

        @Test
        void testDeleteVideo_lastVideo() {
            List<Video> videos = bean.getVideos();
            int lastIndex = videos.size() - 1;
            int lastVideoId = videos.get(lastIndex).getId();
            
            bean.deleteVideo(lastVideoId);
            
            assertFalse(bean.getVideos().stream().anyMatch(v -> v.getId() == lastVideoId), 
                    "Last video should be deleted");
            assertEquals(lastIndex, bean.getVideos().size(), 
                    "Video list size should decrease by 1");
        }

        @Test
        void testDeleteVideo_sequentialDeletes() {
            int initialSize = bean.getVideos().size();
            List<Integer> ids = new ArrayList<>();
            
            for(int i = 0; i < 3 && i < initialSize; i++) {
                ids.add(bean.getVideos().get(i).getId());
            }
            
            for(Integer id : ids) {
                bean.deleteVideo(id);
            }
            
            assertEquals(initialSize - ids.size(), bean.getVideos().size(), 
                    "Video list size should decrease by " + ids.size());
            
            for(Integer id : ids) {
                assertFalse(bean.getVideos().stream().anyMatch(v -> v.getId() == id), 
                        "Video with ID " + id + " should be deleted");
            }
        }

        @Test
        void testGetMyVideos_withoutLoginBean() {
            List<Video> myVideos = bean.getMyVideos();
            assertTrue(myVideos.isEmpty(), "Without login, my videos should be empty");
        }



        @Test
        void testGetVideos_orderPreservation() {
            List<Video> originalList = bean.getVideos();
            List<Video> newList = bean.getVideos();
            
            assertEquals(originalList.size(), newList.size(), "Lists should be same size");
            for (int i = 0; i < originalList.size(); i++) {
                assertEquals(originalList.get(i).getId(), newList.get(i).getId(),
                    "Videos should maintain same order");
            }
        }

        @Test
        void testByCategory_withEmptyCategory() {
            List<Video> videos = bean.getVideos();
            Video firstVideo = videos.get(0);
            String originalCategory = firstVideo.getCategory();
            firstVideo.setCategory("");
            
            List<Video> categoryVideos = bean.byCategory(originalCategory);
            assertFalse(categoryVideos.contains(firstVideo), 
                "Video with empty category should not be in category results");
            firstVideo.setCategory(originalCategory);
        }

        @Test
        void testByCategory_withNullCategory() {
            List<Video> videos = bean.getVideos();
            Video firstVideo = videos.get(0);
            String originalCategory = firstVideo.getCategory();
            firstVideo.setCategory(null);
            List<Video> categoryVideos = bean.byCategory(originalCategory);
            assertFalse(categoryVideos.contains(firstVideo), 
                "Video with null category should not be in category results");
            
            firstVideo.setCategory(originalCategory);
        }

        @Test
        void testByCategory_caseSensitivity() {
            String category = "yoga";  
            List<Video> yogaVideos = bean.byCategory(category);
            
            for (Video v : yogaVideos) {
                assertTrue(v.getCategory().equalsIgnoreCase(category),
                    "Category matching should be case-insensitive");
            }
        }

}

