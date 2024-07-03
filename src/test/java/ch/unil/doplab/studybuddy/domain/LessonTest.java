package ch.unil.doplab.studybuddy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LessonTest {

    private Teacher albert;
    private Teacher martin;
    private Student paul;
    private Student jean;
    private Topic physics;
    private Topic math;
    private Topic theology;
    private Random random;

    @BeforeEach
    void setUp() {
        random = new Random();
        physics = new Topic(
                "Physics",
                "The study of matter, energy, and the fundamental forces of nature.",
                EnumSet.allOf(Level.class));

        math = new Topic(
                "Math",
                "The study of numbers, quantity, structure, space, and change.",
                EnumSet.of(Level.INTERMEDIATE, Level.ADVANCED));

        theology = new Topic(
                "Theology",
                "The study of the nature of the divine.",
                EnumSet.of(Level.BEGINNER, Level.INTERMEDIATE));

        albert = new Teacher(UUID.randomUUID(),
                "Albert",
                "Einstein",
                "einstein@emc2.org",
                "albert");
        albert.addLanguage("German");
        albert.addLanguage("English");
        albert.setDescription("I am a theoretical physicist working at the Swiss Patent Office in Bern.");
        albert.addTimeslot(LocalDate.now(), LocalTime.now().plusHours(1).getHour());
        albert.addTimeslot(LocalDate.now(), LocalTime.now().plusHours(2).getHour());
        albert.addTimeslot(LocalDate.now().plusDays(1), LocalTime.now().getHour());
        albert.addTopic(physics);
        albert.addTopic(math);

        martin = new Teacher(UUID.randomUUID(),
                "Martin",
                "Luther",
                "luther@king.com",
                "martin");
        martin.addTopic(theology);
        martin.addLanguage("German");
        martin.setDescription("I am a German professor of theology and a seminal figure in the Protestant Reformation.");
        martin.addTimeslot(LocalDate.now(), LocalTime.now().plusHours(1).getHour());

        paul = new Student(UUID.randomUUID(),
                "Paul",
                "Dirac",
                "dirac@quantum.org",
                "paul");
        paul.addLanguage("French");
        paul.addLanguage("English");

        jean = new Student(UUID.randomUUID(),
                "Jean",
                "Calvin",
                "calvin@geneva.org",
                "jean");
        jean.addLanguage("French");
    }

    @Test
    void testBookingSuccess() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.ADVANCED;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        lesson.book(albert, paul);
        assertEquals(lesson.getTeacherID(), albert.getUUID());
        assertEquals(lesson.getStudentID(), paul.getUUID());
        assertEquals(topic.getTitle(), lesson.getTopic().getTitle());
        assertTrue(lesson.getTopic().getLevels().contains(level));
        assertEquals(1, lesson.getTopic().getLevels().size());
        assertSame(timeslot, lesson.getTimeslot());

        assertFalse(albert.getTimeslots().contains(timeslot));
        assertNotSame(topic, lesson.getTopic());
        assertNotNull(albert.getLesson(timeslot));
        assertNotNull(paul.getLesson(timeslot));
        assertSame(lesson, albert.getLesson(timeslot));
        assertSame(lesson, paul.getLesson(timeslot));
        assertFalse(albert.getTimeslots().contains(timeslot));
    }

    @Test
    void testBookingFailure_WrongTopic() {
        var topic = theology;
        var level = Level.ADVANCED;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "does not teach " + theology.getTitle();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_WrongLevel() {
        var topic = math;
        var level = Level.BEGINNER;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "does not teach " + math.getTitle();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_NoCommunication() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.INTERMEDIATE;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        albert.removeLanguage("English");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "cannot communicate";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_InsufficientFunds() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.INTERMEDIATE;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "has insufficient funds";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_StudentAlreadyBooked() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.INTERMEDIATE;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(2 * albert.getHourlyFee());
        lesson.book(albert, paul);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "already has a lesson at this time";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_TeacherNotAvailable() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.INTERMEDIATE;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        albert.removeTimeslot(timeslot.toLocalDate(), timeslot.getHour());
        paul.deposit(albert.getHourlyFee());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.book(albert,paul));
        String expectedMessage = "has no availability at this time";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_NullTimeslotOrTopicOrLevel() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.INTERMEDIATE;
        var timeslot = albert.getTimeslots().first();
        var lesson1 = new Lesson(null, topic, level);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson1.book(albert, paul));
        String expectedMessage = "timeslot, topic or level cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);

        var lesson2 = new Lesson(timeslot, null, level);
        exception = assertThrows(IllegalArgumentException.class, () -> lesson2.book(albert, paul));
        expectedMessage = "timeslot, topic or level cannot be null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);

        var lesson3 = new Lesson(timeslot, topic, null);
        exception = assertThrows(IllegalArgumentException.class, () -> lesson3.book(albert, paul));
        expectedMessage = "timeslot, topic or level cannot be null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_NoLevel() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        Level level = Level.INTERMEDIATE;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        lesson.getTopic().getLevels().clear();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "there must be exactly one level in the lesson";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    /*
     * This method is used at the beginning of all cancel tests to first book a lesson successfully
     */
    private Lesson bookLessonSuccessfully() {
        var topics = albert.getTopics();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.ADVANCED;
        var timeslot = albert.getTimeslots().first();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        lesson.book(albert, paul);
        return lesson;
    }

    @Test
    void testCancelSuccess() {
        var lesson = bookLessonSuccessfully();
        lesson.cancel(albert, paul);
        assertNull(albert.getLesson(lesson.getTimeslot()));
        assertNull(paul.getLesson(lesson.getTimeslot()));
        assertTrue(albert.getTimeslots().contains(lesson.getTimeslot()));
    }

    @Test
    void testCancelFailure_NullTeacherOrStudent() {
        var lesson = bookLessonSuccessfully();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.cancel(null, paul));
        String expectedMessage = "teacher or student cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);

        exception = assertThrows(IllegalArgumentException.class, () -> lesson.cancel(albert, null));
        expectedMessage = "teacher or student cannot be null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testCancelFailure_WrongTeacher() {
        var lesson = bookLessonSuccessfully();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.cancel(martin, paul));
        String expectedMessage = "lesson is not with " + martin.getUsername();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }
    @Test
    void testCancelFailure_WrongStudent() {
        var lesson = bookLessonSuccessfully();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.cancel(albert, jean));
        String expectedMessage = "lesson is not with " + jean.getUsername();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }
}