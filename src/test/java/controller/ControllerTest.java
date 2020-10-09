package controller;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ControllerTest {

  static String[] arr = {"BAR DE CERVEZAS", "BAR DE TAPAS", "UNIVERSIDAD DE BELGRANO",
          "UNIVERSIDAD CATOLICA ARGENTINA", "MUSEO NACIONAL DEL ARTE",
          "MUSEO DE LAS ARMAS", "BOQUE"
  };
  @Test
  void testPlaceFinder() {
    List<PlaceLocation> places = new ArrayList<>();
    for (String s : arr) {
      PlaceLocation aux = new PlaceLocation(s, 0, 0);
      places.add(aux);
    }
    PlaceFinder finder = new PlaceFinder(places);
    List<PlaceLocation> similar = finder.findPlaces("BAR");
    String[] toCompare = {"BAR DE TAPAS", "BAR DE CERVEZAS", "BOQUE",
            "MUSEO DE LAS ARMAS", "MUSEO NACIONAL DEL ARTE",
            "UNIVERSIDAD CATOLICA ARGENTINA", "UNIVERSIDAD DE BELGRANO"
    };
    Assertions.assertArrayEquals(similar.stream().map(PlaceLocation::getName).toArray(), toCompare);
    similar = finder.findPlaces("UNIVERSIDAD");
    toCompare = new String[]{"UNIVERSIDAD DE BELGRANO", "UNIVERSIDAD CATOLICA ARGENTINA",
            "BAR DE CERVEZAS", "BAR DE TAPAS", "BOQUE",
            "MUSEO DE LAS ARMAS", "MUSEO NACIONAL DEL ARTE",
    };
    Assertions.assertArrayEquals(similar.stream().map(PlaceLocation::getName).toArray(), toCompare);
  }

  @Test
  void testPathFinder() {
    List<Stop> stops = new ArrayList<>();
    List<Stop> A_stops = new ArrayList<>();
    List<Stop> B_stops = new ArrayList<>();

    A_stops.add(new Stop(new Point(-34.57, -58.47), "A", 0));
    A_stops.add(new Stop(new Point(-34.58, -58.44), "A", 0));
    A_stops.add(new Stop(new Point(-34.6, -58.41), "A", 0));
    B_stops.add(new Stop(new Point(-34.6, -58.41), "B", 0));
    B_stops.add(new Stop(new Point(-34.62, -58.5), "B", 0));

    Trip A = new Trip(A_stops);
    Trip B = new Trip(B_stops);

    stops.addAll(A_stops);
    stops.addAll(B_stops);

    Graph graph = new Graph(stops);
    List<Trip> trips = new ArrayList<>();
    trips.add(A);
    trips.add(B);

    graph.generateEdges(trips, false);

    PathFinder pathFinder = new PathFinder(graph);
    List<BusInPath> l = pathFinder.findPath(-34.57, -58.47,-34.6,-58.41);
    BusInPath[] toCompare = {
            new BusInPath("A",  -34.57, -58.47,-34.6, -58.41, 0)
    };
    Assertions.assertArrayEquals(l.toArray(), toCompare);
    l = pathFinder.findPath(-34.57, -58.47,-34.62,-58.5);
    toCompare = new BusInPath[]{
            new BusInPath("A", -34.57, -58.47, -34.6, -58.41, 0),
            new BusInPath("B", -34.6, -58.41, -34.62, -58.5, 0)
    };
    Assertions.assertArrayEquals(l.toArray(), toCompare);
  }
}