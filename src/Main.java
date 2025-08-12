package src;

import ui.MemberManagementSystem;

/**
 * Entry point for the Member Management System application.
 * 
 * This is an alternative main class that delegates to the UI system.
 * The primary entry point is MemberManagementSystem.main().
 * 
 * @author ICT711 Group Project Team
 * @version 1.0
 */
public class Main {
    /**
     * Application entry point.
     * Delegates execution to the main UI system.
     * 
     * @param args command line arguments (passed through to UI)
     */
    public static void main(String[] args) {
        MemberManagementSystem.main(args);
    }
} 