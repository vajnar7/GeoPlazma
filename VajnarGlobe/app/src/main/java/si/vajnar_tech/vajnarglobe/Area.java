package si.vajnar_tech.vajnarglobe;

import android.util.Log;

import java.util.ArrayList;

abstract class Area extends ArrayList<Line>
{
  Area(ArrayList<Point> points)
  {
    for (int i=0; i<points.size()-1; i++)
    {
      add(new Line(points.get(i), points.get(i+1)));
    }
    add(new Line(points.get(points.size()-1), points.get(0)));
  }

  abstract protected void role(Point p);
}

class Point
{
  float x, y;

  Point(float x, float y)
  {
    this.x = x;
    this.y = y;
  }
}

class Line
{
  Point p1, p2;
  Fun f;

  Line(Point p1, Point p2)
  {
    this.p1 = p1;
    this.p2 = p2;

    float m = _getM();
  }

  private float _getM()
  {
    float a = p2.y - p1.y; // if 0 horizontal
    float b = p2.x - p1.x; // if 0 vertical
    if (a == 0 && b != 0) {
      f = new Fun(true);
      return 0;
    }
    else if (a != 0 && b == 0) {
      f = new Fun(false);
      return 0;
    }
    else if (a == 0) {
      Log.i("IZAA", "NaN");
      return 0;
    }
    float m = a/b;
    new Fun(m, -1, p1.y-(m*p1.x));
    return(m);
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

    Fun (boolean isH)
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
}
