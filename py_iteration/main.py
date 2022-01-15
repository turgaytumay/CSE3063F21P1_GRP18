from CourseManager import CourseManager
from FileManager import FileManager
from RegisterationManager import RegistrationManager
from StudentManager import StudentManager

semester = "SPRING"
course_categories = ["semester1", "semester2", "semester3", "semester4", "semester5", "semester6",
                      "semester7", "semester8", "FTE", "TE", "NTE"]
course_manager = CourseManager()
student_manager = StudentManager()


def start_app():
    # create courses and schedule them.
    course_manager.create_courses(course_categories)

    # write curriculum to file for testing
    course_manager.print_schedule()

    # create students and random transcript till now.
    student_list = student_manager.create_students()
    student_manager.create_transcript(semester, course_manager)

    # write transcript to student's file. BEFORE REGISTRATION
    student_manager.print_transcript('BEFORE REGISTRATION', 'w')

    # start to simulate this semester registration.
    registration_manager = RegistrationManager(student_list, course_manager)
    # select courses randomly for each student to register.
    registration_manager.start_registration(semester)

    # registration_manager.print_requested_courses() '''we can write this to file for logging.'''

    # send student selected courses to system and advisor control.
    registration_manager.start_to_control()

    # write problems to student's file and analysis file.
    problem_records = registration_manager.get_problem_records()
    file_manager = FileManager.get_instance()
    file_manager.write_analysis_file(problem_records)
    file_manager.write_problem_to_student_file(problem_records)
    student_manager.print_transcript('AFTER REGISTRATION', 'a')


if __name__ == '__main__':
    start_app()