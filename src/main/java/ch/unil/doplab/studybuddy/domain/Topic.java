package ch.unil.doplab.studybuddy.domain;

import jakarta.persistence.*;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Topic  implements Comparable<Topic> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @ElementCollection(targetClass = Level.class)
    @CollectionTable(name = "LEVEL", joinColumns = @JoinColumn(name = "TOPIC"))
    @Enumerated(EnumType.STRING)
    private Set<Level> levels;

    private String title;
    private String description;

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
        this.levels = convert(levels);
    }

    public void clearLevels() {
        this.levels = Set.of();
    }

    private EnumSet<Level> convert(Set<Level> levels) {
        if (levels == null) {
            return null;
        }
        if (levels.isEmpty()) {
            return EnumSet.noneOf(Level.class);
        }
        return EnumSet.copyOf(levels);
    }

    private Set<Level> convert(EnumSet<Level> levels) {
        if (levels == null) {
            return null;
        }
        return Set.copyOf(levels);
    }

    public Topic clone() {
        var topic = new Topic();
        topic.title = this.title;
        topic.description = this.description;
        topic.levels = this.levels;
        return topic;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return convert(levels);
    }

    public void setLevels(EnumSet<Level> levels) {
        this.levels = convert(levels);
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
