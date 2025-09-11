package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;
import manager.MemberManager;
import models.*;
import constants.Constants;
import algorithms.MemberSearcher;
import algorithms.MemberSorter;

/**
 * Main GUI interface for the Member Management System.
 * 
 * Provides comprehensive graphical interface for:
 * - Member CRUD operations (Create, Read, Update, Delete)
 * - File operations (Load, Save)
 * - Search and sort functionality
 * - Performance management
 * - Statistical reporting
 * 
 * Requirements addressed:
 * - Intuitive and user-friendly GUI
 * - All functionality from text-based interface
 * - Meaningful user feedback through dialogs
 * - Integration with existing business logic
 * - Search and sort algorithms implementation
 */
public class MemberManagementGUI extends JFrame {
    private static final String TITLE = "Member Management System - GUI";
    
    // Core components
    private MemberManager manager;
    private MemberSearcher searcher;
    private MemberSorter sorter;
    
    // GUI Components
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JComboBox<String> sortCombo;
    private JLabel statusLabel;
    
    // Table columns
    private static final String[] COLUMN_NAMES = {
        "ID", "Name", "Type", "Email", "Phone", "Performance", "Goal", "Monthly Fee"
    };
    
    /**
     * Constructs the main GUI interface.
     */
    public MemberManagementGUI() {
        initializeComponents();
        setupUI();
        loadInitialData();
    }
    
    /**
     * Initializes core components and managers.
     */
    private void initializeComponents() {
        manager = new MemberManager();
        searcher = new MemberSearcher(manager);
        sorter = new MemberSorter();
        
        // Initialize table model
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        memberTable = new JTable(tableModel);
        tableSorter = new TableRowSorter<>(tableModel);
        memberTable.setRowSorter(tableSorter);
    }
    
    /**
     * Sets up the main user interface layout and components.
     */
    private void setupUI() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create main panels
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();
        
        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Configure frame
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Set up table appearance
        setupTableAppearance();
    }
    
    /**
     * Creates the top panel with menu bar and toolbar.
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Menu bar
        JMenuBar menuBar = createMenuBar();
        topPanel.add(menuBar, BorderLayout.NORTH);
        
        // Toolbar
        JToolBar toolbar = createToolbar();
        topPanel.add(toolbar, BorderLayout.SOUTH);
        
        return topPanel;
    }
    
    /**
     * Creates the menu bar with file and operations menus.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createMenuItem("Load Data", e -> loadData()));
        fileMenu.add(createMenuItem("Save Data", e -> saveData()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", e -> System.exit(0)));
        
        // Members Menu
        JMenu membersMenu = new JMenu("Members");
        membersMenu.add(createMenuItem("Add Member", e -> showAddMemberDialog()));
        membersMenu.add(createMenuItem("Update Member", e -> updateSelectedMember()));
        membersMenu.add(createMenuItem("Delete Member", e -> deleteSelectedMember()));
        
        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        reportsMenu.add(createMenuItem("View Statistics", e -> showStatistics()));
        reportsMenu.add(createMenuItem("Performance Reports", e -> showPerformanceReports()));
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(createMenuItem("About", e -> showAboutDialog()));
        
        menuBar.add(fileMenu);
        menuBar.add(membersMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    /**
     * Creates the toolbar with quick action buttons.
     */
    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        
        // Quick action buttons
        toolbar.add(createToolbarButton("Add", "Add new member", e -> showAddMemberDialog()));
        toolbar.add(createToolbarButton("Update", "Update selected member", e -> updateSelectedMember()));
        toolbar.add(createToolbarButton("Delete", "Delete selected member", e -> deleteSelectedMember()));
        toolbar.addSeparator();
        toolbar.add(createToolbarButton("Load", "Load data from file", e -> loadData()));
        toolbar.add(createToolbarButton("Save", "Save data to file", e -> saveData()));
        toolbar.addSeparator();
        
        // Search components
        JLabel searchLabel = new JLabel("Search:");
        toolbar.add(searchLabel);
        
        searchTypeCombo = new JComboBox<>(new String[]{"All Fields", "Name", "ID", "Email", "Type"});
        searchTypeCombo.setMaximumSize(new Dimension(120, 25));
        toolbar.add(searchTypeCombo);
        
        searchField = new JTextField(15);
        searchField.setMaximumSize(new Dimension(200, 25));
        searchField.addActionListener(e -> performSearch());
        toolbar.add(searchField);
        
        toolbar.add(createToolbarButton("Search", "Search members", e -> performSearch()));
        toolbar.add(createToolbarButton("Clear", "Clear search", e -> clearSearch()));
        
        toolbar.addSeparator();
        
        // Sort components
        JLabel sortLabel = new JLabel("Sort by:");
        toolbar.add(sortLabel);
        
        sortCombo = new JComboBox<>(new String[]{"Name", "ID", "Type", "Performance", "Monthly Fee"});
        sortCombo.setMaximumSize(new Dimension(120, 25));
        sortCombo.addActionListener(e -> performSort());
        toolbar.add(sortCombo);
        
        return toolbar;
    }
    
    /**
     * Creates the center panel with member table.
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Members"));
        
        // Table in scroll pane
        JScrollPane scrollPane = new JScrollPane(memberTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    /**
     * Creates the bottom panel with status bar.
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        
        return bottomPanel;
    }
    
    /**
     * Sets up table appearance and selection behavior.
     */
    private void setupTableAppearance() {
        // Only allow selecting one row at a time - makes things simpler
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setRowHeight(25); // make rows a bit taller so they're easier to read
        memberTable.getTableHeader().setReorderingAllowed(false); // prevent users from messing up column order
        
        // Manually set column widths - default sizing looked terrible
        // Spent ages tweaking these numbers to make everything fit nicely
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID - short
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name - needs more space
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Type - medium
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Email - longest field
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Phone - bit longer
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(90);  // Performance - just numbers
        memberTable.getColumnModel().getColumn(6).setPreferredWidth(70);  // Goal - true/false
        memberTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Fee - money amounts
        
        // Double-click to see details - discovered this pattern from other GUI apps
        // Much more intuitive than having a separate "View Details" button
        memberTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // only on double-click
                    showMemberDetails();
                }
            }
        });
    }
    
    /**
     * Helper method to create menu items with action listeners.
     */
    private JMenuItem createMenuItem(String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }
    
    /**
     * Helper method to create toolbar buttons.
     */
    private JButton createToolbarButton(String text, String tooltip, ActionListener action) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.addActionListener(action);
        button.setFocusPainted(false);
        return button;
    }
    
    /**
     * Loads initial data when the application starts.
     */
    private void loadInitialData() {
        try {
            manager.loadFromFile(Constants.DEFAULT_FILE_NAME);
            refreshTable();
            updateStatus("Initial data loaded successfully");
        } catch (IOException e) {
            updateStatus("No existing data file found - starting with empty database");
        }
    }
    
    /**
     * Refreshes the table with current member data.
     */
    private void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear existing rows
            
            List<Member> members = manager.getAllMembers();
            for (Member member : members) {
                Object[] row = {
                    member.getMemberId(),
                    member.getFullName(),
                    member.getMemberType(),
                    member.getEmail(),
                    member.getPhone(),
                    member.getPerformanceRating(),
                    member.isGoalAchieved() ? "Yes" : "No",
                    String.format("$%.2f", member.calculateMonthlyFee())
                };
                tableModel.addRow(row);
            }
            
            updateStatus("Table refreshed - " + members.size() + " members displayed");
        });
    }
    
    /**
     * Updates the status bar with a message.
     */
    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            // Auto-clear status after 5 seconds
            javax.swing.Timer timer = new javax.swing.Timer(5000, e -> statusLabel.setText("Ready"));
            timer.setRepeats(false);
            timer.start();
        });
    }
    
    // Action Methods
    
    /**
     * Handles loading data from file.
     */
    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                manager.loadFromFile(fileName);
                refreshTable();
                updateStatus("Data loaded successfully from " + fileName);
                JOptionPane.showMessageDialog(this, 
                    "Data loaded successfully!", 
                    "Load Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                showErrorDialog("Failed to load data: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handles saving data to file.
     */
    private void saveData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        fileChooser.setSelectedFile(new java.io.File(Constants.DEFAULT_FILE_NAME));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                manager.saveToFile(fileName);
                updateStatus("Data saved successfully to " + fileName);
                JOptionPane.showMessageDialog(this, 
                    "Data saved successfully!", 
                    "Save Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                showErrorDialog("Failed to save data: " + e.getMessage());
            }
        }
    }
    
    /**
     * Shows the add member dialog.
     */
    private void showAddMemberDialog() {
        AddMemberDialog dialog = new AddMemberDialog(this, manager);
        dialog.setVisible(true);
        if (dialog.wasSuccessful()) {
            refreshTable();
            updateStatus("New member added successfully");
        }
    }
    
    /**
     * Updates the currently selected member.
     */
    private void updateSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a member to update.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row to model row (in case table is sorted)
        int modelRow = memberTable.convertRowIndexToModel(selectedRow);
        String memberId = (String) tableModel.getValueAt(modelRow, 0);
        
        Member member = manager.findMemberById(memberId);
        if (member != null) {
            UpdateMemberDialog dialog = new UpdateMemberDialog(this, manager, member);
            dialog.setVisible(true);
            if (dialog.wasSuccessful()) {
                refreshTable();
                updateStatus("Member updated successfully");
            }
        }
    }
    
    /**
     * Deletes the currently selected member.
     */
    private void deleteSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a member to delete.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row to model row
        int modelRow = memberTable.convertRowIndexToModel(selectedRow);
        String memberId = (String) tableModel.getValueAt(modelRow, 0);
        String memberName = (String) tableModel.getValueAt(modelRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete member: " + memberName + " (ID: " + memberId + ")?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.removeMember(memberId)) {
                refreshTable();
                updateStatus("Member deleted successfully");
                JOptionPane.showMessageDialog(this, 
                    "Member deleted successfully!", 
                    "Delete Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                showErrorDialog("Failed to delete member");
            }
        }
    }
    
    /**
     * Shows detailed information for the selected member.
     */
    private void showMemberDetails() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = memberTable.convertRowIndexToModel(selectedRow);
        String memberId = (String) tableModel.getValueAt(modelRow, 0);
        
        Member member = manager.findMemberById(memberId);
        if (member != null) {
            MemberDetailsDialog dialog = new MemberDetailsDialog(this, member);
            dialog.setVisible(true);
        }
    }
    
    /**
     * Performs search based on current search criteria.
     */
    private void performSearch() {
        String searchText = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        // If user cleared the search box, show everything again
        if (searchText.isEmpty()) {
            clearSearch();
            return;
        }
        
        // This is where the magic happens - our search algorithms at work!
        // The searcher automatically picks the best algorithm based on what we're searching for
        List<Member> results = searcher.search(searchText, searchType);
        displaySearchResults(results);
        updateStatus("Search completed - " + results.size() + " members found");
    }
    
    /**
     * Clears the current search and shows all members.
     */
    private void clearSearch() {
        searchField.setText("");
        tableSorter.setRowFilter(null);
        updateStatus("Search cleared - showing all members");
    }
    
    /**
     * Displays search results in the table.
     */
    private void displaySearchResults(List<Member> results) {
        if (results.isEmpty()) {
            tableSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    return false; // Show no rows
                }
            });
        } else {
            Set<String> resultIds = new HashSet<>();
            for (Member member : results) {
                resultIds.add(member.getMemberId());
            }
            
            tableSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    String id = (String) entry.getValue(0); // ID column
                    return resultIds.contains(id);
                }
            });
        }
    }
    
    /**
     * Performs sorting based on selected criteria.
     */
    private void performSort() {
        String sortBy = (String) sortCombo.getSelectedItem();
        List<Member> allMembers = manager.getAllMembers();
        List<Member> sortedMembers = sorter.sort(allMembers, sortBy);
        
        // Update table with sorted data
        tableModel.setRowCount(0);
        for (Member member : sortedMembers) {
            Object[] row = {
                member.getMemberId(),
                member.getFullName(),
                member.getMemberType(),
                member.getEmail(),
                member.getPhone(),
                member.getPerformanceRating(),
                member.isGoalAchieved() ? "Yes" : "No",
                String.format("$%.2f", member.calculateMonthlyFee())
            };
            tableModel.addRow(row);
        }
        
        updateStatus("Members sorted by " + sortBy);
    }
    
    /**
     * Shows system statistics.
     */
    private void showStatistics() {
        StatisticsDialog dialog = new StatisticsDialog(this, manager);
        dialog.setVisible(true);
    }
    
    /**
     * Shows performance reports.
     */
    private void showPerformanceReports() {
        PerformanceReportsDialog dialog = new PerformanceReportsDialog(this, manager);
        dialog.setVisible(true);
    }
    
    /**
     * Shows about dialog.
     */
    private void showAboutDialog() {
        String message = "Member Management System - GUI Version\n\n" +
                        "ICT711 Assessment 4\n" +
                        "Java Application with GUI and Text-Based Interface\n\n" +
                        "Features:\n" +
                        "• Member CRUD operations\n" +
                        "• Advanced search and sort algorithms\n" +
                        "• Performance management\n" +
                        "• Statistical reporting\n" +
                        "• File operations (CSV format)\n\n" +
                        "© 2025 ICT711 Programming and Algorithms";
        
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows error dialog with specified message.
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        updateStatus("Error: " + message);
    }
}