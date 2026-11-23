
import java.time.LocalDate;

public class Assignment {
    private String course;
    private String title;
    private LocalDate dueDate;
    private boolean completed;

    public Assignment(String course, String title, LocalDate dueDate) {
        this.course = course;
        this.title = title;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public String getCourse() {
        return course;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }
}

