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
		// courseManager.printSchedule();

		// create random students with random transcript.
		studentManager.createRandomStudent(semester);

		// simulate registration process for each student.
		studentManager.requestCourses(semester); // request courses and system checks.

		// write problem analysis file and student's new transcript to their own files.
		FileManager.writeProblemToFile("Quota", RegisterationManager.getQuotaProblems().keySet());
		FileManager.writeProblemToFile("Prerequisite", RegisterationManager.getPrerequisiteProblems().keySet());
		FileManager.writeProblemToFile("Collision", Advisor.getCollisionProblems().keySet());
		FileManager.writeProblemToFile("Requirement", Advisor.getRequirementProblems().keySet());
		
		System.out.println("FINISHED");
		courseManager.printSchedule();
	}
}
