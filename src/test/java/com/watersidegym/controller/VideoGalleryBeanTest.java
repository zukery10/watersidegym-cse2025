package com.watersidegym.controller;

import com.watersidegym.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class VideoGalleryBeanTest {

    private VideoGalleryBean bean;

    @BeforeEach
    public void setUp1() {
        bean = new VideoGalleryBean();
        bean.preload();
    }

    @Test
    public void testPreload_addsVideos1() {
        List<Video> videos = bean.getVideos();
        assertNotNull(videos);
        assertFalse(videos.isEmpty());
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
        void testPreload_addsVideos() {
            assertNotNull(bean.getVideos());
            assertEquals(6, bean.getVideos().size());
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
        @Test
    	public void bodyweightOnlyContainBodyweight() {
    		List<Video> bodyweight = bean.getBodyweightOnly();
    		assertFalse(bodyweight.isEmpty());
    		for (Video v : bodyweight) {
    			assertEquals("Bodyweight", v.getCategory());
    		}
    	}

    	@Test
    	public void freeWeightsOnlyContainFreeWeights() {
    		List<Video> freeWeights = bean.getFreeWeightsOnly();
    		assertFalse(freeWeights.isEmpty());
    		for (Video v : freeWeights) {
    			assertEquals("Free Weights", v.getCategory());
    		}
    	}

    	@Test
    	public void machinesOnlyContainMachines() {
    		List<Video> machines = bean.getMachinesOnly();
    		assertFalse(machines.isEmpty());
    		for (Video v : machines) {
    			assertEquals("Machines", v.getCategory());
    		}
    	}

    	@Test
    	public void bandsOnlyContainBands() {
    		List<Video> bands = bean.getBandsOnly();
    		assertFalse(bands.isEmpty());
    		for (Video v : bands) {
    			assertEquals("Bands", v.getCategory());
    		}
    	}

    	@Test
    	public void cardioOnlyContainCardio() {
    		List<Video> cardio = bean.getCardioEquipmentOnly();
    		assertFalse(cardio.isEmpty());
    		for (Video v : cardio) {
    			assertEquals("Cardio Equipment", v.getCategory());
    		}
    	}

        @Test
        void testDeleteVideo_existingVideo() {
            int initialSize = bean.getVideos().size();
            
            int video1 = bean.getVideos().get(0).getId();
            
            bean.deleteVideo(video1);
            
            int video2 = bean.getVideos().get(0).getId();    
            
            assertEquals(initialSize - 1, bean.getVideos().size());
            assertFalse(video1 == video2);
        }

        @Test
        void testDeleteVideo_nonExistentVideo() {
            int initialSize = bean.getVideos().size();
            
            bean.deleteVideo(-1);
            
            assertEquals(initialSize, bean.getVideos().size());
        }

        @Test
        void testDeleteVideo_multipleVideos() {
            int initialSize = bean.getVideos().size();
            
            int firstVideoId = bean.getVideos().get(0).getId();
            int secondVideoId = bean.getVideos().get(1).getId();
            
            bean.deleteVideo(firstVideoId);
            bean.deleteVideo(secondVideoId);
            
            assertEquals(initialSize - 2, bean.getVideos().size());
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
        
        @Test
    	void updateRuleSetsMessage() {
    		Video v = new Video(3000, "Message Test", "/resources/videos/test.mp4", "2025-08-20", "desc", "Public",
    				"Free Weights", "Uploader");
    		bean.getVideos().add(v);

    		v.setRule("Private");
    		bean.updateRule(v);

    		assertTrue(v.getMessage().contains("Visibility updated to Private"));
    	}
        @Test
    	public void freeWeightsVideosSortedNewestFirst() {
    		List<Video> freeWeights = bean.getFreeWeightsOnly();
    		assertFalse(freeWeights.isEmpty());
    		String firstDate = freeWeights.get(0).getDate();
    		String lastDate = freeWeights.get(freeWeights.size() - 1).getDate();
    		assertTrue(firstDate.compareTo(lastDate) >= 0);
    	}

    	@Test
    	void preloadLoadAtLeastSixVideos() {
    		assertNotNull(bean.getVideos());
    		assertTrue(bean.getVideos().size() >= 6);
    	}
    	@Test
    	public void updateRulePublicToPrivateHidden() {
    		Video v = new Video(999, "Test Video", "/resources/videos/test.mp4", "2025-08-19", "Testing updateRule",
    				"Public", "Free Weights", "tester@example.com");
    		bean.getVideos().add(v);

    		v.setRule("Private");
    		bean.updateRule(v);
    		assertEquals("Private", v.getRule());

    		v.setRule("Hidden");
    		bean.updateRule(v);
    		assertEquals("Hidden", v.getRule());
    	}

    	@Test
    	public void updateRulePrivateToPublicHidden() {
    		Video v = new Video(1000, "Test Video", "/resources/videos/test.mp4", "2025-08-19", "Testing updateRule",
    				"Private", "Bodyweight", "tester@example.com");
    		bean.getVideos().add(v);

    		v.setRule("Public");
    		bean.updateRule(v);
    		assertEquals("Public", v.getRule());

    		v.setRule("Hidden");
    		bean.updateRule(v);
    		assertEquals("Hidden", v.getRule());
    	}

    	@Test
    	public void updateRuleHiddenToPublicPrivate() {
    		Video v = new Video(1001, "Test Video", "/resources/videos/test.mp4", "2025-08-19", "Testing updateRule",
    				"Hidden", "Machines", "tester@example.com");
    		bean.getVideos().add(v);

    		v.setRule("Public");
    		bean.updateRule(v);
    		assertEquals("Public", v.getRule());

    		v.setRule("Private");
    		bean.updateRule(v);
    		assertEquals("Private", v.getRule());
    	}

    	@Test
    	void gettersAndSettersWork() {
    		bean.setFormTitle("Test Title");
    		bean.setFormDate("2025-08-20");
    		bean.setFormDescription("Test Desc");
    		bean.setFormRule("Hidden");
    		bean.setFormCategory("Machines");

    		assertEquals("Test Title", bean.getFormTitle());
    		assertEquals("2025-08-20", bean.getFormDate());
    		assertEquals("Test Desc", bean.getFormDescription());
    		assertEquals("Hidden", bean.getFormRule());
    		assertEquals("Machines", bean.getFormCategory());
    	}
}

