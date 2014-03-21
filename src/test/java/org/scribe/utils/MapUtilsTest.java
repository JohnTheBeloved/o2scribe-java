package org.scribe.utils;

import java.util.*;
import org.junit.*;

import org.scribe.BasePrivateConstructorTest;

/**
 * @author: Pablo Fernandez
 */
public class MapUtilsTest extends BasePrivateConstructorTest
{
    protected Class getClazz(){
        return MapUtils.class;
    }
  @Test
  public void shouldPrettyPrintMap()
  {
    Map<Integer, String> map = new HashMap<>();
    map.put(1, "one");
    map.put(2, "two");
    map.put(3, "three");
    map.put(4, "four");
    Assert.assertEquals("{ 1 -> one , 2 -> two , 3 -> three , 4 -> four }", MapUtils.toString(map));
  }

  @Test
  public void shouldHandleEmptyMap()
  {
    Map<Integer, String> map = new HashMap<>();
    Assert.assertEquals("{}", MapUtils.toString(map));
  }

  @Test
  public void shouldHandleNullInputs()
  {
    Assert.assertEquals("", MapUtils.toString(null));
  }
}
