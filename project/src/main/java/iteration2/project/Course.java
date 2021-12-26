package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;


public class Course {
	private int courseSemester;
	private String courseCode;
	private String courseName;
	private String prerequisiteCourse;
	private ArrayList<CourseSection> courseSections;
	private int credit;
	private int akts;
	private int quota;
	private String courseType;

	public Course(int courseSemester, String courseCode, String courseName, int credit, int akts, int quota, String courseType) {
		this.courseSemester = courseSemester;
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.credit = credit;
		this.akts = akts;
		this.quota = quota;
		this.courseType = courseType;
	}
	
	// it will overloading by subclass.
	public boolean checkRequirement(Student student, String semester) {
		if(this.courseCode.equals("CSE4297") && student.getCompletedCredit() < 155) {
			return false;
		}
		return true;
		
	}
	
	// getter - setter

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCourseSemester() {
		return courseSemester;
	}

	public void setCourseSemesters(int courseSemester) {
		this.courseSemester = courseSemester;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getPrerequisiteCourse() {
		return prerequisiteCourse;
	}

	public void setPrerequisiteCourse(String prerequisiteCourse) {
		this.prerequisiteCourse = prerequisiteCourse;
	}

	public ArrayList<CourseSection> getCourseSections() {
		return courseSections;
	}

	public void setCourseSections(CourseSection courseSection) {
		if (this.courseSections == null)
			this.courseSections = new ArrayList<CourseSection>();
		this.courseSections.add(courseSection);
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getAkts() {
		return akts;
	}

	public void setAkts(int akts) {
		this.akts = akts;
	}

	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}
	
	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
}
