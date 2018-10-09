package si.vajnar_tech.vajnarglobe;

import android.util.Log;

class D extends Vector
{
  private double q_x;
  private double q_y;
  private int q_s;

  D()
  {
    q_x = 0;
    q_y = 0;
  }

  void _up(int scalar)
  {
    s = scalar - q_s;
    q_s = scalar;
    //Log.i("IZAA", "Bunkatibunka nocinpadan " + s);
    //Log.i("IZAA", "Bunkatibunka nocinpadan " + q_s);
  }

  D _up(Vector v)
  {
    x = v.x - q_x;
    y = v.y - q_y;
    q_x = v.x;
    q_y = v.y;
    return this;
  }

  void _is(Vector v)
  {
    x = v.x;
    y = v.y;
  }

  void _is(int v)
  {
    s = v;
  }
}
