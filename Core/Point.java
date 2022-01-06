package Core;

import java.util.*;

public class Point {
    public int x, y;
    public static Point[] moveOptions = new Point[] { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point[] shuffledOptions() {
        Random random = new Random();
        Point[] arr = moveOptions;
        for (int i = arr.length - 1; i > 0; i--) {
            int idx = random.nextInt(i + 1);
            Point tmp = arr[idx];
            arr[idx] = arr[i];
            arr[i] = tmp;
        }

        return arr;
    }

    public String toString() {
        return String.format("[%s, %s]", x, y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof Point))
            return false;

        Point otherPoint = (Point) other;
        return otherPoint.x == x && otherPoint.y == y;
    }

    // generates good hash for [100x100]
    public int hashCode() {
        return 100 * this.y + this.x;
    }
}
