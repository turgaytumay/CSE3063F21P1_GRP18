import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CourseManager {
	
	private static ArrayList<Course> courseList = new ArrayList<Course>();
	Random rand = new Random();
	
	
	public void createCourses(JSONArray courses, int semester) {
		String courseCode, courseName, courseType, prerequisiteCourse;
		int credit, akts, quota, practiceHour, theoricHour;
		
		// create day and hours to schedule courses.
		ArrayList<ArrayList<Integer>> availableTimes = new ArrayList<ArrayList<Integer>>(); // monday to friday, 8 to 20
		for(int i=0; i<5; i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int j=0; j<13; j++) {
				temp.add(j+8);
			}
			availableTimes.add(temp);		
		} 
			
		
		// create courses.
		for (Object semesterCourses : courses) {
			courseCode = (String) ((JSONObject) semesterCourses).get("Course Code");
			courseName = (String) ((JSONObject) semesterCourses).get("Course Name");
			courseType = (String) ((JSONObject) semesterCourses).get("Type");
			credit =(int) Float.parseFloat(((String) ((JSONObject) semesterCourses).get("Credit")).replace(",", "."));
			akts = (int) Float.parseFloat(((String) ((JSONObject) semesterCourses).get("ECTS")).replace(",", "."));
			quota = rand.nextInt(31) + 70;
			theoricHour = (int) Float.parseFloat(((String) ((JSONObject) semesterCourses).get("T")).replace(",", "."));
			practiceHour = (int) Float.parseFloat(((String) ((JSONObject) semesterCourses).get("U")).replace(",", "."));
			Course course = new Course(semester, courseCode, courseName, credit, akts, quota, courseType);
			// add prerequisite.
			try {
				course.setPrerequisiteCourse((String) ((JSONObject) semesterCourses).get("precondition"));
			} catch (Exception e) {
			}	
			
			// scheduleCourse
			scheduleCourse(course, practiceHour, theoricHour, availableTimes);
			courseList.add(course);
		}	
	}
	
	public void scheduleCourse(Course course, int practiceHour, int theoricHour, ArrayList<ArrayList<Integer>> availableTimes) {
		Random rand = new Random();
		int remainTheoric = theoricHour;
		CourseSection courseSection = new CourseSection(course.getQuota());
		int randDay, hour;
		
		// decide theoric section number and times. then practice hours.
		while(remainTheoric > 0) {
			while(true) { // till find available time
				randDay = rand.nextInt(availableTimes.size());
				if(availableTimes.get(randDay).size() < remainTheoric) continue; // not enough time for section.
				
				hour = availableTimes.get(randDay).get(0); // to break consecutive hours.
				
				// divide course max 2 hours lessons.
				if(availableTimes.get(randDay).size() >= 2) { 
					// make hours unavailable.
					for(int j=0; j<2; j++)
						availableTimes.get(randDay).remove(0);
					remainTheoric -=2;
					// add course section to schedule.
					courseSection.setDays(randDay);
					courseSection.setHours(String.valueOf(hour) + "-" + String.valueOf(hour+2));
				
				}
				else {
					availableTimes.get(randDay).remove(0);
					remainTheoric -=1;
					courseSection.setDays(randDay);
					courseSection.setHours(String.valueOf(hour) + "-" + String.valueOf(hour+1));
				}
				
				if(availableTimes.get(randDay).isEmpty()) availableTimes.remove(randDay); // remove full day.
				break;
			}
			
		}
		
		// schedule lab sections.
		if(practiceHour > 0) {
			while(true) { // till find available time.
				randDay = rand.nextInt(availableTimes.size());
				if(availableTimes.get(randDay).size() < 2) continue; // not enough time for section.
				hour = availableTimes.get(randDay).get(0);
				for(int j=0; j<2; j++)
					availableTimes.get(randDay).remove(0);
				
				courseSection.setDays(randDay);
				courseSection.setHours(String.valueOf(hour) + "-" + String.valueOf(hour+1));
				break;	
			}
			
			if(course.getQuota() > 80) { // 2 different lab sections.
				CourseSection courseSection2 = new CourseSection(course.getQuota() - course.getQuota()/2);
				courseSection.setQuota(course.getQuota()/2);
				while(true) { // till find available time.
					randDay = rand.nextInt(availableTimes.size());
					if(availableTimes.get(randDay).size() < 2) continue; // not enough time for section.
					hour = availableTimes.get(randDay).get(0);
					for(int j=0; j<2; j++)
						availableTimes.get(randDay).remove(0);
					
					courseSection2.setDays(randDay);
					courseSection2.setHours(String.valueOf(hour) + "-" + String.valueOf(hour+1));
					course.setCourseSections(courseSection2);
					break;	
				}	
				
			}
			
		}
		course.setCourseSections(courseSection);
	}
	
	public ArrayList<Course> getPastSemesterCourses(int semester){
		ArrayList<Course> temp = new ArrayList<Course>();
		for(Course course: courseList) {
			if(course.getCourseSemester() < semester) {
				temp.add(course);
			}
		}
		return temp;
		
	}
	
	public ArrayList<Course> getCurrentSemesterCourses(int semester){
		ArrayList<Course> temp = new ArrayList<Course>();
		for(Course course: courseList) {
			if(course.getCourseSemester() == semester) {
				temp.add(course);
			}
		}
		return temp;
		
	}

	public ArrayList<Course> getCourse() {
		return courseList;
	}


}

