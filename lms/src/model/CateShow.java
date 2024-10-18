package model;

import java.util.List;

import control.CourseOps;

public class CateShow {
	
	
	public static void show(String catName) {
		System.out.println("| "+catName+":");
		if(catName.equals("ALL")) {
			List<CourseInfo> latestCourses = CourseOps.getCourses();
			for (CourseInfo CourseInfo : latestCourses) {
	            System.out.println(CourseInfo);
	            
	        }
		}else {
			List<CourseInfo> latestCourses = CourseOps.getCoursesByCategoryName(catName);
			for (CourseInfo CourseInfo : latestCourses) {
	            System.out.println(CourseInfo);
	        }
		}
		
	}

	
}
