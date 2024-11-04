package model;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;
import view.InstructorDashboard;
import view.MainDashboard;
import view.StudentDashboard;

public class MyAccount {
	static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m";
    static String name;
    static Scanner scanner;
    static AccInfromation AccInfo;
    static int userId = SessionManager.getInstance().getUserId();
    
    public MyAccount() {
    	scanner = new Scanner(System.in);
    }
	public static void myAccountPage() throws SQLException {
		DatabaseOperations DBOps =  new DatabaseOperations();
		 
		name = DBOps.getColumnById("Name",userId); // get username by id
		
		System.out.println("\n👤Welecom, " + name);
		
		System.out.println(lightBlue + "1" + resetColor +"| My infromation ");
		System.out.println(lightBlue + "2" + resetColor +"| Credit ");
		System.out.println(lightBlue + "3" + resetColor +"| Notification ");
		System.out.println(lightBlue + "4" + resetColor +"| <- Return ");
		
		boolean isValidChoice = false;
		while (!isValidChoice) {
            System.out.print("Enter your choice (1-4): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
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

    private static boolean handleChoice(int choice) throws SQLException {
    	DatabaseOperations DB =  new DatabaseOperations();
    	
    	AccInfromation acc =  new AccInfromation();
    	
        switch (choice) {
            case 1 -> {
            	DB.getUserInfo(SessionManager.getInstance().getUserId());
                acc.InfoShow();
                acc.editAcc();
                return true;
            }
            case 2 -> {
                
                return true;
            }
            case 3 -> {
                
                return true;
            }
            case 4 -> {
            	if(DB.getRoleByID(userId) == 1) {
    				StudentDashboard view = new StudentDashboard();
        			view.mainStudentView();
    			}else {
    				InstructorDashboard view = new InstructorDashboard();
        			view.mainInstructorView();
    			}
            	return true;
            }
            
            default -> {
                System.out.println("Invalid choice, please select between 1 and 4.");
                return false;
            }
        }
    }
	
}
