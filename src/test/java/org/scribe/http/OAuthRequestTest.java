package org.scribe.http;

import static org.junit.Assert.*;

import org.junit.*;

import org.scribe.http.OAuthRequest;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;

public class OAuthRequestTest
{

  private OAuthRequest request;

  @Before
  public void setup()
  {
    request = new OAuthRequest(Verb.GET, "http://example.com");
  }

  @Test
  public void shouldAddOAuthParamters()
  {
    request.addOAuthParameter(OAuthConstants.TOKEN.getParamName(), "token");
    request.addOAuthParameter(OAuthConstants.NONCE.getParamName(), "nonce");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP.getParamName(), "ts");
    request.addOAuthParameter(OAuthConstants.SCOPE.getParamName(), "feeds");

    assertEquals(4, request.getOauthParameters().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfParameterIsNotOAuth()
  {
    request.addOAuthParameter("otherParam", "value");
  }
}
