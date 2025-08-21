package com.watersidegym.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.watersidegym.controller.VideoGalleryBean;
import com.watersidegym.model.User;
import com.watersidegym.model.Video;

class VideoServiceTest {
	private VideoService videoservice= new VideoService();;
	private VideoGalleryBean ptAddVideo ;
	//private Video video;

	@BeforeEach
	public void setup() {

		ptAddVideo = new VideoGalleryBean();
		videoservice.setPtAddVideo(ptAddVideo);
	}

	@Test
	public void testGetMemberVideos() {
		//ArrayList<Video> videos = new ArrayList<>();
		Video video = new Video(1, "Yoga Basics", "/resources/videos/yoga.mp4",
				"2025-08-19", "Intro to yoga", "Public", "Yoga", "Joe Soundman");
		Video video1 = new Video(1, "Strength", "/resources/videos/yoga.mp4",
				"2025-08-19", "Weights", "Private", "Yoga", "Joe Soundman");
		Video video2 = new Video(1, "Strength", "/resources/videos/yoga.mp4",
				"2025-08-19", "Weights", "Hidden", "Yoga", "Joe Soundman");

		ptAddVideo.addVideoTest(video);
		ptAddVideo.addVideoTest(video1);
		ptAddVideo.addVideoTest(video2);//add all the videos
		
		assertTrue(videoservice.getMemberVideos().contains(video1));
		assertTrue(videoservice.getMemberVideos().contains(video));
		assertFalse(videoservice.getMemberVideos().contains(video2));
		
		
		assertNotEquals(ptAddVideo.getVideos(),videoservice.getMemberVideos());
		assertEquals(ptAddVideo.getVideos().size()-1, videoservice.getMemberVideos().size()); // -1 because hidden video isnt in the member videos
	}

	
	@Test
	public void testGetNonMemberVideos() {
		//ArrayList<Video> videos = new ArrayList<>();
		Video video = new Video(1, "Yoga Basics", "/resources/videos/yoga.mp4",
				"2025-08-19", "Intro to yoga", "Public", "Yoga", "Joe Soundman");
		Video video1 = new Video(1, "Strength", "/resources/videos/yoga.mp4",
				"2025-08-19", "Weights", "Private", "Yoga", "Joe Soundman");
		Video video2 = new Video(1, "Strength", "/resources/videos/yoga.mp4",
				"2025-08-19", "Weights", "Hidden", "Yoga", "Joe Soundman");
		ptAddVideo.addVideoTest(video);
		ptAddVideo.addVideoTest(video1);
		assertFalse(videoservice.getNonMemberVideos().contains(video1));
		assertTrue(videoservice.getMemberVideos().contains(video));
		assertFalse(videoservice.getMemberVideos().contains(video2));
		assertEquals(ptAddVideo.getVideos(),videoservice.getMemberVideos());
		assertEquals(ptAddVideo.getVideos().size(), videoservice.getMemberVideos().size());
	}

	@Test
	public void testGetTrainerVideos() {
		User user1 = new User();
		user1.setRole("PT");
		user1.setFirstName("Joe");
		user1.setSecondName("Soundman");
		UserService.getUsers().add(user1);
		assertTrue(videoservice.getAllTrainers().contains(user1)); // add new user


		Video video = new Video(1, "Yoga Basics", "/resources/videos/yoga.mp4",
				"2025-08-19", "Intro to yoga", "Public", "Yoga", "Joe Soundman");
		ptAddVideo.addVideoTest(video); //add new video

		videoservice.setSelectedTrainerName("Joe Soundman"); //set the filter

		List<Video> trainerVideos = videoservice.getSelectedTrainerVideos(); 
		assertTrue(trainerVideos.contains(video));	 	
	}

	@Test
	public void testGetTrainers() {
		User user1 = new User();
		user1.setRole("PT");
		user1.setFirstName("Joe");
		user1.setSecondName("Soundman");
		UserService.getUsers().add(user1);
		assertTrue(videoservice.getAllTrainers().contains(user1)); // add new user


		List<User> trainers = videoservice.getAllTrainers(); 
		assertTrue(trainers.contains(user1));	 	
	}
	@Test
	public void testGetSelectedTrainerName() {
		User user1 = new User();
		user1.setRole("PT");
		user1.setFirstName("John");
		user1.setSecondName("Peters");
		UserService.getUsers().add(user1);
		assertTrue(videoservice.getAllTrainers().contains(user1)); // add new user
		videoservice.setSelectedTrainerName(user1.getFirstName().concat(" " + user1.getSecondName()));
		assertEquals( "John Peters", videoservice.getSelectedTrainerName());
	}
	
	@Test
	public void testShowVideo() {
		User user1 = new User();
		user1.setRole("PT");
		user1.setFirstName("John");
		user1.setSecondName("Peters");
		UserService.getUsers().add(user1);
		assertTrue(videoservice.getAllTrainers().contains(user1)); // add new user
		videoservice.setSelectedTrainerName("John Peters");
		List<Video> trainerVideos = videoservice.getSelectedTrainerVideos();  
		
		assertEquals(trainerVideos, videoservice.showVideosMembers());
		videoservice.setSelectedTrainerName(null);
		assertEquals(videoservice.getMemberVideos(), videoservice.showVideosMembers()); // so if filter is null itll show the default
		videoservice.setSelectedTrainerName("");
		assertEquals(videoservice.getMemberVideos(), videoservice.showVideosMembers()); // so if filter is null itll show the default
		
	}

	
	
	@Test
	public void testGetNonMemberPublicVideos_withAlexAndJoe() {
	 
	    // Add video
	    Video vPublicJoe = new Video(1, "Yoga Basics", "/res/yoga.mp4",
	            "2025-08-19", "Intro", "Public", "Yoga", "Joe Soundman");
	    Video vPrivateJoe = new Video(2, "Weights", "/res/weights.mp4",
	            "2025-08-19", "Strength", "Private", "Weights", "Joe Soundman");
	    Video vHiddenAlex = new Video(3, "Pilates", "/res/pilates.mp4",
	            "2025-08-19", "Core", "Hidden", "Pilates", "Alex Byrne");
	   

	    ptAddVideo.getVideos().add(vPublicJoe);
	    ptAddVideo.getVideos().add(vPrivateJoe);
	    ptAddVideo.getVideos().add(vHiddenAlex);
	  
	    List<Video> result = videoservice.getNonMemberPublicVideos();

	    // Test
	    assertTrue(result.contains(vPublicJoe)); //public true
	    assertFalse(result.contains(vPrivateJoe)); //private false
	    assertFalse(result.contains(vHiddenAlex)); //hidden false
	    assertEquals(1, result.size()); //1 public videos
	}
	
	
	@Test
	public void testShowPublicVideosFilteredByTrainer() {
		  // Add video
	    Video JoeSoundman = new Video(1, "Yoga Basics", "/res/yoga.mp4",
	            "2025-08-19", "Intro", "Public", "Yoga", "Joe Soundman");
	    Video JoeSoundman2 = new Video(2, "Weights", "/res/weights.mp4",
	            "2025-08-19", "Strength", "Public", "Weights", "Joe Soundman");
	    Video alexByrne = new Video(3, "Pilates", "/res/pilates.mp4",
	            "2025-08-19", "Core", "Public", "Pilates", "Alex Byrne");
	   

	    ptAddVideo.getVideos().add(JoeSoundman);
	    ptAddVideo.getVideos().add(JoeSoundman2);
	    ptAddVideo.getVideos().add(alexByrne);
	  
	    videoservice.setSelectedTrainerName("Joe Soundman");
	    List<Video> filteredTrainerResult = videoservice.showPublicVideosFilteredByTrainer();
	    
	    assertTrue(filteredTrainerResult.contains(JoeSoundman)); //match
	    assertFalse(filteredTrainerResult.contains(alexByrne)); // no match
	    assertEquals(2, filteredTrainerResult.size()); // 2 public and by JoeSoundman
	    
		
	}

}