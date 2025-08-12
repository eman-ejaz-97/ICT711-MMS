package constants;

/**
 * Constants class containing all hard coded values used throughout the Member Management System
 * This improves maintainability and reduces the risk of errors from scattered literal values
 */
public final class Constants {
    
    // Prevent instantiation of utility class
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
    
    // ========== FILE AND DATA CONSTANTS ==========
    public static final String DEFAULT_FILE_NAME = "member_data.csv";
    public static final String CSV_HEADER = "Type,ID,FirstName,LastName,Email,Phone,PerformanceRating,GoalAchieved,Extra1,Extra2";
    public static final int MIN_CSV_FIELDS_REQUIRED = 7;
    public static final String CSV_DELIMITER = ",";
    
    // ========== MEMBER TYPE CONSTANTS ==========
    public static final String MEMBER_TYPE_REGULAR = "Regular";
    public static final String MEMBER_TYPE_PREMIUM = "Premium";
    public static final String MEMBER_TYPE_STUDENT = "Student";
    
    // ========== DEFAULT VALUES ==========
    public static final String DEFAULT_TRAINER_NAME = "Default Trainer";
    public static final String DEFAULT_UNIVERSITY = "Default University";
    public static final String DEFAULT_STUDENT_ID = "STU001";
    public static final int DEFAULT_SESSIONS_PER_MONTH = 4;
    public static final int DEFAULT_PERFORMANCE_RATING = 5;
    public static final boolean DEFAULT_GOAL_ACHIEVED = false;
    
    // ========== PERFORMANCE THRESHOLDS ==========
    public static final int HIGH_PERFORMANCE_THRESHOLD = 8;
    public static final int LOW_PERFORMANCE_THRESHOLD = 5;
    public static final int PENALTY_PERFORMANCE_THRESHOLD = 3;
    public static final int MIN_PERFORMANCE_RATING = 0;
    public static final int MAX_PERFORMANCE_RATING = 10;
    
    // ========== FEES AND FINANCIAL CONSTANTS ==========
    // Base fees for different member types
    public static final int REGULAR_BASE_FEE = 50;
    public static final int PREMIUM_BASE_FEE = 100;
    public static final int STUDENT_BASE_FEE = 40;
    
    // Session and penalty costs
    public static final int SESSION_COST = 25;
    public static final int LOW_PERFORMANCE_PENALTY = 10;
    public static final int HIGH_PERFORMANCE_BONUS = 20;
    public static final int STUDENT_GOAL_ACHIEVEMENT_BONUS = 5;
    public static final int MINIMUM_STUDENT_FEE = 20;
    
    // Discount rates
    public static final double REGULAR_GOAL_ACHIEVEMENT_DISCOUNT = 0.1; // 10%
    public static final double PREMIUM_GOAL_ACHIEVEMENT_DISCOUNT = 0.15; // 15%
    public static final double STUDENT_BASE_DISCOUNT = 0.3; // 30%
    
    // ========== UI MENU CONSTANTS ==========
    public static final String MENU_TITLE = "===== Member Management System - ICT711 =====";
    public static final String MENU_LOAD_RECORDS = "1. Load records from file";
    public static final String MENU_ADD_MEMBER = "2. Add new member";
    public static final String MENU_UPDATE_MEMBER = "3. Update member information";
    public static final String MENU_DELETE_MEMBER = "4. Delete member";
    public static final String MENU_QUERY_MEMBERS = "5. View/Query member details";
    public static final String MENU_MANAGE_PERFORMANCE = "6. Manage performance and fees";
    public static final String MENU_DISPLAY_STATISTICS = "7. Display statistics";
    public static final String MENU_EXIT = "8. Exit";
    public static final String MENU_PROMPT = "Please choose an option: ";
    
    // ========== CONFIRMATION PROMPTS ==========
    public static final String CONFIRMATION_YES = "y";
    public static final String SAVE_TO_FILE_PROMPT = "Save to file? (y/n): ";
    public static final String UPDATE_EMAIL_PROMPT = "Update email? (y/n): ";
    public static final String UPDATE_PHONE_PROMPT = "Update phone? (y/n): ";
    public static final String UPDATE_RATING_PROMPT = "Update performance rating? (y/n): ";
    public static final String UPDATE_GOAL_PROMPT = "Update goal achievement? (y/n): ";
    public static final String CONFIRM_DELETION_PROMPT = "Confirm deletion? (y/n): ";
    
    // ========== UPDATE FIELD KEYS ==========
    public static final String UPDATE_KEY_EMAIL = "email";
    public static final String UPDATE_KEY_PHONE = "phone";
    public static final String UPDATE_KEY_PERFORMANCE_RATING = "performanceRating";
    public static final String UPDATE_KEY_GOAL_ACHIEVED = "goalAchieved";
    
    // ========== MESSAGES ==========
    public static final String MSG_MEMBER_ADDED = "Member added successfully: %s with ID: %s";
    public static final String MSG_MEMBER_REMOVED = "Member removed successfully: %s";
    public static final String MSG_MEMBER_NOT_FOUND = "Member not found: %s";
    public static final String MSG_MEMBER_UPDATED = "Member updated successfully: %s with ID: %s";
    public static final String MSG_LOADING_MEMBERS = "Finding member by ID: %s";
    public static final String MSG_FINDING_BY_NAME = "Finding members by name: %s";
    public static final String MSG_UPDATING_MEMBER = "Updating member with ID: %s";
    public static final String MSG_MEMBER_NOT_FOUND_UPDATE = "Member not found: %s for update, skipping...";
    public static final String MSG_MEMBERS_LOADED = "Loaded %d members from %s";
    public static final String MSG_MEMBERS_SAVED = "Saved %d members to %s";
    public static final String MSG_EXIT_MESSAGE = "Thank you for using Member Management System!";
    public static final String MSG_INVALID_OPTION = "Invalid option. Please try again.";
    public static final String MSG_INVALID_INPUT = "Invalid input. Please enter a number.";
    public static final String MSG_ERROR_OCCURRED = "An error occurred: %s";
    
    // ========== STATISTICS LABELS ==========
    public static final String STATS_TITLE = "\n===== Gym Statistics =====";
    public static final String STATS_TOTAL_MEMBERS = "Total Members: %d";
    public static final String STATS_REGULAR_MEMBERS = "Regular Members: %d";
    public static final String STATS_PREMIUM_MEMBERS = "Premium Members: %d";
    public static final String STATS_STUDENT_MEMBERS = "Student Members: %d";
    public static final String STATS_AVG_PERFORMANCE = "Average Performance Rating: %.2f";
    public static final String STATS_GOAL_ACHIEVERS = "Members Who Achieved Goals: %d";
    public static final String STATS_FOOTER = "=========================\n";
    public static final String STATS_NO_MEMBERS = "No members in the system.";
    
    // ========== LETTER TEMPLATES ==========
    public static final String APPRECIATION_LETTER_TEMPLATE = 
        "Dear %s,\n\n" +
        "Congratulations on your outstanding performance this month!\n" +
        "Your dedication with a rating of %d/10 is truly commendable.\n" +
        "Keep up the excellent work!\n\n" +
        "Best regards,\nGym Management";
    
    public static final String REMINDER_LETTER_TEMPLATE = 
        "Dear %s,\n\n" +
        "We noticed your performance rating is %d/10 this month.\n" +
        "We encourage you to participate more actively in gym activities.\n" +
        "Our trainers are here to help you achieve your fitness goals!\n\n" +
        "Best regards,\nGym Management";
        
    // ========== MEMBER TYPE DISPLAY NAMES ==========
    public static final String DISPLAY_REGULAR_MEMBERSHIP = "Regular Membership";
    public static final String DISPLAY_PREMIUM_MEMBERSHIP = "Premium Membership (Personal Trainer)";
    public static final String DISPLAY_STUDENT_MEMBERSHIP = "Student Membership";
} 