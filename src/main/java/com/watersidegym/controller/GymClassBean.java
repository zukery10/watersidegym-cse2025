package com.watersidegym.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.watersidegym.model.GymClass;
import com.watersidegym.model.User;
import com.watersidegym.service.GymClassService;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

@ManagedBean
@SessionScoped
public class GymClassBean {

	private GymClassService gymClassService;
	private List<GymClass> gymClassList;
	private Date selectedDate;
	private String errorMessage;
	private List<GymClass> filteredClasses = new ArrayList<>();
	private List<GymClass> cart = new ArrayList<>();
	private int totalPrice = 0;

	private List<GymClass> disabledButtons = new ArrayList<>();
	private User loggedInUser;
	private boolean classPayment = false;
	private static final long CLASS_PAYMENT_TIMEOUT_MILLIS = 5* 60 * 1000;
	private long classPaymentDeadline;

	// we use true to indicate a class payment
	//we use false to indicate a membership payment
	
	public GymClassBean() {
	}

	// Catch any unexpected errors during initialization to prevent app crashes
	// and show a user-friendly error message if loading classes fails.
	@PostConstruct
	public void init() {

		gymClassService = new GymClassService(); // Initialize service here
		try {
			gymClassList = gymClassService.getAllGymClasses();
			filteredClasses = new ArrayList<>(gymClassList); // Start with all classes shown
			cart = new ArrayList<>(); // start with empty cart
		} catch (Exception e) {
			gymClassList = new ArrayList<>();
			filteredClasses = new ArrayList<>();
			errorMessage = "Currently unable to load classes. Please try again later.";
		}
		isMember();
		showPrice();
		// checkClassPaymentSession();
	}

	public GymClassService getGymClassService() {
		return gymClassService;
	}

	public void setGymClassService(GymClassService gymClassService) {
		this.gymClassService = gymClassService;
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public void setGymClassList(List<GymClass> gymClassList) {
		this.gymClassList = gymClassList;
	}

	public List<GymClass> getGymClassList() {
		return gymClassService.getAllGymClasses();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setFilteredClasses(List<GymClass> filteredClasses) {
		this.filteredClasses = filteredClasses;
	}

	public List<GymClass> getFilteredClasses() {
		if (selectedDate == null) {
			return gymClassList; // show all classes by default
		}
		return filteredClasses; // show only filtered results after selection
	}

	// filter for matching dates -> selectedDate

	public String getFilteredClassesAction() {
		filteredClasses = new ArrayList<>();

		if (selectedDate == null) {
			return null; // nothing to filter
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedDate);
		// Reset time part to midnight - mismatches due to time zones
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		for (GymClass gymClass : gymClassList) {

			Calendar classDate = Calendar.getInstance();
			classDate.setTime(gymClass.getClassDate());

			// Reset time part of class date to midnight too
			classDate.set(Calendar.HOUR_OF_DAY, 0);
			classDate.set(Calendar.MINUTE, 0);
			classDate.set(Calendar.SECOND, 0);
			classDate.set(Calendar.MILLISECOND, 0);

			if (classDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
					&& classDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
					&& classDate.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {

				// Match found - add to filtered list
				filteredClasses.add(gymClass);

				// System.out.println("Selected Date: " + selectedDate);
				// System.out.println("Class Date: " + cls.getClassDate());
			}
		}
		return null; // stay on same page
	}

	public List<GymClass> getCart() {
		return cart;
	}

	// booking logic - member booking a class
	public String bookClass(GymClass cls) {
		FacesContext context = FacesContext.getCurrentInstance();
		// get LoginBean and logged-in user
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		User loggedInUser = loginBean.getLoggedInUser();
		// send to service
		boolean booked = gymClassService.bookClass(loggedInUser, cls);
		if (loggedInUser.getRole().equals("Member")) {
			if (!booked) {
				if (loggedInUser.isEnrolledInClass(cls)) {
					addMessage("You have already booked this class.", FacesMessage.SEVERITY_WARN);
				} else if (cls.getNoOfSlots() <= 0) {
					addMessage("Class Full, No slots available.", FacesMessage.SEVERITY_WARN);
				} else {
					addMessage("Booking Failed", FacesMessage.SEVERITY_ERROR);
				}
				return null;
			}
			// booking successful
			addMessage("Class Booked", FacesMessage.SEVERITY_INFO);
		}
		return null; // stay on page
	}

	public boolean isMember() {
		FacesContext context = FacesContext.getCurrentInstance();
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		loggedInUser = loginBean.getLoggedInUser();

		return loggedInUser != null && !"Non-Member".equals(loggedInUser.getRole());
	}

	public boolean showPrice() {
		if (isMember()) {
			return false;
		}
		return true;
	}

	public boolean buttonDisabled(GymClass cls) {
		if (disabledButtons.contains(cls)) {
			return true; // already in cart
		}
		if (loggedInUser != null && loggedInUser.isEnrolledInClass(cls)) {
			return true; // already booked
		}
		return false;
	}

	public boolean isCartEmpty() {
		return cart.isEmpty();
	}

	public String addToCart(GymClass cls) {
		if (cart.isEmpty()) {
			startClassPaymentTimer(); // set deadline
		}
		if (!disabledButtons.contains(cls)) {
			cart.add(cls);
			totalPrice += cls.getPrice();
			disabledButtons.add(cls);
		}
		return null;
	}

	public String toPayment() { //checkout button
		classPayment = true;
		return "payment.xhtml?faces-redirect=true";
	}

	public String removeItemFromCart(GymClass cls) {
		cart.remove(cls); // removes from cart
		disabledButtons.remove(cls); // removes from disabled buttons
		totalPrice -= cls.getPrice(); // minus's price from total

		if (cart.isEmpty()) {
			classPaymentDeadline = 0; // clear the timer
		}
		return null;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	// ===============================================================================
	// the actual purchasing of the class and enrolling the non member \/\/\/\/
	// ===============================================================================
	public String purchaseClass() {
		FacesContext context = FacesContext.getCurrentInstance();
		MemberShipBean membershipBean = context.getApplication().evaluateExpressionGet(context, "#{membershipBean}",
				MemberShipBean.class);

		if (isClassPaymentTimedOut()) {
			addMessage("Your cart session has expired. Please add classes again.", FacesMessage.SEVERITY_ERROR);
			cart.clear();

			return "browse-classes.xhtml?faces-redirect=true";
		}

		// Check PayPal credentials from membershipBean
		if ("user@example.com".equalsIgnoreCase(membershipBean.getPaypalEmail())
				&& "password".equals(membershipBean.getPaypalPassword())) {

			for (GymClass gymClass : cart) {
				loggedInUser.enrollInClass(gymClass);
				int slots = gymClass.getNoOfSlots();
				gymClass.setNoOfSlots(slots - 1);
			}

			cart.clear();
			return "payment-success.xhtml";

		} else {
			addMessage("Payment Failed", FacesMessage.SEVERITY_ERROR);
		}
		return null;
	}

	public boolean isClassPayment() {
		return classPayment;
	}

	public void setClassPayment(boolean classPayment) {
		this.classPayment = classPayment;
	}

	public boolean isClassPaymentTimedOut() {
		if (classPaymentDeadline <= 0) {
			return false; // timer not started
		}
		long now = System.currentTimeMillis();
		return now > classPaymentDeadline;
	}

	public void checkClassPaymentSession() {
		if (!isMember() && isClassPaymentTimedOut() && !cart.isEmpty()) {
			try {
				// Clear cart and reset class payment state
				cart.clear();
				disabledButtons.clear();
				totalPrice = 0;
				classPaymentDeadline = 0;

				// **Set classPayment = true** to indicate this is a class cart timeout
				classPayment = true;

				FacesContext.getCurrentInstance().getExternalContext().redirect("sessionExpired.xhtml");
			} catch (Exception e) {
				System.out.println("Redirect failed: " + e.getMessage());
			}
		}
	}

	public String getClassPaymentRemainingTime() {
		if (classPaymentDeadline == 0) {
			return ""; // nothing shown if no active timer
		}
		long remaining = classPaymentDeadline - System.currentTimeMillis();
		if (remaining <= 0) {
			return "00:00";
		}
		long minutes = (remaining / 1000) / 60;
		long seconds = (remaining / 1000) % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}

	public void startClassPaymentTimer() {
		this.classPaymentDeadline = System.currentTimeMillis() + CLASS_PAYMENT_TIMEOUT_MILLIS;
	}

	public String getRedirectPage() {
		return classPayment ? "nonmember-dashboard.xhtml" : "member-dashboard.xhtml";
	}

	public void resetNonMemberCartAndTimer() {
		if (!isMember()) {
			cart.clear();
			disabledButtons.clear();
			totalPrice = 0;
			classPaymentDeadline = 0;
			classPayment = false;
		} else {
			
		}
	}

	public String resetNonMemberCartAndTimerAndRedirect() {
		resetNonMemberCartAndTimer();
		System.out.println("Button Pressed");
		return "nonmember-dashboard.xhtml?faces-redirect=true";
	}
	public String returnToCart() {
		if (classPayment) {	
		return "browseClasses.xhtml";
		} else 
		return null;
		
	}

	public void setClassPaymentDeadline(long l) {
		this.classPaymentDeadline = l;

	}

	protected void addMessage(String msg, FacesMessage.Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			context.addMessage(null, new FacesMessage(severity, msg, null));
		}
	}
}