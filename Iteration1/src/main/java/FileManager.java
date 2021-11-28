import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Random;

public class FileManager {
	
	private CourseManager courseManager;
	private StudentManager studentManager;
	JSONParser jsonParser = new JSONParser();
	JSONParser student_jsonParser = new JSONParser();
	Random rand = new Random();
	private static  ArrayList<String> student_id_array = new ArrayList<String>(270);
	private static ArrayList<String> student_name_array = new ArrayList<String>(270);
	
	public FileManager(CourseManager courseManager, StudentManager studentManager) {
		this.courseManager = courseManager;
		this.studentManager = studentManager;
		
	}
	
	public void readCourseData(String path){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			Object obj = jsonParser.parse(reader);
			JSONObject courseArray = (JSONObject) obj;
			for(int i = 1; i<9; i++) {
				JSONArray semester = (JSONArray)courseArray.get("semester" + String.valueOf(i));
				courseManager.createCourses(semester, i);
			}
			
			// BURADA AYNI DOSYADA TE - FTE - NTE DERSLERINI DE OKUYUP COURSEMANAGER KISMINDA BIR ARRAY'DE TUTULACAK.
			//JSONArray ders_tipi = (JSONArray)courseArray.get(DERS_TIPI);
			//courseManager.setDers_tipi() blah blah blah.
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void readStudentData(String path, String path2, String semester) {
		/*try{
			/*BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(path2), "UTF-8"));
			
			Object obj = student_jsonParser.parse(reader);
			Object obj1 = student_jsonParser.parse(reader1);
			
			System.out.println();
			JSONArray student_names = (JSONArray) obj;
			JSONArray student_ids = (JSONArray) obj1;
			System.out.println(student_names.size());
			System.out.println(student_ids.size());
			
			
			
			
			for (int i  = 0; i<student_names.size(); i++) {
				int int_random = rand.nextInt(student_names.size()); 
				studentManager.createRandomStudent(student_names.get(int_random).toString(),student_ids.get(int_random).toString(), semester);
			}*/
			//System.out.println(int_random);
			
			
	
		/*} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}*/
		JSONParser jsonParser = new JSONParser();
        
        try 
        {
            //Read JSON file
        	FileReader reader = new FileReader(path);
        	Object obj = jsonParser.parse(reader);
        	 
            JSONArray employeeList = (JSONArray) obj;
             
            // Iterate over employee array
            employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
        	
            FileReader reader1 = new FileReader(path2);
            Object obj1 = jsonParser.parse(reader1);
            
            JSONArray student_ids = (JSONArray) obj1;
            
             
            // Iterate over employee array
            student_ids.forEach( emp -> parseEmployeeObject1( (JSONObject) emp ) );
            //printArray();
            for (int i = 0; i<270;i++) {
            	int int_random = rand.nextInt(270); 
            	studentManager.createRandomStudent(student_id_array.get(int_random),student_name_array.get(int_random), semester);
            }
            
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }	
			
	}
	
	public void writeData(String id , String name) {
		JSONObject stdmobj1 = new JSONObject();
        stdmobj1.put(id, name);
        
        
        
        
        
        
        System.out.println(stdmobj1);
	}

	public static void parseEmployeeObject(JSONObject employee) 
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) employee;
         
        //Get employee first name
        
        String firstName = (String) employeeObject.get("name");    
        student_name_array.add(firstName);
        
         
        
    }
	public static void parseEmployeeObject1(JSONObject employee) 
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) employee;
         
        //Get employee first name
        
        String firstName = (String) employeeObject.get("random_id");    
        student_id_array.add(firstName);
        
         
        
    }
	public void printArray() {
		for (String number : student_id_array) {
            System.out.println(number);
         } 
        for (String number : student_name_array) {
            System.out.println(number);
         } 
	}

}
