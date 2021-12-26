package iteration2.project;

import java.util.HashMap;

public class FteCourse extends Course{

	public FteCourse(int courseSemester, String courseCode, String courseName, int credit, int akts, int quota, String courseType) {
		super(courseSemester, courseCode, courseName, credit, akts, quota, courseType);
	}
	
	public boolean checkRequirement(Student student, String semester) {
		int noOfFTECourses = 0;
		for(Course course: student.getRequestedCourses()) {
			if(course.getCourseCode().equals(this.getCourseCode())) continue;
			if(course instanceof FteCourse) {
				noOfFTECourses++;
			}
			
		}
		
		// student can not take FTE courses more than 1.
		if(noOfFTECourses >= 1) return false;
		
		return true;
		
	}
	

}
