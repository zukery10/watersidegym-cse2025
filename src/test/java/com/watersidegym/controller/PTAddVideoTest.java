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
	public void yogaVideosOnlyContainYoga() {
		List<Video> yoga = bean.getYogaOnly();
		assertFalse(yoga.isEmpty());
		for (Video v : yoga) {
			assertEquals("Yoga", v.getCategory());
		}
	}

	@Test
	public void recentlyAddedOnlyContainRecent() {
		List<Video> recent = bean.getRecentlyAdded();
		assertFalse(recent.isEmpty());
		for (Video v : recent) {
			assertEquals("Recently Added", v.getCategory());
		}
	}

	@Test
	public void tabataVideosOnlyContainTabata() {
		List<Video> tabata = bean.getTabataOnly();
		assertFalse(tabata.isEmpty());
		for (Video v : tabata) {
			assertEquals("Tabata", v.getCategory());
		}
	}

	@Test
	public void formFieldsStoreAndReturnValues() {
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
	public void tabataVideosSortedNewestFirst() {
		List<Video> tabata = bean.getTabataOnly();
		assertFalse(tabata.isEmpty());
		String firstDate = tabata.get(0).getDate();
		String lastDate = tabata.get(tabata.size() - 1).getDate();
		assertTrue(firstDate.compareTo(lastDate) >= 0);
	}

	@Test
	void preloadLoadAtLeastTenVideos() {
		assertNotNull(bean.getVideos());
		assertTrue(bean.getVideos().size() >= 10);
	}

	@Test
	void formFieldsKeepValues() {
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
	void yogaOnlyReturnYogaVideos() {
		for (Video v : bean.getYogaOnly())
			assertEquals("Yoga", v.getCategory());
	}

	@Test
	void recentlyAddedReturnCorrectCategory() {
		for (Video v : bean.getRecentlyAdded())
			assertEquals("Recently Added", v.getCategory());
	}

	@Test
	void tabataOnlyReturnTabataVideos() {
		for (Video v : bean.getTabataOnly())
			assertEquals("Tabata", v.getCategory());
	}

	@Test
	void taiChiOnlyReturnTaiChiVideos() {
		for (Video v : bean.getTaichiOnly())
			assertEquals("Tai Chi", v.getCategory());
	}

	@Test
	void zumbaOnlyReturnZumbaVideos() {
		for (Video v : bean.getZumbaOnly())
			assertEquals("Zumba", v.getCategory());
	}

	@Test
	void coreOnlyReturnCoreStrengthVideos() {
		for (Video v : bean.getCoreOnly())
			assertEquals("Core Strength", v.getCategory());
	}

	@Test
	void kickboxingOnlyReturnKickboxingVideos() {
		for (Video v : bean.getKickboxingOnly())
			assertEquals("Kickboxing", v.getCategory());
	}

	@Test
	void crossfitOnlyReturnCrossfitVideos() {
		for (Video v : bean.getCrossfitOnly())
			assertEquals("CrossFit", v.getCategory());
	}

	@Test
	void aquaAerobicsOnlyReturnAquaVideos() {
		for (Video v : bean.getAquaaerobicsOnly())
			assertEquals("Aqua Aerobics", v.getCategory());
	}

	@Test
	void pilatesOnlyReturnPilatesVideos() {
		for (Video v : bean.getPilatesOnly())
			assertEquals("Pilates", v.getCategory());
	}

	@Test
	public void updateRulePublic() {
		Video v = new Video(999, "Test Video", "/resources/videos/test.mp4", "2025-08-19", "Testing updateRule",
				"Public", "Recently Added", "tester@example.com");
		bean.getVideos().add(v);

		v.setRule("Private");
		bean.updateRule(v);
		assertEquals("Private", v.getRule());

		v.setRule("Hidden");
		bean.updateRule(v);
		assertEquals("Hidden", v.getRule());
	}

	@Test
	public void updateRulePrivate() {
		Video v = new Video(1000, "Test Video", "/resources/videos/test.mp4", "2025-08-19", "Testing updateRule",
				"Private", "Recently Added", "tester@example.com");
		bean.getVideos().add(v);

		v.setRule("Public");
		bean.updateRule(v);
		assertEquals("Public", v.getRule());

		v.setRule("Hidden");
		bean.updateRule(v);
		assertEquals("Hidden", v.getRule());
	}

	@Test
	public void updateRuleHidden() {
		Video v = new Video(1001, "Test Video", "/resources/videos/test.mp4", "2025-08-19", "Testing updateRule",
				"Hidden", "Recently Added", "tester@example.com");
		bean.getVideos().add(v);

		v.setRule("Public");
		bean.updateRule(v);
		assertEquals("Public", v.getRule());

		v.setRule("Private");
		bean.updateRule(v);
		assertEquals("Private", v.getRule());
	}
}
