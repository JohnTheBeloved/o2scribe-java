package org.scribe.model;

import java.io.Serializable;

import org.scribe.utils.Preconditions;

/**
 * Represents an OAuth token (either request or access token) and its secret.
 *
 * @author Pablo Fernandez
 */
public class Token implements Serializable {
  private static final long serialVersionUID = 715000866082812683L;
    private static final int HASH_SPREAD = 31;

    private final String token;
  private final String secret;
  private final String rawResponse;

  /**
   * Default constructor.
   *
   * @param inToken token value. Can't be null.
   * @param inSecret token secret. Can't be null.
   */
  public Token(final String inToken, final String inSecret) {
    this(inToken, inSecret, null);
  }

  public Token(final String inToken, final String inSecret, final String raw) {
    Preconditions.checkNotNull(inToken, "Token can't be null");
    Preconditions.checkNotNull(inSecret, "Secret can't be null");

    this.token = inToken;
    this.secret = inSecret;
    this.rawResponse = raw;
  }

  public String getToken() {
    return token;
  }

  public String getSecret() {
    return secret;
  }

  public String getRawResponse() {
    if (rawResponse == null) {
      throw new IllegalStateException(
              "This token object was not constructed by scribe and does not have a rawResponse");
    }
    return rawResponse;
  }

  @Override
  public String toString() {
    return String.format("Token[%s , %s]", token, secret);
  }

  /**
   * Returns true if the token is empty (token = "", secret = "").
   */
  public boolean isEmpty() {
    return "".equals(this.token) && "".equals(this.secret);
  }

  /**
   * Factory method that returns an empty token (token = "", secret = "").
   *
   * Useful for two legged OAuth.
   */
  public static Token empty() {
    return new Token("", "");
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    Token that = (Token) o;
    return token.equals(that.token) && secret.equals(that.secret);
  }

  @Override
  public int hashCode() {
    return HASH_SPREAD * token.hashCode() + secret.hashCode();
  }
}
