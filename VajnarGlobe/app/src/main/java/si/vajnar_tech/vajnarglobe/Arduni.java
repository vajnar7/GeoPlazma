package si.vajnar_tech.vajnarglobe;

import android.annotation.SuppressLint;
import android.location.Location;

@SuppressLint("ViewConstructor")
public class Arduni extends GPS
{
  Location location;
  MainActivity act;

  Arduni(MainActivity ctx)
  {
    super(ctx);
    act = ctx;
  }

  @Override protected void notifyMe(Vector point)
  {
  }

  @Override
  protected void notifyMe(Location loc)
  {
    location = loc;
    act.printLocation();
  }
}
