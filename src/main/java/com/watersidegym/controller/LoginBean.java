package com.watersidegym.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import com.watersidegym.model.User; //importing the user java
import com.watersidegym.service.UserService; //importing the userservice java

@ManagedBean
@SessionScoped
public class LoginBean {

	private String email;
	private String password;
	private User loggedInUser;

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
		User user = UserService.findUserByEmail(email); // creates an object called user - once method is used, a user
														// object is made and checks
														// if it's email is in the array in userservice
		if (user == null) { // if no email is found
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login Failed, Email Not Recognised")); // input
																														// error
																														// message
			return null;
		}

		if (!user.getPassword().equals(password)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed: Incorrect password", null));
			return null;
		}

		loggedInUser = user;

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful!", null));

		// Clear the login form fields
		email = null;
		password = null;

		switch (loggedInUser.getRole()) {
		case "Member":
			return "member-dashboard?faces-redirect=true";
		case "Non-Member":
			return "nonmember-dashboard?faces-redirect=true";
		default:
			return null;
		}
	}

	public String logout() {
		loggedInUser = null;
		email = null;
		password = null;
		return "index?faces-redirect=true";
	}

	public boolean isLoggedIn() {
		return loggedInUser != null;
	}
	
	public void checkLoggedIn() {
	    if (!isLoggedIn()) {
	        try {
	        	FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
	        } catch (Exception e) {
	            System.out.println("Redirect failed");
	        }
	    }
	}
	
	public User getLoggedInUser() {
	    return loggedInUser;
	}
}