package org.scribe.http;

import org.scribe.model.Verb;

/**
 * Factory for generating oauth requests. This is an interim solution
 * for unit testing
 */
public class OAuthRequestFactory {

    public OAuthRequest createRequest(final Verb verb, final String tokenEndpoint) {
        return new OAuthRequest(verb, tokenEndpoint);
    }
}
