package ch.unil.doplab.studybuddy.domain;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class User {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private int balance;
    private Set<String> languages;
    private Map<LocalDateTime, Lesson> lessons;

    public User() {
        this(null, null, null, null, null);
    }

    public User(String firstName, String lastName, String email, String username, String password) {
        this(null, firstName, lastName, email, username, password);
    }

    public User(UUID uuid, String firstName, String lastName, String email, String username, String password) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.balance = 0;
        this.languages = new TreeSet<>();
        lessons = new TreeMap<>();
    }

    public void replaceWith(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        this.uuid = user.uuid;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.username = user.username;
        this.password = user.password;
        this.balance = user.balance;
        this.languages = user.languages;
        this.lessons = user.lessons;
    }

    public void mergeWith(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (user.uuid != null) {
            this.uuid = user.uuid;
        }
        if (user.firstName != null) {
            this.firstName = user.firstName;
        }
        if (user.lastName != null) {
            this.lastName = user.lastName;
        }
        if (user.email != null) {
            this.email = user.email;
        }
        if (user.username != null) {
            this.username = user.username;
        }
        if (user.password != null) {
            this.password = user.password;
        }
        if (!user.languages.isEmpty()) {
            this.languages.addAll(user.languages);
        }
        if (!user.lessons.isEmpty()) {
            this.lessons.putAll(user.lessons);
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    public void withdraw(int amount) {
        if (this.balance < amount) {
            throw new IllegalStateException("Insufficient funds: CHF " + (amount - this.balance) + " are missing!");
        }
        this.balance -= amount;
    }

    public String describe() {
        return "id=" + this.uuid + ", firstName='" + this.firstName + "', lastName='" + this.lastName + "'" +
                ", username='" + this.username + "', email='" + this.email + "', balance=" + this.balance + ", languages=" + this.languages
                + ", lessons=" + lessons;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{" + this.describe() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getBalance() == user.getBalance() && Objects.equals(uuid, user.uuid) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getLanguages(), user.getLanguages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, getFirstName(), getLastName(), getUsername(), getEmail(), getBalance(), getLanguages());
    }

    public List<String> getLanguages() {
        return languages.stream().toList();
    }

    public void addLanguage(String language) {
        this.languages.add(language);
    }

    public void removeLanguage(String language) {
        this.languages.remove(language);
    }

    public boolean canCommunicateWith(User other) {
        if (this.languages.isEmpty() || other.languages.isEmpty()) {
            return false;
        }
        for (String language : this.languages) {
            if (other.languages.contains(language)) {
                return true;
            }
        }
        return false;
    }

    public Lesson getLesson(LocalDateTime timeslot) {
        return lessons.get(timeslot);
    }

    public boolean putLesson(LocalDateTime timeslot, Lesson lesson) {
        return lessons.putIfAbsent(timeslot, lesson) != null;
    }

    public boolean removeLesson(Lesson lesson) {
        return lessons.remove(lesson.getTimeslot()) != null;
    }

    public Rating rateLesson(LocalDateTime timeslot, Rating rating) {
        if (timeslot == null) {
            throw new IllegalArgumentException("Timeslot must not be null");
        }
        if (rating == null) {
            throw new IllegalArgumentException("Rating must not be null");
        }
        var lesson = lessons.get(timeslot);
        if (lesson == null) {
            throw new IllegalStateException("No lesson found at " + timeslot);
        }
        var oldRating = lesson.getRating();
        lesson.setRatingUpdate(rating);
        ;
        lesson.updateRating();
        return oldRating;
    }

    public void setLanguages(List<String> languages) {
        this.languages = new TreeSet<>(languages);
    }

    public List<Lesson> getLessons() {
        var sortedLessons = lessons.values().stream()
                .sorted(Comparator.comparing(Lesson::getTimeslot))
                .toList(    );
        return sortedLessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons.stream().collect(Collectors.toMap(Lesson::getTimeslot, lesson -> lesson));
    }
}
