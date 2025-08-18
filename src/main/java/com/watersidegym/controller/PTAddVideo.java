package com.watersidegym.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;     // used for file upload

import com.watersidegym.model.User;
import com.watersidegym.model.Video;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@ManagedBean(name = "videoControllerBean")
@ApplicationScoped
public class PTAddVideo {

	// ===== List of all videos =====
	private List<Video> videos = new ArrayList<>();
	private int nextId = 1; 

	// ===== Form fields for uploading =====
	private Part uploadedFile; // the file chosen by user
	private String formTitle;
	private String formDate;
	private String formDescription;
	private String formRule = "Public";
	private String formCategory = "Recently Added";

	// Max upload size (200MB)
	private final long MAX_SIZE = 200L * 1024L * 1024L;

	// ===== Adding some sample videos at startup =====
	@PostConstruct
	public void preload() {
		// all in recently added secton
		addVideo("Tai Chi", "/resources/videos/Jimmi_Chan_Tai%20chi.mp4", "2025-08-12", "Calming Tai Chi", "Public", "Recently Added", "PT@example.com");
		
		addVideo("Core Strength", "/resources/videos/Kaleigh_Cohen_Core_Strength.mp4", "2025-08-12", "Core routine", "Private", "Recently Added", "PTXYZ@example.com");
		addVideo("Pilates", "/resources/videos/Rachel_Lawrence_pilates.mp4", "2025-08-16", "Pilates flow", "Private", "Recently Added", "PT@example.com");
		addVideo("Aqua Aerobics", "/resources/videos/Pooja_Aqua_Aerobics.mp4", "2025-08-11", "Aqua Aerobics routine", "Private", "Recently Added", "PT@example.com");
		addVideo("Kickboxing", "/resources/videos/Kickboxing_Rebecca_Louise.mp4", "2025-08-11", "Kickboxing excercises", "Public", "Recently Added", "PT@example.com");
		addVideo("CrossFit Hidden", "/resources/videos/CrossFit_Dan%20Bailey.mp4", "2025-08-11", "CrossFit HIIT", "Hidden", "Recently Added", "PT@example.com");
		
		//------------------------------------------
		// all in related sections
		addVideo("Tai Chi", "/resources/videos/Jimmi_Chan_Tai%20chi.mp4", "2025-08-12", "Calming Tai Chi", "Public", "Tai Chi", "PT@example.com");
		addVideo("Yoga", "/resources/videos/Francine_Cipollone_Yoga.mp4", "2025-08-12", "Gentle flow", "Public", "Yoga", "PT@example.com");
		addVideo("Yoga", "/resources/videos/Francine_Cipollone_Yoga_2.mp4", "2025-08-12", "Gentle flow", "Public", "Yoga", "PT@example.com");
		addVideo("Yoga", "/resources/videos/Francine_Cipollone_Yoga_3.mp4", "2025-08-12", "Gentle flow", "Public", "Yoga", "PT@example.com");
		addVideo("Core Strength", "/resources/videos/Kaleigh_Cohen_Core_Strength.mp4", "2025-08-12", "Core routine", "Private", "Core Strength", "PTXYZ@example.com");
		addVideo("Zumba", "/resources/videos/Zumba_Holly_Corrente.mp4", "2025-08-15", "Zumba dance", "Private", "Zumba", "PT2@example.com");
		addVideo("Pilates", "/resources/videos/Rachel_Lawrence_pilates.mp4", "2025-08-16", "Pilates flow", "Private", "Pilates", "PT@example.com");
		addVideo("Aqua Aerobics", "/resources/videos/Pooja_Aqua_Aerobics.mp4", "2025-08-11", "Aqua Aerobics routine", "Private", "Aqua Aerobics", "PT@example.com");
		addVideo("Kickboxing", "/resources/videos/Kickboxing_Rebecca_Louise.mp4", "2025-08-11", "Kickboxing excercises", "Public", "Kickboxing", "PT@example.com");
		addVideo("CrossFit", "/resources/videos/CrossFit_Dan%20Bailey.mp4", "2025-08-11", "CrossFit HIIT", "Public", "CrossFit", "PT@example.com");
		addVideo("Tabata", "/resources/videos/Anna_Tabata.mp4", "2025-08-07", "Tabata dance", "Private", "Tabata", "PT2@example.com");
		addVideo("Tabata", "/resources/videos/Anna_Tabata_2.mp4", "2025-08-07", "Tabata dance", "Private", "Tabata", "PT2@example.com");
		addVideo("Tabata", "/resources/videos/Anna_Tabata_3.mp4", "2025-08-07", "Tabata dance", "Private", "Tabata", "PT2@example.com");
	}

	// ===== Adding video to the list =====
	private void addVideo(String title, String link, String date, String desc, String rule, String category, String uploaderUsername) {
		videos.add(new Video(nextId++, title, link, date, desc, rule, category, uploaderUsername));
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

			// get the loggedin user from the LoginBean
			FacesContext context = FacesContext.getCurrentInstance();
			LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
			User loggedInUser = loginBean.getLoggedInUser();


			addVideo(title, link, date, desc, formRule, formCategory, loggedInUser.getEmail());

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
		User loggedInUser = loginBean.getLoggedInUser(); //here is when the checking occurs: if the user is logged in

		if (loggedInUser == null) return mine; //if the user is logged in (mine is list of my videos we just created up)

		String email = loggedInUser.getEmail();
		//System.out.println("LOGGEDN IN EMAIL IS " + email);
		for (Video v : videos) {
			//if the user is already loggedin -> we check the email ofr the user
			// then we compare with the videos li and add the matching email only 
			if (v.getUploadedBy() != null && v.getUploadedBy().equalsIgnoreCase(email)) {
				//System.out.println("v.getUploadedBy() " + v.getUploadedBy());
				//here we add the email for this user
				mine.add(v);
			}
		}
		return mine;
	}

	// ===== Getters/Setters =====
	public List<Video> getVideos() { return videos; }

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
		this.formTitle = formTitle; }
	public String getFormDate() { return formDate; 
	}
	public void setFormDate(String formDate) { 
		this.formDate = formDate; }
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

	//------------------------------------------------

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

	public List<Video> getTaichiOnly() {
		return byCategory("Tai Chi");
	}

	public List<Video> getKickboxingOnly() {
		return byCategory("Kickboxing");
	}

	public List<Video> getCrossfitOnly() {
		return byCategory("CrossFit");
	}

	public List<Video> getAquaaerobicsOnly() {
		return byCategory("Aqua Aerobics");
	}

	public List<Video> getPilatesOnly() {
		return byCategory("Pilates");
	}

	public List<Video> getTabataOnly() {
		return byCategory("Tabata");
	}
}
