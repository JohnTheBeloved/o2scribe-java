package org.scribe.builder.api;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.processors.extractors.TokenExtractor;
import org.scribe.utils.OAuthEncoderUtils;
import org.scribe.utils.Preconditions;

/**
 * The Contact Api Oauth2 provider implementation.
 */
public class ConstantContactApi2 extends DefaultApi20 {
  /** Authorization url for ContactApi. */
  private static final String AUTHORIZE_URL = "https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://oauth2.constantcontact.com/oauth2/oauth/token?grant_type=authorization_code";
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

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }

  @Override
  public TokenExtractor getAccessTokenExtractor() {
    return new TokenExtractor() {

      public Token extract(final String response) {
        Preconditions.checkEmptyString(response,
                "Response body is incorrect. Can't extract a token from an empty string");

        String regex = "\"access_token\"\\s*:\\s*\"([^&\"]+)\"";
        Matcher matcher = Pattern.compile(regex).matcher(response);
        if (matcher.find()) {
          String token = OAuthEncoderUtils.decode(matcher.group(1));
          return new Token(token, "", response);
        } else {
          throw new OAuthException(
                  "Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
      }
    };
  }
}
