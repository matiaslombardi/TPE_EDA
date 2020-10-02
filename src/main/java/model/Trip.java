package model;

import java.util.List;

public class Trip {
    private final List<Stop> stops;

    public Trip(List<Stop> stops) {
        this.stops = stops;
    }

    public List<Stop> getStops() {
        return stops;
    }
}
