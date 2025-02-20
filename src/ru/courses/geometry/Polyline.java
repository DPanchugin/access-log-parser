package ru.courses.geometry;


import java.util.Arrays;

public class Polyline  {
    Point[] points;

    public Polyline(Point... points) {
        this.points = points;
    }


    public double Length() {
        double sum = 0, len1, len2;
        for (int i = 0; i < points.length - 1; i++) {
            len1 = points[i+1].x - points[i].x;
            len2 = points[i+1].y - points[i].y;
            sum += Math.sqrt(len1 * len1 + len2 * len2);
        }
        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Polyline polyLine = (Polyline) obj;
        return Arrays.equals(points, polyLine.points);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(points);
    }
}