package com.watersidegym.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part; // used for file upload
import com.watersidegym.model.User;
import com.watersidegym.model.Video;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@ManagedBean(name = "videoControllerBean")
@ApplicationScoped
public class VideoGalleryBean {

	// ===== List of all videos =====
	private List<Video> videos = new ArrayList<>();// initializing list for all videos preloaded
	private int nextId = 1; // video id initializer, starts with 1

	// ===== Form fields for uploading =====
	private Part uploadedFile; // the file chosen by user
	private String formTitle;
	private String formDate;
	private String formDescription;
	private String formRule = "Public";
	private String formCategory = "Recently Added";

	// Max upload size (200MB)
	private final long MAX_SIZE = 200L * 1024L * 1024L;

	@PostConstruct
	public void preload() {

		addVideo("Chest Press", "/resources/videos/CrossFit_Dan%20Bailey.mp4", "2025-08-11",
				"How to do a barbell chest press", "Hidden", "Free Weights", "Alex Byrne");
		addVideo("Squat", "/resources/videos/Jimmi_Chan_Tai%20chi.mp4", "2025-08-12", "How to do a Squat", "Public",
				"Free Weights", "Joe Soundman");
		addVideo("Plank", "/resources/videos/Kaleigh_Cohen_Core_Strength.mp4", "2025-08-12", "How to do a plank",
				"Private", "Bodyweight", "Joe Soundman");
		addVideo("Incline Walk", "/resources/videos/Kickboxing_Rebecca_Louise.mp4", "2025-08-11",
				"How to use the threadmill at incline", "Private", "Cardio Equipment", "Alex Byrne");
		addVideo("Resistance Band Curl", "/resources/videos/Pooja_Aqua_Aerobics.mp4", "2025-08-11",
				"How to do a resistance band curl", "Public", "Bands", "Alex Byrne");
		addVideo("Cable Row", "/resources/videos/Rachel_Lawrence_pilates.mp4", "2025-08-16", "How to do a cable row",
				"Hidden", "Machines", "Joe Soundman");
	}

	// ===== Adding video to the list =====
	private void addVideo(String title, String link, String date, String desc, String rule, String category,
			String uploaderUsername) {
		videos.add(new Video(nextId++, title, link, date, desc, rule, category, uploaderUsername));// this function will
																									// detect videos and
																									// add them to the
																									// list in this
																									// format
	}

	// ===== Main upload logic =====
	public String upload() {
		// 1. Checking if a file was chosen
		if (uploadedFile == null || uploadedFile.getSize() == 0) { // checking here if the video is selected to be
																	// uploaded
			showMessage(FacesMessage.SEVERITY_WARN, "Upload canceled.");// if file is not selected or file is
																		// corrupted/no weight --> return nothing -->
																		// upload canceled
			return null;
		}

		// 2. Checking file type (only mp4 or mov)
		String type = uploadedFile.getContentType();
		if (type == null || !(type.equals("video/mp4") || type.equals("video/quicktime"))) {
			showMessage(FacesMessage.SEVERITY_ERROR, "Unsupported file type. Please upload MP4 or MOV.");
			return null;
		}

		// 3. Check file size if more than 200MB
		if (uploadedFile.getSize() > MAX_SIZE) {
			showMessage(FacesMessage.SEVERITY_ERROR, "File too large. Max 200MB.");
			return null;
		}

		// 4. Get safe file name, if the file name has sapce we replace the space with
		// "%20"
		String fileName = getFileName(uploadedFile);
		String safeName = fileName.replace(" ", "%20");
		String link = "/resources/videos/" + safeName;

		// 5. Save the file into /resources/videos
		try {
			ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext(); // get
																														// the
																														// server
																														// context
																														// that
																														// is
																														// running
																														// nwo
			String savePath = sc.getRealPath("/resources/videos"); // define where to save the new file uploaded
			System.out.println(savePath);
			//// we check if we have the video folder under /resources/videos on the server
			//// like below as temp folder
			// C:\Users\35385\Desktop\TUS\software_testing\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\watersidegym\resources\videos
			if (savePath != null) {
				File folder = new File(savePath); // we define the new folder path if the folder path doesnt exist as
													// default
				if (!folder.exists())
					folder.mkdirs(); // we create the folder for the videos if not exist

				File target = new File(folder, fileName); // we create the full path for the file we want to upload
				// input the video bytes into the new file created, we write byte by byte
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
			String title = (formTitle == null || formTitle.isEmpty()) ? fileName.replaceFirst("\\.[^.]+$", "")
					: formTitle;
			String date = (formDate == null || formDate.isEmpty()) ? LocalDate.now().toString() : formDate;
			String desc = (formDescription == null) ? "Uploaded by trainer." : formDescription;

			// get the loggedin user from the LoginBean
			FacesContext context = FacesContext.getCurrentInstance();
			LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}",
					LoginBean.class);
			User loggedInUser = loginBean.getLoggedInUser();

			String firstname = loggedInUser.getFirstName();
			String secondname = loggedInUser.getSecondName();
			String fullName = firstname.concat(" " + secondname);
			addVideo(title, link, date, desc, formRule, formCategory, fullName);

			// 7. Reset form fields
			uploadedFile = null;
			formTitle = formDate = formDescription = null;
			formRule = "Public";
			formCategory = "Recently Added";

			showMessage(FacesMessage.SEVERITY_INFO, "Upload complete. Video added.");
		} catch (Exception e) {
			showMessage(FacesMessage.SEVERITY_ERROR, "Upload failed. Please try again.");
		}

		return null;
	}

	public void addVideoTest(Video video) { // testing
		videos.add(video);
	}

	// ===== Cancel upload =====
	public String cancelUpload() {
		uploadedFile = null;
		showMessage(FacesMessage.SEVERITY_INFO, "Upload canceled.");
		return null;
	}

	// ===== Helpers for showing server msgs =====
	public void showMessage(FacesMessage.Severity type, String text) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			context.addMessage(null, new FacesMessage(type, text, null));
		}
	}

	// function to get the file name that we have in the system
	// or example we have filename:C:\USER\A000000\Downloads\video_to_upload.mp4
	// the function will return video_to_upload.mp4
	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {// if the name of the video uploaded video was found in the header of
													// the request if ill get the trim the header and ge the name of the
													// file
				// here we take from the path from \ + 1 to the end
				// or example we have filename:C:\USER\A000000\Downloads\video_to_upload.mp4 ->
				// \+1 -> video_to_upload.mp4
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		// else we return video.mp4 as a fall back but it will not happen.
		return "video.mp4";
	}

	// Return only videos in a given category, newest first
	public List<Video> byCategory(String category) {
		// create a new list to return to the user with the cat selected only.
		List<Video> out = new ArrayList<>();
		// look into the preloaded list to see all the videos then u check if the video
		// you are visiting has same cat then we add to the new one we will return
		for (Video v : videos) {
			if (v.getCategory() != null && v.getCategory().equalsIgnoreCase(category)) {
				out.add(v);
			}
		}
		// we re visit the out list we created before return to the user so we can sort
		// the video in recent data order
		// newest-first by date string (YYYY-MM-DD)
		for (int i = 0; i < out.size() - 1; i++) {
			for (int j = 0; j < out.size() - i - 1; j++) {
				Video a = out.get(j);
				Video b = out.get(j + 1);

				String da = (a.getDate() == null) ? "" : a.getDate();
				String db = (b.getDate() == null) ? "" : b.getDate();

				// if current item is "older" than the next, swapping them
				if (da.compareTo(db) < 0) {
					out.set(j, b);
					out.set(j + 1, a);
				}
			}
		}

		return out;
	}

	public List<Video> getMyVideos() {
		List<Video> mine = new ArrayList<>();

		FacesContext context = FacesContext.getCurrentInstance();
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser(); // here is when the checking occurs: if the user is logged in

		if (loggedInUser == null)
			return mine; // if the user is logged in (mine is list of my videos we just created up)

		for (Video v : videos) {
			// if the user is already loggedin -> we check the email ofr the user
			// then we compare with the videos li and add the matching email only
			String firstname = loggedInUser.getFirstName();
			String secondname = loggedInUser.getSecondName();
			String fullName = firstname.concat(" " + secondname);
			if (v.getUploadedBy() != null && v.getUploadedBy().equalsIgnoreCase(fullName)) {
				// System.out.println("v.getUploadedBy() " + v.getUploadedBy());
				// here we add the email for this user
				mine.add(v);
			}
		}
		return mine;
	}

	// delete video section here
	public void deleteVideo(int id) {
		int index = -1;
		// we want to make sure first if the video is exist before delete
		// System.out.println("deleteing id >> " + id); --> for testinh

		for (Video v : videos) {
			v.setMessage(null);
		}

		for (int i = 0; i < videos.size(); i++) {
			if (videos.get(i).getId() == id) {
				index = i;
				break;
			}
		}
		// if the video is exist we will get index not -1
		// so we delete
		if (index != -1) {
			// System.out.println("video index was deleted >> " + index); --> for testing
			videos.remove(index);
			// here goes success message
		} else {
			// nothing deleted (e.g., already removed)
		}
	}

	public String redirect() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null)
			return null;

		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = (loginBean != null) ? loginBean.getLoggedInUser() : null;

		if (loggedInUser == null || loggedInUser.getRole() == null) {
			return null; // stay on the page
		}

		switch (loggedInUser.getRole()) {
		case "Member":
			return "member-dashboard?faces-redirect=true";
		case "Non-Member":
			return "nonmember-dashboard?faces-redirect=true";
		case "PT":
			return "personal-trainer-dashboard?faces-redirect=true";
		default:
			return null;
		}
	}

	// ===== Getters/Setters =====
	public List<Video> getVideos() {
		return videos;
	}

	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public String getFormDate() {
		return formDate;
	}

	public void setFormDate(String formDate) {
		this.formDate = formDate;
	}

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getFormRule() {
		return formRule;
	}

	public void setFormRule(String formRule) {
		this.formRule = formRule;
	}

	public String getFormCategory() {
		return formCategory;
	}

	public void setFormCategory(String formCategory) {
		this.formCategory = formCategory;
	}

	// ------------------------------------------------

	public List<Video> getBodyweightOnly() {
		return byCategory("Bodyweight");
	}

	public List<Video> getFreeWeightsOnly() {
		return byCategory("Free Weights");
	}

	public List<Video> getMachinesOnly() {
		return byCategory("Machines");
	}

	public List<Video> getBandsOnly() {
		return byCategory("Bands");
	}

	public List<Video> getCardioEquipmentOnly() {
		return byCategory("Cardio Equipment");
	}

	public String updateRule(Video video) {
		// clear messages on all other videos
		for (Video v : videos) {
			if (v.getId() != video.getId()) {
				v.setMessage(null);
			}
		}
		// set the message on the video that was updated
		video.setMessage("Visibility updated to " + video.getRule() + " for " + video.getTitle());
		return null;
	}

}
