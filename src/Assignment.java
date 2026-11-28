
import java.time.LocalDate;

/**
 * Represents an individual assignment in the Assignment Tracker.
 * Stores the course name, assignment title, due date,
 * and completion status.
 */
public class Assignment {

    // Fields that store assignment information
    private String course;
    private String title;
    private LocalDate dueDate;
    private boolean completed;

    /**
     * Creates a new assignment with the provided information.
     * All new assignments are initially marked as incomplete.
     *
     * @param course the name of the course
     * @param title the title of the assignment
     * @param dueDate the deadline for the assignment
     */
    public Assignment(String course, String title, LocalDate dueDate) {
        this.course = course;
        this.title = title;
        this.dueDate = dueDate;

        // New assignments are incomplete by default
        this.completed = false;
    }

    /**
     * Returns the course associated with the assignment.
     *
     * @return the course name
     */
    public String getCourse() {
        return course;
    }

    /**
     * Returns the title of the assignment.
     *
     * @return the assignment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the assignment's deadline.
     * The LocalDate type allows assignments to be sorted by date.
     *
     * @return the assignment due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Checks whether the assignment has been completed.
     *
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Updates the assignment's status to completed.
     * This method is called when the user completes an assignment.
     */
    public void markCompleted() {
        completed = true;
    }
}
