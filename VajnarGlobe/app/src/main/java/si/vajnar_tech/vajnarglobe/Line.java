package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Paint;

class Line
{
  Point p1;
  private Point p2;
  Fun f = null;

  Line(Point p1, Point p2)
  {
    this.p1 = p1;
    this.p2 = p2;
    _defineF();
  }

  Point intersection(Line l2)
  {
    double k = f.a - l2.f.a;
    double s = l2.f.c - f.c;

    // second line is vertical
    if (l2.f.isVertical)
      return new Point(l2.p1.x, f.a * l2.p1.x + f.c);
    // second line is horizontal
    if (l2.f.isHorizontal)
      return new Point((l2.p1.y - f.c) / f.a, l2.p1.y);

    if (k == 0 || s == 0)
      return null;

    double x = s / k;
    double y = f.a * x + f.c;
    return new Point(x, y);
  }

  boolean onMe(Point p)
  {
    Point a = p1;
    Point b = p2;
    // horizontalna
    if (f.isHorizontal) {
      if (p1.x > p2.x) {
        a = p2;
        b = p1;
      }
      if (p.x > b.x || p.x < a.x)
        return false;
    }
    // vertical
    if (f.isVertical) {
      if (p1.y > p2.y) {
        a = p2;
        b = p1;
      }
      if (p.y > b.y || p.y < a.y)
        return false;
    }
    if (a.x > b.x) {
      a = p2;
      b = p1;
    }
    if (f.a > 0)
      return (!(p.x > b.x) || !(p.y > b.y)) && (!(p.x < a.x) || !(p.y < a.y));
    else
      return (!(p.x > b.x) || !(p.y < b.y)) && (!(p.x < a.x) || !(p.y > a.y));
  }

  private void _defineF()
  {
    double a = p2.y - p1.y; // if 0 horizontal
    double b = p2.x - p1.x; // if 0 vertical
    double m;
    if (a == 0 && b != 0) {
      m = p2.x > p1.x ? 1 : -1;
      f = new Fun("horizontal", m);
    } else if (a != 0 && b == 0) {
      m = p2.y > p1.y ? 1 : -1;
      f = new Fun("vertical", m);
    } else if (a == 0)
      f = new Fun("invalid", 0);
    else {
      m = a / b;
      f = new Fun(m, -1, p1.y - (m * p1.x));
    }
  }

  class Fun
  {
    double a;
    double b             = 0;
    double c             = 0;
    boolean isHorizontal = false;
    boolean isVertical   = false;
    boolean isInvalid    = false;

    Fun(String s, double m)
    {
      switch (s) {
      case "vertical":
        isVertical = true;
        break;
      case "horizontal":
        isHorizontal = true;
        break;
      case "invalid":
        isInvalid = true;
        break;
      }
      a = m;
    }

    Fun(double a, double b, double c)
    {
      this.a = a;
      this.b = b;
      this.c = c;
    }
  }

  void draw(Canvas c, Paint p, int color, Area a)
  {
    // render
    p.setColor(color);
    Point o1 = a.transform(new Point(p1.x, p1.y), true);
    Point o2 = a.transform(new Point(p2.x, p2.y), true);
    c.drawLine((float) o1.x, (float) o1.y, (float) o2.x, (float) o2.y, p);
  }

  @Override
  public String toString()
  {
    return ("[" + p1 + "," + p2 + "]");
  }
}
