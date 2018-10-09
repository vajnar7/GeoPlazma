package si.vajnar_tech.vajnarglobe;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

abstract class F
{
  ArrayList<Vector> values;

  abstract Vector f(int t);
  abstract ArrayList<Vector> f();
  abstract Vector integral(int t1, int t2);

  F()
  {
    values = new ArrayList<>();
  }

  void add(int t, Vector v)
  {
    values.add(v);
  }

  int size()
  {
    return values.size();
  }

  Vector get(int t)
  {
    return values.get(t);
  }

  Vector getAt(int i)
  {
    return values.get(i);
  }

  protected ArrayList<Vector> get()
  {
    return values;
  }
}
