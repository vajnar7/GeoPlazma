package si.vajnar_tech.vajnarglobe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity
{
  CurrentArea currentArea     = null;
  MyFragment  currentFragment = null;

  @SuppressLint("InflateParams")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    setContentView(inflater.inflate(R.layout.content_main, null));
    Display display = getWindowManager().getDefaultDisplay();
    display.getSize(C.size);
    setFragment("main", FragmentMain.class, new Bundle());
  }

  public void setFragment(String tag, Class<? extends MyFragment> cls, Bundle params)
  {
    currentFragment = createFragment(tag, cls, params);
    if (currentFragment == null) return;

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, currentFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  public MyFragment createFragment(String tag, Class<? extends MyFragment> cls, Bundle params)
  {
    MyFragment frag;
    frag = (MyFragment) getSupportFragmentManager().findFragmentByTag(tag);
    if (frag == null && cls != null)
      try {
        frag = MyFragment.instantiate(cls, this);
        frag.setArguments(params);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    return frag;
  }

  public MyFragment getCurrentFragment()
  {
    return currentFragment;
  }
}
