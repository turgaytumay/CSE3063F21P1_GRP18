package iteration2.project;

import java.util.HashMap;

public class NteCourse extends Course{

	public NteCourse(int courseSemester, String courseCode, String courseName, int credit, int akts, int quota,  String courseType) {
		super(courseSemester, courseCode, courseName, credit, akts, quota, courseType);
	}
	
	public boolean checkRequirement(Student student, String semester) {
		int noOfNTECourses = 0;
		for(Course course: student.getRequestedCourses()) {
			if(course.getCourseCode().equals(this.getCourseCode())) continue;
			if(course instanceof NteCourse) {
				noOfNTECourses++;
			}
			
		}
		if(semester.equals("FALL")) return false; // this course type can not be taken for FALL semester.
		
		// student can not take NTE courses more than 2.
		if(noOfNTECourses > 2) return false;
		
		return true;
		
	}
	

}
