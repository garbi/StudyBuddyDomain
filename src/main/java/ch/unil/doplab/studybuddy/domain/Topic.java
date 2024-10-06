package ch.unil.doplab.studybuddy.domain;

import java.util.EnumSet;
import java.util.Objects;

public class Topic  implements Comparable<Topic> {

    private String title;
    private String description;
    private EnumSet<Level> levels;

    public Topic() {
        this(null, null, EnumSet.noneOf(Level.class));
    }

    public Topic(String title, String description, Level level) {
        this(title, description, EnumSet.of(level));
    }

    public Topic(String title, Level level) {
        this(title, null, level);
    }

    public Topic(String title, String description, EnumSet<Level> levels) {
        this.title = title;
        this.description = description;
        this.levels = levels;
    }

    public Topic clone() {
        return new Topic(this.title, this.description, this.levels);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EnumSet<Level> getLevels() {
        return levels;
    }

    public void setLevels(EnumSet<Level> levels) {
        this.levels = levels;
    }

    public String listLevels() {
        var levels = this.levels.toString();
        if (levels.length() > 2) {
            levels = levels.substring(1, levels.length() - 1);
        } else {
            levels = "";
        }
        return levels;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", levels=" + levels +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic topic)) return false;
        return Objects.equals(getTitle(), topic.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTitle());
    }

    @Override
    public int compareTo(Topic topic) {
        return this.title.compareTo(topic.getTitle());
    }
}
