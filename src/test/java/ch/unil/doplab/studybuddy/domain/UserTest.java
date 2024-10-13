package ch.unil.doplab.studybuddy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static ch.unil.doplab.studybuddy.domain.Utils.printMethodName;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User spiderman;

    @BeforeEach
    void setup() {
        spiderman = new User();
    }

    @Test
    void testReplace() {
        var ironman = new User(UUID.randomUUID(), "Tony", "Stark", "iron@starkcorp.com", "ironman", "1234");
        ironman.setBalance(1000);
        ironman.addLanguage("English");
        spiderman.setFirstName("Peter");
        spiderman.setLastName("Parker");
        spiderman.setUsername("spiderman");
        spiderman.setEmail("spidey@web.com");
        spiderman.replaceWith(ironman);
        assertEquals(ironman, spiderman);
    }

    @Test
    void testMerge() {
        printMethodName();
        var uuid = UUID.randomUUID();
        var ironman = new User(uuid, "Tony", "Stark", "iron@starkcorp.com", "ironman", "1234");
        spiderman.setFirstName("Peter");
        spiderman.setUsername("spiderman");
        spiderman.addLanguage("English");
        ironman.mergeWith(spiderman);
        assertEquals(ironman.getUUID(), uuid);
        assertEquals(ironman.getFirstName(), spiderman.getFirstName());
        assertEquals(ironman.getLastName(), "Stark");
        assertEquals(ironman.getUsername(), spiderman.getUsername());
        assertEquals(ironman.getEmail(), "iron@starkcorp.com");
        assertEquals(ironman.getBalance(), spiderman.getBalance());
        assertEquals(ironman.getLanguages(), spiderman.getLanguages());
    }

    @Test
    void testUUID() {
        printMethodName();
        UUID uuid = UUID.randomUUID();
        spiderman.setUUID(uuid);
        assertEquals(uuid, spiderman.getUUID());
    }

    @Test
    void testBalance() {
        printMethodName();
        spiderman.setBalance(100);
        assertEquals(100, spiderman.getBalance());

        spiderman.deposit(50);
        assertEquals(150, spiderman.getBalance());

        spiderman.withdraw(75);
        assertEquals(75, spiderman.getBalance());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            spiderman.withdraw(100);
        });
        String expectedMessage = "Insufficient funds";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFirstName() {
        printMethodName();
        spiderman.setFirstName("Peter");
        assertEquals("Peter", spiderman.getFirstName());
    }

    @Test
    void testLastName() {
        printMethodName();
        spiderman.setLastName("Parker");
        assertEquals("Parker", spiderman.getLastName());
    }

    @Test
    void testUsername() {
        printMethodName();
        spiderman.setUsername("spiderman");
        assertEquals("spiderman", spiderman.getUsername());
    }

    @Test
    void testEmail() {
        printMethodName();
        spiderman.setEmail("spidey@web.com");
        assertEquals("spidey@web.com", spiderman.getEmail());
    }

    @Test
    void testLanguages() {
        printMethodName();
        spiderman.getLanguages();
        assertTrue(spiderman.getLanguages().isEmpty());
        spiderman.addLanguage("English");
        spiderman.addLanguage("French");
        assertFalse(spiderman.getLanguages().isEmpty());
        assertTrue(spiderman.getLanguages().contains("English"));
        assertTrue(spiderman.getLanguages().contains("French"));
    }
}