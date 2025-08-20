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
	private final long paymentTimeoutMillis = 5 * 60 * 1000;
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

			addMessage("You have selected the " + membershipPlan + " membership plan, with price of " + membershipPrice,
			           FacesMessage.SEVERITY_INFO);

			// reset classPayment to false because the user is paying for a membership,
			// not a class, so the class payment flow should be disabled.
			FacesContext context = FacesContext.getCurrentInstance();
			GymClassBean gymClassBean = context.getApplication().evaluateExpressionGet(context, "#{gymClassBean}",
					GymClassBean.class);
			gymClassBean.setClassPayment(false);
			//System.out.println(membershipPlan);

			return "payment.xhtml?faces-redirect=true";
		} catch (IllegalArgumentException e) {
			addMessage("The selected plan is not valid.", FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}

	public String processPaymentPayPal() {
		if (isPaymentTimedOut()) {
			addMessage("Your payment session has expired. Please select a plan again.", FacesMessage.SEVERITY_ERROR);
			return "index.xhtml?faces-redirect=true";
			
			
		}

		if (!isValidPayPal()) {
			addMessage("Invalid PayPal credentials.", FacesMessage.SEVERITY_ERROR);
			return null;
		}

		// get the loggedin user from the LoginBean
		FacesContext context = FacesContext.getCurrentInstance();
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser();

		
		
		// update user role to Member
		if (loggedInUser != null) {
			loggedInUser.setRole("Member");
			
			 // assign PT sessions based on membership plan
	        if ("3 Month Plan".equalsIgnoreCase(membershipPlan)) {
	            loggedInUser.setRemainingPTSessions(1);
	        } else if ("6 Month Plan".equalsIgnoreCase(membershipPlan)) {
	            loggedInUser.setRemainingPTSessions(2);
	        } else if ("12 Month Plan".equalsIgnoreCase(membershipPlan)) {
	            loggedInUser.setRemainingPTSessions(3);
	        }
		}

		addMessage("Your " + membershipPlan + " membership has been activated.", FacesMessage.SEVERITY_INFO);
		return "payment-success.xhtml?faces-redirect=true";
	}

	public boolean isPaymentTimedOut() {
		long now = System.currentTimeMillis();
		long elapsed = now - paymentStartTime;
		return elapsed > (5 * 60 * 1000); // 5 minutes
	}

	public boolean isValidPayPal() {
		if ("user@example.com".equalsIgnoreCase(paypalEmail) && "password".equals(paypalPassword)) {
			return true;
		} else {
			return false;
		}
	}

	public void checkPaymentSession() {
		if (isPaymentTimedOut()) { // similar to login check
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("sessionExpired.xhtml");

			} catch (Exception e) {
				System.out.println("Redirect failed");
			}
		}
	}

	public String getMembershipPaymentRemainingTime() {
		if (paymentStartTime == 0) {
			return "";
		}
		long now = System.currentTimeMillis();
		long elapsed = now - paymentStartTime;
		long remaining = paymentTimeoutMillis - elapsed;

		if (remaining <= 0) {
			return "00:00";
		}
		long minutes = (remaining / 1000) / 60;
		long seconds = (remaining / 1000) % 60;

		return String.format("%02d:%02d", minutes, seconds);
	}
	
	protected void addMessage(String msg, FacesMessage.Severity severity) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    if (context != null) {
	        context.addMessage(null, new FacesMessage(severity, msg, null));
	    }
	}
	
	public void setPaymentStartTime(long time) {
	    this.paymentStartTime = time;
	}
	
}
