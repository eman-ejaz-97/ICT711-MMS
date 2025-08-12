package models;

import constants.Constants;

/**
 * Student member with special rates
 * Demonstrates inheritance and polymorphism (Base class: Member)
 */
public class StudentMember extends Member {
    // additional fields for student members
    private String studentId;
    private String university;
    
    public StudentMember(String memberId, String firstName, String lastName,
                        String email, String phone, String studentId, String university) {
        super(memberId, firstName, lastName, email, phone, Constants.STUDENT_BASE_FEE);
        this.studentId = studentId;
        this.university = university;
    }
    
    @Override
    public double calculateMonthlyFee() {
        double fee = getBaseFee() * (1 - Constants.STUDENT_BASE_DISCOUNT);
        
        // additional discount for goal achievement
        if (isGoalAchieved()) {
            fee -= Constants.STUDENT_GOAL_ACHIEVEMENT_BONUS;
        }
        
        // no penalties for students
        return Math.max(fee, Constants.MINIMUM_STUDENT_FEE);
    }
    
    @Override
    public String getMemberType() {
        return Constants.DISPLAY_STUDENT_MEMBERSHIP;
    }
    
    // getters and setters for additional fields for student members (encapsulation)
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
}
