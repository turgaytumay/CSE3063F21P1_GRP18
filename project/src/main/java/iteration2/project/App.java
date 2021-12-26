package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;

public class App {
	public static void main(String[] args) {
		String semester = "SPRING";
		CourseManager courseManager = new CourseManager();
		StudentManager studentManager = new StudentManager();

		// Read datas: create courses and students.
		// create courses with scheduling.
		courseManager.createCourses();

		// create random students with random transcript.
		studentManager.createRandomStudent(semester, courseManager.getElectiveCourses());

		// simulate registration process for each student.
		studentManager.requestCourses(semester, courseManager.getOpenedElectiveCourses()); // request courses and system
																						// checks.

		// write problem to student's file and output file.
		FileManager.writeProblemToStudentFile("Quota", RegisterationManager.getQuotaProblems(), semester);
		FileManager.writeProblemToStudentFile("Prerequisite", RegisterationManager.getPrerequisiteProblems(), semester);
		FileManager.writeProblemToStudentFile("Collision", Advisor.getCollisionProblems(), semester);
		FileManager.writeProblemToStudentFile("Requirement", Advisor.getRequirementProblems(), semester);


		/*
		 * // write the course schedule. courseManager.printSchedule();
		 */

	}
}
