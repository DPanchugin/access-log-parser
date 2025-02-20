package ru.courses.geometry;

public class MeasureUtils {
    public static void printLengths(Measurable... items) {
        for (Measurable item : items) {
            System.out.println("Length: " + item.getLength());
        }
    }
}
