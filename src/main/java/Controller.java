import model.*;
import readers.PathReader;
import readers.PlacesReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class Controller {
  private final PlaceFinder placeFinder;
  private final PathFinder pathFinder;


  public Controller() {
    PlacesReader placesReader = new PlacesReader();
    List<PlaceLocation> places = placesReader.readPlaces();
    if (places == null) System.exit(1);
    placeFinder = new PlaceFinder(places);

    PathReader pathReader = new PathReader();
    Graph graph = pathReader.readPaths();
    pathFinder = new PathFinder(graph);

    System.out.println("Finished importing data");
  }

  public List<BusInPath> findPath(double fromLat, double fromLng, double toLat, double toLng) {
    return pathFinder.findPath(fromLat, fromLng, toLat, toLng);
  }

  public List<PlaceLocation> findPlaces(String searchTerm) {
    return placeFinder.findPlaces(searchTerm);
  }

}