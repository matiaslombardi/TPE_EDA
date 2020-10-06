package model;

public class BusInPath {

  public final String name;
  public double fromLat;
  public double fromLng;
  public double toLat;
  public double toLng;

  public BusInPath(BusInPath busInPath) {
    this(busInPath.name, busInPath.fromLat, busInPath.fromLng, busInPath.toLat, busInPath.toLng);
  }

  public BusInPath(String name, double fromLat, double fromLng, double toLat, double toLng) {
    this.name = name;
    this.fromLat = fromLat;
    this.fromLng = fromLng;
    this.toLat = toLat;
    this.toLng = toLng;
  }

  @Override
  public String toString() {
    return "BusInPath{" +
            "name='" + name + '\'' +
            '}';
  }
}
