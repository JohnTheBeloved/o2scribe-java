package org.scribe.oauth;


import org.scribe.builder.api.Api;
import org.scribe.http.OAuthRequest;
import org.scribe.http.OAuthRequestFactory;
import org.scribe.http.Response;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

/**
 * {@inheritDoc}.
 *
 * This is the default OAuth2 implementation
 */
public class OAuth20ServiceImpl implements OAuthService {
  private final Api api;
  private final OAuthConfig config;
  private final OAuthRequestFactory requestFactory;

  /**
   * Default constructor.
   *
   * @param inApi OAuth2.0 api information
   * @param inConfig OAuth 2.0 configuration param object
   */
  public OAuth20ServiceImpl(final Api inApi, final OAuthConfig inConfig, final OAuthRequestFactory inRequestFactory) {
    this.api = inApi;
    this.config = inConfig;
    this.requestFactory = inRequestFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Token getAccessToken(final Verifier verifier) {
    OAuthRequest request = requestFactory.createRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());

    switch (api.getAccessTokenEncoding()) {
      case QUERY:
        encodeInUrl(verifier, request);
        break;
      case FORM:
        encodeInBody(verifier, request);
        break;
      default:
        throw new UnsupportedOperationException("Unsupported operation, please select a valid encoding");
    }

    Response response = request.send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  private void encodeInBody(final Verifier verifier, final OAuthRequest request) {
    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
    request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
    request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
    if (api.hasGrantType()) {
        request.addBodyParameter(OAuthConstants.GRANT_TYPE, api.getGrantType());
    }
    if (config.hasScope()) {
        request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
    }
  }

  private void encodeInUrl(final Verifier verifier, final OAuthRequest request) {
      request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
      request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
      request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
      request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
      if (config.hasScope()) {
          request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
      }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void signRequest(final Token accessToken, final OAuthRequest request) {
    request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthorizationUrl() {
    return getAuthorizationUrl(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthorizationUrl(final String requestToken) {
    return api.getAuthorizationUrl(config, requestToken);
  }

}
