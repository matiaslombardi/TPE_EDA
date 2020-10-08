package controller;

import model.PlaceFinder;
import model.PlaceLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ControllerTest {

  @Test
  void testPlaceFinder() {
    List<PlaceLocation> places = Arrays.asList();
    PlaceFinder finder = new PlaceFinder(places);
    List<PlaceLocation> similar = finder.findPlaces("");
    Assertions.assertArrayEquals(similar.toArray(), similar.toArray());
    /* TODO */
  }

  @Test
  void testPathFinder() {
    /* TODO */
  }
}