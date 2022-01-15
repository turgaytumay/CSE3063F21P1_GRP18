from Course import Course
from Schedule import Schedule
from Student import Student


class Advisor:
    def __init__(self, name, surname):
        self.name = name
        self.surname = surname

    def check_courses(self, student: Student):
        collision_problems = []
        requirement_problems = []
        accepted_courses = []
        for course in student.requested_courses:
            self.check_collision(student.schedule, course)
            collision_problem = self.check_collision(student.schedule, course)
            if collision_problem is not None:
                collision_problems.append(collision_problem)
                continue
            requirement_problem = self.check_requirement(course, student.get_completed_credit(), accepted_courses)
            if requirement_problem is not None:
                requirement_problems.append(requirement_problem)
                continue
            accepted_courses.append(course)
            self.approval_course(student, course)

        student.requested_courses = []

        # and return problems for analysis files.
        return collision_problems, requirement_problems

    def check_collision(self, schedule: Schedule, course: Course):
        collision_problem = None
        collision_hours, collision_course = schedule.check_collision(course)
        if collision_course is not None:
            total_collesion = int(collision_hours.split('-')[1]) - int(collision_hours.split('-')[0])
            collision_problem = {course.course_code: 'can not take '
                                                                     + course.course_code
                                                                     + ' because of ' + str(total_collesion)
                                                                     + ' hour(s) collision with '
                                                                     + collision_course.course_code}

        return collision_problem

    def check_requirement(self, course: Course, completed_credit, accepted_courses):
        requirement_problem = None
        can_take, error_text = course.check_course_requirement(completed_credit=completed_credit,
                                                                   accepted_courses=accepted_courses)
        if not can_take:
            requirement_problem = {course.course_code: error_text}

        return requirement_problem

    def approval_course(self, student: Student, course: Course):
        student.taken_courses[course] = 'NEW'  # give 'NEW' as letter.
        course.quota -= 1
        student.schedule.add_course_to_schedule(course, 1)