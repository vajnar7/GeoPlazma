package si.vajnar_tech.vajnarglobe;

public class Vector
{
  protected double x;
  protected double y;

  int s;

  Vector()
  {
    this.x = 0;
    this.y = 0;
    this.s = 0;
  }

  Vector(double x, double y)
  {
    this.x = x;
    this.y = y;
  }

  Point _is()
  {
    return new Point((float)x, (float)y);
  }

  Vector _minus(Vector v)
  {
    return new Vector(x - v.x, y - v.y);
  }

  Vector _po(D dt)
  {
    return new Vector(x/dt.s, y/dt.s);
  }

  @Override
  public String toString()
  {
    return("[" + x + "," + y + "]");
  }
}
