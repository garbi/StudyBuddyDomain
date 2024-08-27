package ch.unil.doplab.studybuddy.domain;

import java.lang.reflect.Constructor;

public class Utils {
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
            return (Exception) constructor.newInstance(description.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
