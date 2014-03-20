package org.scribe.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.scribe.exceptions.OAuthException;
import org.scribe.utils.StreamUtils;

/**
 * Represents an HTTP Response.
 *
 * @author Pablo Fernandez
 */
public class Response {
  private static final String EMPTY = "";
    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;

    private int code;
  private String message;
  private String body;
  private InputStream stream;
  private Map<String, String> headers;

  Response(final HttpURLConnection connection) throws IOException {
    //FIXME: Invesitgate a way to not do this in the constructor
    try {
      connection.connect();
      code = connection.getResponseCode();
      message = connection.getResponseMessage();
      headers = parseHeaders(connection);

      if (isSuccessful()) {
        stream = connection.getInputStream();
      } else {
        stream = connection.getErrorStream();
      }
    } catch (UnknownHostException e) {
      throw new OAuthException("The IP address of a host could not be determined.", e);
    }
  }

  private String parseBodyContents() {
    body = StreamUtils.getStreamContents(getStream());
    return body;
  }

  private Map<String, String> parseHeaders(final HttpURLConnection conn) {
    Map<String, String> localHeaders = new HashMap<String, String>();
    for (String key : conn.getHeaderFields().keySet()) {
        localHeaders.put(key, conn.getHeaderFields().get(key).get(0));
    }
    return localHeaders;
  }

  public boolean isSuccessful() {
    return getCode() >= STATUS_OK && getCode() < STATUS_BAD_REQUEST;
  }

  /**
   * Obtains the HTTP Response body.
   *
   * @return response body
   */
  public String getBody() {
    //CHECKSTYLE.OFF: AvoidInlineConditionals - Lazily parse body
    return body != null ? body : parseBodyContents();
    //CHECKSTYLE.ON
  }

  /**
   * Obtains the meaningful stream of the HttpUrlConnection, either inputStream.
   * or errorInputStream, depending on the status code
   *
   * @return input stream / error stream
   */
  public InputStream getStream() {
    return stream;
  }

  /**
   * Obtains the HTTP status code.
   *
   * @return the status code
   */
  public int getCode() {
    return code;
  }

  /**
   * Obtains the HTTP status message.
   * Returns <code>null</code> if the message can not be discerned from the response (not valid HTTP)
   *
   * @return the status message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Obtains a {@link Map} containing the HTTP Response Headers.
   *
   * @return headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Obtains a single HTTP Header value, or null if undefined.
   *
   * @param name the header name.
   *
   * @return header value or null.
   */
  public String getHeader(final String name) {
    return headers.get(name);
  }
}
