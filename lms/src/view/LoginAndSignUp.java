package view;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import control.DatabaseOperations;

public class LoginAndSignUp {
    static final String LIGHT_BLUE = "\u001B[36m";
    String resetText = "\u001B[0m";
    private Scanner scanner;

    public LoginAndSignUp() {
        scanner = new Scanner(System.in);
    }

    public void start() throws SQLException {
        System.out.println("Welcome to the Learning Management System!");
        System.out.println("Please choose an option:");
        System.out.println(LIGHT_BLUE + "1 |" + resetText + "Create Account");
        System.out.println(LIGHT_BLUE + "2 |" + resetText + "Login");

        do {
            getUserChoice().ifPresentOrElse(
                choice -> {
                	try {
						handleUserChoice(choice);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    return;
                },
                () -> System.out.println("Please choose 1 or 2")
            );
        } while (true);
    }

    private Optional<Integer> getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return Optional.of(choice);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void handleUserChoice(int choice) throws SQLException {
        switch (choice) {
            case 1 -> displayCreateAccountMenu();
            case 2 -> login();
            default -> {
                System.out.println("Invalid choice, please try again.");
                start();
            }
        }
    }

    private void displayCreateAccountMenu() throws SQLException {
        System.out.println("""
                Select your role:
                1. Student
                2. Instructor
                3. Exit
                """);

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1 || choice == 2) {
            createAcc(choice);
        } else if (choice == 3) {
            System.out.println("Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Please enter a valid choice (1-3).");
            displayCreateAccountMenu();
        }
    }

    private void login() throws SQLException {
        DatabaseOperations databaseOps = new DatabaseOperations();
        int attempt = 0, maxAttempt = 5;
        boolean valid = false;

        while (!valid && (maxAttempt - attempt) > 0) {
            System.out.print("\nEnter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (databaseOps.checkEmailAndPassword(email, password)) {
                valid = true;
                Optional<Integer> optionalId = Optional.ofNullable(databaseOps.getRoleByEmail(email));
                
                optionalId.ifPresentOrElse(id -> {
                    try {
                        switch (id) {
                            case 1 -> new StudentDashboard().mainStudentView();
                            case 2 -> new InstructorDashboard().mainInstructorView();
                            case 3 -> new AdminDashbord().mainAdminView();
                            default -> System.out.println("Invalid user ID.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }, () -> System.out.println("ID not found for the given email."));
            } else {
                attempt++;
                if ((maxAttempt - attempt) > 0) {
                    System.err.println("Invalid email or password. " + (maxAttempt - attempt) + " attempts left.");
                } else {
                    System.err.println("No attempts left. Please try again later.");
                }
            }
        }
    }

    protected void createAcc(int role) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DatabaseOperations dbOps = new DatabaseOperations();
        String name, email, pass, field;

        // Validate name
        while (true) {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            if (name.matches("[a-zA-Z ]+")) break;
            System.out.println("Invalid name. Please use letters only.");
        }

        // Validate email
        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (email.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$") && !dbOps.isEmailTaken(email)) break;
            System.out.println("Invalid or taken email. Try again.");
        }

        // Validate password
        while (true) {
            System.out.print("Enter your password: ");
            pass = scanner.nextLine();
            if (pass.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) break;
            System.out.println("Invalid password. Must be at least 8 characters, with one letter, one number, and one special character.");
        }

        // Validate field
        while (true) {
            System.out.print("Enter your field: ");
            field = scanner.nextLine();
            if (field.matches("^[a-zA-Z].*")) break;
            System.out.println("Field must start with a letter.");
        }

        if (dbOps.signup(name, email, pass, field, role)) {
            if (role == 1) {
                new StudentDashboard().mainStudentView();
            } else if (role == 2) {
                new InstructorDashboard().mainInstructorView();
            } else {
                System.out.println("Invalid user role.");
            }
        } else {
            System.err.println("Sign-up failed. Please try again.");
        }
    }
}
