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
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 500));
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
        contentPanel.setBackground(new Color(243, 245, 249));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
                10, 15, 10, 15));

        // Assignment counters
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        totalLabel = new JLabel("Total: 0", SwingConstants.CENTER);
        incompleteLabel = new JLabel("Incomplete: 0", SwingConstants.CENTER);
        completedLabel = new JLabel("Completed: 0", SwingConstants.CENTER);

        styleCounter(totalLabel, new Color(37, 62, 99));
        styleCounter(incompleteLabel, new Color(190, 120, 30));
        styleCounter(completedLabel, new Color(35, 130, 75));

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


        assignmentTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        assignmentTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 14));

        assignmentTable.getTableHeader().setBackground(
                new Color(225, 232, 242));

        assignmentTable.setSelectionBackground(
                new Color(210, 225, 245));

        assignmentTable.setSelectionForeground(Color.BLACK);
        assignmentTable.setGridColor(new Color(225, 225, 225));

        assignmentTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        assignmentTable.getColumnModel().getColumn(1).setPreferredWidth(280);
        assignmentTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        assignmentTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        assignmentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        assignmentTable.getTableHeader().setReorderingAllowed(false);
        assignmentTable.setFillsViewportHeight(true);

        assignmentTable.getColumnModel().getColumn(3)
                .setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("SansSerif", Font.BOLD, 13));

                if ("Completed".equals(value)) {
                    setForeground(new Color(35, 130, 75));
                } else {
                    setForeground(new Color(190, 120, 30));
                }

                return this;
            }
        });

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
        
        styleButton(addButton, new Color(37, 62, 99));
        styleButton(completeButton, new Color(35, 130, 75));
        styleButton(sortButton, new Color(65, 105, 160));
        styleButton(saveButton, new Color(95, 100, 115));

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

    
    private void styleCounter(JLabel label, Color color) {
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(color);
        label.setBackground(Color.WHITE);
        label.setOpaque(true);

        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setBorder(BorderFactory.createEmptyBorder(
                10, 18, 10, 18));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }







}

