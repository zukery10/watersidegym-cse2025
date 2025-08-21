package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import com.watersidegym.controller.VideoGalleryBean;
import com.watersidegym.model.User;
import com.watersidegym.model.Video;

@ManagedBean
@SessionScoped

public class VideoService {

	@ManagedProperty("#{videoControllerBean}") // reference the JSF-managed bean
	private VideoGalleryBean ptAddVideo;
	private String selectedTrainerName;
	private List<User> trainers = new ArrayList<>();
	private ArrayList<Video> trainerVideos = new ArrayList<>();

	@PostConstruct
	public void init() {
		setTrainers(getAllTrainers());

	}

	public void setPtAddVideo(VideoGalleryBean ptAddVideo) {
		this.ptAddVideo = ptAddVideo;
	}

	// for member videos
	public List<Video> getMemberVideos() {
		List<Video> memberVideos = new ArrayList<>();
		for (Video video : ptAddVideo.getVideos()) {

			if (video != null && !video.getRule().equalsIgnoreCase("Hidden") && !memberVideos.contains(video)
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
			}
		}

		return trainers;
	}

	public List<Video> getSelectedTrainerVideos() {
		List<Video> v = getMemberVideos(); // make new array list  of member vids to compare trainer name to
		trainerVideos.clear();
		for (Video video : v) {
			if (video.getUploadedBy().equals(selectedTrainerName)) {
				trainerVideos.add(video);
			}
		}
		return trainerVideos;
	}

	public String getSelectedTrainerName() {

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

	// for members
	public List<Video> showVideosMembers() {
		if (selectedTrainerName != null && !selectedTrainerName.isEmpty()) {
			return getSelectedTrainerVideos();
		} else
			return getMemberVideos();
	}

	// nonmember can view only public video
		public List<Video> getNonMemberPublicVideos() {
			List<Video> nonMemberPublicVideos = new ArrayList<>();
			for (Video video : ptAddVideo.getVideos()) {
				if (video != null && video.getRule().equalsIgnoreCase("Public")) {
					nonMemberPublicVideos.add(video);
				}
			}
			return nonMemberPublicVideos;
		}
	
	// nonmember view public videos filtered by trainer
	public List<Video> showPublicVideosFilteredByTrainer() {
	    List<Video> filterByTrainerVideos = new ArrayList<>();

	    if (selectedTrainerName != null && !selectedTrainerName.isEmpty()) {
	        for (Video video : getSelectedTrainerVideos()) {
	            if (video != null && video.getRule().equalsIgnoreCase("Public")) {
	            	filterByTrainerVideos.add(video);
	            }
	        }
	        return filterByTrainerVideos;
	    } else {
	        return getNonMemberPublicVideos();
	    }
	}

}