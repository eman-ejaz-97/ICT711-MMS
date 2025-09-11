package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import manager.MemberManager;
import models.*;
import constants.Constants;

/**
 * Dialog for adding new members to the system.
 * 
 * Provides comprehensive form for collecting member information:
 * - Basic details (ID, name, email, phone)
 * - Member type selection with type-specific fields
 * - Input validation and error handling
 * - User feedback through status messages
 * 
 * Supports all member types:
 * - Regular Member: Basic membership
 * - Premium Member: Additional trainer and session fields
 * - Student Member: Student ID and university fields
 */
public class AddMemberDialog extends JDialog {
    private MemberManager manager;
    private boolean successful = false;
    
    // Form components
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> memberTypeCombo;
    
    // Type-specific fields
    private JTextField trainerField;
    private JTextField sessionsField;
    private JTextField studentIdField;
    private JTextField universityField;
    
    // Panels for type-specific fields
    private JPanel premiumPanel;
    private JPanel studentPanel;
    
    private JButton addButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    
    /**
     * Constructs the Add Member dialog.
     * 
     * @param parent parent frame
     * @param manager member manager for adding members
     */
    public AddMemberDialog(JFrame parent, MemberManager manager) {
        super(parent, "Add New Member", true);
        this.manager = manager;
        initializeComponents();
        setupUI();
        setupEventHandlers();
    }
    
    /**
     * Initializes all form components.
     */
    private void initializeComponents() {
        // Basic fields
        idField = new JTextField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        
        // Member type selection
        memberTypeCombo = new JComboBox<>(new String[]{
            "Regular Member", "Premium Member", "Student Member"
        });
        
        // Type-specific fields
        trainerField = new JTextField(15);
        sessionsField = new JTextField(5);
        studentIdField = new JTextField(15);
        universityField = new JTextField(20);
        
        // Buttons
        addButton = new JButton("Add Member");
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
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
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
        
        // Set focus to first field
        SwingUtilities.invokeLater(() -> idField.requestFocus());
    }
    
    /**
     * Creates the main form panel with all input fields.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Member ID
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Member ID:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);
        
        // First Name
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("First Name:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Last Name:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);
        
        // Email
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Email:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // Phone
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phone:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);
        
        // Member Type
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Member Type:*"), gbc);
        gbc.gridx = 1;
        formPanel.add(memberTypeCombo, gbc);
        
        // Type-specific panels
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel typeSpecificContainer = new JPanel(new CardLayout());
        
        // Premium member panel
        premiumPanel = createPremiumPanel();
        typeSpecificContainer.add(premiumPanel, "Premium");
        
        // Student member panel
        studentPanel = createStudentPanel();
        typeSpecificContainer.add(studentPanel, "Student");
        
        // Empty panel for regular members
        typeSpecificContainer.add(new JPanel(), "Regular");
        
        formPanel.add(typeSpecificContainer, gbc);
        
        // Status label
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);
        
        // Add member type change listener to show/hide type-specific fields
        memberTypeCombo.addActionListener(e -> {
            CardLayout cl = (CardLayout) typeSpecificContainer.getLayout();
            String selectedType = (String) memberTypeCombo.getSelectedItem();
            if (selectedType.contains("Premium")) {
                cl.show(typeSpecificContainer, "Premium");
            } else if (selectedType.contains("Student")) {
                cl.show(typeSpecificContainer, "Student");
            } else {
                cl.show(typeSpecificContainer, "Regular");
            }
            pack(); // Resize dialog to fit content
        });
        
        return formPanel;
    }
    
    /**
     * Creates the panel for premium member specific fields.
     */
    private JPanel createPremiumPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Premium Member Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Trainer Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Trainer Name:"), gbc);
        gbc.gridx = 1;
        panel.add(trainerField, gbc);
        
        // Sessions per Month
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Sessions/Month:"), gbc);
        gbc.gridx = 1;
        panel.add(sessionsField, gbc);
        
        // Add helpful text
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel helpText = new JLabel("<html><i>Premium members get 15% discount if goal achieved<br/>and bonus if performance > 8</i></html>");
        helpText.setForeground(Color.GRAY);
        panel.add(helpText, gbc);
        
        return panel;
    }
    
    /**
     * Creates the panel for student member specific fields.
     */
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Member Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        panel.add(studentIdField, gbc);
        
        // University
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("University:"), gbc);
        gbc.gridx = 1;
        panel.add(universityField, gbc);
        
        // Add helpful text
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel helpText = new JLabel("<html><i>Student members get 30% discount<br/>and bonus if goal achieved</i></html>");
        helpText.setForeground(Color.GRAY);
        panel.add(helpText, gbc);
        
        return panel;
    }
    
    /**
     * Creates the button panel with action buttons.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addButton.setPreferredSize(new Dimension(120, 35));
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setOpaque(true);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setOpaque(true);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
    /**
     * Sets up event handlers for buttons and form interactions.
     */
    private void setupEventHandlers() {
        // Add button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });
        
        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Enter key in text fields triggers add action
        ActionListener enterAction = e -> addMember();
        idField.addActionListener(enterAction);
        firstNameField.addActionListener(enterAction);
        lastNameField.addActionListener(enterAction);
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
        
        // Set default button
        getRootPane().setDefaultButton(addButton);
    }
    
    /**
     * Validates input and adds the new member.
     */
    private void addMember() {
        try {
            // Clear previous status
            updateStatus("", Color.BLUE);
            
            // Validate required fields
            if (!validateRequiredFields()) {
                return;
            }
            
            // Get form values
            String id = idField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String memberType = (String) memberTypeCombo.getSelectedItem();
            
            // Validate business rules
            if (!validateBusinessRules(id, email)) {
                return;
            }
            
            // Create member based on type
            Member newMember = createMemberByType(memberType, id, firstName, lastName, email, phone);
            
            if (newMember != null) {
                // Add member to manager
                manager.addMember(newMember);
                
                // Show success message
                updateStatus("Member added successfully!", new Color(76, 175, 80));
                successful = true;
                
                // Close dialog after short delay
                javax.swing.Timer timer = new javax.swing.Timer(1500, e -> dispose());
                timer.setRepeats(false);
                timer.start();
            }
            
        } catch (Exception e) {
            updateStatus("Error adding member: " + e.getMessage(), Color.RED);
        }
    }
    
    /**
     * Validates that all required fields are filled.
     */
    private boolean validateRequiredFields() {
        if (idField.getText().trim().isEmpty()) {
            updateStatus("Member ID is required", Color.RED);
            idField.requestFocus();
            return false;
        }
        
        if (firstNameField.getText().trim().isEmpty()) {
            updateStatus("First name is required", Color.RED);
            firstNameField.requestFocus();
            return false;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            updateStatus("Last name is required", Color.RED);
            lastNameField.requestFocus();
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            updateStatus("Email is required", Color.RED);
            emailField.requestFocus();
            return false;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            updateStatus("Phone is required", Color.RED);
            phoneField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Validates business rules (unique ID, valid email format, etc.).
     */
    private boolean validateBusinessRules(String id, String email) {
        // Check if member ID already exists
        if (manager.findMemberById(id) != null) {
            updateStatus("Member ID already exists", Color.RED);
            idField.requestFocus();
            return false;
        }
        
        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            updateStatus("Please enter a valid email address", Color.RED);
            emailField.requestFocus();
            return false;
        }
        
        // Validate type-specific fields
        String memberType = (String) memberTypeCombo.getSelectedItem();
        
        if (memberType.contains("Premium")) {
            try {
                String sessionsText = sessionsField.getText().trim();
                if (!sessionsText.isEmpty()) {
                    int sessions = Integer.parseInt(sessionsText);
                    if (sessions < 1 || sessions > 20) {
                        updateStatus("Sessions per month must be between 1 and 20", Color.RED);
                        sessionsField.requestFocus();
                        return false;
                    }
                }
            } catch (NumberFormatException e) {
                updateStatus("Sessions per month must be a valid number", Color.RED);
                sessionsField.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Creates a member object based on the selected type.
     */
    private Member createMemberByType(String memberType, String id, String firstName, String lastName, String email, String phone) {
        Member member = null;
        
        if (memberType.contains("Regular")) {
            member = new RegularMember(id, firstName, lastName, email, phone);
            
        } else if (memberType.contains("Premium")) {
            String trainer = trainerField.getText().trim();
            if (trainer.isEmpty()) {
                trainer = Constants.DEFAULT_TRAINER_NAME;
            }
            
            int sessions = Constants.DEFAULT_SESSIONS_PER_MONTH;
            String sessionsText = sessionsField.getText().trim();
            if (!sessionsText.isEmpty()) {
                try {
                    sessions = Integer.parseInt(sessionsText);
                } catch (NumberFormatException e) {
                    sessions = Constants.DEFAULT_SESSIONS_PER_MONTH;
                }
            }
            
            member = new PremiumMember(id, firstName, lastName, email, phone, trainer, sessions);
            
        } else if (memberType.contains("Student")) {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()) {
                studentId = Constants.DEFAULT_STUDENT_ID;
            }
            
            String university = universityField.getText().trim();
            if (university.isEmpty()) {
                university = Constants.DEFAULT_UNIVERSITY;
            }
            
            member = new StudentMember(id, firstName, lastName, email, phone, studentId, university);
        }
        
        return member;
    }
    
    /**
     * Updates the status label with message and color.
     */
    private void updateStatus(String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            statusLabel.setForeground(color);
        });
    }
    
    /**
     * Returns whether the member was successfully added.
     */
    public boolean wasSuccessful() {
        return successful;
    }
}