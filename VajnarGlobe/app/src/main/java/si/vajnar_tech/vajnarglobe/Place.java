package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

public class Place extends Area
{
  Place(String name, ArrayList<GeoPoint> points)
  {
    super(name);
    pointSet.addAll(_wrapper(points));
  }

  Place(String name)
  {
    super(name);
  }

  private GeoPoint _min(ArrayList<GeoPoint> points)
  {
    GeoPoint res = new GeoPoint(points.get(0).lon, points.get(0).lat);
    for (GeoPoint o : points) {
      if (o.lon < res.lon)
        res.lon = o.lon;
      if (o.lat < res.lat)
        res.lat = o.lat;
    }
    return res;
  }

  private GeoPoint _transform(GeoPoint p, int scale, int xOffset, int yOffset, GeoPoint smuk)
  {
    GeoPoint res = new GeoPoint(p.lon, p.lat);
    res.lon *= scale;
    res.lon -= (xOffset * 1000);
    res.lat *= scale;
    res.lat -= (yOffset * 10000);
    if (smuk != null) {
      res.lon -= smuk.lon;
      res.lat -= smuk.lat;
    }
    return res;
  }

  private ArrayList<GeoPoint> _wrapper(ArrayList<GeoPoint> points)
  {
    ArrayList<GeoPoint> w = new ArrayList<>();
    GeoPoint min = _min(points);
    min = _transform(min, 1000000, 13825, 4648, null);
    for (GeoPoint p: points)
      w.add(_transform(p, 1000000, 13825, 4648, min));
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
