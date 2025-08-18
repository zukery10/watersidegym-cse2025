package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;

import com.watersidegym.controller.PTAddVideo;
import com.watersidegym.model.Video;
@ManagedBean
@SessionScoped
public class VideoService {
	private PTAddVideo ptAddVideo; 
	private List<Video> allVideos = new ArrayList<>(ptAddVideo.getVideos());;
	

	public List<Video> getMemberVideos() {
		List<Video> memberVideos = new ArrayList<>();
		for (Video video : allVideos) {

			if (video != null && video.getRule().equalsIgnoreCase("Private")) {
				memberVideos.add(video);
				System.out.println("Printing true for member videos!!");
			} else System.out.println("Printing false");
		}
		return memberVideos;
	}

	public List<Video> getNonMemberVideos() {
		List<Video> nonMemberVideos = new ArrayList<>();
		for (Video video : allVideos) {
			if (video != null && video.getRule().equalsIgnoreCase("public")) {
				nonMemberVideos.add(video);
			}
		}
		return nonMemberVideos;
	}
}
