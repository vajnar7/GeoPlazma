package si.vajnar_tech.vajnarglobe;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

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

  Point currentPosition;

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

  @Override
  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);

    paint.setColor(Color.parseColor("#CD5C5C"));
    ArrayList<Vector> V = fs.f();
    for (int i = 0; i < V.size() - 1; i++) {
      canvas.drawLine((float) V.get(i).x, (float) V.get(i).y,
                      (float) V.get(i + 1).x, (float) V.get(i + 1).y, paint);
    }

    for (Area a : C.areas.values()) {
      _drawArea(a, canvas);
      if (a.isInside(currentPosition)) {
        _drawArea(a, canvas);
      }
    }
  }

  private void _drawArea(Area are, Canvas canvas)
  {
    are.draw(canvas, paint);
    ArrayList<Point> closestPoints = are.process(currentPosition);
    int i = 0;
    for (Point p : closestPoints) {
      p.draw(canvas, paint);
      Point startPoint = fs.getAt(0).toPoint();
      startPoint.draw(canvas, paint);
      currentPosition.draw(canvas, paint);
      Line approx = new Line(startPoint, currentPosition);
      approx.draw(canvas, paint);
      Point predictor = approx.intersection(are.get(i));
      if (predictor != null)
        predictor.draw(canvas, paint);
      else
        continue;
      Vector ttt = new Vector(predictor.x, predictor.y);
      Vector ccc = new Vector(currentPosition.x, currentPosition.y);
      Vector qqq = ttt._minus(ccc);
      //Log.i("IZAA", "vektor razdalje do=" + qqq);
      Vector sume = fv.integral();
      Vector time = new Vector(Math.abs(qqq.x / sume.x), Math.abs(qqq.y / sume.y));
      //Log.i("IZAA", "vektor casa=" + time);
      Log.i("IZAA", String.format("do meje %d bos prisel cez ", i) + (time.x + time.y) + " sekund");
      i++;
      canvas.drawLine(currentPosition.x, currentPosition.y, p.x, p.y, paint);
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


