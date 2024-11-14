package model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;
import view.InstructorDashboard;

public class ManageCourses {
	static Scanner scanner;
    static int userId = SessionManager.getInstance().getUserId();
	static String name = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"Name", (Integer) userId}));
    static int ID;
    
	public ManageCourses() {
		scanner = new Scanner(System.in);// TODO Auto-generated constructor stub
	}
	
	public static void view() throws SQLException {
		System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET + "| My courses");
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET + "| Create Courses");
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET + "| <- return");
        boolean isValidChoice = false;
        // Loop until the user makes a valid choice
        while (!isValidChoice) {
            System.out.print("Enter your choice (1-2): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter a number between 1,2.");
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
    	myAccount acc = new myAccount();
        switch (choice) {
            case 1 -> {
            	coursesShow();
                return true;
            }
            case 2 -> {
            	CreateCourse();
                return true;
            }
            case 3 -> {
                InstructorDashboard ins = new InstructorDashboard();
                ins.mainInstructorView();
                return true;
            }
            case 4, 5, 6, 7 -> {
                
                return true;
            }
            default -> {
                System.out.println("Invalid choice, please select between 1 and 7.");
                return false;
            }
        }
    }
    
    public static void coursesShow() throws SQLException {
    	int instractorId = SessionManager.getInstance().getUserId();
	    List<CourseInfo> latestCourses = (List<CourseInfo> ) ApplicationClient.requestOperation(new Command("getCoursesInstructor",new Object[] {(Integer) instractorId})); // Retrieve courses

	    // Check if the list is null or empty
	    if (latestCourses == null || latestCourses.isEmpty()) {
	        System.out.println("The course list is empty."); // Message when no courses are found
	        view();
	    } else {
	        for (CourseInfo courseInfo : latestCourses) {
	            System.out.println(courseInfo); // Print each CourseInfo
	        }
	        editAcc(); // Call to editAcc method
	    }
	    
	    
	

    	
    }
    
public static void editAcc() throws SQLException {
		
		
		boolean isValidChoice = false;
		while (!isValidChoice) {
            System.out.print("Do you want edit your courses? y/n: ");
            Optional<String> choice = getUserInput1();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter y or n.");
            }
        }
	}
	
	private static Optional<String> getUserInput1() {
		scanner = new Scanner(System.in);
        try {
            String choice = scanner.nextLine();
            return Optional.of(choice);
        } catch (Exception e) {
            scanner.next(); // Clear invalid input
            return Optional.empty();
        }
    }

    private static boolean handleChoice(String choice) throws SQLException {
    	
    	myAccount myAcc =  new myAccount();
    	
        switch (choice) {
            case "y" -> {
            	checkpass();
                return true;
            }
            case "n" -> {
            	myAcc.myAccountPage();
                return true;
            }
            
            default -> {
                System.out.println("Invalid choice, please select y/n.");
                return false;
            }
        }
    }

    public static void checkpass() throws SQLException {
    	boolean isCorrect = false;
    	EditAcc edit = new EditAcc();
		while (!isCorrect) {
            System.out.print("Enter your password: ");
            scanner.nextLine();
            String pass = scanner.nextLine();
            
            if ((boolean) ApplicationClient.requestOperation(new Command("checkPassword",new Object[] {pass}))) {
                isCorrect = true;
                EditCoursePage();
            } else {
                System.out.println("Invalid password.");
                editAcc();
            }
        }
		
		
    }
    
    public static void getCourseID() {
    	
    }
    
    	public static void EditCoursePage() throws SQLException {
		
		
		System.out.println("\n👤What do you want to change, " + name);

		System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET +"| Title ");
		System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET +"| Descirbtion ");
		System.out.println(ANSIColor.CYAN + "3" + ANSIColor.RESET +"| Credit ");
		
		boolean isValidID = false;
		while (!isValidID) {
			System.out.print("Enter Course ID: ");
			scanner.nextInt();
            int ID = scanner.nextInt();
            
            if((boolean) ApplicationClient.requestOperation(new Command("isCourseIdExistsForUser",new Object[] {(Integer) userId,(Integer) ID}))) {
            	isValidID = true;
            }else {
            	System.err.println("You dont have any course by this ID");
            }
        }
		
		
		boolean isValidChoice = false;
		while (!isValidChoice) {
            System.out.print("Enter your choice (1-3): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleEditChoice(choice.get(),ID);
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            }
        }
		
		
	}

	private static boolean handleEditChoice(int choice,int ID) throws SQLException {
		int CourseID =  ID;
		AccInfromation acc =  new AccInfromation();
		
	    switch (choice) {
	        case 1 -> {
	        	changeTitle();
	            return true;
	        }
	        case 2 -> {
	            
	            return true;
	        }
	        case 3 -> {
	        	
	            return true;
	        }
	        
	        default -> {
	            System.out.println("Invalid choice, please select between 1 and 3.");
	            return false;
	        }
	    }
	    
	    
	}
	
	
	public static void changeTitle() throws SQLException {
        boolean isNameChanged = false;
        Scanner scanner = new Scanner(System.in);
        
        // Get the user's current name
        int userId = SessionManager.getInstance().getUserId();
        
        String currentName = (String ) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"name",(Integer) userId}));
        
        while (!isNameChanged) {
            System.out.println("Current course Title: " + currentName);
            System.out.print("Enter your new Title: ");
            
            // Capture the new name from the user
            scanner.nextLine();
            String newName = scanner.nextLine();
            
            // Validate the new name (only letters and spaces allowed)
            if (newName.matches("[a-zA-Z ]+")) {
                // Update the name in the database
            	
                boolean isUpdated = (boolean ) ApplicationClient.requestOperation(new Command("updateString",new Object[] {"title",newName, (Integer) userId}));
                
                if (isUpdated) {
                    // If update is successful, fetch the updated name
                    currentName = (String ) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"name",(Integer) userId}));
                    System.out.println("\nYour name has been changed to: " + currentName);
                    isNameChanged = true;
                } else {
                    System.out.println("An error occurred while updating your name. Please try again.");
                }
            } else {
                // Inform the user about invalid input
                System.out.println("Invalid name. Please enter a name containing only letters and spaces.");
            }
        }
        
        AccInfromation acc = new AccInfromation();
        String[] userInfo = (String[]) ApplicationClient.requestOperation(new Command("getUserInfo",new Object[] {(Integer) userId}));
        acc.AccInfo(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
        acc.InfoShow();
        acc.editAcc();
        
    }
	
	public static void CreateCourse() throws SQLException {
		Scanner scanner = new Scanner(System.in);
        String Title="", Description, Catagory;
        int Credit;
        
        // Validate name to contain only letters
        while (true) {
            System.out.print("Enter Title: ");
            
            Title = scanner.nextLine(); // Trim leading/trailing spaces
            System.out.println("Title setted: "+Title);
            // Check if the input is null or empty
            if (Title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
            } else if (Title.matches("[a-zA-Z0-9 ]+")) {
            	
                break; // Exit the loop if the title is valid
            } else {
                System.out.println("Invalid Title. Please enter a title containing only letters and numbers.");
            }
        }



        // Validate email format
        
            System.out.print("Enter Description: ");
            
            Description = scanner.nextLine();
          
        // Validate password (at least 8 characters, one letter, one digit, and one special character)
            CourseCategory cate = new CourseCategory();
            String[] categories = cate.cateList();
            while (true) {
                System.out.print("Enter course category: "); 
                
                Catagory = scanner.nextLine();

                // Check if the entered category matches any in the predefined categories
                boolean isValidCategory = false;
                for (String validCategory : categories) {
                    if (validCategory.equalsIgnoreCase(Catagory)) {
                        isValidCategory = true; // Set flag if match found
                        break;
                    }
                }

                if (isValidCategory) {
                    break; // Exit the loop if a valid category is entered
                } else {
                    System.out.println("Invalid category. Please enter one of the following: " + Arrays.toString(categories));
                }
            }

        // Validate credit to start be integer+
            while (true) {
                System.out.print("Enter a Credit (positive integer): ");
               
                Credit = scanner.nextInt(); 
                
                try {
                    // Try to parse the input to an integer
                    int number = Credit;
                    
                    // Check if the number is positive
                    if (number > 0) {
                        System.out.println("You entered: " + number + " for credit."); // Successful input
                        break; // Exit the loop
                    } else {
                        System.out.println("Please enter a positive integer (greater than 0).");
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where input is not a valid integer
                    System.out.println("Invalid input. Please enter a valid positive integer.");
                }
            }
        int instractorId =SessionManager.getInstance().getUserId();
        if ((boolean) ApplicationClient.requestOperation(new Command("CreateCourse",new Object[] {Title, Description,(Integer) Credit, Catagory, (Integer)instractorId}))) {
            System.out.println("your course created succesfully");
            view();
        } else {
            System.err.println("Not Created.");
            view();
        }

    
    } 
	

}
