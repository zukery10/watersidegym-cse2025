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

    // ============================================================================
    // CORE PROPERTIES & INITIALISATION
    // ============================================================================
    
    private GymClassService gymClassService;
    private List<GymClass> gymClassList;
    private String errorMessage;
    private User loggedInUser;
    
    // class filtering properties
    private Date selectedDate;
    private List<GymClass> filteredClasses = new ArrayList<>();
    
    // non-member cart and payment properties
    private List<GymClass> cart = new ArrayList<>();
    private List<GymClass> disabledButtons = new ArrayList<>();
    private int totalPrice = 0;
    private boolean classPayment = false;
    
    // payment timeout settings (5 minutes)
    private static final long CLASS_PAYMENT_TIMEOUT_MILLIS = 5 * 60 * 1000;
    private long classPaymentDeadline;

    public GymClassBean() {
        // Empty constructor - real initialisation happens in init()
    }

    @PostConstruct
    public void init() {
        gymClassService = new GymClassService();
        
        try {
            // Load all classes from database
            gymClassList = gymClassService.getAllGymClasses();
            filteredClasses = new ArrayList<>(gymClassList); // Show all classes initially
            cart = new ArrayList<>(); // Start with empty cart
        } catch (Exception e) {
            // If loading fails, show empty lists and error message
            gymClassList = new ArrayList<>();
            filteredClasses = new ArrayList<>();
            errorMessage = "Currently unable to load classes. Please try again later.";
        }
        
        // Set up user context for display logic
        isMember();
    }

    // ============================================================================
    // CLASS BROWSING & FILTERING LOGIC
    // ============================================================================

    public String getFilteredClassesAction() {
        filteredClasses = new ArrayList<>();

        if (selectedDate == null) {
            return null; // Nothing to filter
        }
        
        // Set selected date to midnight to avoid time comparison issues
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTime(selectedDate);
        resetTimeToMidnight(selectedCal);

        // Check each class to see if it matches the selected date
        for (GymClass gymClass : gymClassList) {
            Calendar classCal = Calendar.getInstance();
            classCal.setTime(gymClass.getClassDate());
            resetTimeToMidnight(classCal);

            // Compare year, month, and day only
            if (isSameDate(selectedCal, classCal)) {
                filteredClasses.add(gymClass);
            }
        }
        return null; // Stay on same page
    }
    private void resetTimeToMidnight(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
    private boolean isSameDate(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
            && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
    // ============================================================================
    // MEMBER BOOKING LOGIC
    // ============================================================================

    public String bookClass(GymClass cls) {
        FacesContext context = FacesContext.getCurrentInstance();
        LoginBean loginBean = getLoginBean(context);
        User user = loginBean.getLoggedInUser();
        
        // Only process booking for members
        if (!"Member".equals(user.getRole())) {
            return null;
        }
        
        // Attempt to book the class via service
        boolean bookingSuccessful = gymClassService.bookClass(user, cls);
        
        if (!bookingSuccessful) {
            // Determine why booking failed and show appropriate message
            if (user.isEnrolledInClass(cls)) {
                showMessage(context, FacesMessage.SEVERITY_WARN, "Already Booked", 
                          "You have already booked this class.");
            } else if (cls.getNoOfSlots() <= 0) {
                showMessage(context, FacesMessage.SEVERITY_WARN, "Class Full", 
                          "No slots available.");
            } else {
                showMessage(context, FacesMessage.SEVERITY_ERROR, "Booking Failed", 
                          "An unexpected error occurred.");
            }
        } else {
            // Booking successful
            showMessage(context, FacesMessage.SEVERITY_INFO, "Class Booked", 
                      "You have successfully booked the class.");
        }
        
        return null;
    }

    // ============================================================================
    // NON-MEMBER CART & PAYMENT LOGIC
    // ============================================================================
    public String addToCart(GymClass cls) {
        // Start timer when cart goes from empty to having items
        if (cart.isEmpty()) {
            startClassPaymentTimer();
        }
        
        // Only add if not already in cart
        if (!disabledButtons.contains(cls)) {
            cart.add(cls);
            totalPrice += cls.getPrice();
            disabledButtons.add(cls); // Disable the button to prevent double-adding
        }
        return null;
    }

    public String removeItemFromCart(GymClass cls) {
        cart.remove(cls);
        disabledButtons.remove(cls); // Re-enable the add button
        totalPrice -= cls.getPrice();

        // Clear timer if cart is now empty
        if (cart.isEmpty()) {
            classPaymentDeadline = 0;
        }
        return null;
    }
    public String purchaseClass() {
        FacesContext context = FacesContext.getCurrentInstance();
        MemberShipBean membershipBean = getMembershipBean(context);

        // Check if payment session has timed out
        if (isClassPaymentTimedOut()) {
            showMessage(context, FacesMessage.SEVERITY_ERROR, "Payment Timeout", 
                      "Your cart session has expired. Please add classes again.");
            cart.clear();
            return "browse-classes.xhtml?faces-redirect=true";
        }

        // Validate PayPal credentials (hardcoded for demo purposes)
        if (isValidPayPalCredentials(membershipBean)) {
            // Process successful payment
            for (GymClass gymClass : cart) {
                loggedInUser.enrollInClass(gymClass);
                // Reduce available slots
                gymClass.setNoOfSlots(gymClass.getNoOfSlots() - 1);
            }
            cart.clear();
            return "payment-success.xhtml";
        } else {
            // Payment failed
            showMessage(context, FacesMessage.SEVERITY_ERROR, "Payment Failed", 
                      "Invalid PayPal credentials.");
        }
        return null;
    }
    public String toPayment() {
        classPayment = true;
        return "payment.xhtml?faces-redirect=true";
    }
    public void startClassPaymentTimer() {
        this.classPaymentDeadline = System.currentTimeMillis() + CLASS_PAYMENT_TIMEOUT_MILLIS;
    }
    public boolean isClassPaymentTimedOut() {
        if (classPaymentDeadline <= 0) {
            return false; // Timer not started
        }
        return System.currentTimeMillis() > classPaymentDeadline;
    }
    public String getClassPaymentRemainingTime() {
        if (classPaymentDeadline == 0) {
            return ""; // No active timer
        }
        
        long remaining = classPaymentDeadline - System.currentTimeMillis();
        if (remaining <= 0) {
            return "00:00";
        }
        
        long minutes = (remaining / 1000) / 60;
        long seconds = (remaining / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
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
    // ============================================================================
    // UTILITY METHODS & GETTERS/SETTERS
    // ============================================================================
    public boolean isMember() {
        FacesContext context = FacesContext.getCurrentInstance();
        LoginBean loginBean = getLoginBean(context);
        loggedInUser = loginBean.getLoggedInUser();
        
        return loggedInUser != null && !"Non-Member".equals(loggedInUser.getRole());
    }
    public boolean showPrice() {
        return !isMember();
    }
    public boolean buttonDisabled(GymClass cls) {
        if (disabledButtons.contains(cls)) {
            return true; // Already in cart
        }
        if (loggedInUser != null && loggedInUser.isEnrolledInClass(cls)) {
            return true; // Already booked
        }
        return false;
    }
    private LoginBean getLoginBean(FacesContext context) {
        return context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
    }

    private MemberShipBean getMembershipBean(FacesContext context) {
        return context.getApplication().evaluateExpressionGet(context, "#{membershipBean}", MemberShipBean.class);
    }

    private void showMessage(FacesContext context, FacesMessage.Severity severity, String summary, String detail) {
        context.addMessage(null, new FacesMessage(severity, summary, detail));
    }
   public boolean isValidPayPalCredentials(MemberShipBean membershipBean) {
        return "user@example.com".equalsIgnoreCase(membershipBean.getPaypalEmail()) 
            && "password".equals(membershipBean.getPaypalPassword());
    }
    // ============================================================================
    // STANDARD GETTERS & SETTERS
    // ============================================================================

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
        if (gymClassService != null) {
            return gymClassService.getAllGymClasses();
        }
        return new ArrayList<>();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setFilteredClasses(List<GymClass> filteredClasses) {
        this.filteredClasses = filteredClasses;
    }

    public List<GymClass> getFilteredClasses() {
    	
        if (selectedDate == null) {
            return gymClassList; // Show all classes by default
        }
        return filteredClasses; // Show filtered results
    }

    public List<GymClass> getCart() {
        return cart;
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public boolean isClassPayment() {
        return classPayment;
    }

    public void setClassPayment(boolean classPayment) {
        this.classPayment = classPayment;
    }

    public String getRedirectPage() {
        return classPayment ? "nonmember-dashboard.xhtml" : "member-dashboard.xhtml";
    }
    
    void setClassPaymentDeadline(long deadline) {
        this.classPaymentDeadline = deadline;
    }
    
    protected void addMessage(String msg, FacesMessage.Severity severity) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(null, new FacesMessage(severity, msg, null));
        }
    }
}