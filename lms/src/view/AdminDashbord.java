package view;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import control.ANSIColor;
import control.DatabaseOperations;
import model.MyAccount;

public class AdminDashbord {
    private static Scanner scanner;
    static DatabaseOperations DB = new DatabaseOperations();

    public AdminDashbord() {
        scanner = new Scanner(System.in);
    }

    public static void mainAdminView() throws SQLException {
        System.out.println("\n| Online Learning Management System |");
        displayMenuOptions();

        MainDashboard mainDashboard = new MainDashboard();
        mainDashboard.mainView();

        boolean isValidChoice = false;

        // Loop until the user makes a valid choice
        while (!isValidChoice) {
            System.out.print("Enter your choice (1-7): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = processChoice(choice.get(), mainDashboard);
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
    }

    private static void displayMenuOptions() {
        System.out.println(ANSIColor.CYAN + "1" + ANSIColor.WHITE + " | 👤 My Account");
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.WHITE + " | New Admin");
        System.out.println(ANSIColor.CYAN + "3" + ANSIColor.WHITE + " | Add User Credit");
        System.out.println(ANSIColor.CYAN + "4" + ANSIColor.WHITE + " | Manage course");
        System.out.println(ANSIColor.CYAN + "5" + ANSIColor.WHITE + " | Manage Account");
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

    private static boolean processChoice(int choice, MainDashboard mainDashboard) throws SQLException {
        
        switch (choice) {
            case 1 -> {
                new MyAccount().myAccountPage();
                return true;
            }
            case 2 -> {
            	new LoginAndSignUp().createAcc(0);
                return true;
            }
            case 3 -> {
            	addCredit();
                return true;
            }
            case 4, 5, 6, 7 -> {
                mainDashboard.processChoice(choice);
                return true;
            }
            default -> {
                System.out.println("Invalid choice, please select between 1 and 7.");
                return false;
            }
        }
    }
    
    private static void addCredit() {
    	while (true) {
            int userId = -1;
            int credit = -1;

            // Loop for user ID input
            while (true) {
                System.out.print("Enter your User ID (only numbers): ");
                if (scanner.hasNextInt()) {
                    userId = scanner.nextInt();
                    if (userId >= 0) {
                        // Check if the user ID exists in the database
                        if (DB.doesUserIdExist(userId)) {
                            break; // Valid user ID
                        } else {
                            System.out.println("User ID does not exist. Please enter a valid ID.");
                        }
                    } else {
                        System.out.println("User ID must be a positive number.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid ID.");
                    scanner.next(); // Clear invalid input
                }
            }

            // Loop for credit input
            while (true) {
                System.out.print("Enter your Credit (only integers): ");
                if (scanner.hasNextInt()) {
                    credit = scanner.nextInt();
                    if (credit >= 0) {
                    	DB.addCredit(userId,credit);
                    	break; // Valid credit
                    }
                    else System.out.println("Credit must be a positive integer.");
                } else {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.next(); // Clear invalid input
                }
            }
            
            // Once valid inputs are received, you can process them here
            System.out.println("credit added succusfully");
            System.out.println("User ID: " + userId + ", Credit: " + credit);

            // Optionally, break the loop or continue based on your needs
            System.out.print("Would you like to enter another User ID and Credit? (yes/no): ");
            scanner.nextLine(); // Clear the newline
            String continueInput = scanner.nextLine();
            if (!continueInput.equalsIgnoreCase("yes")) {
                break; // Exit the loop
            }
        }
    	
    	try {
			mainAdminView();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
