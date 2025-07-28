package com.watersidegym.beans;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;



@ManagedBean
@SessionScoped
public class LoginBean {

	private String email = "admin";
	private String password = "123";
	private boolean isRegistered;
	private String user = "Non-Member";


	public LoginBean() {

	}

	public LoginBean(String username, String password) {
		this.email = username;
		this.password = password;
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

		String email1= "admin";
		String password1= "123";
		if(email1.equals(email) && password1.equals(password)) {
			isRegistered = true;
			if(user.equals("Member")) {
				return "member-dashboard?faces-redirect=true";
			}  else if(user.equals("Non-Member")) {
				return "nonmember-dashboard?faces-redirect=true";
			}
			
		} else {
			isRegistered = false;
			
		}return null;


	}

}
