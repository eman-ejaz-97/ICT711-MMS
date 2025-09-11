package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Initial interface selector allowing users to choose between GUI and Text-based interfaces.
 * 
 * Provides:
 * - Interface mode selection (GUI or Text-based)
 * - Clean separation between interface types
 * - User-friendly startup experience
 * 
 * Requirements addressed:
 * - Allow users to choose their preferred mode of interaction
 * - Provide intuitive way to access both interface types
 */
public class InterfaceSelector extends JFrame {
    private static final String TITLE = "Member Management System - Interface Selection";
    private static final String WELCOME_MESSAGE = "Welcome to the Member Management System";
    private static final String INSTRUCTION_MESSAGE = "Please select your preferred interface mode:";
    
    /**
     * Constructs and displays the interface selector dialog.
     */
    public InterfaceSelector() {
        initializeUI();
    }
    
    /**
     * Initializes the user interface components and layout.
     */
    private void initializeUI() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center on screen
    }
    
    /**
     * Creates the header panel with welcome message and instructions.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        headerPanel.setBackground(Color.WHITE);
        
        // Welcome label
        JLabel welcomeLabel = new JLabel(WELCOME_MESSAGE, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLACK); // Pure black for maximum contrast
        
        // Instruction label
        JLabel instructionLabel = new JLabel(INSTRUCTION_MESSAGE, JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(Color.BLACK); // Pure black text for maximum readability
        
        // Add some spacing
        JLabel spacer = new JLabel(" ");
        
        headerPanel.add(welcomeLabel);
        headerPanel.add(instructionLabel);
        headerPanel.add(spacer);
        
        return headerPanel;
    }
    
    /**
     * Creates the button panel with interface selection options.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        // GUI Mode Button - this is the fancy windowed interface
        JButton guiButton = new JButton("Graphical User Interface (GUI)");
        guiButton.setPreferredSize(new Dimension(350, 60));
        guiButton.setFont(new Font("Arial", Font.BOLD, 16));
        guiButton.setBackground(Color.LIGHT_GRAY);
        guiButton.setForeground(Color.BLACK);
        guiButton.setOpaque(true);
        guiButton.setFocusPainted(false);
        guiButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        guiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Text Mode Button
        JButton textButton = new JButton("Text-Based Interface (TBI)");
        textButton.setPreferredSize(new Dimension(350, 60));
        textButton.setFont(new Font("Arial", Font.BOLD, 16));
        textButton.setBackground(Color.LIGHT_GRAY);
        textButton.setForeground(Color.BLACK);
        textButton.setOpaque(true);
        textButton.setFocusPainted(false);
        textButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        textButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listeners
        guiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchGUIInterface();
            }
        });
        
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchTextInterface();
            }
        });
        
        buttonPanel.add(guiButton);
        buttonPanel.add(textButton);
        
        return buttonPanel;
    }
    
    /**
     * Creates the footer panel with additional information.
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);
        
        JLabel footerLabel = new JLabel("© 2025 ICT711 Member Management System");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.BLACK);
        
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    /**
     * Launches the GUI interface and closes the selector.
     */
    private void launchGUIInterface() {
        SwingUtilities.invokeLater(() -> {
            try {
                new MemberManagementGUI().setVisible(true);
                dispose(); // Close selector window
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error launching GUI interface: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Launches the text-based interface and closes the selector.
     */
    private void launchTextInterface() {
        dispose(); // Close selector window
        
        // Launch text interface in a separate thread to avoid blocking
        new Thread(() -> {
            MemberManagementSystem.main(new String[]{});
        }).start();
    }
    
    /**
     * Application entry point with interface selection.
     */
    public static void main(String[] args) {
        // Set system look and feel for better native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If system L&F fails, use cross-platform L&F
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Could not set Look and Feel: " + ex.getMessage());
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new InterfaceSelector().setVisible(true);
        });
    }
}