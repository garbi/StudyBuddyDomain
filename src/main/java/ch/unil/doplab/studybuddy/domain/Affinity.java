package ch.unil.doplab.studybuddy.domain;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class Affinity extends Topic {
    private UUID studentID;
    private UUID teacherID;
    private String teacherName;
    private String teacherEmail;
    private String teacherDescription;
    private int teacherHourlyRate;
    private double teacherRating;

    public Affinity(String title, String description, Level level, UUID studentID, UUID teacherID, String teacherName, String teacherEmail, String teacherDescription, int teacherHourlyRate, double teacherRating) {
        super(title, description, level != null ? EnumSet.of(level) : EnumSet.noneOf(Level.class));
        this.studentID = studentID;
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.teacherDescription = teacherDescription;
        this.teacherHourlyRate = teacherHourlyRate;
        this.teacherRating = teacherRating;
    }

    public Affinity(Topic topic, Level level, Student student, Teacher teacher) {
        this(topic.getTitle(),
                topic.getDescription(),
                level, student.getUUID(),
                teacher.getUUID(),
                teacher.getFullName(),
                teacher.getEmail(),
                teacher.getDescription(),
                teacher.getHourlyFee(),
                teacher.getRatingAverage()
        );
    }

    public Affinity(Topic topic, Level level) {
        this((topic != null ? topic.getTitle() : null), topic != null ? topic.getDescription() : null, level, null, null, null, null, null, 0, Teacher.noRating);
    }

    public Affinity() {
        this(null, null, null, null, null, null, null, null, 0, Teacher.noRating);
    }

    public void setStudent(Student student) {
        this.studentID = student.getUUID();
    }

    public void setTeacher(Teacher teacher) {
        this.teacherID = teacher.getUUID();
        this.teacherName = teacher.getFullName();
        this.teacherEmail = teacher.getEmail();
        this.teacherDescription = teacher.getDescription();
        this.teacherHourlyRate = teacher.getHourlyFee();
        this.teacherRating = teacher.getRatingAverage();
    }

    public UUID getStudentID() {
        return studentID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public UUID getTeacherID() {
        return teacherID;
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

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherDescription() {
        return teacherDescription;
    }

    public void setTeacherDescription(String teacherDescription) {
        this.teacherDescription = teacherDescription;
    }

    public int getTeacherHourlyRate() {
        return teacherHourlyRate;
    }

    public void setTeacherHourlyRate(int teacherHourlyRate) {
        this.teacherHourlyRate = teacherHourlyRate;
    }

    public double getTeacherRating() {
        return teacherRating;
    }

    public void setTeacherRating(double teacherRating) {
        this.teacherRating = teacherRating;
    }

    public String getFormattedTeacherRating() {
        return teacherRating <= 0 ? "not rated yet" : String.format("%.1f / %d", teacherRating, Teacher.maxRating);
    }

    public Level getLevel() {
        return getLevels().isEmpty() ? null : getLevels().iterator().next();
    }

    public String toString() {
        return "Affinity{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", levels=" + getLevels() +
                ", studentID=" + studentID +
                ", teacherID=" + teacherID +
                ", teacherName='" + teacherName + '\'' +
                ", teacherEmail='" + teacherEmail + '\'' +
                ", teacherDescription='" + teacherDescription + '\'' +
                ", teacherHourlyRate=" + teacherHourlyRate +
                ", teacherRating=" + teacherRating +
                '}';
    }
}
