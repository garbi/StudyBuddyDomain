package ch.unil.doplab.studybuddy.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Student extends User {

    Set<Topic> interests;

    public Student() {
        super();
        interests = new TreeSet<>();
    }

    public Student(String firstName, String lastName, String email, String username, String password) {
        this(null, firstName, lastName, email, username, password);
    }

    public Student(UUID id, String firstName, String lastName, String email, String username, String password) {
        super(id, firstName, lastName, email, username, password);
        interests = new TreeSet<>();
    }

    public String describe() {
        return super.describe() + ", interests=" + interests;
    }

    public void addInterest(Topic interest) {
        interests.add(interest);
    }

    public void removeInterest(Topic interest) {
        interests.remove(interest);
    }

    public void removeInterest(String title) {
        interests.removeIf(interest -> interest.getTitle().equals(title));
    }

    public Set<Affinity> findAffinitiesWith(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher must not be null");
        }
        if (teacher.getCourses().isEmpty() || this.interests.isEmpty() || !canCommunicateWith(teacher)) {
            return Collections.emptySet();
        }
        var affinities = interests.stream()
                .filter(teacher::teaches)
                .map(interest -> new Affinity(teacher.getCourse(interest.getTitle()), interest.getLevels().stream().findFirst().orElseThrow(() -> new NoSuchElementException("No level is specified")), this, teacher))
                .collect(Collectors.toSet());
        return affinities;
    }

    // TODO: check whether this conversion from set to list, and back, is necessary
    public List<Topic> getInterests() {
        return new ArrayList<>(interests);
    }

    public void setInterests(List<Topic> interests) {
        this.interests = new TreeSet<>(interests);
    }

    public void replaceWith(Student student) {
        super.replaceWith(student);
        this.interests = student.interests;
    }
}