from Course import Course


class FteCourse(Course):
    def __init__(self, course_semester, course_code, course_name, credit, akts, quota, course_type):
        super().__init__(course_semester, course_code, course_name, credit, akts, quota, course_type)

    def check_course_requirement(self, **kwargs):
        accepted_courses = kwargs['accepted_courses']
        no_of_fte_courses = 0
        for course in accepted_courses:
            if type(course).__name__ == 'FteCourse':
                no_of_fte_courses += 1

        if no_of_fte_courses > 1:
            return False, 'can not take FTE courses more than 1.'
        return True, None