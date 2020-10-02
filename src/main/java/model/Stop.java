package model;

import java.util.Objects;

public class Stop {
    static int EARTH_RADIUS = 6371;
    private final double lat;
    private final double lon;
    private final String line;
    private final int id;

    public Stop(double lat, double lon, String line, int id) {
        this.lat = lat;
        this.lon = lon;
        this.line = line;
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getLine() {
        return line;
    }

    public double distanceTo(double lat2, double lon2) {
        double dLat = Math.toRadians(lat2-lat);  // deg2rad below
        double dLon = Math.toRadians(lon2-lon);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIUS * c * 1000;
    }

    public double distanceTo(Stop other) {
        return distanceTo(other.getLat(), other.getLon());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;
        Stop stop = (Stop) o;
        return Double.compare(stop.lat, lat) == 0 &&
                Double.compare(stop.lon, lon) == 0 &&
                id == stop.id &&
                line.equals(stop.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, line, id);
    }
}
