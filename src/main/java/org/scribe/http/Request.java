package org.scribe.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.ParameterList;
import org.scribe.model.Verb;

/**
 * Represents an HTTP Request object.
 *
 * @author Pablo Fernandez
 */
public class Request {
  private static final String CONTENT_LENGTH = "Content-Length";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final RequestTuner NOOP = new RequestTuner() {
    @Override public void tune(final Request toBeTuned) { }
  };
  public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

  private String url;
  private Verb verb;
  private ParameterList querystringParams;
  private ParameterList bodyParams;
  private Map<String, String> headers;
  private String payload = null;
  private HttpURLConnection connection;
  private String charset;
  private byte[] bytePayload = null;
  private boolean connectionKeepAlive = false;
  private boolean followRedirects = true;
  private Long connectTimeout = null;
  private Long readTimeout = null;

  /**
   * Creates a new Http Request.
   *
   * @param inVerb Http Verb (GET, POST, etc)
   * @param inUrl url with optional querystring parameters.
   */
  public Request(final Verb inVerb, final String inUrl) {
    this.verb = inVerb;
    this.url = inUrl;
    this.querystringParams = new ParameterList();
    this.bodyParams = new ParameterList();
    this.headers = new HashMap<>();
    this.charset = Charset.defaultCharset().name();
  }

  /**
   * Execute the request and return a {@link Response}.
   *
   * @return Http Response
   * @throws RuntimeException
   *           if the connection cannot be created.
   */
  public Response send(final RequestTuner tuner) {
    try {
      createConnection();
      return doSend(tuner);
    } catch (Exception e) {
      throw new OAuthConnectionException(e);
    }
  }

  public Response send() {
    return send(NOOP);
  }

  private void createConnection() throws IOException {
    String completeUrl = getCompleteUrl();
    if (connection == null) {
      //CHECKSTYLE.OFF: AvoidInlineConditionals
      System.setProperty("http.keepAlive", connectionKeepAlive ? "true" : "false");
      //CHECKSTYLE.ON: AvoidInlineConditionals
      connection = (HttpURLConnection) new URL(completeUrl).openConnection();
      connection.setInstanceFollowRedirects(followRedirects);
    }
  }

  /**
   * Returns the complete url (host + resource + encoded querystring parameters).
   *
   * @return the complete url.
   */
  public String getCompleteUrl() {
    return querystringParams.appendTo(url);
  }

  Response doSend(final RequestTuner tuner) throws IOException {
    connection.setRequestMethod(this.verb.name());
    if (connectTimeout != null) {
      connection.setConnectTimeout(connectTimeout.intValue());
    }
    if (readTimeout != null) {
      connection.setReadTimeout(readTimeout.intValue());
    }
    addHeaders(connection);
    if (verb.equals(Verb.PUT) || verb.equals(Verb.POST)) {
      addBody(connection, getByteBodyContents());
    }
    tuner.tune(this);
    return new Response(connection);
  }

  void addHeaders(final HttpURLConnection conn) {
    for (String key : headers.keySet()) {
        conn.setRequestProperty(key, headers.get(key));
    }
  }

  void addBody(final HttpURLConnection conn, final byte[] content) throws IOException {
    conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));

    // Set default content type if none is set.
    if (conn.getRequestProperty(CONTENT_TYPE) == null) {
      conn.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
    }
    conn.setDoOutput(true);
    conn.getOutputStream().write(content);
  }

  /**
   * Add an HTTP Header to the Request.
   *
   * @param key the header name
   * @param value the header value
   */
  public void addHeader(final String key, final String value) {
    this.headers.put(key, value);
  }

  /**
   * Add a body Parameter (for POST/ PUT Requests).
   *
   * @param key the parameter name
   * @param value the parameter value
   */
  public void addBodyParameter(final String key, final String value) {
    this.bodyParams.add(key, value);
  }

  /**
   * Add a QueryString parameter.
   *
   * @param key the parameter name
   * @param value the parameter value
   */
  public void addQuerystringParameter(final String key, final String value) {
    this.querystringParams.add(key, value);
  }

  /**
   * Add body payload.
   *
   * This method is used when the HTTP body is not a form-url-encoded string,
   * but another thing. Like for example XML.
   *
   * Note: The contents are not part of the OAuth signature
   *
   * @param inPayload the body of the request
   */
  public void addPayload(final String inPayload) {
    this.payload = inPayload;
  }

  /**
   * Overloaded version for byte arrays.
   *
   * @param inPayload
   */
  public void addPayload(final byte[] inPayload) {
    this.bytePayload = inPayload.clone();
  }

  /**
   * Get a {@link ParameterList} with the query string parameters.
   *
   * @return a {@link ParameterList} containing the query string parameters.
   * @throws OAuthException if the request URL is not valid.
   */
  public ParameterList getQueryStringParams() {
    try {
      ParameterList result = new ParameterList();
      String queryString = new URL(url).getQuery();
      result.addQuerystring(queryString);
      result.addAll(querystringParams);
      return result;
    } catch (MalformedURLException mue) {
      throw new OAuthException("Malformed URL", mue);
    }
  }

  /**
   * Obtains a {@link ParameterList} of the body parameters.
   *
   * @return a {@link ParameterList}containing the body parameters.
   */
  public ParameterList getBodyParams() {
    return bodyParams;
  }

  /**
   * Obtains the URL of the HTTP Request.
   *
   * @return the original URL of the HTTP Request
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns the URL without the default port and the query string part.
   *
   * @return the OAuth-sanitized URL
   */
  public String getSanitizedUrl() {
     if (url.startsWith("http://") && (url.endsWith(":80") || url.contains(":80/"))) {
       return url.replaceAll("\\?.*", "").replaceAll(":80", "");
     } else if (url.startsWith("https://") && (url.endsWith(":443") || url.contains(":443/"))) {
       return url.replaceAll("\\?.*", "").replaceAll(":443", "");
     } else {
       return url.replaceAll("\\?.*", "");
     }
   }

  /**
   * Returns the body of the request.
   *
   * @return form encoded string
   * @throws OAuthException if the charset chosen is not supported
   */
  public String getBodyContents() {
    try {
      return new String(getByteBodyContents(), getCharset());
    } catch (UnsupportedEncodingException uee) {
      throw new OAuthException("Unsupported Charset: " + charset, uee);
    }
  }

  byte[] getByteBodyContents() {
    if (bytePayload != null) { return bytePayload; }

    String body;
    if (payload != null) {
        body = payload;
    } else {
        body = bodyParams.asFormUrlEncodedString();
    }

    try {
      return body.getBytes(getCharset());
    } catch (UnsupportedEncodingException uee) {
      throw new OAuthException("Unsupported Charset: " + getCharset(), uee);
    }
  }

  /**
   * Returns the HTTP Verb.
   *
   * @return the verb
   */
  public Verb getVerb() {
    return verb;
  }

  /**
   * Returns the connection headers as a {@link Map}.
   *
   * @return map of headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Returns the connection charset. Defaults to {@link Charset} defaultCharset if not set.
   *
   * @return charset
   */
  public String getCharset() {
    return charset;
  }

  /**
   * Sets the connect timeout for the underlying {@link HttpURLConnection}.
   *
   * @param duration duration of the timeout
   *
   * @param unit unit of time (milliseconds, seconds, etc)
   */
  public void setConnectTimeout(final int duration, final TimeUnit unit) {
    this.connectTimeout = unit.toMillis(duration);
  }

  /**
   * Sets the read timeout for the underlying {@link HttpURLConnection}.
   *
   * @param duration duration of the timeout
   * @param unit unit of time (milliseconds, seconds, etc)
   */
  public void setReadTimeout(final int duration, final TimeUnit unit) {
    this.readTimeout = unit.toMillis(duration);
  }

  /**
   * Set the charset of the body of the request.
   *
   * @param charsetName name of the charset of the request
   */
  public void setCharset(final String charsetName) {
    this.charset = charsetName;
  }

  /**
   * Sets whether the underlying Http Connection is persistent or not.
   *
   * @see http://download.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html
   * @param inConnectionKeepAlive
   */
  public void setConnectionKeepAlive(final boolean inConnectionKeepAlive) {
    this.connectionKeepAlive = inConnectionKeepAlive;
  }

  /**
   * Sets whether the underlying Http Connection follows redirects or not.
   *
   * Defaults to true (follow redirects)
   *
   * @see http://docs.oracle.com/javase/6/docs/api/java/net/HttpURLConnection.html#setInstanceFollowRedirects(boolean)
   * @param inFollowRedirects
   */
  public void setFollowRedirects(final boolean inFollowRedirects) {
    this.followRedirects = inFollowRedirects;
  }

  /*
   * We need this in order to stub the connection object for test cases.
   */
  void setConnection(final HttpURLConnection inConnection) {
    this.connection = inConnection;
  }

  @Override
  public String toString() {
    return String.format("@Request(%s %s)", getVerb(), getUrl());
  }
}
