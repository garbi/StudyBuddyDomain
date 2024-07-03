package ch.unil.doplab.studybuddy.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

public class Lesson {

    private LocalDateTime timeslot;
    private Topic topic;
    private UUID studentID;
    private UUID teacherID;
    private String teacherName;

    public Lesson() {
        this(null, null, null, null, null, null);
    }

    public Lesson(LocalDateTime timeslot, Topic topic, Level level) {
        this(timeslot, topic, level, null, null, null);
    }

    public Lesson(LocalDateTime timeslot, Topic topic, Level level, UUID studentID, UUID teacherID, String teacherName) {
        this.timeslot = timeslot;
        this.studentID = studentID;
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        if (topic != null && level != null) {
            this.topic = new Topic(topic.getTitle(), topic.getDescription(), EnumSet.of(level));
        }  else {
            this.topic = null;
        }
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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

    public UUID getTeacherID() {
        return teacherID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public UUID getStudentID() {
        return studentID;
    }

    public void setTeacherID(UUID teacherID) {
        this.teacherID = teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "timeslot=" + timeslot +
                ", topic=" + topic.getTitle() +
                ", studentID=" + studentID +
                ", teacherID=" + teacherID +
                '}';
    }

    public void book(Teacher teacher, Student student) {
        if (timeslot == null || topic == null || topic.getLevels() == null) {
            throw new IllegalArgumentException("timeslot, topic or level cannot be null");
        }
        if (topic.getLevels().size() != 1) {
            throw new IllegalArgumentException("there must be exactly one level in the lesson");
        }
        if (teacher == null || student == null) {
            throw new IllegalArgumentException("teacher or student cannot be null");
        }
        if (!teacher.canCommunicateWith(student)) {
            throw new IllegalArgumentException(teacher.getUsername() + " and " + student.getUsername()  + " cannot communicate");
        }
        if (student.getBalance() < teacher.getHourlyFee()) {
            throw new IllegalArgumentException(student.getUsername() + " has insufficient funds");
        }
        if (student.getLesson(timeslot) != null) {
            throw new IllegalArgumentException(student.getUsername() + " already has a lesson at this time");
        }
        if (!teacher.isAvailable(timeslot)) {
            throw new IllegalArgumentException(teacher.getUsername() + " has no availability at this time");
        }
        if(!teacher.teaches(topic)) {
            throw new IllegalArgumentException(teacher.getUsername() + " does not teach " + topic.getTitle() + " at " + topic.getLevels());
        }
        studentID = student.getUUID();
        teacherID = teacher.getUUID();
        teacherName = teacher.getFullName();
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
        if (!teacher.getUUID().equals(teacherID)) {
            throw new IllegalArgumentException("lesson is not with " + teacher.getUsername() + " as teacher");
        }
        if (!student.getUUID().equals(studentID)) {
            throw new IllegalArgumentException("lesson is not with " + student.getUsername() + " as student");
        }
        if (teacher.getLesson(timeslot) == null) {
            throw new IllegalArgumentException("no lesson with " + teacher.getUsername() + " at that time (" + timeslot + ")");
        }
        if (student.getLesson(timeslot) == null) {
            throw new IllegalArgumentException("no lesson with " + student.getUsername() + " at that time (" + timeslot + ")");
        }
        student.deposit(teacher.getHourlyFee());
        teacher.withdraw(teacher.getHourlyFee());
        teacher.removeLesson(this);
        student.removeLesson(this);
        teacher.addTimeslot(timeslot);
    }
}
