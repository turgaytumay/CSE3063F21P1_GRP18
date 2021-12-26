package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;

public class Advisor {
	private static HashMap<Student, ArrayList<Course>> RequirementProblems = new HashMap<Student, ArrayList<Course>>();
	private static HashMap<Student, ArrayList<Course>> CollisionProblems = new HashMap<Student, ArrayList<Course>>();

	// STUDENT REQUESTED COURSES ARRAYINDE GEZINEREK CAKISMA VAR MI YOK MU
	// BAKILACAK, ONA GORE EKLENIP SILINECEK.
	public static void checkCollision(Student student) {
		/*
		 * 
		 */
	}

	public static void checkCourseRequirements(Student student, String semester) {
		// TE - NTE - FTE and Engineering Project requirement controls.
		for (Course course : student.getRequestedCourses()) {
			boolean is_taken = course.checkRequirement(student, semester);
			if (!is_taken) {
				if (RequirementProblems.get(student) == null) {
					RequirementProblems.put(student, new ArrayList<Course>());
				}
				RequirementProblems.get(student).add(course);
			}
		}

		// remove courses with requirement problem from requested courses.
		if (RequirementProblems.get(student) != null) {
			for (Course course : RequirementProblems.get(student)) {
				student.getRequestedCourses().remove(course);
			}
		}

	}

	// Approval Courses.
	public static void approvalCourses(Student student) {
		for (Course course : student.getRequestedCourses()) {
			// add course to student's taken courses.
			student.setTakenCourses(course, "--");

			// decrease quota of course.
			course.setQuota(course.getQuota() - 1);

		}
	}

	/* GETTERS */
	// get RequirementProblems to write files.
	public static HashMap<Student, ArrayList<Course>> getRequirementProblems() {
		return RequirementProblems;
	}

	// get CollisionProblems to write files.
	public static HashMap<Student, ArrayList<Course>> getCollisionProblems() {
		return CollisionProblems;
	}
}