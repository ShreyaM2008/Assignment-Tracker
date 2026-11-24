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

    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    private ArrayList<Assignment> assignments = new ArrayList<>();

    public AssignmentTrackerGUI() {
        setTitle("Assignment Tracker");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndExit();
            }
        });
        
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

        
        // Assignment table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                "Your Assignments"));

        String[] columns = {
            "Course", "Assignment Title", "Due Date", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        assignmentTable = new JTable(tableModel);
        assignmentTable.setRowHeight(30);
        assignmentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        assignmentTable.getTableHeader().setReorderingAllowed(false);
        assignmentTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(assignmentTable);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
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
        
        addButton.addActionListener(e -> addAssignment());
        completeButton.addActionListener(e -> completeAssignment());
        sortButton.addActionListener(e -> sortAssignments());

        loadAssignments();
        refreshTable();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AssignmentTrackerGUI();
        });
    }


    
    private void refreshTable() {
        tableModel.setRowCount(0);

        int completedCount = 0;

        for (Assignment a : assignments) {
            tableModel.addRow(new Object[]{
                a.getCourse(),
                a.getTitle(),
                a.getDueDate(),
                a.isCompleted() ? "Completed" : "Incomplete"
            });

            if (a.isCompleted()) {
                completedCount++;
            }
        }

        totalLabel.setText("Total: " + assignments.size());

        incompleteLabel.setText("Incomplete: " +
                (assignments.size() - completedCount));

        completedLabel.setText("Completed: " + completedCount);
    }



    private void addAssignment() {
        String course = courseField.getText().trim();
        String title = titleField.getText().trim();
        String dateInput = dateField.getText().trim();

        if (course.isEmpty() || title.isEmpty() || dateInput.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.");
            return;
        }

        if (course.contains(",") || title.contains(",")) {
            JOptionPane.showMessageDialog(this,
                    "Course and title cannot contain commas.");
            return;
        }

        try {
            LocalDate dueDate = LocalDate.parse(dateInput);

            Assignment assignment =
                    new Assignment(course, title, dueDate);

            assignments.add(assignment);

            refreshTable();

            courseField.setText("");
            titleField.setText("");
            dateField.setText("");

            JOptionPane.showMessageDialog(this,
                    "Assignment added successfully!");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date. Please use YYYY-MM-DD.");
        }
    }



    private void completeAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an assignment first.");
            return;
        }

        Assignment selected = assignments.get(selectedRow);

        if (selected.isCompleted()) {
            JOptionPane.showMessageDialog(this,
                    "Assignment is already completed.");
            return;
        }

        selected.markCompleted();

        refreshTable();

        JOptionPane.showMessageDialog(this,
                "Assignment marked as completed!");
    }


    private void sortAssignments() {
        if (assignments.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No assignments to sort.");
            return;
        }

        assignments.sort(
                Comparator.comparing(Assignment::getDueDate));

        refreshTable();

        JOptionPane.showMessageDialog(this,
                "Assignments sorted by due date!");
    }


    private boolean saveAssignments() {
        try (PrintWriter writer = new PrintWriter("assignments.csv")) {
            for (Assignment a : assignments) {
                writer.println(
                        a.getCourse() + "," +
                        a.getTitle() + "," +
                        a.getDueDate() + "," +
                        a.isCompleted()
                );
            }

            if (writer.checkError()) {
                throw new IOException("Unable to write assignments.");
            }

            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving assignments.");
            return false;
        }
    }


    private void loadAssignments() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader("assignments.csv"))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);

                if (parts.length != 4) {
                    continue;
                }

                try {
                    String course = parts[0];
                    String title = parts[1];
                    LocalDate dueDate = LocalDate.parse(parts[2]);
                    String status = parts[3].trim();

                    if (!status.equalsIgnoreCase("true") &&
                            !status.equalsIgnoreCase("false")) {
                        continue;
                    }

                    Assignment assignment =
                            new Assignment(course, title, dueDate);

                    if (Boolean.parseBoolean(status)) {
                        assignment.markCompleted();
                    }

                    assignments.add(assignment);

                } catch (DateTimeParseException e) {
                    // Skip assignments with invalid dates
                }
            }

        } catch (FileNotFoundException e) {
            // Start with an empty list if no saved file exists
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading assignments.");
        }
    }

    private void saveAndExit() {
        if (saveAssignments()) {
            JOptionPane.showMessageDialog(this,
                    "Assignments saved successfully!");
            dispose();
        }
    }







}

