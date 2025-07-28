package com.watersidegym.controller;

import com.watersidegym.model.User;
import com.watersidegym.service.UserService;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CreateAccountBean {

    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    private User user = new User();

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String register() {
        if (!userService.addUser(user)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email already registered", null));
            return null;
        }
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Account created successfully!", null));

        user = new User(); // reset form
        return "index?faces-redirect=true";
    }
}
