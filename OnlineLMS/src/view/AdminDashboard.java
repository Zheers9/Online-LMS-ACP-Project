package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;
import model.ManageCourses;
import model.SessionManager;
import model.myAccount;

public class AdminDashboard {
	static int userId = SessionManager.getInstance().getUserId();
	static String name = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"Name", (Integer) userId}));
    static  Scanner scanner = new Scanner(System.in);
    
    AdminDashboard(){}

	protected static void mainAdminView() throws SQLException {
		String welcome = ANSIColor.RED+"""
				
 █████╗ ██████╗ ███╗   ███╗██╗███╗   ██╗    ██████╗  █████╗ ███████╗██╗  ██╗   
██╔══██╗██╔══██╗████╗ ████║██║████╗  ██║    ██╔══██╗██╔══██╗██╔════╝██║  ██║   
███████║██║  ██║██╔████╔██║██║██╔██╗ ██║    ██║  ██║███████║███████╗███████║   
██╔══██║██║  ██║██║╚██╔╝██║██║██║╚██╗██║    ██║  ██║██╔══██║╚════██║██╔══██║   
██║  ██║██████╔╝██║ ╚═╝ ██║██║██║ ╚████║    ██████╔╝██║  ██║███████║██║  ██║██╗
╚═╝  ╚═╝╚═════╝ ╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝
                                                                               

				"""+ANSIColor.RESET;
		
		System.out.println(welcome);
		displayMenuOptions();
		getUserInputInLoop();
	}
	private static void displayMenuOptions() {
        System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET + "| Add Credits to student ");
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET + "| Exit");
    }
	private static Optional<Integer> getUserInput() {
        try {
            int choice = scanner.nextInt();
            return Optional.of(choice);
        } catch (Exception e) {
        	scanner.nextLine();// Clear invalid input
            return Optional.empty();
        }
        
    }

    private static boolean handleChoice(int choice, MainDashboard mainDashboard) throws SQLException {
    	myAccount acc = new myAccount();
        switch (choice) {
            case 1 -> {
            	viewListOfStudents();
                return true;
            }
            case 2 -> {
            	new LoginAndSignUp();
                return true;
            }
            default -> {
                System.out.println("Invalid choice, please select between 1 and 2.");
                return false;
            }
        }
    }
    private static void viewListOfStudents() throws SQLException {
        List<String> AllStudentList = (List<String>) ApplicationClient.requestOperation(new Command("getAllStudent", new Object[] {}));
        for (String student : AllStudentList) {
            System.out.println(student); // Print each student name
        }
        System.out.println();
        System.out.print("Enter user Id for adding credit: ");
        
        String userId = scanner.nextLine().trim();  // Read and trim any extra spaces
        
        // Check if the input is a valid integer
        if (isValidInteger(userId)) {
            int userIdInteger = Integer.parseInt(userId);
            
            String studentName = (String) ApplicationClient.requestOperation(new Command("getColumnById", new Object[] {"name", userIdInteger}));
            System.out.print("Are you sure you want to add credit? (y/n) ");
            
            String userInput = scanner.nextLine();  // Capture the next line input
            if (userInput.equals("y")) {
                System.out.print("\nEnter the number of credit you want to add: ");
                int addedCredits = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                System.out.println((String) ApplicationClient.requestOperation(new Command("updateStudentCredit", new Object[] {addedCredits, userIdInteger})));
            } else {
                mainAdminView();
            }
        } else {
            System.out.println("Invalid user ID entered. Please enter a numeric value.");
            viewListOfStudents();  // Ask again if the input is invalid
        }
    }

    // Helper method to validate if a string is a valid integer
    private static boolean isValidInteger(String str) {
        try {
            Integer.parseInt(str);  // Try to parse the string to an integer
            return true;
        } catch (NumberFormatException e) {
            return false;  // Return false if it's not a valid integer
        }
    }



	private static void getUserInputInLoop() throws SQLException {
		// TODO Auto-generated method stub

		boolean isValidChoice = false;
        // Loop until the user makes a valid choice
        while (!isValidChoice) {
            System.out.print("Enter your choice: ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                MainDashboard mainDashboard = new MainDashboard();
				isValidChoice = handleChoice(choice.get(), mainDashboard );
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
	}
}
