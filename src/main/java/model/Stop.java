package model;

import java.util.Objects;

public class Stop {
    private final Point point;
    private final String line;
    private final int id;

    public Stop(Point point, String line, int id) {
        this.point = point;
        this.line = line;
        this.id = id;
    }

    public Stop(Point point, String line) {
        this(point, line, 0);
    }

    public double getLat() {
        return point.getLat();
    }

    public double getLon() {
        return point.getLng();
    }

    public String getLine() {
        return line;
    }

    public Point getPoint() {
        return point;
    }

    public int getId() {
        return id;
    }

    public boolean sameLine(Stop other) {
        return line.equals(other.getLine()) && id == other.id;
    }

    public double distanceTo(Stop other) {
        return point.distanceTo(other.getPoint());
    }

    public double distanceTo(Point p) {
        return point.distanceTo(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;
        Stop stop = (Stop) o;
        return id == stop.id &&
                point.equals(stop.point) &&
                line.equals(stop.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, line, id);
    }

    @Override
    public String toString() {
        return "Stop{" +
                "point=" + point +
                ", line='" + line + '\'' +
                ", id=" + id +
                '}';
    }
}
