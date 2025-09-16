package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;
import manager.MemberManager;
import models.*;
import constants.Constants;

/**
 * GUI interface for the Member Management System.
 * 
 * Provides intuitive graphical interface for:
 * - All CRUD operations (Create, Read, Update, Delete)
 * - File operations (Load/Save)
 * - Search and sort functionality
 * - Performance management
 * - Statistical reporting
 * 
 * Maintains same functionality as text-based interface while providing
 * better user experience through visual components.
 */
public class MemberManagementGUI extends JFrame {
    private MemberManager manager;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JComboBox<String> sortCombo;
    
    /**
     * Constructs the main GUI window and initializes all components.
     */
    public MemberManagementGUI() {
        this.manager = new MemberManager();
        initializeGUI();
        loadDefaultData();
    }
    
    /**
     * Initializes all GUI components and layout.
     */
    private void initializeGUI() {
        setTitle("Member Management System - ICT711");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createMenuBar();
        createMainPanel();
        createTablePanel();
        createButtonPanel();
        
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
    }
    
    /**
     * Creates the menu bar with file operations.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem loadItem = new JMenuItem("Load from File");
        loadItem.addActionListener(e -> loadFromFile());
        
        JMenuItem saveItem = new JMenuItem("Save to File");
        saveItem.addActionListener(e -> saveToFile());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Creates the main search and sort panel.
     */
    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Search components
        mainPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        mainPanel.add(searchField);
        
        searchTypeCombo = new JComboBox<>(new String[]{"All", "By ID", "By Name", "By Performance"});
        mainPanel.add(searchTypeCombo);
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        mainPanel.add(searchButton);
        
        // Sort components
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(new JLabel("Sort by:"));
        sortCombo = new JComboBox<>(new String[]{"Member ID", "Name", "Type", "Performance", "Fee"});
        sortCombo.addActionListener(e -> sortMembers());
        mainPanel.add(sortCombo);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTable());
        mainPanel.add(refreshButton);
        
        add(mainPanel, BorderLayout.NORTH);
    }
    
    /**
     * Creates the table to display member information.
     */
    private void createTablePanel() {
        String[] columnNames = {"ID", "Name", "Type", "Email", "Phone", "Rating", "Goal", "Fee"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setPreferredSize(new Dimension(950, 400));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the button panel with all action buttons.
     */
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton addButton = new JButton("Add Member");
        addButton.addActionListener(e -> showAddMemberDialog());
        
        JButton updateButton = new JButton("Update Member");
        updateButton.addActionListener(e -> showUpdateMemberDialog());
        
        JButton deleteButton = new JButton("Delete Member");
        deleteButton.addActionListener(e -> deleteMember());
        
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> showMemberDetails());
        
        JButton performanceButton = new JButton("Performance Management");
        performanceButton.addActionListener(e -> showPerformanceDialog());
        
        JButton statisticsButton = new JButton("Statistics");
        statisticsButton.addActionListener(e -> showStatistics());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(performanceButton);
        buttonPanel.add(statisticsButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Loads default sample data into the system.
     */
    private void loadDefaultData() {
        // First create initial data file if it doesn't exist
        createInitialDataFile();
        
        try {
            manager.loadFromFile(Constants.DEFAULT_FILE_NAME);
            refreshTable();
        } catch (IOException e) {
            // If file doesn't exist, that's okay - we'll start with empty data
            System.out.println("Starting with empty data: " + e.getMessage());
        }
    }
    
    /**
     * Creates initial CSV data file with sample members if file doesn't exist.
     * Provides 10 sample members across all membership types for testing.
     */
    private void createInitialDataFile() {
        java.io.File file = new java.io.File(Constants.DEFAULT_FILE_NAME);
        if (!file.exists()) {
            try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file))) {
                writer.println(Constants.CSV_HEADER);
                
                // Add 10 initial sample members
                writer.println("Regular,M001,Eman,Ejaz,eman.ejaz@email.com,466-0101,7,true,,");
                writer.println("Premium,M002,Sajina,Rana,sajina.rana@email.com,466-0102,9,true,Elina Trainer,8");
                writer.println("Student,M003,Waqas,Iqbal,waqas.iqbal@email.com,466-0103,6,false,STU2024001,State University");
                writer.println("Regular,M004,Sravanth,Rao,sravanth.ra@email.com,466-0104,5,false,,");
                writer.println("Premium,M005,Jessica,Wilson,jessica.w@email.com,466-0105,8,true,Lisa Coach,6");
                writer.println("Student,M006,David,Martinez,david.m@email.com,466-0106,7,true,STU2024002,Tech College");
                writer.println("Regular,M007,Maria,Garcia,maria.g@email.com,466-0107,4,false,,");
                writer.println("Premium,M008,James,Anderson,james.a@email.com,466-0108,10,true,Tom Trainer,10");
                writer.println("Student,M009,Sophie,Taylor,sophie.t@email.com,466-0109,8,true,STU2024003,City University");
                writer.println("Regular,M010,Robert,Thomas,robert.t@email.com,466-0110,6,false,,");
                
                System.out.println("Initial data file '" + Constants.DEFAULT_FILE_NAME + "' created with 10 sample members.");
            } catch (IOException e) {
                System.out.println("Could not create initial data file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Refreshes the table with current member data.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Member> members = manager.getAllMembers();
        
        for (Member member : members) {
            Object[] row = {
                member.getMemberId(),
                member.getFullName(),
                member.getMemberType(),
                member.getEmail(),
                member.getPhone(),
                member.getPerformanceRating() + "/10",
                member.isGoalAchieved() ? "Yes" : "No",
                String.format("$%.2f", member.calculateMonthlyFee())
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Performs search based on selected criteria.
     */
    private void performSearch() {
        String searchText = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        if (searchText.isEmpty() && !searchType.equals("All")) {
            JOptionPane.showMessageDialog(this, "Please enter search text.", "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        List<Member> results = new ArrayList<>();
        
        switch (searchType) {
            case "All":
                results = manager.getAllMembers();
                break;
            case "By ID":
                Member member = manager.findMemberById(searchText);
                if (member != null) results.add(member);
                break;
            case "By Name":
                results = manager.findMembersByName(searchText);
                break;
            case "By Performance":
                try {
                    int rating = Integer.parseInt(searchText);
                    results = manager.findMembersByPerformance(rating);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number for performance rating.", "Search Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;
        }
        
        for (Member m : results) {
            Object[] row = {
                m.getMemberId(),
                m.getFullName(),
                m.getMemberType(),
                m.getEmail(),
                m.getPhone(),
                m.getPerformanceRating() + "/10",
                m.isGoalAchieved() ? "Yes" : "No",
                String.format("$%.2f", m.calculateMonthlyFee())
            };
            tableModel.addRow(row);
        }
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No members found matching the search criteria.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Sorts members based on selected criteria.
     */
    private void sortMembers() {
        String sortBy = (String) sortCombo.getSelectedItem();
        List<Member> members = manager.getAllMembers();
        
        switch (sortBy) {
            case "Member ID":
                members.sort(Comparator.comparing(Member::getMemberId));
                break;
            case "Name":
                members.sort(Comparator.comparing(Member::getFullName));
                break;
            case "Type":
                members.sort(Comparator.comparing(Member::getMemberType));
                break;
            case "Performance":
                members.sort(Comparator.comparing(Member::getPerformanceRating).reversed());
                break;
            case "Fee":
                members.sort(Comparator.comparing(Member::calculateMonthlyFee).reversed());
                break;
        }
        
        tableModel.setRowCount(0);
        for (Member member : members) {
            Object[] row = {
                member.getMemberId(),
                member.getFullName(),
                member.getMemberType(),
                member.getEmail(),
                member.getPhone(),
                member.getPerformanceRating() + "/10",
                member.isGoalAchieved() ? "Yes" : "No",
                String.format("$%.2f", member.calculateMonthlyFee())
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Shows dialog to add a new member.
     */
    private void showAddMemberDialog() {
        JDialog dialog = new JDialog(this, "Add New Member", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Member type selection
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Member Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Regular", "Premium", "Student"});
        dialog.add(typeCombo, gbc);
        
        // Common fields
        JTextField idField = new JTextField(15);
        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(firstNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(lastNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        dialog.add(phoneField, gbc);
        
        // Type-specific fields
        JTextField trainerField = new JTextField(15);
        JTextField sessionsField = new JTextField(15);
        JTextField studentIdField = new JTextField(15);
        JTextField universityField = new JTextField(15);
        
        JLabel trainerLabel = new JLabel("Trainer Name:");
        JLabel sessionsLabel = new JLabel("Sessions/Month:");
        JLabel studentIdLabel = new JLabel("Student ID:");
        JLabel universityLabel = new JLabel("University:");
        
        gbc.gridx = 0; gbc.gridy = 6;
        dialog.add(trainerLabel, gbc);
        gbc.gridx = 1;
        dialog.add(trainerField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        dialog.add(sessionsLabel, gbc);
        gbc.gridx = 1;
        dialog.add(sessionsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        dialog.add(studentIdLabel, gbc);
        gbc.gridx = 1;
        dialog.add(studentIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9;
        dialog.add(universityLabel, gbc);
        gbc.gridx = 1;
        dialog.add(universityField, gbc);
        
        // Initially hide type-specific fields
        trainerLabel.setVisible(false);
        trainerField.setVisible(false);
        sessionsLabel.setVisible(false);
        sessionsField.setVisible(false);
        studentIdLabel.setVisible(false);
        studentIdField.setVisible(false);
        universityLabel.setVisible(false);
        universityField.setVisible(false);
        
        // Type selection listener
        typeCombo.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            boolean isPremium = "Premium".equals(type);
            boolean isStudent = "Student".equals(type);
            
            trainerLabel.setVisible(isPremium);
            trainerField.setVisible(isPremium);
            sessionsLabel.setVisible(isPremium);
            sessionsField.setVisible(isPremium);
            studentIdLabel.setVisible(isStudent);
            studentIdField.setVisible(isStudent);
            universityLabel.setVisible(isStudent);
            universityField.setVisible(isStudent);
            
            dialog.pack();
        });
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String type = (String) typeCombo.getSelectedItem();
                
                if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Member newMember = null;
                
                switch (type) {
                    case "Regular":
                        newMember = new RegularMember(id, firstName, lastName, email, phone);
                        break;
                    case "Premium":
                        String trainer = trainerField.getText().trim();
                        String sessionsStr = sessionsField.getText().trim();
                        if (trainer.isEmpty() || sessionsStr.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Please fill in trainer and sessions fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        int sessions = Integer.parseInt(sessionsStr);
                        newMember = new PremiumMember(id, firstName, lastName, email, phone, trainer, sessions);
                        break;
                    case "Student":
                        String studentId = studentIdField.getText().trim();
                        String university = universityField.getText().trim();
                        if (studentId.isEmpty() || university.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Please fill in student ID and university fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        newMember = new StudentMember(id, firstName, lastName, email, phone, studentId, university);
                        break;
                }
                
                manager.addMember(newMember);
                refreshTable();
                dialog.dispose();
                
                int result = JOptionPane.showConfirmDialog(this, "Member added successfully! Save to file?", "Save Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    saveToFile();
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for sessions.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error adding member: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    /**
     * Shows dialog to update selected member.
     */
    private void showUpdateMemberDialog() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String memberId = (String) tableModel.getValueAt(selectedRow, 0);
        Member member = manager.findMemberById(memberId);
        
        if (member == null) {
            JOptionPane.showMessageDialog(this, "Member not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Update Member: " + member.getFullName(), true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField emailField = new JTextField(member.getEmail(), 15);
        JTextField phoneField = new JTextField(member.getPhone(), 15);
        JTextField ratingField = new JTextField(String.valueOf(member.getPerformanceRating()), 15);
        JCheckBox goalCheckBox = new JCheckBox("Goal Achieved", member.isGoalAchieved());
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        dialog.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        dialog.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Performance Rating (0-10):"), gbc);
        gbc.gridx = 1;
        dialog.add(ratingField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(goalCheckBox, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");
        
        updateButton.addActionListener(e -> {
            try {
                Map<String, Object> updates = new HashMap<>();
                updates.put(Constants.UPDATE_KEY_EMAIL, emailField.getText().trim());
                updates.put(Constants.UPDATE_KEY_PHONE, phoneField.getText().trim());
                updates.put(Constants.UPDATE_KEY_PERFORMANCE_RATING, Integer.parseInt(ratingField.getText().trim()));
                updates.put(Constants.UPDATE_KEY_GOAL_ACHIEVED, goalCheckBox.isSelected());
                
                if (manager.updateMember(memberId, updates)) {
                    refreshTable();
                    dialog.dispose();
                    
                    int result = JOptionPane.showConfirmDialog(this, "Member updated successfully! Save to file?", "Save Confirmation", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        saveToFile();
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update member.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid performance rating (0-10).", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    /**
     * Deletes the selected member after confirmation.
     */
    private void deleteMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String memberId = (String) tableModel.getValueAt(selectedRow, 0);
        String memberName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete member: " + memberName + " (ID: " + memberId + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (manager.removeMember(memberId)) {
                refreshTable();
                
                int saveResult = JOptionPane.showConfirmDialog(this, "Member deleted successfully! Save to file?", "Save Confirmation", JOptionPane.YES_NO_OPTION);
                if (saveResult == JOptionPane.YES_OPTION) {
                    saveToFile();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete member.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Shows detailed information for selected member.
     */
    private void showMemberDetails() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to view details.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String memberId = (String) tableModel.getValueAt(selectedRow, 0);
        Member member = manager.findMemberById(memberId);
        
        if (member == null) {
            JOptionPane.showMessageDialog(this, "Member not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String details = member.toString() + "\n\n" + member.generatePerformanceReport();
        
        JTextArea textArea = new JTextArea(details);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Member Details: " + member.getFullName(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows performance management dialog.
     */
    private void showPerformanceDialog() {
        JDialog dialog = new JDialog(this, "Performance Management", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton appreciationButton = new JButton("Generate Appreciation Letters");
        appreciationButton.addActionListener(e -> showAppreciationLetters());
        
        JButton reminderButton = new JButton("Generate Reminder Letters");
        reminderButton.addActionListener(e -> showReminderLetters());
        
        JButton feeButton = new JButton("View Fee Details");
        feeButton.addActionListener(e -> showFeeDetails());
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(appreciationButton);
        buttonPanel.add(reminderButton);
        buttonPanel.add(feeButton);
        buttonPanel.add(closeButton);
        
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    /**
     * Shows appreciation letters for high performers.
     */
    private void showAppreciationLetters() {
        StringBuilder letters = new StringBuilder();
        for (Member member : manager.getAllMembers()) {
            String letter = manager.generateAppreciationLetter(member);
            if (letter != null) {
                letters.append(letter).append("\n\n" + "=".repeat(50) + "\n\n");
            }
        }
        
        if (letters.length() == 0) {
            letters.append("No members qualify for appreciation letters at this time.");
        }
        
        showTextDialog("Appreciation Letters", letters.toString());
    }
    
    /**
     * Shows reminder letters for low performers.
     */
    private void showReminderLetters() {
        StringBuilder letters = new StringBuilder();
        for (Member member : manager.getAllMembers()) {
            String letter = manager.generateReminderLetter(member);
            if (letter != null) {
                letters.append(letter).append("\n\n" + "=".repeat(50) + "\n\n");
            }
        }
        
        if (letters.length() == 0) {
            letters.append("No members need reminder letters at this time.");
        }
        
        showTextDialog("Reminder Letters", letters.toString());
    }
    
    /**
     * Shows fee details for all members.
     */
    private void showFeeDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Monthly Fee Details\n");
        details.append("=".repeat(50)).append("\n\n");
        
        double totalFees = 0;
        for (Member member : manager.getAllMembers()) {
            double fee = member.calculateMonthlyFee();
            totalFees += fee;
            details.append(String.format("%-15s | %-25s | $%.2f%n", 
                member.getMemberId(), member.getFullName(), fee));
        }
        
        details.append("\n" + "=".repeat(50)).append("\n");
        details.append(String.format("Total Monthly Revenue: $%.2f", totalFees));
        
        showTextDialog("Fee Details", details.toString());
    }
    
    /**
     * Shows statistics for all members.
     */
    private void showStatistics() {
        // Capture statistics output
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(baos));
        
        manager.displayStatistics();
        
        System.setOut(originalOut);
        String stats = baos.toString();
        
        showTextDialog("System Statistics", stats);
    }
    
    /**
     * Shows a text dialog with scrollable content.
     */
    private void showTextDialog(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Loads member data from file.
     */
    private void loadFromFile() {
        String fileName = JOptionPane.showInputDialog(this, "Enter filename to load:", Constants.DEFAULT_FILE_NAME);
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                manager.loadFromFile(fileName.trim());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Records loaded successfully!", "Load Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Saves member data to file.
     */
    private void saveToFile() {
        String fileName = JOptionPane.showInputDialog(this, "Enter filename to save:", Constants.DEFAULT_FILE_NAME);
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                manager.saveToFile(fileName.trim());
                JOptionPane.showMessageDialog(this, "Records saved successfully!", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Shows about dialog.
     */
    private void showAboutDialog() {
        String message = "This program is developed by Muhammad Eman Ejaz (ID: 20034038) as part of the Individual project.\n"
                       + "It demonstrates OOP principles including Inheritance, Polymorphism, Encapsulation, and Abstraction.";
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}