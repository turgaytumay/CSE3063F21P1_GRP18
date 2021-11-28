import java.util.ArrayList;

public class RegisterManager {
	private ArrayList<Student> studentList = new ArrayList<Student>();
	private ArrayList<Student> studentQuotaProblems = new ArrayList<Student>();
	private ArrayList<Student> studentPrerequisiteProblems = new ArrayList<Student>();
	private FileManager fileManager;
	boolean is_failed = false;
	
	
	public RegisterManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}
	
	public void checkRegistration() {
		for(Student student: studentList) {
			checkPrerequisite(student);
			checkQuota(student);
		}
		
	}
	
	public void checkPrerequisite(Student student) {
		for(Course course: student.getRequestedCourses()) {
			for(Course course2: student.getFailedCourses()) {
				if(course.getPrerequisiteCourse() != null && course.getPrerequisiteCourse().equals(course2.getCourseCode())) {
					is_failed = true;
					break;
				}
			}			
			// student did not take this lesson because of prerequisite.
			if(is_failed) {
				if(studentPrerequisiteProblems == null) studentPrerequisiteProblems = new ArrayList<Student>();
				studentPrerequisiteProblems.add(student);
				//ArrayList<Course> temp = student.getRequestedCourses();
                //temp.remove(course);
				//student.getRequestedCourses().remove(course);
				is_failed = false;
				String message = String.format("Student did not taken %s because of prerequisite problem with %s", course.getCourseCode(), course.getPrerequisiteCourse());
				fileManager.writeData(String.format(".\\\\jsonfiles\\\\%s.json", student.getStudentID()), message);
			}
			
		}
	}
	
	public void checkQuota(Student student) {
		for(Course course: student.getRequestedCourses()) {
			if(course.getQuota() < 1) {
				studentQuotaProblems.add(student);
				student.getRequestedCourses().remove(course);
				String message = String.format("Student did not taken %s because of quota problem", course.getCourseCode());
				fileManager.writeData(String.format(".\\\\jsonfiles\\\\%s.json", student.getStudentID()), message);
				continue;
			}
			else {
				student.setTakenCourses(course);
				student.setLetterGrades("NEW");	
			}
		}
		
	}
	
	
	
	public ArrayList<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(ArrayList<Student> studentList) {
		this.studentList = studentList;
	}

	public ArrayList<Student> getStudentQuotaProblems() {
		return studentQuotaProblems;
	}

	public void setStudentQuotaProblems(ArrayList<Student> studentQuotaProblems) {
		this.studentQuotaProblems = studentQuotaProblems;
	}

	public ArrayList<Student> getStudentPrerequisiteProblems() {
		return studentPrerequisiteProblems;
	}

	public void setStudentPrerequisiteProblems(ArrayList<Student> studentPrerequisiteProblems) {
		this.studentPrerequisiteProblems = studentPrerequisiteProblems;
	}

	public void createAnalyzeFile() {
        fileManager.writeData(String.format(".\\jsonfiles\\%s.json", "Hata_rapor"), "Total Error of Prerequisite: " + studentPrerequisiteProblems.size());
        for(Student student: studentPrerequisiteProblems) {
            fileManager.writeData(String.format(".\\jsonfiles\\%s.json", "Hata_rapor"), student.getStudentID());
        }

        fileManager.writeData(String.format(".\\jsonfiles\\%s.json", "Hata_rapor"), "Total Error of Quota: " + studentQuotaProblems.size());
        for(Student student: studentQuotaProblems) {
            fileManager.writeData(String.format(".\\jsonfiles\\%s.json", "Hata_rapor"), student.getStudentID());
        }

    }

	

}