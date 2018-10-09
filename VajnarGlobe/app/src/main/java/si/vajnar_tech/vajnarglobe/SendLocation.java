package si.vajnar_tech.vajnarglobe;

import static si.vajnar_tech.vajnarglobe.Login.AREAS;

public class SendLocation extends REST<LocationObj>
{
  private Double latitude;
  private Double longitude;

  SendLocation(MainActivity ctx, Double longitude, Double latitude)
  {
    super(String.format(AREAS, ctx.gpsService.area));
    this.longitude = longitude;
    this.latitude = latitude;
  }

  @Override
  public LocationObj backgroundFunc()
  {
    return callServer(new LocationData(String.valueOf(longitude), String.valueOf(latitude)), OUTPUT_TYPE_JSON);
  }

  @Override
  protected void onPostExecute(LocationObj j)
  {
    super.onPostExecute(j);
  }
}

@SuppressWarnings("WeakerAccess")
class LocationData
{
  String lon;
  String lat;

  LocationData(String longitude, String latitude)
  {
    this.lon = longitude;
    this.lat = latitude;
  }
}

@SuppressWarnings("WeakerAccess")
class LocationObj
{
  String return_code;

  @Override
  public String toString()
  {
    return "LocationObj{" +
           "return_code='" + return_code + '\'' +
           "}";
  }
}