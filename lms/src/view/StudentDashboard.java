package view;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import model.myAccount;

public class StudentDashboard {
	static final String LIGHT_BLUE = "\u001B[36m"; // ANSI code for light blue
    static final String RESET_COLOR = "\u001B[0m";
    static Scanner scanner;
    public StudentDashboard() {
    	scanner = new Scanner(System.in);
    	
    }
    
    public static void mainStudentView() throws SQLException {
        System.out.println("\n| Online Learning Management System |");
        displayMenuOptions(); MainDashboard.MainView();

        boolean isValidChoice = false;

        // Loop until the user makes a valid choice
        while (!isValidChoice) {
            System.out.print("Enter your choice (1-7): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
    }

    private static void displayMenuOptions() {
        System.out.println(LIGHT_BLUE + "1" + RESET_COLOR + "| 👤My Account");
        System.out.println(LIGHT_BLUE + "2" + RESET_COLOR + "| My Courses");
        
        
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

    private static boolean handleChoice(int choice) throws SQLException {
    	
        switch (choice) {
            case 1 -> {
				myAccount.myAccountPage();
                return true;
            }
            case 2 -> {
                
                return true;
            }
            case 3 -> {
            	MainDashboard.searchThroughCourses();
                return true;
            }
            case 4, 5, 6, 7 -> {
				MainDashboard.choice(choice);
                return true;
            }
            default -> {
                System.out.println("Invalid choice, please select between 1 and 7.");
                return false;
            }
        }
    }
}

