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
  D dv = new D();
  D dt = new D();

  Point currentPosition;

  WhereAmI(MainActivity ctx)
  {
    super(ctx);
    startTestGPSService();
  }

  @Override
  protected void notifyMe(Vector point, int t)
  {
    fs.add(t, point);
    ds._up(point);
    dt._up(t);
    dv._is(ds._po(dt));
    currentPosition = point._is();
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
    canvas.drawCircle(currentPosition.x, currentPosition.y, 3, paint);
  }

  private void _drawArea(Area are, Canvas canvas)
  {
    for (int i=0; i<are.size(); i++)
    {
      Line l = are.get(i);
      canvas.drawLine(l.p1.x, l.p1.y, l.p2.x, l.p2.y, paint);
    }
    are.role(currentPosition);
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

    @Override Vector integral(int t1, int t2)
    {
      return null;
    }
  }

  private void startTestGPSService()
  {
    final int min = 5;
    final int max = 23;
    new Thread(new Runnable() {
      @Override public void run()
      {
        Random r = new Random();
        while (true) {
          int t = r.nextInt(max - min) + min;
          currentTime += t;
          notifyMe(new Vector(longitude, latitude), currentTime);
          longitude += r.nextInt(max - min) + min;
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


