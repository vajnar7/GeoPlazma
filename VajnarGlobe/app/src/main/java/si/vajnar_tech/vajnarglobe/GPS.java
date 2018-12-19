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

public abstract class GPS extends View implements LocationListener
{
  private static final String TAG              = "IZAA-GPS";
  private static final double DEF_LONGITUDE    = 122;  //x
  private static final double DEF_LATITUDE     = 36;   //y
  private static final int    MINIMUM_TIME     = 10000;// 10s
  private static final int    MINIMUM_DISTANCE = 30;   // 30m

  public    String       area;
  protected double       latitude;
  protected double       longitude;
  protected int          currentTime;
  protected MainActivity ctx;
  public boolean gotLocation = false;

  GPS(MainActivity ctx, String areaName)
  {
    super(ctx);
    this.ctx = ctx;
    latitude = DEF_LATITUDE;
    longitude = DEF_LONGITUDE;
    currentTime = 0;
    area = areaName;
    enableGPSService();
  }

  private void enableGPSService()
  {
    LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

    if (locationManager == null)
      Log.i(TAG, "Cannot get the LocationManager");
    else
      Log.i(TAG, "The LocationManager successfully granted");

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