package models;

import constants.Constants;

/**
 * Premium member with personal trainer
 * Demonstrates inheritance and polymorphism (Base class: Member)
 */
public class PremiumMember extends Member {
    // additional fields for premium members
    private String trainerName;
    private int sessionsPerMonth;
    
    public PremiumMember(String memberId, String firstName, String lastName,
                        String email, String phone, String trainerName, int sessionsPerMonth) {
        super(memberId, firstName, lastName, email, phone, Constants.PREMIUM_BASE_FEE);
        this.trainerName = trainerName;
        this.sessionsPerMonth = sessionsPerMonth;
    }
    
    @Override
    public double calculateMonthlyFee() {
        double fee = getBaseFee();
        
        // add cost for personal training sessions
        fee += (sessionsPerMonth * Constants.SESSION_COST);
        
        // apply discount if goal achieved
        if (isGoalAchieved()) {
            fee = fee * (1 - Constants.PREMIUM_GOAL_ACHIEVEMENT_DISCOUNT);
        }
        
        // premium members get bonus for high performance
        if (getPerformanceRating() >= Constants.HIGH_PERFORMANCE_THRESHOLD) {
            fee -= Constants.HIGH_PERFORMANCE_BONUS;
        }
        
        return fee;
    }
    
    @Override
    public String getMemberType() {
        return Constants.DISPLAY_PREMIUM_MEMBERSHIP;
    }
    
    // getters and setters for additional fields for premium members (encapsulation)
    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
    
    public int getSessionsPerMonth() { return sessionsPerMonth; }
    public void setSessionsPerMonth(int sessionsPerMonth) { this.sessionsPerMonth = sessionsPerMonth; }
}
