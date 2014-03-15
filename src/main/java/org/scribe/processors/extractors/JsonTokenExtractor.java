package org.scribe.processors.extractors;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class JsonTokenExtractor implements TokenExtractor {
  public static final String ACCESS_TOKEN = "access_token";
  private static final String EMPTY_SECRET = "";

  public Token extract(String response) {
    Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
    try {
      JSONObject jsonResponse = new JSONObject(response);
      return new Token(jsonResponse.getString(ACCESS_TOKEN), EMPTY_SECRET, response);
    } catch (JSONException e) {
      throw new OAuthException("Cannot extract an access token.Not a valid json response");
    }
  }
}