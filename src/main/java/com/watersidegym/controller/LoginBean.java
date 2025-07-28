package com.watersidegym.controller;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.watersidegym.model.User;   //importing the user java
import com.watersidegym.service.UserService; //importing the userservice java




@ManagedBean
@SessionScoped
public class LoginBean {

	private String email ;
	private String password ;
	private boolean isRegistered;
	private String user ;
	//private UserService userservice;


	// Inject UserService
	@ManagedProperty(value = "#{userService}")  //injects managed bean called userserivce into this this bean
	private UserService userService;  //creates an instance of userservice
	public LoginBean() {

	}


	public LoginBean(String username, String password) {
		this.email = username;
		this.password = password;
	}
	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


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

		User user  = userService.findUserByEmail(email);  // creates an object called user - once method is used, a user object is made and checks
															//if it's email is in the array in userservice 

		if(user == null) {  //if no email is found
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Email or Password not found.")); //input error message
			return null;
		}


		if(user.getEmail().equals(email) && user.getPassword().equals(password)) { //checks is email/password matches the input 
			isRegistered = true;
			if(user.getRole().equals("Member")) { //checks if member
				return "member-dashboard?faces-redirect=true";
			}  else if(user.getRole().equals("Non-Member")) { //checks if nonmember
				return "nonmember-dashboard?faces-redirect=true";
			}

		} else {
			isRegistered = false;

		}return null;


	}

}
