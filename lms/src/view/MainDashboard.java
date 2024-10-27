package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;
import model.CateShow;
import model.CourseCategory;
import model.CourseInfo;
import model.SessionManager;

public class MainDashboard {
	private static Scanner scanner;
	static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m";
    static DatabaseOperations DB;
	
	public MainDashboard() {
		scanner = new Scanner(System.in);
	}
	
	public static void MainView() {
		
		System.out.println(lightBlue + "\n3" + resetColor +"| Search: 🔍");
		System.out.println(lightBlue + "4" + resetColor +"| new course" );
		System.out.println(lightBlue + "5" + resetColor +"| Category");
		System.out.println(lightBlue + "6" + resetColor +"| Show all");
		System.out.println(lightBlue + "7" + resetColor +"| EXIT");
		
			
	}
	
	public static void choice(int choice) throws SQLException {
		List<CourseInfo> latestCourses = CourseOps.getLatestCourses();
		CourseCategory Category =  new CourseCategory();
		
		Boolean valid = false;
	
		switch (choice) {
	        case 4 -> {
	        	for (CourseInfo CourseInfo : latestCourses) {
	                System.out.println(CourseInfo);
	                
	            }
	        	System.out.print("\n<- return ?: ");
	    		Scanner scanner = new Scanner(System.in);
	    		String y = scanner.next();
	    		int userId = SessionManager.getInstance().getUserId();
	    		if(y.equalsIgnoreCase("y")) {
	    			if(DB.getRoleByID(userId) == 1) {
	    				StudentDashboard view = new StudentDashboard();
		    			view.mainStudentView();
	    			}else {
	    				InstructorDashboard view = new InstructorDashboard();
		    			view.mainInstructorView();
	    			}
	    			StudentDashboard view = new StudentDashboard();
	    			view.mainStudentView();
	    		}
	        	valid = true;
	        }
	        case 5 -> {
	        	Category.Category();
	        	valid = true;
	        }
	        case 6 -> {
	        	CateShow.show("ALL");
	        	valid = true;
	        }
	        case 7 -> {
	        	System.out.println("Good bye, don't stop learning, we waiting for you boss");
	        	System.exit(0);
	        	valid = true;
	        }
	        default -> System.out.println("Invalid choice, please choice 1,2");
		}
	
  
	}
}
