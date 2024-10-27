package model; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;

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
  	DatabaseOperations DB =  new DatabaseOperations();
  	
  	AccInfromation acc =  new AccInfromation();
  	
      switch (choice) {
          case 1 -> {
        	  viewComment(courseId);
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
	    CourseOps db = new CourseOps();
	    db.insertRatingAndComment(StudentId, courseId,Rate, comment,currentDate);
	    coursePage back = new coursePage(courseId);
	    
	    
	    
	}

  
  public static String averageRate(int course_ID) {
	    String query = "SELECT AVG(Rate) AS average_rating FROM CommentRate WHERE course_ID = " + course_ID + ";";
	    double averageRating = 0.0;

	    try (ResultSet resultSet = DatabaseOperations.executeQuery(query)) {
	        if (resultSet.next()) {
	            averageRating = resultSet.getDouble("average_rating");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle exceptions appropriately
	    }

	    // Round to nearest 0.5
	    averageRating = Math.round(averageRating * 2.0) / 2.0;

	    return convertRatingToStars(averageRating);
	}

	private static String convertRatingToStars(double rating) {
	    StringBuilder stars = new StringBuilder();

	    int fullStars = (int) rating; // Get full star count
	    boolean halfStar = (rating - fullStars) >= 0.5; // Check for half star

	    for (int i = 0; i < fullStars; i++) {
	        stars.append("★"); // Append filled star
	    }
	    if (halfStar) {
	        stars.append("✮"); // Append half star
	    }
	    for (int i = fullStars + (halfStar ? 1 : 0); i < 5; i++) {
	        stars.append("☆"); // Append empty star
	    }

	    return stars.toString(); // Return the star representation
	}
  
  public static void viewComment(int course_ID) { 
      String query = "SELECT * FROM CommentRate WHERE course_ID = " + course_ID;

      try { 
          ResultSet resultSet = DatabaseOperations.executeQuery(query);
           
          if (!resultSet.isBeforeFirst()) {
              System.out.println("No ratings found for this course.");
          } else { 
              while (resultSet.next()) { 
                  int studentId = resultSet.getInt("student_ID");
                  int courseId = resultSet.getInt("course_ID");
                  int rate = resultSet.getInt("Rate");
                  String comment = resultSet.getString("comment");
                  String date = resultSet.getString("date");

                  // Display the data
                  System.out.println("Student ID: " + studentId);
                  System.out.println("Course ID: " + courseId);
                  StringBuilder stars = new StringBuilder();
                  for (int i = 0; i < rate; i++) {
                      stars.append("★"); // Append a star for each point of the rating
                  }
                  for (int i = rate; i < 5; i++) {
                      stars.append("☆"); // Append an empty star for the remaining points
                  }
                  System.out.println("Rate: " + stars.toString());
                  System.out.println("Comment: " + (comment != null ? comment : "No comment"));
                  System.out.println("Date: " + date);
                  System.out.println("-------------------------------");
              }
          }
 
          resultSet.close();
      } catch (SQLException e) {
          e.printStackTrace();
      }
      
      
  }
}
