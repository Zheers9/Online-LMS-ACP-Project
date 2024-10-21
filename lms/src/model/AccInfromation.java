package model;

import java.util.Optional;
import java.util.Scanner;

import control.DatabaseOperations;

public class AccInfromation {
	static Scanner scanner;
	private static String name;
	private static String email;
	private static String field;
	private static int credit;
	private static String role;
	static final String LIGHT_BLUE = "\u001B[36m"; // ANSI code for light blue
    static final String RESET_COLOR = "\u001B[0m";
    
	public  void AccInfo(String name,String email,String field, int credit, String role) {
    	this.name = name;
        this.email =  email;
        this.field =  field;
        this.credit = credit;
        this.role = role;
        
    }
	
	public static void InfoShow() {
		System.out.println(LIGHT_BLUE + "\nYour Account Infromation" + RESET_COLOR);
		System.out.println(LIGHT_BLUE + "Name|" + RESET_COLOR + name);
        System.out.println(LIGHT_BLUE + "Email|" + RESET_COLOR + email);
        System.out.println(LIGHT_BLUE + "Field|" + RESET_COLOR + field);
        System.out.println(LIGHT_BLUE + "Credit|" + RESET_COLOR + credit);
        System.out.println(LIGHT_BLUE + "role|" + RESET_COLOR + role);
        
       
	}
	
	public static void editAcc() {
		
		
		boolean isValidChoice = false;
		while (!isValidChoice) {
            System.out.print("Do you want edit your infromations? y/n: ");
            Optional<String> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter y or n.");
            }
        }
	}
	
	private static Optional<String> getUserInput() {
		scanner = new Scanner(System.in);
        try {
            String choice = scanner.nextLine();
            return Optional.of(choice);
        } catch (Exception e) {
            scanner.next(); // Clear invalid input
            return Optional.empty();
        }
    }

    private static boolean handleChoice(String choice) {
    	DatabaseOperations DB =  new DatabaseOperations();
    	
    	AccInfromation acc =  new AccInfromation();
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

    public static void checkpass() {
    	boolean isCorrect = false;
    	EditAcc edit = new EditAcc();
		while (!isCorrect) {
            System.out.print("Enter your password: ");
            String pass = scanner.nextLine();
            DatabaseOperations DB = new DatabaseOperations();

            if (DB.checkPassword(pass)) {
                isCorrect = true;
                edit.EditAccountPage();
            } else {
                System.out.println("Invalid password.");
                editAcc();
            }
        }
		
		
    }
    // Getters
    public String getName() {
    	return name;
    }

    public String getEmail() {
        return email;
    }

    public int getCredit() {
        return credit;
    }
    public String getfield() {
        return field;
    }
    public String getRole() {
    	return role;
    }
    
    

    
}
