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
	Random rand = new Random();


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
				quota = rand.nextInt(20) + 40;
				theoricHour = (int) Float.parseFloat(((String) ((JSONObject) c).get("T")).replace(",", "."));
				practiceHour = (int) Float.parseFloat(((String) ((JSONObject) c).get("U")).replace(",", "."));

				Course course;
				// action wrt. course type.
				switch (courseCategory[i]) {
				case "FTE":
					course = new FteCourse(i + 1, courseCode, courseName, credit, akts, quota, courseType);
					break;
				case "NTE":
					course = new NteCourse(i + 1, courseCode, courseName, credit, akts, quota, courseType);
					break;
				case "TE":
					course = new TeCourse(i + 1, courseCode, courseName, credit, akts, quota, courseType);
					break;

				default: // 'must' course.
					course = new Course(i + 1, courseCode, courseName, credit, akts, 1000, courseType);
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
				if (i < 8) { // curriculum courses
					if (curriculumCourses.get(courseCategory[i]) == null) {
						curriculumCourses.put(courseCategory[i], new ArrayList<Course>());
					}
					curriculumCourses.get(courseCategory[i]).add(course);
				} else { // elective courses.
					if (electiveCourses.get(courseCategory[i]) == null) {
						electiveCourses.put(courseCategory[i], new ArrayList<Course>());
					}
					electiveCourses.get(courseCategory[i]).add(course);
				}
			}
			scheduleManager.resetAvailableTimes();

		}

	}

	// get previous years' courses to create transcript
	public static ArrayList<Course> getPastSemesterCourses(int semester) {
		ArrayList<Course> result = new ArrayList<Course>();
		for (int i = 1; i < semester; i++) {
			ArrayList<Course> semesterCourses = new ArrayList<Course>(
					curriculumCourses.get("semester" + String.valueOf(i)));
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

	
	public HashMap<String, ArrayList<Course>> getElectiveCourses(){
		return electiveCourses;
	}


	// select opened elective courses for this semester.
	public HashMap<String, ArrayList<Course>> getOpenedElectiveCourses(){
		HashMap<String, ArrayList<Course>> openedElectiveCourses = new HashMap<String, ArrayList<Course>>();
		int randIndex;
		for(int i=0; i<6; i++) {
			// TE
			randIndex = rand.nextInt(electiveCourses.get("TE").size());
			if (openedElectiveCourses.get("TE") == null) {
				openedElectiveCourses.put("TE", new ArrayList<Course>());
			}
			openedElectiveCourses.get("TE").add(electiveCourses.get("TE").get(randIndex));
			
			//FTE
			randIndex = rand.nextInt(electiveCourses.get("FTE").size());
			if (openedElectiveCourses.get("FTE") == null) {
				openedElectiveCourses.put("FTE", new ArrayList<Course>());
			}
			openedElectiveCourses.get("FTE").add(electiveCourses.get("FTE").get(randIndex));	
			
			//NTE
			randIndex = rand.nextInt(electiveCourses.get("NTE").size());
			if (openedElectiveCourses.get("NTE") == null) {
				openedElectiveCourses.put("NTE", new ArrayList<Course>());
			}
			openedElectiveCourses.get("NTE").add(electiveCourses.get("NTE").get(randIndex));	
			
		}
		
		return openedElectiveCourses;
	}

}
