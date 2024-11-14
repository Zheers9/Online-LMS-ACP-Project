package model;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;
import view.InstructorDashboard;
import view.StudentDashboard;

public class CoursePage {

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

    public static void setCourseInfo(String title, String description, int credit, String categoryName) {
        CoursePage.title = title;
        CoursePage.description = description;
        CoursePage.credit = credit;
        CoursePage.categoryName = categoryName;
    }

    public static void view(int course) throws SQLException {
        // Fetch course info
        String[] courseInfo = (String[]) ApplicationClient.requestOperation(new Command("getCourseInfo",new Object[] {(Integer) id}));
        CoursePage.setCourseInfo(courseInfo[0],courseInfo[1],Integer.parseInt(courseInfo[2]),courseInfo[3]);
        displayCourseDetails();
        handleUserChoice();
    }

    private static void displayCourseDetails() {
        System.out.println();
        System.out.println(ANSIColor.CYAN + title + ANSIColor.RESET + ", " + id);
        
        System.out.println((String)ApplicationClient.requestOperation(new Command("averageRate",new Object[] {(Integer)id})));
        System.out.println("\n" + categoryName + "\n" + description + "\nCredit: " + credit);

        String enrollmentOption = (boolean) ApplicationClient.requestOperation(new Command("isStudentEnroll",new Object[] {(Integer)userId,(Integer) id})) ? "Start" : "Enroll";
        System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET + " | " + enrollmentOption);
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET + " | Comment & Rate");
        System.out.println(ANSIColor.CYAN + "3" + ANSIColor.RESET + " | Exit");
    }

    private static void handleUserChoice() throws SQLException {
        boolean isValidChoice = false;

        while (!isValidChoice) {
            System.out.print("Enter your choice (1, 2, 3): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = processChoice(choice.get());
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

    private static boolean processChoice(int choice) throws SQLException {
        switch (choice) {
            case 1:
                handleEnrollment();
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

    private static void handleEnrollment() throws SQLException {
    	
        if ((boolean) ApplicationClient.requestOperation(new Command("isStudentEnroll",new Object[] {(Integer)userId,(Integer) id}))) {
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
       
        
        if (((Integer) ApplicationClient.requestOperation(new Command("getRoleByID",new Object[] {(Integer) userId}))) == 1) {
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
                
            	String[] userInfo = (String[]) ApplicationClient.requestOperation(new Command("getUserInfo",new Object[] {(Integer) SessionManager.getInstance().getUserId()}));
                String message = (boolean) ApplicationClient.requestOperation(new Command("enrollCourse",new Object[] {(Integer)userId,(Integer) id,credit})) ? "Enrollment successful!" : ANSIColor.RED+"Enrollment failed."+ANSIColor.RESET;
                if (Integer.parseInt(userInfo[2]) < credit) {
                    System.out.println("You Don't have enough credit");
                }
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

