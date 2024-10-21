package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.CourseInfo;
import model.SessionManager;

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
    
    public static List<CourseInfo> getCoursesInstructor() {
        List<CourseInfo> courses = new ArrayList<>();
        String query = "SELECT Course_ID, Title, description, Credit, Catagory_Name FROM course WHERE user_ID = ? LIMIT 10";
        
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
        	 pstmt.setInt(1, SessionManager.getInstance().getUserId());
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

}
