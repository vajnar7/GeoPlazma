package si.vajnar_tech.vajnarglobe;

import android.util.Log;

import java.util.ArrayList;

import static si.vajnar_tech.vajnarglobe.Login.GET_ALL;

public class GetAreas extends REST<AreaQ>
{
  GetAreas()
  {
    super(GET_ALL);
  }

  @Override
  public AreaQ backgroundFunc()
  {
    return callServer(null, REST.OUTPUT_TYPE_JSON);
  }

  @Override
  protected void onPostExecute(AreaQ j)
  {
    super.onPostExecute(j);
    if (j != null) {
      for (AreaP t : j.areas) {
        C.areas.put(t.name, new Place(t.name, (ArrayList<GeoPoint>) t.points).constructArea());
      }
    }
  }
}