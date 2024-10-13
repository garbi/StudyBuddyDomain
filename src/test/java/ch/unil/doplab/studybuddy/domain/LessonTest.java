package ch.unil.doplab.studybuddy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

import static ch.unil.doplab.studybuddy.domain.Utils.printMethodName;
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
        LocalDateTime timeslot;
        random = new Random();
        physics = new Topic(
                "Physics",
                "The study of matter, energy, and the fundamental forces of nature.",
                EnumSet.allOf(Level.class));

        math = new Topic(
                "Math",
                "The study of numbers, quantity, structure, space, and change.",
                EnumSet.of(Level.Intermediate, Level.Advanced));

        theology = new Topic(
                "Theology",
                "The study of the nature of the divine.",
                EnumSet.of(Level.Beginner, Level.Intermediate));

        albert = new Teacher(UUID.randomUUID(),
                "Albert",
                "Einstein",
                "einstein@emc2.org",
                "albert",
                "1234");
        albert.addLanguage("German");
        albert.addLanguage("English");
        albert.setBiography("I am a theoretical physicist working at the Swiss Patent Office in Bern.");
        timeslot = LocalDateTime.now().plusDays(1).plusHours(1).withMinute(0).withSecond(0).withNano(0);
        albert.addTimeslot(timeslot);
        timeslot = LocalDateTime.now().plusDays(1).plusHours(2).withMinute(0).withSecond(0).withNano(0);
        albert.addTimeslot(timeslot);
        timeslot = LocalDateTime.now().plusDays(1).plusHours(3).withMinute(0).withSecond(0).withNano(0);
        albert.addTimeslot(timeslot);
        albert.addCourse(physics);
        albert.addCourse(math);

        martin = new Teacher(UUID.randomUUID(),
                "Martin",
                "Luther",
                "luther@king.com",
                "martin",
                "1234");
        martin.addCourse(theology);
        martin.addLanguage("German");
        martin.setBiography("I am a German professor of theology and a seminal figure in the Protestant Reformation.");
        timeslot = LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0);
        martin.addTimeslot(timeslot);

        paul = new Student(UUID.randomUUID(),
                "Paul",
                "Dirac",
                "dirac@quantum.org",
                "paul",
                "1234");
        paul.addLanguage("French");
        paul.addLanguage("English");

        jean = new Student(UUID.randomUUID(),
                "Jean",
                "Calvin",
                "calvin@geneva.org",
                "jean",
                "1234");
        jean.addLanguage("French");
    }

    @Test
    void testBookingSuccess() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Advanced;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        lesson.book(albert, paul);
        assertEquals(lesson.getTeacherID(), albert.getUUID());
        assertEquals(lesson.getStudentID(), paul.getUUID());
        assertEquals(topic.getTitle(), lesson.getAffinity().getTitle());
        assertTrue(lesson.getAffinity().getLevels().contains(level));
        assertEquals(1, lesson.getAffinity().getLevels().size());
        assertSame(timeslot, lesson.getTimeslot());

        assertFalse(albert.getTimeslots().contains(timeslot));
        assertNotSame(topic, lesson.getAffinity());
        assertNotNull(albert.getLesson(timeslot));
        assertNotNull(paul.getLesson(timeslot));
        assertSame(lesson, albert.getLesson(timeslot));
        assertSame(lesson, paul.getLesson(timeslot));
        assertFalse(albert.getTimeslots().contains(timeslot));
    }

    @Test
    void testBookingFailure_WrongTopic() {
        printMethodName();
        var topic = theology;
        var level = Level.Advanced;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "does not teach " + theology.getTitle();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_WrongLevel() {
        printMethodName();
        var topic = math;
        var level = Level.Beginner;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "does not teach " + math.getTitle();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_NoCommunication() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Intermediate;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        albert.removeLanguage("English");
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "cannot communicate";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_InsufficientFunds() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Intermediate;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "has insufficient funds";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_StudentAlreadyBooked() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Intermediate;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(2 * albert.getHourlyFee());
        lesson.book(albert, paul);
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "already has a lesson at this time";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_TeacherNotAvailable() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Intermediate;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        albert.removeTimeslot(timeslot.toLocalDate(), timeslot.getHour());
        paul.deposit(albert.getHourlyFee());
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert,paul));
        String expectedMessage = "has no availability at this time";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_NullTimeslotOrTopicOrLevel() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Intermediate;
        var timeslot = albert.firstAvailableTimeslot();

        var lesson1 = new Lesson(null, topic, level);
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson1.book(albert, paul));
        String expectedMessage = "Timeslot, topic or level cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);

        var lesson2 = new Lesson(timeslot, null, level);
        exception = assertThrows(IllegalStateException.class, () -> lesson2.book(albert, paul));
        expectedMessage = "Timeslot, topic or level cannot be null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);

        var lesson3 = new Lesson(timeslot, topic, null);
        exception = assertThrows(IllegalStateException.class, () -> lesson3.book(albert, paul));
        expectedMessage = "Timeslot, topic or level cannot be null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_NoLevel() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        Level level = Level.Intermediate;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        lesson.getAffinity().getLevels().clear();
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "Timeslot, topic or level cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testBookingFailure_TimeslotInThePast() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Advanced;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        lesson.setTimeslot(LocalDateTime.now().minusDays(1));
        paul.deposit(albert.getHourlyFee());
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.book(albert, paul));
        String expectedMessage = "Lessons must be booked at least one day in advance";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    /*
     * This method is used at the beginning of all cancel tests to first book a lesson successfully
     */
    private Lesson bookLessonSuccessfully() {
        printMethodName();
        var topics = albert.getCourseList();
        var topic = topics.get(random.nextInt(topics.size()));
        var level = Level.Advanced;
        var timeslot = albert.firstAvailableTimeslot();
        var lesson = new Lesson(timeslot, topic, level);
        paul.deposit(albert.getHourlyFee());
        lesson.book(albert, paul);
        return lesson;
    }

    @Test
    void testCancelSuccess() {
        printMethodName();
        var lesson = bookLessonSuccessfully();
        lesson.cancel(albert, paul);
        assertNull(albert.getLesson(lesson.getTimeslot()));
        assertNull(paul.getLesson(lesson.getTimeslot()));
        assertTrue(albert.getTimeslots().contains(lesson.getTimeslot()));
    }

    @Test
    void testCancelFailure_NullTeacherOrStudent() {
        printMethodName();
        var lesson = bookLessonSuccessfully();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> lesson.cancel(null, paul));
        String expectedMessage = "Teacher or student cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);

        exception = assertThrows(IllegalArgumentException.class, () -> lesson.cancel(albert, null));
        expectedMessage = "Teacher or student cannot be null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }

    @Test
    void testCancelFailure_WrongTeacher() {
        printMethodName();
        var lesson = bookLessonSuccessfully();
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.cancel(martin, paul));
        String expectedMessage = "Lesson is not with " + martin.getUsername();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }
    @Test
    void testCancelFailure_WrongStudent() {
        printMethodName();
        var lesson = bookLessonSuccessfully();
        Exception exception = assertThrows(IllegalStateException.class, () -> lesson.cancel(albert, jean));
        String expectedMessage = "Lesson is not with " + jean.getUsername();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(actualMessage);
    }
}