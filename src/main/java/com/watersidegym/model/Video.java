package com.watersidegym.model;

public class Video {

	private int id;
	private String title;
	private String link; // Path : /resources/videos/file.mp4
	private String date; // YYYY-MM-DD
	private String description;
	private String rule; // Private / Public / Hidden
	private String category; // e.g., Recently Added, Yoga, etc.
	private String uploadedBy; // uploader email/username
	private String message;

	public Video(int id, String title, String link, String date, String description, String rule, String category,
			String uploadedBy) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.date = date;
		this.description = description;
		this.rule = rule;
		this.category = category;
		this.uploadedBy = uploadedBy;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public String getRule() {
		return rule;
	}

	public String getCategory() {
		return category;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

//------------------------------------------------------------
	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
