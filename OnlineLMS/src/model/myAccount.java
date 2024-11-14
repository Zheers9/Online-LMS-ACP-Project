package model;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import Networking.ApplicationClient;
import Networking.Command;
import control.ANSIColor;
import view.StudentDashboard;

public class myAccount {
    static String name;
    static Scanner scanner;
    static AccInfromation AccInfo;
    
    public myAccount() {
    	scanner = new Scanner(System.in);
    }
	public static void myAccountPage() throws SQLException {
		
		// get username by id
		name = (String) ApplicationClient.requestOperation(new Command("getColumnById",new Object[] {"name", (Integer)SessionManager.getInstance().getUserId()}));
		
		System.out.println("\n👤Welecom, " + name);

		System.out.println(ANSIColor.CYAN + "1" + ANSIColor.RESET +"| My infromation ");
		System.out.println(ANSIColor.CYAN + "2" + ANSIColor.RESET +"| Credit ");
		System.out.println(ANSIColor.CYAN + "3" + ANSIColor.RESET +"| Notification ");
		System.out.println(ANSIColor.CYAN + "4" + ANSIColor.RESET +"| <- Return ");
		
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
    	
    	AccInfromation acc =  new AccInfromation();
    	
        switch (choice) {
            case 1 -> {//choosed my account
            	
                String[] userInfo = (String[]) ApplicationClient.requestOperation(new Command("getUserInfo",new Object[] {(Integer) SessionManager.getInstance().getUserId()}));
            	acc.AccInfo(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
                acc.InfoShow();
                acc.editAcc();
                return true;
            }
            case 2 -> {
                
                return true;
            }
            case 3 -> {
                
                return true;
            }
            case 4 -> {
            	StudentDashboard studntView = new StudentDashboard();
            	studntView.mainStudentView();
            	return true;
            }
            
            default -> {
                System.out.println("Invalid choice, please select between 1 and 4.");
                return false;
            }
        }
    }
	
}
