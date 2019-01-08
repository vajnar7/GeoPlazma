package si.vajnar_tech.vajnarglobe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class F_Capture extends MyFragment
{
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    if (act.root != null)
      return act.root;
    return inflater.inflate(R.layout.activity_main, container, false);
  }
}
