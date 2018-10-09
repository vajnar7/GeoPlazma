//http://rkg.gov.si/GERK/WebViewer/
package si.vajnar_tech.vajnarglobe;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class GPS extends View implements LocationListener
{
  private static final String TAG              = "IZAA";
  private static final double DEF_LONGITUDE    = 300.0;
  private static final double DEF_LATITUDE     = 10.0;
  private static final int    MINIMUM_TIME     = 10000;// 10s
  private static final int    MINIMUM_DISTANCE = 30;   // 50m
  public  String       area;
  protected double       latitude;
  protected double       longitude;
  protected int       currentTime;
  protected MainActivity ctx;
  private boolean        gotLocation = false;
  private List<GeoPoint> points      = new ArrayList<>();

  HashMap<String, Area> areas = new HashMap<>();
  static boolean test = false;

  GPS(MainActivity ctx)
  {
    super(ctx);
    this.ctx = ctx;
    latitude = DEF_LATITUDE;
    longitude = DEF_LONGITUDE;
    currentTime = 0;
    enableGPSService();

    _area1();
    _area2();
  }

  private void _area2()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(300, 10));
    a.add(new Point(550, 45));
    a.add(new Point(600, 250));
    a.add(new Point(300, 300));
    areas.put("iza2", new AreaBase(a));

    if (test)
      ccc(a);
  }

  private void _area1()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(10, 10));
    a.add(new Point(300, 10));
    a.add(new Point(300, 300));
    a.add(new Point(10, 300));
    areas.put("iza1", new AreaBase(a));

    if (test)
      ccc(a);
  }

  private void ccc(ArrayList<Point> a)
  {
    for (Point p: a)
      new SendLocation(ctx, (double)p.x, (double)p.y);
  }

  private void enableGPSService()
  {

    LocationManager locationManager = (LocationManager) ctx.getSystemService(
        Context.LOCATION_SERVICE);

    if (locationManager == null) {
      Log.d(TAG, "Cannot get the LocationManager");
    } else {
      Log.d(TAG, "The LocationManager succesfuly granted");
    }

    // We have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
    if (ContextCompat.checkSelfPermission(ctx,
                                          android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(ctx,
                                             android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

      assert locationManager != null;
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME,
                                             MINIMUM_DISTANCE, this);
    }
  }

  @Override
  public void onLocationChanged(Location location)
  {
    latitude = location.getLatitude();
    longitude = location.getLongitude();
    gotLocation = true;
    Toast.makeText(ctx, "GPS UPDATED", Toast.LENGTH_SHORT).show();
    notifyMe(new Vector(longitude, latitude), currentTime);
  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle)
  { }

  @Override
  public void onProviderEnabled(String s)
  {

  }

  @Override
  public void onProviderDisabled(String s)
  {

  }

  protected abstract void notifyMe(Vector point, int timestamp);

  public double getLatitude()
  {
    return latitude;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public boolean isGotLocation()
  {
    return gotLocation;
  }

  class GeoPoint
  {
    Double lon;
    Double lat;

    GeoPoint(double lon, double lat)
    {
      this.lon = lon;
      this.lat = lat;
    }
  }
}

class AreaBase extends Area
{

  AreaBase(ArrayList<Point> points)
  {
    super(points);
  }

  @Override
  protected void role(Point p)
  {
    for (Line l : this)
    {
      double d;

      if (l.f.isHorizontal != null)
        d = l.f.isHorizontal ? (double)Math.abs(l.p1.x-p.x) : (double)Math.abs(l.p1.y-p.y);
      else {
        // |ax0 + by0 + c| / sqr(a^2 + b^2)
        d = Math.abs(l.f.a * p.x + l.f.b * p.y + l.f.c);
        double k = Math.sqrt(l.f.a * l.f.a + l.f.b * l.f.b);
        Log.i("IZAA", "...d=" + d);
        Log.i("IZAA", "...k=" + k);
        d /= k;
      }
      Log.i("IZAA", "d(f(x), x0)=" + d);
    }
  }
}
