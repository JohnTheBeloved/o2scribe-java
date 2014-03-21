package org.scribe.builder.api;

import org.scribe.model.Encoding;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.processors.extractors.TokenExtractor;

/**
 * Contains all the configuration needed to instantiate a valid {@link OAuthService}.
 *
 * @author Pablo Fernandez
 *
 */
public interface Api {
    TokenExtractor getAccessTokenExtractor();

    Verb getAccessTokenVerb();

    boolean hasGrantType();

    String getGrantType();

    Encoding getAccessTokenEncoding();

    /**
     * Returns the URL that receives the access token requests.
     *
     * @return access token URL
     */
    String getAccessTokenEndpoint();

    /**
     * Returns the URL where you should redirect your users to authenticate
     * your application.
     *
     * @param config OAuth 2.0 configuration param object
     * @param stateToken optional state token for protecting against csrf attacks
     * @return the URL where you should redirect your users
     */
    String getAuthorizationUrl(OAuthConfig config, String stateToken);

    /**
   * Creates an {@link OAuthService}.
   *
   * @param config for this service
   * @return fully configured {@link OAuthService}
   */
  OAuthService createService(OAuthConfig config);
}
