# Member Management System (MMS) - Project Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [UML Class Diagram](#uml-class-diagram)
3. [OOP Concepts Demonstration](#oop-concepts-demonstration)
4. [Java Collections Usage](#java-collections-usage)
5. [File Handling Implementation](#file-handling-implementation)
6. [Exception Handling](#exception-handling)
7. [Testing Guide](#testing-guide)
8. [Setup Instructions](#setup-instructions)

## System Overview

The Member Management System (MMS) is a comprehensive Java application designed for gym administration. It manages members, tracks their performance, calculates fees dynamically, and provides various administrative functions through a text-based interface.

### Key Features
- Member registration and management (add, update, delete)
- Three member types with different fee structures
- Performance tracking and goal achievement monitoring
- Dynamic fee calculation based on member type and performance
- File persistence using CSV format
- Comprehensive querying capabilities
- Performance letters generation (appreciation/reminder)

## UML Class Diagram

```
                           <<abstract>>
                         +-------------+
                         |   Member    |
                         +-------------+
                         | -memberId: String
                         | -firstName: String
                         | -lastName: String
                         | -email: String
                         | -phone: String
                         | -joinDate: LocalDate
                         | -baseFee: double
                         | -performanceRating: int
                         | -goalAchieved: boolean
                         +-------------+
                         | +calculateMonthlyFee(): double {abstract}
                         | +getMemberType(): String {abstract}
                         | +generatePerformanceReport(): String
                         | +getters/setters...
                         +-------------+
                                 ▲
                                 |
                    inheritance  |
            ┌────────────────────┼────────────────────┐
            |                    |                    |
    +----------------+   +----------------+   +----------------+
    | RegularMember  |   | PremiumMember  |   | StudentMember  |
    +----------------+   +----------------+   +----------------+
    | -GOAL_DISCOUNT |   | -trainerName   |   | -studentId     |
    |     : double   |   | -sessionsPerMonth | -university    |
    +----------------+   | -GOAL_DISCOUNT |   | -STUDENT_DISCOUNT|
    | +calculateMonthlyFee() +----------------+   +----------------+
    | +getMemberType()|   | +calculateMonthlyFee() | +calculateMonthlyFee()
    +----------------+   | +getMemberType()|   | +getMemberType()|
                         | +getTrainerName()|   | +getStudentId()|
                         +----------------+   +----------------+

                         +------------------+
                         |  MemberManager   |
                         +------------------+
                         | -members: ArrayList<Member>
                         | -currentFileName: String
                         +------------------+
                         | +addMember(Member): void
                         | +removeMember(String): boolean
                         | +findMemberById(String): Member
                         | +findMembersByName(String): List<Member>
                         | +findMembersByPerformance(int): List<Member>
                         | +updateMember(String, Map): boolean
                         | +loadFromFile(String): void
                         | +saveToFile(String): void
                         | +generateAppreciationLetter(Member): String
                         | +generateReminderLetter(Member): String
                         | +displayStatistics(): void
                         +------------------+
                                 ▲
                                 | uses
                                 |
                    +----------------------------+
                    | MemberManagementSystem     |
                    +----------------------------+
                    | -manager: MemberManager    |
                    | -scanner: Scanner          |
                    +----------------------------+
                    | +main(String[]): void      |
                    | -displayMenu(): void       |
                    | -loadRecords(): void       |
                    | -addNewMember(): void      |
                    | -updateMember(): void      |
                    | -deleteMember(): void      |
                    | -queryMembers(): void      |
                    | -managePerformance(): void |
                    | -saveToFile(): void        |
                    | -createInitialDataFile(): void |
                    +----------------------------+
```

## OOP Concepts Demonstration

### 1. **Abstraction**
- **Abstract Class**: `Member` is an abstract base class that defines common properties and behaviors for all member types
- **Abstract Methods**: 
  - `calculateMonthlyFee()` - Each subclass implements its own fee calculation logic
  - `getMemberType()` - Returns the specific type of member
- **Benefit**: Hides implementation details while providing a common interface

### 2. **Inheritance**
- **Class Hierarchy**: Three concrete classes extend the abstract `Member` class:
  - `RegularMember` - Basic membership with standard fees
  - `PremiumMember` - Premium membership with personal trainer
  - `StudentMember` - Discounted membership for students
- **Code Reusability**: Common attributes (name, ID, email) and methods are inherited from the base class
- **Extension**: Each subclass adds specific attributes (e.g., `trainerName` for PremiumMember)

### 3. **Polymorphism**
- **Method Overriding**: Each member type overrides `calculateMonthlyFee()` with its own implementation
- **Dynamic Binding**: The system treats all members as `Member` type but calls the appropriate overridden method at runtime
- **Example**: When iterating through a list of `Member` objects, each calculates fees differently based on its actual type

```java
// Polymorphic behavior example
List<Member> members = new ArrayList<>();
members.add(new RegularMember(...));
members.add(new PremiumMember(...));
members.add(new StudentMember(...));

// Each member calculates fee differently
for (Member member : members) {
    double fee = member.calculateMonthlyFee(); // Polymorphism in action
}
```

### 4. **Encapsulation**
- **Private Fields**: All member attributes are private with controlled access through getters/setters
- **Data Validation**: Setters include validation (e.g., `performanceRating` must be 0-10)
- **Information Hiding**: Internal implementation details are hidden from external classes

## Java Collections Usage

### ArrayList Implementation
- **Primary Storage**: `ArrayList<Member>` for dynamic member storage
- **Justification**: 
  - Fast random access (O(1))
  - Dynamic resizing
  - Maintains insertion order
  - Suitable for frequent searches and iterations

### Stream API Usage
- **Filtering**: Finding members by criteria
- **Sorting**: Ordering members by performance
- **Statistical Operations**: Calculating averages and counts

```java
// Example of Stream API usage
members.stream()
    .filter(m -> m.getPerformanceRating() >= minRating)
    .sorted((m1, m2) -> Integer.compare(m2.getPerformanceRating(), m1.getPerformanceRating()))
    .collect(Collectors.toList());
```

### Alternative Data Structures Considered
- **LinkedList**: Could be used for frequent insertions/deletions at arbitrary positions
- **Queue**: Could implement a waiting list for new members
- **HashMap**: Could provide O(1) lookup by member ID if needed

## File Handling Implementation

### CSV Format Design
```csv
Type,ID,FirstName,LastName,Email,Phone,PerformanceRating,GoalAchieved,Extra1,Extra2
Regular,M001,John,Smith,john@email.com,555-0101,7,true,,
Premium,M002,Sarah,Johnson,sarah@email.com,555-0102,9,true,Mike Trainer,8
Student,M003,Emily,Davis,emily@email.com,555-0103,6,false,STU001,State University
```

### File Operations
1. **Reading**: BufferedReader with line-by-line parsing
2. **Writing**: PrintWriter for efficient text output
3. **Error Handling**: Try-with-resources for automatic resource management

## Exception Handling

### Implemented Exception Handling
1. **FileNotFoundException**: When loading non-existent files
2. **IOException**: General file I/O errors
3. **NumberFormatException**: Invalid numeric input
4. **NullPointerException**: Prevented through null checks

### Try-Catch Implementation Examples
```java
// File reading with exception handling
try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
    // File reading logic
} catch (FileNotFoundException e) {
    System.out.println("File not found: " + fileName);
} catch (IOException e) {
    System.out.println("Error reading file: " + e.getMessage());
}

// User input validation
try {
    int choice = Integer.parseInt(scanner.nextLine());
    // Process choice
} catch (NumberFormatException e) {
    System.out.println("Invalid input. Please enter a number.");
}
```

## Testing Guide

### Test Cases to Execute

#### 1. Member Not Found
- **Test**: Search for member ID "M999"
- **Expected**: "Member not found" message

#### 2. Update Non-Existent Member
- **Test**: Try to update member ID "INVALID"
- **Expected**: System should handle gracefully with error message

#### 3. Invalid Performance Rating
- **Test**: Set performance rating to 15
- **Expected**: System should reject (valid range is 0-10)

#### 4. File Not Found
- **Test**: Load from "nonexistent.csv"
- **Expected**: Appropriate error message without crash

#### 5. Empty Query Results
- **Test**: Search for name "ZZZZZ"
- **Expected**: "No members found" message

#### 6. Fee Calculation Verification
- **Test**: Create different member types and verify fee calculations
- **Expected**: 
  - Regular: Base fee with discounts/penalties
  - Premium: Base + session fees with bonuses
  - Student: Discounted rates

#### 7. Performance Letters
- **Test**: Generate letters for high/low performers
- **Expected**: Appropriate letters only for qualifying members

## Setup Instructions

### Prerequisites
- Java JDK 8 or higher
- Text editor or IDE (Eclipse, IntelliJ IDEA, VS Code)

### Compilation Steps
1. Save all classes in the same directory
2. Compile from command line:
```bash
javac *.java
```

### Running the Application
```bash
java MemberManagementSystem
```

### Initial Setup
1. The system will create `member_data.csv` automatically with 10 sample members
3. Load the file using option 1 in the menu

## System Features Walkthrough

### 1. Loading Records
- Reads member data from CSV file
- Parses and creates appropriate member objects
- Handles file errors gracefully

### 2. Adding Members
- Interactive prompts for member details
- Supports all three member types
- Option to save immediately

### 3. Updating Members
- Search by ID
- Selective field updates
- Preserves unchanged data

### 4. Deleting Members
- Confirmation required
- Removes from memory and optionally from file

### 5. Querying Members
- By ID (exact match)
- By name (partial match)
- By performance rating (minimum threshold)
- View all members

### 6. Performance Management
- Generate appreciation letters (rating ≥ 8)
- Generate reminder letters (rating < 5)
- View fee breakdown for all members
- Calculate total monthly revenue

### 7. Statistics Display
- Member count by type
- Average performance rating
- Goal achievement statistics

## Conclusion

This Member Management System successfully demonstrates all required OOP concepts, Java Collections usage, file handling, and exception management. The system is extensible, maintainable, and provides a robust solution for gym member management. This is a group project for ICT711.

Group Members:
Muhammad Eman Ejaz - 20034038
Sajina Rana - 
Muhammad Waqas Iqbal - 
Sravanth Rao -


### Key Achievements
✓ Complete OOP implementation with abstraction, inheritance, polymorphism, and encapsulation
✓ Efficient use of Java Collections (ArrayList, Streams)
✓ Robust file I/O with CSV format
✓ Comprehensive exception handling
✓ User-friendly text-based interface
✓ Extensible design for future enhancements
