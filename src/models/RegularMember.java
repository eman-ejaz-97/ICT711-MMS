package models;

import constants.Constants;

/**
 * Regular gym member with standard pricing and benefits.
 * 
 * Extends Member base class to provide specific implementation for:
 * - Goal achievement discount (10%)
 * - Low performance penalty for ratings below 3
 * 
 * Demonstrates inheritance and polymorphism from Member base class.
 * 
 * @author ICT711 Group Project Team
 * @version 1.0
 */
public class RegularMember extends Member {
    
    /**
     * Constructs a new RegularMember with specified details.
     * Automatically sets the base fee to the regular member rate.
     * 
     * @param memberId unique identifier for the member
     * @param firstName member's first name
     * @param lastName member's last name
     * @param email member's email address
     * @param phone member's phone number
     */
    public RegularMember(String memberId, String firstName, String lastName,
                        String email, String phone) {
        super(memberId, firstName, lastName, email, phone, Constants.REGULAR_BASE_FEE);
    }
    
    /**
     * Calculates monthly fee for regular members.
     * 
     * Fee calculation includes:
     * - Base fee for regular membership
     * - 10% discount if goal achieved
     * - $10 penalty if performance rating below 3
     * 
     * @return calculated monthly fee
     */
    @Override
    public double calculateMonthlyFee() {
        double fee = getBaseFee();
        
        // Apply goal achievement discount
        if (isGoalAchieved()) {
            fee = fee * (1 - Constants.REGULAR_GOAL_ACHIEVEMENT_DISCOUNT);
        }
        
        // Apply penalty for low performance
        if (getPerformanceRating() < Constants.PENALTY_PERFORMANCE_THRESHOLD) {
            fee += Constants.LOW_PERFORMANCE_PENALTY;
        }
        
        return fee;
    }
    
    /**
     * Returns the display name for regular membership.
     * 
     * @return "Regular Membership"
     */
    @Override
    public String getMemberType() {
        return Constants.DISPLAY_REGULAR_MEMBERSHIP;
    }
}