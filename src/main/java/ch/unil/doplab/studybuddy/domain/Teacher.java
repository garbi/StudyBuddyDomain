package ch.unil.doplab.studybuddy.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Teacher extends User {

    public static int defaultHourlyRate = 50;
    public static int minRating = 0;
    public static int maxRating = 5;
    public static int noRating = -1;

    private int hourlyFee;
    private String description;
    private final SortedSet<LocalDateTime> timeslots;
    private final Map<String,Topic> topics;
    private double ratingAverage;
    private long ratingCount;

    public Teacher() {
        this(null, null, null, null);
    }

    public Teacher(String firstName, String lastName, String email, String username) {
        this(null, firstName, lastName, email, username);
    }
    public Teacher(UUID id, String firstName, String lastName, String email, String username) {
        this(id, firstName, lastName, email, username, defaultHourlyRate);
    }
    public Teacher(UUID id, String firstName, String lastName, String email, String username, int hourlyRate) {
        super(id, firstName, lastName, email, username);
        this.hourlyFee = hourlyRate;
        timeslots = new TreeSet<>();
        topics = new TreeMap<>();
        ratingAverage = noRating;
        ratingCount = 0;
    }

    public String describe() {
        return super.describe() + ", hourlyRate=" + hourlyFee + " CHF/h, rating=" + (ratingAverage >= 0 ? ratingAverage : "none") +
                ", description='" + description + "', timeslots=" + timeslots + "', topics=" + topics;
    }

    public int getHourlyFee() {
        return hourlyFee;
    }

    public void setHourlyFee(int hourlyFee) {
        this.hourlyFee = hourlyFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SortedSet<LocalDateTime> getTimeslots() {
        return timeslots;
    }

    public void addTimeslot(LocalDate date, int hour) {
        timeslots.add(LocalDateTime.of(date, LocalTime.of(hour, 0)));
    }

    public void addTimeslot(LocalDateTime timeslot) {
        timeslots.add(timeslot);
    }

    public void removeTimeslot(LocalDate date, int hour) {
        timeslots.remove(LocalDateTime.of(date, LocalTime.of(hour, 0)));
    }

    public void removeTimeslot(LocalDateTime timeslot) {
        timeslots.remove(timeslot);
    }

    public boolean isAvailable(LocalDateTime timeslot) {
        return timeslots.contains(timeslot);
    }

    public void addTopic(Topic topic) {
        topics.put(topic.getTitle(), topic);
    }

    public void removeTopic(String title) {
        topics.remove(title);
    }

    public Topic getTopic(String title) {
        return topics.get(title);
    }

    public List<Topic> getTopics() {
        return topics.values().stream().toList();
    }

    public boolean teaches(Topic topic) {
        if (!topics.containsKey(topic.getTitle())) {
            return false;
        }
        return topics.get(topic.getTitle()).getLevels().containsAll(topic.getLevels());
    }

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void rate(int rating) {
        if (rating < minRating || rating > maxRating) {
            throw new IllegalArgumentException("rating must be between " + minRating + " and " + maxRating);
        }
        ratingAverage = (ratingAverage * ratingCount + rating) / ++ratingCount;
    }
}
