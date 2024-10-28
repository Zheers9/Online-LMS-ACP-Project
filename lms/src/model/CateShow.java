package model;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;

public class CateShow {
	static Scanner scanner;
	public CateShow() {
		scanner = new Scanner(System.in);
	}
	
	public static void show(String catName) {
		System.out.println("| "+catName+":");
		
			List<CourseInfo> latestCourses = CourseOps.getCoursesByCategoryName(catName);
			for (CourseInfo CourseInfo : latestCourses) {
	            System.out.println(latestCourses);
	        }
	}
	
	public static void courseChoice() throws SQLException {
		boolean isValidChoice = false;
		while (!isValidChoice) {
            System.out.print("Choice course by ID: ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter Course ID");
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
    	
        return true;
        
        
    }

	
}
