package view;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import control.ANSIColor;
import model.MyAccount;
import model.SessionManager;

public class StudentDashboard {
    private static Scanner scanner;

    public StudentDashboard() {
        scanner = new Scanner(System.in);
    }

    public static void mainStudentView() throws SQLException {
    	System.out.println(SessionManager.getInstance().getUserId());
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
                isValidChoice = processChoice(choice.get(), mainDashboard);
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
    }

    private static void displayMenuOptions() {
        System.out.println(ANSIColor.CYAN + "1" + ANSIColor.WHITE + " | 👤 My Account");
        System.out.println(ANSIColor.CYAN + "2" + ANSIColor.WHITE + " | My Courses");
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

    private static boolean processChoice(int choice, MainDashboard mainDashboard) throws SQLException {
        MyAccount myAccount = new MyAccount();
        switch (choice) {
            case 1 -> {
                myAccount.myAccountPage();
                return true;
            }
            case 2, 3 -> {
                // Placeholder for other cases to be handled here if needed.
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
