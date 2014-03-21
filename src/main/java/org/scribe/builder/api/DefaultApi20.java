package org.scribe.builder.api;

import org.scribe.http.OAuthRequestFactory;
import org.scribe.model.Encoding;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.processors.extractors.TokenExtractor;
import org.scribe.processors.extractors.TokenExtractor20Impl;

/**
 * Default implementation of the OAuth protocol, version 2.0 (draft 11)
 *
 * This class is meant to be extended by concrete implementations of the API,
 * providing the endpoints and endpoint-http-verbs.
 *
 * If your Api adheres to the 2.0 (draft 11) protocol correctly, you just need to extend
 * this class and define the getters for your endpoints.
 *
 * If your Api does something a bit different, you can override the different
 * extractors or services, in order to fine-tune the process. Please read the
 * javadocs of the interfaces to get an idea of what to do.
 *
 * @author Diego Silveira
 *
 */
public abstract class DefaultApi20 implements Api {

  /**
   * Returns the access token extractor.
   *
   * @return access token extractor
   */
  @Override
  public TokenExtractor getAccessTokenExtractor() {
    return new TokenExtractor20Impl();
  }

  /**
   * Returns the verb for the access token endpoint (defaults to GET).
   *
   * @return access token endpoint verb
   */
  @Override
  public Verb getAccessTokenVerb() {
    return Verb.GET;
  }

  @Override
  public boolean hasGrantType() { return false; }

  @Override
  public String getGrantType() {
    throw new UnsupportedOperationException("NOt Supported");
  }

  /**
   * Returns the encoding for the access token request (defaults to QUERY).
   *
   * @return access token encoding type
   */
  @Override
  public Encoding getAccessTokenEncoding() { return Encoding.QUERY; }

    /**
   * {@inheritDoc}
   */
  public OAuthService createService(final OAuthConfig config) {
    return new OAuth20ServiceImpl(this, config, new OAuthRequestFactory());
  }
}
