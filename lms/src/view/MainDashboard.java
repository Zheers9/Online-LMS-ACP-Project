package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import control.ANSIColor;
import control.CourseOps;
import control.DatabaseOperations;
import control.HandleChoice;
import model.CateShow;
import model.CourseCategory;
import model.CourseInfo;
import model.CoursePage;
import model.SessionManager;

public class MainDashboard {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DatabaseOperations DB = new DatabaseOperations();
    private static final int userId = SessionManager.getInstance().getUserId();
    private static final HandleChoice courseChoiceHandler = new HandleChoice();

    public MainDashboard() {
    }

    public static void mainView() {
        System.out.println(ANSIColor.CYAN + "\n3" + ANSIColor.RESET + " | Search: 🔍");
        System.out.println(ANSIColor.CYAN + "4" + ANSIColor.RESET + " | New Course");
        System.out.println(ANSIColor.CYAN + "5" + ANSIColor.RESET + " | Category");
        System.out.println(ANSIColor.CYAN + "6" + ANSIColor.RESET + " | Show All");
        System.out.println(ANSIColor.CYAN + "7" + ANSIColor.RESET + " | EXIT");
    }

    public static void processChoice(int choice) throws SQLException {
        List<CourseInfo> latestCourses = CourseOps.getLatestCourses();
        CourseCategory category = new CourseCategory();

        switch (choice) {
            case 4 -> {
                for (CourseInfo courseInfo : latestCourses) {
                    System.out.println(courseInfo);
                }
                courseChoiceHandler.courseChoice();
            }
            case 5 -> {
                category.showCategory();
                courseChoiceHandler.courseChoice();
            }
            case 6 -> {
                CateShow.show("ALL");
            }
            case 7 -> {
                System.out.println("Goodbye! Don't stop learning. We're waiting for you, boss.");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice, please select 1-7.");
        }
    }

    public static void returnBack() throws SQLException {
        System.out.print("\n<- Return (y/n)?: ");
        String response = scanner.next();

        if (response.equalsIgnoreCase("y")) {
            int roleId = DB.getRoleByID(userId);
            if (roleId == 1) {
                StudentDashboard studentView = new StudentDashboard();
                studentView.mainStudentView();
            } else {
                InstructorDashboard instructorView = new InstructorDashboard();
                instructorView.mainInstructorView();
            }
        }
    }

    private static Optional<Integer> getUserInput() {
        try {
            return Optional.of(scanner.nextInt());
        } catch (Exception e) {
            scanner.next(); // Clear invalid input
            return Optional.empty();
        }
    }

    private static boolean handleChoice(int choice) throws SQLException {
        if (choice == 0) {
            int roleId = DB.getRoleByID(userId);
            if (roleId == 1) {
                StudentDashboard studentView = new StudentDashboard();
                studentView.mainStudentView();
            } else {
                InstructorDashboard instructorView = new InstructorDashboard();
                instructorView.mainInstructorView();
            }
        } else {
            CoursePage coursePage = new CoursePage(choice);
            coursePage.view(choice);
        }
        return true;
    }
}
