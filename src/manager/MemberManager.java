package manager;

// ========== MemberManager.java (Manager Class) ==========
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import models.*;
import constants.Constants;

/**
 * Manager class for handling member operations
 * Demonstrates use of Java Collections and File I/O
 */
public class MemberManager {
    private ArrayList<Member> members; // using ArrayList for dynamic storage for members
    private String currentFileName;
    
    public MemberManager() {
        this.members = new ArrayList<>();
        this.currentFileName = Constants.DEFAULT_FILE_NAME;
    }
    
    // add member to the system
    public void addMember(Member member) {
        members.add(member);
        System.out.printf(Constants.MSG_MEMBER_ADDED, member.getFullName(), member.getMemberId());
        System.out.println();
    }
    
    // remove member by ID
    public boolean removeMember(String memberId) {
        boolean removed = members.removeIf(m -> m.getMemberId().equals(memberId));
        if (removed) {
            System.out.printf(Constants.MSG_MEMBER_REMOVED, memberId);
            System.out.println();
        } else {
            System.out.printf(Constants.MSG_MEMBER_NOT_FOUND, memberId);
            System.out.println();
        }
        return removed;
    }
    
    // find member by ID
    public Member findMemberById(String memberId) {
        System.out.printf(Constants.MSG_LOADING_MEMBERS, memberId);
        System.out.println();
        return members.stream()
                .filter(m -> m.getMemberId().equals(memberId))
                .findFirst()
                .orElse(null);
    }
    
    // find members by name (partial match)
    public List<Member> findMembersByName(String name) {
        System.out.printf(Constants.MSG_FINDING_BY_NAME, name);
        System.out.println();
        String searchName = name.toLowerCase();
        return members.stream()
                .filter(m -> m.getFullName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }
    
    // find members by performance rating
    public List<Member> findMembersByPerformance(int minRating) {
        return members.stream()
                .filter(m -> m.getPerformanceRating() >= minRating)
                .sorted((m1, m2) -> Integer.compare(m2.getPerformanceRating(), m1.getPerformanceRating()))
                .collect(Collectors.toList());
    }
    
    // update member information
    public boolean updateMember(String memberId, Map<String, Object> updates) {
        System.out.printf(Constants.MSG_UPDATING_MEMBER, memberId);
        System.out.println();
        Member member = findMemberById(memberId);
        if (member == null) {
            System.out.printf(Constants.MSG_MEMBER_NOT_FOUND_UPDATE, memberId);
            System.out.println();
            return false;
        }
        
        // update fields based on the map
        if (updates.containsKey(Constants.UPDATE_KEY_EMAIL)) {
            member.setEmail((String) updates.get(Constants.UPDATE_KEY_EMAIL));
        }
        if (updates.containsKey(Constants.UPDATE_KEY_PHONE)) {
            member.setPhone((String) updates.get(Constants.UPDATE_KEY_PHONE));
        }
        if (updates.containsKey(Constants.UPDATE_KEY_PERFORMANCE_RATING)) {
            member.setPerformanceRating((Integer) updates.get(Constants.UPDATE_KEY_PERFORMANCE_RATING));
        }
        if (updates.containsKey(Constants.UPDATE_KEY_GOAL_ACHIEVED)) {
            member.setGoalAchieved((Boolean) updates.get(Constants.UPDATE_KEY_GOAL_ACHIEVED));
        }

        System.out.printf(Constants.MSG_MEMBER_UPDATED, member.getFullName(), member.getMemberId());
        System.out.println();
        
        return true;
    }
    
    // load members from CSV file
    public void loadFromFile(String fileName) throws IOException {
        // clear the members list
        members.clear();
        currentFileName = fileName;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            reader.readLine(); // skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Constants.CSV_DELIMITER);
                // each line has 7 required fields (type, id, first name, last name, email, phone, performance rating, goal achieved)
                // extra fields are for premium members (trainer name and sessions per month) and student members (student ID and university)
                if (parts.length >= Constants.MIN_CSV_FIELDS_REQUIRED) {
                    String type = parts[0];
                    String id = parts[1];
                    String firstName = parts[2];
                    String lastName = parts[3];
                    String email = parts[4];
                    String phone = parts[5];
                    int performanceRating = Integer.parseInt(parts[6]);
                    boolean goalAchieved = Boolean.parseBoolean(parts[7]);
                    
                    Member member = null;
                    
                    switch (type) {
                        case Constants.MEMBER_TYPE_REGULAR:
                            member = new RegularMember(id, firstName, lastName, email, phone);
                            break;
                        case Constants.MEMBER_TYPE_PREMIUM:
                            String trainer = parts.length > 8 ? parts[8] : Constants.DEFAULT_TRAINER_NAME;
                            int sessions = parts.length > 9 ? Integer.parseInt(parts[9]) : Constants.DEFAULT_SESSIONS_PER_MONTH;
                            member = new PremiumMember(id, firstName, lastName, email, phone, trainer, sessions);
                            break;
                        case Constants.MEMBER_TYPE_STUDENT:
                            String studentId = parts.length > 8 ? parts[8] : Constants.DEFAULT_STUDENT_ID;
                            String university = parts.length > 9 ? parts[9] : Constants.DEFAULT_UNIVERSITY;
                            member = new StudentMember(id, firstName, lastName, email, phone, studentId, university);
                            break;
                    }
                    
                    if (member != null) {
                        member.setPerformanceRating(performanceRating);
                        member.setGoalAchieved(goalAchieved);
                        members.add(member);
                    }
                }
            }
            System.out.printf(Constants.MSG_MEMBERS_LOADED, members.size(), fileName);
            System.out.println();
        }
    }
    
    // Save members to CSV file
    public void saveToFile(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(Constants.CSV_HEADER);
            
            for (Member member : members) {
                writer.print(member.getMemberType().split(" ")[0] + Constants.CSV_DELIMITER);
                writer.print(member.getMemberId() + Constants.CSV_DELIMITER);
                writer.print(member.getFirstName() + Constants.CSV_DELIMITER);
                writer.print(member.getLastName() + Constants.CSV_DELIMITER);
                writer.print(member.getEmail() + Constants.CSV_DELIMITER);
                writer.print(member.getPhone() + Constants.CSV_DELIMITER);
                writer.print(member.getPerformanceRating() + Constants.CSV_DELIMITER);
                writer.print(member.isGoalAchieved());
                
                if (member instanceof PremiumMember) {
                    PremiumMember pm = (PremiumMember) member;
                    writer.print(Constants.CSV_DELIMITER + pm.getTrainerName() + Constants.CSV_DELIMITER + pm.getSessionsPerMonth());
                } else if (member instanceof StudentMember) {
                    StudentMember sm = (StudentMember) member;
                    writer.print(Constants.CSV_DELIMITER + sm.getStudentId() + Constants.CSV_DELIMITER + sm.getUniversity());
                }
                writer.println();
            }
            System.out.printf(Constants.MSG_MEMBERS_SAVED, members.size(), fileName);
            System.out.println();
        }
    }
    
    // Generate appreciation letter for high performers
    public String generateAppreciationLetter(Member member) {
        if (member.getPerformanceRating() >= Constants.HIGH_PERFORMANCE_THRESHOLD) {
            return String.format(Constants.APPRECIATION_LETTER_TEMPLATE,
                member.getFullName(), member.getPerformanceRating());
        }
        return null;
    }
    
    // Generate reminder letter for low performers
    public String generateReminderLetter(Member member) {
        if (member.getPerformanceRating() < Constants.LOW_PERFORMANCE_THRESHOLD) {
            return String.format(Constants.REMINDER_LETTER_TEMPLATE,
                member.getFullName(), member.getPerformanceRating());
        }
        return null;
    }
    
    // Get all members
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }
    
    // Get statistics
    public void displayStatistics() {
        if (members.isEmpty()) {
            System.out.println(Constants.STATS_NO_MEMBERS);
            return;
        }
        
        long regularCount = members.stream().filter(m -> m instanceof RegularMember).count();
        long premiumCount = members.stream().filter(m -> m instanceof PremiumMember).count();
        long studentCount = members.stream().filter(m -> m instanceof StudentMember).count();
        
        double avgPerformance = members.stream()
                .mapToInt(Member::getPerformanceRating)
                .average()
                .orElse(0);
        
        long goalAchievers = members.stream().filter(Member::isGoalAchieved).count();
        
        System.out.println(Constants.STATS_TITLE);
        System.out.printf(Constants.STATS_TOTAL_MEMBERS, members.size());
        System.out.println();
        System.out.printf(Constants.STATS_REGULAR_MEMBERS, regularCount);
        System.out.println();
        System.out.printf(Constants.STATS_PREMIUM_MEMBERS, premiumCount);
        System.out.println();
        System.out.printf(Constants.STATS_STUDENT_MEMBERS, studentCount);
        System.out.println();
        System.out.printf(Constants.STATS_AVG_PERFORMANCE, avgPerformance);
        System.out.println();
        System.out.printf(Constants.STATS_GOAL_ACHIEVERS, goalAchievers);
        System.out.println();
        System.out.println(Constants.STATS_FOOTER);
    }
}
