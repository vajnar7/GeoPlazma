package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

public class Place extends Area
{
  Place (String name, ArrayList<GeoPoint> points)
  {
    super(name);
    pointSet.addAll(points);
  }

  Place(String name)
  {
    super(name);
  }

  @Override
  protected Area mark(GeoPoint a)
  {
    pointSet.add(a);
    return this;
  }

  @Override
  protected ArrayList<Point> process(Point p)
  {
    ArrayList<Point> closestsPoints = new ArrayList<>();
    for (Line l : this) {
      double d;

      if (l.f.isHorizontal != null) {
        d = l.f.isHorizontal ? (double) Math.abs(l.p1.y - p.y) : (double) Math.abs(l.p1.x - p.x);
        closestsPoints.add(getClosestPoint(l, p, l.f.isHorizontal));
      } else {
        // |ax0 + by0 + c| / sqr(a^2 + b^2)
        d = Math.abs(l.f.a * p.x + l.f.b * p.y + l.f.c);
        double k = Math.sqrt(l.f.a * l.f.a + l.f.b * l.f.b);
        d /= k;
        closestsPoints.add(getClosestPoint(l, p, null));
      }
      Log.i("IZAA", "d(f(x), x0)=" + d);
    }
    return closestsPoints;
  }

  @Override
  public void draw(Canvas canvas, Paint paint)
  {
    for (int i = 0; i < size(); i++) {
      Line l = get(i);
      l.draw(canvas, paint);
    }
  }

  @Override
  public Area save()
  {
    for (GeoPoint p : pointSet)
      new SendLocation(getName(), p.timestamp, (double) p.lon, (double) p.lat);
    return this;
  }
}
