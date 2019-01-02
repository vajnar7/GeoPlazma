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
          loc.setLatitude(act.gpsService.latitude - 0.000013);
          loc.setLongitude(act.gpsService.longitude + 0.0000005);
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
