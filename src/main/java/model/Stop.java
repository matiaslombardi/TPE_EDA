package model;

public class Stop {
    static int EARTH_RADIUS = 6371;
    private final double lat;
    private final double lon;
    private final String line;

    public Stop(double lat, double lon, String line) {
        this.lat = lat;
        this.lon = lon;
        this.line = line;
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

}
