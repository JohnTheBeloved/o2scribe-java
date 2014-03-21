package org.scribe.builder;

import java.io.OutputStream;

import org.scribe.builder.api.Api;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.SignatureType;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;

/**
 * Implementation of the Builder pattern, with a fluent interface that creates a.
 * {@link OAuthService}
 *
 * @author Pablo Fernandez
 *
 */
public class ServiceBuilder {
  private String apiKey;
  private String apiSecret;
  private String callback;
  private Api api;
  private String scope;
  private SignatureType signatureType;
  private OutputStream debugStream;

  /**
   * Default constructor.
   */
  public ServiceBuilder() {
    this.callback = OAuthConstants.OUT_OF_BAND.getParamName();
    this.signatureType = SignatureType.Header;
    this.debugStream = null;
  }

  /**
   * Configures the {@link Api}.
   *
   * @param apiClass the class of one of the existent {@link Api}s on org.scribe.api package
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder provider(final Class<? extends Api> apiClass) {
    this.api = createApi(apiClass);
    return this;
  }

  private Api createApi(final Class<? extends Api> apiClass) {
    Preconditions.checkNotNull(apiClass, "Api class cannot be null");

    try {
      return apiClass.newInstance();
    } catch (Exception e) {
      throw new OAuthException("Error while creating the Api object", e);
    }
  }

  /**
   * Configures the {@link Api}
   *
   * Overloaded version. Let's you use an instance instead of a class.
   *
   * @param inApi instance of {@link Api}s
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder provider(final Api inApi) {
      Preconditions.checkNotNull(inApi, "Api cannot be null");
      this.api = inApi;
      return this;
  }

  /**
   * Adds an OAuth callback url.
   *
   * @param inCallback callback url. Must be a valid url or 'oob' for out of band OAuth
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder callback(final String inCallback) {
    Preconditions.checkNotNull(inCallback, "Callback can't be null");
    this.callback = inCallback;
    return this;
  }

  /**
   * Configures the api key.
   *
   * @param inApiKey The api key for your application
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder apiKey(final String inApiKey) {
    Preconditions.checkEmptyString(inApiKey, "Invalid Api key");
    this.apiKey = inApiKey;
    return this;
  }

  /**
   * Configures the api secret.
   *
   * @param inApiSecret The api secret for your application
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder apiSecret(final String inApiSecret) {
    Preconditions.checkEmptyString(inApiSecret, "Invalid Api secret");
    this.apiSecret = inApiSecret;
    return this;
  }

  /**
   * Configures the OAuth scope. This is only necessary in some APIs (like Google's).
   *
   * @param inScope The OAuth scope
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder scope(final String inScope) {
    Preconditions.checkEmptyString(inScope, "Invalid OAuth scope");
    this.scope = inScope;
    return this;
  }

  /**
   * Configures the signature type, choose between header, querystring, etc. Defaults to Header.
   *
   * @param type The Signature Type
   * @return the {@link ServiceBuilder} instance for method chaining
   */
  public ServiceBuilder signatureType(final SignatureType type) {
    Preconditions.checkNotNull(type, "Signature type can't be null");
    this.signatureType = type;
    return this;
  }

  public ServiceBuilder debugStream(final OutputStream stream) {
    Preconditions.checkNotNull(stream, "debug stream can't be null");
    this.debugStream = stream;
    return this;
  }

  public ServiceBuilder debug() {
    this.debugStream(System.out);
    return this;
  }

  /**
   * Returns the fully configured {@link OAuthService}.
   *
   * @return fully configured {@link OAuthService}
   */
  public OAuthService build() {
    Preconditions.checkNotNull(api, "You must specify a valid api through the provider() method");
    Preconditions.checkEmptyString(apiKey, "You must provide an api key");
    Preconditions.checkEmptyString(apiSecret, "You must provide an api secret");
    return api.createService(new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope, debugStream));
  }
}
