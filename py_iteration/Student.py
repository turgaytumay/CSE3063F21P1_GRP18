import Course


class Student:
    def __init__(self, student_id, student_name):
        self.student_id = student_id
        self.student_name = student_name
        self.grade = 0
        self.gpa = 0
        self.taken_courses = {}  # key: Course, value: Grade Letter
        self.requested_courses = []
        self.schedule = None  # it keeps student's weekly course program. Schedule class.
        self.advisor = None
        self.success_coefficient = 0.0
        self.cumulative_credit = 0
        self.cumulative_success_coefficient = 0.0
        self.completed_credit = 0

    def set_grade(self):
        grade = 21 - int(self.student_id[4:6])
        self.grade = grade

    def add_taken_course(self, course: Course, letter):
        self.taken_courses[course] = letter

    def is_prerequisite_failed(self, course_code):
        for course in self.taken_courses:
            if course.course_code == course_code:
                if self.taken_courses[course] == 'FF' or self.taken_courses[course] == 'FD':
                    return True
                else:
                    return False
        return False

    # return failed courses which are available for given semester.
    def get_failed_courses(self, semester):
        failed_courses = []
        for course in self.taken_courses:
            if semester == 'FALL':
                if course.course_semester % 2 == 1 and (self.taken_courses[course] == 'FF' or
                                                        self.taken_courses[course] == 'FD'):
                    failed_courses.append(course)
            else:
                if course.course_semester % 2 == 0 and (self.taken_courses[course] == 'FF' or
                                                        self.taken_courses[course] == 'FD'):
                    failed_courses.append(course)

        return failed_courses


    def get_completed_credit(self):
        for key in self.taken_courses:
            if self.taken_courses[key] == 'FF' or self.taken_courses[key] == 'FD':
                continue
            self.completed_credit += int(str(key.credit)[0])
        return self.completed_credit

    def send_courses_to_advisor(self):
        self.advisor.check_courses(self.schedule, self.requested_courses, self.get_completed_credit())
        

    def calculate_gpa(self):

        if not self.taken_courses:
            return 0.00
        
        for key in self.taken_courses:
            if self.taken_courses[key] == 'FF':
                self.success_coefficient = 0.0
            if self.taken_courses[key] == 'FD':
                self.success_coefficient = 0.5
            if self.taken_courses[key] == 'DD':
                self.success_coefficient = 1.0
            if self.taken_courses[key] == 'DC':
                self.success_coefficient = 1.5
            if self.taken_courses[key] == 'CC':
                self.success_coefficient = 2.0
            if self.taken_courses[key] == 'CB':
                self.success_coefficient = 2.5
            if self.taken_courses[key] == 'BB':
                self.success_coefficient = 3.0
            if self.taken_courses[key] == 'BA':
                self.success_coefficient = 3.5
            if self.taken_courses[key] == 'AA':
                self.success_coefficient = 4.0
        
            key.credit = int(str(key.credit)[0])
            self.cumulative_credit += key.credit
            self.cumulative_success_coefficient += key.credit * self.success_coefficient

        self.gpa = (self.cumulative_success_coefficient /  self.cumulative_credit)
        self.gpa = "%.2f" % self.gpa
        return self.gpa
