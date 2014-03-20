package org.scribe.model;

import java.io.OutputStream;

/**
 * Parameter object that groups OAuth config values.
 *
 * @author Pablo Fernandez
 */
public class OAuthConfig {
  private final String apiKey;
  private final String apiSecret;
  private final String callback;
  private final SignatureType signatureType;
  private final String scope;
  private final OutputStream debugStream;

  public OAuthConfig(final String key, final String secret) {
    this(key, secret, null, null, null, null);
  }

  public OAuthConfig(final String key,
                     final String secret,
                     final String inCallback,
                     final SignatureType type,
                     final String inScope,
                     final OutputStream stream) {
    this.apiKey = key;
    this.apiSecret = secret;
    this.callback = inCallback;
    this.signatureType = type;
    this.scope = inScope;
    this.debugStream = stream;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getApiSecret() {
    return apiSecret;
  }

  public String getCallback() {
    return callback;
  }

  public SignatureType getSignatureType() {
    return signatureType;
  }

  public String getScope() {
    return scope;
  }

  public boolean hasScope() {
    return scope != null;
  }

  //CHECKSTYLE.OFF: FinalParameters
  public void log(String message) {
    if (debugStream != null) {
      message = message + "\n";
      try {
        debugStream.write(message.getBytes("UTF8"));
      } catch (Exception e) {
        throw new RuntimeException("there were problems while writting to the debug stream", e);
      }
    }
  }
  //CHECKSTYLE.ON: FinalParameters
}
