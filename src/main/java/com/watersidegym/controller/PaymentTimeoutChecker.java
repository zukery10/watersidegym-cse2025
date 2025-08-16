package com.watersidegym.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

//Utility bean that checks for payment session timeouts across the application.
@ManagedBean(name = "paymentTimeoutChecker")
@RequestScoped
public class PaymentTimeoutChecker {
    public void checkTimeout() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        // get the main beans that handle payment state
        GymClassBean gymClassBean = getGymClassBean(context);
        MemberShipBean membershipBean = getMembershipBean(context);

        try {
            // check which payment flow we're currently in
            if (gymClassBean.isClassPayment()) {
                // CLASS PAYMENT FLOW - non-members buying classes
                if (gymClassBean.isClassPaymentTimedOut()) {
                    redirectToSessionExpired(context);
                }
            } else {
                // MEMBERSHIP PAYMENT FLOW - users buying memberships
                if (membershipBean.isPaymentTimedOut()) {
                    // Mark as membership timeout (not class payment)
                    gymClassBean.setClassPayment(false);
                    redirectToSessionExpired(context);
                }
            }
        } catch (Exception e) {
            // log error but don't crash the application
            System.out.println("Payment timeout check failed: " + e.getMessage());
        }
    }
    private GymClassBean getGymClassBean(FacesContext context) {
        return context.getApplication().evaluateExpressionGet(
            context, "#{gymClassBean}", GymClassBean.class);
    }
    private MemberShipBean getMembershipBean(FacesContext context) {
        return context.getApplication().evaluateExpressionGet(
            context, "#{membershipBean}", MemberShipBean.class);
    }
    private void redirectToSessionExpired(FacesContext context) throws Exception {
        context.getExternalContext().redirect("sessionExpired.xhtml");
    }
}