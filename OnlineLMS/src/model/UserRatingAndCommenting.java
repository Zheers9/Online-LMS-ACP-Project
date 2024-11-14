package model; 

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;

public class UserRatingAndCommenting { 
	static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m";
  static int Rate = 0 ;
  static String comment ;
  String Date ;
    static LocalDate currentDate;
    static int StudentId = SessionManager.getInstance().getUserId();
    static int courseId;
    static Scanner scanner;
  
    public UserRatingAndCommenting() {
    	
    }
  public UserRatingAndCommenting(int id) throws SQLException {
	this.courseId = id;
    scanner = new Scanner(System.in);
    System.out.println(lightBlue + "1" + resetColor +"| view comment & rate ");
	System.out.println(lightBlue + "2" + resetColor +"| enter new commnt & rate ");
	
	boolean isValidChoice = false;
	while (!isValidChoice) {
        System.out.print("Enter your choice 1,2: ");
        Optional<Integer> choice = getUserInput();

        if (choice.isPresent()) {
            isValidChoice = handleChoice(choice.get());
        } else {
            System.out.println("Invalid input. Please enter a number 1,2.");
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
  	
      switch (choice) {
          case 1 -> {
        	  List<String> allCommentAndRate = (List<String>)ApplicationClient.requestOperation(new Command("viewComment",new Object[] {(Integer)courseId}));
        	  for (String string : allCommentAndRate) {
				System.out.println(allCommentAndRate);
			}
              return true;
          }
          case 2 -> {
        	  InsertRatingAndCommenting();
              return true;
          }
          default -> {
              System.out.println("Invalid choice, please select between 1 and 4.");
              return false;
          }
      }
      
      
  }
  
  public static int getRate() {
    return Rate;
  }
  
  public static String getComment() {
    return comment;
  }
  
  public static void InsertRatingAndCommenting() throws SQLException { 
	    // Use a single scanner instance for the class instead of creating a new one
	    boolean validInput = false;
	    
	    while (!validInput) {
	        System.out.print("Enter the Rate of course (1 to 5): ");

	        if (scanner.hasNextInt()) { 
	            int rate = scanner.nextInt();
	            if (rate >= 1 && rate <= 5) {
	                Rate = rate;
	                validInput = true; 
	            } else {
	                System.out.print("\nPlease Enter a Rate between 1 and 5:");
	            }
	        } else {
	            System.out.println("\nInvalid input. Please enter an integer: ");
	            scanner.next();   
	        }
	    } 

	    currentDate = LocalDate.now();
	    System.out.print("\nDo you want to write a comment? (yes - no): ");
	    while (true) {
	        String writeComment = scanner.next();
	        scanner.nextLine();  // Consume newline left-over

	        if (writeComment.equalsIgnoreCase("yes")) {
	            System.out.print("\nWrite the comment: ");
	            comment = scanner.nextLine();
	            break;  // Exit loop after writing the comment
	        } else if (writeComment.equalsIgnoreCase("no")) {
	            comment = null;
	            break;  // Exit loop when no comment is needed
	        } else {
	            System.out.println("Please enter 'yes' or 'no'.");  // Invalid input message
	        }
	    }
	    ApplicationClient.requestOperation(new Command("insertRatingAndComment",new Object[] {(Integer)StudentId, (Integer)courseId, (Integer)Rate, comment,currentDate}));
	    
	    new CoursePage(courseId);
	    
	    
	    
	}

  
  
}
