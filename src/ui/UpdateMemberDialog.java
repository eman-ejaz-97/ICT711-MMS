package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import manager.MemberManager;
import models.Member;
import constants.Constants;

/**
 * Dialog for updating existing member information.
 * 
 * Allows modification of:
 * - Contact information (email, phone)
 * - Performance metrics (rating, goal achievement)
 * - Member-specific details where applicable
 * 
 * Provides validation and user feedback for all updates.
 */
public class UpdateMemberDialog extends JDialog {
    private MemberManager manager;
    private Member member;
    private boolean successful = false;
    
    // Form components
    private JLabel memberInfoLabel;
    private JTextField emailField;
    private JTextField phoneField;
    private JSpinner performanceSpinner;
    private JCheckBox goalAchievedBox;
    
    private JButton updateButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    
    /**
     * Constructs the Update Member dialog.
     * 
     * @param parent parent frame
     * @param manager member manager
     * @param member member to update
     */
    public UpdateMemberDialog(JFrame parent, MemberManager manager, Member member) {
        super(parent, "Update Member", true);
        this.manager = manager;
        this.member = member;
        initializeComponents();
        setupUI();
        setupEventHandlers();
        populateFields();
    }
    
    /**
     * Initializes form components.
     */
    private void initializeComponents() {
        // Member info display
        memberInfoLabel = new JLabel();
        
        // Editable fields
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        
        // Performance rating spinner (0-10)
        performanceSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        
        // Goal achievement checkbox
        goalAchievedBox = new JCheckBox("Goal Achieved");
        
        // Buttons
        updateButton = new JButton("Update Member");
        cancelButton = new JButton("Cancel");
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.BLUE);
    }
    
    /**
     * Sets up the user interface layout.
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    /**
     * Creates the header panel with member information.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBorder(BorderFactory.createTitledBorder("Member Information"));
        
        memberInfoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(memberInfoLabel);
        
        return headerPanel;
    }
    
    /**
     * Creates the form panel with editable fields.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Update Fields"));
        
        // GridBagConstraints is tricky - had to google this a lot!
        // It controls how components are positioned and sized in the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // padding around each component
        gbc.anchor = GridBagConstraints.WEST; // align stuff to the left
        
        int row = 0; // keep track of which row we're adding to
        
        // Email field - took forever to get the sizing right
        gbc.gridx = 0; gbc.gridy = row; // first column, current row
        gbc.fill = GridBagConstraints.NONE; // label doesn't need to stretch
        gbc.weightx = 0.0; // don't give extra space to the label
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; // move to second column for the text field
        gbc.fill = GridBagConstraints.HORIZONTAL; // make text field stretch horizontally
        gbc.weightx = 1.0; // give all extra space to the text field
        emailField.setPreferredSize(new Dimension(250, 25)); // had to set this or field was tiny!
        formPanel.add(emailField, gbc);
        
        // Phone field - same pattern as email
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        phoneField.setPreferredSize(new Dimension(250, 25)); // same size as email field
        formPanel.add(phoneField, gbc);
        
        // Performance Rating - using a spinner for number input
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Performance Rating:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE; // spinner shouldn't stretch
        gbc.weightx = 0.0;
        // Put spinner and label in their own panel so they stay together
        JPanel performancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        performanceSpinner.setPreferredSize(new Dimension(60, 25)); // make spinner big enough to use
        performancePanel.add(performanceSpinner);
        performancePanel.add(new JLabel(" (0-10)")); // helpful reminder for user
        formPanel.add(performancePanel, gbc);
        
        // Goal Achievement checkbox
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Goal Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE; // checkbox doesn't need to stretch
        gbc.weightx = 0.0;
        formPanel.add(goalAchievedBox, gbc);
        
        // Status label for showing messages to user
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2; // span across both columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(statusLabel, gbc);
        
        return formPanel;
    }
    
    /**
     * Creates the button panel.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        updateButton.setPreferredSize(new Dimension(130, 35));
        updateButton.setBackground(Color.LIGHT_GRAY);
        updateButton.setForeground(Color.BLACK);
        updateButton.setFont(new Font("Arial", Font.BOLD, 12));
        updateButton.setOpaque(true);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setOpaque(true);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
    /**
     * Sets up event handlers.
     */
    private void setupEventHandlers() {
        updateButton.addActionListener(e -> updateMember());
        cancelButton.addActionListener(e -> dispose());
        
        // Enter key triggers update
        ActionListener enterAction = e -> updateMember();
        emailField.addActionListener(enterAction);
        phoneField.addActionListener(enterAction);
        
        // Escape key closes dialog
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        getRootPane().setDefaultButton(updateButton);
    }
    
    /**
     * Populates form fields with current member data.
     */
    private void populateFields() {
        // Set member info display
        memberInfoLabel.setText(String.format("Updating: %s (ID: %s) - %s", 
            member.getFullName(), member.getMemberId(), member.getMemberType()));
        
        // Populate editable fields
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone());
        performanceSpinner.setValue(member.getPerformanceRating());
        goalAchievedBox.setSelected(member.isGoalAchieved());
        
        // Focus on first editable field
        SwingUtilities.invokeLater(() -> emailField.requestFocus());
    }
    
    /**
     * Validates input and updates the member.
     */
    private void updateMember() {
        try {
            updateStatus("", Color.BLUE);
            
            // Validate input
            if (!validateInput()) {
                return;
            }
            
            // Prepare update map
            Map<String, Object> updates = new HashMap<>();
            
            // Check for changes and add to update map
            String newEmail = emailField.getText().trim();
            if (!newEmail.equals(member.getEmail())) {
                updates.put(Constants.UPDATE_KEY_EMAIL, newEmail);
            }
            
            String newPhone = phoneField.getText().trim();
            if (!newPhone.equals(member.getPhone())) {
                updates.put(Constants.UPDATE_KEY_PHONE, newPhone);
            }
            
            int newRating = (Integer) performanceSpinner.getValue();
            if (newRating != member.getPerformanceRating()) {
                updates.put(Constants.UPDATE_KEY_PERFORMANCE_RATING, newRating);
            }
            
            boolean newGoalStatus = goalAchievedBox.isSelected();
            if (newGoalStatus != member.isGoalAchieved()) {
                updates.put(Constants.UPDATE_KEY_GOAL_ACHIEVED, newGoalStatus);
            }
            
            // Check if any changes were made
            if (updates.isEmpty()) {
                updateStatus("No changes detected", Color.ORANGE);
                return;
            }
            
            // Apply updates
            if (manager.updateMember(member.getMemberId(), updates)) {
                updateStatus("Member updated successfully!", new Color(76, 175, 80));
                successful = true;
                
                // Close dialog after short delay
                javax.swing.Timer timer = new javax.swing.Timer(1500, e -> dispose());
                timer.setRepeats(false);
                timer.start();
            } else {
                updateStatus("Failed to update member", Color.RED);
            }
            
        } catch (Exception e) {
            updateStatus("Error updating member: " + e.getMessage(), Color.RED);
        }
    }
    
    /**
     * Validates user input.
     */
    private boolean validateInput() {
        // Email validation
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            updateStatus("Email cannot be empty", Color.RED);
            emailField.requestFocus();
            return false;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            updateStatus("Please enter a valid email address", Color.RED);
            emailField.requestFocus();
            return false;
        }
        
        // Phone validation
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            updateStatus("Phone cannot be empty", Color.RED);
            phoneField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Updates the status label.
     */
    private void updateStatus(String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            statusLabel.setForeground(color);
        });
    }
    
    /**
     * Returns whether the update was successful.
     */
    public boolean wasSuccessful() {
        return successful;
    }
}