import javax.swing.*;
import java.awt.*;

public class AssignmentTrackerGUI extends JFrame {

    private JTextField courseField;
    private JTextField titleField;
    private JTextField dateField;

    private JButton addButton;
    private JButton completeButton;
    private JButton sortButton;
    private JButton saveButton;

    private JLabel totalLabel;
    private JLabel incompleteLabel;
    private JLabel completedLabel;

    public AssignmentTrackerGUI() {
        setTitle("Assignment Tracker");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel header = new JLabel(
                "Assignment Tracker", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(37, 62, 99));
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(
                15, 10, 15, 10));

        add(header, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
                10, 15, 10, 15));

        // Assignment counters
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        totalLabel = new JLabel("Total: 0", SwingConstants.CENTER);
        incompleteLabel = new JLabel("Incomplete: 0", SwingConstants.CENTER);
        completedLabel = new JLabel("Completed: 0", SwingConstants.CENTER);

        statsPanel.add(totalLabel);
        statsPanel.add(incompleteLabel);
        statsPanel.add(completedLabel);

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                "Add New Assignment"));

        courseField = new JTextField();
        titleField = new JTextField();
        dateField = new JTextField();
        addButton = new JButton("Add Assignment");

        inputPanel.add(new JLabel("Course Name:"));
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Assignment Title:"));
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        inputPanel.add(new JLabel(""));
        inputPanel.add(addButton);

        // Combine counters and input fields
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(statsPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Placeholder for assignment table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                "Your Assignments"));

        JLabel placeholder = new JLabel(
                "No assignments to display", SwingConstants.CENTER);

        tablePanel.add(placeholder, BorderLayout.CENTER);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        completeButton = new JButton("Mark Completed");
        sortButton = new JButton("Sort by Due Date");
        saveButton = new JButton("Save & Exit");

        buttonPanel.add(completeButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(saveButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AssignmentTrackerGUI();
        });
    }
}

