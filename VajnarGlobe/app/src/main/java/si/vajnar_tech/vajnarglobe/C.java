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
    final int      minT = 1;
    final int      maxT = 3;
    final double   offX = 0.000015;
    final double   offY = -0.000013;
    final Location loc  = new Location("");
    new Thread(new Runnable()
    {
      @Override public void run()
      {
        Random r = new Random();
        while (true) {
          int t = r.nextInt(maxT - minT) + minT;
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
    static final int           n       = 3;    // get 3 points to determine current position
    static final AtomicInteger lim     = new AtomicInteger(35);
    static final int           minTime = 0;    // ms
    static final float         minDist = 0.0f; // m
    static final int           ZZ      = 3;    // 3 points back from current
  }
}
