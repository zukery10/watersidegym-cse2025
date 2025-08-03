package com.watersidegym.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.watersidegym.model.MembershipPlan;

@ManagedBean(name = "membershipBean")
@SessionScoped
public class MemberShipBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MembershipPlan membershipPlan;
    private String confirmationMessage;

    private String cardholderName;
    private String cardNumber;
    private String expiry;
    private String cvv;
    private String paypalEmail;
    private String paypalPassword;

    public MembershipPlan getMembershipPlan() {
        return membershipPlan;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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

    public String choosePlan(String planCode) {
        try {
            this.membershipPlan = MembershipPlan.valueOf(planCode);
            this.confirmationMessage = "You have selected the " + membershipPlan.getLabel() + " membership plan.";

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Membership Selected", confirmationMessage));

            return "payment.xhtml?faces-redirect=true";

        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Plan", "The selected plan is not valid."));
            return null;
        }
    }

    public String processPayment() {
        if (isCardExpired()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment Failed", "Card has expired."));
            return null;
        }

        if (!isValidCardDetails()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment Failed", "Invalid card details."));
            return null;
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Payment Successful",
                        "Your " + membershipPlan.getLabel() + " membership has been activated."));

        return "payment-success.xhtml?faces-redirect=true";
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
                        "Your " + membershipPlan.getLabel() + " membership has been activated."));

        return "payment-success.xhtml?faces-redirect=true";
    }

    public double getPriceFor(String planCode) {
        try {
            return MembershipPlan.valueOf(planCode).getPrice();
        } catch (IllegalArgumentException e) {
            return 0.0;
        }
    }


    public boolean isValidCardDetails() {
    	if (cardholderName != null && !cardholderName.trim().isEmpty()
                && cardNumber != null && cardNumber.trim().length() >= 16
                && cvv != null && cvv.length() >= 3) 
    	{
    		return true;
    	} 
    	else {
            return false;
    		
    	}
    }

    public boolean isCardExpired() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryDate = YearMonth.parse(expiry, formatter);
            YearMonth now = YearMonth.now();
            return expiryDate.isBefore(now);
        } catch (DateTimeParseException e) {
            return true;
        }
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
