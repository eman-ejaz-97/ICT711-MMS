# ICT711 Member Management System - Assessment 4

## Overview
This is a comprehensive Java application for gym member management with both GUI and text-based interfaces. The system demonstrates advanced programming concepts including inheritance, polymorphism, algorithm implementation, and user interface design.

## Features
- **Dual Interface Support**: Choose between graphical and text-based interfaces
- **Complete CRUD Operations**: Add, update, delete, and query member information
- **Multiple Member Types**: Regular, Premium, and Student memberships with different pricing
- **Advanced Algorithms**: Multiple searching and sorting algorithms with performance analysis
- **Performance Tracking**: Member fitness goals and performance rating management
- **Statistical Reporting**: Comprehensive analytics and reporting features
- **Data Persistence**: CSV file-based data storage and retrieval

## Project Structure
```
src/
├── algorithms/           # Searching and sorting algorithm implementations
├── constants/           # Application constants and configuration
├── manager/            # Business logic and data management
├── models/             # Member class hierarchy
├── testing/            # Comprehensive testing suite
├── ui/                 # User interface components
├── documentation/      # Project documentation and diagrams
├── run.sh             # Unix/Linux run script
├── run.bat            # Windows run script
└── README.md          # This file
```

## Quick Start

### Prerequisites
- Java 11 or higher
- Any modern operating system (Windows, macOS, Linux)

### Running the Application

#### Option 1: Using Run Scripts (Recommended)

**On Unix/Linux/macOS:**
```bash
cd src
./run.sh
```

**On Windows:**
```cmd
cd src
run.bat
```

The script will compile all files and present you with options to run different parts of the application.

#### Option 2: Manual Compilation and Execution

1. **Compile all Java files:**
```bash
javac -cp . ui/*.java models/*.java manager/*.java algorithms/*.java constants/*.java testing/*.java
```

2. **Run the Interface Selector:**
```bash
java -cp . ui.InterfaceSelector
```

3. **Or run specific interfaces directly:**
```bash
# GUI Interface
java -cp . ui.MemberManagementGUI

# Text Interface
java -cp . ui.MemberManagementSystem

# Test Suite
java -cp . testing.MemberManagementSystemTests
```

## User Guide

### Interface Selection
When you start the application, you'll see an interface selector with three options:
1. **Graphical User Interface (GUI)** - Modern, user-friendly interface with buttons and forms
2. **Text-Based Interface (Console)** - Traditional command-line interface
3. **Exit Application** - Close the application

### GUI Interface Features
- **Search Panel**: Search members by ID, name, or performance rating
- **Sort Options**: Sort members by various criteria
- **Member Table**: View all member information in a sortable table
- **Action Buttons**:
  - **Add Member**: Create new member with type-specific forms
  - **Update Member**: Modify existing member information
  - **Delete Member**: Remove member (with confirmation)
  - **View Details**: See comprehensive member information
  - **Performance Management**: Generate reports and letters
  - **Statistics**: View system-wide analytics

### Text Interface Features
- **Menu-driven interface** with numbered options
- **All CRUD operations** available through simple commands
- **Performance management** and reporting
- **Statistical analysis** and member queries

### Sample Data
The application automatically creates sample data with 10 members across all membership types when first run. This includes:
- Regular members with various performance levels
- Premium members with different trainers and session counts
- Student members from different universities

## Member Types

### Regular Members
- Base fee: $50/month
- 10% discount if goal achieved
- $10 penalty if performance rating < 3

### Premium Members
- Base fee: $100/month + session costs
- Session cost: $25 per session
- 15% discount if goal achieved
- $20 bonus if performance rating ≥ 8

### Student Members
- Base fee: $40/month
- 30% base discount
- $5 additional bonus if goal achieved
- Minimum fee: $20/month

## Algorithm Demonstrations

The application includes implementations of various algorithms for educational purposes:

### Searching Algorithms
- **Linear Search**: O(n) - Sequential search through data
- **Binary Search**: O(log n) - Efficient search on sorted data
- **Hash Search**: O(1) average - HashMap-based instant lookup
- **Fuzzy Search**: Partial name matching
- **Range Search**: Find members within performance ranges

### Sorting Algorithms
- **Bubble Sort**: O(n²) - Simple comparison-based sort
- **Selection Sort**: O(n²) - Minimum selection sort
- **Insertion Sort**: O(n²) - Adaptive insertion sort
- **Merge Sort**: O(n log n) - Stable divide-and-conquer sort
- **Quick Sort**: O(n log n) average - Efficient partition sort
- **Heap Sort**: O(n log n) - Heap-based sort

## Testing

The application includes a comprehensive testing suite that validates:
- Member class hierarchy and polymorphism
- CRUD operations functionality
- Algorithm correctness and performance
- File I/O operations
- Error handling and edge cases
- Performance calculations

To run the tests:
```bash
java -cp . testing.MemberManagementSystemTests
```

## Files and Data

### Data Files
- **member_data.csv**: Default data storage file
- Automatically created with sample data on first run
- Can be loaded/saved through both interfaces

### Configuration
- All constants defined in `constants/Constants.java`
- Easy to modify fees, thresholds, and messages
- Centralized configuration management

## Troubleshooting

### Common Issues

**Issue**: "member_data.csv not found"
- **Solution**: This is normal on first run. The application will create the file automatically.

**Issue**: Button text not readable
- **Solution**: Updated with better contrast colors. If still having issues, try running on different OS or Java version.

**Issue**: Compilation errors
- **Solution**: Ensure you're using Java 11 or higher and all files are in the correct directory structure.

**Issue**: GUI not appearing
- **Solution**: Make sure you have a display available. On headless systems, use the text interface instead.

## Development Notes

### Code Quality
- Comprehensive JavaDoc documentation
- Consistent naming conventions
- Proper exception handling
- Separation of concerns with layered architecture

### Design Patterns Used
- **Strategy Pattern**: Algorithm implementations
- **Template Method**: Member hierarchy
- **Factory Pattern**: Member creation
- **Observer Pattern**: GUI event handling

### Performance Considerations
- Efficient algorithms for large datasets
- Memory-conscious implementations
- Scalable design patterns

## Assessment Requirements Fulfilled

✅ GUI and text-based interface choice  
✅ All CRUD operations implemented  
✅ Sorting and searching algorithms  
✅ Algorithm complexity analysis  
✅ Comprehensive testing suite  
✅ Performance management features  
✅ Statistical reporting  
✅ File I/O operations  
✅ Error handling and validation  
✅ Object-oriented design principles  

## Contact and Support

This project was developed for ICT711 Programming and Algorithms T225, Assessment 4(Muhammad Eman Ejaz - 20034038).

- The detailed report: `documentation/ICT711_Assessment4_Report.md`

---