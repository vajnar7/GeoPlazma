package si.vajnar_tech.vajnarglobe;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

@SuppressWarnings("InfiniteLoopStatement")
@SuppressLint("ViewConstructor")
public class WhereAmI extends GPS
{
  MainActivity act;
  Paint      paint = new Paint();
  Function_v fv    = new Function_v();
  Function_v fs    = new Function_v();
  D          ds    = new D();
  D          dt    = new D();
  D dv;

  Point currentPosition = new Point(0, 0);

  WhereAmI(MainActivity ctx)
  {
    super(ctx);
    act = ctx;
  }

  @Override
  protected void notifyMe(Vector point, int t)
  {
    dv = new D();
    fs.add(t, point); // := f(t)
    ds._up(point);
    dt._up(t);
    dv._is(ds._po(dt));
    fv.add(t, dv);
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

    paint.setColor(Color.parseColor("#CD5C5C"));
    ArrayList<Vector> V = fs.f();
    for (int i = 0; i < V.size() - 1; i++)
      canvas.drawLine((float) V.get(i).x, (float) V.get(i).y,
                      (float) V.get(i + 1).x, (float) V.get(i + 1).y, paint);

    for (Area a : C.areas.values())
      _drawArea(a, canvas);
  }

  private void _drawArea(Area area, Canvas canvas)
  {
    area.draw(canvas, paint, Color.BLACK);
    currentPosition.draw(canvas, paint, Color.BLUE);
    if (!area.isInside(currentPosition))
      return;
    ArrayList<Point> closestPoints = area.process(currentPosition);
    int              i             = -1;
    for (Point p : closestPoints) {
      i++;
      if (area.get(i).onMe(p))
        p.draw(canvas, paint, Color.GREEN);
      Point startPoint = fs.getAt(0).toPoint();
      startPoint.draw(canvas, paint, Color.YELLOW);

      Line approx = new Line(startPoint, currentPosition);
      if (approx.f.isInvalid)
        continue;
      approx.draw(canvas, paint, Color.RED);


      Point predictor = approx.intersection(area.get(i));
      if (predictor != null && area.get(i).onMe(predictor))
        predictor.draw(canvas, paint, Color.MAGENTA, 5);
      else
        continue;

      Vector ttt = new Vector(predictor.x, predictor.y);
      Vector ccc = new Vector(currentPosition.x, currentPosition.y);
      Vector qqq = ttt._minus(ccc);
      //Log.i("IZAA", "vektor razdalje do=" + qqq);
      Vector sume = fv.integral();
      Vector time = new Vector(Math.abs(qqq.x / sume.x), Math.abs(qqq.y / sume.y));
      //Log.i("IZAA", "vektor casa=" + time);
      Log.i("IZAAA", String.format("do meje %d bos prisel cez ", i) + (time.x + time.y) + " sekund");
    }
  }

  class Function_v extends F
  {
    @Override
    Vector f(int t)
    {
      return get(t);
    }

    @Override ArrayList<Vector> f()
    {
      return (get());
    }

    @Override Vector integral()
    {
      Vector res = new Vector();
      for (Vector v : values) {
        res._plus_je(v);
      }
      Vector k = new Vector(size(), size());
      res._deljeno_je(k);
      return res;
    }
  }
}


