package ch.unil.doplab.studybuddy.domain;

import java.util.UUID;

public class Student extends User {
    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String email, String username) {
        this(null, firstName, lastName, email, username);
    }

    public Student(UUID id, String firstName, String lastName, String email, String username) {
        super(id, firstName, lastName, email, username);
    }

    public void replace(Student student) {
        super.replace(student);
    }
    public void merge(Student student) {
        super.merge(student);
    }
    public String describe() {
        return super.describe();
    }
}