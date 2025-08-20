package com.watersidegym.controller;

import com.watersidegym.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import javax.faces.application.FacesMessage;

import static org.junit.jupiter.api.Assertions.*;

public class PTAddVideoTest {

	private PTAddVideo bean;

	@BeforeEach
	public void setUp1() {
		bean = new PTAddVideo() {
			@Override
			protected void showMessage(FacesMessage.Severity type, String text) {
				// Do nothing in tests
			}
		};
		bean.preload();
	}

	@Test
	public void preloadLoadVideos() {
		List<Video> videos = bean.getVideos();
		assertNotNull(videos);
		assertFalse(videos.isEmpty());
	}

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
	public void formFieldsStoreAndReturnValues() {
		bean.setFormTitle("My Video");
		bean.setFormDate("2025-08-18");
		bean.setFormDescription("Test video");
		bean.setFormRule("Member");
		bean.setFormCategory("Free Weights");

		assertEquals("My Video", bean.getFormTitle());
		assertEquals("2025-08-18", bean.getFormDate());
		assertEquals("Test video", bean.getFormDescription());
		assertEquals("Member", bean.getFormRule());
		assertEquals("Free Weights", bean.getFormCategory());
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

	@Test
	void cancelUploadResetsUpload() {
		bean.setUploadedFile(null);
		String result = bean.cancelUpload();
		assertNull(result);
	}

	@Test
	void notifyFileChosenRuns() {
		bean.notifyFileChosen(null);
	}

	@Test
	void addVideoTestAddsVideo() {
		int before = bean.getVideos().size();
		bean.addVideoTest(new Video(2000, "Test", "/resources/videos/test.mp4", "2025-08-20", "desc", "Public", "Bands",
				"Uploader"));
		assertEquals(before + 1, bean.getVideos().size());
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

}
