package model;

import java.sql.SQLException;
import java.util.List;

import Networking.ApplicationClient;
import Networking.Command;
import control.HandleChoice;

public class CateShow {

    public static void show(String catName) throws SQLException {
        System.out.println("| " + catName + ":");
        if(catName.equalsIgnoreCase("ALL")) {
        	
        	 List<CourseInfo> getCourses = (List<CourseInfo> ) ApplicationClient.requestOperation(new Command("getCourses",new Object[] {}));
        	 getCourses.forEach(System.out::println);
        	 
        }else {
        	 List<CourseInfo> latestCourses = (List<CourseInfo> ) ApplicationClient.requestOperation(new Command("getCoursesByCategoryName",new Object[] {catName}));
             latestCourses.forEach(System.out::println);
        }
       
        HandleChoice handler = new HandleChoice();
        handler.courseChoice();
    }

    
}

