package control;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import model.SessionManager;
import model.coursePage;
import view.InstructorDashboard;
import view.StudentDashboard;

public class HandleChoice {
	static Scanner scanner;
    static int userId = SessionManager.getInstance().getUserId();

	
	public HandleChoice() {
		scanner = new Scanner(System.in);
	}
	
	public static void courseChoice() throws SQLException {
        boolean isValidChoice = false;
        while (!isValidChoice) {
            System.out.print("Choose course by ID (0 to return): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter a Course ID.");
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
    	DatabaseOperations DB = new DatabaseOperations();
    	if(choice == 0 ) {
    		if(DB.getRoleByID(userId) == 1) {
				StudentDashboard view = new StudentDashboard();
    			view.mainStudentView();
			}else {
				InstructorDashboard view = new InstructorDashboard();
    			view.mainInstructorView();
			}
			StudentDashboard view = new StudentDashboard();
			view.mainStudentView();
    	}else {
    		CourseOps CourseDB = new CourseOps();
    		CourseDB.getCourseInfo(choice);
    		coursePage course = new coursePage(choice);  // Assuming CoursePage has a constructor that takes choice as an argument
            course.view(choice);  // Assuming view method in CoursePage accepts an argument
            
    	} // Assuming view method in CoursePage accepts an argument
        return true;
    }
}
