package com.watersidegym.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;



@ManagedBean(name = "membershipBean")
@SessionScoped
public class MemberShipBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String membershipPlan;
    private String membershipPrice;
	private String confirmationMessage;

    private String paypalEmail;
    private String paypalPassword;

    
    public String getMembershipPrice() {
		return membershipPrice;
	}

	public void setMembershipPrice(String membershipPrice) {
		this.membershipPrice = membershipPrice;
	}

	public void setMembershipPlan(String membershipPlan) {
		this.membershipPlan = membershipPlan;
	}

    public String getMembershipPlan() {
        return membershipPlan;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public String getPaypalPassword() {
        return paypalPassword;
    }

    public void setPaypalPassword(String paypalPassword) {
        this.paypalPassword = paypalPassword;
    }

    public String choosePlan(String planCode, String price) {
        try {
            this.membershipPlan = planCode;
            this.membershipPrice = price;
            this.confirmationMessage = "You have selected the " + planCode + " membership plan, with price of " + price;

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Selected", confirmationMessage));

            return "payment.xhtml?faces-redirect=true";

        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Plan", "The selected plan is not valid."));
            return null;
        }
    }

    public String processPaymentPayPal() {
        if (!isValidPayPal()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment Failed", "Invalid PayPal credentials."));
            return null;
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Payment Successful",
                        "Your " + this.membershipPlan + " membership has been activated."));

        return "payment-success.xhtml?faces-redirect=true";
    }


    public boolean isValidPayPal() {
    	if ("user@example.com".equals(paypalEmail)
                && "password".equals(paypalPassword)) {
    		return true;
    	}
    	else {
    		return false ;
    	}
    }
}
