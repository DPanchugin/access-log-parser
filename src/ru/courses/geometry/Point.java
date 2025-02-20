package ru.courses.geometry;

import java.util.Objects;


public class Point implements Cloneable {
     double x;
     double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public String toString() {
        return "ru.courses.geometry.Point{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public Point clone() {
        try {
            return (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
