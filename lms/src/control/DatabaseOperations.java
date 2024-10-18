package control;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import model.AccInfromation;
import model.SessionManager;

import java.sql.ResultSet;


public class DatabaseOperations {
    private final static String URL = "jdbc:mysql://localhost:3306/lms";
    private final static String username = "root";
    private static String password = "123456";
    
    private static Connection connect ;
    private static Statement statement ;
    
    
    public static void executeQuery(String query) { 
        try {
        	Connection conn = getConnection();
            statement = connect.createStatement();
            statement.executeQuery(query);
            
            statement.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    } 
 
    // Insert, Delete, Update
    public static void updateData(String query) {
        try {
        	Connection conn = getConnection();
            statement = connect.createStatement();
            statement.executeUpdate(query);
            
            statement.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public static void testConnection() {
        try {
        	Connection conn = getConnection();
            if (connect != null) {
                System.out.println("Connection successful!");
            }
            connect.close();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
    
    
    
    
    
    public boolean checkEmailAndPassword(String email, String password) {
        String query = "SELECT password,user_ID FROM user WHERE email = ?";
        
        
        
        try (Connection conn = getConnection(); // Assume getConnection() method exists to establish a DB connection
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) { // If a record is found
                String storedHashedPassword = rs.getString("password"); // Get the stored hashed password
                int userID = rs.getInt("user_ID"); // Get the stored hashed password
                String hashedInputPassword = hashPassword(password); // Hash the input password

                SessionManager.getInstance().setUserId(userID); //store ID of user
                // Compare the hashed passwords
                return storedHashedPassword.equals(hashedInputPassword);
                
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in production code
        }
        return false; // Return false if no record found or an exception occurs
    }
	
    
    // Method to hash the password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString(); // Return the hashed password
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // Handle exceptions appropriately
        }
    }
    
    
    public int getRoleByEmail(String email) throws SQLException {
        int id = -1; // Initialize with -1 to indicate "not found" or error
        String query = "SELECT role FROM user WHERE email = ?";
        
        try (Connection conn = getConnection(); // Assuming you have a method to get a connection
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the email parameter in the prepared statement
            pstmt.setString(1, email);

            // Execute the query and process the result
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("role"); // Get the id from the result set
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception (you can log it or throw it further)
        }

        return id; // Return the id (or -1 if not found)
    }
    
    
    public static boolean signup(String name, String email, String password, String field, int role) {
        String sql = "INSERT INTO user (name, email, password, field, role) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Hash the password using SHA-256 before storing it
            String hashedPassword = hashPassword(password);

            // Set the values for the placeholders
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);  // Use the hashed password
            pstmt.setString(4, field);
            pstmt.setInt(5, role);

            // Execute the insert operation
            int rowsAffected = pstmt.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                return true;
            }

        } catch (SQLException e) {
            // Handle any SQL exceptions
            return false;
        }

        return false;
    }
    
    public String getUserNameById(int userId) {
        String userName = null;
        String query = "SELECT name FROM user WHERE user_ID = ?";  // SQL query to fetch name by user ID
        
        try (Connection conn = getConnection();  // Assuming you have a method to get the DB connection
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);  // Set the user ID in the query

            try (ResultSet rs = pstmt.executeQuery()) {  // Execute the query
                if (rs.next()) {
                    userName = rs.getString("name");  // Retrieve the name from the result set
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        }

        return userName;  // Return the name (or null if not found)
    }

    public static void getUserInfo(int userId) {
    	 String name;
    	 String email;
    	 int credit;
    	 
    	 String field;
    	 int roleID;
    	 String role;
        String query = "SELECT name,email,credit,field,role FROM user WHERE user_ID = ?";  // SQL query to fetch name by user ID
        
        try (Connection conn = getConnection();  // Assuming you have a method to get the DB connection
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);  // Set the user ID in the query

            try (ResultSet rs = pstmt.executeQuery()) {  // Execute the query
                if (rs.next()) {
                    name = rs.getString("name");  // Retrieve the name from the result set
                    email = rs.getString("email");
                    credit = rs.getInt("credit");  // Retrieve the name from the result set
                    field = rs.getString("field");
                    roleID = rs.getInt("role");
                    if(roleID == 1)
                    	role = "student";
                    else if(roleID == 2)
                    	role = "Instructor";
                    else
                    	role = "Admin";
                    	
                    AccInfromation accInfo = new AccInfromation();
                    accInfo.AccInfo(name, email, field, credit, role);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        }

         
    }
    
    public static Connection getConnection() throws SQLException {

        return  DriverManager.getConnection(URL, username, password);
        
    }
    

	
    
    
    

 
}
