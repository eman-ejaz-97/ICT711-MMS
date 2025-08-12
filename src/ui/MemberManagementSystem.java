package ui;

// ========== MemberManagementSystem.java (Main Class with UI) ==========
import java.io.*;
import java.util.*;
import manager.*;
import models.*;
import constants.Constants;

/**
 * Main class with text-based user interface
 * Demonstrates exception handling and user interaction
 */
public class MemberManagementSystem {
    private static MemberManager manager = new MemberManager();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Create initial data file if it doesn't exist
        createInitialDataFile();
        
        boolean running = true;
        
        while (running) {
            // display the text based ui menu
            displayMenu();
            
            try {
                // get the user's choice
                int choice = Integer.parseInt(scanner.nextLine());
                
                // switch case to handle the user's choice
                switch (choice) {
                    case 1:
                        loadRecords();
                        break;
                    case 2:
                        addNewMember();
                        break;
                    case 3:
                        updateMember();
                        break;
                    case 4:
                        deleteMember();
                        break;
                    case 5:
                        queryMembers();
                        break;
                    case 6:
                        managePerformance();
                        break;
                    case 7:
                        manager.displayStatistics();
                        break;
                    case 8:
                        running = false;
                        System.out.println(Constants.MSG_EXIT_MESSAGE);
                        break;
                    default:
                        System.out.println(Constants.MSG_INVALID_OPTION);
                }
            } catch (NumberFormatException e) {
                System.out.println(Constants.MSG_INVALID_INPUT);
            } catch (Exception e) {
                System.out.printf(Constants.MSG_ERROR_OCCURRED, e.getMessage());
                System.out.println();
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n" + Constants.MENU_TITLE);
        System.out.println(Constants.MENU_LOAD_RECORDS);
        System.out.println(Constants.MENU_ADD_MEMBER);
        System.out.println(Constants.MENU_UPDATE_MEMBER);
        System.out.println(Constants.MENU_DELETE_MEMBER);
        System.out.println(Constants.MENU_QUERY_MEMBERS);
        System.out.println(Constants.MENU_MANAGE_PERFORMANCE);
        System.out.println(Constants.MENU_DISPLAY_STATISTICS);
        System.out.println(Constants.MENU_EXIT);
        System.out.print(Constants.MENU_PROMPT);
    }
    
    private static void loadRecords() {
        System.out.print("Enter filename to load (or press Enter for default '" + Constants.DEFAULT_FILE_NAME + "'): ");
        String fileName = scanner.nextLine();
        if (fileName.isEmpty()) {
            fileName = Constants.DEFAULT_FILE_NAME;
        }
        
        try {
            manager.loadFromFile(fileName);
            System.out.println("Records loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    private static void addNewMember() {
        System.out.println("\n=== Add New Member ===");
        System.out.println("1. Regular Member");
        System.out.println("2. Premium Member (with trainer)");
        System.out.println("3. Student Member (with student ID and university)");
        System.out.print("Select member type: ");
        
        try {
            int type = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Member ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine();
            
            Member newMember = null;
            
            switch (type) {
                case 1:
                    System.out.println("- Regular Member will get 10% discount if goal achieved -");
                    System.out.println("- Regular Member will get penalty if performance rating is below " + Constants.PENALTY_PERFORMANCE_THRESHOLD + " -");
                    newMember = new RegularMember(id, firstName, lastName, email, phone);
                    break;
                case 2:
                    System.out.println("- Premium Member will get 15% discount if goal achieved -");
                    System.out.println("- Premium Member will get bonus if performance rating is above " + Constants.HIGH_PERFORMANCE_THRESHOLD + " -");
                    System.out.print("Enter Trainer Name: ");
                    String trainer = scanner.nextLine();
                    System.out.print("Enter Sessions per Month: ");
                    int sessions = Integer.parseInt(scanner.nextLine());
                    newMember = new PremiumMember(id, firstName, lastName, email, phone, trainer, sessions);
                    break;
                case 3:
                    System.out.println("- Student Member will get 30% discount -");
                    System.out.println("- Student Member will get bonus if goal achieved -");
                    System.out.println("- Student Member fee is minimum " + Constants.MINIMUM_STUDENT_FEE + " -");
                    System.out.print("Enter Student ID: ");
                    String studentId = scanner.nextLine();
                    System.out.print("Enter University: ");
                    String university = scanner.nextLine();
                    newMember = new StudentMember(id, firstName, lastName, email, phone, studentId, university);
                    break;
                default:
                    System.out.println("Invalid member type.");
                    return;
            }
            
            manager.addMember(newMember);
            
            System.out.print(Constants.SAVE_TO_FILE_PROMPT);
            if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
                saveToFile();
            }
            
        } catch (Exception e) {
            System.out.println("Error adding member: " + e.getMessage());
        }
    }
    
    private static void updateMember() {
        System.out.print("Enter Member ID to update: ");
        String id = scanner.nextLine();
        
        Member member = manager.findMemberById(id);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        
        System.out.println("Current Details: " + member);
        
        Map<String, Object> updates = new HashMap<>();
        
        System.out.print(Constants.UPDATE_EMAIL_PROMPT);
        if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
            System.out.print("New email: ");
            updates.put(Constants.UPDATE_KEY_EMAIL, scanner.nextLine());
        }
        
        System.out.print(Constants.UPDATE_PHONE_PROMPT);
        if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
            System.out.print("New phone: ");
            updates.put(Constants.UPDATE_KEY_PHONE, scanner.nextLine());
        }
        
        System.out.print(Constants.UPDATE_RATING_PROMPT);
        if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
            System.out.print("New rating (0-10): ");
            updates.put(Constants.UPDATE_KEY_PERFORMANCE_RATING, Integer.parseInt(scanner.nextLine()));
        }
        
        System.out.print(Constants.UPDATE_GOAL_PROMPT);
        if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
            System.out.print("Goal achieved? (true/false): ");
            updates.put(Constants.UPDATE_KEY_GOAL_ACHIEVED, Boolean.parseBoolean(scanner.nextLine()));
        }
        
        if (manager.updateMember(id, updates)) {
            System.out.println("Member updated successfully!");
            
            System.out.print(Constants.SAVE_TO_FILE_PROMPT);
            if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
                saveToFile();
            }
        } else {
            System.out.println("Failed to update member.");
        }
    }
    
    private static void deleteMember() {
        System.out.print("Enter Member ID to delete: ");
        String id = scanner.nextLine();
        
        Member member = manager.findMemberById(id);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        
        System.out.println("Member to delete: " + member);
        System.out.print(Constants.CONFIRM_DELETION_PROMPT);
        
        if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
            if (manager.removeMember(id)) {
                System.out.println("Member deleted successfully!");
                
                System.out.print(Constants.SAVE_TO_FILE_PROMPT);
                if (scanner.nextLine().toLowerCase().startsWith(Constants.CONFIRMATION_YES)) {
                    saveToFile();
                }
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private static void queryMembers() {
        System.out.println("\n=== Query Members ===");
        System.out.println("1. Query by ID");
        System.out.println("2. Query by Name");
        System.out.println("3. Query by Performance Rating");
        System.out.println("4. Show all members");
        System.out.print("Select query type: ");
        
        try {
            int queryType = Integer.parseInt(scanner.nextLine());
            
            switch (queryType) {
                case 1:
                    System.out.print("Enter Member ID: ");
                    String id = scanner.nextLine();
                    Member member = manager.findMemberById(id);
                    if (member != null) {
                        System.out.println("\n" + member);
                        System.out.println(member.generatePerformanceReport());
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;
                    
                case 2:
                    System.out.print("Enter name (partial match): ");
                    String name = scanner.nextLine();
                    List<Member> byName = manager.findMembersByName(name);
                    if (!byName.isEmpty()) {
                        System.out.println("\nFound " + byName.size() + " member(s):");
                        byName.forEach(System.out::println);
                    } else {
                        System.out.println("No members found with that name.");
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter minimum performance rating (0-10): ");
                    int minRating = Integer.parseInt(scanner.nextLine());
                    List<Member> byPerformance = manager.findMembersByPerformance(minRating);
                    if (!byPerformance.isEmpty()) {
                        System.out.println("\nFound " + byPerformance.size() + " member(s):");
                        byPerformance.forEach(System.out::println);
                    } else {
                        System.out.println("No members found with that performance rating.");
                    }
                    break;
                    
                case 4:
                    List<Member> allMembers = manager.getAllMembers();
                    if (!allMembers.isEmpty()) {
                        System.out.println("\nAll Members (" + allMembers.size() + " total):");
                        allMembers.forEach(System.out::println);
                    } else {
                        System.out.println("No members in the system.");
                    }
                    break;
                    
                default:
                    System.out.println("Invalid query type.");
            }
        } catch (Exception e) {
            System.out.println("Error during query: " + e.getMessage());
        }
    }
    
    private static void managePerformance() {
        System.out.println("\n=== Performance Management ===");
        System.out.println("1. Generate appreciation letters");
        System.out.println("2. Generate reminder letters");
        System.out.println("3. View fee details for all members");
        System.out.print("Select option: ");
        
        try {
            int option = Integer.parseInt(scanner.nextLine());
            
            switch (option) {
                case 1:
                    for (Member member : manager.getAllMembers()) {
                        String letter = manager.generateAppreciationLetter(member);
                        if (letter != null) {
                            System.out.println("\n" + letter);
                            System.out.println("------------------------");
                        }
                    }
                    break;
                    
                case 2:
                    for (Member member : manager.getAllMembers()) {
                        String letter = manager.generateReminderLetter(member);
                        if (letter != null) {
                            System.out.println("\n" + letter);
                            System.out.println("------------------------");
                        }
                    }
                    break;
                    
                case 3:
                    System.out.println("\n=== Monthly Fee Details ===");
                    double totalFees = 0;
                    for (Member member : manager.getAllMembers()) {
                        double fee = member.calculateMonthlyFee();
                        totalFees += fee;
                        System.out.printf("%s - %s: $%.2f%n", 
                            member.getMemberId(), member.getFullName(), fee);
                    }
                    System.out.printf("Total Monthly Revenue: $%.2f%n", totalFees);
                    break;
                    
                default:
                    System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("Error in performance management: " + e.getMessage());
        }
    }
    
    private static void saveToFile() {
        System.out.print("Enter filename to save (or press Enter for '" + Constants.DEFAULT_FILE_NAME + "'): ");
        String fileName = scanner.nextLine();
        if (fileName.isEmpty()) {
            fileName = Constants.DEFAULT_FILE_NAME;
        }
        
        try {
            manager.saveToFile(fileName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void createInitialDataFile() {
        File file = new File(Constants.DEFAULT_FILE_NAME);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(Constants.CSV_HEADER);
                
                // add 10 initial members (static data)
                writer.println("Regular,M001,Eman,Ejaz,eman.ejaz@email.com,466-0101,7,true,,");
                writer.println("Premium,M002,Sajina,Rana,sajina.rana@email.com,466-0102,9,true,Elina Trainer,8");
                writer.println("Student,M003,Waqas,Iqbal,waqas.iqbal@email.com,466-0103,6,false,STU2024001,State University");
                writer.println("Regular,M004,Sravanth,Rao,sravanth.ra@email.com,466-0104,5,false,,");
                writer.println("Premium,M005,Jessica,Wilson,jessica.w@email.com,466-0105,8,true,Lisa Coach,6");
                writer.println("Student,M006,David,Martinez,david.m@email.com,466-0106,7,true,STU2024002,Tech College");
                writer.println("Regular,M007,Maria,Garcia,maria.g@email.com,466-0107,4,false,,");
                writer.println("Premium,M008,James,Anderson,james.a@email.com,466-0108,10,true,Tom Trainer,10");
                writer.println("Student,M009,Sophie,Taylor,sophie.t@email.com,466-0109,8,true,STU2024003,City University");
                writer.println("Regular,M010,Robert,Thomas,robert.t@email.com,466-0110,6,false,,");
                
                System.out.println("Initial data file '" + Constants.DEFAULT_FILE_NAME + "' created with 10 sample members.");
            } catch (IOException e) {
                System.out.println("Could not create initial data file: " + e.getMessage());
            }
        }
    }
}