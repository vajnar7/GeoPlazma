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
    act = _getActivityInstance();
    signal = new CountDownLatch(1);
  }

  @Test
  public void test1() throws InterruptedException
  {
    signal.await(5, TimeUnit.SECONDS);
    _startTestGPSService();
    signal.await(5, TimeUnit.SECONDS);
  }

  @Test
  public void getAllAreas() throws InterruptedException
  {
    signal.await(5, TimeUnit.SECONDS);
    new GetAreas(act.getWindowManager().getDefaultDisplay());
    signal.await(5, TimeUnit.SECONDS);
  }

  @Test
  public void testFillAllAreas()
  {
    testAddArea1();
    testAddArea2();
    testAddArea3();
  }

  @Test
  public void testStartWithArea1()
  {
    new CurrentArea("Iza1")
        .mark(new GeoPoint(10, 10))
        .mark(new GeoPoint(300, 10))
        .mark(new GeoPoint(300, 300))
        .mark(new GeoPoint(10, 300))
        .constructArea();
  }

  @Test
  public void testAddArea1()
  {
    ArrayList<GeoPoint> a = new ArrayList<>();
    a.add(new GeoPoint(10, 10));
    a.add(new GeoPoint(300, 10));
    a.add(new GeoPoint(300, 300));
    a.add(new GeoPoint(10, 300));
    new Place("Iza1", a, act.getWindowManager().getDefaultDisplay()).constructArea().save();
  }

  @Test
  public void testAddArea2()
  {
    ArrayList<GeoPoint> a = new ArrayList<>();
    a.add(new GeoPoint(300, 10));
    a.add(new GeoPoint(550, 45));
    a.add(new GeoPoint(600, 250));
    a.add(new GeoPoint(300, 300));
    new Place("Iza2", a, act.getWindowManager().getDefaultDisplay()).constructArea().save();
  }

  @Test
  public void testAddArea3()
  {
    ArrayList<GeoPoint> a = new ArrayList<>();
    a.add(new GeoPoint(10, 300));
    a.add(new GeoPoint(300, 300));
    a.add(new GeoPoint(520, 610));
    a.add(new GeoPoint(5, 510));
    a.add(new GeoPoint(35, 470));
    a.add(new GeoPoint(7, 380));
    new Place("Iza3", a, act.getWindowManager().getDefaultDisplay()).constructArea().save();
  }

  @Test
  public void hoja() throws InterruptedException
  {
    new GetAreas(act.getWindowManager().getDefaultDisplay());
    signal.await(2, TimeUnit.SECONDS);
    _startTestGPSService();
    signal.await(2000, TimeUnit.SECONDS);
  }

  //**********************************

  private MainActivity _getActivityInstance()
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

  private void _startTestGPSService()
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