package org.scribe.model;

import org.scribe.utils.OAuthEncoderUtils;

/**
 * @author: Pablo Fernandez
 */
public class Parameter implements Comparable<Parameter> {
  private static final String UTF = "UTF8";

  private final String key;
  private final String value;

  public Parameter(final String inKey, final String inValue) {
    this.key = inKey;
    this.value = inValue;
  }

  public String asUrlEncodedPair() {
    return OAuthEncoderUtils.encode(key).concat("=").concat(OAuthEncoderUtils.encode(value));
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null) { return false; }
    if (other == this) { return true; }
    if (!(other instanceof Parameter)) { return false; }

    Parameter otherParam = (Parameter) other;
    return otherParam.key.equals(key) && otherParam.value.equals(value);
  }

  @Override
  public int hashCode() {
    return key.hashCode() + value.hashCode();
  }

  @Override
  public int compareTo(final Parameter parameter) {
    int keyDiff = key.compareTo(parameter.key);

    if (keyDiff != 0) {
        return keyDiff;
    } else {
        return value.compareTo(parameter.value);
    }
  }
}
