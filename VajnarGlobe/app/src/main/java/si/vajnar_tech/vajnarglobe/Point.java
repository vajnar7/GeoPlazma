package si.vajnar_tech.vajnarglobe;

import android.graphics.Canvas;
import android.graphics.Paint;

class Point
{
  float x, y;

  Point(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString()
  {
    return ("P(" + x + "," + y + ")");
  }

  void draw(Canvas canvas, Paint paint)
  {
    canvas.drawCircle(x, y, 3, paint);
  }
}

