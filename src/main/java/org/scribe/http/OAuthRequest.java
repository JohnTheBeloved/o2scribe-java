package org.scribe.http;

import java.util.HashMap;
import java.util.Map;

import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;

/**
 * The representation of an OAuth HttpRequest.
 *
 * Adds OAuth-related functionality to the {@link Request}
 *
 * @author Pablo Fernandez
 */
public class OAuthRequest extends Request {
  private static final String OAUTH_PREFIX = "oauth_";
  private Map<String, String> oauthParameters;
  private String realm;

  /**
   * Default constructor.
   *
   * @param verb Http verb/method
   * @param url resource URL
   */
  public OAuthRequest(final Verb verb, final String url) {
    super(verb, url);
    this.oauthParameters = new HashMap<String, String>();
  }

  /**
   * Adds an OAuth parameter.
   *
   * @param key name of the parameter
   * @param value value of the parameter
   *
   * @throws IllegalArgumentException if the parameter is not an OAuth parameter
   */
  public void addOAuthParameter(final String key, final String value) {
    oauthParameters.put(checkKey(key), value);
  }

  private String checkKey(final String key) {
    if (key.startsWith(OAUTH_PREFIX) || key.equals(OAuthConstants.SCOPE.getParamName())) {
      return key;
    } else {
      throw new IllegalArgumentException(String.format("OAuth parameters must either be '%s' or start with '%s'",
              OAuthConstants.SCOPE.getParamName(), OAUTH_PREFIX));
    }
  }

  /**
   * Returns the {@link Map} containing the key-value pair of parameters.
   *
   * @return parameters as map
   */
  public Map<String, String> getOauthParameters() {
    return oauthParameters;
  }

  public void setRealm(final String inRealm) {
    this.realm = inRealm;
  }

  public String getRealm() {
    return realm;
  }

  @Override
  public String toString() {
    return String.format("@OAuthRequest(%s, %s)", getVerb(), getUrl());
  }
}
