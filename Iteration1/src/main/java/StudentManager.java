import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StudentManager {
	private ArrayList<Student> studentList = new ArrayList<Student>();
	private String[] letterGrades = {"FF", "FD", "DD", "DC", "CC","CC", "CC", "CC","CB", "CB","CB", "CB","BB", "BA", "AA"}; // NORMAL DISTRIBUTION YAPILACAK. DIYELIM %2 KALIYOR. 2 TANE FF KOYARIZ.
	CourseManager courseManager = new CourseManager();
	Random rand = new Random();
	
	public void createRandomStudent(String id, String name ,String semester) {
		// BU ASAGIDAKILERIN HEPSI FOR DONGUSU ICINDE YAPILICAK. AYNI ISLEMLER. SADECE DOSYA OKUMA VAR TEK KISI YERINE COK KISI 
		// GELECEK JSONOBJECT KISMINDAN. SEMESTER BILGISI SADECE FALL YA DA SPRING. ONEMLI DEGIL O.
		Student student = new Student(id, name);
		
		int entry_year = Integer.parseInt(student.getStudentID().substring(4, 6));
		student.setGrade(Math.min(21 - entry_year, 4));
		
		// create transcript.
		randomTranscript(student, semester);
		
		// select courses for registration.
		requestCourses(student, semester);
		studentList.add(student);		
	}
	
	public void randomTranscript(Student student, String semester) {
		int studentSemester = student.getGrade() * 2;
		if(semester == "FALL") studentSemester -= 1;
		if(studentSemester <= 1) return; // no past course.
		ArrayList<Course> failedCourses = new ArrayList<Course>();
		int randLetter;
		ArrayList<Course> oldCourses =	courseManager.getPastSemesterCourses(studentSemester);
		boolean is_failed = false;
		for(Course course: oldCourses) {
			// control if student failed prerequisite course for this course. give "--" not given.
			for(Course course2: failedCourses) {
				if(course.getPrerequisiteCourse() != null && course.getPrerequisiteCourse().equals(course2.getCourseCode())) {
					is_failed = true;
					break;
				}
			}
			student.setTakenCourses(course);
			
			// student did not take this lesson because of prerequisite.
			if(is_failed) {
				student.setLetterGrades("--");
				is_failed = false;
				continue;
			}
			randLetter = rand.nextInt(letterGrades.length);
			student.setLetterGrades(letterGrades[randLetter]);
			if(letterGrades[randLetter].equals("FF")) {
				failedCourses.add(course);
			
			}			
		}
	}
	
	
	public void requestCourses(Student student, String semester) {
		int studentSemester = student.getGrade() * 2;
		if(semester == "FALL") studentSemester -= 1;
		
		System.out.println(student.getStudentName());
		System.out.println(student.getGrade());
		System.out.println(student.getFailedCourses().toString());
		System.out.println(student.getStudentID());
		// create courses for student's registration.
		ArrayList<Course> studentCourses = new ArrayList<Course>();
		if(student.getFailedCourses() != null) {
			for(Course course: student.getFailedCourses()) {
			studentCourses.add(course);
			}
		}
		
		
		for(Course course: courseManager.getCurrentSemesterCourses(studentSemester)) {
			studentCourses.add(course);
		}
		
		// select random number between 4-10. maximum 10 courses can be selected.
		int no_courses = rand.nextInt(7) + 4;
		int rand_course;
		for(int i = 0; i<no_courses; i++) {
			rand_course = rand.nextInt(studentCourses.size()); // select courses randomly.			
			student.setRequestedCourses(studentCourses.get(rand_course));
			
		}
				
	}
	/*public void calculateGPA(Student student, String semester) {
        String[] letters =  {"FF", "FD", "DD", "DC", "CC","CC", "CC", "CC","CB", "CB","CB", "CB","BB", "BA", "AA"};
        double[] grades = {4.33, 4.00, 3.67, 3.33, 3.00, 2.67, 2.33, 2.00, 1.67, 1.00, 0.00};

        int tempGPA = 0;
        int totalGrade = 0;
        int totalCreditnum = 0;
        int n=0;
        for (Course course : student.getTakenCourses()) {
            String templetter = student.getLetterGrades()[n];
            n++;
            int tempgradesnum = 0;
            int l=0;
            for (String letter : letters) {
                if (letter == templetter) {
                    tempgradesnum = grades[l];
                }
                l++;
            }
            int creditOfCourse = course.getCredit();
            totalGrade + (creditOfCourse * tempgradesnum) = totalGrade; 
            totalCreditnum + tempgradesnum = totalCreditnum;
        }
        tempGPA = totalGrade / totalCreditnum;
        student.setGPA(tempGPA);
    }*/
	public ArrayList<Student> getStudentList(){
		return studentList;
	}
	

}
