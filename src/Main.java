
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Assignment> assignments = new ArrayList<>();
        
        loadAssignments(assignments);
        int choice = 0;

        while (choice != 4) {
            System.out.println("\n=== Assignment Tracker ===");
            System.out.println("1. Add Assignment");
            System.out.println("2. View Assignments");
            System.out.println("3. Complete Assignment");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
                continue;
            }

            switch (choice) {
                
                case 1:
                    System.out.print("Enter course name: ");
                    String course = scanner.nextLine().trim();

                    System.out.print("Enter assignment title: ");
                    String title = scanner.nextLine().trim();

                    System.out.print("Enter due date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine().trim();

                    if (course.isEmpty() || title.isEmpty()) {
                        System.out.println("Course and title cannot be empty.");
                        break;
                    }

                    try {
                        LocalDate dueDate = LocalDate.parse(dateInput);

                        Assignment assignment = new Assignment(course, title, dueDate);
                        assignments.add(assignment);

                        System.out.println("Assignment added successfully!");
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date. Use YYYY-MM-DD.");
                    }
                    break;
                case 2:
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments found.");
                    } else {
                        System.out.println("\n=== Your Assignments ===");

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
                case 3:
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments to complete.");
                        break;
                    }

                    System.out.println("\n=== Select an Assignment ===");

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

                        if (assignmentNumber < 1 ||
                                assignmentNumber > assignments.size()) {
                            System.out.println("Invalid assignment number.");
                        } else {
                            Assignment selected = assignments.get(assignmentNumber - 1);

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
                case 4:
                    saveAssignments(assignments);
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    public static void saveAssignments(ArrayList<Assignment> assignments) {
        try (PrintWriter writer = new PrintWriter("assignments.csv")) {
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
            System.out.println("Error saving assignments.");
        }
    }


    public static void loadAssignments(ArrayList<Assignment> assignments) {
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
                    boolean completed = Boolean.parseBoolean(parts[3]);

                    Assignment assignment =
                            new Assignment(course, title, dueDate);

                    if (completed) {
                        assignment.markCompleted();
                    }

                    assignments.add(assignment);
                } catch (DateTimeParseException e) {
                    System.out.println("Skipping assignment with invalid date.");
                }
            }

            System.out.println("Saved assignments loaded successfully!");

        } catch (java.io.FileNotFoundException e) {
            System.out.println("No saved assignments found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading assignments.");
        }
    }

}

