package org.scribe.processors.extractors;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;

/**
 * An exxtract to extract Tokens from a json response with standard properties.
 */
public class JsonTokenExtractor implements TokenExtractor {
  public static final String ACCESS_TOKEN = "access_token";
  private static final String EMPTY_SECRET = "";

  public Token extract(final String response) {
    Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
    try {
      JSONObject jsonResponse = new JSONObject(response);
      return new Token(jsonResponse.getString(ACCESS_TOKEN), EMPTY_SECRET, response);
    } catch (JSONException e) {
      throw new OAuthException("Cannot extract an access token.Not a valid json response");
    }
  }
}
