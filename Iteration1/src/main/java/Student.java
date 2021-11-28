import java.util.ArrayList;

public class Student {
	private String studentID;
	private String studentName;
	private int grade;
	private float gpa;
	private ArrayList<Course> takenCourses;
	private ArrayList<String> letterGrades;
	private ArrayList<Course> requestedCourses;
	
	
	
	public Student(String studentID, String studentName) {
		this.studentID = studentID;
		this.studentName = studentName;
	}


	public String getStudentID() {
		return studentID;
	}


	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}


	// getter - setter
	public int getGrade() {
		return grade;
	}


	public void setGrade(int grade) {
		this.grade = grade;
	}


	public float getGPA() {
		return gpa;
	}


	public void setGPA(float GPA) {
		GPA = gpa;
	}


	public ArrayList<Course> getTakenCourses() {
		return takenCourses;
	}


	public void setTakenCourses(Course takenCourse) {
		if(this.takenCourses == null) this.takenCourses = new ArrayList<Course>();
		this.takenCourses.add(takenCourse);
	}
	

	public ArrayList<String> getLetterGrades() {
		return letterGrades;
	}


	public void setLetterGrades(String letterGrade) {
		if(this.letterGrades == null) this.letterGrades = new ArrayList<String>();
		this.letterGrades.add(letterGrade);
	}

	
	public ArrayList<Course> getRequestedCourses() {
		return requestedCourses;
	}


	public void setRequestedCourses(Course requestedCourse) {
		if(this.requestedCourses == null) this.requestedCourses = new ArrayList<Course>();
		this.requestedCourses.add(requestedCourse);
	}
	
	public ArrayList<Course> getFailedCourses() {
		ArrayList<Course> temp = new ArrayList<Course>();
		if(letterGrades != null) {
			for(int i=0; i<letterGrades.size(); i++) {
			if(letterGrades.get(i).equals("FF") || letterGrades.get(i).equals("--")) {
				temp.add(takenCourses.get(i));
			}
		}
		}
		
		return temp;
		
	}
	
	
	
	
	
	

}