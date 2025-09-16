@echo off
REM Member Management System - ICT711 Assessment 4 - Muhammad Eman Ejaz (20034038)
REM Run script for Windows

echo === ICT711 Member Management System ===
echo Compiling Java files...

REM Compile all Java files
javac -cp . ui/*.java models/*.java manager/*.java algorithms/*.java constants/*.java testing/*.java

if %errorlevel% == 0 (
    echo Compilation successful!
    echo.
    echo Choose how to run the application:
    echo 1. Interface Selector (GUI + Text options)
    echo 2. GUI Interface directly
    echo 3. Text Interface directly
    echo 4. Run comprehensive tests
    echo 5. Exit
    
    set /p choice="Enter choice (1-5): "
    
    if "%choice%"=="1" (
        echo Starting Interface Selector...
        java -cp . ui.InterfaceSelector
    ) else if "%choice%"=="2" (
        echo Starting GUI Interface...
        java -cp . ui.MemberManagementGUI
    ) else if "%choice%"=="3" (
        echo Starting Text Interface...
        java -cp . ui.MemberManagementSystem
    ) else if "%choice%"=="4" (
        echo Running comprehensive test suite...
        java -cp . testing.MemberManagementSystemTests
    ) else if "%choice%"=="5" (
        echo Goodbye!
        exit /b 0
    ) else (
        echo Invalid choice. Starting Interface Selector by default...
        java -cp . ui.InterfaceSelector
    )
) else (
    echo Compilation failed. Please check for errors above.
    pause
    exit /b 1
)

pause