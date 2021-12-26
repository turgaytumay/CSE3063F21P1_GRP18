package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterationManager {
	private static HashMap<Student, ArrayList<Course>> QuotaProblems = new HashMap<Student, ArrayList<Course>>();
	private static HashMap<Student, ArrayList<Course>> PrerequisiteProblems = new HashMap<Student, ArrayList<Course>>();

	public static void checkQuotaProblems(Student student) {
		for (Course course : student.getRequestedCourses()) {

			if (course.getQuota() == 0) {
				if (QuotaProblems.get(student) == null) {
					QuotaProblems.put(student, new ArrayList<Course>());
				}
				QuotaProblems.get(student).add(course);
			}
		}

		// remove courses with quota problem from requested courses.
		if (QuotaProblems.get(student) != null) {
			for (Course course : QuotaProblems.get(student)) {
				student.getRequestedCourses().remove(course);
			}

		}

	}

	public static void checkPrerequisiteProblems(Student student) {
		if (student.getFailedCourses() == null)
			return; // no need check

		boolean is_failed = false;
		for (Course requestedCourse : student.getRequestedCourses()) {
			if (requestedCourse.getPrerequisiteCourse() == null)
				continue; // no need to check.
			for (Course failedCourse : student.getFailedCourses()) {
				if (requestedCourse.getPrerequisiteCourse().equals(failedCourse.getCourseCode())) {
					is_failed = true;
					break;
				}
			}

			if (is_failed) {
				if (PrerequisiteProblems.get(student) == null) {
					PrerequisiteProblems.put(student, new ArrayList<Course>());
				}
				PrerequisiteProblems.get(student).add(requestedCourse);
				is_failed = false; // reset.

			}

		}

		// remove courses with prerequisite problem from requested courses.
		if (PrerequisiteProblems.get(student) != null) {
			for (Course course : PrerequisiteProblems.get(student)) {
				student.getRequestedCourses().remove(course);
			}
		}


	}

	// get QuotaProblems to write files.
	public static HashMap<Student, ArrayList<Course>> getQuotaProblems() {
		return QuotaProblems;
	}

	// get PrerequisiteProblems to write files.
	public static HashMap<Student, ArrayList<Course>> getPrerequisiteProblems() {
		return PrerequisiteProblems;
	}

}
