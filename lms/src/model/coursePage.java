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
    static int userId = SessionManager.getInstance().getUserId();
    static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m";
    
    
    
	public coursePage(int courseID) {
		this.id =  courseID;
		
		scanner = new Scanner(System.in);
	}
	
	public void CourseInfo(String title, String description, int credit, String categoryName) {
        this.title = title;
        this.description = description;
        this.credit = credit;
        this.categoryName = categoryName;
    }
	
	public static void view(int course) throws SQLException {
		//display info
		CourseOps DB = new CourseOps();
		DB.getCourseInfo(id);
		
		
		System.out.println();
		System.out.println(lightBlue + title + resetColor+ " ,"+id);
		//rateComment.averageRate(id);
		System.out.println("\n" + categoryName + "\n" + description + "\ncredit : " + credit);
		
		
		if(DB.isStudentEnroll(userId,id)) 
			System.out.println(lightBlue + "1" + resetColor +"| Start ");
		else
			System.out.println(lightBlue + "1" + resetColor +"| Enroll ");
		
		System.out.println(lightBlue + "2" + resetColor +"| comment & rate ");
		UserRatingAndCommenting rateComment = new UserRatingAndCommenting(id);
		//handle user choice
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
            	 if (v) {
                       
                 } else {
                       
                 }
                return true;
            }
            case 2 -> {
            	UserRatingAndCommenting rateComment = new UserRatingAndCommenting(id);
            	
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
