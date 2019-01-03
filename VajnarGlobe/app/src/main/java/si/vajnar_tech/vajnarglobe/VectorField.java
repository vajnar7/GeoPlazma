package si.vajnar_tech.vajnarglobe;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class VectorField extends ArrayList<Vector>
{
  private static final int n = 7;
  private static final AtomicInteger lim = new AtomicInteger(35);

  @Override
  public boolean add(Vector v)
  {
    if (lim.getAndDecrement() > 0)
      return true;
    super.add(v);
    if (size() == n) {
      done(average());
      clear();
    }
    return true;
  }

  private Vector average()
  {
    Vector a = new Vector();
    for (Vector v: this)
      a._plus_je(v);
    a._deljeno_je(new Vector(n, n));
    return (a);
  }

  abstract void done(Vector point);
}
