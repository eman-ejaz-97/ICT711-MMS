package algorithms;

import manager.MemberManager;
import models.Member;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced search algorithms for member data.
 * 
 * Implements multiple search algorithms:
 * - Linear Search for basic text matching
 * - Binary Search for sorted data (when applicable)
 * - Hash-based search for exact matches
 * - Pattern matching for flexible searches
 * 
 * Provides search capabilities across:
 * - All member fields
 * - Specific field filtering
 * - Performance-based criteria
 * - Complex multi-field searches
 * 
 * Algorithm Complexity Analysis:
 * - Linear Search: O(n) time, O(1) space
 * - Binary Search: O(log n) time, O(1) space (requires sorted data)
 * - Hash Search: O(1) average time, O(n) space
 * - Pattern Search: O(n*m) time where m is pattern length
 */
public class MemberSearcher {
    private MemberManager manager;
    private Map<String, Member> idIndex;      // Hash index for ID searches
    private Map<String, List<Member>> nameIndex;  // Hash index for name searches
    private boolean indexesBuilt;
    
    /**
     * Constructs a MemberSearcher with the specified manager.
     * 
     * @param manager the member manager to search within
     */
    public MemberSearcher(MemberManager manager) {
        this.manager = manager;
        this.indexesBuilt = false;
        buildIndexes();
    }
    
    /**
     * Builds hash indexes for optimized searching.
     * Called automatically when needed or when data changes.
     * 
     * Time Complexity: O(n) where n is number of members
     * Space Complexity: O(n) for storing indexes
     */
    public void buildIndexes() {
        idIndex = new HashMap<>();
        nameIndex = new HashMap<>();
        
        List<Member> allMembers = manager.getAllMembers();
        
        for (Member member : allMembers) {
            // Build ID index for O(1) ID lookups
            idIndex.put(member.getMemberId().toLowerCase(), member);
            
            // Build name index for faster name searches
            String[] nameParts = member.getFullName().toLowerCase().split("\\s+");
            for (String part : nameParts) {
                nameIndex.computeIfAbsent(part, k -> new ArrayList<>()).add(member);
            }
        }
        
        indexesBuilt = true;
    }
    
    /**
     * Main search method that routes to appropriate search algorithm.
     * 
     * @param searchText the text to search for
     * @param searchType the type of search ("All Fields", "Name", "ID", "Email", "Type")
     * @return list of members matching the search criteria
     */
    public List<Member> search(String searchText, String searchType) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return manager.getAllMembers();
        }
        
        if (!indexesBuilt) {
            buildIndexes();
        }
        
        String normalizedText = searchText.trim().toLowerCase();
        
        switch (searchType) {
            case "ID":
                return searchById(normalizedText);
            case "Name":
                return searchByName(normalizedText);
            case "Email":
                return searchByEmail(normalizedText);
            case "Type":
                return searchByType(normalizedText);
            case "All Fields":
            default:
                return searchAllFields(normalizedText);
        }
    }
    
    /**
     * Searches members by ID using hash-based lookup.
     * 
     * Algorithm: Hash Search
     * Time Complexity: O(1) average case, O(n) worst case
     * Space Complexity: O(1)
     * 
     * @param searchText the ID to search for
     * @return list containing the member with matching ID, or empty list
     */
    public List<Member> searchById(String searchText) {
        List<Member> results = new ArrayList<>();
        
        // First try exact match using hash index
        Member exactMatch = idIndex.get(searchText);
        if (exactMatch != null) {
            results.add(exactMatch);
            return results;
        }
        
        // If no exact match, fall back to partial matching using linear search
        return linearSearchById(searchText);
    }
    
    /**
     * Searches members by name using optimized index lookup combined with pattern matching.
     * 
     * Algorithm: Hash Index + Linear Search
     * Time Complexity: O(k) where k is number of members with matching name parts
     * Space Complexity: O(k)
     * 
     * @param searchText the name to search for
     * @return list of members with names containing the search text
     */
    public List<Member> searchByName(String searchText) {
        Set<Member> results = new HashSet<>();
        
        // Search using name index for efficiency
        String[] searchParts = searchText.split("\\s+");
        
        for (String part : searchParts) {
            // Exact word matches from index
            List<Member> exactMatches = nameIndex.get(part);
            if (exactMatches != null) {
                results.addAll(exactMatches);
            }
            
            // Partial matches within indexed words
            for (Map.Entry<String, List<Member>> entry : nameIndex.entrySet()) {
                if (entry.getKey().contains(part)) {
                    results.addAll(entry.getValue());
                }
            }
        }
        
        // If no results from index, fall back to linear search for partial matching
        if (results.isEmpty()) {
            return linearSearchByName(searchText);
        }
        
        return new ArrayList<>(results);
    }
    
    /**
     * Searches members by email using linear search with pattern matching.
     * 
     * Algorithm: Linear Search with String Matching
     * Time Complexity: O(n*m) where n is number of members, m is search text length
     * Space Complexity: O(k) where k is number of matches
     * 
     * @param searchText the email pattern to search for
     * @return list of members with emails containing the search text
     */
    public List<Member> searchByEmail(String searchText) {
        return manager.getAllMembers().stream()
                .filter(member -> member.getEmail().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
    
    /**
     * Searches members by type using linear search.
     * 
     * Algorithm: Linear Search
     * Time Complexity: O(n)
     * Space Complexity: O(k) where k is number of matches
     * 
     * @param searchText the member type to search for
     * @return list of members of the specified type
     */
    public List<Member> searchByType(String searchText) {
        return manager.getAllMembers().stream()
                .filter(member -> member.getMemberType().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
    
    /**
     * Searches all fields using multiple search strategies.
     * 
     * Algorithm: Multiple Search Algorithms Combined
     * Time Complexity: O(n*m) in worst case
     * Space Complexity: O(k) where k is number of matches
     * 
     * @param searchText the text to search for across all fields
     * @return list of members matching the search text in any field
     */
    public List<Member> searchAllFields(String searchText) {
        Set<Member> results = new HashSet<>();
        
        // Search each field type and combine results
        results.addAll(searchById(searchText));
        results.addAll(searchByName(searchText));
        results.addAll(searchByEmail(searchText));
        results.addAll(searchByType(searchText));
        
        // Additional searches for numeric fields
        try {
            int numericSearch = Integer.parseInt(searchText);
            results.addAll(searchByPerformanceRating(numericSearch));
        } catch (NumberFormatException e) {
            // Not a number, skip numeric searches
        }
        
        return new ArrayList<>(results);
    }
    
    /**
     * Searches members by performance rating.
     * 
     * Algorithm: Linear Search with Numeric Comparison
     * Time Complexity: O(n)
     * Space Complexity: O(k) where k is number of matches
     * 
     * @param rating the exact performance rating to search for
     * @return list of members with the specified performance rating
     */
    public List<Member> searchByPerformanceRating(int rating) {
        return manager.getAllMembers().stream()
                .filter(member -> member.getPerformanceRating() == rating)
                .collect(Collectors.toList());
    }
    
    /**
     * Searches members by performance rating range.
     * 
     * Algorithm: Linear Search with Range Check
     * Time Complexity: O(n)
     * Space Complexity: O(k) where k is number of matches
     * 
     * @param minRating minimum performance rating (inclusive)
     * @param maxRating maximum performance rating (inclusive)
     * @return list of members within the performance rating range
     */
    public List<Member> searchByPerformanceRange(int minRating, int maxRating) {
        return manager.getAllMembers().stream()
                .filter(member -> member.getPerformanceRating() >= minRating && 
                                member.getPerformanceRating() <= maxRating)
                .collect(Collectors.toList());
    }
    
    /**
     * Advanced search with multiple criteria.
     * 
     * @param criteria map of search criteria (field -> value)
     * @return list of members matching all specified criteria
     */
    public List<Member> advancedSearch(Map<String, String> criteria) {
        return manager.getAllMembers().stream()
                .filter(member -> matchesCriteria(member, criteria))
                .collect(Collectors.toList());
    }
    
    /**
     * Checks if a member matches all specified criteria.
     * 
     * @param member the member to check
     * @param criteria the criteria to match against
     * @return true if member matches all criteria
     */
    private boolean matchesCriteria(Member member, Map<String, String> criteria) {
        for (Map.Entry<String, String> criterion : criteria.entrySet()) {
            String field = criterion.getKey().toLowerCase();
            String value = criterion.getValue().toLowerCase();
            
            boolean matches = false;
            switch (field) {
                case "id":
                    matches = member.getMemberId().toLowerCase().contains(value);
                    break;
                case "name":
                    matches = member.getFullName().toLowerCase().contains(value);
                    break;
                case "email":
                    matches = member.getEmail().toLowerCase().contains(value);
                    break;
                case "type":
                    matches = member.getMemberType().toLowerCase().contains(value);
                    break;
                case "goal":
                    boolean goalValue = Boolean.parseBoolean(value);
                    matches = member.isGoalAchieved() == goalValue;
                    break;
                default:
                    matches = true; // Unknown field, don't filter
            }
            
            if (!matches) {
                return false;
            }
        }
        return true;
    }
    
    // Fallback linear search methods for when indexes don't provide results
    
    /**
     * Linear search by ID for partial matching.
     * Used as fallback when hash lookup fails.
     */
    private List<Member> linearSearchById(String searchText) {
        return manager.getAllMembers().stream()
                .filter(member -> member.getMemberId().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
    
    /**
     * Linear search by name for partial matching.
     * Used as fallback when index lookup fails.
     */
    private List<Member> linearSearchByName(String searchText) {
        return manager.getAllMembers().stream()
                .filter(member -> member.getFullName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
    
    /**
     * Binary search implementation for sorted member lists.
     * Requires the list to be pre-sorted by the search key.
     * 
     * Algorithm: Binary Search
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     * 
     * @param sortedMembers list of members sorted by ID
     * @param targetId the ID to search for
     * @return the member with matching ID, or null if not found
     */
    public Member binarySearchById(List<Member> sortedMembers, String targetId) {
        int left = 0;
        int right = sortedMembers.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Member midMember = sortedMembers.get(mid);
            int comparison = midMember.getMemberId().compareToIgnoreCase(targetId);
            
            if (comparison == 0) {
                return midMember; // Found exact match
            } else if (comparison < 0) {
                left = mid + 1; // Search right half
            } else {
                right = mid - 1; // Search left half
            }
        }
        
        return null; // Not found
    }
    
    /**
     * Gets search performance statistics.
     * 
     * @return map containing search performance metrics
     */
    public Map<String, Object> getSearchStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMembers", manager.getAllMembers().size());
        stats.put("indexSize", idIndex.size());
        stats.put("nameIndexEntries", nameIndex.size());
        stats.put("indexesBuilt", indexesBuilt);
        return stats;
    }
    
    /**
     * Invalidates search indexes.
     * Should be called when member data is modified.
     */
    public void invalidateIndexes() {
        indexesBuilt = false;
        if (idIndex != null) idIndex.clear();
        if (nameIndex != null) nameIndex.clear();
    }
}