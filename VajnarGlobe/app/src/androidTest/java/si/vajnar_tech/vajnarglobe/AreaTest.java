package si.vajnar_tech.vajnarglobe;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.runner.lifecycle.Stage.RESUMED;


@RunWith(AndroidJUnit4.class)
public class AreaTest
{
  private MainActivity   act;
  private CountDownLatch signal;

  @Rule
  public ActivityTestRule<MainActivity> actRule = new ActivityTestRule<>(MainActivity.class);

  @BeforeClass
  public static void initClass()
  {
  }

  @AfterClass
  public static void afterClass()
  {
  }

  @Before
  public void setUp()
  {
    act = getActivityInstance();
    _area1();_area2();_area3();
    signal = new CountDownLatch(1);
  }

  @Test
  public void test1() throws InterruptedException
  {
    signal.await(5, TimeUnit.SECONDS);
    startTestGPSService();
    signal.await(5000, TimeUnit.SECONDS);
  }

  @Test
  public void testObtainArea()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(10, 300));
    a.add(new Point(300, 300));
    a.add(new Point(520, 610));
    a.add(new Point(5, 510));
    a.add(new Point(35, 470));
    a.add(new Point(7, 380));
    act.sendLocation(a);
  }

  // TODO: areas naj se nafilajo ob startu preko REST
  private void _area3()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(10, 300));
    a.add(new Point(300, 300));
    a.add(new Point(520, 610));
    a.add(new Point(5, 510));
    a.add(new Point(35, 470));
    a.add(new Point(7, 380));
    act.areas.put("iza3", new Place(a));
  }

  private void _area2()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(300, 10));
    a.add(new Point(550, 45));
    a.add(new Point(600, 250));
    a.add(new Point(300, 300));
    act.areas.put("iza2", new Place(a));
  }

  private void _area1()
  {
    ArrayList<Point> a = new ArrayList<>();
    a.add(new Point(10, 10));
    a.add(new Point(300, 10));
    a.add(new Point(300, 300));
    a.add(new Point(10, 300));
    act.areas.put("iza1", new Place(a));
  }

  private static MainActivity getActivityInstance()
  {
    final MainActivity[] currentActivity = new MainActivity[1];
    getInstrumentation().runOnMainSync(new Runnable()
    {
      public void run()
      {
        Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
        if (resumedActivities.iterator().hasNext()) {
          currentActivity[0] = (MainActivity) resumedActivities.iterator().next();
        }
      }
    });
    return currentActivity[0];
  }

  private void startTestGPSService()
  {
    // test parameters
    final int min  = 5;
    final int max  = 25;
    final int minT = 5;
    final int maxT = 10;
    final Location loc = new Location("");
    new Thread(new Runnable()
    {
      @Override public void run()
      {
        Random r = new Random();
        while (true) {
          int t = r.nextInt(maxT - minT) + minT;
          act.gpsService.currentTime += t;
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