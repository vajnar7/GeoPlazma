package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

abstract class Area extends ArrayList<Line>
{
  Area(ArrayList<Point> points)
  {
    for (int i = 0; i < points.size() - 1; i++)
      add(new Line(points.get(i), points.get(i + 1)));
    add(new Line(points.get(points.size() - 1), points.get(0)));
  }

  abstract protected ArrayList<Point> process(Point p);

  abstract public void draw(Canvas canvas, Paint paint);

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

  Point getClosestPoint(Line l, Point p, Boolean isHorizontal)
  {
    if (isHorizontal != null)
      return isHorizontal ? new Point(p.x, l.p1.y) : new Point(l.p1.x, p.y);
    double a, b, c;
    a = l.f.a;
    b = l.f.b;
    c = l.f.c;
    double k  = (l.f.a * l.f.a + l.f.b * l.f.b); //a^2 + b^2
    double qx = b * (b * p.x - a * p.y) - a * c;
    double qy = a * (-b * p.x + a * p.y) - b * c;
    return new Point((float) (qx / k), (float) (qy / k));
  }
}