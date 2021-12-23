package iteration2.project;

import java.util.HashMap;

public class NteCourse extends Course{

	public NteCourse(int courseSemester, String courseCode, String courseName, int credit, int akts, int quota,  String courseType) {
		super(courseSemester, courseCode, courseName, credit, akts, quota, courseType);
	}
	
	//BURASI DOLDURULACAK.
	public boolean checkRequirement(Student student) {
		return true;
		
	}
	

}
