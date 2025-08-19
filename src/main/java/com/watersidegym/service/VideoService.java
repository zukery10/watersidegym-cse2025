package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.watersidegym.controller.PTAddVideo;
import com.watersidegym.model.User;
import com.watersidegym.model.Video;

@ManagedBean
@SessionScoped

public class VideoService {

	@ManagedProperty("#{videoControllerBean}") // reference the JSF-managed bean
	private PTAddVideo ptAddVideo;
	private String selectedTrainerName;
	private List<User> trainers = new ArrayList<>();
	private ArrayList<Video> trainerVideos = new ArrayList<>();

	@PostConstruct
	public void init() {
		setTrainers(getAllTrainers());

	}

	public void setPtAddVideo(PTAddVideo ptAddVideo) {
		this.ptAddVideo = ptAddVideo;
	}

	// for member videos
	public List<Video> getMemberVideos() {
		List<Video> memberVideos = new ArrayList<>();
		for (Video video : ptAddVideo.getVideos()) {

			if (video != null && !video.getCategory().equalsIgnoreCase("Hidden") && !memberVideos.contains(video)
					&& !video.getCategory().equalsIgnoreCase("Recently Added")) {
				memberVideos.add(video);
			}
		}
		return memberVideos;
	}

	// for non member videos
	public List<Video> getNonMemberVideos() {
		List<Video> nonMemberVideos = new ArrayList<>();
		for (Video video : ptAddVideo.getVideos()) {
			if (video != null && video.getRule().equalsIgnoreCase("Public")
					&& !video.getCategory().equalsIgnoreCase("Recently Added")) {
				nonMemberVideos.add(video);
			}
		}
		return nonMemberVideos;
	}

	public List<User> getAllTrainers() {
		trainers = new ArrayList<>();
		for (User u : UserService.getUsers()) {
			// check if user role is PT
			if ("PT".equals(u.getRole())) {
				trainers.add(u);
				System.out.println(u.getFirstName());
				System.out.println(u.getSecondName());
			}
		}
		
		return trainers;
	}

	public List<Video> getSelectedTrainerVideos() {
		trainerVideos.clear();
		for (Video video : ptAddVideo.getVideos()) {
			if (video.getUploadedBy().equals(selectedTrainerName)) {
				trainerVideos.add(video);
			}
		}
		return trainerVideos;

	}

	public String getSelectedTrainerName() {
		System.out.println(selectedTrainerName);
		return selectedTrainerName;
	}

	public void setSelectedTrainerName(String selectedTrainerName) {
		this.selectedTrainerName = selectedTrainerName;
	}

	public List<User> getTrainers() {
		return trainers;
	}

	public void setTrainers(List<User> trainers) {
		this.trainers = trainers;
	}

	public ArrayList<Video> getTrainerVideos() {
		return trainerVideos;
	}

	public void setTrainerVideos(ArrayList<Video> trainerVideos) {
		this.trainerVideos = trainerVideos;
	}

	public List<Video> showVideos() {
		if (selectedTrainerName != null && !selectedTrainerName.isEmpty()) {
			return getSelectedTrainerVideos();
		} else {
			return getMemberVideos(); 
		}

	}

}
