import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;



public class main {

	public static void main(String[] args) {
		String semester = "FALL";
		CourseManager courseManager = new CourseManager();
		StudentManager studentManager = new StudentManager();
		// create course from course.json
		FileManager fileManager = new FileManager(courseManager, studentManager);
		fileManager.readCourseData(".\\\\jsonfiles\\\\courses.json");
		fileManager.readStudentData(".\\\\jsonfiles\\\\student_names.json",".\\\\jsonfiles\\\\student_ids.json", semester);
		//fileManager.writeData(id, name);
		// courses and students are ready to registration process.
		RegisterManager registerManager = new RegisterManager(fileManager);
		ArrayList<Student> studentList = studentManager.getStudentList();
		registerManager.setStudentList(studentList);
		registerManager.checkRegistration();
		registerManager.createAnalyzeFile();
	}

}
