package model;

import java.util.Objects;

public class Stop {
    static int EARTH_RADIUS = 6371;
    //private final double lat;
    //private final double lon;
    private final Point point;
    private final String line;
    private final int id;

    public Stop(Point point, String line, int id) {
        this.point = point;
        this.line = line;
        this.id = id;
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
