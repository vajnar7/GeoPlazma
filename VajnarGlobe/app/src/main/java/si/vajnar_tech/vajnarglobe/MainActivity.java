package si.vajnar_tech.vajnarglobe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
  HashMap<String, Area> areas = new HashMap<>();
  WhereAmI gpsService;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    gpsService = new WhereAmI(this, "Pupika");
    setContentView(gpsService);
  }

  @Override
  public void onClick(View v)
  {
    switch (v.getId()) {
    case R.id.send:
      break;
    }
  }

  public void sendLocation (ArrayList<Point> a)
  {
    for (Point p: a)
      new SendLocation(this, (double)p.x, (double)p.y);
  }
}
