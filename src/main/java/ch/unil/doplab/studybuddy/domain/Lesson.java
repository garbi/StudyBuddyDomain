package ch.unil.doplab.studybuddy.domain;

import static ch.unil.doplab.studybuddy.domain.Utils.testMode;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Lesson {
    private LocalDateTime timeslot;
    private Affinity affinity;
    private Rating rating;
    private Rating ratingUpdate;

    public Lesson() {
        this(null, null);
    }

    public Lesson(LocalDateTime timeslot, Topic topic, Level level) {
        this.timeslot = timeslot;
        this.affinity = new Affinity(topic, level);
        this.rating = Rating.NO_RATING;
        this.ratingUpdate = rating;
    }

    public Lesson(LocalDateTime timeslot, Affinity affinity) {
        this.timeslot = timeslot;
        this.affinity = affinity;
        this.rating = Rating.NO_RATING;
        this.ratingUpdate = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void updateRating() {
        this.rating = ratingUpdate;
    }

    public Rating getRatingUpdate() {
        return ratingUpdate;
    }

    public void setRatingUpdate(Rating update) {
        if (update != null) {
            this.ratingUpdate = update;
        } else {
            this.ratingUpdate = Rating.NO_RATING;
        }
    }

    public Affinity getAffinity() {
        return affinity;
    }

    public void setAffinity(Affinity affinity) {
        this.affinity = affinity;
    }

    public LocalDateTime getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(LocalDateTime timeslot) {
        this.timeslot = timeslot;
    }

    public Date getDate() {
        return Date.from(timeslot.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }

    public String getTitle() {
        return affinity.getTitle();
    }

    public Level getLevel() {
        return affinity.getLevel();
    }

    public UUID getTeacherID() {
        return affinity.getTeacherID();
    }

    public void setStudentID(UUID studentID) {
        affinity.setStudentID(studentID);
    }

    public UUID getStudentID() {
        return affinity.getStudentID();
    }

    public String getStudentName() {
        return affinity.getStudentName();
    }

    public void setStudentName(String studentName) {
        affinity.setStudentName(studentName);
    }

    public String getStudentEmail() {
        return affinity.getStudentEmail();
    }

    public void setStudentEmail(String studentEmail) {
        affinity.setStudentEmail(studentEmail);
    }

    public void setTeacherID(UUID teacherID) {
        affinity.setTeacherID(teacherID);
    }

    public String getTeacherName() {
        return affinity.getTeacherName();
    }

    public void setTeacherName(String teacherName) {
        affinity.setTeacherName(teacherName);
    }

    public String getTeacherEmail() {
        return affinity.getTeacherEmail();
    }

    public void setTeacherEmail(String teacherEmail) {
        affinity.setTeacherEmail(teacherEmail);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "timeslot=" + timeslot +
                ", affinity=" + affinity +
                '}';
    }

    public void book(Teacher teacher, Student student) {
        if (teacher == null || student == null) {
            throw new IllegalArgumentException("Teacher or student cannot be null");
        }
        if (timeslot == null || affinity == null || affinity.getTitle() == null || affinity.getLevel() == null) {
            throw new IllegalStateException("Timeslot, topic or level cannot be null");
        }
        var timeMargin = LocalDateTime.now().plusDays(1);
        if (!testMode() && timeslot.isBefore(timeMargin)) {
            throw new IllegalStateException("Lessons must be booked at least one day in advance");
        }
        if (!teacher.canCommunicateWith(student)) {
            throw new IllegalStateException(teacher.getFullName() + " and " + student.getFullName() + " cannot communicate");
        }
        if (student.getBalance() < teacher.getHourlyFee()) {
            throw new IllegalStateException("Student " + student.getFullName() + " has insufficient funds");
        }
        if (student.getLesson(timeslot) != null) {
            throw new IllegalStateException("Student " + student.getFullName() + " already has a lesson at this time");
        }
        if (!teacher.isAvailable(timeslot)) {
            throw new IllegalStateException("Teacher " + teacher.getFullName() + " has no availability at this time");
        }
        if (!teacher.teaches(affinity)) {
            throw new IllegalStateException("Teacher " + teacher.getFullName() + " does not teach " + affinity.getTitle() + " at " + affinity.getLevel());
        }
        affinity.setStudent(student);
        affinity.setTeacher(teacher);
        student.withdraw(teacher.getHourlyFee());
        teacher.deposit(teacher.getHourlyFee());
        teacher.putLesson(this.getTimeslot(), this);
        student.putLesson(this.getTimeslot(), this);
        teacher.removeTimeslot(this.getTimeslot());
    }

    public void cancel(Teacher teacher, Student student) {
        if (teacher == null || student == null) {
            throw new IllegalArgumentException("Teacher or student cannot be null");
        }
        if (!teacher.getUUID().equals(affinity.getTeacherID())) {
            throw new IllegalStateException("Lesson is not with " + teacher.getUsername() + " as teacher");
        }
        if (!student.getUUID().equals(affinity.getStudentID())) {
            throw new IllegalStateException("Lesson is not with " + student.getUsername() + " as student");
        }
        if (teacher.getLesson(timeslot) == null) {
            throw new IllegalStateException("No lesson with " + teacher.getUsername() + " at that time (" + timeslot + ")");
        }
        if (student.getLesson(timeslot) == null) {
            throw new IllegalStateException("No lesson with " + student.getUsername() + " at that time (" + timeslot + ")");
        }
        student.deposit(teacher.getHourlyFee());
        teacher.withdraw(teacher.getHourlyFee());
        teacher.removeLesson(this);
        student.removeLesson(this);
        teacher.addTimeslot(timeslot);
    }

    public boolean isCompleted() {
        return LocalDateTime.now().isAfter(timeslot);
    }

    public String getTopic() {
        return affinity.getTitle();
    }
}
