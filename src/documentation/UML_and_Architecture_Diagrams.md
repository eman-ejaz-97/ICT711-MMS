# Member Management System - UML and Architecture Diagrams

This document contains comprehensive UML and architecture diagrams for the ICT711 Member Management System project.

## 1. Class Hierarchy Diagram

```mermaid
classDiagram
    class Member {
        <<abstract>>
        -String memberId
        -String firstName
        -String lastName
        -String email
        -String phone
        -LocalDate joinDate
        -double baseFee
        -int performanceRating
        -boolean goalAchieved
        +Member(String, String, String, String, String, double)
        +calculateMonthlyFee()* double
        +getMemberType()* String
        +generatePerformanceReport() String
        +getters/setters()
    }
    
    class RegularMember {
        +RegularMember(String, String, String, String, String)
        +calculateMonthlyFee() double
        +getMemberType() String
    }
    
    class PremiumMember {
        -String trainerName
        -int sessionsPerMonth
        +PremiumMember(String, String, String, String, String, String, int)
        +calculateMonthlyFee() double
        +getMemberType() String
        +getTrainerName() String
        +getSessionsPerMonth() int
    }
    
    class StudentMember {
        -String studentId
        -String university
        +StudentMember(String, String, String, String, String, String, String)
        +calculateMonthlyFee() double
        +getMemberType() String
        +getStudentId() String
        +getUniversity() String
    }
    
    Member <|-- RegularMember : inherits
    Member <|-- PremiumMember : inherits
    Member <|-- StudentMember : inherits
```

## 2. System Architecture Diagram

```mermaid
graph TB
    subgraph "Presentation Layer"
        UI1[InterfaceSelector]
        UI2[MemberManagementGUI]
        UI3[MemberManagementSystem - Text Interface]
    end
    
    subgraph "Business Logic Layer"
        BL1[MemberManager]
        BL2[SearchingAlgorithms]
        BL3[SortingAlgorithms]
    end
    
    subgraph "Data Model Layer"
        DM1[Member - Abstract]
        DM2[RegularMember]
        DM3[PremiumMember]
        DM4[StudentMember]
    end
    
    subgraph "Utilities Layer"
        UT1[Constants]
        UT2[Testing Suite]
    end
    
    subgraph "Data Persistence"
        DP1[CSV Files]
        DP2[File I/O Operations]
    end
    
    UI1 --> UI2
    UI1 --> UI3
    UI2 --> BL1
    UI3 --> BL1
    BL1 --> BL2
    BL1 --> BL3
    BL1 --> DM1
    DM1 --> DM2
    DM1 --> DM3
    DM1 --> DM4
    BL1 --> DP2
    DP2 --> DP1
    BL1 --> UT1
    UT2 --> BL1
    UT2 --> BL2
    UT2 --> BL3
```

## 3. Sequence Diagram - Add New Member (GUI)

```mermaid
sequenceDiagram
    participant User
    participant GUI as MemberManagementGUI
    participant Manager as MemberManager
    participant Member as Member (subclass)
    participant File as File System
    
    User->>GUI: Click "Add Member"
    GUI->>GUI: showAddMemberDialog()
    GUI->>User: Display add member form
    User->>GUI: Fill form and click "Save"
    GUI->>GUI: Validate input data
    
    alt Input is valid
        GUI->>Member: new MemberType(id, name, email, ...)
        Member-->>GUI: Member object created
        GUI->>Manager: addMember(member)
        Manager->>Manager: members.add(member)
        Manager-->>GUI: Success message
        GUI->>GUI: refreshTable()
        GUI->>User: Show success dialog
        
        alt User chooses to save
            User->>GUI: Click "Yes" to save
            GUI->>Manager: saveToFile(filename)
            Manager->>File: Write CSV data
            File-->>Manager: File saved
            Manager-->>GUI: Save confirmation
            GUI->>User: Show save success
        end
    else Input is invalid
        GUI->>User: Show error message
    end
```

## 4. Sequence Diagram - Search Operations

```mermaid
sequenceDiagram
    participant User
    participant GUI as MemberManagementGUI
    participant Manager as MemberManager
    participant SearchAlg as SearchingAlgorithms
    
    User->>GUI: Enter search term and click "Search"
    GUI->>GUI: performSearch()
    GUI->>GUI: Validate search criteria
    
    alt Search by ID
        GUI->>Manager: findMemberById(id)
        Manager->>SearchAlg: linearSearchById(members, id)
        SearchAlg-->>Manager: Member or null
        Manager-->>GUI: Search result
    else Search by Name
        GUI->>Manager: findMembersByName(name)
        Manager->>SearchAlg: fuzzySearchByName(members, name)
        SearchAlg-->>Manager: List<Member>
        Manager-->>GUI: Search results
    else Search by Performance
        GUI->>Manager: findMembersByPerformance(rating)
        Manager->>SearchAlg: rangeSearchByPerformance(members, rating, 10)
        SearchAlg-->>Manager: List<Member>
        Manager-->>GUI: Search results
    end
    
    GUI->>GUI: Update table with results
    GUI->>User: Display search results
```

## 5. Component Diagram

```mermaid
graph LR
    subgraph "UI Components"
        C1[Interface Selector]
        C2[GUI Interface]
        C3[Text Interface]
    end
    
    subgraph "Core Components"
        C4[Member Manager]
        C5[Algorithm Components]
    end
    
    subgraph "Data Components"
        C6[Member Models]
        C7[Constants]
        C8[File Handler]
    end
    
    subgraph "Testing Components"
        C9[Test Suite]
        C10[Performance Tests]
    end
    
    C1 ..> C2 : creates
    C1 ..> C3 : creates
    C2 --> C4 : uses
    C3 --> C4 : uses
    C4 --> C5 : uses
    C4 --> C6 : manages
    C4 --> C7 : references
    C4 --> C8 : uses
    C9 --> C4 : tests
    C9 --> C5 : tests
    C10 --> C5 : measures
```

## 6. Use Case Diagram

```mermaid
graph LR
    User((User))
    Admin((Admin))
    
    subgraph "Member Management System"
        UC1[Add Member]
        UC2[Update Member]
        UC3[Delete Member]
        UC4[Search Members]
        UC5[Sort Members]
        UC6[View Member Details]
        UC7[Generate Reports]
        UC8[Load Data]
        UC9[Save Data]
        UC10[View Statistics]
        UC11[Performance Management]
        UC12[Algorithm Analysis]
    end
    
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9
    
    Admin --> UC10
    Admin --> UC11
    Admin --> UC12
    Admin --> UC1
    Admin --> UC2
    Admin --> UC3
    Admin --> UC4
    Admin --> UC5
    Admin --> UC6
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9
```

## 7. Algorithm Complexity Diagram

```mermaid
graph TD
    subgraph "Search Algorithms"
        SA1["Linear Search<br/>O(n)"]
        SA2["Binary Search<br/>O(log n)"]
        SA3["Hash Search<br/>O(1) avg"]
    end
    
    subgraph "Sort Algorithms"
        SO1["Bubble Sort<br/>O(n²)"]
        SO2["Selection Sort<br/>O(n²)"]
        SO3["Insertion Sort<br/>O(n²)"]
        SO4["Merge Sort<br/>O(n log n)"]
        SO5["Quick Sort<br/>O(n log n) avg"]
        SO6["Heap Sort<br/>O(n log n)"]
    end
    
    subgraph "Dataset Size Recommendations"
        DS1["Small (n < 50)<br/>Any algorithm suitable"]
        DS2["Medium (50 ≤ n < 1000)<br/>O(n log n) recommended"]
        DS3["Large (n ≥ 1000)<br/>Hash search + O(n log n) sort"]
    end
    
    SA1 --> DS1
    SA1 --> DS2
    SA2 --> DS2
    SA2 --> DS3
    SA3 --> DS3
    
    SO1 --> DS1
    SO2 --> DS1
    SO3 --> DS1
    SO4 --> DS2
    SO4 --> DS3
    SO5 --> DS2
    SO5 --> DS3
    SO6 --> DS2
    SO6 --> DS3
```

## 8. Data Flow Diagram

```mermaid
flowchart TD
    Start([User Starts Application])
    Select{Choose Interface}
    GUI[GUI Interface]
    Text[Text Interface]
    
    Load[Load Member Data]
    Display[Display Members]
    
    Action{User Action}
    Add[Add Member]
    Update[Update Member]
    Delete[Delete Member]
    Search[Search Members]
    Sort[Sort Members]
    Report[Generate Reports]
    Stats[View Statistics]
    
    Validate[Validate Input]
    Process[Process Request]
    UpdateData[Update Data Model]
    Refresh[Refresh Display]
    
    Save{Save to File?}
    SaveFile[Save to CSV]
    End([End])
    
    Start --> Select
    Select -->|GUI| GUI
    Select -->|Text| Text
    
    GUI --> Load
    Text --> Load
    Load --> Display
    
    Display --> Action
    Action -->|Add| Add
    Action -->|Update| Update
    Action -->|Delete| Delete
    Action -->|Search| Search
    Action -->|Sort| Sort
    Action -->|Report| Report
    Action -->|Stats| Stats
    Action -->|Exit| End
    
    Add --> Validate
    Update --> Validate
    Delete --> Process
    Search --> Process
    Sort --> Process
    Report --> Process
    Stats --> Process
    
    Validate -->|Valid| Process
    Validate -->|Invalid| Display
    
    Process --> UpdateData
    UpdateData --> Refresh
    Refresh --> Save
    
    Save -->|Yes| SaveFile
    Save -->|No| Display
    SaveFile --> Display
```

## 9. Deployment Diagram

```mermaid
graph TB
    subgraph "Development Environment"
        DE1[Java Development Kit 11+]
        DE2[IDE (Eclipse/IntelliJ/VS Code)]
        DE3[Source Code Files]
    end
    
    subgraph "Runtime Environment"
        RE1[Java Runtime Environment]
        RE2[Member Management System JAR]
        RE3[CSV Data Files]
        RE4[Configuration Files]
    end
    
    subgraph "User Interface Options"
        UI1[Swing GUI Application]
        UI2[Console Text Application]
    end
    
    DE1 --> RE1 : deploys to
    DE2 --> RE2 : builds
    DE3 --> RE2 : compiled into
    
    RE1 --> UI1 : runs
    RE1 --> UI2 : runs
    RE2 --> RE3 : reads/writes
    RE2 --> RE4 : uses
```

## 10. State Diagram - Member Lifecycle

```mermaid
stateDiagram-v2
    [*] --> Created : new Member()
    
    Created --> Active : addMember()
    
    state Active {
        [*] --> LowPerformance : rating < 5
        [*] --> AveragePerformance : rating 5-7
        [*] --> HighPerformance : rating >= 8
        
        LowPerformance --> AveragePerformance : updateRating()
        LowPerformance --> HighPerformance : updateRating()
        AveragePerformance --> LowPerformance : updateRating()
        AveragePerformance --> HighPerformance : updateRating()
        HighPerformance --> AveragePerformance : updateRating()
        HighPerformance --> LowPerformance : updateRating()
        
        LowPerformance --> GoalNotAchieved
        AveragePerformance --> GoalAchieved
        AveragePerformance --> GoalNotAchieved
        HighPerformance --> GoalAchieved
    }
    
    Active --> Updated : updateMember()
    Updated --> Active : save changes
    
    Active --> Deleted : removeMember()
    Deleted --> [*]
```

## Summary

These diagrams provide a comprehensive view of the Member Management System architecture and design:

1. **Class Hierarchy** - Shows inheritance relationships and polymorphism
2. **System Architecture** - Illustrates layered architecture and component separation
3. **Sequence Diagrams** - Detail interaction flows for key operations
4. **Component Diagram** - Shows system components and their relationships
5. **Use Case Diagram** - Identifies system functionality and user interactions
6. **Algorithm Complexity** - Visualizes performance characteristics
7. **Data Flow** - Shows how data moves through the system
8. **Deployment** - Illustrates system deployment structure
9. **State Diagram** - Shows member lifecycle states

The system demonstrates good object-oriented design principles, separation of concerns, and scalable architecture suitable for educational purposes and real-world application.