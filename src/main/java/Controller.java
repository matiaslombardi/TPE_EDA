import model.BusInPath;
import model.PathFinder;
import model.PlaceFinder;
import model.PlaceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class Controller {
  private final PlaceFinder placeFinder;
  private final PathFinder pathFinder;


  public Controller() throws IOException {
    placeFinder = new PlaceFinder();
    pathFinder = new PathFinder();
    System.out.println("Finished importing data");
  }

  public List<BusInPath> findPath(double fromLat, double fromLng, double toLat, double toLng) {
    return pathFinder.findPath(fromLat, fromLng, toLat, toLng);
  }

  public List<PlaceLocation> findPlaces(String searchTerm) {
    return placeFinder.findPlaces(searchTerm);
  }

}