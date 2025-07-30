package com.watersidegym.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

import com.watersidegym.model.MembershipPlan;


@ManagedBean (name = "membershipBean")
@SessionScoped
public class MemberShipBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MembershipPlan membershipPlan;
    private String confirmationMessage;

    public MembershipPlan getMembershipPlan() {
        return membershipPlan;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public String choosePlan(String planCode) {
        try {
            this.membershipPlan = MembershipPlan.valueOf(planCode);
            this.confirmationMessage = "You have selected the " + membershipPlan.getLabel() + " membership plan.";

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Selected", confirmationMessage));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Plan", "The selected plan is not valid."));
        }

        return null;
    }
    
}

