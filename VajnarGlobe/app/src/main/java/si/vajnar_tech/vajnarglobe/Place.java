package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;

import java.util.ArrayList;

public class Place extends Area
{
  Place(String name, ArrayList<GeoPoint> points, Display d)
  {
    super(name);
    pointSet.addAll(_wrapper(points, d));
  }

  Place(String name)
  {
    super(name);
  }

  private ArrayList<GeoPoint> _wrapper(ArrayList<GeoPoint> points, Display display)
  {
    android.graphics.Point size    = new android.graphics.Point();
    display.getSize(size);
    ArrayList<GeoPoint> w = new ArrayList<>();
    for (GeoPoint p: points) {
      double x = p.lon;
      {
        x *= 1000;
        x -= 13825;
        x *= 1000;

        x -= 200;
      }
      double y = p.lat;
      {
        y *= 100;
        y -= 4648;
        y *= 1000;

        y *= 10;
        y -= 5900;

        y = size.y - y;
      }
      Log.i("IZAA", "X=" + x);
      Log.i("IZAA", "Y=" + y);
      Log.i("IZAA", "sizeX=" + size.x);
      Log.i("IZAA", "sizeY=" + size.y);
      w.add(new GeoPoint(x, y));
    }

    return w;
  }

  @Override
  protected Area mark(GeoPoint a)
  {
    pointSet.add(a);
    new SendLocation(getName(), a.timestamp, a.lon, a.lat);
    return this;
  }

  @Override
  protected ArrayList<Point> process(Point p)
  {
    ArrayList<Point> closestPoints = new ArrayList<>();
    for (Line l : this)
      closestPoints.add(getClosestPoint(l, p));
    return closestPoints;
  }

  private double _distance(Point p)
  {
    double d = 0;
    for (Line l : this) {
      if (l.f.isHorizontal)
        d = Math.abs(l.p1.y - p.y);
      else if (l.f.isVertical)
        d = Math.abs(l.p1.x - p.x);
      else
        // |ax0 + by0 + c| / sqr(a^2 + b^2)
        d = Math.abs(l.f.a * p.x + l.f.b * p.y + l.f.c);
      double k = Math.sqrt(l.f.a * l.f.a + l.f.b * l.f.b);
      d /= k;
    }
    Log.i("IZAA", "d(f(x), x0)=" + d);
    return d;
  }

  @Override
  public void draw(Canvas canvas, Paint paint, int color)
  {
    paint.setColor(color);
    for (int i = 0; i < size(); i++) {
      Line l = get(i);
      l.draw(canvas, paint, Color.BLACK);
    }
  }

  @Override
  public void save()
  {
    for (GeoPoint p : pointSet) {
      new SendLocation(getName(), p.timestamp, p.lon, p.lat);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
