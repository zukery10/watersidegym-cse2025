package com.watersidegym.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import com.watersidegym.model.PTSession;
import com.watersidegym.model.User; //importing the user java
import com.watersidegym.service.PTSessionService;
import com.watersidegym.service.UserService; //importing the userservice java
import java.util.Arrays;
import java.util.List;

@ManagedBean
@SessionScoped
public class LoginBean {

	private String email;
	private String password;
	private User loggedInUser;
    private PTSessionService ptSessionService = new PTSessionService();

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String login() {
	    if (emptyFields()) {
	        addMessage("Fill in all fields", FacesMessage.SEVERITY_ERROR);
	        return null;
	    }

	    User user = UserService.findUserByEmail(email);

	    if (user == null || passwordMismatch(user)) {
	        // Single generic message for any login failure
	        addMessage("Username or password is incorrect", FacesMessage.SEVERITY_ERROR);
	        return null;
	    }

	    loggedInUser = user;
	    addMessage("Login successful!", FacesMessage.SEVERITY_INFO);

	    return redirect(loggedInUser);
	}
	public String redirect(User loggedInUser) {
		String redirect = "";
		switch (loggedInUser.getRole()) { // checks if user is member or non member
		case "Member":
			redirect = "member-dashboard";
			break;
		case "Non-Member":
			redirect = "nonmember-dashboard";
			break;
		case "PT":
			redirect = "personal-trainer-dashboard";
			break;
		default:
			redirect = null;
			break;
		}
		return redirect;

	}
	public boolean noEmailFound(User user) { // returns true if user is null
		if (user == null) {
			return true;

		} else
			return false;
	}
	public boolean passwordMismatch(User user) { // returns true if password mismatches
		if (!user.getPassword().equals(password)) {
			return true;
		} else
			return false;
	}
	public boolean emptyFields() { // to test
		if (email == null || "".equals(email) || password == null || "".equals(password)) { // if email or password is
																							// null or blank
			email = null; // puts input box to blank again
			password = null;
			return true;
		} else
			return false;
	}

	public String logout() {
	    // clear session and redirect to home page
	    loggedInUser = null;
	    email = null;
	    password = null;
	    return "index"; // redirects to home
	}

	public boolean isLoggedIn() {
		return loggedInUser != null;
	}
	public void checkLoggedIn() {
	    if (!isLoggedIn()) {
	        try {
	            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	        } catch (Exception e) {
	            System.out.println("Redirect failed");
	        }
	        return;
	    }
	    String currentPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
	    String expectedPage = "/" + redirect(loggedInUser) + ".xhtml";

	    // List pages that are allowed for members besides their dashboard
	    List<String> allowedPagesForMembers = Arrays.asList("/browseClasses.xhtml");
	   
	    boolean isAllowedPage = false;
	    if ("Member".equals(loggedInUser.getRole())) {
	        isAllowedPage = allowedPagesForMembers.contains(currentPage);
	    }

	    if (!currentPage.equals(expectedPage) && !isAllowedPage) {
	        try {
	            FacesContext.getCurrentInstance().getExternalContext().redirect(expectedPage);
	        } catch (Exception e) {
	            System.out.println("Redirect failed");
	        }
	    }
	}
	public User getLoggedInUser() {
		return loggedInUser;
	}

	protected void addMessage(String msg, FacesMessage.Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			context.addMessage(null, new FacesMessage(severity, msg, null));
		}
	}
	public List<PTSession> getMyPTSessions() {
        if (loggedInUser != null && "PT".equalsIgnoreCase(loggedInUser.getRole())) {
            return ptSessionService.getSessionsForTrainer(loggedInUser);
        }
        return null;
    }
	//setter for loggedInUser used only in unit tests to simulate a loggedin state	 
	public void setLoggedInUser(User user) {
	    this.loggedInUser = user;
	}
}