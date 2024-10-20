package ch.unil.doplab.studybuddy.domain;

import java.lang.reflect.Constructor;
import org.mindrot.jbcrypt.BCrypt;

public class Utils {
    public static final String studentRole = "student";
    public static final String teacherRole = "teacher";

    private static boolean testMode = false;

    public static void testModeOn() {
        testMode = true;
    }

    public static void testModeOff() {
        testMode = false;
    }

    public static boolean testMode() {
        return testMode;
    }

    public static void printMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        var methodName = stackTraceElements[2].getMethodName();
        var className = stackTraceElements[2].getClassName();
        className = className.substring(className.lastIndexOf('.') + 1);
        var method = className + "." + methodName;
        printSeparator('-', method.length());
        System.out.println(method);
        printSeparator('-', method.length());
    }

    public static void printSeparator(char c, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(c);
        }
        System.out.println();
    }

    public static Exception buildException(ExceptionDescription description) {
        try {
            Class<?> exceptionClass = Class.forName(description.getType());
            Constructor<?> constructor = exceptionClass.getConstructor(String.class);
            Exception exception = (Exception) constructor.newInstance(description.getMessage());
            return exception;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatTeacherRating(double teacherRating) {
        return teacherRating <= 0 ? "not rated yet" : String.format("%.1f / %d", teacherRating, Teacher.maxRating);
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
