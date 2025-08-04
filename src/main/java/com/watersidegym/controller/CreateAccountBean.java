package com.watersidegym.controller;

import com.watersidegym.model.User;
import com.watersidegym.service.UserService;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CreateAccountBean {
	private User user = new User();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String register() {
		if (!UserService.addUser(user)) {
			addMessage("Email already registered", FacesMessage.SEVERITY_ERROR);
			return null;
		}

		addMessage("Account created successfully!", FacesMessage.SEVERITY_INFO);
		user = new User();
		return "index";
	}

	protected void addMessage(String msg, FacesMessage.Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			context.addMessage(null, new FacesMessage(severity, msg, null));
		}
	}

	public void printUsersToConsole() {
		System.out.println("---- List of Users ----");
		for (User u : UserService.getUsers()) {
			System.out.println("Email: " + u.getEmail() + ", Name: " + u.getFirstName() + " " + u.getSecondName());
		}
		System.out.println("-----------------------");
	}
}
