
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.io.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Provides a graphical interface for the Assignment Tracker.
 * Allows users to add, view, complete, and sort assignments.
 * Assignment information is saved to a CSV file and restored
 * when the application is reopened.
 */
public class AssignmentTrackerGUI extends JFrame {

    // Input fields for entering assignment information
    private JTextField courseField;
    private JTextField titleField;
    private JTextField dateField;

    // Buttons for performing assignment operations
    private JButton addButton;
    private JButton completeButton;
    private JButton sortButton;
    private JButton saveButton;

    // Labels that display assignment statistics
    private JLabel totalLabel;
    private JLabel incompleteLabel;
    private JLabel completedLabel;

    // Table and model used to display assignment information
    private JTable assignmentTable;
    private DefaultTableModel tableModel;

    // Stores all assignment objects during the current session
    private ArrayList<Assignment> assignments = new ArrayList<>();

    /**
     * Constructs the graphical interface, initializes its components,
     * connects action listeners, and loads previously saved assignments.
     */
    public AssignmentTrackerGUI() {

        // Configure the main application window
        setTitle("Assignment Tracker");
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 500));

        // Prevent the window from closing before assignments are saved
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Save assignments when the user closes the window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndExit();
            }
        });

        // Center the window and establish its main layout
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Create and style the application header
        JLabel header = new JLabel(
                "Assignment Tracker", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(37, 62, 99));
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(
                15, 10, 15, 10));

        add(header, BorderLayout.NORTH);

        // Create the main panel that holds the application components
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(243, 245, 249));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
                10, 15, 10, 15));

        // Create three counters arranged horizontally
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        totalLabel = new JLabel("Total: 0", SwingConstants.CENTER);
        incompleteLabel = new JLabel("Incomplete: 0", SwingConstants.CENTER);
        completedLabel = new JLabel("Completed: 0", SwingConstants.CENTER);

        // Assign different colors to distinguish assignment statistics
        styleCounter(totalLabel, new Color(37, 62, 99));
        styleCounter(incompleteLabel, new Color(190, 120, 30));
        styleCounter(completedLabel, new Color(35, 130, 75));

        statsPanel.add(totalLabel);
        statsPanel.add(incompleteLabel);
        statsPanel.add(completedLabel);

        // Create the input panel for adding new assignments
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                "Add New Assignment"));

        // Initialize input fields and the Add Assignment button
        courseField = new JTextField();
        titleField = new JTextField();
        dateField = new JTextField();
        addButton = new JButton("Add Assignment");

        // Pair each input field with a descriptive label
        inputPanel.add(new JLabel("Course Name:"));
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Assignment Title:"));
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        inputPanel.add(new JLabel(""));
        inputPanel.add(addButton);

        // Combine the statistics and input panels
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(statsPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Create the panel that displays all assignments
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                "Your Assignments"));

        // Define the information displayed in each table column
        String[] columns = {
            "Course", "Assignment Title", "Due Date", "Status"
        };

        // Prevent users from directly editing assignment data in the table
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Initialize the table using the assignment table model
        assignmentTable = new JTable(tableModel);
        assignmentTable.setRowHeight(30);

        // Configure table fonts and colors for readability
        assignmentTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        assignmentTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 14));

        assignmentTable.getTableHeader().setBackground(
                new Color(225, 232, 242));

        assignmentTable.setSelectionBackground(
                new Color(210, 225, 245));

        assignmentTable.setSelectionForeground(Color.BLACK);
        assignmentTable.setGridColor(new Color(225, 225, 225));

        // Set preferred column widths based on expected content length
        assignmentTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        assignmentTable.getColumnModel().getColumn(1).setPreferredWidth(280);
        assignmentTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        assignmentTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        // Allow only one assignment to be selected at a time
        assignmentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        // Maintain a consistent column order and fill the available space
        assignmentTable.getTableHeader().setReorderingAllowed(false);
        assignmentTable.setFillsViewportHeight(true);

        // Customize the status column to visually distinguish
        // completed and incomplete assignments
        assignmentTable.getColumnModel().getColumn(3)
                .setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                // Preserve the default Swing cell rendering behavior
                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("SansSerif", Font.BOLD, 13));

                // Green represents completed assignments,
                // while orange represents incomplete assignments
                if ("Completed".equals(value)) {
                    setForeground(new Color(35, 130, 75));
                } else {
                    setForeground(new Color(190, 120, 30));
                }

                return this;
            }
        });

        // Allow users to scroll when the assignment list becomes large
        JScrollPane scrollPane = new JScrollPane(assignmentTable);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Create the bottom panel containing assignment action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        completeButton = new JButton("Mark Completed");
        sortButton = new JButton("Sort by Due Date");
        saveButton = new JButton("Save & Exit");

        buttonPanel.add(completeButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(saveButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the completed content panel to the main window
        add(contentPanel, BorderLayout.CENTER);

        // Apply consistent styling to all action buttons
        styleButton(addButton, new Color(37, 62, 99));
        styleButton(completeButton, new Color(35, 130, 75));
        styleButton(sortButton, new Color(65, 105, 160));
        styleButton(saveButton, new Color(95, 100, 115));

        // Connect each button to its corresponding operation
        addButton.addActionListener(e -> addAssignment());
        completeButton.addActionListener(e -> completeAssignment());
        sortButton.addActionListener(e -> sortAssignments());
        saveButton.addActionListener(e -> saveAndExit());

        // Restore saved assignments and update the interface
        loadAssignments();
        refreshTable();

        // Display the application after initialization is complete
        setVisible(true);
    }

    /**
     * Starts the graphical version of the Assignment Tracker.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        // Create the GUI on Swing's Event Dispatch Thread
        // to ensure thread-safe component initialization
        SwingUtilities.invokeLater(() -> {
            new AssignmentTrackerGUI();
        });
    }

    /**
     * Refreshes the assignment table using the current assignment list.
     * Also recalculates the total, incomplete, and completed counters.
     */
    private void refreshTable() {

        // Remove existing rows to prevent duplicate table entries
        tableModel.setRowCount(0);

        int completedCount = 0;

        // Populate the table with the latest assignment information
        for (Assignment a : assignments) {
            tableModel.addRow(new Object[]{
                a.getCourse(),
                a.getTitle(),
                a.getDueDate(),
                a.isCompleted() ? "Completed" : "Incomplete"
            });

            // Count completed assignments while rebuilding the table
            if (a.isCompleted()) {
                completedCount++;
            }
        }

        // Update all three assignment statistics
        totalLabel.setText("Total: " + assignments.size());

        incompleteLabel.setText("Incomplete: " +
                (assignments.size() - completedCount));

        completedLabel.setText("Completed: " + completedCount);
    }

    /**
     * Creates a new assignment using information entered by the user.
     * Validates required fields, CSV compatibility, and the due date
     * before adding the assignment to the list.
     */
    private void addAssignment() {

        // Retrieve user input and remove unnecessary whitespace
        String course = courseField.getText().trim();
        String title = titleField.getText().trim();
        String dateInput = dateField.getText().trim();

        // Prevent assignments from being created with missing information
        if (course.isEmpty() || title.isEmpty() || dateInput.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.");
            return;
        }

        // Reject commas because they are used as CSV field separators
        if (course.contains(",") || title.contains(",")) {
            JOptionPane.showMessageDialog(this,
                    "Course and title cannot contain commas.");
            return;
        }

        try {
            // Convert the entered date into a LocalDate object
            LocalDate dueDate = LocalDate.parse(dateInput);

            // Create and store the new assignment
            Assignment assignment =
                    new Assignment(course, title, dueDate);

            assignments.add(assignment);

            // Update the table and assignment counters
            refreshTable();

            // Clear input fields for the next assignment
            courseField.setText("");
            titleField.setText("");
            dateField.setText("");

            JOptionPane.showMessageDialog(this,
                    "Assignment added successfully!");

        } catch (DateTimeParseException e) {

            // Display an error instead of allowing invalid dates
            // to interrupt the application
            JOptionPane.showMessageDialog(this,
                    "Invalid date. Please use YYYY-MM-DD.");
        }
    }

    /**
     * Marks the currently selected assignment as completed.
     * Ensures an assignment is selected and has not already
     * been completed before updating its status.
     */
    private void completeAssignment() {

        // Retrieve the index of the selected table row
        int selectedRow = assignmentTable.getSelectedRow();

        // A value of -1 indicates that no row is selected
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an assignment first.");
            return;
        }

        // Retrieve the corresponding assignment from the list
        Assignment selected = assignments.get(selectedRow);

        // Prevent an already completed assignment from being completed again
        if (selected.isCompleted()) {
            JOptionPane.showMessageDialog(this,
                    "Assignment is already completed.");
            return;
        }

        // Update the assignment's completion status
        selected.markCompleted();

        // Refresh the table and statistics to reflect the change
        refreshTable();

        JOptionPane.showMessageDialog(this,
                "Assignment marked as completed!");
    }

    /**
     * Sorts assignments chronologically from earliest to latest due date.
     * This is the additional Schedule feature that helps users
     * prioritize upcoming deadlines.
     */
    private void sortAssignments() {

        // Ensure there are assignments available to sort
        if (assignments.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No assignments to sort.");
            return;
        }

        // Compare assignment due dates using their LocalDate values
        assignments.sort(
                Comparator.comparing(Assignment::getDueDate));

        // Display assignments in their updated chronological order
        refreshTable();

        JOptionPane.showMessageDialog(this,
                "Assignments sorted by due date!");
    }

    /**
     * Saves all assignments to a local CSV file.
     * Each row contains the course name, assignment title,
     * due date, and completion status.
     *
     * @return true if saving succeeds, false otherwise
     */
    private boolean saveAssignments() {

        // Try-with-resources automatically closes the writer
        try (PrintWriter writer = new PrintWriter("assignments.csv")) {

            // Write each assignment as a comma-separated row
            for (Assignment a : assignments) {
                writer.println(
                        a.getCourse() + "," +
                        a.getTitle() + "," +
                        a.getDueDate() + "," +
                        a.isCompleted()
                );
            }

            // PrintWriter does not automatically throw exceptions
            // for every writing error, so explicitly check for errors
            if (writer.checkError()) {
                throw new IOException("Unable to write assignments.");
            }

            return true;

        } catch (IOException e) {

            // Notify the user if saving fails
            JOptionPane.showMessageDialog(this,
                    "Error saving assignments.");
            return false;
        }
    }

    /**
     * Loads previously saved assignments from the local CSV file.
     * Reconstructs Assignment objects and restores their completion
     * statuses. Invalid rows are skipped so that malformed data
     * does not interrupt the application.
     */
    private void loadAssignments() {

        // Open the saved CSV file using try-with-resources
        try (BufferedReader reader = new BufferedReader(
                new FileReader("assignments.csv"))) {

            String line;

            // Read the saved assignments one row at a time
            while ((line = reader.readLine()) != null) {

                // Split the CSV row while preserving empty fields
                String[] parts = line.split(",", -1);

                // Each valid assignment must contain exactly four fields
                if (parts.length != 4) {
                    continue;
                }

                try {
                    // Extract the saved assignment information
                    String course = parts[0];
                    String title = parts[1];
                    LocalDate dueDate = LocalDate.parse(parts[2]);
                    String status = parts[3].trim();

                    // Skip rows containing invalid completion statuses
                    if (!status.equalsIgnoreCase("true") &&
                            !status.equalsIgnoreCase("false")) {
                        continue;
                    }

                    // Reconstruct the assignment using its saved information
                    Assignment assignment =
                            new Assignment(course, title, dueDate);

                    // Restore the completion status because new
                    // assignments are incomplete by default
                    if (Boolean.parseBoolean(status)) {
                        assignment.markCompleted();
                    }

                    // Add the restored assignment to the current list
                    assignments.add(assignment);

                } catch (DateTimeParseException e) {

                    // Skip assignments with invalid dates and
                    // continue loading the remaining rows
                }
            }

        } catch (FileNotFoundException e) {

            // A missing CSV file is expected when the application
            // is opened for the first time

        } catch (IOException e) {

            // Notify the user if an error occurs while reading the file
            JOptionPane.showMessageDialog(this,
                    "Error loading assignments.");
        }
    }

    /**
     * Saves all assignments before closing the application.
     * The window remains open if saving fails so the user
     * can attempt to save again without losing current changes.
     */
    private void saveAndExit() {

        // Only close the window after a successful save
        if (saveAssignments()) {
            JOptionPane.showMessageDialog(this,
                    "Assignments saved successfully!");
            dispose();
        }
    }

    /**
     * Applies consistent visual styling to assignment counter labels.
     *
     * @param label the counter label to style
     * @param color the foreground color of the label
     */
    private void styleCounter(JLabel label, Color color) {

        // Configure the counter's font and colors
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(color);
        label.setBackground(Color.WHITE);
        label.setOpaque(true);

        // Combine an outer border with inner padding
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
    }

    /**
     * Applies consistent visual styling to application buttons.
     *
     * @param button the button to style
     * @param color the background color of the button
     */
    private void styleButton(JButton button, Color color) {

        // Configure the button's font and colors
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);

        // Customize the button's appearance
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Add padding around the button text
        button.setBorder(BorderFactory.createEmptyBorder(
                10, 18, 10, 18));

        // Display a hand cursor when hovering over the button
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
