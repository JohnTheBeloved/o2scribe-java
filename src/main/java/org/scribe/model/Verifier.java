package org.scribe.model;

import org.scribe.utils.Preconditions;

/**
 * Represents an OAuth verifier code.
 *
 * @author Pablo Fernandez
 */
public class Verifier {

  private final String value;

  /**
   * Default constructor.
   *
   * @param inValue verifier value
   */
  public Verifier(final String inValue) {
    Preconditions.checkNotNull(inValue, "Must provide a valid string as verifier");
    this.value = inValue;
  }

  public String getValue() {
    return value;
  }
}
