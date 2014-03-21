package org.scribe.oauth;

import java.util.ArrayList;
import java.util.List;

import org.easymock.IMocksControl;
import org.scribe.http.OAuthRequest;
import org.scribe.http.OAuthRequestFactory;
import org.scribe.http.Response;
import org.scribe.model.Verb;

/**
 *
 */
public class TestRequestFactory extends OAuthRequestFactory {
    private final Response mockResponse;
    private IMocksControl control;
    private List<TestOAuthRequest> requests;

    public TestRequestFactory(IMocksControl control){
        this.control = control;
        mockResponse = control.createMock(Response.class);
        requests = new ArrayList<TestOAuthRequest>();
    }

    @Override
    public OAuthRequest createRequest(final Verb verb, final String tokenEndpoint){
        TestOAuthRequest req = new TestOAuthRequest(verb, tokenEndpoint, mockResponse);
        requests.add(req);
        return req;
    }

    public List<TestOAuthRequest> getRequests(){
        return requests;
    }

    public Response getMockResponse() {
        return mockResponse;
    }
}
