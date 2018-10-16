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
  Paint paint=new Paint();
  Function_v fv = new Function_v();
  Function_v fs = new Function_v();
  D ds = new D();
  D dt = new D();
  D dv;

  Point currentPosition;

  WhereAmI(MainActivity ctx)
  {
    super(ctx);
    startTestGPSService();
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
    
    ctx.runOnUiThread(new Runnable() {
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
    for (int i = 0; i < V.size() - 1; i++)
    {
      canvas.drawLine((float)V.get(i).x, (float)V.get(i).y,
                      (float)V.get(i+1).x, (float)V.get(i+1).y, paint);
    }

    for (Area a: areas.values())
    {
      if (a.isInside(currentPosition)) {
        _drawArea(a, canvas);
      }
    }
    //_drawArea(areas.get("iza1"), canvas);
    //_drawArea(areas.get("iza2"), canvas);
    //_drawArea(areas.get("iza3"), canvas);
    //currentPosition.draw(canvas, paint);
    //canvas.drawCircle(currentPosition.x, currentPosition.y, 3, paint);
  }

  private void _drawArea(Area are, Canvas canvas)
  {
    for (int i=0; i<are.size(); i++)
    {
      Line l = are.get(i);
      l.draw(canvas, paint);
      //canvas.drawLine(l.p1.x, l.p1.y, l.p2.x, l.p2.y, paint);
    }
    ArrayList<Point> closestPoints = are.role(currentPosition);
    Log.i("IZAA", "***************************************************");
    int i = 0;
    for (Point p: closestPoints) {
      if (i == 1) {
        Point startPoint = fs.getAt(0).toPoint();
        startPoint.draw(canvas, paint);
        currentPosition.draw(canvas, paint);
        Line soda_linija = new Line(startPoint, currentPosition);
        Line cavk_linija = are.get(i);
        soda_linija.draw(canvas, paint);
        Point havdre = soda_linija.intersection(cavk_linija);
        if (havdre != null)
          havdre.draw(canvas, paint);
        else
          continue;
        Vector ttt = new Vector(havdre.x, havdre.y);
        Vector ccc = new Vector(currentPosition.x, currentPosition.y);
        Vector qqq = ttt._minus(ccc);
        Log.i("IZAA", "vektor razdalje do=" + qqq);
        Vector sume = fv.integral();
        Vector time = new Vector(Math.abs(qqq.x/sume.x), Math.abs(qqq.y/sume.y));
        Log.i("IZAA", "vektor casa=" + time);
        Log.i("IZAA", "do vzhodne meje bos prisel cez " + (time.x + time.y) + " sekund");
      }
      i ++;
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
      for (Vector v: values) {
        res._plus_je(v);
      }
      Vector k = new Vector(size(), size());
      res._deljeno_je(k);
      return res;
    }
  }

  private void startTestGPSService()
  {
    final int min = 5;
    final int max = 25;
    final int minT = 5;
    final int maxT = 10;
    new Thread(new Runnable() {
      @Override public void run()
      {
        Random r = new Random();
        while (true) {
          int t = r.nextInt(maxT - minT) + minT;
          currentTime += t;
          notifyMe(new Vector(longitude, latitude), currentTime);
          longitude += r.nextInt(max - min) + min;
          longitude += 10;
          latitude += r.nextInt(max - min) + min;
          try {
            Thread.sleep(t * 1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }
}


