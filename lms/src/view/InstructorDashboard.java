package view;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import control.ANSIColor;
import control.DatabaseOperations;
import model.ManageCourses;
import model.MyAccount;
import model.SessionManager;

public class InstructorDashboard {
    private static final DatabaseOperations DBOps = new DatabaseOperations();
    private static final String name = DBOps.getColumnById("Name", SessionManager.getInstance().getUserId());
    private static final Scanner scanner = new Scanner(System.in);

    public InstructorDashboard() {
    }

    public static void mainInstructorView() throws SQLException {
        System.out.println("\n| Online Learning Management System |");
        
        displayMenuOptions();

        MainDashboard mainDashboard = new MainDashboard();
        mainDashboard.mainView();

        boolean isValidChoice = false;

        // Loop until the user makes a valid choice
        while (!isValidChoice) {
            System.out.print("Enter your choice (1-7): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get(), mainDashboard);
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
    }

    private static void displayMenuOptions() {
        System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET + " | 👤 My Account");
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET + " | Manage Courses");
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

    private static boolean handleChoice(int choice, MainDashboard mainDashboard) throws SQLException {
        MyAccount account = new MyAccount();
        switch (choice) {
            case 1 -> {
                account.myAccountPage();
                return true;
            }
            case 2 -> {
                ManageCourses manageCourses = new ManageCourses();
                manageCourses.view();
                return true;
            }
            case 3 -> {
                // Placeholder for additional cases if needed
                return true;
            }
            case 4, 5, 6, 7 -> {
                mainDashboard.processChoice(choice);
                return true;
            }
            default -> {
                System.out.println("Invalid choice, please select between 1 and 7.");
                return false;
            }
        }
    }
}
