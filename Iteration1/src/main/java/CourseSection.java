import java.util.ArrayList;

public class CourseSection {
	private int quota;
	private ArrayList<Integer> days;
	private ArrayList<String> hours;
	
	public CourseSection(int quota) {
		this.quota = quota;
	}

	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}

	public ArrayList<Integer> getDays() {
		return days;
	}

	public void setDays(int day) {
		if(this.days == null) this.days = new ArrayList<Integer>();
		this.days.add(day);
	}

	public ArrayList<String> getHours() {
		return hours;
	}

	public void setHours(String hour) {
		if(this.hours == null) this.hours = new ArrayList<String>();
		this.hours.add(hour);
	}
	
	

}
