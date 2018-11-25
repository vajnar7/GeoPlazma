package si.vajnar_tech.vajnarglobe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
  HashMap<String, Area> areas = new HashMap<>();
  WhereAmI gpsService;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    gpsService = new WhereAmI(this);
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
}
