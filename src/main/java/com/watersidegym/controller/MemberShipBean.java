package com.watersidegym.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import com.watersidegym.model.User;

@ManagedBean(name = "membershipBean")
@SessionScoped
public class MemberShipBean {

    private long paymentStartTime; // for session timeout
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
            this.paymentStartTime = System.currentTimeMillis();
            
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
        if (isPaymentTimedOut()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment Timeout",
                            "Your payment session has expired. Please select a plan again."));
            return "index.xhtml?faces-redirect=true";
        }

        if (!isValidPayPal()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment Failed", "Invalid PayPal credentials."));
            return null;
        }

        // get the loggedin user from the LoginBean
		FacesContext context = FacesContext.getCurrentInstance();
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser();

		// update user role to Member
		if (loggedInUser != null) {
			loggedInUser.setRole("Member");
		}

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Payment Successful",
                        "Your " + this.membershipPlan + " membership has been activated."));

        return "payment-success.xhtml?faces-redirect=true";
    }
    
    public boolean isPaymentTimedOut() {
        long now = System.currentTimeMillis();
        long elapsed = now - paymentStartTime;
        return elapsed > (5 * 60 * 1000); // 5 minutes
    }

    public boolean isValidPayPal() {
    	if ("user@example.com".equalsIgnoreCase(paypalEmail)
                && "password".equals(paypalPassword)) {
    		return true;
    	}
    	else {
    		return false ;
    	}
    }
    
    public void checkPaymentSession() {
        if (isPaymentTimedOut()) { //similar to login check
        	   try {
   	        	FacesContext.getCurrentInstance().getExternalContext().redirect("sessionExpired.xhtml");

        	   } catch (Exception e) {
   	            System.out.println("Redirect failed");
   	        }
        }
    }    
}
