package org.scribe.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import static org.junit.Assert.*;

import org.scribe.BasePrivateConstructorTest;

public class StreamUtilsTest extends BasePrivateConstructorTest
{
    protected Class getClazz(){
        return StreamUtils.class;
    }
  @Test
  public void shouldCorrectlyDecodeAStream()
  {
    String value = "expected";
    InputStream is = new ByteArrayInputStream(value.getBytes());
    String decoded = StreamUtils.getStreamContents(is);
    assertEquals("expected", decoded);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailForNullParameter()
  {
    InputStream is = null;
    StreamUtils.getStreamContents(is);
    fail("Must throw exception before getting here");
  }
}
