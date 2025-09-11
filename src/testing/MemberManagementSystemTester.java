package testing;

import manager.MemberManager;
import models.*;
import algorithms.*;
import constants.Constants;
import java.util.*;
import java.io.*;

/**
 * Comprehensive test suite for the Member Management System.
 * 
 * Tests all core functionality including:
 * - Member CRUD operations
 * - File I/O operations
 * - Search algorithm performance and correctness
 * - Sort algorithm performance and correctness
 * - Business logic validation
 * - Error handling scenarios
 * 
 * Provides detailed test results and performance metrics.
 */
public class MemberManagementSystemTester {
    private MemberManager manager;
    private MemberSearcher searcher;
    private MemberSorter sorter;
    
    private int testsPassed = 0;
    private int testsFailed = 0;
    private List<String> failedTests = new ArrayList<>();
    
    public MemberManagementSystemTester() {
        manager = new MemberManager();
        searcher = new MemberSearcher(manager);
        sorter = new MemberSorter();
    }
    
    /**
     * Runs all test suites and generates comprehensive report.
     */
    public void runAllTests() {
        System.out.println("=== MEMBER MANAGEMENT SYSTEM - COMPREHENSIVE TESTING ===\n");
        
        long startTime = System.currentTimeMillis();
        
        // Test suites
        testMemberOperations();
        testFileOperations();
        testSearchAlgorithms();
        testSortAlgorithms();
        testBusinessLogic();
        testErrorHandling();
        testPerformance();
        
        long endTime = System.currentTimeMillis();
        
        // Generate final report
        generateTestReport(endTime - startTime);
    }
    
    /**
     * Tests member CRUD operations.
     */
    private void testMemberOperations() {
        System.out.println("--- Testing Member Operations ---");
        
        // Test member creation
        testAddMember();
        testAddDifferentMemberTypes();
        
        // Test member retrieval
        testFindMemberById();
        testFindMembersByName();
        testFindMembersByPerformance();
        
        // Test member updates
        testUpdateMember();
        
        // Test member deletion
        testRemoveMember();
        
        System.out.println();
    }
    
    private void testAddMember() {
        try {
            RegularMember member = new RegularMember("TEST001", "John", "Doe", "john@email.com", "123-456-7890");
            manager.addMember(member);
            
            Member found = manager.findMemberById("TEST001");
            assert found != null : "Member should be found after adding";
            assert found.getFullName().equals("John Doe") : "Member name should match";
            
            reportTest("Add Regular Member", true, "Successfully added and retrieved member");
        } catch (Exception e) {
            reportTest("Add Regular Member", false, "Error: " + e.getMessage());
        }
    }
    
    private void testAddDifferentMemberTypes() {
        try {
            // Premium member
            PremiumMember premium = new PremiumMember("PREM001", "Jane", "Smith", "jane@email.com", "123-456-7891", "Trainer A", 8);
            manager.addMember(premium);
            
            // Student member
            StudentMember student = new StudentMember("STU001", "Bob", "Johnson", "bob@email.com", "123-456-7892", "STU2024001", "University A");
            manager.addMember(student);
            
            List<Member> allMembers = manager.getAllMembers();
            boolean hasPremium = allMembers.stream().anyMatch(m -> m instanceof PremiumMember);
            boolean hasStudent = allMembers.stream().anyMatch(m -> m instanceof StudentMember);
            
            assert hasPremium : "Should contain premium member";
            assert hasStudent : "Should contain student member";
            
            reportTest("Add Different Member Types", true, "Successfully added premium and student members");
        } catch (Exception e) {
            reportTest("Add Different Member Types", false, "Error: " + e.getMessage());
        }
    }
    
    private void testFindMemberById() {
        try {
            Member found = manager.findMemberById("TEST001");
            assert found != null : "Should find existing member";
            assert found.getMemberId().equals("TEST001") : "Should return correct member";
            
            Member notFound = manager.findMemberById("NONEXISTENT");
            assert notFound == null : "Should return null for non-existent member";
            
            reportTest("Find Member By ID", true, "Correctly handles existing and non-existent members");
        } catch (Exception e) {
            reportTest("Find Member By ID", false, "Error: " + e.getMessage());
        }
    }
    
    private void testFindMembersByName() {
        try {
            List<Member> found = manager.findMembersByName("John");
            assert !found.isEmpty() : "Should find members with matching names";
            
            List<Member> notFound = manager.findMembersByName("NonExistentName");
            assert notFound.isEmpty() : "Should return empty list for non-matching names";
            
            reportTest("Find Members By Name", true, "Correctly searches by name");
        } catch (Exception e) {
            reportTest("Find Members By Name", false, "Error: " + e.getMessage());
        }
    }
    
    private void testFindMembersByPerformance() {
        try {
            // Set some performance ratings first
            Member member = manager.findMemberById("TEST001");
            if (member != null) {
                member.setPerformanceRating(8);
            }
            
            List<Member> highPerformers = manager.findMembersByPerformance(7);
            boolean containsHighPerformer = highPerformers.stream().anyMatch(m -> m.getPerformanceRating() >= 7);
            assert containsHighPerformer : "Should find high-performing members";
            
            reportTest("Find Members By Performance", true, "Correctly filters by performance rating");
        } catch (Exception e) {
            reportTest("Find Members By Performance", false, "Error: " + e.getMessage());
        }
    }
    
    private void testUpdateMember() {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put(Constants.UPDATE_KEY_EMAIL, "newemail@test.com");
            updates.put(Constants.UPDATE_KEY_PERFORMANCE_RATING, 9);
            
            boolean updated = manager.updateMember("TEST001", updates);
            assert updated : "Update should succeed";
            
            Member member = manager.findMemberById("TEST001");
            assert member.getEmail().equals("newemail@test.com") : "Email should be updated";
            assert member.getPerformanceRating() == 9 : "Performance rating should be updated";
            
            reportTest("Update Member", true, "Successfully updates member fields");
        } catch (Exception e) {
            reportTest("Update Member", false, "Error: " + e.getMessage());
        }
    }
    
    private void testRemoveMember() {
        try {
            boolean removed = manager.removeMember("TEST001");
            assert removed : "Removal should succeed";
            
            Member notFound = manager.findMemberById("TEST001");
            assert notFound == null : "Member should not be found after removal";
            
            boolean notRemoved = manager.removeMember("NONEXISTENT");
            assert !notRemoved : "Removing non-existent member should return false";
            
            reportTest("Remove Member", true, "Correctly removes existing members");
        } catch (Exception e) {
            reportTest("Remove Member", false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Tests file I/O operations.
     */
    private void testFileOperations() {
        System.out.println("--- Testing File Operations ---");
        
        testSaveToFile();
        testLoadFromFile();
        testFileErrorHandling();
        
        System.out.println();
    }
    
    private void testSaveToFile() {
        try {
            String testFile = "test_save.csv";
            manager.saveToFile(testFile);
            
            File file = new File(testFile);
            assert file.exists() : "File should be created";
            assert file.length() > 0 : "File should contain data";
            
            // Clean up
            file.delete();
            
            reportTest("Save To File", true, "Successfully saves data to CSV file");
        } catch (Exception e) {
            reportTest("Save To File", false, "Error: " + e.getMessage());
        }
    }
    
    private void testLoadFromFile() {
        try {
            // Create test data file
            String testFile = "test_load.csv";
            try (PrintWriter writer = new PrintWriter(new FileWriter(testFile))) {
                writer.println("Type,ID,FirstName,LastName,Email,Phone,PerformanceRating,GoalAchieved,Extra1,Extra2");
                writer.println("Regular,LOAD001,Load,Test,load@test.com,555-0001,7,true,,");
                writer.println("Premium,LOAD002,Premium,Test,premium@test.com,555-0002,8,true,TestTrainer,6");
                writer.println("Student,LOAD003,Student,Test,student@test.com,555-0003,6,false,STU001,Test University");
            }
            
            int beforeCount = manager.getAllMembers().size();
            manager.loadFromFile(testFile);
            int afterCount = manager.getAllMembers().size();
            
            assert afterCount > beforeCount : "Should load additional members";
            
            Member loadedMember = manager.findMemberById("LOAD001");
            assert loadedMember != null : "Should load regular member";
            
            // Clean up
            new File(testFile).delete();
            
            reportTest("Load From File", true, "Successfully loads data from CSV file");
        } catch (Exception e) {
            reportTest("Load From File", false, "Error: " + e.getMessage());
        }
    }
    
    private void testFileErrorHandling() {
        try {
            // Test loading non-existent file
            try {
                manager.loadFromFile("nonexistent.csv");
                reportTest("File Error Handling", false, "Should throw exception for non-existent file");
            } catch (IOException e) {
                reportTest("File Error Handling", true, "Correctly handles missing file: " + e.getClass().getSimpleName());
            }
        } catch (Exception e) {
            reportTest("File Error Handling", false, "Unexpected error: " + e.getMessage());
        }
    }
    
    /**
     * Tests search algorithms.
     */
    private void testSearchAlgorithms() {
        System.out.println("--- Testing Search Algorithms ---");
        
        // Create test data
        createTestMembers();
        
        testLinearSearch();
        testHashSearch();
        testAdvancedSearch();
        testSearchPerformance();
        
        System.out.println();
    }
    
    private void createTestMembers() {
        // Clear existing members
        for (Member member : new ArrayList<>(manager.getAllMembers())) {
            manager.removeMember(member.getMemberId());
        }
        
        // Add test members
        for (int i = 1; i <= 100; i++) {
            String id = String.format("M%03d", i);
            String firstName = "Member" + i;
            String lastName = "Test" + (i % 10);
            String email = "member" + i + "@test.com";
            String phone = "555-" + String.format("%04d", i);
            
            Member member;
            if (i % 3 == 0) {
                member = new PremiumMember(id, firstName, lastName, email, phone, "Trainer" + (i % 5), i % 10 + 1);
            } else if (i % 5 == 0) {
                member = new StudentMember(id, firstName, lastName, email, phone, "STU" + i, "University" + (i % 3));
            } else {
                member = new RegularMember(id, firstName, lastName, email, phone);
            }
            
            member.setPerformanceRating(i % 11); // 0-10
            member.setGoalAchieved(i % 3 == 0);
            
            manager.addMember(member);
        }
        
        // Rebuild search indexes
        searcher.buildIndexes();
    }
    
    private void testLinearSearch() {
        try {
            long startTime = System.nanoTime();
            List<Member> results = searcher.search("Member50", "Name");
            long endTime = System.nanoTime();
            
            assert !results.isEmpty() : "Should find members with matching names";
            assert results.get(0).getFirstName().contains("Member50") : "Should return correct member";
            
            double searchTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
            reportTest("Linear Search", true, String.format("Found %d results in %.2f ms", results.size(), searchTime));
        } catch (Exception e) {
            reportTest("Linear Search", false, "Error: " + e.getMessage());
        }
    }
    
    private void testHashSearch() {
        try {
            long startTime = System.nanoTime();
            List<Member> results = searcher.searchById("M050");
            long endTime = System.nanoTime();
            
            assert results.size() == 1 : "Should find exactly one member";
            assert results.get(0).getMemberId().equals("M050") : "Should return correct member";
            
            double searchTime = (endTime - startTime) / 1_000_000.0;
            reportTest("Hash Search", true, String.format("Found member in %.2f ms", searchTime));
        } catch (Exception e) {
            reportTest("Hash Search", false, "Error: " + e.getMessage());
        }
    }
    
    private void testAdvancedSearch() {
        try {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("type", "Premium");
            criteria.put("goal", "true");
            
            List<Member> results = searcher.advancedSearch(criteria);
            
            boolean allPremium = results.stream().allMatch(m -> m instanceof PremiumMember);
            boolean allGoalAchieved = results.stream().allMatch(Member::isGoalAchieved);
            
            assert allPremium : "All results should be premium members";
            assert allGoalAchieved : "All results should have achieved goals";
            
            reportTest("Advanced Search", true, String.format("Found %d members matching multiple criteria", results.size()));
        } catch (Exception e) {
            reportTest("Advanced Search", false, "Error: " + e.getMessage());
        }
    }
    
    private void testSearchPerformance() {
        try {
            Map<String, Object> stats = searcher.getSearchStatistics();
            
            assert stats.containsKey("totalMembers") : "Should provide total members count";
            assert stats.containsKey("indexSize") : "Should provide index size";
            assert (Integer) stats.get("totalMembers") > 0 : "Should have members in system";
            
            reportTest("Search Performance", true, String.format("Statistics: %d members, %d indexed", 
                stats.get("totalMembers"), stats.get("indexSize")));
        } catch (Exception e) {
            reportTest("Search Performance", false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Tests sort algorithms.
     */
    private void testSortAlgorithms() {
        System.out.println("--- Testing Sort Algorithms ---");
        
        testQuickSort();
        testMergeSort();
        testHeapSort();
        testInsertionSort();
        testCountingSort();
        testSortPerformance();
        
        System.out.println();
    }
    
    private void testQuickSort() {
        try {
            List<Member> members = manager.getAllMembers();
            List<Member> sorted = sorter.sort(members, "Name", MemberSorter.SortOrder.ASCENDING, MemberSorter.SortAlgorithm.QUICK_SORT);
            
            assert isSortedByName(sorted) : "Should be sorted by name";
            assert sorted.size() == members.size() : "Should preserve all members";
            
            reportTest("Quick Sort", true, "Correctly sorts by name");
        } catch (Exception e) {
            reportTest("Quick Sort", false, "Error: " + e.getMessage());
        }
    }
    
    private void testMergeSort() {
        try {
            List<Member> members = manager.getAllMembers();
            List<Member> sorted = sorter.sort(members, "Performance", MemberSorter.SortOrder.DESCENDING, MemberSorter.SortAlgorithm.MERGE_SORT);
            
            assert isSortedByPerformanceDesc(sorted) : "Should be sorted by performance descending";
            
            reportTest("Merge Sort", true, "Correctly sorts by performance (descending)");
        } catch (Exception e) {
            reportTest("Merge Sort", false, "Error: " + e.getMessage());
        }
    }
    
    private void testHeapSort() {
        try {
            List<Member> members = manager.getAllMembers();
            List<Member> sorted = sorter.sort(members, "Monthly Fee", MemberSorter.SortOrder.ASCENDING, MemberSorter.SortAlgorithm.HEAP_SORT);
            
            assert isSortedByFee(sorted) : "Should be sorted by monthly fee";
            
            reportTest("Heap Sort", true, "Correctly sorts by monthly fee");
        } catch (Exception e) {
            reportTest("Heap Sort", false, "Error: " + e.getMessage());
        }
    }
    
    private void testInsertionSort() {
        try {
            // Test with smaller dataset for insertion sort
            List<Member> smallSet = manager.getAllMembers().subList(0, Math.min(20, manager.getAllMembers().size()));
            List<Member> sorted = sorter.sort(smallSet, "ID", MemberSorter.SortOrder.ASCENDING, MemberSorter.SortAlgorithm.INSERTION_SORT);
            
            assert isSortedById(sorted) : "Should be sorted by ID";
            
            reportTest("Insertion Sort", true, "Correctly sorts small dataset by ID");
        } catch (Exception e) {
            reportTest("Insertion Sort", false, "Error: " + e.getMessage());
        }
    }
    
    private void testCountingSort() {
        try {
            List<Member> members = manager.getAllMembers();
            List<Member> sorted = sorter.countingSortByPerformance(members, MemberSorter.SortOrder.ASCENDING);
            
            assert isSortedByPerformance(sorted) : "Should be sorted by performance";
            
            reportTest("Counting Sort", true, "Correctly sorts by performance rating");
        } catch (Exception e) {
            reportTest("Counting Sort", false, "Error: " + e.getMessage());
        }
    }
    
    private void testSortPerformance() {
        try {
            Map<String, Long> benchmarkResults = sorter.benchmark(manager.getAllMembers(), "Name");
            
            assert !benchmarkResults.isEmpty() : "Should provide benchmark results";
            
            StringBuilder results = new StringBuilder("Benchmark times (ns): ");
            benchmarkResults.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEach(entry -> results.append(entry.getKey()).append("=").append(entry.getValue()).append(" "));
            
            reportTest("Sort Performance", true, results.toString());
        } catch (Exception e) {
            reportTest("Sort Performance", false, "Error: " + e.getMessage());
        }
    }
    
    // Helper methods for sort validation
    private boolean isSortedByName(List<Member> members) {
        for (int i = 1; i < members.size(); i++) {
            if (members.get(i-1).getFullName().compareToIgnoreCase(members.get(i).getFullName()) > 0) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByPerformanceDesc(List<Member> members) {
        for (int i = 1; i < members.size(); i++) {
            if (members.get(i-1).getPerformanceRating() < members.get(i).getPerformanceRating()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByPerformance(List<Member> members) {
        for (int i = 1; i < members.size(); i++) {
            if (members.get(i-1).getPerformanceRating() > members.get(i).getPerformanceRating()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedByFee(List<Member> members) {
        for (int i = 1; i < members.size(); i++) {
            if (members.get(i-1).calculateMonthlyFee() > members.get(i).calculateMonthlyFee()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSortedById(List<Member> members) {
        for (int i = 1; i < members.size(); i++) {
            if (members.get(i-1).getMemberId().compareToIgnoreCase(members.get(i).getMemberId()) > 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Tests business logic and calculations.
     */
    private void testBusinessLogic() {
        System.out.println("--- Testing Business Logic ---");
        
        testMemberTypeBehavior();
        testPerformanceCalculations();
        testStatisticsGeneration();
        
        System.out.println();
    }
    
    private void testMemberTypeBehavior() {
        try {
            RegularMember regular = new RegularMember("REG001", "Regular", "Member", "reg@test.com", "555-1111");
            PremiumMember premium = new PremiumMember("PREM001", "Premium", "Member", "prem@test.com", "555-2222", "Trainer", 8);
            StudentMember student = new StudentMember("STU001", "Student", "Member", "stu@test.com", "555-3333", "STU001", "University");
            
            // Test different fee calculations
            double regFee = regular.calculateMonthlyFee();
            double premFee = premium.calculateMonthlyFee();
            double stuFee = student.calculateMonthlyFee();
            
            assert regFee > 0 : "Regular member should have positive fee";
            assert premFee > 0 : "Premium member should have positive fee";
            assert stuFee > 0 : "Student member should have positive fee";
            
            // Test polymorphism
            assert regular.getMemberType().contains("Regular") : "Should identify as Regular";
            assert premium.getMemberType().contains("Premium") : "Should identify as Premium";
            assert student.getMemberType().contains("Student") : "Should identify as Student";
            
            reportTest("Member Type Behavior", true, String.format("Fees: Regular=%.2f, Premium=%.2f, Student=%.2f", regFee, premFee, stuFee));
        } catch (Exception e) {
            reportTest("Member Type Behavior", false, "Error: " + e.getMessage());
        }
    }
    
    private void testPerformanceCalculations() {
        try {
            Member member = manager.getAllMembers().get(0);
            if (member != null) {
                member.setPerformanceRating(8);
                member.setGoalAchieved(true);
                
                String report = member.generatePerformanceReport();
                assert report != null && !report.isEmpty() : "Should generate performance report";
                assert report.contains("Performance Rating: 8") : "Report should contain performance rating";
                
                reportTest("Performance Calculations", true, "Successfully generates performance reports");
            } else {
                reportTest("Performance Calculations", false, "No members available for testing");
            }
        } catch (Exception e) {
            reportTest("Performance Calculations", false, "Error: " + e.getMessage());
        }
    }
    
    private void testStatisticsGeneration() {
        try {
            // Capture statistics output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(baos));
            
            manager.displayStatistics();
            
            System.setOut(originalOut);
            String output = baos.toString();
            
            assert output.contains("Total Members") : "Should display total members";
            assert output.contains("Average Performance") : "Should display average performance";
            
            reportTest("Statistics Generation", true, "Successfully generates system statistics");
        } catch (Exception e) {
            reportTest("Statistics Generation", false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Tests error handling scenarios.
     */
    private void testErrorHandling() {
        System.out.println("--- Testing Error Handling ---");
        
        testNullInputHandling();
        testInvalidDataHandling();
        testBoundaryConditions();
        
        System.out.println();
    }
    
    private void testNullInputHandling() {
        try {
            // Test search with null input
            List<Member> results = searcher.search(null, "Name");
            assert results.size() == manager.getAllMembers().size() : "Null search should return all members";
            
            // Test sort with empty list
            List<Member> emptyList = new ArrayList<>();
            List<Member> sortedEmpty = sorter.sort(emptyList, "Name");
            assert sortedEmpty.isEmpty() : "Sorting empty list should return empty list";
            
            reportTest("Null Input Handling", true, "Correctly handles null and empty inputs");
        } catch (Exception e) {
            reportTest("Null Input Handling", false, "Error: " + e.getMessage());
        }
    }
    
    private void testInvalidDataHandling() {
        try {
            // Test invalid performance rating
            Member testMember = new RegularMember("INVALID001", "Test", "Invalid", "test@invalid.com", "555-0000");
            testMember.setPerformanceRating(-1); // Invalid rating
            
            // Should handle gracefully
            double fee = testMember.calculateMonthlyFee();
            assert fee >= 0 : "Fee calculation should handle invalid ratings gracefully";
            
            reportTest("Invalid Data Handling", true, "Handles invalid performance ratings gracefully");
        } catch (Exception e) {
            reportTest("Invalid Data Handling", false, "Error: " + e.getMessage());
        }
    }
    
    private void testBoundaryConditions() {
        try {
            // Test with maximum performance rating
            Member testMember = new RegularMember("BOUNDARY001", "Test", "Boundary", "test@boundary.com", "555-0000");
            testMember.setPerformanceRating(10); // Maximum rating
            
            double maxFee = testMember.calculateMonthlyFee();
            
            // Test with minimum performance rating
            testMember.setPerformanceRating(0); // Minimum rating
            double minFee = testMember.calculateMonthlyFee();
            
            assert maxFee >= minFee : "Higher performance should not result in lower fee";
            
            reportTest("Boundary Conditions", true, "Handles boundary performance ratings correctly");
        } catch (Exception e) {
            reportTest("Boundary Conditions", false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Tests overall system performance.
     */
    private void testPerformance() {
        System.out.println("--- Testing Performance ---");
        
        testLargeDatasetHandling();
        testMemoryUsage();
        testConcurrentOperations();
        
        System.out.println();
    }
    
    private void testLargeDatasetHandling() {
        try {
            int originalSize = manager.getAllMembers().size();
            
            // Add 500 more members
            long startTime = System.currentTimeMillis();
            for (int i = 1000; i < 1500; i++) {
                RegularMember member = new RegularMember("LARGE" + i, "Member", "Large" + i, "large" + i + "@test.com", "555-" + i);
                manager.addMember(member);
            }
            long addTime = System.currentTimeMillis() - startTime;
            
            // Test search performance on large dataset
            searcher.buildIndexes();
            startTime = System.currentTimeMillis();
            List<Member> results = searcher.search("LARGE1250", "ID");
            long searchTime = System.currentTimeMillis() - startTime;
            
            assert !results.isEmpty() : "Should find member in large dataset";
            assert manager.getAllMembers().size() == originalSize + 500 : "Should have added all members";
            
            reportTest("Large Dataset Handling", true, String.format("Added 500 members in %d ms, searched in %d ms", addTime, searchTime));
        } catch (Exception e) {
            reportTest("Large Dataset Handling", false, "Error: " + e.getMessage());
        }
    }
    
    private void testMemoryUsage() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            
            // Perform memory-intensive operation
            for (int i = 0; i < 10; i++) {
                List<Member> sorted = sorter.sort(manager.getAllMembers(), "Name");
                sorted.clear(); // Help GC
            }
            
            System.gc(); // Suggest garbage collection
            Thread.sleep(100); // Give GC time to work
            
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = afterMemory - beforeMemory;
            
            reportTest("Memory Usage", true, String.format("Memory delta: %d KB", memoryUsed / 1024));
        } catch (Exception e) {
            reportTest("Memory Usage", false, "Error: " + e.getMessage());
        }
    }
    
    private void testConcurrentOperations() {
        try {
            // Simple concurrency test
            List<Thread> threads = new ArrayList<>();
            List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
            
            for (int i = 0; i < 5; i++) {
                final int threadId = i;
                Thread thread = new Thread(() -> {
                    try {
                        // Each thread performs different operations
                        List<Member> members = manager.getAllMembers();
                        List<Member> sorted = sorter.sort(members, "Name");
                        List<Member> searched = searcher.search("Member", "Name");
                    } catch (Exception e) {
                        exceptions.add(e);
                    }
                });
                threads.add(thread);
                thread.start();
            }
            
            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join(5000); // 5 second timeout
            }
            
            assert exceptions.isEmpty() : "No exceptions should occur during concurrent operations";
            
            reportTest("Concurrent Operations", true, "Successfully handles concurrent read operations");
        } catch (Exception e) {
            reportTest("Concurrent Operations", false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Reports test result and updates counters.
     */
    private void reportTest(String testName, boolean passed, String details) {
        String status = passed ? "PASS" : "FAIL";
        String color = passed ? "\u001B[32m" : "\u001B[31m"; // Green or Red
        String reset = "\u001B[0m";
        
        System.out.printf("%s[%s]%s %s: %s%n", color, status, reset, testName, details);
        
        if (passed) {
            testsPassed++;
        } else {
            testsFailed++;
            failedTests.add(testName + ": " + details);
        }
    }
    
    /**
     * Generates final test report.
     */
    private void generateTestReport(long totalTime) {
        System.out.println("\n=== TEST EXECUTION SUMMARY ===");
        System.out.println("Total Tests Run: " + (testsPassed + testsFailed));
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.printf("Success Rate: %.1f%%\n", (testsPassed * 100.0) / (testsPassed + testsFailed));
        System.out.println("Total Execution Time: " + totalTime + " ms");
        
        if (!failedTests.isEmpty()) {
            System.out.println("\nFAILED TESTS:");
            for (String failure : failedTests) {
                System.out.println("  ❌ " + failure);
            }
        }
        
        System.out.println("\n=== SYSTEM HEALTH CHECK ===");
        System.out.println("Total Members in System: " + manager.getAllMembers().size());
        
        Map<String, Object> searchStats = searcher.getSearchStatistics();
        System.out.println("Search Index Size: " + searchStats.get("indexSize"));
        
        Map<String, Object> sortStats = sorter.getSortStatistics();
        if (sortStats.get("lastAlgorithmUsed") != null) {
            System.out.println("Last Sort Algorithm: " + sortStats.get("lastAlgorithmUsed"));
            System.out.println("Last Sort Time: " + sortStats.get("lastSortTimeMs") + " ms");
        }
        
        System.out.println("\n=== TESTING COMPLETE ===");
    }
    
    /**
     * Main method to run all tests.
     */
    public static void main(String[] args) {
        MemberManagementSystemTester tester = new MemberManagementSystemTester();
        tester.runAllTests();
    }
}