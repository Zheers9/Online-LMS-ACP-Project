package model;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;
import view.InstructorDashboard;
import view.StudentDashboard;

public class CoursePage {
    private static final String LIGHT_BLUE = "\u001B[36m"; // ANSI code for light blue
    private static final String RESET_COLOR = "\u001B[0m";

    private static Scanner scanner;
    private static int id;
    private static String title;
    private static String description;
    private static int credit;
    private static String categoryName;
    private static int userId = SessionManager.getInstance().getUserId();

    public CoursePage(int courseID) {
        CoursePage.id = courseID;
        scanner = new Scanner(System.in);
    }

    public void setCourseInfo(String title, String description, int credit, String categoryName) {
        CoursePage.title = title;
        CoursePage.description = description;
        CoursePage.credit = credit;
        CoursePage.categoryName = categoryName;
    }

    public static void view(int course) throws SQLException {
        CourseOps courseOps = new CourseOps();
        UserRatingAndCommenting rateComment = new UserRatingAndCommenting();

        // Fetch course info
        courseOps.getCourseInfo(id);

        displayCourseDetails(rateComment);
        handleUserChoice(courseOps);
    }

    private static void displayCourseDetails(UserRatingAndCommenting rateComment) {
        System.out.println();
        System.out.println(LIGHT_BLUE + title + RESET_COLOR + ", " + id);
        System.out.println(rateComment.averageRate(id));
        System.out.println("\n" + categoryName + "\n" + description + "\nCredit: " + credit);

        String enrollmentOption = CourseOps.isStudentEnroll(userId, id) ? "Start" : "Enroll";
        System.out.println(LIGHT_BLUE + "1" + RESET_COLOR + " | " + enrollmentOption);
        System.out.println(LIGHT_BLUE + "2" + RESET_COLOR + " | Comment & Rate");
        System.out.println(LIGHT_BLUE + "3" + RESET_COLOR + " | Exit");
    }

    private static void handleUserChoice(CourseOps courseOps) throws SQLException {
        boolean isValidChoice = false;

        while (!isValidChoice) {
            System.out.print("Enter your choice (1, 2, 3): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = processChoice(choice.get(), courseOps);
            } else {
                System.out.println("Invalid input. Please enter (1, 2, 3).");
            }
        }
    }

    private static Optional<Integer> getUserInput() {
        try {
            int choice = scanner.nextInt();
            return Optional.of(choice);
        } catch (Exception e) {
            scanner.next(); // Clear invalid input
            return Optional.empty();
        }
    }

    private static boolean processChoice(int choice, CourseOps courseOps) throws SQLException {
        switch (choice) {
            case 1:
                handleEnrollment(courseOps);
                return true;
            case 2:
                viewCommentsAndRatings();
                return true;
            case 3:
                navigateToDashboard();
                return true;
            default:
                System.out.println("Invalid choice, please select 1, 2, or 3.");
                return false;
        }
    }

    private static void handleEnrollment(CourseOps courseOps) throws SQLException {
        if (courseOps.isStudentEnroll(userId, id)) {
            System.out.println("Opening course...");
        } else {
            enroll();
        }
    }

    private static void viewCommentsAndRatings() throws SQLException {
        UserRatingAndCommenting rateComment = new UserRatingAndCommenting(id);
        view(id);
    }

    private static void navigateToDashboard() throws SQLException {
        DatabaseOperations dbOperations = new DatabaseOperations();
        if (dbOperations.getRoleByID(userId) == 1) {
            new StudentDashboard().mainStudentView();
        } else {
            new InstructorDashboard().mainInstructorView();
        }
    }

    private static void enroll() throws SQLException {
        System.out.print("\n" + title + "\n$ Total Credit: " + credit + "\nAre you sure you want to enroll? (y/n): ");

        while (true) {
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("y")) {
                CourseOps courseOps = new CourseOps();
                String message = courseOps.enrollCourse(userId, id, credit) ? "Enrollment successful!" : "Enrollment failed.";
                System.out.println(message);
                view(id);
                break; // Exit the loop after handling the response
            } else if (choice.equalsIgnoreCase("n")) {
                System.out.println("Enrollment cancelled.");
                break; // Exit the loop if the user cancels
            } else {
                System.out.println("Invalid choice. Please enter 'y' or 'n'.");
            }
        }
    }
}
