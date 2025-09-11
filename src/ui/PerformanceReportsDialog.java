package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import manager.MemberManager;
import models.Member;

/**
 * Dialog for displaying performance reports and letters.
 */
public class PerformanceReportsDialog extends JDialog {
    private MemberManager manager;
    
    public PerformanceReportsDialog(JFrame parent, MemberManager manager) {
        super(parent, "Performance Reports", true);
        this.manager = manager;
        setupUI();
    }
    
    private void setupUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Tab pane for different report types
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Appreciation letters tab
        JTextArea appreciationArea = new JTextArea(generateAppreciationLetters());
        appreciationArea.setEditable(false);
        appreciationArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane appreciationScroll = new JScrollPane(appreciationArea);
        appreciationScroll.setPreferredSize(new Dimension(600, 400));
        tabbedPane.addTab("Appreciation Letters", appreciationScroll);
        
        // Reminder letters tab
        JTextArea reminderArea = new JTextArea(generateReminderLetters());
        reminderArea.setEditable(false);
        reminderArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane reminderScroll = new JScrollPane(reminderArea);
        tabbedPane.addTab("Reminder Letters", reminderScroll);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
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
    
    private String generateAppreciationLetters() {
        StringBuilder letters = new StringBuilder();
        List<Member> members = manager.getAllMembers();
        
        letters.append("=== APPRECIATION LETTERS ===\n\n");
        
        boolean hasAppreciationLetters = false;
        for (Member member : members) {
            String letter = manager.generateAppreciationLetter(member);
            if (letter != null) {
                letters.append(letter).append("\n\n");
                letters.append("-------------------\n\n");
                hasAppreciationLetters = true;
            }
        }
        
        if (!hasAppreciationLetters) {
            letters.append("No appreciation letters to generate.\n");
            letters.append("(Members need performance rating >= 8)");
        }
        
        return letters.toString();
    }
    
    private String generateReminderLetters() {
        StringBuilder letters = new StringBuilder();
        List<Member> members = manager.getAllMembers();
        
        letters.append("=== REMINDER LETTERS ===\n\n");
        
        boolean hasReminderLetters = false;
        for (Member member : members) {
            String letter = manager.generateReminderLetter(member);
            if (letter != null) {
                letters.append(letter).append("\n\n");
                letters.append("-------------------\n\n");
                hasReminderLetters = true;
            }
        }
        
        if (!hasReminderLetters) {
            letters.append("No reminder letters to generate.\n");
            letters.append("(Members need performance rating < 5)");
        }
        
        return letters.toString();
    }
}