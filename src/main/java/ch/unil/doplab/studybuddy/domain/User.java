package ch.unil.doplab.studybuddy.domain;

import java.util.UUID;

public class User {
    private UUID uuid;
    private String firstName;
    private String lastName;

    private String username;
    private String email;

//    private String passwordHash;

    public User() {
        this(null, null, null, null, null);
    }

    public User(String firstName, String lastName, String email, String username) {
        this(null, firstName, lastName, email, username);
    }

    public User(UUID uuid, String firstName, String lastName, String email, String username) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
    }

    public void replace(User user) {
        this.uuid = user.uuid;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.username = user.username;
    }
    public void merge(User user) {
        if (user.uuid != null) {
            this.uuid = user.uuid;
        }
        if (user.firstName != null) {
            this.firstName = user.firstName;
        }
        if (user.lastName != null) {
            this.lastName = user.lastName;
        }
        if (user.email != null) {
            this.email = user.email;
        }
        if (user.username != null) {
            this.username = user.username;
        }
    }
    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String describe() {
        return "id=" + this.uuid + ", firstName='" + this.firstName + "', lastName='" + this.lastName + "'" +
                ", username='" + this.username + "', email='" + this.email + "'";
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{" + this.describe() + "}";
    }
}
