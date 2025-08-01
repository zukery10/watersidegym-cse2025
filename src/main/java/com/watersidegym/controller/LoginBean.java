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
			
		if (emptyFields()) {//made a method below, tried to use for testing	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Must fill all fields", null)); //output this message
			return null;
		}
		
		User user = UserService.findUserByEmail(email); // creates an object called user - once method is used, a user
														// object is made and checks		
															// if it's email is in the array in userservice
														
		if (noEmailFound(user)) { // if no email is found
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login Failed, Email Not Recognised")); // input error																											// message
			return null;
		}

		if (passwordMismatch(user)) { //if the inputted password doesnt match the passaword in the array
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed: Incorrect password", null));
			return null;
		}
		
		
		

		loggedInUser = user; //if username and password match, creates saves the user in the bean

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful!", null)); // success message

		// Clear the login form fields
		email = null;
		password = null;

		switch (loggedInUser.getRole()) { //checks if user is member or non member
		case "Member":
			return "member-dashboard";
		case "Non-Member":
			return "nonmember-dashboard";
		case "PT":
			return "personal-trainer-dashboard";
		default:
			return null;
		}
	}
	
	
	public boolean noEmailFound(User user) { //returns true if user is null
		if(user == null) {
			return true;
			
		} else return false;
	}
	
	
	public boolean passwordMismatch(User user) { //returns true if password mismatches
		if (!user.getPassword().equals(password)) {
			return true;
		} else return false;
	}
	
	
	public boolean emptyFields() { // to test
		if ( email == null || "".equals(email) || password == null || "".equals(password)  ) {  // if email or password is null or blank 
			email = null; // puts input box to blank again
			password = null;
			return true;
	} else return false;
}
	

	public String logout() { // resets all inputs fields and user
		loggedInUser = null;
		email = null;
		password = null;
		return "index"; //redirects back to index
	}
	
	

	public boolean isLoggedIn() {
		return loggedInUser != null;
	}
	
	
	public void checkLoggedIn() {
	    if (!isLoggedIn()) { //check the method isLoggedIn= if false
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