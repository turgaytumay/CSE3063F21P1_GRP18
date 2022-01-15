from Student import Student


class Course:
    def __init__(self, course_semester, course_code, course_name, credit, akts, quota, course_type):
        self.course_semester = course_semester
        self.course_code = course_code
        self.course_name = course_name
        self.prerequisite_course = None
        self.course_sections = []
        self.credit = credit
        self.akts = akts
        self.quota = quota
        self.course_type = course_type

    # check special requirement for course, it will be overloading by subclasses
    def check_course_requirement(self, **kwargs):  # here control for engineering project.
        completed_credit = kwargs['completed_credit']
        if self.course_code == 'CSE4297' and completed_credit < 155:
            return False, 'can not take because completed credit < 155'
        return True, None
