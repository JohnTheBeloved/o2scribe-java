package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.processors.extractors.JsonTokenExtractor;
import org.scribe.processors.extractors.TokenExtractor;

/**
 * Renren(http://www.renren.com/) OAuth 2.0 based api.
 */
public class RenrenApi extends DefaultApi20 {
  private static final String AUTHORIZE_URL = "https://graph.renren.com/oauth/authorize";

  @Override
  public TokenExtractor getAccessTokenExtractor() {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint() {
    return "https://graph.renren.com/oauth/token?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(final OAuthConfig config, final String state) {
      AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

      builder.setEndpoint(AUTHORIZE_URL)
              .setClientId(config.getApiKey())
              .setRedirectUrl(config.getCallback())
              .setScope(config.getScope())
              .setState(state)
              .setResponseType(OAuthConstants.CODE);
      return builder.build();
  }
}
