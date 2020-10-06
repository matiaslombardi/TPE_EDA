package model;

import java.util.Objects;

public class Point {
    private final double lat;
    private final double lng;

    private static final int EARTH_RADIUS = 6371;

    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double distanceTo(Point other) {
        double dLat = other.lat-lat;
        double dLon = other.lng-lng;
        return Math.sqrt(Math.pow(dLat, 2) + Math.pow(dLon,2));
        /*
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(other.lng)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIUS * c * 1000;
         */
    }

    @Override
    public String toString() {
        return "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Double.compare(point.lat, lat) == 0 &&
                Double.compare(point.lng, lng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }
}
