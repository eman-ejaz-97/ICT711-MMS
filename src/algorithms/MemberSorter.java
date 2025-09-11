package algorithms;

import models.Member;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced sorting algorithms for member data.
 * 
 * Implements multiple sorting algorithms:
 * - Quick Sort for general-purpose sorting
 * - Merge Sort for stable sorting
 * - Heap Sort for guaranteed O(n log n) performance
 * - Insertion Sort for small datasets
 * - Bubble Sort for educational purposes
 * - Counting Sort for integer fields (performance ratings)
 * 
 * Supports sorting by:
 * - Name (alphabetical)
 * - ID (alphanumeric)
 * - Member Type
 * - Performance Rating (numeric)
 * - Monthly Fee (numeric)
 * - Join Date
 * - Goal Achievement Status
 * 
 * Algorithm Complexity Analysis:
 * - Quick Sort: O(n log n) average, O(n²) worst case
 * - Merge Sort: O(n log n) guaranteed, stable
 * - Heap Sort: O(n log n) guaranteed, not stable
 * - Insertion Sort: O(n²) worst case, O(n) best case
 * - Counting Sort: O(n + k) where k is range of values
 */
public class MemberSorter {
    
    /**
     * Enumeration of available sorting algorithms.
     */
    public enum SortAlgorithm {
        QUICK_SORT,
        MERGE_SORT,
        HEAP_SORT,
        INSERTION_SORT,
        BUBBLE_SORT,
        COUNTING_SORT
    }
    
    /**
     * Enumeration of sort orders.
     */
    public enum SortOrder {
        ASCENDING,
        DESCENDING
    }
    
    private long lastSortTime; // For performance measurement
    private String lastAlgorithmUsed;
    private int lastDataSize;
    
    /**
     * Main sort method that chooses optimal algorithm based on data characteristics.
     * 
     * @param members list of members to sort
     * @param sortBy field to sort by
     * @return new sorted list of members
     */
    public List<Member> sort(List<Member> members, String sortBy) {
        return sort(members, sortBy, SortOrder.ASCENDING, chooseOptimalAlgorithm(members.size()));
    }
    
    /**
     * Sort with specified order.
     * 
     * @param members list of members to sort
     * @param sortBy field to sort by
     * @param order sort order (ascending/descending)
     * @return new sorted list of members
     */
    public List<Member> sort(List<Member> members, String sortBy, SortOrder order) {
        return sort(members, sortBy, order, chooseOptimalAlgorithm(members.size()));
    }
    
    /**
     * Sort with specified algorithm.
     * 
     * @param members list of members to sort
     * @param sortBy field to sort by
     * @param order sort order
     * @param algorithm sorting algorithm to use
     * @return new sorted list of members
     */
    public List<Member> sort(List<Member> members, String sortBy, SortOrder order, SortAlgorithm algorithm) {
        if (members == null || members.isEmpty()) {
            return new ArrayList<>();
        }
        
        long startTime = System.nanoTime();
        
        List<Member> result = new ArrayList<>(members); // Create copy to avoid modifying original
        Comparator<Member> comparator = getComparator(sortBy, order);
        
        switch (algorithm) {
            case QUICK_SORT:
                quickSort(result, comparator);
                lastAlgorithmUsed = "Quick Sort";
                break;
            case MERGE_SORT:
                result = mergeSort(result, comparator);
                lastAlgorithmUsed = "Merge Sort";
                break;
            case HEAP_SORT:
                heapSort(result, comparator);
                lastAlgorithmUsed = "Heap Sort";
                break;
            case INSERTION_SORT:
                insertionSort(result, comparator);
                lastAlgorithmUsed = "Insertion Sort";
                break;
            case BUBBLE_SORT:
                bubbleSort(result, comparator);
                lastAlgorithmUsed = "Bubble Sort";
                break;
            case COUNTING_SORT:
                if ("Performance".equals(sortBy)) {
                    result = countingSortByPerformance(result, order);
                    lastAlgorithmUsed = "Counting Sort";
                } else {
                    // Fall back to quick sort for non-integer fields
                    quickSort(result, comparator);
                    lastAlgorithmUsed = "Quick Sort (Counting Sort not applicable)";
                }
                break;
            default:
                quickSort(result, comparator);
                lastAlgorithmUsed = "Quick Sort (default)";
        }
        
        lastSortTime = System.nanoTime() - startTime;
        lastDataSize = members.size();
        
        return result;
    }
    
    /**
     * Chooses the optimal sorting algorithm based on data size.
     * 
     * @param dataSize number of elements to sort
     * @return recommended sorting algorithm
     */
    private SortAlgorithm chooseOptimalAlgorithm(int dataSize) {
        if (dataSize <= 10) {
            return SortAlgorithm.INSERTION_SORT; // Best for small datasets
        } else if (dataSize <= 1000) {
            return SortAlgorithm.QUICK_SORT; // Good general purpose
        } else {
            return SortAlgorithm.MERGE_SORT; // Stable O(n log n) for large datasets
        }
    }
    
    /**
     * Gets appropriate comparator for the specified field and order.
     * 
     * @param sortBy field to sort by
     * @param order sort order
     * @return comparator for the specified criteria
     */
    private Comparator<Member> getComparator(String sortBy, SortOrder order) {
        Comparator<Member> comparator;
        
        switch (sortBy) {
            case "Name":
                comparator = Comparator.comparing(Member::getFullName, String.CASE_INSENSITIVE_ORDER);
                break;
            case "ID":
                comparator = Comparator.comparing(Member::getMemberId, String.CASE_INSENSITIVE_ORDER);
                break;
            case "Type":
                comparator = Comparator.comparing(Member::getMemberType);
                break;
            case "Performance":
                comparator = Comparator.comparing(Member::getPerformanceRating);
                break;
            case "Monthly Fee":
                comparator = Comparator.comparing(Member::calculateMonthlyFee);
                break;
            case "Goal":
                comparator = Comparator.comparing(Member::isGoalAchieved);
                break;
            default:
                comparator = Comparator.comparing(Member::getFullName, String.CASE_INSENSITIVE_ORDER);
        }
        
        return order == SortOrder.DESCENDING ? comparator.reversed() : comparator;
    }
    
    // Sorting Algorithm Implementations
    
    /**
     * Quick Sort implementation.
     * 
     * Algorithm: Quick Sort (in-place)
     * Time Complexity: O(n log n) average, O(n²) worst case
     * Space Complexity: O(log n) due to recursion
     * 
     * @param list list to sort
     * @param comparator comparison function
     */
    public void quickSort(List<Member> list, Comparator<Member> comparator) {
        if (list.size() <= 1) return;
        quickSortHelper(list, 0, list.size() - 1, comparator);
    }
    
    private void quickSortHelper(List<Member> list, int low, int high, Comparator<Member> comparator) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, comparator);
            quickSortHelper(list, low, pivotIndex - 1, comparator);
            quickSortHelper(list, pivotIndex + 1, high, comparator);
        }
    }
    
    private int partition(List<Member> list, int low, int high, Comparator<Member> comparator) {
        Member pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
    
    /**
     * Merge Sort implementation.
     * 
     * Algorithm: Merge Sort (stable)
     * Time Complexity: O(n log n) guaranteed
     * Space Complexity: O(n)
     * 
     * @param list list to sort
     * @param comparator comparison function
     * @return new sorted list
     */
    public List<Member> mergeSort(List<Member> list, Comparator<Member> comparator) {
        if (list.size() <= 1) return new ArrayList<>(list);
        
        int mid = list.size() / 2;
        List<Member> left = mergeSort(list.subList(0, mid), comparator);
        List<Member> right = mergeSort(list.subList(mid, list.size()), comparator);
        
        return merge(left, right, comparator);
    }
    
    private List<Member> merge(List<Member> left, List<Member> right, Comparator<Member> comparator) {
        List<Member> result = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        
        // Add remaining elements
        result.addAll(left.subList(i, left.size()));
        result.addAll(right.subList(j, right.size()));
        
        return result;
    }
    
    /**
     * Heap Sort implementation.
     * 
     * Algorithm: Heap Sort (in-place)
     * Time Complexity: O(n log n) guaranteed
     * Space Complexity: O(1)
     * 
     * @param list list to sort
     * @param comparator comparison function
     */
    public void heapSort(List<Member> list, Comparator<Member> comparator) {
        int n = list.size();
        
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, n, i, comparator);
        }
        
        // Extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(list, 0, i);
            heapify(list, i, 0, comparator);
        }
    }
    
    private void heapify(List<Member> list, int n, int i, Comparator<Member> comparator) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && comparator.compare(list.get(left), list.get(largest)) > 0) {
            largest = left;
        }
        
        if (right < n && comparator.compare(list.get(right), list.get(largest)) > 0) {
            largest = right;
        }
        
        if (largest != i) {
            Collections.swap(list, i, largest);
            heapify(list, n, largest, comparator);
        }
    }
    
    /**
     * Insertion Sort implementation.
     * 
     * Algorithm: Insertion Sort (stable)
     * Time Complexity: O(n²) worst case, O(n) best case
     * Space Complexity: O(1)
     * 
     * Best for small datasets or nearly sorted data.
     * 
     * @param list list to sort
     * @param comparator comparison function
     */
    public void insertionSort(List<Member> list, Comparator<Member> comparator) {
        for (int i = 1; i < list.size(); i++) {
            Member key = list.get(i);
            int j = i - 1;
            
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
    
    /**
     * Bubble Sort implementation.
     * 
     * Algorithm: Bubble Sort (stable)
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     * 
     * Included for educational purposes. Not recommended for production use.
     * 
     * @param list list to sort
     * @param comparator comparison function
     */
    public void bubbleSort(List<Member> list, Comparator<Member> comparator) {
        int n = list.size();
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            // Optimization: if no swapping occurred, list is sorted
            if (!swapped) break;
        }
    }
    
    /**
     * Counting Sort implementation for performance ratings.
     * 
     * Algorithm: Counting Sort
     * Time Complexity: O(n + k) where k is range of values (0-10 for performance)
     * Space Complexity: O(k)
     * 
     * Only applicable to integer fields with known range.
     * 
     * @param list list to sort
     * @param order sort order
     * @return new sorted list
     */
    public List<Member> countingSortByPerformance(List<Member> list, SortOrder order) {
        if (list.isEmpty()) return new ArrayList<>();
        
        // Performance ratings are 0-10, so we need array of size 11
        final int RANGE = 11;
        List<List<Member>> buckets = new ArrayList<>();
        for (int i = 0; i < RANGE; i++) {
            buckets.add(new ArrayList<>());
        }
        
        // Distribute members into buckets based on performance rating
        for (Member member : list) {
            int rating = member.getPerformanceRating();
            if (rating >= 0 && rating < RANGE) {
                buckets.get(rating).add(member);
            }
        }
        
        // Collect members from buckets in order
        List<Member> result = new ArrayList<>();
        if (order == SortOrder.ASCENDING) {
            for (int i = 0; i < RANGE; i++) {
                result.addAll(buckets.get(i));
            }
        } else {
            for (int i = RANGE - 1; i >= 0; i--) {
                result.addAll(buckets.get(i));
            }
        }
        
        return result;
    }
    
    /**
     * Radix Sort implementation for numeric fields.
     * 
     * Algorithm: Radix Sort (LSD)
     * Time Complexity: O(d * (n + k)) where d is number of digits
     * Space Complexity: O(n + k)
     * 
     * @param list list to sort by numeric field
     * @param valueExtractor function to extract numeric value from member
     * @param order sort order
     * @return new sorted list
     */
    public List<Member> radixSort(List<Member> list, java.util.function.ToIntFunction<Member> valueExtractor, SortOrder order) {
        if (list.isEmpty()) return new ArrayList<>();
        
        List<Member> result = new ArrayList<>(list);
        
        // Find maximum value to determine number of digits
        int max = list.stream().mapToInt(valueExtractor).max().orElse(0);
        
        // Sort by each digit position
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(result, valueExtractor, exp);
        }
        
        if (order == SortOrder.DESCENDING) {
            Collections.reverse(result);
        }
        
        return result;
    }
    
    private void countingSortByDigit(List<Member> list, java.util.function.ToIntFunction<Member> valueExtractor, int exp) {
        List<Member> output = new ArrayList<>(Collections.nCopies(list.size(), null));
        int[] count = new int[10];
        
        // Count occurrences of each digit
        for (Member member : list) {
            int digit = (valueExtractor.applyAsInt(member) / exp) % 10;
            count[digit]++;
        }
        
        // Change count[i] to actual position
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        // Build output array
        for (int i = list.size() - 1; i >= 0; i--) {
            Member member = list.get(i);
            int digit = (valueExtractor.applyAsInt(member) / exp) % 10;
            output.set(count[digit] - 1, member);
            count[digit]--;
        }
        
        // Copy output to original list
        for (int i = 0; i < list.size(); i++) {
            list.set(i, output.get(i));
        }
    }
    
    /**
     * Multi-field sort with priority ordering.
     * 
     * @param members list to sort
     * @param sortFields list of fields to sort by (in priority order)
     * @param orders list of sort orders for each field
     * @return new sorted list
     */
    public List<Member> multiFieldSort(List<Member> members, List<String> sortFields, List<SortOrder> orders) {
        if (sortFields.isEmpty()) return new ArrayList<>(members);
        
        List<Member> result = new ArrayList<>(members);
        
        // Create composite comparator
        Comparator<Member> comparator = null;
        
        for (int i = 0; i < sortFields.size(); i++) {
            String field = sortFields.get(i);
            SortOrder order = i < orders.size() ? orders.get(i) : SortOrder.ASCENDING;
            Comparator<Member> fieldComparator = getComparator(field, order);
            
            if (comparator == null) {
                comparator = fieldComparator;
            } else {
                comparator = comparator.thenComparing(fieldComparator);
            }
        }
        
        result.sort(comparator);
        lastAlgorithmUsed = "Multi-field Sort (Java Collections)";
        
        return result;
    }
    
    /**
     * Gets sorting performance statistics.
     * 
     * @return map containing performance metrics
     */
    public Map<String, Object> getSortStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("lastSortTime", lastSortTime);
        stats.put("lastSortTimeMs", lastSortTime / 1_000_000.0);
        stats.put("lastAlgorithmUsed", lastAlgorithmUsed);
        stats.put("lastDataSize", lastDataSize);
        
        if (lastDataSize > 0 && lastSortTime > 0) {
            stats.put("itemsPerSecond", (double) lastDataSize / (lastSortTime / 1_000_000_000.0));
        }
        
        return stats;
    }
    
    /**
     * Benchmarks different sorting algorithms on the given dataset.
     * 
     * @param members dataset to benchmark
     * @param sortBy field to sort by
     * @return map of algorithm names to execution times (nanoseconds)
     */
    public Map<String, Long> benchmark(List<Member> members, String sortBy) {
        Map<String, Long> results = new HashMap<>();
        SortOrder order = SortOrder.ASCENDING;
        
        // Test each algorithm
        for (SortAlgorithm algorithm : SortAlgorithm.values()) {
            try {
                List<Member> testData = new ArrayList<>(members);
                long startTime = System.nanoTime();
                sort(testData, sortBy, order, algorithm);
                long endTime = System.nanoTime();
                results.put(algorithm.name(), endTime - startTime);
            } catch (Exception e) {
                results.put(algorithm.name(), -1L); // Error occurred
            }
        }
        
        return results;
    }
}