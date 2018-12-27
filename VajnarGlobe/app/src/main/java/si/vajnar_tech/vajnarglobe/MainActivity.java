package si.vajnar_tech.vajnarglobe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
  private final String TAG = "IZAA-MAIN";

  WhereAmI gpsService;
  Arduni   arduni;
  CurrentArea currentArea = null;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    // gpsService = new WhereAmI(this);
    arduni = new Arduni(this);

    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    @SuppressLint("InflateParams")
    LinearLayout root = (LinearLayout) inflater.inflate(R.layout.activity_main, null);

    setContentView(root);

    findViewById(R.id.send).setOnClickListener(this);
    findViewById(R.id.construct).setOnClickListener(this);
    currentArea = new CurrentArea("Test1");

    arduni = new Arduni(this);

    printLocation();
  }

  public void printLocation()
  {
    TextView latitudeT  = findViewById(R.id.latitude);
    TextView longitudeT = findViewById(R.id.longitude);
    String   lonS       = "Longitude:";
    String   latS       = "Latitude";
    latitudeT.setText(latS);
    longitudeT.setText(lonS);
    if (arduni.gotLocation) {
      latS += arduni.location.getLatitude();
      lonS += arduni.location.getLongitude();
      latitudeT.setText(latS);
      longitudeT.setText(lonS);
    }
  }

  @Override
  public void onClick(View v)
  {
    switch (v.getId()) {
    case R.id.send:
      if (arduni.gotLocation) {
        currentArea.mark(new GeoPoint(arduni.location.getLongitude(), arduni.location.getLatitude()));
        Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
      }
      break;
    case R.id.construct:
      if (currentArea != null)
        currentArea.constructArea();
    }
  }
}
