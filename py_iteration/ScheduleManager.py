import random
from Course import Course
from CourseSection import CourseSection


class ScheduleManager:
    def __init__(self):
        self.days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
        self.availableTimes = [list(range(8, 21)) for i in range(5)]

    def reset_available_times(self):
        self.availableTimes = [list(range(8, 21)) for i in range(5)]

    def schedule_course(self, course: Course, theoretic_hour, practice_hour):
        remain_theoretic_hour = theoretic_hour
        course_section1 = CourseSection(course.quota)

        # schedule theoretic part.
        while remain_theoretic_hour > 0:
            # adjust course hours max 2 hours in a day.
            if remain_theoretic_hour >= 2:
                available_days = self.find_available_days(2)
                scheduled_hour = 2
            else:
                available_days = self.find_available_days(1)
                scheduled_hour = 1

            # select random day.
            rand_day = random.randint(0, len(available_days) - 1)

            # decrease remain hours.
            remain_theoretic_hour -= scheduled_hour

            # get day, starting hour and ending hour of course.
            day = self.days[available_days[rand_day]]
            starting_hour = self.availableTimes[available_days[rand_day]][0]
            ending_hour = starting_hour + scheduled_hour

            # schedule the course section.
            course_section1.add_to_schedule(day, "{}-{}".format(starting_hour, ending_hour))

            # remove hours from available times.
            for i in range(scheduled_hour):
                del self.availableTimes[available_days[rand_day]][0]

        # schedule practice hours if any.
        if practice_hour > 0:
            available_days = self.find_available_days(practice_hour)

            # select random day.
            rand_day = random.randint(0, len(available_days) - 1)

            # get day, starting hour and ending hour of course.
            day = self.days[available_days[rand_day]]
            starting_hour = self.availableTimes[available_days[rand_day]][0]
            ending_hour = starting_hour + practice_hour

            # schedule the course section.
            course_section1.add_to_schedule(day, "{}-{}".format(starting_hour, ending_hour))

            # remove hours from available times.
            for i in range(practice_hour):
                del self.availableTimes[available_days[rand_day]][0]

        course.course_sections.append(course_section1)

    # give available day for that course hours.
    def find_available_days(self, hours):
        available_days = []
        for i in range(len(self.availableTimes)):
            if len(self.availableTimes[i]) < hours:
                continue
            else:
                available_days.append(i)
        if len(available_days) == 0:
            self.reset_available_times()
            available_days = [0, 1, 2, 3, 4]  # every day is available.
        return available_days








