package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class CourseManager {
	private static HashMap<String, ArrayList<Course>> curriculumCourses = new HashMap<String, ArrayList<Course>>();
	private static HashMap<String, ArrayList<Course>> electiveCourses = new HashMap<String, ArrayList<Course>>();
	private static ScheduleManager scheduleManager = new ScheduleManager(); // to schedule courses.

	// create courses with schedule.
	public void createCourses() {
		// course attributes.
		String courseCode, courseName, prerequisiteCourse, courseType;
		int courseSemester, credit, akts, quota, practiceHour, theoricHour;
		Random rand = new Random(); // quota bilgisi eklenince silinecek.
		String[] courseCategory = { "semester1", "semester2", "semester3", "semester4", "semester5", "semester6",
				"semester7", "semester8", "FTE", "TE", "NTE" };

		/*** create courses and schedule. ***/

		// get semester courses.
		for (int i = 0; i < courseCategory.length; i++) {
			JSONArray courses = FileManager.readCourses(
					"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\jsonFiles\\courses.json",
					courseCategory[i]);
			// create and schedule course.
			// create
			for (Object c : courses) {
				courseCode = (String) ((JSONObject) c).get("Course Code");
				courseName = (String) ((JSONObject) c).get("Course Name");
				courseType = (String) ((JSONObject) c).get("Type");
				credit = (int) Float.parseFloat(((String) ((JSONObject) c).get("Credit")).replace(",", "."));
				akts = (int) Float.parseFloat(((String) ((JSONObject) c).get("ECTS")).replace(",", "."));
				quota = rand.nextInt(31) + 70;
				theoricHour = (int) Float.parseFloat(((String) ((JSONObject) c).get("T")).replace(",", "."));
				practiceHour = (int) Float.parseFloat(((String) ((JSONObject) c).get("U")).replace(",", "."));

				Course course;
				// action wrt. course type.
				switch (courseCategory[i]) {
				case "FTE":
					course = new FteCourse(i, courseCode, courseName, credit, akts, quota, courseType);
					break;
				case "NTE":
					course = new NteCourse(i, courseCode, courseName, credit, akts, quota, courseType);
					break;
				case "TE":
					course = new TeCourse(i, courseCode, courseName, credit, akts, quota, courseType);
					break;

				default: // 'must' course.
					course = new Course(i, courseCode, courseName, credit, akts, quota, courseType);
					break;
				}

				// add prerequisite if exist.
				try {
					course.setPrerequisiteCourse((String) ((JSONObject) c).get("precondition"));
				} catch (Exception e) {
				}

				// schedule course.
				scheduleManager.scheduleCourse(course, practiceHour, theoricHour);

				// add course to hashMap.
				switch (courseCategory[i]) {
				case "FTE":
					if (electiveCourses.get(courseCategory[i]) == null) {
						electiveCourses.put(courseCategory[i], new ArrayList<Course>());
					}
					electiveCourses.get(courseCategory[i]).add(course);
					break;
				case "NTE":
					if (electiveCourses.get(courseCategory[i]) == null) {
						electiveCourses.put(courseCategory[i], new ArrayList<Course>());
					}
					electiveCourses.get(courseCategory[i]).add(course);
					break;
				case "TE":
					if (electiveCourses.get(courseCategory[i]) == null) {
						electiveCourses.put(courseCategory[i], new ArrayList<Course>());
					}
					electiveCourses.get(courseCategory[i]).add(course);
					break;

				default: // 'must' course.
					if (curriculumCourses.get(courseCategory[i]) == null) {
						curriculumCourses.put(courseCategory[i], new ArrayList<Course>());
					}
					curriculumCourses.get(courseCategory[i]).add(course);
					break;
				}
			}
			scheduleManager.resetAvailableTimes();

		}

	}

	// get previous years' courses to create transcript
	public static ArrayList<Course> getPastSemesterCourses(int semester) {
		ArrayList<Course> result = new ArrayList<Course>();
		for (int i = 1; i < semester; i++) {
			ArrayList<Course> semesterCourses = new ArrayList<Course>(curriculumCourses.get("semester" + String.valueOf(i)));
			for (Course course : semesterCourses) {
				result.add(course);
			}
		}
		return result;
	}
	
	// get courses of requested semester for registration process.
	public static ArrayList<Course> getCurrentSemesterCourses(int semester) {
		return curriculumCourses.get("semester" + String.valueOf(semester));

	}
	// it prints courses with their days and hours.
	public void printSchedule() {
		for (int i = 1; i < 9; i++) {
			System.out.println("*************" + "SEMESTER " + i + "****************");
			for (Course course : curriculumCourses.get("semester" + i)) {
				int sectionNo = 0;
				System.out.println("********************");
				System.out.println("Course Name: " + course.getCourseName());
				System.out.println("Course Quota: " + course.getQuota());
				for (CourseSection section : course.getCourseSections()) {
					sectionNo++;
					System.out.println("Section " + sectionNo + ":" + section.getSchedule());

				}

			}
		}

	}

}
