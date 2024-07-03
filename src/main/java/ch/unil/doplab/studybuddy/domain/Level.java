package ch.unil.doplab.studybuddy.domain;

public enum Level {
    BEGINNER, INTERMEDIATE, ADVANCED;

    public String capitalize() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
