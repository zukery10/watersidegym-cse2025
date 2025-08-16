package com.watersidegym.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "paymentTimeoutChecker")
@RequestScoped
public class PaymentTimeoutChecker {

    public void checkTimeout() {
        FacesContext context = FacesContext.getCurrentInstance();
        GymClassBean gymClassBean = context.getApplication()
            .evaluateExpressionGet(context, "#{gymClassBean}", GymClassBean.class);
        MemberShipBean membershipBean = context.getApplication()
            .evaluateExpressionGet(context, "#{membershipBean}", MemberShipBean.class);

        try {
            // Membership payment timeout
            if (membershipBean.isPaymentTimedOut()) {
                // **Set classPayment = false** so sessionExpired.xhtml knows it's a membership timeout
                gymClassBean.setClassPayment(false);

                context.getExternalContext().redirect("sessionExpired.xhtml");
            }

            // You can still check class cart timeout here too if you want a single centralized check
            if (gymClassBean.isClassPayment() && gymClassBean.isClassPaymentTimedOut()) {
                context.getExternalContext().redirect("sessionExpired.xhtml");
            }

        } catch (Exception e) {
            System.out.println("Redirect failed: " + e.getMessage());
        }
    }

}


