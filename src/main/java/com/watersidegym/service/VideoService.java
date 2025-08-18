package com.watersidegym.service;

import java.util.ArrayList;
import java.util.List;

import com.watersidegym.controller.PTAddVideo;
import com.watersidegym.model.Video;

public class VideoService {
	
	private PTAddVideo ptaddvideo;
	private ArrayList<Video> allvids = new ArrayList<>(ptaddvideo.getVideos());
	private ArrayList<Video> membervids = new ArrayList<>();
	private ArrayList<Video> nonmembervids = new ArrayList<>();
	
	public List<Video> memberVideos(ArrayList<Video> allvids){
		
		for(Video video : allvids) {
			if(video != null && video.getRule().equalsIgnoreCase("private")) {
				membervids.add(video);
			}		
		}
		return membervids;
	}
	
	public List<Video> nonmemberVideos(ArrayList<Video> allvids){
		
		for(Video video : allvids) {
			if(video != null && video.getRule().equalsIgnoreCase("public")) {
				nonmembervids.add(video);
			}		
		}
		return nonmembervids;
	}

	public ArrayList<Video> getMembervids() {
		return membervids;
	}

	public ArrayList<Video> getNonmembervids() {
		return nonmembervids;
	}
	
	
}
