package control;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.CourseInfo;
import model.SessionManager;
import model.CoursePage;

public class CourseOps {
    // Method to retrieve the five newest courses
    public static List<CourseInfo> getLatestCourses() {
        List<CourseInfo> courses = new ArrayList<>();
        String query = "SELECT Course_ID, Title, Description, Credit, Catagory_Name FROM course ORDER BY created_at DESC LIMIT 3"; // Adjust 'created_at' based on your database schema

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
            	int id = rs.getInt("Course_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                int credit = rs.getInt("Credit");
                String categoryName = rs.getString("Catagory_Name");
                
                courses.add(new CourseInfo(id,title, description, credit, categoryName)); // Add course information to the list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        
        return courses; // Return the list of courses
    }
    
    public static List<CourseInfo> getCoursesByCategoryName(String categoryName) {
        List<CourseInfo> courses = new ArrayList<>();
        String query = "SELECT Course_ID, Title, description, Credit, Catagory_Name FROM course WHERE Catagory_Name = ?";
        
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            // Set the category name parameter in the prepared statement
            pstmt.setString(1, categoryName);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterate through the result set and add courses to the list
                while (rs.next()) {
                    
                    int id = rs.getInt("Course_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    int credit = rs.getInt("Credit");
                     categoryName = rs.getString("Catagory_Name");
                    
                    courses.add(new CourseInfo(id,title, description, credit, categoryName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        
        return courses; // Return the list of courses
    }

    public static List<CourseInfo> getCourses() {
        List<CourseInfo> courses = new ArrayList<>();
        String query = "SELECT Course_ID, Title, description, Credit, Catagory_Name FROM course LIMIT 10";
        
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            // Set the category name parameter in the prepared statement
            

            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterate through the result set and add courses to the list
                while (rs.next()) {
                    
                    int id = rs.getInt("Course_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    int credit = rs.getInt("Credit");
                    String categoryName = rs.getString("Catagory_Name");
                    
                    courses.add(new CourseInfo(id,title, description, credit, categoryName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        
        return courses; // Return the list of courses
    }
    
    public static boolean getCourseInfo(int id) {
        
        String query = "SELECT Course_ID, Title, description, Credit, Catagory_Name FROM course WHERE Course_ID = ?";
        
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            // Set the category name parameter in the prepared statement
        	pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterate through the result set and add courses to the list
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    int credit = rs.getInt("Credit");
                    String categoryName = rs.getString("Catagory_Name");
                    CoursePage course = new CoursePage(id);
                    course.setCourseInfo(title, description, credit, categoryName);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false;
        }
        
        return false; 
    }
    
    public static List<CourseInfo> getCoursesInstructor() {
        List<CourseInfo> courses = new ArrayList<>();
        String query = "SELECT Course_ID, Title, Description, Credit, Catagory_Name FROM course WHERE Instructor_ID = ?"; // Ensure 'user_ID' is a valid column name

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, SessionManager.getInstance().getUserId()); // Set user ID parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterate through the result set and add courses to the list
                while (rs.next()) {
                    int id = rs.getInt("Course_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description"); // Ensure column name matches
                    int credit = rs.getInt("Credit");
                    String categoryName = rs.getString("Catagory_Name"); // Ensure column name matches
                    
                    courses.add(new CourseInfo(id, title, description, credit, categoryName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        
        return courses; // Return the list of courses
    }
    public boolean updateString(String column,String newData, int CourseId) {
        String query = "UPDATE course SET " + column + " = ? WHERE Course_ID = ?";
        

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            
            pstmt.setString(1, newData);      
            pstmt.setInt(2, CourseId);      // Set the user ID

            int rowsAffected = pstmt.executeUpdate();  // Execute update query

            return rowsAffected > 0;  // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if an exception occurs
    }
    
    public static boolean CreateCourse(String Title, String Desc, int Credit, String Cate) {
        String sql = "INSERT INTO course (title, description, credit, Instructor_ID, Catagory_Name,created_at) VALUES (?, ?, ?, ?, ?,?)";

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            
        	Date CurrentDate = Date.valueOf(LocalDate.now());
            // Set the values for the placeholders
            pstmt.setString(1, Title);
            pstmt.setString(2, Desc);
            pstmt.setInt(3, Credit);  // Use the hashed password
            pstmt.setInt(4, SessionManager.getInstance().getUserId());
            pstmt.setString(5, Cate);
            pstmt.setDate(6, CurrentDate);

            // Execute the insert operation
            int rowsAffected = pstmt.executeUpdate();

            // Check if the insertion was successful
            return rowsAffected > 0;  // Return true if at least one row was affected

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();  // Log the error message (optional, for debugging)
            return false;  // Return false if there's an exception
        }
    }
    
    public static boolean isCourseIdExistsForUser(int userId, int courseId) {
        String query = "SELECT COUNT(*) FROM course WHERE user_ID = ? AND Course_ID = ?";
        boolean exists = false;

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);   // Set user ID
            pstmt.setInt(2, courseId);  // Set course ID

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    exists = (count > 0); // If count is greater than 0, the course ID exists for the user
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly in your application
        }

        return exists; // Return true if course ID exists for the user, false otherwise
    }
    
    public static boolean isStudentEnroll(int userId, int courseId) {
    	String query = "SELECT COUNT(*) FROM enroll WHERE student_ID = ? AND course_ID = ?";
    	boolean exists = false;
    	
    	try (Connection conn = DatabaseOperations.getConnection();
    			PreparedStatement pstmt = conn.prepareStatement(query)) {
    		
    		pstmt.setInt(1, userId);   // Set user ID
    		pstmt.setInt(2, courseId);  // Set course ID
    		
    		try (ResultSet rs = pstmt.executeQuery()) {
    			if (rs.next()) {
    				int count = rs.getInt(1);
    				exists = (count > 0); // If count is greater than 0, the course ID exists for the user
    			}
    		}
    	} catch (SQLException e) {
    		e.printStackTrace(); // Handle exceptions properly in your application
    	}
    	
    	return exists; // Return true if course ID exists for the user, false otherwise
    }
    
    public static void insertRatingAndComment(int studentId, int courseId, int rate, String comment, LocalDate date) throws SQLException {
        String query = "INSERT INTO commentrate (student_ID, course_ID, Rate, comment, date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseOperations.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.setInt(3, rate);
            preparedStatement.setString(4, comment);
            preparedStatement.setObject(5, date);
            
            preparedStatement.executeUpdate();
            System.out.println("Rating and comment inserted successfully.");
        } catch (SQLException e) {
            throw new SQLException("Error while inserting rating and comment: " + e.getMessage(), e);
        }
    }
    
    
    public static boolean enrollCourse(int userId, int courseId,int credit) {
        String checkCreditQuery = "SELECT credit FROM user WHERE user_id = ?";
        String courseCreditQuery = "SELECT credit FROM course WHERE course_id = ?";
        String updateUserCreditsQuery = "UPDATE user SET credit = ? WHERE user_id = ?";
        String enrollQuery = "INSERT INTO enroll (student_id, course_id, date) VALUES (?, ?, ?)";
        String creditTransactionQuery = "INSERT INTO credit (user_id, credit, type, date) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseOperations.getConnection();
            conn.setAutoCommit(false);  // Begin transaction

            // Step 1: Get user's available credits
            try (PreparedStatement checkUserCreditsStmt = conn.prepareStatement(checkCreditQuery)) {
                checkUserCreditsStmt.setInt(1, userId);
                ResultSet rs = checkUserCreditsStmt.executeQuery();
                if (!rs.next()) {
                    conn.rollback();
                    return false;  // User not found
                }
                int userCredits = rs.getInt("credit");

                // Step 2: Get course credit requirement
                try (PreparedStatement checkCourseCreditsStmt = conn.prepareStatement(courseCreditQuery)) {
                    checkCourseCreditsStmt.setInt(1, courseId);
                    ResultSet courseRs = checkCourseCreditsStmt.executeQuery();
                    if (!courseRs.next()) {
                        conn.rollback();
                        return false;  // Course not found
                    }
                    int courseCredits = courseRs.getInt("credit");

                    // Step 3: Check if user has enough credits to enroll
                    if (userCredits < courseCredits) {
                        conn.rollback();
                        System.out.println("You Don't have enough credit");
                        return false;
                    }

                    // Step 4: Deduct credits and update user record
                    int updatedCredits = userCredits - courseCredits;
                    try (PreparedStatement updateUserCreditsStmt = conn.prepareStatement(updateUserCreditsQuery)) {
                        updateUserCreditsStmt.setInt(1, updatedCredits);
                        updateUserCreditsStmt.setInt(2, userId);
                        updateUserCreditsStmt.executeUpdate();
                    }

                    // Step 5: Insert enrollment record
                    try (PreparedStatement enrollStmt = conn.prepareStatement(enrollQuery)) {
                        enrollStmt.setInt(1, userId);
                        enrollStmt.setInt(2, courseId);
                        enrollStmt.setDate(3, Date.valueOf(LocalDate.now()));
                        enrollStmt.executeUpdate();
                    }

                    // Step 6: Record transaction in credit table
                    try (PreparedStatement creditTransactionStmt = conn.prepareStatement(creditTransactionQuery)) {
                        creditTransactionStmt.setInt(1, userId);
                        creditTransactionStmt.setInt(2, credit);
                        creditTransactionStmt.setString(3, "sub");
                        creditTransactionStmt.setDate(4, Date.valueOf(LocalDate.now()));
                        creditTransactionStmt.executeUpdate();
                    }

                    // Commit transaction
                    conn.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback transaction on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Reset auto-commit to default
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
