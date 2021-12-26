package iteration2.project;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Student {
	private String studentID;
	private String studentName;
	private int grade;
	private float gpa;
	private LinkedHashMap<Course, String> takenCourses;
	private ArrayList<Course> requestedCourses;

	public Student(String studentID, String studentName) {
		this.studentID = studentID;
		this.studentName = studentName;
	}

	public int getCompletedCredit() {
		int completedCredit = 0;
		for (Course course : takenCourses.keySet()) {
			if (takenCourses.get(course).equals("FF") || takenCourses.get(course).equals("FD"))
				continue;
			completedCredit += course.getCredit();
		}
		return completedCredit;
	}

	// getter - setter

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

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public float getGPA() {
		double successCoefficient = 0;
		int cumulativeCredit = 0;
		float cumulativeSuccessCoefficient = 0;
		if(takenCourses.isEmpty()) {
			return 0.0F;
		}
		for (Course course : takenCourses.keySet()) {
			if (takenCourses.get(course).equals("FF")) {
				successCoefficient = 0.0;
			} else if (takenCourses.get(course).equals("FD")) {
				successCoefficient = 0.5;
			} else if (takenCourses.get(course).equals("DD")) {
				successCoefficient = 1.0;
			} else if (takenCourses.get(course).equals("DC")) {
				successCoefficient = 1.5;
			} else if (takenCourses.get(course).equals("CC")) {
				successCoefficient = 2.0;
			} else if (takenCourses.get(course).equals("CB")) {
				successCoefficient = 2.5;
			} else if (takenCourses.get(course).equals("BB")) {
				successCoefficient = 3.0;
			} else if (takenCourses.get(course).equals("BA")) {
				successCoefficient = 3.5;
			} else if (takenCourses.get(course).equals("AA")) {
				successCoefficient = 4.0;

			}
			cumulativeCredit += course.getCredit();
			cumulativeSuccessCoefficient += course.getCredit() * successCoefficient;

		}

		gpa = (cumulativeSuccessCoefficient / cumulativeCredit);
		return gpa;
	}

	public void setGPA(float GPA) {
		GPA = gpa;
	}

	public LinkedHashMap<Course, String> getTakenCourses() {
		return takenCourses;
	}

	public void setTakenCourses(Course takenCourse, String letterGrade) {
		if (this.takenCourses == null)
			this.takenCourses = new LinkedHashMap<Course, String>();
		this.takenCourses.put(takenCourse, letterGrade);
	}

	public ArrayList<Course> getRequestedCourses() {
		return requestedCourses;
	}

	public void setRequestedCourses(Course requestedCourse) {
		if (this.requestedCourses == null)
			this.requestedCourses = new ArrayList<Course>();
		this.requestedCourses.add(requestedCourse);
	}

	public ArrayList<Course> getFailedCourses() {
		ArrayList<Course> temp = new ArrayList<Course>();
		if (this.takenCourses != null) {
			for (Course course : takenCourses.keySet()) {
				if (takenCourses.get(course).equals("FF") || takenCourses.get(course).equals("--")
						|| takenCourses.get(course).equals("FD")) {
					temp.add(course);
				}

			}
		}

		return temp;

	}

	public void setTakenCoursesForNew() {
		this.takenCourses = new LinkedHashMap<Course, String>();
	}
}
