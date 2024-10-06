package ch.unil.doplab.studybuddy.domain;

public enum Rating {
    NO_RATING(0),
    ONE_STAR(1),
    TWO_STARS(2),
    THREE_STARS(3),
    FOUR_STARS(4),
    FIVE_STARS(5);

    private int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Rating fromValue(int value) {
        return switch (value) {
            case 0 -> NO_RATING;
            case 1 -> ONE_STAR;
            case 2 -> TWO_STARS;
            case 3 -> THREE_STARS;
            case 4 -> FOUR_STARS;
            case 5 -> FIVE_STARS;
            default -> throw new IllegalArgumentException("Invalid rating value: " + value);
        };
    }

    public String getLabel() {
        return switch (value) {
            case 0 -> "No rating";
            case 1 -> "★";
            case 2 -> "★★";
            case 3 -> "★★★";
            case 4 -> "★★★★";
            case 5 -> "★★★★★";
            default -> throw new IllegalArgumentException("Invalid rating value: " + value);
        };
    }
}