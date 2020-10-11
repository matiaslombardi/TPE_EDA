package model;

import java.util.Objects;

public class BusInPath {

  public final String name;
  public double fromLat;
  public double fromLng;
  public double toLat;
  public double toLng;
  public int id;

  public BusInPath(BusInPath busInPath) {
    this(busInPath.name, busInPath.fromLat, busInPath.fromLng,
            busInPath.toLat, busInPath.toLng, busInPath.id);
  }

  public BusInPath(String name, double fromLat, double fromLng, double toLat, double toLng, int id) {
    this.name = name;
    this.fromLat = fromLat;
    this.fromLng = fromLng;
    this.toLat = toLat;
    this.toLng = toLng;
    this.id = id;
  }

  public boolean samePath(Stop stop) {
    return id == stop.getId() && name.equals(stop.getLine());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BusInPath busInPath = (BusInPath) o;
    return Double.compare(busInPath.fromLat, fromLat) == 0 &&
            Double.compare(busInPath.fromLng, fromLng) == 0 &&
            Double.compare(busInPath.toLat, toLat) == 0 &&
            Double.compare(busInPath.toLng, toLng) == 0 &&
            id == busInPath.id &&
            Objects.equals(name, busInPath.name);
  }


  @Override
  public String toString() {
    return "BusInPath{" +
            "name='" + name + '\'' +
            '}';
  }
}
