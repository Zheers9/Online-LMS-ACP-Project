package model;

import java.util.Optional;
import java.util.Scanner;

import view.MainDashboard;

public class ManageCourses {
	static Scanner scanner;
	static final String LIGHT_BLUE = "\u001B[36m"; // ANSI code for light blue
    static final String RESET_COLOR = "\u001B[0m";
    
	public ManageCourses() {
		scanner = new Scanner(System.in);// TODO Auto-generated constructor stub
	}
	
	public static void view() {
		System.out.println(LIGHT_BLUE + "1" + RESET_COLOR + "| 👤My courses");
        System.out.println(LIGHT_BLUE + "2" + RESET_COLOR + "| Upload Courses");
        boolean isValidChoice = false;
        // Loop until the user makes a valid choice
        while (!isValidChoice) {
            System.out.print("Enter your choice (1-2): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter a number between 1,2.");
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
    	myAccount acc = new myAccount();
        switch (choice) {
            case 1 -> {
                acc.myAccountPage();
                return true;
            }
            case 2 -> {
                
                return true;
            }
            case 3 -> {
                
                return true;
            }
            case 4, 5, 6, 7 -> {
                
                return true;
            }
            default -> {
                System.out.println("Invalid choice, please select between 1 and 7.");
                return false;
            }
        }
    }
    
    public static void myCourse() {
    	
    }
	
}
