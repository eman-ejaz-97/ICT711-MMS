package algorithms;

import models.Member;
import java.util.*;

/**
 * Implementation of various searching algorithms for the Member Management System.
 * 
 * This class demonstrates different searching techniques and their applications
 * in managing member data efficiently. Each algorithm has different time complexities
 * and use cases.
 * 
 * Searching Algorithms Implemented:
 * 1. Linear Search - O(n) - Best for unsorted data
 * 2. Binary Search - O(log n) - Requires sorted data
 * 3. Hash-based Search - O(1) average - Uses HashMap for fast lookups
 * 
 * @author ICT711 Student
 * @version 1.0
 */
public class SearchingAlgorithms {
    
    /**
     * Linear Search Algorithm - searches through list sequentially.
     * 
     * Time Complexity: O(n) where n is the number of members
     * Space Complexity: O(1)
     * 
     * Best Use Case: When data is unsorted or small datasets
     * Worst Case: Target is at the end or not present
     * 
     * @param members List of members to search through
     * @param memberId Target member ID to find
     * @return Member object if found, null otherwise
     */
    public static Member linearSearchById(List<Member> members, String memberId) {
        // Start from the beginning and check each member one by one
        for (int i = 0; i < members.size(); i++) {
            Member currentMember = members.get(i);
            
            // Compare current member's ID with target ID
            if (currentMember.getMemberId().equals(memberId)) {
                System.out.println("Linear Search: Found member at index " + i + " after " + (i + 1) + " comparisons");
                return currentMember; // Found the member, return it
            }
        }
        
        System.out.println("Linear Search: Member not found after " + members.size() + " comparisons");
        return null; // Member not found
    }
    
    /**
     * Binary Search Algorithm - searches through SORTED list by dividing search space in half.
     * 
     * Time Complexity: O(log n) where n is the number of members
     * Space Complexity: O(1) for iterative version
     * 
     * Prerequisite: List must be sorted by member ID
     * Best Use Case: Large sorted datasets
     * 
     * @param sortedMembers List of members SORTED by member ID
     * @param memberId Target member ID to find
     * @return Member object if found, null otherwise
     */
    public static Member binarySearchById(List<Member> sortedMembers, String memberId) {
        int left = 0; // Start of search range
        int right = sortedMembers.size() - 1; // End of search range
        int comparisons = 0; // Track number of comparisons for analysis
        
        // Continue searching while there are elements to examine
        while (left <= right) {
            comparisons++;
            int middle = left + (right - left) / 2; // Calculate middle index (avoids overflow)
            Member middleMember = sortedMembers.get(middle);
            
            // Compare middle element with target
            int comparison = middleMember.getMemberId().compareTo(memberId);
            
            if (comparison == 0) {
                // Found exact match!
                System.out.println("Binary Search: Found member at index " + middle + " after " + comparisons + " comparisons");
                return middleMember;
            } else if (comparison < 0) {
                // Middle element is smaller than target, search right half
                left = middle + 1;
            } else {
                // Middle element is larger than target, search left half
                right = middle - 1;
            }
        }
        
        System.out.println("Binary Search: Member not found after " + comparisons + " comparisons");
        return null; // Member not found
    }
    
    /**
     * Hash-based Search Algorithm - uses HashMap for O(1) average lookup time.
     * 
     * Time Complexity: O(1) average case, O(n) worst case (hash collisions)
     * Space Complexity: O(n) for the hash map
     * 
     * Best Use Case: Frequent searches on large datasets
     * Trade-off: Uses more memory but provides fastest search
     * 
     * @param members List of members to build hash map from
     * @param memberId Target member ID to find
     * @return Member object if found, null otherwise
     */
    public static Member hashSearchById(List<Member> members, String memberId) {
        // Build hash map for O(1) lookups (one-time cost)
        Map<String, Member> memberMap = buildMemberHashMap(members);
        
        // Perform O(1) lookup
        Member result = memberMap.get(memberId);
        
        if (result != null) {
            System.out.println("Hash Search: Found member in O(1) time using HashMap");
        } else {
            System.out.println("Hash Search: Member not found in O(1) time");
        }
        
        return result;
    }
    
    /**
     * Builds a HashMap from member list for fast ID-based lookups.
     * This is a utility method used by hash-based search.
     * 
     * @param members List of members to index
     * @return HashMap with member ID as key and Member object as value
     */
    private static Map<String, Member> buildMemberHashMap(List<Member> members) {
        Map<String, Member> memberMap = new HashMap<>();
        
        // Add each member to the hash map with ID as key
        for (Member member : members) {
            memberMap.put(member.getMemberId(), member);
        }
        
        return memberMap;
    }
    
    /**
     * Fuzzy Search Algorithm - finds members with names containing search term.
     * 
     * Time Complexity: O(n * m) where n is number of members, m is average name length
     * Space Complexity: O(k) where k is number of matches
     * 
     * Use Case: When users don't know exact spelling or want partial matches
     * 
     * @param members List of members to search
     * @param searchTerm Partial name to search for (case-insensitive)
     * @return List of members whose names contain the search term
     */
    public static List<Member> fuzzySearchByName(List<Member> members, String searchTerm) {
        List<Member> results = new ArrayList<>();
        String lowerSearchTerm = searchTerm.toLowerCase(); // Convert to lowercase for case-insensitive search
        
        // Check each member's name for the search term
        for (Member member : members) {
            String fullName = member.getFullName().toLowerCase();
            
            // Check if the name contains the search term
            if (fullName.contains(lowerSearchTerm)) {
                results.add(member);
            }
        }
        
        System.out.println("Fuzzy Search: Found " + results.size() + " members matching '" + searchTerm + "'");
        return results;
    }
    
    /**
     * Range Search Algorithm - finds members with performance ratings in specified range.
     * 
     * Time Complexity: O(n) where n is number of members
     * Space Complexity: O(k) where k is number of matches
     * 
     * Use Case: Finding members within performance rating ranges for reports
     * 
     * @param members List of members to search
     * @param minRating Minimum performance rating (inclusive)
     * @param maxRating Maximum performance rating (inclusive)
     * @return List of members with ratings in the specified range
     */
    public static List<Member> rangeSearchByPerformance(List<Member> members, int minRating, int maxRating) {
        List<Member> results = new ArrayList<>();
        
        // Validate input range
        if (minRating > maxRating) {
            System.out.println("Range Search: Invalid range - minimum cannot be greater than maximum");
            return results;
        }
        
        // Find all members within the performance range
        for (Member member : members) {
            int rating = member.getPerformanceRating();
            
            // Check if rating falls within the specified range
            if (rating >= minRating && rating <= maxRating) {
                results.add(member);
            }
        }
        
        System.out.println("Range Search: Found " + results.size() + 
                          " members with performance rating between " + minRating + " and " + maxRating);
        return results;
    }
    
    /**
     * Multi-criteria Search Algorithm - finds members matching multiple criteria.
     * 
     * Time Complexity: O(n) where n is number of members
     * Space Complexity: O(k) where k is number of matches
     * 
     * Use Case: Complex searches with multiple filters
     * 
     * @param members List of members to search
     * @param memberType Type of membership (null to ignore)
     * @param minRating Minimum performance rating (-1 to ignore)
     * @param goalAchieved Goal achievement status (null to ignore)
     * @return List of members matching all specified criteria
     */
    public static List<Member> multiCriteriaSearch(List<Member> members, String memberType, 
                                                  int minRating, Boolean goalAchieved) {
        List<Member> results = new ArrayList<>();
        
        for (Member member : members) {
            boolean matches = true; // Assume member matches until proven otherwise
            
            // Check member type criteria (if specified)
            if (memberType != null && !member.getMemberType().contains(memberType)) {
                matches = false;
            }
            
            // Check performance rating criteria (if specified)
            if (minRating >= 0 && member.getPerformanceRating() < minRating) {
                matches = false;
            }
            
            // Check goal achievement criteria (if specified)
            if (goalAchieved != null && member.isGoalAchieved() != goalAchieved) {
                matches = false;
            }
            
            // If all criteria match, add to results
            if (matches) {
                results.add(member);
            }
        }
        
        System.out.println("Multi-Criteria Search: Found " + results.size() + " members matching all criteria");
        return results;
    }
    
    /**
     * Demonstrates search algorithm performance comparison.
     * This method is useful for testing and educational purposes.
     * 
     * @param members List of members to test algorithms on
     * @param targetId Member ID to search for
     */
    public static void compareSearchAlgorithms(List<Member> members, String targetId) {
        System.out.println("=== Search Algorithm Performance Comparison ===");
        System.out.println("Dataset size: " + members.size() + " members");
        System.out.println("Searching for member ID: " + targetId);
        System.out.println();
        
        // Test Linear Search
        long startTime = System.nanoTime();
        Member result1 = linearSearchById(members, targetId);
        long linearTime = System.nanoTime() - startTime;
        
        // Test Hash Search
        startTime = System.nanoTime();
        Member result2 = hashSearchById(members, targetId);
        long hashTime = System.nanoTime() - startTime;
        
        // Test Binary Search (requires sorted data)
        List<Member> sortedMembers = new ArrayList<>(members);
        sortedMembers.sort(Comparator.comparing(Member::getMemberId));
        
        startTime = System.nanoTime();
        Member result3 = binarySearchById(sortedMembers, targetId);
        long binaryTime = System.nanoTime() - startTime;
        
        // Display results
        System.out.println("\n=== Performance Results ===");
        System.out.println("Linear Search: " + linearTime + " nanoseconds");
        System.out.println("Hash Search: " + hashTime + " nanoseconds");
        System.out.println("Binary Search: " + binaryTime + " nanoseconds (on sorted data)");
        
        // Verify all algorithms found the same result
        boolean allMatch = (result1 == null && result2 == null && result3 == null) ||
                          (result1 != null && result2 != null && result3 != null &&
                           result1.getMemberId().equals(result2.getMemberId()) &&
                           result2.getMemberId().equals(result3.getMemberId()));
        
        System.out.println("All algorithms consistent: " + allMatch);
    }
}