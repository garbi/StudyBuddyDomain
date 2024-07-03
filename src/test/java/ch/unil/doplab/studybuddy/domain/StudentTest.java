package ch.unil.doplab.studybuddy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    private Teacher albert;
    private Student paul;
    private Topic physics;
    private Topic math;

    @BeforeEach
    void setUp() {
        albert = new Teacher(UUID.randomUUID(),
                "Albert",
                "Einstein",
                "einstein@emc2.org",
                "albert");

        physics = new Topic(
                "Physics",
                "The study of matter, energy, and the fundamental forces of nature.",
                EnumSet.allOf(Level.class));

        math = new Topic(
                "Math",
                "The study of numbers, quantity, structure, space, and change.",
                EnumSet.of(Level.INTERMEDIATE, Level.ADVANCED));

        albert.addTopic(physics);
        albert.addTopic(math);

        paul = new Student(UUID.randomUUID(),
                "Paul",
                "Dirac",
                    "paul@quantum.org",
                "paul");
    }

    @Test
    void testMatchingSuccess() {
        paul.addInterest(new Topic("Math", null, Level.INTERMEDIATE));
        var matches = paul.matchInterest(albert);
        assertEquals(1, matches.size());
        assertTrue(matches.contains(math));
        printTopics(albert.getUsername(), albert.getTopics());
        printTopics(paul.getUsername(), paul.getTopics());
        printTopics("matches", matches);
        System.out.println("------");
    }

    @Test
    void testMatchingFailure_MissingTopic() {
        paul.addInterest(new Topic("Biology", null, Level.INTERMEDIATE));
        var matches = paul.matchInterest(albert);
        assertEquals(0, matches.size());
        printTopics(albert.getUsername(), albert.getTopics());
        printTopics(paul.getUsername(), paul.getTopics());
        printTopics("matches", matches);
        System.out.println("------");
    }

    @Test
    void testMatchingFailure_MissingLevel() {
        paul.addInterest(new Topic("Math", null, Level.BEGINNER));
        var matches = paul.matchInterest(albert);
        assertEquals(0, matches.size());
        printTopics(albert.getUsername(), albert.getTopics());
        printTopics(paul.getUsername(), paul.getTopics());
        printTopics("matches", matches);
        System.out.println("------");
    }

    private void printTopics(String header, Collection<Topic> topics) {
        System.out.print(header + " = ");
        if (topics.isEmpty()) {
            System.out.println("[]");
        } else {
            topics.stream().forEach((t) -> {
                System.out.print("[" + t.getTitle() + ", " + t.getLevels() + "] ");
            });
            System.out.println();
        }
    }
}