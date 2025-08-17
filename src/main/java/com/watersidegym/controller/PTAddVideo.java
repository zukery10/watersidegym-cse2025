package com.watersidegym.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;     // used for file upload
import java.io.*;
import java.time.LocalDate;
import java.util.*;

@ManagedBean(name = "videoControllerBean")
@ApplicationScoped
public class PTAddVideo {

	// ===== Inner class to represent a Video =====
	public static class Video {
		private int id;
		private String title;
		private String link;        // Path to file (ex: /resources/videos/file.mp4)
		private String date;        // Date in YYYY-MM-DD
		private String description;
		private String rule;        // Who can watch: Member / Non-Member
		private String category;    // Group name (ex: Recently Added)
		private String uploaderUsername;
		
		public Video(int id, String title, String link, String date,
				String description, String rule, String category) {
			this.id = id;
			this.title = title;
			this.link = link;
			this.date = date;
			this.description = description;
			this.rule = rule;
			this.category = category;
		}

		// Getters + Setters
		public int getId() { return id; }
		public String getTitle() { return title; }
		public String getLink() { return link; }
		public String getDate() { return date; }
		public String getDescription() { return description; }
		public String getRule() { return rule; }
		public String getCategory() { return category; }

		public void setId(int id) { this.id = id; }
		public void setTitle(String title) { this.title = title; }
		public void setLink(String link) { this.link = link; }
		public void setDate(String date) { this.date = date; }
		public void setDescription(String description) { this.description = description; }
		public void setRule(String rule) { this.rule = rule; }
		public void setCategory(String category) { this.category = category; }
	}

	// ===== List of all videos =====
	private List<Video> videos = new ArrayList<>();
	private int nextId = 1; 

	// ===== Form fields for uploading =====
	private Part uploadedFile; // the file chosen by user
	private String formTitle;
	private String formDate;
	private String formDescription;
	private String formRule = "Non-Member";
	private String formCategory = "Recently Added";

	// Max upload size (200MB)
	private final long MAX_SIZE = 200L * 1024L * 1024L;

	// ===== Adding some sample videos at startup =====
	@PostConstruct
	public void preload() {
		addVideo("Tai Chi", "/resources/videos/Jimmi_Chan_Tai%20chi.mp4", "2025-08-12", "Calming Tai Chi", "Non-Member", "Recently Added");
		addVideo("Yoga", "/resources/videos/Francine_Cipollone_Yoga.mp4", "2025-08-12", "Gentle flow", "Non-Member", "Recently Added");
		addVideo("Core Strength", "/resources/videos/Kaleigh_Cohen_Core_Strength.mp4", "2025-08-12", "Core routine", "Member", "Recently Added");
		addVideo("Core Strength1", "/resources/videos/Kaleigh_Cohen_Core_Strength.mp4", "2025-08-12", "Core routine", "Member", "Recently Added");
	}

	// ===== Adding video to the list =====
	private void addVideo(String title, String link, String date, String desc, String rule, String category) {
		videos.add(new Video(nextId++, title, link, date, desc, rule, category));
	}

	// ===== Main upload logic =====
	public String upload() {
		// 1. Checking if a file was chosen
		if (uploadedFile == null || uploadedFile.getSize() == 0) {
			showMessage(FacesMessage.SEVERITY_WARN, "Upload canceled.");
			return null;
		}

		// 2. Checking file type (only mp4 or mov)
		String type = uploadedFile.getContentType();
		if (type == null || !(type.equals("video/mp4") || type.equals("video/quicktime"))) {
			showMessage(FacesMessage.SEVERITY_ERROR, "Unsupported file type. Please upload MP4 or MOV.");
			return null;
		}

		// 3. Check file size
		if (uploadedFile.getSize() > MAX_SIZE) {
			showMessage(FacesMessage.SEVERITY_ERROR, "File too large. Max 200MB.");
			return null;
		}

		// 4. Get safe file name
		String fileName = getFileName(uploadedFile);
		String safeName = fileName.replace(" ", "%20");
		String link = "/resources/videos/" + safeName;

		// 5. Save the file into /resources/videos
		try {
			ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String savePath = sc.getRealPath("/resources/videos");
			if (savePath != null) {
				File folder = new File(savePath);
				if (!folder.exists()) folder.mkdirs();

				File target = new File(folder, fileName);
				try (InputStream in = uploadedFile.getInputStream();
						FileOutputStream out = new FileOutputStream(target)) {
					byte[] buffer = new byte[1024];
					int bytes;
					while ((bytes = in.read(buffer)) != -1) {
						out.write(buffer, 0, bytes);
					}
				}
			}

			// 6. Add to video list
			String title = (formTitle == null || formTitle.isEmpty()) ? fileName.replaceFirst("\\.[^.]+$", "") : formTitle;
			String date = (formDate == null || formDate.isEmpty()) ? LocalDate.now().toString() : formDate;
			String desc = (formDescription == null) ? "Uploaded by trainer." : formDescription;

			addVideo(title, link, date, desc, formRule, formCategory);

			// 7. Reset form fields
			uploadedFile = null;
			formTitle = formDate = formDescription = null;
			formRule = "Non-Member";
			formCategory = "Recently Added";

			showMessage(FacesMessage.SEVERITY_INFO, "Upload complete. Video added.");
		} catch (Exception e) {
			showMessage(FacesMessage.SEVERITY_ERROR, "Upload failed. Please try again.");
		}

		return null; // stay on same page
	}

	// ===== Cancel upload =====
	public String cancelUpload() {
		uploadedFile = null;
		showMessage(FacesMessage.SEVERITY_INFO, "Upload canceled.");
		return null;
	}

	// ===== Helpers =====
	private void showMessage(FacesMessage.Severity type, String text) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, text, null));
	}

	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return "video.mp4";
	}


	// Return only videos in a given category, newest first
	private List<Video> byCategory(String category) {
		List<Video> out = new ArrayList<>();
		for (Video v : videos) {
			if (v.getCategory() != null && v.getCategory().equalsIgnoreCase(category)) {
				out.add(v);
			}
		}
		// newest-first by date string (YYYY-MM-DD)
		for (int i = 0; i < out.size() - 1; i++) {
		    for (int j = 0; j < out.size() - i - 1; j++) {
		        Video a = out.get(j);
		        Video b = out.get(j + 1);

		        String da = (a.getDate() == null) ? "" : a.getDate();
		        String db = (b.getDate() == null) ? "" : b.getDate();

		        // if current item is "older" than the next, swap them
		        if (da.compareTo(db) < 0) {
		            out.set(j, b);
		            out.set(j + 1, a);
		        }
		    }
		}

		return out;
	}

	// ===== Getters/Setters =====
	public List<Video> getVideos() { return videos; }

	public Part getUploadedFile() { return uploadedFile; }
	public void setUploadedFile(Part uploadedFile) { this.uploadedFile = uploadedFile; }

	public String getFormTitle() { return formTitle; }
	public void setFormTitle(String formTitle) { this.formTitle = formTitle; }
	public String getFormDate() { return formDate; }
	public void setFormDate(String formDate) { this.formDate = formDate; }
	public String getFormDescription() { return formDescription; }
	public void setFormDescription(String formDescription) { this.formDescription = formDescription; }
	public String getFormRule() { return formRule; }
	public void setFormRule(String formRule) { this.formRule = formRule; }
	public String getFormCategory() { return formCategory; }
	public void setFormCategory(String formCategory) { this.formCategory = formCategory; }
	public List<Video> getRecentlyAdded() {
		return byCategory("Recently Added");
	}

	public List<Video> getYogaOnly() {
		return byCategory("Yoga");
	}
	
	public List<Video> getZumbaOnly() {
		return byCategory("Zumba");
	}
	
	public List<Video> getCoreOnly() {
		return byCategory("Core Strength");
	}
}
