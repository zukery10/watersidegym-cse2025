package com.watersidegym.controller;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import com.watersidegym.model.PTSession;

@ManagedBean(name = "ptSessionBean")
@SessionScoped
public class PTSessionBean {

	PTSession ptSession = new PTSession();

	
	
	public PTSession getPtSession() {
		return ptSession;
	}



	public void setPtSession(PTSession ptSession) {
		this.ptSession = ptSession;
	}



	public String book() {
		
		boolean hasError = false;
		FacesContext fc = FacesContext.getCurrentInstance();
		
		if(ptSession.getTrainerEmail() == null || ptSession.getTrainerEmail().isEmpty()) {
			 fc.addMessage("ptForm:trainer", new FacesMessage("Please choose a trainer."));
			hasError = true;
			
		}
		
		if(ptSession.getSessionDate() == null ) {
			 fc.addMessage("ptForm:date", new FacesMessage("Please select a date"));
			hasError = true;
			
		}
		
		if(ptSession.getSessionTime() == null || ptSession.getSessionTime().isEmpty()) {
			 fc.addMessage("ptForm:time", new FacesMessage("Please select a time."));
			hasError = true;
			
		}
		
		if(ptSession.getDuration() == null || ptSession.getDuration().isEmpty()) {
			 fc.addMessage("ptForm:dur", new FacesMessage("Please select a duration."));
			hasError = true;
			
		}
		
		 if (hasError) {
	            return null; // if error - stay on the same page
	        }
		 fc.addMessage(null, new FacesMessage("Booking Confirmed"));
		//return null;
		 return "member-dashboard?faces-redirect=true";
	}
	
	
}
