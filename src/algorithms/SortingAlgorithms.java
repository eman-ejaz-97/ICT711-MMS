package algorithms;

import models.Member;
import java.util.*;

/**
 * Implementation of various sorting algorithms for the Member Management System.
 * 
 * This class demonstrates different sorting techniques and their applications
 * in organizing member data efficiently. Each algorithm has different time complexities,
 * space requirements, and stability characteristics.
 * 
 * Sorting Algorithms Implemented:
 * 1. Bubble Sort - O(n²) - Simple but inefficient for large datasets
 * 2. Selection Sort - O(n²) - Good for small datasets
 * 3. Insertion Sort - O(n²) best case O(n) - Efficient for small/nearly sorted data
 * 4. Merge Sort - O(n log n) - Stable, consistent performance
 * 5. Quick Sort - O(n log n) average, O(n²) worst - Generally fastest
 * 6. Heap Sort - O(n log n) - Consistent, in-place
 * 
 * @author ICT711 Student
 * @version 1.0
 */
public class SortingAlgorithms {
    
    /**
     * Bubble Sort Algorithm - repeatedly swaps adjacent elements if they're in wrong order.
     * 
     * Time Complexity: O(n²) worst and average case, O(n) best case (already sorted)
     * Space Complexity: O(1) - sorts in place
     * Stability: Stable (maintains relative order of equal elements)
     * 
     * Best Use Case: Educational purposes, very small datasets
     * Characteristics: Simple to understand but inefficient for large data
     * 
     * @param members List of members to sort by member ID
     * @return New sorted list (original list unchanged)
     */
    public static List<Member> bubbleSortById(List<Member> members) {
        List<Member> sortedList = new ArrayList<>(members); // Create copy to avoid modifying original
        int n = sortedList.size();
        int comparisons = 0;
        int swaps = 0;
        
        System.out.println("Starting Bubble Sort on " + n + " members...");
        
        // Bubble sort implementation
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // Optimization: detect if array is already sorted
            
            // Last i elements are already in place
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                
                // Compare adjacent elements and swap if they're in wrong order
                if (sortedList.get(j).getMemberId().compareTo(sortedList.get(j + 1).getMemberId()) > 0) {
                    // Swap elements
                    Member temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                    swapped = true;
                    swaps++;
                }
            }
            
            // If no swapping occurred, array is sorted
            if (!swapped) {
                break;
            }
        }
        
        System.out.println("Bubble Sort completed: " + comparisons + " comparisons, " + swaps + " swaps");
        return sortedList;
    }
    
    /**
     * Selection Sort Algorithm - finds minimum element and places it at beginning.
     * 
     * Time Complexity: O(n²) for all cases
     * Space Complexity: O(1) - sorts in place
     * Stability: Not stable (may change relative order of equal elements)
     * 
     * Best Use Case: Small datasets where memory is limited
     * Characteristics: Simple, minimal swaps, not adaptive
     * 
     * @param members List of members to sort by performance rating (descending)
     * @return New sorted list (original list unchanged)
     */
    public static List<Member> selectionSortByPerformance(List<Member> members) {
        List<Member> sortedList = new ArrayList<>(members);
        int n = sortedList.size();
        int comparisons = 0;
        int swaps = 0;
        
        System.out.println("Starting Selection Sort on " + n + " members by performance...");
        
        // Selection sort implementation (descending order for performance)
        for (int i = 0; i < n - 1; i++) {
            int maxIndex = i; // Find index of maximum element in remaining array
            
            // Find the maximum element in remaining unsorted array
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                if (sortedList.get(j).getPerformanceRating() > sortedList.get(maxIndex).getPerformanceRating()) {
                    maxIndex = j;
                }
            }
            
            // Swap the found maximum element with the first element
            if (maxIndex != i) {
                Member temp = sortedList.get(i);
                sortedList.set(i, sortedList.get(maxIndex));
                sortedList.set(maxIndex, temp);
                swaps++;
            }
        }
        
        System.out.println("Selection Sort completed: " + comparisons + " comparisons, " + swaps + " swaps");
        return sortedList;
    }
    
    /**
     * Insertion Sort Algorithm - builds final sorted array one element at a time.
     * 
     * Time Complexity: O(n²) worst case, O(n) best case (nearly sorted)
     * Space Complexity: O(1) - sorts in place
     * Stability: Stable
     * 
     * Best Use Case: Small datasets, nearly sorted data, online algorithm
     * Characteristics: Adaptive, simple, efficient for small data
     * 
     * @param members List of members to sort by full name
     * @return New sorted list (original list unchanged)
     */
    public static List<Member> insertionSortByName(List<Member> members) {
        List<Member> sortedList = new ArrayList<>(members);
        int n = sortedList.size();
        int comparisons = 0;
        int shifts = 0;
        
        System.out.println("Starting Insertion Sort on " + n + " members by name...");
        
        // Insertion sort implementation
        for (int i = 1; i < n; i++) {
            Member key = sortedList.get(i); // Element to be positioned
            int j = i - 1;
            
            // Move elements greater than key one position ahead
            while (j >= 0) {
                comparisons++;
                if (sortedList.get(j).getFullName().compareTo(key.getFullName()) > 0) {
                    sortedList.set(j + 1, sortedList.get(j));
                    j--;
                    shifts++;
                } else {
                    break;
                }
            }
            
            // Place key at its correct position
            sortedList.set(j + 1, key);
        }
        
        System.out.println("Insertion Sort completed: " + comparisons + " comparisons, " + shifts + " shifts");
        return sortedList;
    }
    
    /**
     * Merge Sort Algorithm - divide and conquer approach, splits array and merges sorted halves.
     * 
     * Time Complexity: O(n log n) for all cases
     * Space Complexity: O(n) - requires additional space for merging
     * Stability: Stable
     * 
     * Best Use Case: Large datasets, when stability is required, guaranteed performance
     * Characteristics: Consistent performance, stable, requires extra memory
     * 
     * @param members List of members to sort by monthly fee
     * @return New sorted list (original list unchanged)
     */
    public static List<Member> mergeSortByFee(List<Member> members) {
        System.out.println("Starting Merge Sort on " + members.size() + " members by monthly fee...");
        List<Member> sortedList = new ArrayList<>(members);
        mergeSortRecursive(sortedList, 0, sortedList.size() - 1);
        System.out.println("Merge Sort completed");
        return sortedList;
    }
    
    /**
     * Recursive helper method for merge sort.
     * 
     * @param list List to sort
     * @param left Left boundary of current subarray
     * @param right Right boundary of current subarray
     */
    private static void mergeSortRecursive(List<Member> list, int left, int right) {
        if (left < right) {
            // Find the middle point to divide array into two halves
            int middle = left + (right - left) / 2;
            
            // Recursively sort both halves
            mergeSortRecursive(list, left, middle);
            mergeSortRecursive(list, middle + 1, right);
            
            // Merge the sorted halves
            merge(list, left, middle, right);
        }
    }
    
    /**
     * Merges two sorted subarrays into one sorted array.
     * 
     * @param list Main list containing subarrays
     * @param left Start index of first subarray
     * @param middle End index of first subarray
     * @param right End index of second subarray
     */
    private static void merge(List<Member> list, int left, int middle, int right) {
        // Create temporary arrays for the two subarrays
        List<Member> leftArray = new ArrayList<>();
        List<Member> rightArray = new ArrayList<>();
        
        // Copy data to temporary arrays
        for (int i = left; i <= middle; i++) {
            leftArray.add(list.get(i));
        }
        for (int j = middle + 1; j <= right; j++) {
            rightArray.add(list.get(j));
        }
        
        // Merge the temporary arrays back into list[left..right]
        int i = 0, j = 0, k = left;
        
        while (i < leftArray.size() && j < rightArray.size()) {
            // Compare monthly fees (ascending order)
            if (leftArray.get(i).calculateMonthlyFee() <= rightArray.get(j).calculateMonthlyFee()) {
                list.set(k, leftArray.get(i));
                i++;
            } else {
                list.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }
        
        // Copy remaining elements
        while (i < leftArray.size()) {
            list.set(k, leftArray.get(i));
            i++;
            k++;
        }
        while (j < rightArray.size()) {
            list.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }
    
    /**
     * Quick Sort Algorithm - divide and conquer using a pivot element.
     * 
     * Time Complexity: O(n log n) average case, O(n²) worst case
     * Space Complexity: O(log n) average case due to recursion
     * Stability: Not stable
     * 
     * Best Use Case: Large datasets, when average case performance is important
     * Characteristics: Generally fastest sorting algorithm, in-place, not stable
     * 
     * @param members List of members to sort by member type
     * @return New sorted list (original list unchanged)
     */
    public static List<Member> quickSortByType(List<Member> members) {
        System.out.println("Starting Quick Sort on " + members.size() + " members by type...");
        List<Member> sortedList = new ArrayList<>(members);
        quickSortRecursive(sortedList, 0, sortedList.size() - 1);
        System.out.println("Quick Sort completed");
        return sortedList;
    }
    
    /**
     * Recursive helper method for quick sort.
     * 
     * @param list List to sort
     * @param low Starting index
     * @param high Ending index
     */
    private static void quickSortRecursive(List<Member> list, int low, int high) {
        if (low < high) {
            // Partition the array and get pivot index
            int pivotIndex = partition(list, low, high);
            
            // Recursively sort elements before and after partition
            quickSortRecursive(list, low, pivotIndex - 1);
            quickSortRecursive(list, pivotIndex + 1, high);
        }
    }
    
    /**
     * Partitions the array around a pivot element.
     * 
     * @param list List to partition
     * @param low Starting index
     * @param high Ending index
     * @return Index of pivot after partitioning
     */
    private static int partition(List<Member> list, int low, int high) {
        // Choose rightmost element as pivot
        Member pivot = list.get(high);
        int i = low - 1; // Index of smaller element
        
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (list.get(j).getMemberType().compareTo(pivot.getMemberType()) <= 0) {
                i++;
                // Swap elements
                Member temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        
        // Swap pivot with element at i+1
        Member temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        
        return i + 1;
    }
    
    /**
     * Heap Sort Algorithm - builds a max heap and repeatedly extracts maximum.
     * 
     * Time Complexity: O(n log n) for all cases
     * Space Complexity: O(1) - sorts in place
     * Stability: Not stable
     * 
     * Best Use Case: When consistent O(n log n) performance is needed with O(1) space
     * Characteristics: Consistent performance, in-place, not stable
     * 
     * @param members List of members to sort by join date (newest first)
     * @return New sorted list (original list unchanged)
     */
    public static List<Member> heapSortByJoinDate(List<Member> members) {
        System.out.println("Starting Heap Sort on " + members.size() + " members by join date...");
        List<Member> sortedList = new ArrayList<>(members);
        int n = sortedList.size();
        
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(sortedList, n, i);
        }
        
        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            Member temp = sortedList.get(0);
            sortedList.set(0, sortedList.get(i));
            sortedList.set(i, temp);
            
            // Call heapify on reduced heap
            heapify(sortedList, i, 0);
        }
        
        System.out.println("Heap Sort completed");
        return sortedList;
    }
    
    /**
     * Maintains heap property for a subtree rooted at given index.
     * 
     * @param list List representing the heap
     * @param heapSize Size of heap
     * @param rootIndex Index of root of subtree
     */
    private static void heapify(List<Member> list, int heapSize, int rootIndex) {
        int largest = rootIndex; // Initialize largest as root
        int leftChild = 2 * rootIndex + 1;
        int rightChild = 2 * rootIndex + 2;
        
        // If left child exists and is greater than root
        if (leftChild < heapSize && 
            list.get(leftChild).getJoinDate().isAfter(list.get(largest).getJoinDate())) {
            largest = leftChild;
        }
        
        // If right child exists and is greater than largest so far
        if (rightChild < heapSize && 
            list.get(rightChild).getJoinDate().isAfter(list.get(largest).getJoinDate())) {
            largest = rightChild;
        }
        
        // If largest is not root
        if (largest != rootIndex) {
            Member temp = list.get(rootIndex);
            list.set(rootIndex, list.get(largest));
            list.set(largest, temp);
            
            // Recursively heapify the affected subtree
            heapify(list, heapSize, largest);
        }
    }
    
    /**
     * Custom comparison-based sort using Java's built-in TimSort (hybrid merge-insertion sort).
     * This demonstrates how to use Java's optimized sorting with custom comparators.
     * 
     * Time Complexity: O(n log n) worst case, O(n) best case
     * Space Complexity: O(n)
     * Stability: Stable
     * 
     * @param members List of members to sort
     * @param sortBy Criteria to sort by ("id", "name", "performance", "fee", "type")
     * @param ascending Whether to sort in ascending order
     * @return New sorted list
     */
    public static List<Member> customSort(List<Member> members, String sortBy, boolean ascending) {
        List<Member> sortedList = new ArrayList<>(members);
        Comparator<Member> comparator;
        
        // Choose comparator based on sort criteria
        switch (sortBy.toLowerCase()) {
            case "id":
                comparator = Comparator.comparing(Member::getMemberId);
                break;
            case "name":
                comparator = Comparator.comparing(Member::getFullName);
                break;
            case "performance":
                comparator = Comparator.comparing(Member::getPerformanceRating);
                break;
            case "fee":
                comparator = Comparator.comparing(Member::calculateMonthlyFee);
                break;
            case "type":
                comparator = Comparator.comparing(Member::getMemberType);
                break;
            default:
                throw new IllegalArgumentException("Invalid sort criteria: " + sortBy);
        }
        
        // Reverse comparator if descending order is requested
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        // Use Java's optimized sorting algorithm
        sortedList.sort(comparator);
        
        System.out.println("Custom sort completed: " + members.size() + " members sorted by " + 
                          sortBy + " (" + (ascending ? "ascending" : "descending") + ")");
        
        return sortedList;
    }
    
    /**
     * Demonstrates and compares performance of different sorting algorithms.
     * This method is useful for testing and educational purposes.
     * 
     * @param members List of members to test algorithms on
     */
    public static void compareSortingAlgorithms(List<Member> members) {
        System.out.println("=== Sorting Algorithm Performance Comparison ===");
        System.out.println("Dataset size: " + members.size() + " members");
        System.out.println();
        
        // Test each sorting algorithm and measure time
        long startTime, endTime;
        
        // Bubble Sort (only for small datasets due to O(n²) complexity)
        if (members.size() <= 1000) {
            startTime = System.nanoTime();
            bubbleSortById(members);
            endTime = System.nanoTime();
            System.out.println("Bubble Sort: " + (endTime - startTime) + " nanoseconds");
        } else {
            System.out.println("Bubble Sort: Skipped (dataset too large)");
        }
        
        // Selection Sort
        if (members.size() <= 1000) {
            startTime = System.nanoTime();
            selectionSortByPerformance(members);
            endTime = System.nanoTime();
            System.out.println("Selection Sort: " + (endTime - startTime) + " nanoseconds");
        } else {
            System.out.println("Selection Sort: Skipped (dataset too large)");
        }
        
        // Insertion Sort
        startTime = System.nanoTime();
        insertionSortByName(members);
        endTime = System.nanoTime();
        System.out.println("Insertion Sort: " + (endTime - startTime) + " nanoseconds");
        
        // Merge Sort
        startTime = System.nanoTime();
        mergeSortByFee(members);
        endTime = System.nanoTime();
        System.out.println("Merge Sort: " + (endTime - startTime) + " nanoseconds");
        
        // Quick Sort
        startTime = System.nanoTime();
        quickSortByType(members);
        endTime = System.nanoTime();
        System.out.println("Quick Sort: " + (endTime - startTime) + " nanoseconds");
        
        // Heap Sort
        startTime = System.nanoTime();
        heapSortByJoinDate(members);
        endTime = System.nanoTime();
        System.out.println("Heap Sort: " + (endTime - startTime) + " nanoseconds");
        
        // Java's built-in sort (TimSort)
        startTime = System.nanoTime();
        customSort(members, "id", true);
        endTime = System.nanoTime();
        System.out.println("Java TimSort: " + (endTime - startTime) + " nanoseconds");
        
        System.out.println("\n=== Algorithm Complexity Summary ===");
        System.out.println("Bubble Sort: O(n²) time, O(1) space");
        System.out.println("Selection Sort: O(n²) time, O(1) space");
        System.out.println("Insertion Sort: O(n²) time, O(1) space");
        System.out.println("Merge Sort: O(n log n) time, O(n) space");
        System.out.println("Quick Sort: O(n log n) avg time, O(log n) space");
        System.out.println("Heap Sort: O(n log n) time, O(1) space");
        System.out.println("Java TimSort: O(n log n) time, O(n) space");
    }
}