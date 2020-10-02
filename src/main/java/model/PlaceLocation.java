package model;

public class PlaceLocation implements Comparable<PlaceLocation> {

  private final double lat;
  private final double lng;
  private final String name;
  private double sim = Double.MAX_VALUE;

  public PlaceLocation(String name, double lat, double lng) {
    this.name = name;
    this.lat = lat;
    this.lng = lng;
  }

  public double getSim() {
    return sim;
  }

  public void setSim(double sim) {
    this.sim = sim;
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

  @Override
  public int compareTo(PlaceLocation o) {
    int cmp = Double.compare(o.sim, sim);
    if (cmp == 0)
      cmp = name.compareTo(o.name);

    return cmp;
  }
}
