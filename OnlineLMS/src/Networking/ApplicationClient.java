package Networking;


import model.AccInfromation;
import model.CourseInfo;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import control.ANSIColor;

public class ApplicationClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    public static Object requestOperation(Command request) {
    	try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
               // Request for latest courses
               output.writeObject(request);
               output.flush();
               
               Object receivedData = input.readObject();
               
//               if (receivedData instanceof List<?>) {
//                   List<CourseInfo> dataList = (List<CourseInfo>) receivedData;
//                   return dataList;
//               }
               
               if (receivedData instanceof List<?>) {
            	    // Check if it's a List<CourseInfo>
            	    if (receivedData instanceof List<?>) {
            	        List<?> list = (List<?>) receivedData;

            	        // Check for first value if 
            	        // it is an instance CourseInfo then the whole list is courseInfo
            	        if (!list.isEmpty() && list.get(0) instanceof CourseInfo) {
            	            List<CourseInfo> dataList = (List<CourseInfo>) list;
            	            // Process the CourseInfo list
            	            return dataList;
            	        }
            	        // Check for first value if 
            	        // it is an instance String then the whole list is String
            	        else if (!list.isEmpty() && list.get(0) instanceof String) {
            	            List<String> stringList = (List<String>) list;
            	            // Process the List<String> data
            	            return stringList;
            	        }
            	    }
            	}else if (receivedData instanceof String) {
                   String dataString = (String) receivedData;
                   return dataString;
                   // Process the string data
               } else if (receivedData instanceof Integer) {
                   Integer dataInteger = (Integer) receivedData;
                   return dataInteger;
                  // Process the integer data
               } else if (receivedData instanceof Boolean){
            	   Boolean dataBoolean = (Boolean) receivedData;
            	   return dataBoolean;
               }else if (receivedData instanceof String[]) {
            	   String[] dataStringArray = (String[]) receivedData;
            	   return receivedData;
               }
               else {
            	   System.err.println("The object(s) received type was not recognized ...\nOr it may returned NULL ");
               }

           } catch (IOException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    //old approuch 
//    
//    public static String getReturnType(String operation) {
//        if (operation.equals(getLatestCourses) || 
//            operation.equals(getCoursesByCategoryName) || 
//            operation.equals(getCourses) || 
//            operation.equals(getCoursesInstructor) || 
//            operation.equals(searchCourses)) {
//            return "List<CourseInfo>";
//        } else if (operation.equals(getCourseInfo) || 
//                   operation.equals(updateString) || 
//                   operation.equals(CreateCourse) || 
//                   operation.equals(isCourseIdExistsForUser) || 
//                   operation.equals(enrollCourse) || 
//                   operation.equals(checkEmailAndPassword) || 
//                   operation.equals(checkPassword) || 
//                   operation.equals(isEmailTaken) || 
//                   operation.equals(isEqual) || 
//                   operation.equals(ChangePass) || 
//                   operation.equals(deleteAccount)) {
//            return "boolean";
//        } else if (operation.equals(insertRatingAndComment) || 
//                   operation.equals(updateData) || 
//                   operation.equals(testConnection) || 
//                   operation.equals(getUserInfo)) {
//            return "void";
//        } else if (operation.equals(executeQuery)) {
//            return "ResultSet";
//        } else if (operation.equals(hashPassword)) {
//            return "String";
//        } else if (operation.equals(getRoleByEmail) || 
//                   operation.equals(getRoleByID)) {
//            return "int";
//        } else if (operation.equals(getColumnById)) {
//            return "String";
//        } else if (operation.equals(getConnection)) {
//            return "Connection";
//        } else {
//            return "Unknown operation";
//        }
//    }

    
}
