package ch.unil.doplab.studybuddy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

import static ch.unil.doplab.studybuddy.domain.Utils.printMethodName;
import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    private Teacher albert;
    private Teacher martin;

    @BeforeEach
    void setUp() {
        albert = new Teacher(UUID.randomUUID(),
                "Albert",
                "Einstein",
                "einstein@emc2.org",
                "albert",
                "1234");

        martin = new Teacher(UUID.randomUUID(),
                "Martin",
                "Luther",
                "luther@king.com",
                "martin",
                "1234");
    }

    @Test
    void testRatingSuccess() {
        printMethodName();
        System.out.println("albert is not rated yet (" + albert.getRatingAverage() + ")");
        assertEquals(Teacher.noRating, albert.getRatingAverage());
        albert.rate(Teacher.maxRating);
        assertEquals(Teacher.maxRating, albert.getRatingAverage());
        System.out.println("albert is rated " + albert.getRatingAverage());

        System.out.println("martin is not rated yet (" + martin.getRatingAverage() + ")");
        assertEquals(Teacher.noRating, martin.getRatingAverage());
        martin.rate(Teacher.minRating);
        assertEquals(Teacher.minRating, martin.getRatingAverage());
        System.out.println("martin is rated " + martin.getRatingAverage());

        var previousRating = albert.getRatingAverage();

        albert.rate(Teacher.minRating);
        assertEquals((Teacher.minRating + Teacher.maxRating) / 2.0, albert.getRatingAverage());
        System.out.println("albert is rated " + albert.getRatingAverage());

        albert.unrate(Teacher.minRating);
        assertEquals(previousRating, albert.getRatingAverage());
        System.out.println("albert is rated " + albert.getRatingAverage());

        previousRating = martin.getRatingAverage();

        martin.rate(3);
        assertEquals((Teacher.minRating + 3) / 2.0, martin.getRatingAverage());
        System.out.println("martin is rated " + martin.getRatingAverage());

        martin.unrate(3);
        assertEquals(previousRating, martin.getRatingAverage());
        System.out.println("martin is rated " + martin.getRatingAverage());
    }

    @Test
    void testRatingFailure() {
        printMethodName();
        System.out.println("albert is not rated yet (" + albert.getRatingAverage() + ")");
        assertEquals(Teacher.noRating, albert.getRatingAverage());

        String expectedMessage = "Rating must be between " + Teacher.minRating + " and " + Teacher.maxRating;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> albert.rate(Teacher.maxRating + 1));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(Teacher.noRating, albert.getRatingAverage());

        exception = assertThrows(IllegalArgumentException.class, () -> albert.rate(Teacher.minRating - 1));
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(Teacher.noRating, albert.getRatingAverage());

        exception = assertThrows(IllegalArgumentException.class, () -> albert.unrate(Teacher.maxRating + 1));
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(Teacher.noRating, albert.getRatingAverage());

        exception = assertThrows(IllegalArgumentException.class, () -> albert.unrate(Teacher.minRating - 1));
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(Teacher.noRating, albert.getRatingAverage());
    }
}