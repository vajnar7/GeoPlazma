package si.vajnar_tech.vajnarglobe;

import android.location.Location;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class C
{
  // operation Mode
  public static String mode = "track";
  // DB of areas
  public static HashMap<String, Area> areas = new HashMap<>();
  // timestamper
  public static AtomicInteger         stamp = new AtomicInteger(1);
  // screen dimensions
  public static android.graphics.Point size = new android.graphics.Point();

  public static void startTestGPSService(final MainActivity act)
  {
    // test parameters
    final int      min  = 5;
    final int      max  = 25;
    final int      minT = 5;
    final int      maxT = 10;
    final Location loc  = new Location("");
    new Thread(new Runnable()
    {
      @Override public void run()
      {
        Random r = new Random();
        while (true) {
          int t = r.nextInt(maxT - minT) + minT;
          loc.setLatitude(act.gpsService.latitude + r.nextInt(max - min) + min);
          loc.setLongitude(act.gpsService.longitude + r.nextInt(max - min) + min);
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
}
