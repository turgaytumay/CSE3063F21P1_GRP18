import random

from Course import Course
from CourseManager import CourseManager
from Student import Student


class RegistrationManager:
    def __init__(self, student_list, course_manager: CourseManager):
        self.student_list = student_list
        self.course_manager = course_manager
        # key: student object, value: [{course_object: error_text}], these are for analysis and log files.
        self.quota_problems = {}
        self.prerequisite_problems = {}
        self.collision_problems = {}
        self.requirement_problems = {}

    def start_registration(self, semester):
        for student in self.student_list:
            opened_courses = self.get_opened_courses(student, semester)
            max_course_number = min(10, len(opened_courses))
            min_course_number = min(5, len(opened_courses))
            rand_course_number = random.randint(min_course_number, max_course_number)  # student can take 10 course max.
            # select course randomly.
            requested_courses = []
            count = 0
            while count < rand_course_number:
                rand_index = random.randint(0, len(opened_courses) - 1)
                selected_course = opened_courses[rand_index]
                opened_courses.remove(selected_course)
                requested_courses.append(selected_course)
                count += 1
            # save requested courses to student.
            student.requested_courses = requested_courses

    # return opened courses for this semester for the student
    def get_opened_courses(self, student: Student, semester):
        possible_courses = []
        elective_types = []  # to determine which elective type will be opened.
        # first, add failed courses of student by checking semester
        for failed_course in student.get_failed_courses(semester):
            possible_courses.append(failed_course)

        # second, add this semester courses.
        curr_semester = student.grade * 2
        if semester == 'FALL':
            curr_semester -= 1
        for course in self.course_manager.get_current_semester_courses("semester"+str(curr_semester)):
            if course.course_code == 'TExxx':
                elective_types.append('TE')
            elif course.course_code == 'FTExxx':
                elective_types.append('FTE')
            elif course.course_code == 'NTExxx':
                elective_types.append('NTE')
            else:
                if course not in possible_courses:
                    possible_courses.append(course)

        # third, add 5 elective courses for each elective course type.
        for typ in elective_types:
            for course in self.course_manager.get_five_elective_courses(typ):
                if course not in possible_courses:
                    possible_courses.append(course)
        return possible_courses

    def print_requested_courses(self):
        for student in self.student_list:
            print("Student Name: {}, Student Number: {}, Student Grade: {}".format(student.student_name,
                                                                                   student.student_id,
                                                                                   student.grade))
            for course in student.requested_courses:
                print("Course Code: {}, Course Type: {}, Course Semester: {}".format(course.course_code,
                                                                                     course.course_type,
                                                                                     course.course_semester))
            print("******************************")

    def start_to_control(self):
        for student in self.student_list:
            self.start_system_control(student)
            self.send_advisor(student)
            # student.schedule.print_schedule()  '''we can save student schedule to its file'''


    def start_system_control(self, student: Student):
        rejected_courses = []  # store rejected courses to delete from requested list.
        for course in student.requested_courses:
            # first check quota.
            can_take = self.check_quota_problems(course)
            if not can_take:
                if student not in self.quota_problems:
                    self.quota_problems[student.student_id] = []
                self.quota_problems[student.student_id].append({course.course_code: 'can not take ' + course.course_code + ' because of quota!'})
                rejected_courses.append(course)
                continue

            # second check prerequisite
            can_take = self.check_prerequisite_problems(student, course)
            if not can_take:
                if student not in self.prerequisite_problems:
                    self.prerequisite_problems[student.student_id] = []
                self.prerequisite_problems[student.student_id].append({course.course_code: 'can not take '
                                                               + course.course_code
                                                               + ' because of prerequisite of '
                                                               + course.prerequisite_course})
                rejected_courses.append(course)
                continue

        # delete rejected courses from requested list below.
        for course in rejected_courses:
            student.requested_courses.remove(course)


        '''we can write this to file for logging.'''
        if len(rejected_courses) != 0:
            if student.student_id in self.quota_problems.keys():
                print("For student: {}, rejected courses because of quota:"
                      .format(student.student_name))
                for problem in self.quota_problems[student.student_id]:
                    course_code = list(problem.keys())[0]
                    print(course_code, end=', ')
                print('\n***********')
            if student.student_id in self.prerequisite_problems.keys():
                print("For student: {}, rejected courses because of prerequisite:"
                      .format(student.student_name))
                for problem in self.prerequisite_problems[student.student_id]:
                    course_code = list(problem.keys())[0]
                    print(course_code, end=', ')
                print('\n***********')

    def send_advisor(self, student: Student):
        collesion_problems, requirement_problems = student.advisor.check_courses(student)

        # add problem to problem list
        for problem in collesion_problems:
            if student not in self.collision_problems:
                self.collision_problems[student.student_id] = []
            self.collision_problems[student.student_id].append(problem)

        for problem in requirement_problems:
            if student not in self.requirement_problems:
                self.requirement_problems[student.student_id] = []
            self.requirement_problems[student.student_id].append(problem)

        '''we can write this to file for logging.'''
        if len(collesion_problems) != 0:
            print("For student: {}, rejected courses because of collision:".format(student.student_id))
            for problem in collesion_problems:
                course_code = list(problem.keys())[0]
                print(course_code, end=', ')
            print('\n***********')
        if len(requirement_problems) != 0:
            print("For student: {}, rejected courses because of requirement problem:".format(student.student_id))
            for problem in requirement_problems:
                course_code = list(problem.keys())[0]
                print(course_code, end=', ')
            print('\n***********')

    # **controls**
    def check_quota_problems(self, course: Course):
        if course.quota != 0:
            return True
        return False

    def check_prerequisite_problems(self, student: Student, course: Course):
        if course.prerequisite_course is not None and student.is_prerequisite_failed(course.prerequisite_course):
            return False
        return True


    def get_problem_records(self):
        return {
            'quota': self.quota_problems,
            'prerequisite': self.prerequisite_problems,
            'collision': self.collision_problems,
            'requirement': self.requirement_problems
        }



    