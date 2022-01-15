from Course import Course
from FileManager import FileManager
from FteCourse import FteCourse
from NteCourse import NteCourse
from ScheduleManager import ScheduleManager
import random

from TeCourse import TeCourse


class CourseManager:
    def __init__(self):
        self.curriculumCourses = {}  # key: semester, value: [Courses]
        self.electiveCourses = {}    # key: type, value:[Courses]
        self.scheduleManager = ScheduleManager()   # to create and schedule courses.

    def create_courses(self, course_categories):
        file_manager = FileManager.get_instance()
        for i in range(len(course_categories)):
            courses_json = file_manager.read_courses(course_categories[i])
            for obj in courses_json:
                quota_rand = random.randint(40, 70)
                # create course according to type.
                if course_categories[i] == 'FTE':
                    course = FteCourse(8, obj['Course Code'], obj['Course Name'], obj['Credit'], obj['ECTS'],
                                    quota_rand, obj['Type'])
                elif course_categories[i] == 'NTE':
                    course = NteCourse(8, obj['Course Code'], obj['Course Name'], obj['Credit'], obj['ECTS'],
                                       quota_rand, obj['Type'])
                elif course_categories[i] == 'TE':
                    course = TeCourse(8, obj['Course Code'], obj['Course Name'], obj['Credit'], obj['ECTS'],
                                      quota_rand, obj['Type'])
                else:  # must courses.
                    course = Course(i+1, obj['Course Code'], obj['Course Name'], obj['Credit'], obj['ECTS'],
                                    quota_rand, obj['Type'])

                # add prerequisite course if exists
                if 'precondition' in obj:
                    course.prerequisite_course = obj['precondition']

                # schedule the course
                theoretic_hour = int(obj['T'])
                practice_hour = int(obj['U'])
                self.scheduleManager.schedule_course(course, theoretic_hour, practice_hour)

                # add course to its course dict.
                if i < 8:  # must courses.
                    if course_categories[i] not in self.curriculumCourses:
                        self.curriculumCourses[course_categories[i]] = []
                    self.curriculumCourses[course_categories[i]].append(course)
                else:  # elective courses.
                    if course_categories[i] not in self.electiveCourses:
                        self.electiveCourses[course_categories[i]] = []
                    self.electiveCourses[course_categories[i]].append(course)

            # reset available times for next course category schedule
            self.scheduleManager.reset_available_times()

    # to create transcript BEFORE REGISTRATION.
    def get_past_semester_courses(self, semester):
        courses = []
        for i in range(1, semester):
            for course in self.curriculumCourses["semester" + str(i)]:
                courses.append(course)
        return courses

    def get_current_semester_courses(self, semester):
        return self.curriculumCourses[semester]

    # determine elective courses to open for registration.
    def get_five_elective_courses(self, elective_type):
        courses = []
        count = 0
        while count < 5:
            rand_index = random.randint(0, len(self.electiveCourses[elective_type]) - 1)
            if self.electiveCourses[elective_type][rand_index] not in courses:
                courses.append(self.electiveCourses[elective_type][rand_index])
                count += 1
        return courses

    def print_schedule(self):
        file = open('outputFiles/course_curriculum.txt', 'w')
        for semester in self.curriculumCourses:
            file.write("\n************{}************\n".format(semester))
            for course in self.curriculumCourses[semester]:
                file.write("Course Code: {}, Course Schedule: {}\n".format(course.course_code, course.course_sections[0].schedule))
        for typ in self.electiveCourses:
            file.write("\n************{}************\n".format(typ))
            for course in self.electiveCourses[typ]:
                file.write("Course Code: {}, Course Schedule: {}\n".format(course.course_code, course.course_sections[0].schedule))
        file.close()
