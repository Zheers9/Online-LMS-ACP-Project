package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;
import model.AccInfromation;
import model.CourseInfo;
import model.EditAcc;
import model.ManageCourses;
import model.SessionManager;
import model.myAccount;

public class InstructorDashboard {
    static DatabaseOperations DBOps =  new DatabaseOperations();

	static String name = DBOps.getColumnById("Name",SessionManager.getInstance().getUserId());
	static final String LIGHT_BLUE = "\u001B[36m"; // ANSI code for light blue
    static final String RESET_COLOR = "\u001B[0m";
    static Scanner scanner;
    static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m";
   
    public InstructorDashboard() {
    	scanner = new Scanner(System.in);
    }
    
    public static void mainInstructorView() throws SQLException {
        System.out.println("\n| Online Learning Management System |");
        displayMenuOptions();
        
        MainDashboard mainDashboard = new MainDashboard();
        mainDashboard.MainView();

        boolean isValidChoice = false;

        // Loop until the user makes a valid choice
        while (!isValidChoice) {
            System.out.print("Enter your choice (1-7): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get(), mainDashboard);
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
    }

    private static void displayMenuOptions() {
        System.out.println(LIGHT_BLUE + "1" + RESET_COLOR + "| 👤My Account");
        System.out.println(LIGHT_BLUE + "2" + RESET_COLOR + "| Manage Courses");
        
        
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

    private static boolean handleChoice(int choice, MainDashboard mainDashboard) throws SQLException {
    	myAccount acc = new myAccount();
        switch (choice) {
            case 1 -> {
            	acc.myAccountPage();
                return true;
            }
            case 2 -> {
            	ManageCourses manage = new ManageCourses();
            	manage.view();
                return true;
            }
            case 3 -> {
                
                return true;
            }
            case 4, 5, 6, 7 -> {
                mainDashboard.choice(choice);
                return true;
            }
            default -> {
                System.out.println("Invalid choice, please select between 1 and 7.");
                return false;
            }
        }
    }
    
    
    
    
    
    

}
