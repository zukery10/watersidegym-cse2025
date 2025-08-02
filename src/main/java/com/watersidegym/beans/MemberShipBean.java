package com.watersidegym.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

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

    // === GETTERS & SETTERS ===

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

    // === ACTION METHODS ===

    /**
     * Called from purchase-membership.xhtml. Stores selected plan and redirects to payment page.
     */
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

    /**
     * Called from payment.xhtml after submitting payment info.
     */
    public String processPayment() {
        // Optionally simulate payment logic...

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


}