package view;
import java.sql.SQLException;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import java.util.Optional;
import model.SessionManager;
import view.*;


public class LoginAndSignUp {
   
	private Scanner scanner;

    public LoginAndSignUp() {
        scanner = new Scanner(System.in);
    }

    public void start() throws SQLException {
    	System.out.println("Welcome to the Learning Management System!");
        System.out.println("Please choose an option:");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        
        getUserChoice()
            .ifPresentOrElse(
                choice -> handleUserChoice(choice),
                () -> {
                     System.out.println("please choice 1,2");
                }
            );
    }
    
    private Optional<Integer> getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim()); // Read input and remove extra spaces
            return Optional.of(choice);
        } catch (NumberFormatException e) {
            return Optional.empty(); // Return empty if input is not a valid number
        }
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
    	String getInfo="""
    			Select your role:
    			1. Student
    			2. Instructor
    			3. Exite
    			""";
    	
        System.out.println(getInfo);

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

    private void login() throws SQLException {
        
        
        //query to check email,password is valid
        int Attempt =  0;
        int MaxAttempt =  5;
        Boolean valid = false;
        while(!valid && (MaxAttempt-Attempt) > 0) {
        	System.out.println();
        	System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            
            SessionManager.getInstance().setUserId((Integer) ApplicationClient.requestOperation(new Command("getIdThroughEmail",new Object[] {email})));
            
        	if ((boolean) ApplicationClient.requestOperation(new Command("checkEmailAndPassword",new Object[] {email, password}))) {
        		valid = true;
        		
                Optional<Integer> optionalId = Optional.ofNullable((Integer) ApplicationClient.requestOperation(new Command("getRoleByEmail",new Object[] {email})));

                optionalId.ifPresentOrElse(id -> {
                    switch (id) {
                    	case 0->{
                    	try {
							AdminDashboard.mainAdminView();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	}
                        case 1 -> {
                            
                            try {
                            	new StudentDashboard().mainStudentView();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                        case 2 -> {
                            
                            try {
                            	new InstructorDashboard().mainInstructorView();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                        case 3 -> {
                        	new InstructorDashboard();
                            
                        }
                        default -> System.out.println("Invalid user ID.");
                    }
                }, () -> System.out.println("ID not found for the given email."));
                		 
            } else {
            	 
            	Attempt++;
            	if( (MaxAttempt-Attempt) > 0)
            		System.err.println("Invalid email or password, Please try again. " + (MaxAttempt-Attempt) +" Attempt is left");
            	else
            		System.err.println("Invalid email or password. Please try again later, No Attempt is left");
            	
            }
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
            
            // Check if the email format is valid
            if (email.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
                // Check if the email is already taken
                if (!(boolean) ApplicationClient.requestOperation(new Command("isEmailTaken",new Object[] {email}))) {
                    break; // Exit the loop if the email is valid and not taken
                } else {
                    // Print error message and ask for email again
                    System.err.println("The email is already taken.\n");
                }
            } else {
                // Print invalid email message and ask for email again
                System.out.println("Invalid email. Please enter a valid email address.\n");
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
        
        
        
        if ((boolean) ApplicationClient.requestOperation(new Command("signUp",new Object[] {name, email, pass, field, (Integer)role}))) {
            switch (role) {
                case 1 -> {
                    new StudentDashboard().mainStudentView();
                }
                case 2 -> {
                	new StudentDashboard().mainStudentView();
                }
                case 3 -> {
                    new InstructorDashboard();
                   
                }
                default -> System.out.println("Invalid user role.");
            }
        } else {
            System.err.println("Sign-up failed. Please try again.");
        }

    
    } 
    
}
