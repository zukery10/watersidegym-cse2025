package com.watersidegym.service;

import java.util.ArrayList;

import com.watersidegym.controller.PTAddVideo;
import com.watersidegym.model.Video;

public class VideoService {
	PTAddVideo ptaddvideo;
	private ArrayList<Video> allvideos = new ArrayList<>(ptaddvideo.getVideos());
	private ArrayList<Video> membervideos = new ArrayList<>();
	private ArrayList<Video> nonmembervideos = new ArrayList<>();
	
	public ArrayList<Video> membervideos(ArrayList<Video> allvideos){
		
		for(Video video : allvideos) {
			if(video != null && video.getRule().equalsIgnoreCase("private")) {
				membervideos.add(video);
			}
		}
		return membervideos;
	}
}
