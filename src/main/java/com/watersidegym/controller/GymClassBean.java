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
	private static boolean classPayment = false;
	private String paypalEmail = "user@example.com";
	private String paypalPassword = "password";
	private static final long CLASS_PAYMENT_TIMEOUT_MILLIS = 5 * 60 * 1000;
	private long classPaymentDeadline;

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
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Already Booked",
							"You have already booked this class."));
				} else if (cls.getNoOfSlots() <= 0) {
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "Class Full", "No slots available."));
				} else {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Booking Failed",
							"An unexpected error occurred."));
				}
				return null;
			}
			// booking successful
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Class Booked",
					"You have successfully booked the class."));
		}
		return null; // stay on page
	}

	public boolean isMember() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    LoginBean loginBean = context.getApplication()
	            .evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
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

	public String toPayment() {
		classPayment = true;
		// startClassPaymentTimer(); // set deadline again when payment page starts
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

	public boolean isClassPayment() {
		return classPayment;
	}

	public static void setClassPayment(boolean classPayment1) {
		classPayment = classPayment1;
	}
	// the actual purchasing of the class and enrolling the non member \/\/\/\/

	public boolean isValidPayPal() {
		if ("user@example.com".equalsIgnoreCase(paypalEmail) && "password".equals(paypalPassword)) {
			return true;
		} else {
			return false;
		}
	}

	public String purchaseClass() {
		if (isClassPaymentTimedOut()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Payment Timeout", "Your cart session has expired. Please add classes again."));
			cart.clear();
			return "browse-classes.xhtml?faces-redirect=true";
		}

		if (isValidPayPal()) {
			for (GymClass gymClass : cart) {
				loggedInUser.enrollInClass(gymClass);
				int slots = gymClass.getNoOfSlots();
				gymClass.setNoOfSlots(slots - 1);
			}
			cart.clear();
			return "payment-success.xhtml";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment Failed", "Invalid PayPal credentials."));
		}
		return null;
	}

	public boolean isClassPaymentTimedOut() {
		return System.currentTimeMillis() > classPaymentDeadline;
	}

	public void checkClassPaymentSession() {
		if (isClassPaymentTimedOut()) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("sessionExpired.xhtml");
			} catch (Exception e) {
				System.out.println("Redirect failed");
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
	    if (!isMember() && !cart.isEmpty()) {
	        cart.clear();
	        disabledButtons.clear();
	        totalPrice = 0;
	        classPaymentDeadline = 0;
	        classPayment = false;
	    }
	}
	public String resetNonMemberCartAndTimerAndRedirect() {
	    resetNonMemberCartAndTimer();
	    return "nonmember-dashboard?faces-redirect=true";
	}
}
