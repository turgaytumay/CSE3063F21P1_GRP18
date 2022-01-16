import Course
import logging

logging.basicConfig(filename='filemanager.log', level=logging.INFO,
                    format='%(asctime)s:%(levelname)s:%(message)s')

class Schedule:
    def __init__(self):
        self.program = {}  # key: day, value: {hours: Course}

    def add_course_to_schedule(self, course: Course, section_no):
        course_schedule = course.course_sections[section_no - 1].schedule
        for day in course_schedule:
            for hours in course_schedule[day]:
                if day not in self.program:
                    self.program[day] = {}
                self.program[day][hours] = course

    # return collision course and hours.
    def check_collision(self, course: Course):
        course_section_schedule = course.course_sections[0].schedule
        for day in course_section_schedule:
            for hours in course_section_schedule[day]:
                if day in self.program and hours == list(self.program[day].keys())[0]:
                    return hours, list(self.program[day].values())[0]
        return None, None

    def print_schedule(self):
        for day in self.program:
            logging.info("****** " + day + " ******")
            for hours in self.program[day].keys():
                logging.info("{} --> {}".format(hours, self.program[day][hours].course_name))
