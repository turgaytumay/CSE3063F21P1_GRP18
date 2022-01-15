import random
from Advisor import Advisor
from CourseManager import CourseManager
from FileManager import FileManager
from Schedule import Schedule
from Student import Student


class StudentManager:
    def __init__(self):
        self.studentList = []
        self.letterGrades = ["FF", "FD", "DD", "DC", "CC", "CC", "CC", "CC", "CB", "CB", "CB", "CB", "BB", "BA", "AA"]

    def create_students(self):
        file_manager = FileManager.get_instance()
        student_datas = file_manager.read_students()   # student_datas = {student id: student name} for all student.
        for std_id in student_datas:
            student = Student(std_id, student_datas[std_id])
            student.set_grade()  # it sets grade of student wrt id.
            student.schedule = Schedule()
            student.advisor = Advisor("Murat", "Fidan")
            self.studentList.append(student)
        return self.studentList

    # create transcript randomly till this semester for each student. BEFORE REGISTRATION..
    def create_transcript(self, semester, course_manager: CourseManager):
        for student in self.studentList:
            student_semester = student.grade * 2
            if semester == 'FALL':
                student_semester -= 1
            if student_semester <= 1:
                return  # no past course.

            # get courses till this semester
            old_courses = course_manager.get_past_semester_courses(student_semester)

            # add student's taken courses by checking prerequisite
            for course in old_courses:
                # could not take because of prerequisite.
                if course.prerequisite_course is not None and student.is_prerequisite_failed(course.prerequisite_course):
                    student.add_taken_course(course, "--")
                else:
                    # control for engineering project-1 for last grade students
                    if semester == 8 and course.course_code == 'CSE4297' and student.get_completed_credit() < 155:
                        student.add_taken_course(course, "--")
                    else:  # add other course with random letter.
                        # if course is elective, select random elective course to add student courses.
                        if course.course_type == 'Elective':
                            if course.course_code == 'TExxx':
                                rand_index = random.randint(0, len(course_manager.electiveCourses['TE'])-1)
                                course = course_manager.electiveCourses['TE'][rand_index]
                            elif course.course_code == 'FTExxx':
                                rand_index = random.randint(0, len(course_manager.electiveCourses['FTE'])-1)
                                course = course_manager.electiveCourses['FTE'][rand_index]
                            else:  # NTE
                                rand_index = random.randint(0, len(course_manager.electiveCourses['NTE'])-1)
                                course = course_manager.electiveCourses['NTE'][rand_index]
                        rand_letter = random.randint(0, len(self.letterGrades) - 1)
                        student.add_taken_course(course, self.letterGrades[rand_letter])

    def print_transcript(self, info, mode):
        file_manager = FileManager.get_instance()
        for student in self.studentList:
            file_manager.write_transcript(student, info, mode)