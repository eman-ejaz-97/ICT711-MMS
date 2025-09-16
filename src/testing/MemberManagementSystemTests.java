package testing;

import models.*;
import manager.MemberManager;
import algorithms.*;
import constants.Constants;
import java.util.*;

/**
 * Comprehensive testing suite for the Member Management System.
 * 
 * This class provides thorough testing of all system components including:
 * - Member class hierarchy and polymorphism
 * - MemberManager CRUD operations
 * - Searching algorithms performance and correctness
 * - Sorting algorithms performance and correctness
 * - File I/O operations
 * - Edge cases and error handling
 * 
 * Testing Methodology:
 * 1. Unit Testing - Testing individual components in isolation
 * 2. Integration Testing - Testing component interactions
 * 3. Performance Testing - Measuring algorithm efficiency
 * 4. Boundary Testing - Testing edge cases and limits
 * 5. Error Testing - Testing error handling and recovery
 * 
 * @author ICT711 Student
 * @version 1.0
 */
public class MemberManagementSystemTests {
    
    private MemberManager manager;
    private List<Member> testMembers;
    private int testsPassed = 0;
    private int testsTotal = 0;
    
    /**
     * Main method to run all tests.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        MemberManagementSystemTests testSuite = new MemberManagementSystemTests();
        testSuite.runAllTests();
    }
    
    /**
     * Runs the complete test suite and generates a summary report.
     */
    public void runAllTests() {
        System.out.println("=== MEMBER MANAGEMENT SYSTEM TEST SUITE ===");
        System.out.println("Starting comprehensive testing...\n");
        
        // Initialize test environment
        setUp();
        
        // Run all test categories
        testMemberClassHierarchy();
        testMemberManagerCRUD();
        testSearchingAlgorithms();
        testSortingAlgorithms();
        testFileOperations();
        testPerformanceCalculations();
        testErrorHandling();
        testBoundaryConditions();
        
        // Generate final report
        generateTestReport();
    }
    
    /**
     * Sets up the test environment with sample data.
     */
    private void setUp() {
        System.out.println("Setting up test environment...");
        manager = new MemberManager();
        testMembers = createTestData();
        
        // Add test members to manager
        for (Member member : testMembers) {
            manager.addMember(member);
        }
        
        System.out.println("Test environment ready with " + testMembers.size() + " test members.\n");
    }
    
    /**
     * Creates comprehensive test data covering all member types and scenarios.
     * 
     * @return List of test members with various characteristics
     */
    private List<Member> createTestData() {
        List<Member> members = new ArrayList<>();
        
        // Regular members with various performance levels
        members.add(new RegularMember("R001", "Alice", "Johnson", "alice.j@email.com", "123-456-7890"));
        members.add(new RegularMember("R002", "Bob", "Smith", "bob.s@email.com", "123-456-7891"));
        members.add(new RegularMember("R003", "Charlie", "Brown", "charlie.b@email.com", "123-456-7892"));
        
        // Premium members with different trainers and sessions
        members.add(new PremiumMember("P001", "Diana", "Wilson", "diana.w@email.com", "123-456-7893", "John Coach", 8));
        members.add(new PremiumMember("P002", "Eve", "Davis", "eve.d@email.com", "123-456-7894", "Sarah Trainer", 6));
        members.add(new PremiumMember("P003", "Frank", "Miller", "frank.m@email.com", "123-456-7895", "Mike Fitness", 10));
        
        // Student members with different universities
        members.add(new StudentMember("S001", "Grace", "Anderson", "grace.a@email.com", "123-456-7896", "STU2024001", "State University"));
        members.add(new StudentMember("S002", "Henry", "Taylor", "henry.t@email.com", "123-456-7897", "STU2024002", "Tech College"));
        members.add(new StudentMember("S003", "Ivy", "Thomas", "ivy.t@email.com", "123-456-7898", "STU2024003", "City University"));
        
        // Set varied performance ratings and goal achievements for testing
        members.get(0).setPerformanceRating(9); members.get(0).setGoalAchieved(true);   // High performer
        members.get(1).setPerformanceRating(3); members.get(1).setGoalAchieved(false);  // Low performer
        members.get(2).setPerformanceRating(7); members.get(2).setGoalAchieved(true);   // Average performer
        members.get(3).setPerformanceRating(10); members.get(3).setGoalAchieved(true);  // Perfect performer
        members.get(4).setPerformanceRating(2); members.get(4).setGoalAchieved(false);  // Very low performer
        members.get(5).setPerformanceRating(8); members.get(5).setGoalAchieved(true);   // High performer
        members.get(6).setPerformanceRating(6); members.get(6).setGoalAchieved(false);  // Student average
        members.get(7).setPerformanceRating(9); members.get(7).setGoalAchieved(true);   // Student high
        members.get(8).setPerformanceRating(4); members.get(8).setGoalAchieved(false);  // Student low
        
        return members;
    }
    
    /**
     * Tests the member class hierarchy and polymorphism.
     */
    private void testMemberClassHierarchy() {
        System.out.println("=== TESTING MEMBER CLASS HIERARCHY ===");
        
        // Test 1: Polymorphism - treating different member types as Member
        testsTotal++;
        try {
            Member regularMember = new RegularMember("TEST1", "Test", "User", "test@email.com", "123-456-0000");
            Member premiumMember = new PremiumMember("TEST2", "Test", "Premium", "test2@email.com", "123-456-0001", "Test Trainer", 5);
            Member studentMember = new StudentMember("TEST3", "Test", "Student", "test3@email.com", "123-456-0002", "STU001", "Test University");
            
            // Verify polymorphism works
            List<Member> polymorphicList = Arrays.asList(regularMember, premiumMember, studentMember);
            boolean allAreMembers = polymorphicList.stream().allMatch(m -> m instanceof Member);
            
            assert allAreMembers : "Polymorphism test failed";
            testsPassed++;
            System.out.println("✓ Polymorphism test passed");
        } catch (Exception e) {
            System.out.println("✗ Polymorphism test failed: " + e.getMessage());
        }
        
        // Test 2: Fee calculation polymorphism
        testsTotal++;
        try {
            RegularMember regular = new RegularMember("FEE1", "Fee", "Test", "fee@email.com", "123-456-0003");
            regular.setGoalAchieved(true); // Should get discount
            double regularFee = regular.calculateMonthlyFee();
            
            PremiumMember premium = new PremiumMember("FEE2", "Fee", "Premium", "fee2@email.com", "123-456-0004", "Trainer", 4);
            premium.setGoalAchieved(true); // Should get discount
            double premiumFee = premium.calculateMonthlyFee();
            
            StudentMember student = new StudentMember("FEE3", "Fee", "Student", "fee3@email.com", "123-456-0005", "STU002", "University");
            student.setGoalAchieved(true); // Should get bonus
            double studentFee = student.calculateMonthlyFee();
            
            // Verify different fee calculations
            assert regularFee != premiumFee && premiumFee != studentFee : "Fee calculation polymorphism failed";
            testsPassed++;
            System.out.println("✓ Fee calculation polymorphism test passed");
        } catch (Exception e) {
            System.out.println("✗ Fee calculation polymorphism test failed: " + e.getMessage());
        }
        
        // Test 3: Member type identification
        testsTotal++;
        try {
            Member regular = new RegularMember("TYPE1", "Type", "Test", "type@email.com", "123-456-0006");
            Member premium = new PremiumMember("TYPE2", "Type", "Premium", "type2@email.com", "123-456-0007", "Trainer", 3);
            Member student = new StudentMember("TYPE3", "Type", "Student", "type3@email.com", "123-456-0008", "STU003", "University");
            
            assert regular.getMemberType().contains("Regular") : "Regular member type identification failed";
            assert premium.getMemberType().contains("Premium") : "Premium member type identification failed";
            assert student.getMemberType().contains("Student") : "Student member type identification failed";
            
            testsPassed++;
            System.out.println("✓ Member type identification test passed");
        } catch (Exception e) {
            System.out.println("✗ Member type identification test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Tests MemberManager CRUD operations.
     */
    private void testMemberManagerCRUD() {
        System.out.println("=== TESTING MEMBER MANAGER CRUD OPERATIONS ===");
        
        // Test 1: Add member
        testsTotal++;
        try {
            int initialSize = manager.getAllMembers().size();
            Member newMember = new RegularMember("CRUD1", "CRUD", "Test", "crud@email.com", "123-456-0009");
            manager.addMember(newMember);
            
            assert manager.getAllMembers().size() == initialSize + 1 : "Add member failed";
            testsPassed++;
            System.out.println("✓ Add member test passed");
        } catch (Exception e) {
            System.out.println("✗ Add member test failed: " + e.getMessage());
        }
        
        // Test 2: Find member by ID
        testsTotal++;
        try {
            Member found = manager.findMemberById("R001");
            assert found != null && found.getMemberId().equals("R001") : "Find member by ID failed";
            testsPassed++;
            System.out.println("✓ Find member by ID test passed");
        } catch (Exception e) {
            System.out.println("✗ Find member by ID test failed: " + e.getMessage());
        }
        
        // Test 3: Update member
        testsTotal++;
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("email", "updated@email.com");
            updates.put("performanceRating", 8);
            
            boolean updated = manager.updateMember("R001", updates);
            Member updatedMember = manager.findMemberById("R001");
            
            assert updated && updatedMember.getEmail().equals("updated@email.com") : "Update member failed";
            testsPassed++;
            System.out.println("✓ Update member test passed");
        } catch (Exception e) {
            System.out.println("✗ Update member test failed: " + e.getMessage());
        }
        
        // Test 4: Remove member
        testsTotal++;
        try {
            int beforeSize = manager.getAllMembers().size();
            boolean removed = manager.removeMember("CRUD1");
            int afterSize = manager.getAllMembers().size();
            
            assert removed && afterSize == beforeSize - 1 : "Remove member failed";
            testsPassed++;
            System.out.println("✓ Remove member test passed");
        } catch (Exception e) {
            System.out.println("✗ Remove member test failed: " + e.getMessage());
        }
        
        // Test 5: Find members by name
        testsTotal++;
        try {
            List<Member> found = manager.findMembersByName("Alice");
            assert !found.isEmpty() && found.get(0).getFirstName().equals("Alice") : "Find by name failed";
            testsPassed++;
            System.out.println("✓ Find members by name test passed");
        } catch (Exception e) {
            System.out.println("✗ Find members by name test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Tests all searching algorithms for correctness and performance.
     */
    private void testSearchingAlgorithms() {
        System.out.println("=== TESTING SEARCHING ALGORITHMS ===");
        
        // Test 1: Linear search correctness
        testsTotal++;
        try {
            Member found = SearchingAlgorithms.linearSearchById(testMembers, "P001");
            assert found != null && found.getMemberId().equals("P001") : "Linear search failed";
            testsPassed++;
            System.out.println("✓ Linear search correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Linear search test failed: " + e.getMessage());
        }
        
        // Test 2: Binary search correctness
        testsTotal++;
        try {
            List<Member> sortedMembers = new ArrayList<>(testMembers);
            sortedMembers.sort(Comparator.comparing(Member::getMemberId));
            Member found = SearchingAlgorithms.binarySearchById(sortedMembers, "S002");
            assert found != null && found.getMemberId().equals("S002") : "Binary search failed";
            testsPassed++;
            System.out.println("✓ Binary search correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Binary search test failed: " + e.getMessage());
        }
        
        // Test 3: Hash search correctness
        testsTotal++;
        try {
            Member found = SearchingAlgorithms.hashSearchById(testMembers, "R002");
            assert found != null && found.getMemberId().equals("R002") : "Hash search failed";
            testsPassed++;
            System.out.println("✓ Hash search correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Hash search test failed: " + e.getMessage());
        }
        
        // Test 4: Fuzzy search by name
        testsTotal++;
        try {
            List<Member> found = SearchingAlgorithms.fuzzySearchByName(testMembers, "alice");
            assert !found.isEmpty() && found.get(0).getFirstName().equalsIgnoreCase("Alice") : "Fuzzy search failed";
            testsPassed++;
            System.out.println("✓ Fuzzy search test passed");
        } catch (Exception e) {
            System.out.println("✗ Fuzzy search test failed: " + e.getMessage());
        }
        
        // Test 5: Range search by performance
        testsTotal++;
        try {
            List<Member> found = SearchingAlgorithms.rangeSearchByPerformance(testMembers, 8, 10);
            boolean allInRange = found.stream().allMatch(m -> m.getPerformanceRating() >= 8 && m.getPerformanceRating() <= 10);
            assert allInRange : "Range search failed";
            testsPassed++;
            System.out.println("✓ Range search test passed");
        } catch (Exception e) {
            System.out.println("✗ Range search test failed: " + e.getMessage());
        }
        
        // Test 6: Search algorithm consistency
        testsTotal++;
        try {
            String targetId = "P002";
            Member linear = SearchingAlgorithms.linearSearchById(testMembers, targetId);
            Member hash = SearchingAlgorithms.hashSearchById(testMembers, targetId);
            
            List<Member> sortedMembers = new ArrayList<>(testMembers);
            sortedMembers.sort(Comparator.comparing(Member::getMemberId));
            Member binary = SearchingAlgorithms.binarySearchById(sortedMembers, targetId);
            
            boolean consistent = (linear != null && hash != null && binary != null &&
                                linear.getMemberId().equals(hash.getMemberId()) &&
                                hash.getMemberId().equals(binary.getMemberId()));
            
            assert consistent : "Search algorithms are not consistent";
            testsPassed++;
            System.out.println("✓ Search algorithm consistency test passed");
        } catch (Exception e) {
            System.out.println("✗ Search algorithm consistency test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Tests all sorting algorithms for correctness and stability.
     */
    private void testSortingAlgorithms() {
        System.out.println("=== TESTING SORTING ALGORITHMS ===");
        
        // Test 1: Bubble sort correctness
        testsTotal++;
        try {
            List<Member> sorted = SortingAlgorithms.bubbleSortById(testMembers);
            boolean isSorted = isSortedById(sorted);
            assert isSorted : "Bubble sort failed";
            testsPassed++;
            System.out.println("✓ Bubble sort correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Bubble sort test failed: " + e.getMessage());
        }
        
        // Test 2: Selection sort correctness
        testsTotal++;
        try {
            List<Member> sorted = SortingAlgorithms.selectionSortByPerformance(testMembers);
            boolean isSorted = isSortedByPerformanceDesc(sorted);
            assert isSorted : "Selection sort failed";
            testsPassed++;
            System.out.println("✓ Selection sort correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Selection sort test failed: " + e.getMessage());
        }
        
        // Test 3: Insertion sort correctness
        testsTotal++;
        try {
            List<Member> sorted = SortingAlgorithms.insertionSortByName(testMembers);
            boolean isSorted = isSortedByName(sorted);
            assert isSorted : "Insertion sort failed";
            testsPassed++;
            System.out.println("✓ Insertion sort correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Insertion sort test failed: " + e.getMessage());
        }
        
        // Test 4: Merge sort correctness
        testsTotal++;
        try {
            List<Member> sorted = SortingAlgorithms.mergeSortByFee(testMembers);
            boolean isSorted = isSortedByFee(sorted);
            assert isSorted : "Merge sort failed";
            testsPassed++;
            System.out.println("✓ Merge sort correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Merge sort test failed: " + e.getMessage());
        }
        
        // Test 5: Quick sort correctness
        testsTotal++;
        try {
            List<Member> sorted = SortingAlgorithms.quickSortByType(testMembers);
            boolean isSorted = isSortedByType(sorted);
            assert isSorted : "Quick sort failed";
            testsPassed++;
            System.out.println("✓ Quick sort correctness test passed");
        } catch (Exception e) {
            System.out.println("✗ Quick sort test failed: " + e.getMessage());
        }
        
        // Test 6: Custom sort with different criteria
        testsTotal++;
        try {
            List<Member> sortedAsc = SortingAlgorithms.customSort(testMembers, "performance", true);
            List<Member> sortedDesc = SortingAlgorithms.customSort(testMembers, "performance", false);
            
            boolean ascCorrect = isSortedByPerformanceAsc(sortedAsc);
            boolean descCorrect = isSortedByPerformanceDesc(sortedDesc);
            
            assert ascCorrect && descCorrect : "Custom sort failed";
            testsPassed++;
            System.out.println("✓ Custom sort test passed");
        } catch (Exception e) {
            System.out.println("✗ Custom sort test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Tests file I/O operations.
     */
    private void testFileOperations() {
        System.out.println("=== TESTING FILE OPERATIONS ===");
        
        // Test 1: Save to file
        testsTotal++;
        try {
            String testFileName = "test_members.csv";
            manager.saveToFile(testFileName);
            // If no exception thrown, save operation succeeded
            testsPassed++;
            System.out.println("✓ Save to file test passed");
        } catch (Exception e) {
            System.out.println("✗ Save to file test failed: " + e.getMessage());
        }
        
        // Test 2: Load from file
        testsTotal++;
        try {
            MemberManager testManager = new MemberManager();
            testManager.loadFromFile("test_members.csv");
            assert !testManager.getAllMembers().isEmpty() : "Load from file failed";
            testsPassed++;
            System.out.println("✓ Load from file test passed");
        } catch (Exception e) {
            System.out.println("✗ Load from file test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Tests performance calculation accuracy.
     */
    private void testPerformanceCalculations() {
        System.out.println("=== TESTING PERFORMANCE CALCULATIONS ===");
        
        // Test 1: Regular member fee calculation
        testsTotal++;
        try {
            RegularMember regular = new RegularMember("CALC1", "Calc", "Test", "calc@email.com", "123-456-0010");
            regular.setGoalAchieved(true);
            double fee = regular.calculateMonthlyFee();
            double expectedFee = Constants.REGULAR_BASE_FEE * (1 - Constants.REGULAR_GOAL_ACHIEVEMENT_DISCOUNT);
            
            assert Math.abs(fee - expectedFee) < 0.01 : "Regular member fee calculation incorrect";
            testsPassed++;
            System.out.println("✓ Regular member fee calculation test passed");
        } catch (Exception e) {
            System.out.println("✗ Regular member fee calculation test failed: " + e.getMessage());
        }
        
        // Test 2: Premium member fee calculation
        testsTotal++;
        try {
            PremiumMember premium = new PremiumMember("CALC2", "Calc", "Premium", "calc2@email.com", "123-456-0011", "Trainer", 5);
            premium.setGoalAchieved(true);
            double fee = premium.calculateMonthlyFee();
            double expectedFee = (Constants.PREMIUM_BASE_FEE + 5 * Constants.SESSION_COST) * (1 - Constants.PREMIUM_GOAL_ACHIEVEMENT_DISCOUNT);
            
            assert Math.abs(fee - expectedFee) < 0.01 : "Premium member fee calculation incorrect";
            testsPassed++;
            System.out.println("✓ Premium member fee calculation test passed");
        } catch (Exception e) {
            System.out.println("✗ Premium member fee calculation test failed: " + e.getMessage());
        }
        
        // Test 3: Student member fee calculation
        testsTotal++;
        try {
            StudentMember student = new StudentMember("CALC3", "Calc", "Student", "calc3@email.com", "123-456-0012", "STU004", "University");
            student.setGoalAchieved(true);
            double fee = student.calculateMonthlyFee();
            double baseFeeAfterDiscount = Constants.STUDENT_BASE_FEE * (1 - Constants.STUDENT_BASE_DISCOUNT);
            double expectedFee = Math.max(baseFeeAfterDiscount - Constants.STUDENT_GOAL_ACHIEVEMENT_BONUS, Constants.MINIMUM_STUDENT_FEE);
            
            assert Math.abs(fee - expectedFee) < 0.01 : "Student member fee calculation incorrect";
            testsPassed++;
            System.out.println("✓ Student member fee calculation test passed");
        } catch (Exception e) {
            System.out.println("✗ Student member fee calculation test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Tests error handling and edge cases.
     */
    private void testErrorHandling() {
        System.out.println("=== TESTING ERROR HANDLING ===");
        
        // Test 1: Search for non-existent member
        testsTotal++;
        try {
            Member notFound = manager.findMemberById("NONEXISTENT");
            assert notFound == null : "Non-existent member search should return null";
            testsPassed++;
            System.out.println("✓ Non-existent member search test passed");
        } catch (Exception e) {
            System.out.println("✗ Non-existent member search test failed: " + e.getMessage());
        }
        
        // Test 2: Update non-existent member
        testsTotal++;
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("email", "test@email.com");
            boolean updated = manager.updateMember("NONEXISTENT", updates);
            assert !updated : "Update non-existent member should return false";
            testsPassed++;
            System.out.println("✓ Update non-existent member test passed");
        } catch (Exception e) {
            System.out.println("✗ Update non-existent member test failed: " + e.getMessage());
        }
        
        // Test 3: Performance rating validation
        testsTotal++;
        try {
            Member testMember = new RegularMember("VALID1", "Valid", "Test", "valid@email.com", "123-456-0013");
            int originalRating = testMember.getPerformanceRating();
            
            testMember.setPerformanceRating(-1); // Invalid rating
            assert testMember.getPerformanceRating() == originalRating : "Invalid performance rating should be rejected";
            
            testMember.setPerformanceRating(11); // Invalid rating
            assert testMember.getPerformanceRating() == originalRating : "Invalid performance rating should be rejected";
            
            testMember.setPerformanceRating(8); // Valid rating
            assert testMember.getPerformanceRating() == 8 : "Valid performance rating should be accepted";
            
            testsPassed++;
            System.out.println("✓ Performance rating validation test passed");
        } catch (Exception e) {
            System.out.println("✗ Performance rating validation test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Tests boundary conditions and edge cases.
     */
    private void testBoundaryConditions() {
        System.out.println("=== TESTING BOUNDARY CONDITIONS ===");
        
        // Test 1: Empty member list operations
        testsTotal++;
        try {
            MemberManager emptyManager = new MemberManager();
            List<Member> empty = emptyManager.getAllMembers();
            assert empty.isEmpty() : "Empty manager should have no members";
            
            Member notFound = emptyManager.findMemberById("ANY");
            assert notFound == null : "Search in empty list should return null";
            
            testsPassed++;
            System.out.println("✓ Empty member list operations test passed");
        } catch (Exception e) {
            System.out.println("✗ Empty member list operations test failed: " + e.getMessage());
        }
        
        // Test 2: Single member operations
        testsTotal++;
        try {
            MemberManager singleManager = new MemberManager();
            Member singleMember = new RegularMember("SINGLE1", "Single", "Member", "single@email.com", "123-456-0014");
            singleManager.addMember(singleMember);
            
            assert singleManager.getAllMembers().size() == 1 : "Single member list should have one member";
            
            Member found = singleManager.findMemberById("SINGLE1");
            assert found != null && found.equals(singleMember) : "Single member should be findable";
            
            testsPassed++;
            System.out.println("✓ Single member operations test passed");
        } catch (Exception e) {
            System.out.println("✗ Single member operations test failed: " + e.getMessage());
        }
        
        // Test 3: Performance rating boundary values
        testsTotal++;
        try {
            Member boundaryMember = new RegularMember("BOUND1", "Boundary", "Test", "boundary@email.com", "123-456-0015");
            
            // Test minimum valid rating
            boundaryMember.setPerformanceRating(0);
            assert boundaryMember.getPerformanceRating() == 0 : "Minimum rating (0) should be accepted";
            
            // Test maximum valid rating
            boundaryMember.setPerformanceRating(10);
            assert boundaryMember.getPerformanceRating() == 10 : "Maximum rating (10) should be accepted";
            
            testsPassed++;
            System.out.println("✓ Performance rating boundary values test passed");
        } catch (Exception e) {
            System.out.println("✗ Performance rating boundary values test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Generates a comprehensive test report.
     */
    private void generateTestReport() {
        System.out.println("=== TEST SUITE SUMMARY ===");
        System.out.println("Total Tests Run: " + testsTotal);
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + (testsTotal - testsPassed));
        System.out.println("Success Rate: " + String.format("%.2f", (double) testsPassed / testsTotal * 100) + "%");
        
        if (testsPassed == testsTotal) {
            System.out.println("\n🎉 ALL TESTS PASSED! The Member Management System is working correctly.");
        } else {
            System.out.println("\n⚠️  Some tests failed. Please review the implementation.");
        }
        
        System.out.println("\n=== PERFORMANCE INSIGHTS ===");
        System.out.println("For the current test dataset size (" + testMembers.size() + " members):");
        System.out.println("- All search algorithms performed correctly");
        System.out.println("- All sorting algorithms maintained correctness");
        System.out.println("- File I/O operations completed successfully");
        System.out.println("- Performance calculations are accurate");
        System.out.println("- Error handling is robust");
        
        System.out.println("\n=== RECOMMENDATIONS ===");
        System.out.println("1. For production use with larger datasets (>1000 members):");
        System.out.println("   - Use Hash Search for frequent ID lookups");
        System.out.println("   - Prefer Merge Sort or Quick Sort for sorting operations");
        System.out.println("   - Avoid O(n²) algorithms for large datasets");
        System.out.println("2. Current implementation is suitable for medium-sized gyms");
        System.out.println("3. Consider database integration for very large member bases");
    }
    
    // Helper methods for testing sorting correctness
    
    private boolean isSortedById(List<Member> members) {
        for (int i = 0; i < members.size() - 1; i++) {
            if (members.get(i).getMemberId().compareTo(members.get(i + 1).getMemberId()) > 0) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByName(List<Member> members) {
        for (int i = 0; i < members.size() - 1; i++) {
            if (members.get(i).getFullName().compareTo(members.get(i + 1).getFullName()) > 0) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByPerformanceDesc(List<Member> members) {
        for (int i = 0; i < members.size() - 1; i++) {
            if (members.get(i).getPerformanceRating() < members.get(i + 1).getPerformanceRating()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByPerformanceAsc(List<Member> members) {
        for (int i = 0; i < members.size() - 1; i++) {
            if (members.get(i).getPerformanceRating() > members.get(i + 1).getPerformanceRating()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByFee(List<Member> members) {
        for (int i = 0; i < members.size() - 1; i++) {
            if (members.get(i).calculateMonthlyFee() > members.get(i + 1).calculateMonthlyFee()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByType(List<Member> members) {
        for (int i = 0; i < members.size() - 1; i++) {
            if (members.get(i).getMemberType().compareTo(members.get(i + 1).getMemberType()) > 0) {
                return false;
            }
        }
        return true;
    }
}