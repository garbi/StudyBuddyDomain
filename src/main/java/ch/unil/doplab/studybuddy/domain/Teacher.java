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
    private Map<String,Topic> courses;
    private double ratingAverage;
    private long ratingCount;

    public Teacher() {
        this(null, null, null, null, null, null, null);
    }

    public Teacher(String firstName, String lastName, String email, String username, String password, String biography) {
        this(null, firstName, lastName, email, username, password, biography);
    }

    public Teacher(String firstName, String lastName, String email, String username, String password) {
        this(firstName, lastName, email, username, password, null);
    }


    public Teacher(String firstName, String lastName, String email, String username, String password, String biography, int hourlyRate) {
        this(null, firstName, lastName, email, username, password, biography, hourlyRate);
    }

    public Teacher(UUID id, String firstName, String lastName, String email, String username, String password) {
        this(id, firstName, lastName, email, username, password, null);
    }

    public Teacher(UUID id, String firstName, String lastName, String email, String username, String password, String biography) {
        this(id, firstName, lastName, email, username, password, biography, defaultHourlyRate);
    }

    public Teacher(UUID id, String firstName, String lastName, String email, String username, String password, String biography, int hourlyRate) {
        super(id, firstName, lastName, email, username, password);
        this.biography = biography;
        this.hourlyFee = hourlyRate;
        timeslots = new TreeSet<>();
        courses = new TreeMap<>();
        ratingAverage = noRating;
        ratingCount = 0;
    }

    public String describe() {
        return super.describe() + ", hourlyRate=" + hourlyFee + " CHF/h, rating=" + (ratingAverage >= 0 ? ratingAverage : "none") +
                ", description='" + biography + "', timeslots=" + timeslots + "', courses=" + courses;
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

    public void addCourse(Topic course) {
        courses.put(course.getTitle(), course);
    }

    public void removeCourse(String title) {
        courses.remove(title);
    }

    public Topic getCourse(String title) {
        return courses.get(title);
    }

    public List<Topic> getCourseList() {
        return courses.values().stream().toList();
    }

    public Map<String,Topic> getCourses() {
        return courses;
    }

    public void setCourses(Map<String,Topic> courses) {
        this.courses = courses;
    }

    public boolean teaches(Topic course) {
        if (!courses.containsKey(course.getTitle())) {
            return false;
        }
        return courses.get(course.getTitle()).getLevels().containsAll(course.getLevels());
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
        this.courses = teacher.courses;
        this.ratingAverage = teacher.ratingAverage;
        this.ratingCount = teacher.ratingCount;
    }

}
