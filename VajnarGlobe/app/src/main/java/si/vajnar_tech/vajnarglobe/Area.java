package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

abstract class Area extends ArrayList<Line>
{
  private String areaName;
  GeoPoint min;
  ArrayList<GeoPoint> pointSet = new ArrayList<>();
  ArrayList<Point> points = new ArrayList<>();

  Area(String name)
  {
    areaName = name;
  }

  Area constructArea()
  {
    _sortPoints();
    for (GeoPoint p : pointSet)
      points.add(new Point(p.lon, p.lat));
    for (int i = 0; i < points.size() - 1; i++)
      add(new Line(points.get(i), points.get(i + 1)));
    add(new Line(points.get(points.size() - 1), points.get(0)));
    return this;
  }

  private void _sortPoints()
  {
    Collections.sort(pointSet, new Comparator<GeoPoint>()
    {
      @Override
      public int compare(GeoPoint o1, GeoPoint o2)
      {
        return o1.timestamp - o2.timestamp;
      }
    });
  }

  public String getName()
  {
    return areaName;
  }

  abstract protected Area mark(GeoPoint a);

  abstract protected ArrayList<Point> process(Point p);

  abstract public void draw(Canvas canvas, Paint paint, int color);

  protected boolean isInside(Point p)
  {
    boolean oddNodes = false;
    int     j        = size() - 1;
    int     i;

    for (i = 0; i < size(); i++) {
      float iy = get(i).p1.y;
      float ix = get(i).p1.x;
      float jy = get(j).p1.y;
      float jx = get(j).p1.x;
      if ((iy < p.y && jy >= p.y || jy < p.y && iy >= p.y) && (ix <= p.x || jx <= p.x))
        if (ix + (p.y - iy) / (jy - iy) * (jx - ix) < p.x)
          oddNodes = !oddNodes;
      j = i;
    }
    return oddNodes;
  }

  Point getClosestPoint(Line l, Point p)
  {
    if (l.f.isHorizontal)
      return new Point(p.x, l.p1.y);
    if (l.f.isVertical)
      return new Point(l.p1.x, p.y);
    double a, b, c;
    a = l.f.a;
    b = l.f.b;
    c = l.f.c;
    double k  = (l.f.a * l.f.a + l.f.b * l.f.b); //a^2 + b^2
    double qx = b * (b * p.x - a * p.y) - a * c;
    double qy = a * (-b * p.x + a * p.y) - b * c;
    return new Point((float) (qx / k), (float) (qy / k));
  }

  protected abstract void save();
}