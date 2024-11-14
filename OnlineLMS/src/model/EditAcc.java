package model;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;
import view.LoginAndSignUp;

public class EditAcc {
	
	static String name = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"name",(Integer)SessionManager.getInstance().getUserId()}));// get username by id
    static Scanner scanner;
    
    public EditAcc() {
    	scanner = new Scanner(System.in);
    }
	public static void EditAccountPage() throws SQLException {
		
		
		System.out.println("\n👤What do you want to change, " + name);

		System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET +"| Name ");
		System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET +"| Email ");
		System.out.println(ANSIColor.CYAN + "3" + ANSIColor.RESET +"| Password ");
		System.out.println(ANSIColor.CYAN + "4" + ANSIColor.RESET +"| Field ");
		System.out.println(ANSIColor.CYAN + "5" + ANSIColor.RESET +"| Delete account ");
		
		
		boolean isValidChoice = false;
		while (!isValidChoice) {
            System.out.print("Enter your choice (1-4): ");
            Optional<Integer> choice = getUserInput();

            if (choice.isPresent()) {
                isValidChoice = handleChoice(choice.get());
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
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
    	
    	
        switch (choice) {
            case 1 -> {
            	changeName();
                return true;
            }
            case 2 -> {
                chanegEmail();
                return true;
            }
            case 3 -> {
            	changePass();
                return true;
            }
            case 4 -> {
            	chanegField();
            	return true;
            }
            case 5 -> {
            	Delete();
            	return true;
            }
            
            default -> {
                System.out.println("Invalid choice, please select between 1 and 4.");
                return false;
            }
        }
        
        
    }
    
    public static void Delete() throws SQLException {
    	boolean isAccDelet = false;
        
        
        // Get the user's current name
        int userId = SessionManager.getInstance().getUserId();
        
        String currentName = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"name",(Integer)userId}));
        
        while (!isAccDelet) {
            
            System.out.print("Are you sure want to Delete your account y/n: ");
            
            // Capture the new name from the user
            scanner.nextLine();
            String checkAgain = scanner.nextLine();
            
            // Validate the new name (only letters and spaces allowed)
            if (checkAgain.equalsIgnoreCase("y")) {
                // Update the name in the database
            	
                boolean isUpdated = (boolean) ApplicationClient.requestOperation(new Command("deleteAccount",new Object[] {(Integer)userId}));
                
                if (isUpdated) {
                    // If update is successful, fetch the updated name
                    
                    System.out.println("\nYour account has been delete succufully");
                    LoginAndSignUp starterPage = new LoginAndSignUp();
                    starterPage.start();
                    isAccDelet = true;
                } else {
                    System.out.println("An error occurred while Deleting your account, try again later.");
                }
            } else if(checkAgain.equalsIgnoreCase("n")) {
            	isAccDelet = true;
            }else {
            	System.out.println("Invalid choice, please enter y/n");
            }
        }
        
        AccInfromation acc = new AccInfromation();
        String[] userInfo = (String[]) ApplicationClient.requestOperation(new Command("getUserInfo",new Object[] {(Integer) userId}));
        acc.AccInfo(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
        acc.InfoShow();
        acc.editAcc();
    }
    public static void changeName() throws SQLException {
        boolean isNameChanged = false;
        
        // Get the user's current name
        int userId = SessionManager.getInstance().getUserId();
        
        String currentName = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"name",(Integer)userId}));
        
        while (!isNameChanged) {
            System.out.println("Your current name is: " + currentName);
            System.out.print("Enter your new name: ");
            
            // Capture the new name from the user
            scanner.nextLine();
            String newName = scanner.nextLine();
            
            // Validate the new name (only letters and spaces allowed)
            if (newName.matches("[a-zA-Z ]+")) {
                // Update the name in the database
            	
                boolean isUpdated = (boolean) ApplicationClient.requestOperation(new Command("updateUserString",new Object[] {"name",newName, (Integer)userId}));
                
                if (isUpdated) {
                    // If update is successful, fetch the updated name
                	
                    currentName = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"name",(Integer) userId}));
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

    public static void chanegEmail() throws SQLException {
    	boolean isNameChanged = false;
        
        
        // Get the user's current name
        int userId = SessionManager.getInstance().getUserId();
        
        String currentEmail = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"email",(Integer) userId}));
        
        while (!isNameChanged) {
            System.out.println("Your current email is: " + currentEmail);
            System.out.print("Enter your new email: ");
            
            // Capture the new name from the user
           
            scanner.nextLine();
            String newEmail = scanner.nextLine();
            // Validate the new name (only letters and spaces allowed)
            if (newEmail.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$") ) {
            	
            	
            	if( !(boolean) ApplicationClient.requestOperation(new Command("isEmailTaken",new Object[] {newEmail})))  {
            		boolean isUpdated = (boolean) ApplicationClient.requestOperation(new Command("updateUserString",new Object[] {"email",newEmail,(Integer) userId}));
                    
                    if (isUpdated) {
                        // If update is successful, fetch the updated name
                        currentEmail = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"email",(Integer) userId}));
                        System.out.println("\nYour email has been changed to: " + currentEmail);
                        isNameChanged = true;
                    } else {
                        System.out.println("An error occurred while updating your name. Please try again.");
                    }
            	}else
            		System.err.println("The email is already taken.");
                // Update the name in the database
                
            } else {
                // Inform the user about invalid input
                System.out.println("Invalid email. Please enter a valid email address.ex(name@gmail.com)");
            }
        }
        
        AccInfromation acc = new AccInfromation();
        String[] userInfo = (String[]) ApplicationClient.requestOperation(new Command("getUserInfo",new Object[] {(Integer) userId}));
        acc.AccInfo(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
        acc.InfoShow();
        acc.editAcc();
    }
    public static void changePass() throws SQLException {
    	int userId = SessionManager.getInstance().getUserId();
        while (true) {
            System.out.print("Enter New password: ");
            
            scanner.nextLine();
            String NewPass = scanner.nextLine();

            // Check if the password meets the required criteria
            if (NewPass.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            	
                // Check if the new password is different from the old password
                if (!(boolean) ApplicationClient.requestOperation(new Command("isEqual",new Object[] {NewPass,(Integer)userId }))) {
                	
                    // Try changing the password and give feedback
                    if ((boolean) ApplicationClient.requestOperation(new Command("ChangePass",new Object[] {NewPass, (Integer) userId}))) {
                        System.out.println("Password changed successfully!");
                    } else {
                        System.out.println("Password not changed, try again.");
                    }
                    
                    break;  // Exit the loop after a successful change or failed attempt

                } else {
                    System.out.println("This is your old password. Please enter a different one.");
                }

            } else {
                // Password doesn't meet the criteria
                System.out.println("Invalid password. It must contain at least 8 characters, one letter, one number, and one special character.");
            }
        }
        
        AccInfromation acc = new AccInfromation();
        String[] userInfo = (String[]) ApplicationClient.requestOperation(new Command("getUserInfo",new Object[] {(Integer) SessionManager.getInstance().getUserId()}));
        acc.AccInfo(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
        acc.InfoShow();
        acc.editAcc();
    }

    public static void chanegField() throws SQLException {
    	boolean isNameChanged = false;
       
        
        // Get the user's current name
        int userId = SessionManager.getInstance().getUserId();
        
        String currentField = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"field",(Integer) userId}));
        
        while (!isNameChanged) {
            System.out.println("Your current field is: " + currentField);
            System.out.print("Enter your new field: ");
            
            // Capture the new name from the user
            scanner.nextLine();
            String newField = scanner.nextLine();
            
            // Validate the new name (only letters and spaces allowed)
            if (newField.matches("[a-zA-Z ]+")) {
                // Update the name in the database
            	
                boolean isUpdated = (boolean) ApplicationClient.requestOperation(new Command("updateUserString",new Object[] {"field",newField, (Integer)userId}));
                
                if (isUpdated) {
                    // If update is successful, fetch the updated name
                	currentField = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"field",(Integer) userId}));
                    System.out.println("\nYour field has been changed to: " + currentField);
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
	
}
