package view;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Optional;

import control.DatabaseOperations;
import view.*;


public class LoginAndSignUp {
    private Scanner scanner;

    public LoginAndSignUp() {
        scanner = new Scanner(System.in);
    }

    public void start() throws SQLException {
    	String welcomeToLoginAndSignUpText ="""
    			Welcome to the Learning Management System!
    			Please choose an option:
    			1. Create Account
    			2. Login
    			""";
    	System.out.println(welcomeToLoginAndSignUpText);
    	int choice = Integer.parseInt(scanner.nextLine().trim());
    	
        handleUserChoice(choice);
    }
    
    
    private void handleUserChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> createAccount();
                case 2 -> login();
                default -> {
                    System.out.println("invalid choice please try again");
                    start(); // Restart the process for invalid option
                }
            }
        } catch (SQLException e) {
            System.err.println("Error processing your request: " + e.getMessage());
        }
    }
    
    private void createAccount() throws SQLException {
    	String getUserTypeText="""
    			Select your role:
    			1. Student
    			2. Instructor
    			3. Exite
    			""";
    	
        System.out.println(getUserTypeText);

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
        	createAcc(choice);
        } else if (choice == 2) {
        	createAcc(choice);
        } else if (choice==3) {
        	System.out.println("bye bye");
            System.exit(0); // Restart the account creation process
        }
    }

    private static final int MAX_ATTEMPTS = 5;

    private void login() throws SQLException {
        int attempts = 0;
        boolean isValid = false;

        while (!isValid && attempts < MAX_ATTEMPTS) {
        	System.out.println();
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (DatabaseOperations.checkEmailAndPassword(email, password)) {
                isValid = true;
                Optional<Integer> optionalRole = Optional.ofNullable(DatabaseOperations.getRoleByEmail(email));
                
                optionalRole.ifPresentOrElse(role -> {
                    try {
                        launchDashboardForRole(role);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }, () -> System.err.println("No ID found for the given email."));
            }

            
            else {
                attempts++;
                if ((MAX_ATTEMPTS - attempts) > 0) {
                    System.err.println("Invalid email or password. Please try again. " + (MAX_ATTEMPTS - attempts) + " attempt(s) left.");
                } else {
                    System.err.println("Invalid email or password, No attempts left.");
                }
            }
        }
    }

    private void launchDashboardForRole(int roleId) throws SQLException {
        switch (roleId) {
            case 1 -> new StudentDashboard().mainStudentView();
            case 2 -> new InstructorDashboard().mainInstructorView();
            default -> System.out.println("Invalid user role ID.");
        }
    }


    private void createAcc(int role) throws SQLException {
    	
    	Scanner scanner = new Scanner(System.in);
        String name, email, pass, field;

        // Validate name to contain only letters
        while (true) {
        	
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            if (name.matches("[a-zA-Z ]+")) {
                break; // Exit the loop if the name is valid
            } else {
                System.out.println("Invalid name. Please enter a name containing only letters.");
            }
        }

        // Validate email format
        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (email.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
                break; // Exit the loop if the email is valid
            } else {
                System.out.println("Invalid email. Please enter a valid email address.");
            }
        }

        // Validate password (at least 8 characters, one letter, one digit, and one special character)
        while (true) {
            System.out.print("Enter your password: ");
            pass = scanner.nextLine();
            if (pass.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                break; // Exit the loop if the password is valid
            } else {
                System.out.println("Invalid password. It must contain at least 8 characters, one letter, one number, and one special character.");
            }
        }

        // Validate field to start with a letter
        while (true) {
            System.out.print("Enter your field: ");
            field = scanner.nextLine();
            if (field.matches("^[a-zA-Z].*")) {
                break; // Exit the loop if the field starts with a letter
            } else {
                System.out.println("Invalid field. It must start with a letter.");
            }
        }
        
        
        
        DatabaseOperations set =  new DatabaseOperations();
        if (set.signup(name, email, pass, field, role)) {
            switch (role) {
                case 1 -> {
                    StudentDashboard studentDashboard = new StudentDashboard();
                    studentDashboard.mainStudentView();
                }
                case 2 -> {
                	StudentDashboard studentDashboard = new StudentDashboard();
                    studentDashboard.mainStudentView();
                }
                case 3 -> {
                    InstructorDashboard instructorDashboard = new InstructorDashboard();
                   
                }
                default -> System.out.println("Invalid user role.");
            }
        } else {
            System.err.println("Sign-up failed. Please try again.");
        }

    
    } 
    
}
