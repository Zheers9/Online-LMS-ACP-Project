package model;

public class AccInfromation {
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