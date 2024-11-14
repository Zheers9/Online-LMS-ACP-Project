package model;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;

public class AccInfromation {
	static Scanner scanner;
	private static String name;
	private static String email;
	private static String field;
	private static String credit;
	private static String role;
    
	public  void AccInfo(String name,String email,String credit, String field, String role) {
    	this.name = name;
        this.email =  email;
        this.field =  field;
        this.credit = credit;
        this.role = role;
        
    }
	
	public static void InfoShow() {
		System.out.println(ANSIColor.CYAN + "\nYour Account Infromation" + ANSIColor.RESET);
		System.out.println(ANSIColor.CYAN + "Name|" + ANSIColor.RESET + name);
        System.out.println(ANSIColor.CYAN + "Email|" + ANSIColor.RESET + email);
        System.out.println(ANSIColor.CYAN + "Field|" + ANSIColor.RESET + field);
        System.out.println(ANSIColor.CYAN + "Credit|" + ANSIColor.RESET + credit);
        System.out.println(ANSIColor.CYAN + "role|" + ANSIColor.RESET + role);
        
       
	}
	
	public static void editAcc() throws SQLException {
		
		
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

    private static boolean handleChoice(String choice) throws SQLException {
    	
    
    	
        switch (choice) {
            case "y" -> {
            	checkpass();
                return true;
            }
            case "n" -> {
            	new myAccount().myAccountPage();
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
            String pass = scanner.nextLine();
            
            
            if ((boolean) ApplicationClient.requestOperation(new Command("checkPassword",new Object[] {pass,(Integer) SessionManager.getInstance().getUserId()}))) {
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

    public String getCredit() {
        return credit;
    }
    public String getfield() {
        return field;
    }
    public String getRole() {
    	return role;
    }
    
    

    
}
