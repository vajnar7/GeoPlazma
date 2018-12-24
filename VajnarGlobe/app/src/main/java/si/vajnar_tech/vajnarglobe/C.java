package si.vajnar_tech.vajnarglobe;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class C
{
  // DB of areas
  public static HashMap<String, Area> areas = new HashMap<>();
  // timestamper
  public static AtomicInteger stamp = new AtomicInteger(1);
}
