package view;

import java.sql.SQLException;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;
import model.CateShow;
import model.CourseCategory;
import model.CourseInfo;
import model.CoursePage;
import model.SessionManager;
import model.CoursePage; 

public class MainDashboard {
	private static Scanner scanner;
	static String lightBlue = "\u001B[36m"; // ANSI code for light blue
    static String resetColor = "\u001B[0m";

    static int userId = SessionManager.getInstance().getUserId();
	
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
		
		List<CourseInfo> latestCourses = (List<CourseInfo>) ApplicationClient.requestOperation(new Command("getLatestCourses",new Object[] {}));
		CourseCategory Category =  new CourseCategory();
		
		Boolean valid = false;
	
		switch (choice) {
	        case 4 -> {
	        	for (CourseInfo CourseInfo : latestCourses) {
	                System.out.println(CourseInfo);
	            }
	        	courseChoice();
	        	
	        }
	        case 5 -> {
	        	Category.Category();
	        	courseChoice();
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
	
	public static void returnBack() throws SQLException {
		System.out.print("\n<- return ?: ");
		Scanner scanner = new Scanner(System.in);
		String y = scanner.next();
		userId = SessionManager.getInstance().getUserId();
		if(y.equalsIgnoreCase("y")) {
			
			if((Integer) ApplicationClient.requestOperation(new Command("getRoleByID",new Object[] {(Integer)userId}))== 1) {
				StudentDashboard view = new StudentDashboard();
    			view.mainStudentView();
			}else {
				InstructorDashboard view = new InstructorDashboard();
    			view.mainInstructorView();
			}
			StudentDashboard view = new StudentDashboard();
			view.mainStudentView();
		}
    	
	}
	
	public static void courseChoice() throws SQLException {
        boolean isValidChoice = false;
        while (!isValidChoice) {
            System.out.print("Choose course by ID(0 for going back): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter a Course ID.");
            }
        }
    }

    private static Optional<Integer> getUserInput() {
        try {
            return Optional.of(scanner.nextInt());
        } catch (Exception e) {
            scanner.next(); // Clear invalid input
            return Optional.empty();
        }
    }

    private static boolean handleChoice(int choice) throws SQLException {
    	if(choice == 0 ) {
    		
    		if((Integer) ApplicationClient.requestOperation(new Command("getRoleByID",new Object[] {(Integer)userId}))== 1) {
				StudentDashboard view = new StudentDashboard();
    			view.mainStudentView();
			}else {
				InstructorDashboard view = new InstructorDashboard();
    			view.mainInstructorView();
			}
//			StudentDashboard view = new StudentDashboard();
//			view.mainStudentView();
    	}else {
    		CoursePage course = new CoursePage(choice);  // Assuming CoursePage has a constructor that takes choice as an argument
            course.view(choice);  // Assuming view method in CoursePage accepts an argument
            
    	}
    	return true;
    		
        
    }

	public static void searchThroughCourses() {
		System.out.print(ANSIColor.PURPLE +"Search for courses: "+ANSIColor.RESET);
    	String userInput=  new Scanner(System.in).nextLine();
    	List<CourseInfo> searchedCourse = (List<CourseInfo>) ApplicationClient.requestOperation(new Command("searchCourses",new Object[] {userInput}));
    	for (CourseInfo CourseInfo : searchedCourse)  System.out.println(CourseInfo);
		
	}
}
