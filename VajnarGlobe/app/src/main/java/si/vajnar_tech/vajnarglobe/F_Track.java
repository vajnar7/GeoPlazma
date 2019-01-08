package si.vajnar_tech.vajnarglobe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class F_Track extends MyFragment
{
  WhereAmI gps;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    init();
    return gps;
  }

  @Override
  protected void init()
  {
    gps = new WhereAmI(act);
  }
}