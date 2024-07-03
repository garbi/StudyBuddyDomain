package ch.unil.doplab.studybuddy.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Student extends User {

    Set<Topic> topics;

    public Student() {
        super();
        topics = new HashSet<>();
    }

    public Student(String firstName, String lastName, String email, String username) {
        this(null, firstName, lastName, email, username);
    }

    public Student(UUID id, String firstName, String lastName, String email, String username) {
        super(id, firstName, lastName, email, username);
        topics = new HashSet<>();
    }

    public String describe() {
        return super.describe() + ", interests=" + topics;
    }

    public void addInterest(Topic topic) {
        topics.add(topic);
    }

    public void removeInterest(Topic topic) {
        topics.remove(topic);
    }

    public Set<Topic> matchInterest(Teacher teacher) {
        var matches = topics.stream()
                .filter(topic -> teacher.getTopics().contains(topic) &&
                        (!teacher.getTopic(topic.getTitle()).getLevels().stream()
                                .filter(topic.getLevels()::contains)
                                .collect(Collectors.toSet()).isEmpty()))
                .collect(Collectors.toSet());
        return matches;
    }

    public List<Topic> getTopics() {
        return new ArrayList<>(topics);
    }
}