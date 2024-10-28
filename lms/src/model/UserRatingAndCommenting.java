package model; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import control.DatabaseOperations;

public class UserRatingAndCommenting { 
  int Rate = 0 ;
  String comment ;
  String Date ;
    LocalDate currentDate;
    int StudentId = SessionManager.getInstance().getUserId();
    int courseId = 3;
    
  public UserRatingAndCommenting() {
    Scanner user = new Scanner(System.in);
    boolean validInput = false;
 
      while (!validInput) {
          System.out.println("Enter the Rate of course (1 to 5)");

          if (user.hasNextInt()) { 
              int rate = user.nextInt();
              if (rate >= 1 && rate <= 5) {
                  Rate = rate;
                  validInput = true; 
              } else {
                  System.out.println("Please Enter a Rate between 1 and 5");
              }
          } else {
              System.out.println("Invalid input. Please enter an integer.");
              user.next();   
          }
      } 
    
      currentDate = LocalDate.now();
    System.out.println("Do you want to write comment? (yes - no)");
     while (true) {
       String writeComment = user.next();
           user.nextLine();  // Consume newline left-over
              
           if (writeComment.equalsIgnoreCase("yes")) {
             System.out.println("Write the comment:");
               comment = user.nextLine();
               break;  // Exit loop after writing the comment
           } else if (writeComment.equalsIgnoreCase("no")) {
               comment = null;
               break;  // Exit loop when no comment is needed
           } else {
               System.out.println("Please enter 'yes' or 'no'.");  // Invalid input message
           }
      }
  }
  
  public int getRate() {
    return Rate;
  }
  
  public String getComment() {
    return comment;
  }
  
  public void InsertRatingAndCommenting() { 
      String commentToInsert = (this.getComment() != null) ? "'" + this.getComment() + "'" : "NULL";
       
      String query = "INSERT INTO commentrate (student_ID, course_ID, Rate, comment, date) VALUES (" 
                      + StudentId + ", " 
                      + courseId + ", " 
                      + this.getRate() + ", " 
                      + commentToInsert + ", '" 
                      + currentDate + "')";   

      DatabaseOperations.updateData(query);
  }
  
  public void averageRate(int course_ID) {
    String query ="SELECT AVG(Rate) AS average_rating FROM CommentRate WHERE course_ID = "+course_ID+";";
    DatabaseOperations.executeQuery(query);
  }
  
  public void viewComment(int course_ID) { 
      String query = "SELECT * FROM ratings WHERE course_ID = " + course_ID;

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
                  System.out.println("Rate: " + rate);
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
