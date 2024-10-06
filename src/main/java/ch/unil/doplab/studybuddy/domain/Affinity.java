package ch.unil.doplab.studybuddy.domain;
import java.util.EnumSet;
import java.util.UUID;

public class Affinity extends Topic {
    private UUID studentID;
    private UUID teacherID;
    private String studentName;
    private String studentEmail;
    private String teacherName;
    private String teacherEmail;
    private String teacherBiography;
    private int teacherHourlyRate;
    private double teacherRating;

    public Affinity(String title, String description, Level level, UUID studentID, String studentName, String studentEmail, UUID teacherID, String teacherName, String teacherEmail, String teacherDescription, int teacherHourlyRate, double teacherRating) {
        super(title, description, level != null ? EnumSet.of(level) : EnumSet.noneOf(Level.class));
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.teacherBiography = teacherDescription;
        this.teacherHourlyRate = teacherHourlyRate;
        this.teacherRating = teacherRating;
    }

    public Affinity(Topic topic, Level level, Student student, Teacher teacher) {
        this(topic.getTitle(),
                topic.getDescription(),
                level,
                student.getUUID(),
                student.getFullName(),
                student.getEmail(),
                teacher.getUUID(),
                teacher.getFullName(),
                teacher.getEmail(),
                teacher.getBiography(),
                teacher.getHourlyFee(),
                teacher.getRatingAverage()
        );
    }

    public Affinity(Topic topic, Level level) {
        this((topic != null ? topic.getTitle() : null), topic != null ? topic.getDescription() : null, level, null, null, null, null, null, null, null, 0, Teacher.noRating);
    }

    public Affinity() {
        this(null, null, null, null, null, null, null, null, null, null, 0, Teacher.noRating);
    }

    public void setStudent(Student student) {
        this.studentID = student.getUUID();
        this.studentName = student.getFullName();
        this.studentEmail = student.getEmail();
    }

    public void setTeacher(Teacher teacher) {
        this.teacherID = teacher.getUUID();
        this.teacherName = teacher.getFullName();
        this.teacherEmail = teacher.getEmail();
        this.teacherBiography = teacher.getBiography();
        this.teacherHourlyRate = teacher.getHourlyFee();
        this.teacherRating = teacher.getRatingAverage();
    }

    public UUID getStudentID() {
        return studentID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentMail) {
        this.studentEmail = studentMail;
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

    public String getTeacherBiography() {
        return teacherBiography;
    }

    public void setTeacherBiography(String teacherBiography) {
        this.teacherBiography = teacherBiography;
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
        return Utils.formatTeacherRating(teacherRating);
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
                ", studentName=" + studentName +
                ", studentEmail=" + studentEmail +
                ", teacherID=" + teacherID +
                ", teacherName='" + teacherName + '\'' +
                ", teacherEmail='" + teacherEmail + '\'' +
                ", teacherDescription='" + teacherBiography + '\'' +
                ", teacherHourlyRate=" + teacherHourlyRate +
                ", teacherRating=" + teacherRating +
                '}';
    }
}
