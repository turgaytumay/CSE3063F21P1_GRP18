package iteration2.project;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseSection {
	private int quota;
	private HashMap<String, ArrayList<String>> schedule;
	
	public CourseSection(int quota) {
		this.quota = quota;
		this.schedule = new HashMap<String, ArrayList<String>>();
	}
	
	public void addToSchedule(String day, String hours) {
		if(schedule.get(day) == null) {
			schedule.put(day, new ArrayList<String>());
		}
		schedule.get(day).add(hours);	
	}

	
	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}

	public HashMap<String, ArrayList<String>> getSchedule() {
		return schedule;
	}
	
}

