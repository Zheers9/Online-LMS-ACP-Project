package model;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;

public class coursePage {
	static Scanner scanner;
	private static int  id;
    private static String title;
    private static String description;
    private static int credit;
    private static String categoryName;
    private static Date date;
    
	public coursePage() {
		scanner = new Scanner(System.in);
	}
	
	public static void view() throws SQLException {
		System.out.println();
		System.out.println(title + " ,"+id);
		rate();
		System.out.println("\n" + categoryName + "\n" + description + "\n credit : " + credit);
		
		if(1==1) 
			System.out.println("1- start learning");
		else if( 2==2)
			System.out.println("1- enroll course");
		
		System.out.println("\n2- rate & comment");
		
		boolean isValidChoice = false;
		while (!isValidChoice) {
            System.out.print("Enter your choice (1,2): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter ( 1,2 ).");
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
    	Boolean v = true;
    	
        switch (choice) {
            case 1 -> {
            	if(v)
                	System.out.println("ss");
                else 
                	System.out.println("en");
                return true;
            }
            case 2 -> {
            	UserRatingAndCommenting rateComment = new UserRatingAndCommenting();
                return true;
            }
            case 3 -> {
            	
                return true;
            }
            
            
            default -> {
                System.out.println("Invalid choice, please select 1,2.");
                return false;
            }
        }
        
        
    }
	
	
	public static void rate() {
		
	}
	
	public static void start() {
		
	}
	
	public static void enroll() {
		
	}
	
	
}
