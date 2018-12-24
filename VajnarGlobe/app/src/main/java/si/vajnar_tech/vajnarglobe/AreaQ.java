package si.vajnar_tech.vajnarglobe;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AreaQ
{
  public List<AreaP> areas;

  @Override
  public String toString()
  {
    return "AreaQ{" +
           "areas=" + areas +
           '}';
  }
}

@SuppressWarnings("WeakerAccess")
class AreaP
{
  public String         name;
  public List<GeoPoint> points;

  @Override
  public String toString()
  {
    return "AreaP{" +
           "name=" + name +
           ", points=" + points +
           '}';
  }
}

@SuppressWarnings("WeakerAccess")
class GeoPoint
{
  public int timestamp;
  public float lon;
  public float lat;

  GeoPoint(int timestamp, float lon, float lat)
  {
    this.timestamp = timestamp;
    this.lon = lon;
    this.lat = lat;
  }

  @Override
  public String toString()
  {
    return "GeoPoint{" +
           "timestamp=" + timestamp +
           "lon=" + lon +
           ", lat=" + lat +
           '}';
  }
}
