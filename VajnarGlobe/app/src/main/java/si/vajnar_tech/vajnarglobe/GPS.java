//http://rkg.gov.si/GERK/WebViewer/
package si.vajnar_tech.vajnarglobe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public abstract class GPS extends View implements LocationListener
{
  private static final String TAG              = "IZAA-GPS";
  private static final double DEF_LONGITUDE    = 13.82535063201905;  //x
  private static final double DEF_LATITUDE     = 46.486136939697225;  //y

  protected double latitude;
  protected double longitude;
  protected MainActivity ctx;
  public boolean gotLocation = false;

  GPS(MainActivity ctx)
  {
    super(ctx);
    this.ctx = ctx;
    latitude = DEF_LATITUDE;
    longitude = DEF_LONGITUDE;
    enableGPSService();
  }

  private void enableGPSService()
  {
    final String[] INITIAL_PERMS = {
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    };
    final int INITIAL_REQUEST = 1337;

    ctx.requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);

    LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

    if (locationManager == null) {
      Log.i(TAG, "Cannot get the LocationManager");
      return;
    } else
      Log.i(TAG, "The LocationManager successfully granted");

    if (ActivityCompat.checkSelfPermission(
        ctx,
        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            ctx,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      (new AlertDialog.Builder(ctx)
           .setTitle("GPS not granted")
           .setMessage("Application will exit")
           .setPositiveButton("OK", new DialogInterface.OnClickListener()
           {
             @Override public void onClick(DialogInterface dialog, int which)
             {
               ctx.finish();
             }
           }).create()).show();
      return;
    }

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    Toast.makeText(ctx, "GPS granted", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onLocationChanged(Location location)
  {
    latitude = location.getLatitude();
    longitude = location.getLongitude();
    gotLocation = true;
    notifyMe(new Vector(longitude, latitude));
    notifyMe(location);
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

  protected abstract void notifyMe(Vector point);

  protected abstract void notifyMe(Location loc);
}