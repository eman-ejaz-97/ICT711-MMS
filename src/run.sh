#!/bin/bash

# Member Management System - ICT711 Assessment 4
# Run script for the application

echo "=== ICT711 Member Management System === (Muhammad Eman Ejaz - 20034038)"
echo "Compiling Java files..."

# Compile all Java files
javac -cp . ui/*.java models/*.java manager/*.java algorithms/*.java constants/*.java testing/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "Choose how to run the application:"
    echo "1. Interface Selector (GUI + Text options)"
    echo "2. GUI Interface directly"
    echo "3. Text Interface directly"
    echo "4. Run comprehensive tests"
    echo "5. Exit"
    
    read -p "Enter choice (1-5): " choice
    
    case $choice in
        1)
            echo "Starting Interface Selector..."
            java -cp . ui.InterfaceSelector
            ;;
        2)
            echo "Starting GUI Interface..."
            java -cp . ui.MemberManagementGUI
            ;;
        3)
            echo "Starting Text Interface..."
            java -cp . ui.MemberManagementSystem
            ;;
        4)
            echo "Running comprehensive test suite..."
            java -cp . testing.MemberManagementSystemTests
            ;;
        5)
            echo "Goodbye!"
            exit 0
            ;;
        *)
            echo "Invalid choice. Starting Interface Selector by default..."
            java -cp . ui.InterfaceSelector
            ;;
    esac
else
    echo "Compilation failed. Please check for errors above."
    exit 1
fi