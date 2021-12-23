package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ScheduleManager {
	private static ArrayList<ArrayList<Integer>> availableTimes = new ArrayList<ArrayList<Integer>>();

	// make default for new scheduling.
	public void resetAvailableTimes() {
		availableTimes.clear();
		for (int i = 0; i < 5; i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for (int j = 0; j < 13; j++) {
				temp.add(j + 8);
			}
			availableTimes.add(temp);
		}
	}

	public void scheduleCourse(Course course, int practiceHour, int theoricHour) {

		// reset available times if empty.
		if (availableTimes.size() == 0) {
			resetAvailableTimes();
		}
		Random rand = new Random();
		String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		int remainTheoric = theoricHour;
		CourseSection courseSection = new CourseSection(course.getQuota());
		int randDay, hour;
		
		// decide theoric section number and times. then practice hours.
		while (remainTheoric > 0) {
			while (true) { // till find available time
				if(availableTimes.size() == 0) resetAvailableTimes(); // to prevent 0 size while scheduling elective courses.
				randDay = rand.nextInt(availableTimes.size());
				if (availableTimes.get(randDay).size() < 2 && availableTimes.get(randDay).size() < remainTheoric) {
					if (!course.getCourseType().equals("Must")) {
						resetAvailableTimes(); // to prevent infinite loop for elective courses because of number of
												// courses.
					}
					continue; // not enough time for section.
				}

				hour = availableTimes.get(randDay).get(0); // to break consecutive hours.

				// divide course max 2 hours lessons.
				if (availableTimes.get(randDay).size() >= 2 && remainTheoric >= 2) {
					// make hours unavailable.
					for (int j = 0; j < 2; j++)
						availableTimes.get(randDay).remove(0);
					remainTheoric -= 2;
					// add course section to schedule.
					courseSection.addToSchedule(days[randDay] + "T", String.valueOf(hour) + "-" + String.valueOf(hour + 2));
				} else {
					availableTimes.get(randDay).remove(0);
					remainTheoric -= 1;
					courseSection.addToSchedule(days[randDay] + "T", String.valueOf(hour) + "-" + String.valueOf(hour + 1));

				}

				if (availableTimes.get(randDay).isEmpty()) {
					availableTimes.remove(randDay); // remove full day.
				}
				break;
			}

		}

		// schedule lab sections.
		if (practiceHour > 0) {
			HashMap<String, ArrayList<String>> theoricSchedule = new HashMap<String, ArrayList<String>>(courseSection.getSchedule()); // copy theoric hours to add course section 2. 
			
			while (true) { // till find available time.
				randDay = rand.nextInt(availableTimes.size());
				if (availableTimes.get(randDay).size() < practiceHour) {
					continue; // not enough time for section.
				}

				hour = availableTimes.get(randDay).get(0);
				for (int j = 0; j < practiceHour - 1; j++)
					availableTimes.get(randDay).remove(0);

				courseSection.addToSchedule(days[randDay],
						String.valueOf(hour) + "-" + String.valueOf(hour + practiceHour));
				break;
			}

			if (course.getQuota() > 80 && theoricHour > 0) { // 2 different lab sections.
				CourseSection courseSection2 = new CourseSection(course.getQuota() - course.getQuota() / 2);
				
				// add theoric hour to course section2.
				for(String key: theoricSchedule.keySet()) {
					for(String hr: theoricSchedule.get(key)) {
						courseSection2.addToSchedule(key, hr);
					}					
				}
		
				
				courseSection.setQuota(course.getQuota() / 2);
				while (true) { // till find available time.

					randDay = rand.nextInt(availableTimes.size());
					if (availableTimes.get(randDay).size() < practiceHour)
						continue; // not enough time for section.
					hour = availableTimes.get(randDay).get(0);
					for (int j = 0; j < practiceHour; j++)
						availableTimes.get(randDay).remove(0);

					courseSection2.addToSchedule(days[randDay],
							String.valueOf(hour) + "-" + String.valueOf(hour + practiceHour));
					course.setCourseSections(courseSection2);
					break;
				}

			}

		}
		course.setCourseSections(courseSection);

	}

}
