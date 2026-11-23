
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Assignment> assignments = new ArrayList<>();
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
                    System.out.println("Complete Assignment selected.");
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}

