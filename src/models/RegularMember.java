package models;

import constants.Constants;

/**
 * Regular member class - extends Member
 * Demonstrates inheritance (Base class: Member)
 */
public class RegularMember extends Member {
    
    public RegularMember(String memberId, String firstName, String lastName,
                        String email, String phone) {
        // calling base class constructor
        super(memberId, firstName, lastName, email, phone, Constants.REGULAR_BASE_FEE);
    }
    
    @Override
    public double calculateMonthlyFee() {
        double fee = getBaseFee();
        
        // apply discount if goal achieved
        if (isGoalAchieved()) {
            fee = fee * (1 - Constants.REGULAR_GOAL_ACHIEVEMENT_DISCOUNT);
        }
        
        // apply penalty for low performance
        if (getPerformanceRating() < Constants.PENALTY_PERFORMANCE_THRESHOLD) {
            fee += Constants.LOW_PERFORMANCE_PENALTY;
        }
        
        return fee;
    }
    
    @Override
    public String getMemberType() {
        return Constants.DISPLAY_REGULAR_MEMBERSHIP;
    }
}