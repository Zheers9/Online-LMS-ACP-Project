package model;

public class SessionManager {
    private static SessionManager instance;
    private int userId; // Variable to store the logged-in user's ID

    // Private constructor to restrict instantiation
    private SessionManager() {}

    // Get the single instance of SessionManager
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Set the user ID when a user logs in
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Get the user ID from any class
    public int getUserId() {
        return userId;
    }
    
    
        // SessionManager.getInstance().setUserId(userId); // Save the user ID in the singleton
        //  int userId = SessionManager.getInstance().getUserId(); // Get the logged-in user's ID

     
}

