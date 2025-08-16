package com.watersidegym.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "paymentTimeoutChecker")
@RequestScoped
public class PaymentTimeoutChecker {

	public void checkTimeout() {
		FacesContext context = FacesContext.getCurrentInstance();
		GymClassBean gymClassBean = context.getApplication().evaluateExpressionGet(context, "#{gymClassBean}",
				GymClassBean.class);
		MemberShipBean membershipBean = context.getApplication().evaluateExpressionGet(context, "#{membershipBean}",
				MemberShipBean.class);

		try {
			if (gymClassBean.isClassPayment()) {
				// CLASS PAYMENT FLOW ONLY
				if (gymClassBean.isClassPaymentTimedOut()) {
					context.getExternalContext().redirect("sessionExpired.xhtml");
				}
			} else {
				// MEMBERSHIP PAYMENT FLOW ONLY
				if (membershipBean.isPaymentTimedOut()) {
					gymClassBean.setClassPayment(false); // mark membership timeout
					context.getExternalContext().redirect("sessionExpired.xhtml");
				}
			}
		} catch (Exception e) {
			System.out.println("Redirect failed: " + e.getMessage());
		}
	}

}
