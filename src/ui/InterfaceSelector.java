package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interface selector that allows users to choose between GUI and Text-based interfaces.
 * 
 * This class serves as the entry point for the application, providing users with
 * the option to select their preferred mode of interaction:
 * - Graphical User Interface (GUI) - More user-friendly and visual
 * - Text-Based Interface (TBI) - Traditional command-line style interface
 * 
 * Demonstrates good user experience design by allowing interface preference selection.
 * 
 * @author ICT711 Student
 * @version 1.0
 */
public class InterfaceSelector extends JFrame {
    
    /**
     * Constructs the interface selection window.
     * Sets up the GUI components and layout for interface selection.
     */
    public InterfaceSelector() {
        initializeSelectionGUI();
    }
    
    /**
     * Initializes the GUI components for interface selection.
     * Creates a simple, clean interface with two main options.
     */
    private void initializeSelectionGUI() {
        // Set up main window properties
        setTitle("Member Management System - Interface Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        // Create main panel with padding for better appearance
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(Color.WHITE);
        
        // Title section with application information
        JLabel titleLabel = new JLabel("Member Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 102, 204)); // Professional blue color
        
        JLabel subtitleLabel = new JLabel("ICT711 Assessment 4 - Individual Project");
        JLabel subtitleLabel2 = new JLabel("Muhammad Eman Ejaz (20034038)");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(Color.GRAY);

        subtitleLabel2.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel2.setForeground(Color.GRAY);
        
        // Instructions for user
        JLabel instructionLabel = new JLabel("Please select your preferred interface:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add spacing between components
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitleLabel);
        mainPanel.add(subtitleLabel2);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(instructionLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Button panel for interface options
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        
        // Set window size and center on screen
        pack();
        setLocationRelativeTo(null);
        
        // Set minimum size to prevent window from being too small
        setMinimumSize(new Dimension(450, 300));
    }
    
    /**
     * Creates the button panel with interface selection options.
     * 
     * @return JPanel containing the interface selection buttons
     */
    private JPanel createButtonPanel() {
        // Create panel with vertical layout for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); // Transparent background
        
        // GUI Interface Button
        JButton guiButton = new JButton("Graphical User Interface (GUI)");
        guiButton.setFont(new Font("Arial", Font.PLAIN, 14));
        guiButton.setPreferredSize(new Dimension(300, 50));
        guiButton.setMaximumSize(new Dimension(300, 50));
        guiButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guiButton.setBackground(new Color(34, 139, 34)); // Forest green background
        guiButton.setForeground(Color.WHITE);
        guiButton.setOpaque(true);
        guiButton.setFocusPainted(false); // Remove focus border
        guiButton.setBorder(BorderFactory.createRaisedBevelBorder());
        guiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add tooltip for better user experience
        guiButton.setToolTipText("Launch the graphical interface with windows, buttons, and visual elements");
        
        // Action listener for GUI button
        guiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the selector window
                dispose();
                
                // Launch GUI interface in a separate thread to prevent blocking
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Create and show the GUI interface
                            MemberManagementGUI gui = new MemberManagementGUI();
                            gui.setVisible(true);
                        } catch (Exception ex) {
                            // Handle any errors in GUI creation
                            JOptionPane.showMessageDialog(null, 
                                "Error launching GUI: " + ex.getMessage(), 
                                "Launch Error", 
                                JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        
        // Text-Based Interface Button
        JButton textButton = new JButton("Text-Based Interface (Console)");
        textButton.setFont(new Font("Arial", Font.PLAIN, 14));
        textButton.setPreferredSize(new Dimension(300, 50));
        textButton.setMaximumSize(new Dimension(300, 50));
        textButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        textButton.setBackground(new Color(25, 25, 112)); // Midnight blue background
        textButton.setForeground(Color.WHITE);
        textButton.setOpaque(true);
        textButton.setFocusPainted(false);
        textButton.setBorder(BorderFactory.createRaisedBevelBorder());
        textButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add tooltip for better user experience
        textButton.setToolTipText("Launch the traditional command-line interface");
        
        // Action listener for text interface button
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the selector window
                dispose();
                
                // Launch text interface in a separate thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Launch the existing text-based system
                            MemberManagementSystem.main(new String[0]);
                        } catch (Exception ex) {
                            // Handle any errors in text interface creation
                            System.err.println("Error launching text interface: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        
        // Exit Button for users who want to quit
        JButton exitButton = new JButton("Exit Application");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 12));
        exitButton.setPreferredSize(new Dimension(200, 35));
        exitButton.setMaximumSize(new Dimension(200, 35));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setBackground(new Color(178, 34, 34)); // Firebrick red background
        exitButton.setForeground(Color.WHITE);
        exitButton.setOpaque(true);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Action listener for exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Confirm exit with user
                int result = JOptionPane.showConfirmDialog(
                    InterfaceSelector.this,
                    "Are you sure you want to exit the application?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0); // Exit the application
                }
            }
        });
        
        // Add buttons to panel with spacing
        panel.add(guiButton);
        panel.add(Box.createVerticalStrut(15)); // Space between buttons
        panel.add(textButton);
        panel.add(Box.createVerticalStrut(25)); // More space before exit button
        panel.add(exitButton);
        
        return panel;
    }
    
    /**
     * Main method - Entry point for the application.
     * Creates and displays the interface selector window.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use default look and feel for consistency across platforms
        
        // Create and show the interface selector on the Event Dispatch Thread
        // This is the proper way to create Swing GUIs
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    InterfaceSelector selector = new InterfaceSelector();
                    selector.setVisible(true);
                } catch (Exception e) {
                    // Handle any unexpected errors during startup
                    JOptionPane.showMessageDialog(null, 
                        "Error starting application: " + e.getMessage(), 
                        "Startup Error", 
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}