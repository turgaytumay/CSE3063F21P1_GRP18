package iteration2.project;

import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileManager {
	static JSONParser jsonParser = new JSONParser();

	// read requested courses. FTE, TE, NTE, Semester1 etc.
	public static JSONArray readCourses(String path, String courseCategory) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			JSONObject allCourses = (JSONObject) jsonParser.parse(reader); // all courses.
			JSONArray requestedCourses = (JSONArray) allCourses.get(courseCategory);
			return requestedCourses;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	// read student names and ids.
	public static HashMap<String, JSONArray> readStudents(String idPath, String namePath) {
		HashMap<String, JSONArray> students = new HashMap<String, JSONArray>();
		try {
			// read ids.
			FileReader reader = new FileReader(idPath);
			Object studentIDs = jsonParser.parse(reader); // all courses.
			JSONArray studentIDsArray = (JSONArray) studentIDs;
			students.put("IDs", studentIDsArray);

			// read names.
			FileReader reader2 = new FileReader(namePath);
			Object studentNames = jsonParser.parse(reader2);
			JSONArray studentNamesArray = (JSONArray) studentNames;
			students.put("Names", studentNamesArray);
			return students;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// write transcript to file for student.
	public static void writeTranscript(ArrayList<Student> studentList) {
		for (Student student : studentList) {
			// Creating a student object and array for courses.
			JSONObject studentObject = new JSONObject();
			JSONArray studentCourses = new JSONArray();
			studentObject.put("Student Name", student.getStudentName());
			for (Course course : student.getTakenCourses().keySet()) {
				studentCourses.add("Course Name: " + course.getCourseName() + ", Course Letter: "
						+ student.getTakenCourses().get(course));
			}

			studentObject.put("Courses:", studentCourses);

			// write to file.
			writeToFile(
					"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\transcripts\\"
							+ student.getStudentID() + ".json",
					studentObject);
		}
	}

	// write problems to output file.
	public static void writeProblemToFile(String problemTitle, Set<Student> studentList) {
		// write problem to problem file.
		JSONObject ProblemTextObject = new JSONObject();
		ProblemTextObject.put(problemTitle, studentList.size());
		int count = 1;
		for (Student student : studentList) {
			ProblemTextObject.put(count, student.getStudentID());
		}
		
		// write to file.
		writeToFile("C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\output\\output.json", ProblemTextObject);
	}


	// common method to write text to file.
	public static void writeToFile(String path, JSONObject textObject) {
		// write to file.
		try {
			File file = new File(path);
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(
					path,
					true);
			fileWriter.write(textObject.toJSONString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}