package model;

import java.util.Optional;
import java.util.Scanner;

public class CourseCategory{
	private static Scanner scanner;
	static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m"; 

    static String[] categories = {
        "Programming & IT", 
        "Graphic Design", 
        "Mathmatics", 
        "Language Learning", 
        "Science", 
        "Health and Medicine", 
        "Engineering", 
        "Business and Management", 
        "Personal Development", 
        "Marketing",
        "Others"
    };
    
    
	public CourseCategory() {
		scanner  = new Scanner(System.in);
	}
	
	public static String[] cateList() {
		return categories;
	}
	
	public static void Category() {
		
        System.out.println("| Category:");
        for (int i = 0; i < categories.length; i++) {
            // Print the category number in light blue and reset color afterward
            System.out.printf("%s%d-%s %s", lightBlue, i + 1, resetColor, categories[i]);

            // Print a line break after every two categories
            if (i % 2 == 1) {
                System.out.println();
            } else {
                System.out.print("  "); // Add spacing for formatting
            }
        }

        // Print "0- ALL" with the number in light blue
        System.out.println(lightBlue + "0- ALL" + resetColor);
        selectCategory();
	}
	
	
	// Select category that user wants
    public static void selectCategory() {
        Optional<Integer> choice = Optional.empty();

        while (choice.isEmpty()) {
            System.out.print("Please select a category (0 to see all): ");
            choice = readValidInput();
            
            choice.ifPresent(ch -> CateShow.show(ch == 0 ? "ALL" : categories[ch - 1]));
        }
    }

    // Read user input and return valid category number
    private static Optional<Integer> readValidInput() {
        try {
            int input = scanner.nextInt();
            if (input >= 0 && input <= categories.length) {
                return Optional.of(input);
            } else {
                System.out.println("Invalid input. Please enter a number between 0 and " + categories.length + ".");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Clear invalid input
        }
        return Optional.empty();
    }
        
    
	
}
