package Networking;


import model.CourseInfo;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

import control.ANSIColor;

public class DatabaseServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            String ASCIIArt=ANSIColor.GREEN+"""
            		
███████╗███████╗██████╗ ██╗   ██╗███████╗██████╗   
██╔════╝██╔════╝██╔══██╗██║   ██║██╔════╝██╔══██╗  
███████╗█████╗  ██████╔╝██║   ██║█████╗  ██████╔╝  
╚════██║██╔══╝  ██╔══██╗╚██╗ ██╔╝██╔══╝  ██╔══██╗  
███████║███████╗██║  ██║ ╚████╔╝ ███████╗██║  ██║  
╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝  
                                                                                                               

            		""";
			System.out.println("Server is running on port " + PORT+"\n\n"+ASCIIArt);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle client requests
    public static class ClientHandler extends Thread {
    	
    	ObjectOutputStream output;
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        public void handleCommand(Command command) {
            String methodName = command.getMethodName();
            Object[] parameters = command.getParameters();
            try {
                /*
                 * Get the parameter type
                 * Note: Parameters will be assigned in classes 
                 * means if an integer was a parameter it will assign it 
                 * in integer class not " int " class and that's true for others.
                 * */
                Class<?>[] parameterTypes = new Class<?>[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameterTypes[i] = parameters[i].getClass();
                }
                System.out.println("Server recived request for method: "+ANSIColor.RESET+methodName+ANSIColor.GREEN );
                Object result;//result of the output.
                Method method = this.getClass().getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                /*
                 * Invoke the method and get the result,
                 * we put a checking if the method has parameter or not
                 * although java does it automatically but for avoiding 
                 * errors we did it again.
                 * */

				if (parameters == null || parameters.length == 0) {
				        // Call the method with no parameters
				        result = method.invoke(this);
				 } else {
				       // Call the method with 
					 result = method.invoke(this, parameters);
				 }
				
				//Now we check the return type of the method to handle it.
				if (method.getReturnType().equals(Void.TYPE)) {
					/*
					 * you can end a simple acknowledgment or
					 * skip sending data back
					 * you can use:
					 * objectOutputStream.writeObject("OK");
					 * */
					System.out.println("Server Responded Successfully ");
				} else {
					 // Send the actual result for non-void methods
					output.writeObject(result);
					output.flush();
					System.out.println("Server Responded Successfully ");
				}

            } catch (Exception e) {
                e.printStackTrace();
                // Handle error and send error message to client if necessary
            }
        }
        
        @Override
        public void run() {
            try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());) {
            	output = new ObjectOutputStream(clientSocket.getOutputStream());
                
            	Command command =  (Command) input.readObject();
                handleCommand(command);
                
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private final static String URL = "jdbc:mysql://localhost:3306/lms";
        private final static String username = "root";
        private static String password = "";
        
        private static Connection connect ;
        private static Statement statement ;
        
        
        public static ResultSet executeQuery(String query) {
            ResultSet resultSet = null;  
            try {
                Connection conn = getConnection();  
                Statement statement = conn.createStatement();
                resultSet = statement.executeQuery(query);   

                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultSet;   
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
        
        

        public int getIdThroughEmail(String email) {
            String query = "SELECT user_ID FROM user WHERE email = ?";
            Integer userID=0;
            
            
            try (Connection conn = getConnection(); // Assume getConnection() method exists to establish a DB connection
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                 
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) { // If a record is found
                    userID = rs.getInt("user_ID"); 
                    // Compare the hashed passwords
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exceptions appropriately in production code
            }
            return userID; // Return false if no record found or an exception occurs
        }
        
        public List<String> getAllStudent(){
            String query = "select user_ID,name,email,credit,field from user where role=1;";
            List<String> AllStudents = new ArrayList<>();
            
            try (Connection conn = getConnection(); // Assume getConnection() method exists to establish a DB connection
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                 
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) { // If a record is found
                	String userId= rs.getString("user_ID");
                	String name= rs.getString("name");
                	String email= rs.getString("email");
                	String credit= rs.getString("credit");
                	String field= rs.getString("field");
                    AllStudents.add(ANSIColor.CYAN+"User Id: "+ANSIColor.RESET+ userId+"\n"+
                    		ANSIColor.CYAN+"User Name: "+ANSIColor.RESET+ name+"\n"+
                    		ANSIColor.CYAN+"User Email: "+ANSIColor.RESET+ email+"\n"+
                    		ANSIColor.CYAN+"User Credit: "+ANSIColor.RESET+ credit+"\n"+
                    		ANSIColor.CYAN+"User Field: "+ANSIColor.RESET+ field+"\n"+
                    		"------------------------------\n");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exceptions appropriately in production code
            }
        	
			return AllStudents;
        	
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

                     
                    // Compare the hashed passwords
                    return storedHashedPassword.equals(hashedInputPassword);
                    
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exceptions appropriately in production code
            }
            return false; // Return false if no record found or an exception occurs
        }
    	
        public boolean checkPassword(String password,Integer userId) {
        	String query = "SELECT password FROM user WHERE user_ID  = ?";
        	
        	
        	
        	try (Connection conn = getConnection(); // Assume getConnection() method exists to establish a DB connection
        			PreparedStatement pstmt = conn.prepareStatement(query)) {
        		
        		pstmt.setInt(1, userId);
        		ResultSet rs = pstmt.executeQuery();
        		
        		if (rs.next()) { // If a record is found
        			String storedHashedPassword = rs.getString("password"); // Get the stored hashed password
        			
        			String hashedInputPassword = hashPassword(password); // Hash the input password
        			
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
        
        public static int getRoleByID(Integer ID){
            int id = -1; // Initialize with -1 to indicate "not found" or error
            String query = "SELECT role FROM user WHERE user_ID = ?";
            
            try (Connection conn = getConnection(); // Assuming you have a method to get a connection
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                // Set the email parameter in the prepared statement
                pstmt.setInt(1, ID);

                // Execute the query and process the result
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt("role"); // Get the id from the result set
                        return id;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQL exception (you can log it or throw it further)
            }

            return id; // Return the id (or -1 if not found)
        }
        
        
        public boolean signUp(String name, String email, String password, String field, Integer role) {
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
                return rowsAffected > 0;  // Return true if at least one row was affected

            } catch (SQLException e) {
                // Handle any SQL exceptions
                e.printStackTrace();  // Log the error message (optional, for debugging)
                return false;  // Return false if there's an exception
            }
        }

        
        public String getColumnById(String column,Integer userId) {
            String userName = ANSIColor.RED+"Not Available"+ANSIColor.RESET;
            String query = "SELECT " + column + " FROM user WHERE user_ID = ?";  // SQL query to fetch a column value by user ID

            try (Connection conn = getConnection(); // Assuming you have a method to get the DB connection
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userId);  // Set the user ID in the query

                try (ResultSet rs = pstmt.executeQuery()) { // Execute the query and get ResultSet
                    

                    // Check if there is a result before trying to get the column data
                    if (rs.next()) {
                        userName = rs.getString(column); // Retrieve the name from the result set
                    } else {
                        System.err.println("No data found for the given user ID.");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();  // Handle SQL exceptions
            }
            
            return userName;  // Return the name (or null if not found)
        }

        public static String[] getUserInfo(Integer userId) {
       	 
       	 String[] userInfo = new String[5];
           String query = "SELECT name,email,credit,field,role FROM user WHERE user_ID = ?";  // SQL query to fetch name by user ID
           
           try (Connection conn = getConnection();  // Assuming you have a method to get the DB connection
                PreparedStatement pstmt = conn.prepareStatement(query)) {
               
               pstmt.setInt(1, userId);  // Set the user ID in the query

               try (ResultSet rs = pstmt.executeQuery()) {  // Execute the query
                   if (rs.next()) {
                       userInfo[0] = rs.getString("name");  // Retrieve the name from the result set
                       userInfo[1] = rs.getString("email");
                       userInfo[2] = rs.getString("credit");  // Retrieve the name from the result set
                       userInfo[3] = rs.getString("field");
                       userInfo[4] = rs.getString("role");
                       if(userInfo[4].equals("1"))
                       	userInfo[4] = "student";
                       else if(userInfo[4].equals("2"))
                       		userInfo[4] = "Instructor";
                       	else
                       		userInfo[4] = "Admin";
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();  // Handle SQL exceptions
           }

            return userInfo;
       }
        
        
        public boolean isEmailTaken(String email) {
            String query = "SELECT COUNT(*) FROM user WHERE email = ?";

            try (Connection conn = getConnection(); 
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, email);  // Set the email to check

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);  // Get the count of records
                        return count > 0;  // Return true if email is taken (count > 0)
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;  // Return false if no records were found or an exception occurs
        }
        
        public static Boolean isEqual(String Compare,Integer userId) {
        	String query = "SELECT password FROM user WHERE user_ID = ?";
            try (Connection conn = getConnection(); 
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
            	 pstmt.setInt(1, userId); 
               
            	String hashedPassword = hashPassword(Compare);
                try (ResultSet rs = pstmt.executeQuery()) {
                	if (rs.next()) {
                        String dbPassword = rs.getString("password");  // Get the password from the database
                        
                        // Compare the passwords
                        if (dbPassword.equals(hashedPassword)) {
                        	return true; 
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        
        public Boolean ChangePass(String NewPass, Integer userId) {
            String query = "UPDATE user SET password = ? WHERE user_ID = ?";
            boolean isPasswordChanged = false;

            try (Connection conn = getConnection();  // Assuming you have a method to get the DB connection
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                String hashedPassword = hashPassword(NewPass);  // Hash the new password
                pstmt.setString(1, hashedPassword); 
                pstmt.setInt(2, userId);  // Set the user ID in the query

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    isPasswordChanged = true;  // Password successfully changed
                }

            } catch (SQLException e) {
                e.printStackTrace();  // Handle SQL exceptions
            }

            // Return the correct result
            return isPasswordChanged;
        }
        
     // Method to delete a user account based on user ID
        public static boolean deleteAccount(Integer userId) {
            String query = "DELETE FROM user WHERE user_ID = ?"; // SQL delete query
            boolean isDeleted = false;

            try (Connection conn = getConnection(); // Assume this method gets the DB connection
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                // Set the user ID in the query
                pstmt.setInt(1, userId);

                // Execute the delete operation
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    isDeleted = true;  // Account was successfully deleted
                }

            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQL exceptions
            }

            return isDeleted; // Return whether the account was successfully deleted
        }



        
        public static Connection getConnection() throws SQLException {

            return  DriverManager.getConnection(URL, username, password);
            
        }
        

    	
        

    	public static List<CourseInfo> searchCourses(String searchedCourse) {
    	    List<CourseInfo> courses = new ArrayList<>();
    	    String query =
    	    "SELECT Course_ID, Title, Description, Credit, Catagory_Name FROM course WHERE Title LIKE ?;";

    	    try (Connection conn = getConnection();
    	         PreparedStatement pstmt = conn.prepareStatement(query)) {
    	         
    	        // Set the parameter with wildcard for LIKE clause
    	        pstmt.setString(1, "%" + searchedCourse + "%");
    	        
    	        try (ResultSet rs = pstmt.executeQuery()) {
    	            while (rs.next()) {
    	                int id = rs.getInt("Course_ID");
    	                String title = rs.getString("Title");
    	                String description = rs.getString("Description");
    	                int credit = rs.getInt("Credit");
    	                String categoryName = rs.getString("Catagory_Name");
    	                
    	                // Add course information to the list
    	                courses.add(new CourseInfo(id, title, description, credit, categoryName));
    	            }
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace(); // Handle exceptions appropriately
    	    }
    	    return courses; // Return the list after processing all results
    	}
    	
        // Method to retrieve the five newest courses
        public static List<CourseInfo> getLatestCourses() {
            List<CourseInfo> courses = new ArrayList<>();
            String query = "SELECT Course_ID, Title, Description, Credit, Catagory_Name FROM course ORDER BY created_at DESC LIMIT 5"; // Adjust 'created_at' based on your database schema

            try (Connection conn = getConnection();
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
            
            try (Connection conn = getConnection();
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
            
            try (Connection conn = getConnection();
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
        
        public static String[] getCourseInfo(Integer id) {
            
            String query = "SELECT Course_ID, Title, description, Credit, Catagory_Name FROM course WHERE Course_ID = ?";
            String[] courseInfo = new String[4];
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                 
                // Set the category name parameter in the prepared statement
            	pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    // Iterate through the result set and add courses to the list
                    while (rs.next()) {
                        courseInfo[0] = rs.getString("Title");
                        courseInfo[1] = rs.getString("Description");
                        courseInfo[2] = rs.getString("Credit");
                        courseInfo[3]= rs.getString("Catagory_Name");
                        
                       
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
            
            return courseInfo;
             
        }
        
        public static List<CourseInfo> getCoursesInstructor(Integer instractorId) {
            List<CourseInfo> courses = new ArrayList<>();
            String query = "SELECT Course_ID, Title, Description, Credit, Catagory_Name FROM course WHERE Instructor_ID = ?"; // Ensure 'user_ID' is a valid column name
            
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, instractorId); // Set user ID parameter

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
        
        
        public boolean updateUserString(String column,String newData, Integer userId) {
            String query = "UPDATE user SET " + column + " = ? WHERE user_ID = ?";
            

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                
                pstmt.setString(1, newData);      
                pstmt.setInt(2, userId);      // Set the user ID

                int rowsAffected = pstmt.executeUpdate();  // Execute update query

                return rowsAffected > 0;  // Return true if update was successful
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;  // Return false if an exception occurs
        }
        
        
        public boolean updateString(String column,String newData, Integer CourseId) {
            String query = "UPDATE course SET " + column + " = ? WHERE Course_ID = ?";
            

            try (Connection conn = getConnection();
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
        
        public static boolean CreateCourse(String Title, String Desc, Integer Credit, String Cate, Integer InsrtractorId) {
            String sql = "INSERT INTO course (title, description, credit, Instructor_ID, Catagory_Name,created_at) VALUES (?, ?, ?, ?, ?,?)";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                
            	Date CurrentDate = Date.valueOf(LocalDate.now());
                // Set the values for the placeholders
                pstmt.setString(1, Title);
                pstmt.setString(2, Desc);
                pstmt.setInt(3, Credit);  // Use the hashed password
                pstmt.setInt(4, InsrtractorId);
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
        
        public static boolean isCourseIdExistsForUser(Integer userId, Integer courseId) {
            String query = "SELECT COUNT(*) FROM course WHERE user_ID = ? AND Course_ID = ?";
            boolean exists = false;

            try (Connection conn = getConnection();
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
        
        public static boolean isStudentEnroll(Integer userId, Integer courseId) {
        	String query = "SELECT COUNT(*) FROM enroll WHERE student_ID = ? AND course_ID = ?";
        	boolean exists = false;
        	
        	try (Connection conn = getConnection();
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
        
        public static String insertRatingAndComment(Integer studentId, Integer courseId, Integer rate, String comment, LocalDate date) throws SQLException {
            String query = "INSERT INTO commentrate (student_ID, course_ID, Rate, comment, date) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection connection = getConnection(); 
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                 
                preparedStatement.setInt(1, studentId);
                preparedStatement.setInt(2, courseId);
                preparedStatement.setInt(3, rate);
                preparedStatement.setString(4, comment);
                preparedStatement.setObject(5, date);
                
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error while inserting rating and comment: " + e);
            }
            return "Rating and comment inserted successfully.";
        }
        
        public String updateStudentCredit(Integer addedCredit, Integer userId){
        	String query = "UPDATE `user`SET `credit` = `credit` + ? WHERE `user_ID` = ?;";
            
            try (Connection connection = getConnection(); 
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                 
                preparedStatement.setInt(1, addedCredit);
                preparedStatement.setInt(2, userId);
                
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error while updating credit: " + e);
            }
            return "credit successfuly added.";
        }
        
        
        public static boolean enrollCourse(Integer userId, Integer courseId,Integer credit) {
            String checkCreditQuery = "SELECT credit FROM user WHERE user_id = ?";
            String courseCreditQuery = "SELECT credit FROM course WHERE course_id = ?";
            String updateUserCreditsQuery = "UPDATE user SET credit = ? WHERE user_id = ?";
            String enrollQuery = "INSERT INTO enroll (student_id, course_id, date) VALUES (?, ?, ?)";
            String creditTransactionQuery = "INSERT INTO credit (user_id, credit, type, date) VALUES (?, ?, ?, ?)";
            
            Connection conn = null;
            try {
                conn = getConnection();
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
        
        public static String averageRate(Integer course_ID) {
    	    String query = "SELECT AVG(Rate) AS average_rating FROM CommentRate WHERE course_ID = " + course_ID + ";";
    	    double averageRating = 0.0;

    	    try (ResultSet resultSet = executeQuery(query)) {
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

    	private static String convertRatingToStars(Double rating) {
    	    StringBuilder stars = new StringBuilder();

    	    int fullStars = (int) rating.doubleValue(); // Get full star count
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
      


    	  public static List<String> viewComment(Integer course_ID) { 
    	      String query = "SELECT * FROM CommentRate WHERE course_ID = " + course_ID;
    	      List<String> allCommentAndRates= new ArrayList<>();
    	      try { 
    	          ResultSet resultSet = executeQuery(query);
    	           
    	          if (!resultSet.isBeforeFirst()) {
    	              System.out.println("No ratings found for this course.");
    	          } else { 
    	              while (resultSet.next()) { 
    	                  String studentId = resultSet.getString("student_ID");
    	                  String courseId = resultSet.getString("course_ID");
    	                  int rate = resultSet.getInt("Rate");
    	                  String comment = resultSet.getString("comment");
    	                  String date = resultSet.getString("date");

    	                  
    	                  StringBuilder stars = new StringBuilder();
    	                  for (int i = 0; i < rate; i++) {
    	                      stars.append("★"); // Append a star for each point of the rating
    	                  }
    	                  for (int i = rate; i < 5; i++) {
    	                      stars.append("☆"); // Append an empty star for the remaining points
    	                  }
    	                  
    	                  allCommentAndRates.add("Student ID: " + studentId+"\n"+
    	                		  				 "Course ID: " + courseId+"\n"+
    	                		  				 "Rate: " + stars.toString() + "\n" +
    	                          				 "Comment: " + (comment != null ? comment : "No comment") + "\n" +
    	                          				 "Date: " + date + "\n" +
    	                          				 "-------------------------------");
    	                  
    	              }
    	          }
    	 
    	          resultSet.close();
    	      } catch (SQLException e) {
    	          e.printStackTrace();
    	      }
    	      return allCommentAndRates;
    	      
    	  }

        
    }
        
}