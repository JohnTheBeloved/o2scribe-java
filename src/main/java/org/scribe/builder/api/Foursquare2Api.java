package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.processors.extractors.JsonTokenExtractor;
import org.scribe.processors.extractors.TokenExtractor;


/**
 * The Foursquare Api Oauth2 provider implementation.
 */
public class Foursquare2Api extends DefaultApi20 {
  private static final String AUTHORIZATION_URL = "https://foursquare.com/oauth2/authenticate";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://foursquare.com/oauth2/access_token?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(final OAuthConfig config, final String state) {
      AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

      builder.setEndpoint(AUTHORIZATION_URL)
              .setClientId(config.getApiKey())
              .setRedirectUrl(config.getCallback())
              .setScope(config.getScope())
              .setState(state)
              .setResponseType(OAuthConstants.CODE.getParamName());
    return builder.build();
  }

  @Override
  public TokenExtractor getAccessTokenExtractor() {
    return new JsonTokenExtractor();
  }
}
