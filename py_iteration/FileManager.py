import json

from Course import Course
from Student import Student


class FileManager:
    __instance = None

    @staticmethod
    def get_instance():
        if FileManager.__instance is None:
            FileManager()
        return FileManager.__instance

    def __init__(self):
        if FileManager.__instance is not None:
            raise Exception("This class singleton!")
        else:
            self.courses_file = open('jsonFiles/courses.json')
            self.course_data = json.load(self.courses_file)
            self.student_ids_file = open('jsonFiles/student_ids.json')
            self.student_id_data = json.load(self.student_ids_file)
            self.student_names_file = open('jsonFiles/student_names.json')
            self.student_names_data = json.load(self.student_names_file)
            FileManager.__instance = self

    # read courses of given category from file.
    def read_courses(self, course_category):
        try:
            return self.course_data[course_category]
        except Exception as e:
            print(e)

    # read students
    def read_students(self):
        students = {}
        try:
            lenght = min(len(self.student_id_data), len(self.student_names_data))
            for i in range(lenght):
                std_id = self.student_id_data[i]['random_id']
                std_name = self.student_names_data[i]['name']
                students[std_id] = std_name
        except Exception as e:
            print(e)
        finally:
            return students

    # write problem analysis file
    def write_analysis_file(self, problem_dict):
        file = open('outputFiles/output.txt', 'w')
        for problem in problem_dict:
            if len(problem_dict[problem]) == 0:
                file.write("\n**There is not any student could not register because of {} problem".format(problem))
                continue

            file.write('\n**{} students can not register course because of {} problem.'.format(len(problem_dict[problem]), problem))
            detail_analysis, student_list = self.find_problem_count_of_courses(problem_dict[problem])
            for course in detail_analysis:
                file.write('\n   --> {} students can not register {} because of {} problem'.format(detail_analysis[course],
                                                                                              course, problem))

                count = 1
                for student in student_list[course]:
                    file.write('\n      --> {}. {}'.format(count, student))
                    count += 1
        file.close()

        # find total problem because of courses. '''Ex: 15 students could not register A course.'''
    def find_problem_count_of_courses(self, problem_records):
        summary = {}  # course: problem_count
        student_list = {}  # course: student_list
        for student in problem_records:
            for problem in problem_records[student]:
                course = list(problem.keys())[0]
                if course not in summary:
                    summary[course] = 0
                summary[course] += 1

                if course not in student_list:
                    student_list[course] = []
                if student not in student_list[course]:
                    student_list[course].append(student)

        return summary, student_list

    # write problem to student file
    def write_problem_to_student_file(self, problem_dict):
        for problem in problem_dict:
            for student in problem_dict[problem]:
                file = open("outputFiles/studentFiles/" + student + '.txt', 'a')
                file.write("\n******{} Problem******".format(problem))
                for error in problem_dict[problem][student]:
                    file.write("\n{}: {}".format(list(error.keys())[0], list(error.values())[0]))
                file.write("\n*********************\n")
                file.close()

    # write transcript to student file.
    def write_transcript(self, student: Student, info, mode):
        file = open('outputFiles/studentFiles/' + student.student_id + '.txt', mode)
        file.write("\n*************" + info + "*************\n")
        file.write("\nStudent ID: {}\nStudent Name: {}\nStudent GPA: {}\n*****COURSES*****\n".format(student.student_id,
                                                                                                   student.student_name,
                                                                                                   student.calculate_gpa()))
        for course in student.taken_courses:
            file.write("Course Code: {}, Course Grade Letter: {}\n".format(course.course_code, student.taken_courses[course]))
        file.write("*************\n")
        file.close()
