package model;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;
import view.MainDashboard;

public class EditAcc {
	static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m";
    static DatabaseOperations DBOps =  new DatabaseOperations();
	static String name = DBOps.getUserNameById(SessionManager.getInstance().getUserId());// get username by id
    static Scanner scanner;
    
    public EditAcc() {
    	scanner = new Scanner(System.in);
    }
	public static void EditAccountPage() {
		
		
		System.out.println("\n👤What do you want to change, " + name);

		System.out.println(lightBlue + "1" + resetColor +"| Name ");
		System.out.println(lightBlue + "2" + resetColor +"| Email ");
		System.out.println(lightBlue + "3" + resetColor +"| Password ");
		System.out.println(lightBlue + "4" + resetColor +"| Field ");
		
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

    private static boolean handleChoice(int choice) {
    	DatabaseOperations DB =  new DatabaseOperations();
    	
    	AccInfromation acc =  new AccInfromation();
    	
        switch (choice) {
            case 1 -> {
            	changeName();
                return true;
            }
            case 2 -> {
                chanegEmail();
                return true;
            }
            case 3 -> {
                
                return true;
            }
            
            default -> {
                System.out.println("Invalid choice, please select between 1 and 4.");
                return false;
            }
        }
        
        
    }
    
    public static void changeName() {
        boolean isNameChanged = false;
        Scanner scanner = new Scanner(System.in);
        
        // Get the user's current name
        int userId = SessionManager.getInstance().getUserId();
        String currentName = DBOps.getUserNameById(userId);
        
        while (!isNameChanged) {
            System.out.println("Your current name is: " + currentName);
            System.out.print("Enter your new name: ");
            
            // Capture the new name from the user
            String newName = scanner.nextLine();
            
            // Validate the new name (only letters and spaces allowed)
            if (newName.matches("[a-zA-Z ]+")) {
                // Update the name in the database
                boolean isUpdated = DBOps.updateString("name",newName, userId);
                
                if (isUpdated) {
                    // If update is successful, fetch the updated name
                    currentName = DBOps.getUserNameById(userId);
                    System.out.println("\nYour name has been changed to: " + currentName);
                    isNameChanged = true;
                } else {
                    System.out.println("An error occurred while updating your name. Please try again.");
                }
            } else {
                // Inform the user about invalid input
                System.out.println("Invalid name. Please enter a name containing only letters and spaces.");
            }
        }
        
        AccInfromation acc = new AccInfromation();
        DBOps.getUserInfo(SessionManager.getInstance().getUserId());
        acc.InfoShow();
        acc.editAcc();
        
    }

    public static void chanegEmail() {
    	boolean isNameChanged = false;
        Scanner scanner = new Scanner(System.in);
        
        // Get the user's current name
        int userId = SessionManager.getInstance().getUserId();
        String currentName = DBOps.getUserNameById(userId);
        
        while (!isNameChanged) {
            System.out.println("Your current email is: " + currentName);
            System.out.print("Enter your new email: ");
            
            // Capture the new name from the user
            String newEmail = scanner.nextLine();
            
            // Validate the new name (only letters and spaces allowed)
            if (newEmail.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$") ) {
            	if(!DBOps.isEmailTaken(newEmail)) {
            		boolean isUpdated = DBOps.updateString("email",newEmail, userId);
                    
                    if (isUpdated) {
                        // If update is successful, fetch the updated name
                        currentName = DBOps.getUserNameById(userId);
                        System.out.println("\nYour email has been changed to: " + currentName);
                        isNameChanged = true;
                    } else {
                        System.out.println("An error occurred while updating your name. Please try again.");
                    }
            	}else
            		System.err.println("The email is already taken.");
                // Update the name in the database
                
            } else {
                // Inform the user about invalid input
                System.out.println("Invalid email. Please enter a valid email address.ex(name@gmail.com)");
            }
        }
        
        AccInfromation acc = new AccInfromation();
        DBOps.getUserInfo(SessionManager.getInstance().getUserId());
        acc.InfoShow();
        acc.editAcc();
    }
    public static void chanegPass() {
    	
    }
    public static void chanegField() {
    	
    }
	
}
