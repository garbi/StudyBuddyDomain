package ch.unil.doplab.studybuddy.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

public class Lesson {

    private LocalDateTime timeslot;
    private Affinity affinity;

    public Lesson() {
        this(null, null);
    }

    public Lesson(LocalDateTime timeslot, Topic topic, Level level) {
        this.timeslot = timeslot;
        this.affinity = new Affinity(topic, level);
    }

    public Lesson(LocalDateTime timeslot, Affinity affinity) {
        this.timeslot = timeslot;
        this.affinity = affinity;
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

    public void setTeacherID(UUID teacherID) {
        affinity.setTeacherID(teacherID);
    }

    public String getTeacherName() {
        return affinity.getTeacherName();
    }

    public void setTeacherName(String teacherName) {
        affinity.setTeacherName(teacherName);
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
        if (!teacher.canCommunicateWith(student)) {
            throw new IllegalStateException(teacher.getFullName() + " and " + student.getFullName()  + " cannot communicate");
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
        if(!teacher.teaches(affinity)) {
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
            throw new IllegalArgumentException("teacher or student cannot be null");
        }
        if (!teacher.getUUID().equals(affinity.getTeacherID())) {
            throw new IllegalStateException("lesson is not with " + teacher.getUsername() + " as teacher");
        }
        if (!student.getUUID().equals(affinity.getStudentID())) {
            throw new IllegalStateException("lesson is not with " + student.getUsername() + " as student");
        }
        if (teacher.getLesson(timeslot) == null) {
            throw new IllegalStateException("no lesson with " + teacher.getUsername() + " at that time (" + timeslot + ")");
        }
        if (student.getLesson(timeslot) == null) {
            throw new IllegalStateException("no lesson with " + student.getUsername() + " at that time (" + timeslot + ")");
        }
        student.deposit(teacher.getHourlyFee());
        teacher.withdraw(teacher.getHourlyFee());
        teacher.removeLesson(this);
        student.removeLesson(this);
        teacher.addTimeslot(timeslot);
    }
}
