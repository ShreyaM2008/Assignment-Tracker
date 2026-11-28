
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

/**
 * Provides the command-line version of the Assignment Tracker.
 * Allows users to add, view, complete, and sort assignments.
 * Assignments are saved to a CSV file for future sessions.
 */
public class Main {

    /**
     * Starts the application and handles user interaction
     * through an interactive menu.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Store all assignments during the current session
        ArrayList<Assignment> assignments = new ArrayList<>();

        // Restore previously saved assignments before displaying the menu
        loadAssignments(assignments);
        int choice = 0;

        // Continue displaying the menu until the user chooses to exit
        while (choice != 5) {
            System.out.println("\n=== Assignment Tracker ===");
            System.out.println("1. Add Assignment");
            System.out.println("2. View Assignments");
            System.out.println("3. Complete Assignment");
            System.out.println("4. Sort Assignments by Due Date");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();

            // Validate menu input to prevent crashes from non-numeric entries
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
                continue;
            }

            // Execute the operation selected by the user
            switch (choice) {

                // Option 1: Add a new assignment
                case 1:
                    System.out.print("Enter course name: ");
                    String course = scanner.nextLine().trim();

                    System.out.print("Enter assignment title: ");
                    String title = scanner.nextLine().trim();

                    System.out.print("Enter due date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine().trim();

                    // Prevent assignments with missing information
                    if (course.isEmpty() || title.isEmpty()) {
                        System.out.println("Course and title cannot be empty.");
                        break;
                    }

                    try {
                        // Convert the entered date into a LocalDate object
                        LocalDate dueDate = LocalDate.parse(dateInput);

                        // Create and store the new assignment
                        Assignment assignment = new Assignment(course, title, dueDate);
                        assignments.add(assignment);

                        System.out.println("Assignment added successfully!");
                    } catch (DateTimeParseException e) {
                        // Handle invalid dates without terminating the program
                        System.out.println("Invalid date. Use YYYY-MM-DD.");
                    }
                    break;

                // Option 2: Display all assignments and their details
                case 2:
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments found.");
                    } else {
                        System.out.println("\n=== Your Assignments ===");

                        // Display each assignment with user-friendly numbering
                        for (int i = 0; i < assignments.size(); i++) {
                            Assignment a = assignments.get(i);

                            System.out.println("\nAssignment " + (i + 1));
                            System.out.println("Course: " + a.getCourse());
                            System.out.println("Title: " + a.getTitle());
                            System.out.println("Due Date: " + a.getDueDate());
                            System.out.println("Status: " +
                                (a.isCompleted() ? "Completed" : "Incomplete"));
                        }
                    }
                    break;

                // Option 3: Mark an existing assignment as completed
                case 3:
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments to complete.");
                        break;
                    }

                    System.out.println("\n=== Select an Assignment ===");

                    // Display numbered assignments for user selection
                    for (int i = 0; i < assignments.size(); i++) {
                        Assignment a = assignments.get(i);

                        System.out.println((i + 1) + ". " + a.getTitle()
                                + " (" + a.getCourse() + ") - "
                                + (a.isCompleted() ? "Completed" : "Incomplete"));
                    }

                    System.out.print("Enter assignment number: ");
                    String numberInput = scanner.nextLine();

                    try {
                        int assignmentNumber = Integer.parseInt(numberInput);

                        // Ensure the selected assignment number is within range
                        if (assignmentNumber < 1 ||
                                assignmentNumber > assignments.size()) {
                            System.out.println("Invalid assignment number.");
                        } else {
                            // Convert user numbering to a zero-based ArrayList index
                            Assignment selected = assignments.get(assignmentNumber - 1);

                            // Avoid marking an already completed assignment again
                            if (selected.isCompleted()) {
                                System.out.println("Assignment is already completed.");
                            } else {
                                selected.markCompleted();
                                System.out.println("Assignment marked as completed!");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                    break;

                // Option 4: Schedule feature - sort assignments by due date
                case 4:
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments to sort.");
                    } else {
                        // Compare LocalDate values to arrange deadlines
                        // from earliest to latest
                        assignments.sort(
                            Comparator.comparing(Assignment::getDueDate)
                        );

                        System.out.println("Assignments sorted by due date!");

                        // Display assignments in their updated chronological order
                        for (Assignment a : assignments) {
                            System.out.println(
                                a.getDueDate() + " - " +
                                a.getCourse() + ": " +
                                a.getTitle()
                            );
                        }
                    }
                    break;

                // Option 5: Save assignment data before exiting
                case 5:
                    saveAssignments(assignments);
                    System.out.println("Goodbye!");
                    break;

                // Handle menu choices outside the valid range
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        // Release the Scanner resource when the application ends
        scanner.close();
    }

    /**
     * Saves all assignments to a local CSV file.
     * Each row contains the course, title, due date,
     * and completion status of an assignment.
     *
     * @param assignments the list of assignments to save
     */
    public static void saveAssignments(ArrayList<Assignment> assignments) {

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

            System.out.println("Assignments saved successfully!");
        } catch (FileNotFoundException e) {
            // Notify the user if the output file cannot be opened
            System.out.println("Error saving assignments.");
        }
    }

    /**
     * Loads previously saved assignments from the CSV file.
     * Reconstructs Assignment objects and restores their
     * original completion statuses.
     *
     * Invalid rows are skipped to prevent corrupted data
     * from interrupting the application.
     *
     * @param assignments the list that receives the loaded assignments
     */
    public static void loadAssignments(ArrayList<Assignment> assignments) {

        // Open the saved file using try-with-resources
        try (BufferedReader reader = new BufferedReader(
                new FileReader("assignments.csv"))) {

            String line;

            // Read the CSV file one assignment at a time
            while ((line = reader.readLine()) != null) {

                // Preserve empty fields when splitting the CSV row
                String[] parts = line.split(",", -1);

                // Each valid assignment must contain exactly four fields
                if (parts.length != 4) {
                    System.out.println("Skipping invalid CSV row.");
                    continue;
                }

                try {
                    // Extract assignment information from the CSV fields
                    String course = parts[0];
                    String title = parts[1];
                    LocalDate dueDate = LocalDate.parse(parts[2]);
                    String status = parts[3].trim();

                    // Ensure the saved completion status is a valid boolean
                    if (!status.equalsIgnoreCase("true") &&
                            !status.equalsIgnoreCase("false")) {
                        System.out.println("Skipping assignment with invalid status.");
                        continue;
                    }

                    boolean completed = Boolean.parseBoolean(status);

                    // Reconstruct the assignment using its saved information
                    Assignment assignment =
                            new Assignment(course, title, dueDate);

                    // Restore completion status since new objects
                    // are incomplete by default
                    if (completed) {
                        assignment.markCompleted();
                    }

                    // Add the restored assignment to the current list
                    assignments.add(assignment);
                } catch (DateTimeParseException e) {
                    // Skip invalid dates while continuing to load other rows
                    System.out.println("Skipping assignment with invalid date.");
                }
            }

            System.out.println("Saved assignments loaded successfully!");

        } catch (java.io.FileNotFoundException e) {
            // A missing file is expected when running for the first time
            System.out.println("No saved assignments found. Starting fresh.");
        } catch (IOException e) {
            // Handle other errors that occur while reading the file
            System.out.println("Error loading assignments.");
        }
    }
}
