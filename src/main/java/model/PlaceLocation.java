package model;

public class PlaceLocation {

  private final double lat;
  private final double lng;
  private final String name;

  public PlaceLocation(String name, double lat, double lng) {
    this.name = name;
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "PlaceLocation{" +
            "lat=" + lat +
            ", lng=" + lng +
            ", name='" + name + '\'' +
            '}';
  }
}
