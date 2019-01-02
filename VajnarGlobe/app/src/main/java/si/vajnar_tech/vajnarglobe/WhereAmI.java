package si.vajnar_tech.vajnarglobe;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.Log;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("InfiniteLoopStatement")
@SuppressLint("ViewConstructor")
public class WhereAmI extends GPS
{
  MainActivity act;

  Paint    paint = new Paint();
  Function fv    = new Function();
  Function fs    = new Function();
  D        ds    = new D();
  D        dt    = new D();

  D dv;

  Long erste = null;

  Point currentPosition = null;

  VectorField H = new VectorField()
  {
    @Override
    void done(Vector v)
    {
      if (erste == null)
        erste = System.currentTimeMillis();
      _hector(v);
    }
  };

  WhereAmI(MainActivity ctx)
  {
    super(ctx);
    act = ctx;
  }

  @Override
  protected void notifyMe(Vector point)
  {
    H.add(point);
  }

  private void _hector(Vector point)
  {
    long t = System.currentTimeMillis();
    dv = new D();
    fs.put(t, point);
    ds._up(point);
    dt._up(t);
    dv._is(ds._po(dt));
    fv.put(t, dv);
    currentPosition = point.toPoint();
    ctx.runOnUiThread(new Runnable()
    {
      @Override public void run()
      {
        invalidate();
      }
    });
  }

  @Override protected void notifyMe(Location loc)
  {
  }

  @Override
  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    for (Area a : C.areas.values())
      _drawArea(a, canvas);
  }

  private void _foza()
  {
//    TODO:
//    paint.setColor(Color.parseColor("#CD5C5C"));
//    LongSparseArray<Vector> V = fs.f();
//    for (int i = 0; i < V.size() - 1; i++)
//      canvas.drawLine((float) V.get(V.keyAt(i)).x, (float) V.get(V.keyAt(i)).y,
//                      (float) V.get(V.keyAt(i) + 1).x, (float) V.get(V.keyAt(i) + 1).y, paint);
  }

  //TODO: predict position
  private Line _predictor(Point startPoint)
  {
    Line approx = new Line(startPoint, currentPosition);
    if (approx.f.isInvalid)
      return null;
    return approx;
  }

  private void _drawArea(Area area, Canvas canvas)
  {
    area.draw(canvas, paint, Color.BLACK);
    if (currentPosition == null)
      return;
    currentPosition.draw(canvas, paint, Color.RED, 5, area);
    if (!area.isInside(currentPosition))
      return;
    ArrayList<Point> closestPoints = area.process(currentPosition);
    int              i             = -1;
    for (Point p : closestPoints) {
      i++;
      if (area.get(i).onMe(p))
        p.draw(canvas, paint, Color.GREEN, area);

      Point startPoint = fs.f(erste).toPoint();
      startPoint.draw(canvas, paint, Color.BLUE, area);
      new Line(startPoint, currentPosition).draw(canvas, paint, Color.CYAN, area);

      fs.f(System.currentTimeMillis() + 1000).toPoint().draw(canvas, paint, Color.MAGENTA, 2, area);

//      Line approx = _predictor(startPoint);
//      if (approx == null)
//        continue;
//      approx.draw(canvas, paint, Color.RED, area);
//
//      Point predictor = approx.intersection(area.get(i));
//      if (predictor != null && area.get(i).onMe(predictor))
//        predictor.draw(canvas, paint, Color.MAGENTA, 5, area);
//      else
//        continue;
//
//      Vector ttt = new Vector(predictor.x, predictor.y);
//      Vector ccc = new Vector(currentPosition.x, currentPosition.y);
//      Vector qqq = ttt._minus(ccc);
//      //Log.i("IZAA", "vektor razdalje do=" + qqq);
//      Vector sume = fv.integral();
//      Vector time = new Vector(Math.abs(qqq.x / sume.x), Math.abs(qqq.y / sume.y));
//      //Log.i("IZAA", "vektor casa=" + time);
//      Log.i("IZAAA", String.format("do meje %d bos prisel cez ", i) + (time.x + time.y) + " sekund");
    }
  }
}

@SuppressWarnings("SuspiciousNameCombination")
class Function extends F<Vector>
{
  private F_V<LinearFun> fun = null;

  @Override
  Vector f(long t)
  {
    if (size() < 2)
      return null;
    return new Vector(fun.f1.f(t), fun.f2.f(t));
  }

  @Override Vector f(String s)
  {
    return null;
  }

  @Override Vector integral()
  {
    return null;
  }

  @Override
  public void put(long key, Vector value)
  {
    super.put(key, value);
    if (size() > 2) {
      long k0 = keyAt(0);
      long kn = keyAt(size()-1);
      //Log.i("IZAA", "k0=" + k0);
      //Log.i("IZAA", "kn=" + kn);

      Point p11 = new Point(k0, get(k0).x);
      Point p12 = new Point(kn, get(kn).x);
      Point p21 = new Point(k0, get(k0).y);
      Point p22 = new Point(kn, get(kn).y);
      fun = new F_V<>(new LinearFun(p11, p12), new LinearFun(p21, p22));
    }
  }
}

