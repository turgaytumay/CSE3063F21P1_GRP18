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

    # WILL BE ADDED.
    def get_completed_credit(self):
        return 160

    def send_courses_to_advisor(self):
        self.advisor.check_courses(self.schedule, self.requested_courses, self.get_completed_credit())

    def calculate_gpa(self):
        self.gpa = 2.4
        return self.gpa
