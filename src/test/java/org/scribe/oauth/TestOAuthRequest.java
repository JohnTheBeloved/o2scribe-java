package org.scribe.oauth;

import org.scribe.http.OAuthRequest;
import org.scribe.http.Response;
import org.scribe.model.Verb;

/**
 *
 */
public class TestOAuthRequest extends OAuthRequest {
    private final Response mockResponse;

    public TestOAuthRequest(Verb verb, String tokenEndpoint, Response mockResponse) {
        super(verb, tokenEndpoint);
        this.mockResponse = mockResponse;
    }

    @Override
    public Response send(){
        return mockResponse;
    }
}
