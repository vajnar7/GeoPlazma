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
    float k = f.a - l2.f.a;
    float s = l2.f.c - f.c;

    // second line is vertical
    if (l2.f.isVertical) {
      float yyy = f.a * l2.p1.x + f.c;
      return new Point(l2.p1.x, yyy);
    }
    // second line is horizontal
    if (l2.f.isHorizontal) {
      float xxx = (l2.p1.y - f.c) / f.a;
      return new Point(xxx, l2.p1.y);
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
      f = new Fun("horizontal");
    else if (a != 0 && b == 0)
      f = new Fun("vertical");
    else if (a == 0)
      f = new Fun("invalid");
    else {
      float m = a / b;
      f = new Fun(m, -1, p1.y - (m * p1.x));
    }
  }

  class Fun
  {
    float   a            = 0;
    float   b            = 0;
    float   c            = 0;
    boolean isHorizontal = false;
    boolean isVertical   = false;
    boolean isInvalid    = false;

    Fun(String s)
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
    }

    Fun(float a, float b, float c)
    {
      this.a = a;
      this.b = b;
      this.c = c;
    }
  }

  void draw(Canvas c, Paint p, int color)
  {
    p.setColor(color);
    c.drawLine(p1.x, p1.y, p2.x, p2.y, p);
  }

  @Override
  public String toString()
  {
    return ("[" + p1 + "," + p2 + "]");
  }
}
