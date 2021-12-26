package iteration2.project;

import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileManager {
	static JSONParser jsonParser = new JSONParser();
	static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
			studentObject.put("Student Credit:", student.getCompletedCredit());
			for (Course course : student.getTakenCourses().keySet()) {
				studentCourses.add("Course Type: " + course.getCourseType() + ", Course Name: " + course.getCourseName()
						+ ", Course Letter: " + student.getTakenCourses().get(course));
			}

			studentObject.put("Courses:", studentCourses);

			// write to file.
			writeToFile(
					"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\transcripts\\"
							+ student.getStudentID() + ".json",
					studentObject);
		}
	}
	
	// write problems to student's file.
	public static void writeProblemToStudentFile(String problemTitle,
			HashMap<Student, ArrayList<Course>> problemHashMap, String semester) {
		switch (problemTitle) {
		case "Quota":
			for (Student student : problemHashMap.keySet()) {
				// Creating a student object and array for courses.
				JSONObject studentObject = new JSONObject();
				studentObject.put("System Control:", "Quota");
				for (Course course : problemHashMap.get(student)) {
					studentObject.put(course.getCourseCode(),
							"Student could not register this course because of quota.");
				}

				// write to file.
				writeToFile(
						"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\transcripts\\"
								+ student.getStudentID() + ".json",
						studentObject);
				
			}
			break;

		case "Prerequisite":
			for (Student student : problemHashMap.keySet()) {
				// Creating a student object and array for courses.
				JSONObject studentObject = new JSONObject();
				studentObject.put("System Control:", "Prerequisite");
				for (Course course : problemHashMap.get(student)) {
					studentObject.put(course.getCourseCode(),
							"Can not be taken because of prereq. " + course.getPrerequisiteCourse());
				}

				// write to file.
				writeToFile(
						"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\transcripts\\"
								+ student.getStudentID() + ".json",
						studentObject);
			}
			break;

		case "Collision":

			/*
			 * 
			 * 
			 * BURASI YAZILACAK.
			 * 
			 * 
			 * 
			 */
			break;

		case "Requirement":
			for (Student student : problemHashMap.keySet()) {
				// Creating a student object and array for courses.
				JSONObject studentObject = new JSONObject();
				studentObject.put("Advisor Control:", "Requirement");
				for (Course course : problemHashMap.get(student)) {
					if (course.getCourseCode().equals("CSE4297")) {
						studentObject.put(course.getCourseCode(),
								"Can not be taken because student completed credits less than 155");
						continue;
					}

					if (course instanceof TeCourse) {
						String message;
						if (semester.equals("FALL")) {
							message = "Can not be taken because student already took 2 TE and in FALL semester only 2 TE can be taken";
						} else {
							message = "Can not be taken because student already took 3 TE and in SPRING semester only 3 TE can be taken";
						}
						studentObject.put(course.getCourseCode(), message);
					} else if (course instanceof NteCourse) {
						studentObject.put(course.getCourseCode(),
								"Can not be taken because student already took 1 NTE.");
					} else if (course instanceof FteCourse) {
						studentObject.put(course.getCourseCode(),
								"Can not be taken because students can't take FTE in FALL semester unless they are graduating this semester");
					}

				}

				// write to file.
				writeToFile(
						"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\transcripts\\"
								+ student.getStudentID() + ".json",
						studentObject);
			}
			break;

		default:
			break;
		}
		
		writeDetailedProblemToOutputFile(problemTitle, problemHashMap);
	}

	// write detailed analysis to output file.
	public static void writeDetailedProblemToOutputFile(String problemTitle, HashMap<Student, ArrayList<Course>> problemHashMap) {
		HashMap<Course, ArrayList<Student>> detailedProblem = new HashMap<Course, ArrayList<Student>>();		
		for(Student student: problemHashMap.keySet()) {
			for(Course course: problemHashMap.get(student)) {
				if(detailedProblem.get(course) == null) {
					detailedProblem.put(course, new ArrayList<Student>());
				}
				detailedProblem.get(course).add(student);
			}
		}
		
		for(Course course: detailedProblem.keySet()) {
			JSONObject problemTextObject = new JSONObject();
			String message = detailedProblem.get(course).size() + " STUDENTS COULD NOT REGISTER FOR " + course.getCourseCode() + " DUE TO " + problemTitle;
			problemTextObject.put(course.getCourseCode(), message);
			String listOfStudent = "";
			int count = 1;
			for(Student student: detailedProblem.get(course)) {
				listOfStudent += count + "- " + student.getStudentID() + " ";
				count++;
			}
			problemTextObject.put("STUDENT LIST:", listOfStudent);
			writeToFile(
					"C:\\Users\\murat\\eclipse-workspace\\project\\src\\main\\java\\iteration2\\iteration2\\project\\src\\main\\java\\output\\output.json",
					problemTextObject);
		}
	}
	
	// common method to write text to file.
	public static void writeToFile(String path, JSONObject textObject) {

		// write to file.
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(path, true);
			fileWriter.write(gson.toJson(textObject));
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}