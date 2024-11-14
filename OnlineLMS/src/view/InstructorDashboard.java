package view;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;
import model.ManageCourses;
import model.SessionManager;
import model.myAccount;

public class InstructorDashboard {
    static int userId = SessionManager.getInstance().getUserId();
	static String name = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"Name", (Integer) userId}));
    static Scanner scanner;
   
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
        System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET + "| 👤My Account");
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET + "| Manage Courses");
        
        
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
            	MainDashboard.searchThroughCourses();
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
