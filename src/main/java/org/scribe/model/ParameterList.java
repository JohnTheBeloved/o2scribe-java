package org.scribe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.scribe.utils.OAuthEncoderUtils;
import org.scribe.utils.Preconditions;

/**
 * @author: Pablo Fernandez
 */
public class ParameterList {
  private static final char QUERY_STRING_SEPARATOR = '?';
  private static final String PARAM_SEPARATOR = "&";
  private static final String PAIR_SEPARATOR = "=";
  private static final String EMPTY_STRING = "";

  private final List<Parameter> params;

  public ParameterList() {
    params = new ArrayList<Parameter>();
  }

  ParameterList(final List<Parameter> inParams) {
    this.params = new ArrayList<Parameter>(inParams);
  }

  public ParameterList(final Map<String, String> map) {
    this();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      params.add(new Parameter(entry.getKey(), entry.getValue()));
    }
  }

  public void add(final String key, final String value) {
    params.add(new Parameter(key, value));
  }

  public String appendTo(final String url) {
    Preconditions.checkNotNull(url, "Cannot append to null URL");
    String queryString = asFormUrlEncodedString();
    if (queryString.equals(EMPTY_STRING)) {
      return url;
    } else {
      StringBuilder sb = new StringBuilder(url);
      if (url.indexOf(QUERY_STRING_SEPARATOR) != -1) {
          sb.append(PARAM_SEPARATOR);
      } else {
          sb.append(QUERY_STRING_SEPARATOR);
      }

      sb.append(queryString);
      return sb.toString();
    }
  }

  public String asOauthBaseString() {
    return OAuthEncoderUtils.encode(asFormUrlEncodedString());
  }

  public String asFormUrlEncodedString() {
    if (params.size() == 0) { return EMPTY_STRING; }

    StringBuilder builder = new StringBuilder();
    for (Parameter p : params) {
      builder.append('&').append(p.asUrlEncodedPair());
    }
    return builder.toString().substring(1);
  }

  public void addAll(final ParameterList other) {
    params.addAll(other.params);
  }

  public void addQuerystring(final String queryString) {
    if (queryString != null && queryString.length() > 0) {
      for (String param : queryString.split(PARAM_SEPARATOR)) {
        String [] pair = param.split(PAIR_SEPARATOR);
        String key = OAuthEncoderUtils.decode(pair[0]);

        String value = EMPTY_STRING;
        if (pair.length > 1) {
            value =  OAuthEncoderUtils.decode(pair[1]);
        }
        params.add(new Parameter(key, value));
      }
    }
  }

  public boolean contains(final Parameter param) {
    return params.contains(param);
  }

  public int size() {
    return params.size();
  }

  public ParameterList sort() {
    ParameterList sorted = new ParameterList(params);
    Collections.sort(sorted.params);
    return sorted;
  }
}
