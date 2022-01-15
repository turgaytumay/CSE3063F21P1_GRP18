from Course import Course


class NteCourse(Course):
    def __init__(self, course_semester, course_code, course_name, credit, akts, quota, course_type):
        super().__init__(course_semester, course_code, course_name, credit, akts, quota, course_type)

    def check_course_requirement(self, **kwargs):
        accepted_courses = kwargs['accepted_courses']
        no_of_nte_courses = 0
        for course in accepted_courses:
            if type(course).__name__ == 'NteCourse':
                no_of_nte_courses += 1

        if no_of_nte_courses > 2:
            return False, 'can not take NTE courses more than 2.'
        return True, None