package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import manager.MemberManager;
import models.*;

/**
 * Dialog for displaying system statistics and analytics.
 */
public class StatisticsDialog extends JDialog {
    private MemberManager manager;
    
    public StatisticsDialog(JFrame parent, MemberManager manager) {
        super(parent, "System Statistics", true);
        this.manager = manager;
        setupUI();
    }
    
    private void setupUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Statistics content
        JTextArea statsArea = new JTextArea(generateStatistics());
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsArea.setBackground(getBackground());
        
        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(getParent());
    }
    
    private String generateStatistics() {
        StringBuilder stats = new StringBuilder();
        List<Member> members = manager.getAllMembers();
        
        stats.append("=== MEMBER MANAGEMENT SYSTEM STATISTICS ===\n\n");
        
        // Basic counts
        long regularCount = members.stream().filter(m -> m instanceof RegularMember).count();
        long premiumCount = members.stream().filter(m -> m instanceof PremiumMember).count();
        long studentCount = members.stream().filter(m -> m instanceof StudentMember).count();
        
        stats.append("MEMBERSHIP OVERVIEW:\n");
        stats.append("Total Members: ").append(members.size()).append("\n");
        stats.append("Regular Members: ").append(regularCount).append("\n");
        stats.append("Premium Members: ").append(premiumCount).append("\n");
        stats.append("Student Members: ").append(studentCount).append("\n\n");
        
        // Performance statistics
        if (!members.isEmpty()) {
            double avgPerformance = members.stream()
                    .mapToInt(Member::getPerformanceRating)
                    .average()
                    .orElse(0);
            
            long goalAchievers = members.stream().filter(Member::isGoalAchieved).count();
            double goalPercentage = (goalAchievers * 100.0) / members.size();
            
            stats.append("PERFORMANCE METRICS:\n");
            stats.append("Average Performance Rating: ").append(String.format("%.2f", avgPerformance)).append("/10\n");
            stats.append("Members Who Achieved Goals: ").append(goalAchievers).append(" (").append(String.format("%.1f", goalPercentage)).append("%)\n\n");
        }
        
        // Revenue information
        double totalRevenue = members.stream()
                .mapToDouble(Member::calculateMonthlyFee)
                .sum();
        
        stats.append("REVENUE INFORMATION:\n");
        stats.append("Total Monthly Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n\n");
        
        return stats.toString();
    }
}