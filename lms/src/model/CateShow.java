package model;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import control.CourseOps;
import control.DatabaseOperations;
import control.HandleChoice;

public class CateShow {
    private static final Scanner scanner = new Scanner(System.in);

    public static void show(String catName) throws SQLException {
        System.out.println("| " + catName + ":");
        
        List<CourseInfo> latestCourses = CourseOps.getCoursesByCategoryName(catName);
        latestCourses.forEach(System.out::println);
        HandleChoice handler = new HandleChoice();
        handler.courseChoice();
    }

    
}

