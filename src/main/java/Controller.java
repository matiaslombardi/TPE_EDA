import model.BusInPath;
import model.PlaceFinder;
import model.PlaceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class Controller {
  private PlaceFinder placeFinder;
  public Controller() throws IOException {
    placeFinder = new PlaceFinder();
  }

  public List<BusInPath> findPath(double fromLat, double fromLng, double toLat, double toLng) {
    return Arrays.asList(new BusInPath("No implementado", 0, 0, 0, 0));
  }

  public List<PlaceLocation> findPlaces(String searchTerm) {
    return placeFinder.findPlaces(searchTerm);
  }

}