package org.scribe.processors.resolvers;


import org.scribe.exceptions.OAuthParametersMissingException;
import org.scribe.http.OAuthRequest;
import org.scribe.model.ParameterList;
import org.scribe.utils.OAuthEncoderUtils;
import org.scribe.utils.Preconditions;


/**
 * Default implementation of {@link Resolver}. Conforms to OAuth 1.0a.
 *
 * @author Pablo Fernandez
 *
 */
public class BaseStringResolverImpl implements Resolver {
  /** Paramter string template. */
  private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

  /**
   * {@inheritDoc}
   */
  @Override
  public String extract(final OAuthRequest request) {
    checkPreconditions(request);
    String verb = OAuthEncoderUtils.encode(request.getVerb().name());
    String url = OAuthEncoderUtils.encode(request.getSanitizedUrl());
    String params = getSortedAndEncodedParams(request);
    return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
  }

  private String getSortedAndEncodedParams(final OAuthRequest request) {
    ParameterList params = new ParameterList();
    params.addAll(request.getQueryStringParams());
    params.addAll(request.getBodyParams());
    params.addAll(new ParameterList(request.getOauthParameters()));
    return params.sort().asOauthBaseString();
  }

  private void checkPreconditions(final OAuthRequest request) {
    Preconditions.checkNotNull(request, "Cannot extract base string from null object");

    if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
      throw new OAuthParametersMissingException(request);
    }
  }
}
