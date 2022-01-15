class CourseSection:
    def __init__(self, quota):
        self.quota = quota
        self.schedule = {}  # key: day, value: [hours] --> hours: "11-13"

    def add_to_schedule(self, day, hours):
        if day not in self.schedule:
            self.schedule[day] = []
        self.schedule[day].append(hours)