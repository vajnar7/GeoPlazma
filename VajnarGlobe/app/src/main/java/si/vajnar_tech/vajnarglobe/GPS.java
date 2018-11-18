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

public abstract class GPS extends View implements LocationListener
{
  private static final String TAG              = "IZAA";
  private static final double DEF_LONGITUDE    = 122;  //x
  private static final double DEF_LATITUDE     = 36;  //y
  private static final int    MINIMUM_TIME     = 10000;// 10s
  private static final int    MINIMUM_DISTANCE = 30;   // 50m
  public  String       area;
  protected double       latitude;
  protected double       longitude;
  protected int       currentTime;
  protected MainActivity ctx;
  public boolean gotLocation = false;

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
    _area3();
  }

  private void _area3()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(10, 300));
    a.add(new Point(300, 300));
    a.add(new Point(520, 610));
    a.add(new Point(5, 510));
    a.add(new Point(35, 470));
    a.add(new Point(7, 380));
    areas.put("iza3", new Place(a));

    if (test)
      ccc(a);
  }

  private void _area2()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(300, 10));
    a.add(new Point(550, 45));
    a.add(new Point(600, 250));
    a.add(new Point(300, 300));
    areas.put("iza2", new Place(a));

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
    areas.put("iza1", new Place(a));

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
      Log.d(TAG, "The LocationManager successfully granted");
    }

    // We have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
    if (ContextCompat.checkSelfPermission(
        ctx,
        android.Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            ctx,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {
      assert locationManager != null;
      locationManager.requestLocationUpdates(
          LocationManager.GPS_PROVIDER, MINIMUM_TIME,
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
  {}

  @Override
  public void onProviderEnabled(String s)
  {}

  @Override
  public void onProviderDisabled(String s)
  {}

  protected abstract void notifyMe(Vector point, int timestamp);
}