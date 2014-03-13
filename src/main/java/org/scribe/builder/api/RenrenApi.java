package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Renren(http://www.renren.com/) OAuth 2.0 based api.
 */
public class RenrenApi extends DefaultApi20 {
  private static final String AUTHORIZE_URL = "https://graph.renren.com/oauth/authorize";

  @Override
  public AccessTokenExtractor getAccessTokenExtractor() {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint() {
    return "https://graph.renren.com/oauth/token?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config, String state) {
      AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

      builder.setEndpoint(AUTHORIZE_URL)
              .setClientId(config.getApiKey())
              .setRedirectUrl(config.getCallback())
              .setScope(config.getScope())
              .setState(state)
              .setResponseType("code");
      return builder.build();
  }
}
