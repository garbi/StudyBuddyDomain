package ch.unil.doplab.studybuddy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static ch.unil.doplab.studybuddy.domain.Utils.*;

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
                "albert",
                "1234");

        physics = new Topic(
                "Physics",
                "The study of matter, energy, and the fundamental forces of nature.",
                EnumSet.allOf(Level.class));

        math = new Topic(
                "Math",
                "The study of numbers, quantity, structure, space, and change.",
                EnumSet.of(Level.Intermediate, Level.Advanced));

        albert.addCourse(physics);
        albert.addCourse(math);
        albert.addLanguage("English");

        paul = new Student(UUID.randomUUID(),
                "Paul",
                "Dirac",
                    "paul@quantum.org",
                "paul",
                "1234");
    }

    @Test
    void testMatchingSuccess() {
        printMethodName();
        paul.addInterest(new Topic("Math", null, Level.Intermediate));
        paul.addLanguage("English");
        var matches = paul.findAffinitiesWith(albert);
        assertEquals(1, matches.size());
        assertTrue(matches.contains(math));
        printTopics(albert.getUsername(), albert.getCourseList());
        printTopics(paul.getUsername(), paul.getInterests());
        printTopics("matches", matches);
    }

    @Test
    void testMatchingFailure_NoTopics() {
        printMethodName();
        paul.addLanguage("English");
        var matches = paul.findAffinitiesWith(albert);
        assertEquals(0, matches.size());
        printTopics(albert.getUsername(), albert.getCourseList());
        printTopics(paul.getUsername(), paul.getInterests());
        printTopics("matches", matches);
    }

    @Test
    void testMatchingFailure_NoCommunication() {
        printMethodName();
        paul.addInterest(new Topic("Math", null, Level.Intermediate));
        var matches = paul.findAffinitiesWith(albert);
        assertEquals(0, matches.size());
        printTopics(albert.getUsername(), albert.getCourseList());
        printTopics(paul.getUsername(), paul.getInterests());
        printTopics("matches", matches);
    }


        @Test
    void testMatchingFailure_MissingTopic() {
        printMethodName();
        paul.addInterest(new Topic("Biology", null, Level.Intermediate));
        var matches = paul.findAffinitiesWith(albert);
        assertEquals(0, matches.size());
        printTopics(albert.getUsername(), albert.getCourseList());
        printTopics(paul.getUsername(), paul.getInterests());
        printTopics("matches", matches);
    }

    @Test
    void testMatchingFailure_MissingLevel() {
        printMethodName();
        paul.addInterest(new Topic("Math", null, Level.Beginner));
        var matches = paul.findAffinitiesWith(albert);
        assertEquals(0, matches.size());
        printTopics(albert.getUsername(), albert.getCourseList());
        printTopics(paul.getUsername(), paul.getInterests());
        printTopics("matches", matches);
    }

    private void printTopics(String header, Collection<? extends Topic> topics) {
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