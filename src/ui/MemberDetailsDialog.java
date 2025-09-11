package ui;

import javax.swing.*;
import java.awt.*;
import models.Member;
import models.PremiumMember;
import models.StudentMember;

/**
 * Dialog for displaying detailed member information.
 * Shows comprehensive member details in a read-only format.
 */
public class MemberDetailsDialog extends JDialog {
    private Member member;
    
    /**
     * Constructs the Member Details dialog.
     */
    public MemberDetailsDialog(JFrame parent, Member member) {
        super(parent, "Member Details", true);
        this.member = member;
        setupUI();
    }
    
    /**
     * Sets up the user interface.
     */
    private void setupUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    /**
     * Creates the content panel with member details.
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Basic Information
        addSection(panel, gbc, row++, "BASIC INFORMATION");
        addField(panel, gbc, row++, "Member ID:", member.getMemberId());
        addField(panel, gbc, row++, "Full Name:", member.getFullName());
        addField(panel, gbc, row++, "Member Type:", member.getMemberType());
        addField(panel, gbc, row++, "Email:", member.getEmail());
        addField(panel, gbc, row++, "Phone:", member.getPhone());
        
        // Performance Information
        row++;
        addSection(panel, gbc, row++, "PERFORMANCE INFORMATION");
        addField(panel, gbc, row++, "Performance Rating:", member.getPerformanceRating() + "/10");
        addField(panel, gbc, row++, "Goal Achieved:", member.isGoalAchieved() ? "Yes" : "No");
        addField(panel, gbc, row++, "Monthly Fee:", String.format("$%.2f", member.calculateMonthlyFee()));
        
        // Type-specific information
        if (member instanceof PremiumMember) {
            PremiumMember pm = (PremiumMember) member;
            row++;
            addSection(panel, gbc, row++, "PREMIUM MEMBER DETAILS");
            addField(panel, gbc, row++, "Trainer:", pm.getTrainerName());
            addField(panel, gbc, row++, "Sessions per Month:", String.valueOf(pm.getSessionsPerMonth()));
        } else if (member instanceof StudentMember) {
            StudentMember sm = (StudentMember) member;
            row++;
            addSection(panel, gbc, row++, "STUDENT MEMBER DETAILS");
            addField(panel, gbc, row++, "Student ID:", sm.getStudentId());
            addField(panel, gbc, row++, "University:", sm.getUniversity());
        }
        
        // Performance Report
        row++;
        addSection(panel, gbc, row++, "PERFORMANCE REPORT");
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        
        JTextArea reportArea = new JTextArea(member.generatePerformanceReport());
        reportArea.setEditable(false);
        reportArea.setBackground(getBackground());
        reportArea.setFont(new Font("Arial", Font.PLAIN, 12));
        reportArea.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        panel.add(scrollPane, gbc);
        
        return panel;
    }
    
    /**
     * Adds a section header.
     */
    private void addSection(JPanel panel, GridBagConstraints gbc, int row, String title) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sectionLabel.setForeground(new Color(51, 102, 153));
        sectionLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        panel.add(sectionLabel, gbc);
        
        // Reset for next field
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
    }
    
    /**
     * Adds a label-value field pair.
     */
    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        // Label
        gbc.gridx = 0; gbc.gridy = row;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(fieldLabel, gbc);
        
        // Value
        gbc.gridx = 1;
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(valueLabel, gbc);
    }
}