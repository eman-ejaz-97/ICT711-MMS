package models;

import java.io.Serializable;
import java.time.LocalDate;
import constants.Constants;

/**
 * Abstract base class representing a gym member
 * Demonstrates abstraction and encapsulation
 */
public abstract class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Encapsulated attributes
    private String memberId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate joinDate;
    private double baseFee;
    private int performanceRating; // 0-10 scale
    private boolean goalAchieved;
    
    // default constructor
    public Member(String memberId, String firstName, String lastName, 
                  String email, String phone, double baseFee) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.joinDate = LocalDate.now();
        this.baseFee = baseFee;
        this.performanceRating = Constants.DEFAULT_PERFORMANCE_RATING;
        this.goalAchieved = Constants.DEFAULT_GOAL_ACHIEVED;
    }
    
    // abstract method - must be implemented by subclasses (Polymorphism)
    public abstract double calculateMonthlyFee();
    
    // abstract method for member type
    public abstract String getMemberType();
    
    // common method for all members
    public String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("Performance Report for ").append(getFullName()).append("\n");
        report.append("Member Type: ").append(getMemberType()).append("\n");
        report.append("Performance Rating: ").append(performanceRating).append("/10\n");
        report.append("Goal Achievement: ").append(goalAchieved ? "Yes" : "No").append("\n");
        report.append("Monthly Fee: $").append(String.format("%.2f", calculateMonthlyFee())).append("\n");
        return report.toString();
    }
    
    // getters and setters (encapsulation)
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { return firstName + " " + lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    
    public double getBaseFee() { return baseFee; }
    public void setBaseFee(double baseFee) { this.baseFee = baseFee; }
    
    public int getPerformanceRating() { return performanceRating; }
    public void setPerformanceRating(int performanceRating) {
        if (performanceRating >= Constants.MIN_PERFORMANCE_RATING && 
            performanceRating <= Constants.MAX_PERFORMANCE_RATING) {
            this.performanceRating = performanceRating;
        }
    }
    
    public boolean isGoalAchieved() { return goalAchieved; }
    public void setGoalAchieved(boolean goalAchieved) { this.goalAchieved = goalAchieved; }
    
    @Override
    public String toString() {
        return String.format("Member ID: %s | Name: %s | Type: %s | Rating: %d/10 | Fee: $%.2f",
                memberId, getFullName(), getMemberType(), performanceRating, calculateMonthlyFee());
    }
}
