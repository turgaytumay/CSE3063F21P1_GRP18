package iteration2.project;

import java.util.HashMap;

public class TeCourse extends Course{

	public TeCourse(int courseSemester, String courseCode, String courseName, int credit, int akts, int quota,  String courseType) {
		super(courseSemester, courseCode, courseName, credit, akts, quota, courseType);
	}
	
	public boolean checkRequirement(Student student, String semester) {
		int noOfTECourses = 0;
		for(Course course: student.getRequestedCourses()) {
			if(course.getCourseCode().equals(this.getCourseCode())) continue;
			if(course instanceof TeCourse) {
				noOfTECourses++;
			}
			
		}
		
		// student can not take TE courses more than 2.
		if(noOfTECourses > 2 && semester.equals("FALL")) return false;
		
		if(noOfTECourses > 3 && semester.equals("SPRING")) return false;
		
		return true;
		
	}

}
