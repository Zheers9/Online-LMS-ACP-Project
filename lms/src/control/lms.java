package control;

import java.sql.SQLException;

import view.LoginAndSignUp;

public class lms {

	public static void main(String[] args) throws SQLException {
        LoginAndSignUp login =  new LoginAndSignUp();
        login.start();
	}

}
