package si.vajnar_tech.vajnarglobe;

import android.location.Location;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class C
{
  // operation Mode
  public static String                 mode  = "track";
  // DB of areas
  public static HashMap<String, Area>  areas = new HashMap<>();
  // timestamper
  public static AtomicInteger          stamp = new AtomicInteger(1);
  // screen dimensions
  public static android.graphics.Point size  = new android.graphics.Point();

  public static void startTestGPSService(final MainActivity act)
  {
    // test parameters
    final int      min = 5;
    final int      max = 13;
    final int      minT = 1;
    final int      maxT = 3;
    final Location loc  = new Location("");
    new Thread(new Runnable()
    {
      @Override public void run()
      {
        Random r = new Random();
        while (true) {
          int t = r.nextInt(maxT - minT) + minT;
          int rx = r.nextInt(max - min) + min;
          int ry = r.nextInt(max - min) + min;
          double offX = (double) rx / 1000000.0;
          double offY = -(double) ry / 1000000.0;
          loc.setLatitude(act.gpsService.latitude + offY);
          loc.setLongitude(act.gpsService.longitude + offX);
          act.gpsService.onLocationChanged(loc);
          try {
            Thread.sleep(t * 1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }
        }
      }
    }).start();
  }

  // parameters
  public static class Parameters
  {
    // approximation
    static final int           n       = 1;    // get ~ points to determine current position
    // 35 is this value if min Time and minDist are zero.
    static final AtomicInteger lim     = new AtomicInteger(1);
    static final int           minTime = 100;    // ms
    static final float         minDist = 0.5f; // m
    static final int           ZZ      = 3;    // ~ points back from current
  }
}
