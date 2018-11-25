package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Paint;

class Line
{
  Point p1;
  private Point p2;
  Fun f;

  Line(Point p1, Point p2)
  {
    this.p1 = p1;
    this.p2 = p2;
    _defineF();
  }

  Point intersection(Line l2)
  {
    if (f == null)
      return null;
    float k = f.a - l2.f.a;
    float s = l2.f.c - f.c;

    // linija je vertikalna ali horizontalna
    if (l2.f.isHorizontal != null) {
      // second line is vertical
      if (!l2.f.isHorizontal) {
        float yyy = f.a * l2.p1.x + f.c;
        return new Point(l2.p1.x, yyy);
      }
    }
    if (k == 0 || s == 0)
      return null;
    float x = s / k;
    float y = f.a * x + f.c;
    return new Point(x, y);
  }

  private void _defineF()
  {
    float a = p2.y - p1.y; // if 0 horizontal
    float b = p2.x - p1.x; // if 0 vertical
    if (a == 0 && b != 0)
      f = new Fun(true);
    else if (a != 0 && b == 0)
      f = new Fun(false);
    float m = a / b;
    f = new Fun(m, -1, p1.y - (m * p1.x));
  }

  float f(float x)
  {
    float m = (p2.y - p1.y) / (p2.x - p1.x);
    return m * (x - p1.x) + p1.y;
  }

  class Fun
  {
    float a, b, c;
    Boolean isHorizontal;

    Fun(boolean isH)
    {
      isHorizontal = isH;
      a = b = c = 0;
    }

    Fun(float a, float b, float c)
    {
      isHorizontal = null;
      this.a = a;
      this.b = b;
      this.c = c;
    }
  }

  void draw(Canvas c, Paint p)
  {
    c.drawLine(p1.x, p1.y, p2.x, p2.y, p);
  }

  @Override
  public String toString()
  {
    return ("[" + p1 + "," + p2 + "]");
  }
}
