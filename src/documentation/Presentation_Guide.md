# ICT711 Assessment 4 - Presentation Guide
## Complete Execution Flow and Implementation Details

**Student:** Muhammad Eman Ejaz (20034038)  
**Project:** Member Management System with GUI Implementation

---

## Table of Contents
1. [Application Overview](#1-application-overview)
2. [Execution Flow](#2-execution-flow)
3. [Class-by-Class Implementation](#3-class-by-class-implementation)
4. [Algorithm Implementations](#4-algorithm-implementations)
5. [Key Design Decisions](#5-key-design-decisions)
6. [Demo Scenarios](#6-demo-scenarios)
7. [Technical Q&A Preparation](#7-technical-qa-preparation)

---

## 1. Application Overview

### 1.1 What This Project Demonstrates
- **Object-Oriented Programming**: Inheritance, polymorphism, encapsulation, abstraction
- **Algorithm Implementation**: 6 sorting + 5 searching algorithms with complexity analysis
- **GUI Development**: Swing-based graphical interface with event handling
- **Software Engineering**: Testing, documentation, error handling, file I/O
- **Design Patterns**: Strategy, Template Method, Factory patterns

### 1.2 Project Architecture (Layered Design)
```
┌─────────────────────────────────────────┐
│     PRESENTATION LAYER                  │
│  InterfaceSelector → GUI/Text Interface │
├─────────────────────────────────────────┤
│     BUSINESS LOGIC LAYER                │
│  MemberManager ↔ Search/Sort Algorithms │
├─────────────────────────────────────────┤
│     DATA MODEL LAYER                    │
│  Member (Abstract) → Concrete Subclasses│
├─────────────────────────────────────────┤
│     UTILITIES LAYER                     │
│  Constants + Testing Suite              │
├─────────────────────────────────────────┤
│     DATA PERSISTENCE LAYER              │
│  CSV File I/O Operations                │
└─────────────────────────────────────────┘
```

---

## 2. Execution Flow

### 2.1 Application Startup Flow
```
1. main() in InterfaceSelector
2. SwingUtilities.invokeLater() → Thread safety for GUI
3. InterfaceSelector window appears
4. User chooses interface type
5a. GUI: MemberManagementGUI.constructor()
5b. Text: MemberManagementSystem.main()
6. MemberManager initialized
7. createInitialDataFile() if needed
8. loadFromFile() → CSV parsing
9. Interface ready for user interaction
```

### 2.2 Detailed Startup Sequence

#### Step 1: InterfaceSelector.main()
```java
public static void main(String[] args) {
    // Creates GUI on Event Dispatch Thread for thread safety
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            InterfaceSelector selector = new InterfaceSelector();
            selector.setVisible(true);
        }
    });
}
```

#### Step 2: Interface Selection
- **GUI Button Clicked**: Calls `MemberManagementGUI` constructor
- **Text Button Clicked**: Calls `MemberManagementSystem.main()`

#### Step 3: System Initialization
```java
// In both interfaces:
private MemberManager manager = new MemberManager();

// In GUI constructor:
initializeGUI();      // Create all GUI components
loadDefaultData();    // Load or create sample data
```

### 2.3 Data Loading Flow
```
1. createInitialDataFile() checks if member_data.csv exists
2. If not exists: Creates CSV with 10 sample members
3. manager.loadFromFile() reads CSV
4. CSV parsing in MemberManager.loadFromFile():
   - Split each line by comma
   - Identify member type (Regular/Premium/Student)
   - Create appropriate Member subclass object
   - Set performance rating and goal achievement
   - Add to members ArrayList
5. refreshTable() (GUI) or display ready (Text)
```

---

## 3. Class-by-Class Implementation

### 3.1 InterfaceSelector Class
**Purpose**: Entry point allowing user to choose interface type

**Key Components**:
```java
private JPanel createButtonPanel() {
    // Creates three buttons: GUI, Text, Exit
    // Each button has ActionListener for interface launching
}
```

**Event Handling**:
```java
guiButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        dispose(); // Close selector
        SwingUtilities.invokeLater(() -> {
            MemberManagementGUI gui = new MemberManagementGUI();
            gui.setVisible(true);
        });
    }
});
```

### 3.2 Member Class Hierarchy

#### Abstract Member Class
**Purpose**: Base class defining common attributes and abstract methods

**Key Concepts Demonstrated**:
```java
public abstract class Member {
    // ENCAPSULATION: Private fields with public getters/setters
    private String memberId;
    private int performanceRating;
    
    // ABSTRACTION: Abstract methods force subclass implementation
    public abstract double calculateMonthlyFee();
    public abstract String getMemberType();
    
    // VALIDATION: Controlled access to data
    public void setPerformanceRating(int rating) {
        if (rating >= 0 && rating <= 10) {
            this.performanceRating = rating;
        }
    }
}
```

#### RegularMember Class
**Purpose**: Basic membership with goal-based discounts

**Fee Calculation Logic**:
```java
public double calculateMonthlyFee() {
    double fee = getBaseFee(); // $50
    
    // 10% discount if goal achieved
    if (isGoalAchieved()) {
        fee *= (1 - Constants.REGULAR_GOAL_ACHIEVEMENT_DISCOUNT);
    }
    
    // $10 penalty if performance < 3
    if (getPerformanceRating() < Constants.PENALTY_PERFORMANCE_THRESHOLD) {
        fee += Constants.LOW_PERFORMANCE_PENALTY;
    }
    
    return fee;
}
```

#### PremiumMember Class
**Purpose**: Enhanced membership with trainer and sessions

**Additional Attributes**:
```java
private String trainerName;
private int sessionsPerMonth;

// Fee includes base + session costs + discounts/bonuses
public double calculateMonthlyFee() {
    double fee = getBaseFee() + (sessionsPerMonth * Constants.SESSION_COST);
    
    if (isGoalAchieved()) {
        fee *= (1 - Constants.PREMIUM_GOAL_ACHIEVEMENT_DISCOUNT); // 15%
    }
    
    if (getPerformanceRating() >= Constants.HIGH_PERFORMANCE_THRESHOLD) {
        fee -= Constants.HIGH_PERFORMANCE_BONUS; // $20 bonus
    }
    
    return fee;
}
```

#### StudentMember Class
**Purpose**: Discounted membership for students

**Special Logic**:
```java
public double calculateMonthlyFee() {
    // Start with 30% student discount
    double fee = getBaseFee() * (1 - Constants.STUDENT_BASE_DISCOUNT);
    
    // Additional $5 bonus if goal achieved
    if (isGoalAchieved()) {
        fee -= Constants.STUDENT_GOAL_ACHIEVEMENT_BONUS;
    }
    
    // Ensure minimum fee of $20
    return Math.max(fee, Constants.MINIMUM_STUDENT_FEE);
}
```

### 3.3 MemberManager Class

**Purpose**: Central business logic controller

**Key Responsibilities**:
1. **CRUD Operations**: Add, find, update, remove members
2. **File I/O**: Save/load CSV data
3. **Algorithm Integration**: Coordinate with searching/sorting classes
4. **Data Validation**: Ensure data integrity

**Critical Methods**:

#### addMember()
```java
public void addMember(Member member) {
    members.add(member);
    System.out.printf("Member added: %s with ID: %s", 
                     member.getFullName(), member.getMemberId());
}
```

#### updateMember()
```java
public boolean updateMember(String memberId, Map<String, Object> updates) {
    Member member = findMemberById(memberId);
    if (member == null) return false;
    
    // Apply updates based on map keys
    if (updates.containsKey("email")) {
        member.setEmail((String) updates.get("email"));
    }
    if (updates.containsKey("performanceRating")) {
        member.setPerformanceRating((Integer) updates.get("performanceRating"));
    }
    
    return true;
}
```

#### CSV File Loading
```java
public void loadFromFile(String fileName) throws IOException {
    members.clear(); // Start fresh
    
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        String line;
        reader.readLine(); // Skip header
        
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String type = parts[0];
            
            // FACTORY PATTERN: Create objects based on type
            Member member = null;
            switch (type) {
                case "Regular":
                    member = new RegularMember(parts[1], parts[2], parts[3], parts[4], parts[5]);
                    break;
                case "Premium":
                    member = new PremiumMember(parts[1], parts[2], parts[3], parts[4], parts[5], 
                                             parts[8], Integer.parseInt(parts[9]));
                    break;
                case "Student":
                    member = new StudentMember(parts[1], parts[2], parts[3], parts[4], parts[5], 
                                             parts[8], parts[9]);
                    break;
            }
            
            if (member != null) {
                member.setPerformanceRating(Integer.parseInt(parts[6]));
                member.setGoalAchieved(Boolean.parseBoolean(parts[7]));
                members.add(member);
            }
        }
    }
}
```

### 3.4 MemberManagementGUI Class

**Purpose**: Complete Swing-based graphical interface

**GUI Architecture**:
```
JFrame (Main Window)
├── MenuBar (File operations)
├── SearchPanel (North) - Search controls
├── JTable in JScrollPane (Center) - Data display
└── ButtonPanel (South) - Action buttons
```

**Key GUI Components**:

#### Table Creation
```java
private void createTablePanel() {
    String[] columnNames = {"ID", "Name", "Type", "Email", "Phone", "Rating", "Goal", "Fee"};
    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Read-only table for data integrity
        }
    };
    
    memberTable = new JTable(tableModel);
    memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}
```

#### Add Member Dialog
```java
private void showAddMemberDialog() {
    JDialog dialog = new JDialog(this, "Add New Member", true);
    
    // Dynamic form based on member type
    JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Regular", "Premium", "Student"});
    
    // Type-specific fields shown/hidden based on selection
    typeCombo.addActionListener(e -> {
        String type = (String) typeCombo.getSelectedItem();
        boolean isPremium = "Premium".equals(type);
        boolean isStudent = "Student".equals(type);
        
        // Show/hide relevant fields
        trainerField.setVisible(isPremium);
        sessionsField.setVisible(isPremium);
        studentIdField.setVisible(isStudent);
        universityField.setVisible(isStudent);
        
        dialog.pack(); // Resize dialog
    });
}
```

#### Event Handling Pattern
```java
// All buttons follow this pattern:
button.addActionListener(e -> methodName());

// Example:
addButton.addActionListener(e -> showAddMemberDialog());
updateButton.addActionListener(e -> showUpdateMemberDialog());
deleteButton.addActionListener(e -> deleteMember());
```

---

## 4. Algorithm Implementations

### 4.1 Searching Algorithms

#### Linear Search - O(n)
**When to Use**: Small datasets, unsorted data
```java
public static Member linearSearchById(List<Member> members, String memberId) {
    for (int i = 0; i < members.size(); i++) {
        if (members.get(i).getMemberId().equals(memberId)) {
            System.out.println("Found at index " + i + " after " + (i + 1) + " comparisons");
            return members.get(i);
        }
    }
    return null;
}
```

#### Binary Search - O(log n)
**When to Use**: Large sorted datasets
```java
public static Member binarySearchById(List<Member> sortedMembers, String memberId) {
    int left = 0, right = sortedMembers.size() - 1;
    int comparisons = 0;
    
    while (left <= right) {
        comparisons++;
        int middle = left + (right - left) / 2; // Avoid overflow
        Member middleMember = sortedMembers.get(middle);
        
        int comparison = middleMember.getMemberId().compareTo(memberId);
        
        if (comparison == 0) {
            System.out.println("Found at index " + middle + " after " + comparisons + " comparisons");
            return middleMember;
        } else if (comparison < 0) {
            left = middle + 1; // Search right half
        } else {
            right = middle - 1; // Search left half
        }
    }
    return null;
}
```

#### Hash Search - O(1) average
**When to Use**: Frequent searches, memory available
```java
public static Member hashSearchById(List<Member> members, String memberId) {
    // Build HashMap for O(1) lookups
    Map<String, Member> memberMap = new HashMap<>();
    for (Member member : members) {
        memberMap.put(member.getMemberId(), member);
    }
    
    return memberMap.get(memberId); // O(1) lookup
}
```

### 4.2 Sorting Algorithms

#### Bubble Sort - O(n²)
**Educational Value**: Shows basic comparison sorting
```java
public static List<Member> bubbleSortById(List<Member> members) {
    List<Member> sortedList = new ArrayList<>(members);
    int n = sortedList.size();
    
    for (int i = 0; i < n - 1; i++) {
        boolean swapped = false; // Optimization
        
        for (int j = 0; j < n - i - 1; j++) {
            if (sortedList.get(j).getMemberId().compareTo(
                sortedList.get(j + 1).getMemberId()) > 0) {
                
                // Swap elements
                Member temp = sortedList.get(j);
                sortedList.set(j, sortedList.get(j + 1));
                sortedList.set(j + 1, temp);
                swapped = true;
            }
        }
        
        if (!swapped) break; // Already sorted
    }
    
    return sortedList;
}
```

#### Merge Sort - O(n log n)
**Key Feature**: Stable, guaranteed performance
```java
public static List<Member> mergeSortByFee(List<Member> members) {
    List<Member> sortedList = new ArrayList<>(members);
    mergeSortRecursive(sortedList, 0, sortedList.size() - 1);
    return sortedList;
}

private static void mergeSortRecursive(List<Member> list, int left, int right) {
    if (left < right) {
        int middle = left + (right - left) / 2; // Avoid overflow
        
        // Divide: Sort both halves
        mergeSortRecursive(list, left, middle);
        mergeSortRecursive(list, middle + 1, right);
        
        // Conquer: Merge sorted halves
        merge(list, left, middle, right);
    }
}

private static void merge(List<Member> list, int left, int middle, int right) {
    // Create temporary arrays
    List<Member> leftArray = new ArrayList<>();
    List<Member> rightArray = new ArrayList<>();
    
    // Copy data to temp arrays
    for (int i = left; i <= middle; i++) {
        leftArray.add(list.get(i));
    }
    for (int j = middle + 1; j <= right; j++) {
        rightArray.add(list.get(j));
    }
    
    // Merge back to original array
    int i = 0, j = 0, k = left;
    while (i < leftArray.size() && j < rightArray.size()) {
        if (leftArray.get(i).calculateMonthlyFee() <= 
            rightArray.get(j).calculateMonthlyFee()) {
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
        i++; k++;
    }
    while (j < rightArray.size()) {
        list.set(k, rightArray.get(j));
        j++; k++;
    }
}
```

---

## 5. Key Design Decisions

### 5.1 Why Layered Architecture?
- **Separation of Concerns**: Each layer has single responsibility
- **Maintainability**: Changes in one layer don't affect others
- **Testability**: Each layer can be tested independently
- **Reusability**: Business logic shared between GUI and text interfaces

### 5.2 Why Abstract Member Class?
- **Code Reuse**: Common attributes and methods in base class
- **Polymorphism**: Treat all member types uniformly
- **Extensibility**: Easy to add new member types
- **Template Method Pattern**: Common structure, specific implementations

### 5.3 Why Separate Algorithm Classes?
- **Strategy Pattern**: Algorithm selection at runtime
- **Educational Value**: Clear demonstration of different approaches
- **Performance Comparison**: Easy to benchmark different algorithms
- **Single Responsibility**: Each class focused on one type of operation

### 5.4 Why HashMap + ArrayList Combination?
- **ArrayList**: Maintains order, supports sorting
- **HashMap**: Fast lookups for search operations
- **Flexibility**: Can choose best data structure for each operation

---

## 6. Demo Scenarios

### 6.1 Basic Functionality Demo Script

#### Scenario 1: Interface Selection
1. **Start Application**: `java -cp . ui.InterfaceSelector`
2. **Show Interface Options**: Point out the three buttons
3. **Select GUI**: Click "Graphical User Interface" button
4. **Explain**: "This demonstrates user choice in interface design"

#### Scenario 2: View Sample Data
1. **Point to Table**: "10 sample members automatically loaded"
2. **Explain Member Types**: Regular (M001, M004, M007, M010), Premium (M002, M005, M008), Student (M003, M006, M009)
3. **Show Fee Calculations**: Point out different fees based on member type

#### Scenario 3: Add New Member
1. **Click "Add Member"**: Opens dynamic form
2. **Select "Premium"**: Show how form changes (trainer fields appear)
3. **Fill Form**: ID: "P999", Name: "Demo User", Trainer: "Sarah Coach", Sessions: 5
4. **Save**: Show confirmation dialog
5. **Verify**: New member appears in table with calculated fee

#### Scenario 4: Search Demonstration
1. **Search by ID**: Enter "M001", click search
2. **Search by Name**: Enter "Alice", click search
3. **Search by Performance**: Enter "8", click search
4. **Explain**: "Different search algorithms used internally"

#### Scenario 5: Sorting Demonstration
1. **Sort by Name**: Select from dropdown, show alphabetical order
2. **Sort by Performance**: Show highest performers first
3. **Sort by Fee**: Show fee-based ordering
4. **Explain**: "Multiple sorting algorithms implemented"

### 6.2 Algorithm Demonstration

#### Performance Comparison Demo
1. **Run Test Suite**: `java -cp . testing.MemberManagementSystemTests`
2. **Show Results**: All 31 tests pass
3. **Explain Performance**: 
   - Linear Search: 1-9 comparisons depending on position
   - Binary Search: Maximum 4 comparisons for 9 members
   - Hash Search: 1 operation

#### Complexity Analysis Demo
1. **Show Algorithm Analysis**: 
   ```java
   manager.analyzeAlgorithmComplexity();
   ```
2. **Explain Big O**: 
   - O(n²) algorithms: Good for small data (< 50 members)
   - O(n log n): Best for medium data (50-1000 members)
   - O(1): Best for frequent searches

### 6.3 Error Handling Demo

#### Input Validation
1. **Try Invalid Performance Rating**: Enter -1 or 11
2. **Show Validation**: Rating remains unchanged
3. **Try Empty Fields**: Show error messages

#### File Operations
1. **Delete CSV File**: Show how application creates new one
2. **Show Graceful Recovery**: Application continues working

---

## 7. Technical Q&A Preparation

### 7.1 Object-Oriented Programming Questions

**Q: Explain polymorphism in your project.**
A: Polymorphism is demonstrated through the Member class hierarchy. All member objects are stored as Member references in the ArrayList, but each calls its own calculateMonthlyFee() method:

```java
for (Member member : members) {
    double fee = member.calculateMonthlyFee(); // Calls subclass method
    System.out.println(member.getMemberType()); // Calls subclass method
}
```

**Q: How does inheritance work in your Member classes?**
A: The Member abstract class provides common attributes (id, name, email, etc.) and methods (getters, setters, validation). Subclasses inherit these and provide specific implementations:

```java
// RegularMember inherits all Member attributes and methods
public class RegularMember extends Member {
    // Only needs to implement abstract methods
    public double calculateMonthlyFee() { /* specific logic */ }
    public String getMemberType() { return "Regular Membership"; }
}
```

**Q: What is encapsulation in your project?**
A: Private fields with controlled access through getters/setters:

```java
private int performanceRating; // Cannot be accessed directly

public void setPerformanceRating(int rating) {
    // Validation ensures data integrity
    if (rating >= 0 && rating <= 10) {
        this.performanceRating = rating;
    }
}
```

### 7.2 Algorithm Questions

**Q: When would you use binary search vs linear search?**
A: 
- **Linear Search**: Small datasets (< 50), unsorted data, simple implementation
- **Binary Search**: Large datasets (> 100), sorted data required, O(log n) performance
- **Hash Search**: Frequent searches, when memory usage is acceptable

**Q: Why implement multiple sorting algorithms?**
A: Different algorithms have different characteristics:
- **Bubble/Selection**: Educational value, O(n²) complexity
- **Insertion**: Good for small or nearly-sorted data
- **Merge**: Stable, guaranteed O(n log n), needs extra memory
- **Quick**: Generally fastest, in-place, O(n²) worst case
- **Heap**: Guaranteed O(n log n), in-place, not stable

**Q: How do you measure algorithm performance?**
A: 
1. **Time Complexity**: Count comparisons and operations
2. **Space Complexity**: Measure additional memory usage
3. **Empirical Testing**: Actual runtime measurement
4. **Big O Analysis**: Theoretical performance characteristics

### 7.3 Design Pattern Questions

**Q: What design patterns did you use?**
A:
1. **Strategy Pattern**: Different algorithms in separate classes
2. **Template Method**: Member class defines structure, subclasses implement details
3. **Factory Pattern**: Member creation based on type string
4. **Observer Pattern**: GUI event handling (ActionListeners)

**Q: Why separate the UI from business logic?**
A: 
- **Maintainability**: Changes to GUI don't affect business rules
- **Testability**: Can test business logic without GUI
- **Reusability**: Same logic works for both GUI and text interfaces
- **Single Responsibility**: Each class has one job

### 7.4 Testing Questions

**Q: How did you test your application?**
A: Comprehensive testing approach:
1. **Unit Tests**: Individual method testing
2. **Integration Tests**: Component interaction testing
3. **Performance Tests**: Algorithm efficiency verification
4. **Boundary Tests**: Edge cases and limits
5. **Error Tests**: Exception handling validation

**Q: Why is testing important?**
A:
- **Correctness**: Ensures algorithms work as expected
- **Reliability**: Catches edge cases and errors
- **Confidence**: Allows safe refactoring and enhancement
- **Documentation**: Tests show how components should work

### 7.5 File I/O Questions

**Q: Why use CSV format?**
A:
- **Simplicity**: Easy to read and write
- **Human Readable**: Can open in Excel or text editor
- **Cross-platform**: Works on any operating system
- **Lightweight**: No database setup required

**Q: How do you handle file errors?**
A: Try-catch blocks with specific error handling:

```java
try {
    manager.loadFromFile(fileName);
} catch (FileNotFoundException e) {
    System.out.println("File not found: " + fileName);
    createInitialDataFile(); // Graceful recovery
} catch (IOException e) {
    System.out.println("Error reading file: " + e.getMessage());
}
```

---

## 8. Presentation Tips

### 8.1 What to Emphasize
1. **Object-Oriented Design**: Show inheritance hierarchy clearly
2. **Algorithm Variety**: Demonstrate different approaches to same problem
3. **User Interface Choice**: Show flexibility in interface design
4. **Code Quality**: Point out documentation, testing, error handling
5. **Real-world Application**: Gym management is practical use case

### 8.2 Common Mistakes to Avoid
- Don't just click through the GUI - explain the code behind it
- Don't memorize the presentation - understand the concepts
- Don't skip error handling demonstrations
- Don't forget to mention testing and validation

### 8.3 Demonstration Flow
1. **Start with Overview**: Show architecture diagram
2. **Code Walkthrough**: Key classes and methods
3. **Live Demo**: GUI and functionality
4. **Algorithm Focus**: Show different approaches
5. **Testing Results**: Prove it works
6. **Q&A Preparation**: Be ready for technical questions

### 8.4 Key Points to Remember
- This is educational software - focus on learning concepts
- GUI is functional, not beautiful - that's intentional
- Multiple algorithms show understanding of trade-offs
- Testing proves reliability and correctness
- Documentation shows professional development practices

---

**Final Note**: This project demonstrates comprehensive understanding of object-oriented programming, algorithm design, GUI development, and software engineering principles. The focus is on code quality, educational value, and technical correctness rather than aesthetic perfection.