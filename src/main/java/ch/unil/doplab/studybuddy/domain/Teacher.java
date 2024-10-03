package ch.unil.doplab.studybuddy.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Teacher extends User {

    public static int defaultHourlyRate = 50;
    public static int minRating = Rating.ONE_STAR.getValue();
    public static int maxRating = Rating.FIVE_STARS.getValue();
    public static int noRating =  Rating.NO_RATING.getValue();

    private int hourlyFee;
    private String biography;
    private SortedSet<LocalDateTime> timeslots;
    private Map<String,Topic> topics;
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
                ", description='" + biography + "', timeslots=" + timeslots + "', topics=" + topics;
    }

    public int getHourlyFee() {
        return hourlyFee;
    }

    public void setHourlyFee(int hourlyFee) {
        this.hourlyFee = hourlyFee;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<LocalDateTime> getTimeslots() {
        return new ArrayList<>(timeslots);
    }

    public void setTimeslots(List<LocalDateTime> timeslots) {
        this.timeslots.clear();
        this.timeslots.addAll(timeslots);
    }

    public void addTimeslot(LocalDate date, int hour) {
        timeslots.add(LocalDateTime.of(date, LocalTime.of(hour, 0)));
    }

    public LocalDateTime firstAvailableTimeslot() {
        return timeslots.first();
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

    List<Topic> getTopicList() {
        return topics.values().stream().toList();
    }

    public Map<String,Topic> getTopics() {
        return topics;
    }

    public void setTopics(Map<String,Topic> topics) {
        this.topics = topics;
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

    public void setRatingAverage(double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public void rate(int rating) {
        if (rating < minRating || rating > maxRating) {
            throw new IllegalArgumentException("Rating must be between " + minRating + " and " + maxRating);
        }
        ratingAverage = (ratingAverage * ratingCount + rating) / ++ratingCount;
    }

    public void unrate(int rating) {
        if (rating < minRating || rating > maxRating) {
            throw new IllegalArgumentException("Rating must be between " + minRating + " and " + maxRating);
        }
        if (ratingCount > 1) {
            ratingAverage = (ratingAverage * ratingCount - rating) / --ratingCount;
        } else {
            ratingAverage = noRating;
            ratingCount = 0;
        }
    }

    public Rating rateLesson(LocalDateTime timeslot, Rating rating) {
        var oldRating = super.rateLesson(timeslot, rating);
        if (rating != oldRating) {
            if (oldRating != null && oldRating != Rating.NO_RATING) {
                unrate(oldRating.getValue());
            }
            if (rating != Rating.NO_RATING) {
                rate(rating.getValue());
            }
        }
        return rating;
    }

    public void replaceWith(Teacher teacher) {
        super.replaceWith(teacher);
        this.hourlyFee = teacher.hourlyFee;
        this.biography = teacher.biography;
        this.timeslots = teacher.timeslots;
        this.topics = teacher.topics;
        this.ratingAverage = teacher.ratingAverage;
        this.ratingCount = teacher.ratingCount;
    }

}
