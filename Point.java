public class Point {
    int x, y;
    static Point[] moveOptions = new Point[] { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

    Point(int x, int y) {
        this.x = x;
        this.y = y;
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
