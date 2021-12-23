package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StudentManager {
	private static ArrayList<Student> studentList = new ArrayList<Student>();
	Random rand = new Random();
	private String[] letterGrades = { "FF", "FD", "DD", "DC", "CC", "CC", "CC", "CC", "CB", "CB", "CB", "CB", "BB",
			"BA", "AA" }; // normal distribution.

	public void createRandomStudent(String semester) {
		HashMap<String, JSONArray> students = FileManager.readStudents(
				"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\jsonFiles\\student_ids.json",
				"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\jsonFiles\\student_names.json");
		for (int i = 0; i < students.get("Names").size(); i++) {
			HashMap<String, String> nameMap = (HashMap<String, String>) students.get("Names").get(i);
			HashMap<String, String> idMap = (HashMap<String, String>) students.get("IDs").get(i);

			// map name and id and create object.
			Student student = new Student(idMap.get("random_id"), nameMap.get("name"));

			// find grade of student.
			int entry_year = Integer.parseInt(student.getStudentID().substring(4, 6));
			student.setGrade(Math.min(21 - entry_year, 4));

			// create random transcript for taken courses till now.
			randomTranscript(student, semester);

			studentList.add(student);

		}

		// write transcripts to their own json file for each student.
		FileManager.writeTranscript(studentList);

	}

	// create transcript randomly for each student till this semester. BEFORE
	// REGISTRATION
	public void randomTranscript(Student student, String semester) {

		// get student semester.
		int studentSemester = student.getGrade() * 2;
		if (semester == "FALL")
			studentSemester -= 1;
		if (studentSemester <= 1)
			return; // no past course.

		// give course and random letter grade to student.
		ArrayList<Course> failedCourses = new ArrayList<Course>();
		ArrayList<Course> oldCourses = CourseManager.getPastSemesterCourses(studentSemester);
		int randLetter;
		boolean is_failed = false;
		for (Course course : oldCourses) {
			// control if student failed prerequisite course for this course. give "--".
			// Means: could not take this course.
			if (course.getPrerequisiteCourse() != null) {
				for (Course course2 : failedCourses) {
					if (course.getPrerequisiteCourse().equals(course2.getCourseCode())) {
						is_failed = true;
						break;
					}
				}
			}

			if (is_failed) { // student could not take this lesson because could not passed its prerequisite
								// course.
				student.setTakenCourses(course, "--");
				is_failed = false;
				continue;
			}

			// give random letter.
			randLetter = rand.nextInt(letterGrades.length);
			String randomLetter = letterGrades[randLetter];
			student.setTakenCourses(course, randomLetter);

			// check failed.
			if (randomLetter.equals("FF")) {
				failedCourses.add(course);
			}
		}
	}

	// select course to register randomly for each student for this semester.
	public void requestCourses(String semester) {
		for (Student student : studentList) {
			ArrayList<Course> possibleCourses = new ArrayList<Course>(); // possible courses to take.
			int studentSemester = student.getGrade() * 2;
			if (semester == "FALL")
				studentSemester -= 1;

			// first add courses from failed courses to possibleCourses to be taken.
			if (student.getFailedCourses() != null) {
				for (Course course : student.getFailedCourses()) {
					possibleCourses.add(course);
				}
			}

			// second add this semester courses to possibleCourses to be taken.
			for (Course course : CourseManager.getCurrentSemesterCourses(studentSemester)) {
				possibleCourses.add(course);
			}

			// now select courses from possible courses to be taken randomly.
			int TakenCourseNumber = Math.min(possibleCourses.size(), 10); // can take max 10 courses.
			int rand_course;
			for (int i = 0; i < TakenCourseNumber; i++) {
				rand_course = rand.nextInt(possibleCourses.size()); // select courses randomly.
				student.setRequestedCourses(possibleCourses.get(rand_course));
				possibleCourses.remove(rand_course); // delete selected course.

			}

			// make system controls for requested courses of student.
			RegisterationManager.checkQuotaProblems(student);
			RegisterationManager.checkPrerequisiteProblems(student);

			// send advisor.
			Advisor.checkCourseRequirements(student);
			Advisor.checkCollision(student);

			// approval courses which can be taken by student.
			Advisor.approvalCourses(student);

			// write new transcript to student's file.
			writeNewTranscript(student);
		}
	}

	// write new transcript after registration to student file.
	public void writeNewTranscript(Student student) {
		JSONObject studentObject = new JSONObject();
		JSONArray studentCourses = new JSONArray();
		studentObject.put("AFTER REGISTRATION", "NEW TRANSCRIPT");
		for (Course course : student.getRequestedCourses()) {
			studentCourses.add("Course Name: " + course.getCourseName() + ", Course Letter: " + "--");
		}

		studentObject.put("Courses:", studentCourses);

		FileManager.writeToFile(
				"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\transcripts\\"
						+ student.getStudentID() + ".json",
				studentObject);

	}

}
