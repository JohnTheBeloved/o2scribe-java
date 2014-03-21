package org.scribe.oauth;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.scribe.builder.api.Api;
import org.scribe.http.OAuthRequest;
import org.scribe.http.Response;
import org.scribe.model.Encoding;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Parameter;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.processors.extractors.TokenExtractor20Impl;

/**
 *
 */
public class OAuthServiceTest {
    private static final String TEST_URL = "http://www.example.com";
    private Api mockApi;
    private static OAuthConfig defaultConfig;
    private OAuthService oauthService;
    private IMocksControl mocksControl;
    private TestRequestFactory requestFactory;

    @BeforeClass
    public static void config(){
        defaultConfig = new OAuthConfig("testkey", "testsecret");
    }

    @Before
    public void mocks(){
        mocksControl = EasyMock.createControl();
        mockApi = mocksControl.createMock(Api.class);
        requestFactory = new TestRequestFactory(mocksControl);
        oauthService = new OAuth20ServiceImpl(mockApi, defaultConfig, requestFactory);
    }

    @Test
    public void getAuthorizationUrlWithoutState(){
        expect(mockApi.getAuthorizationUrl(defaultConfig, null)).andReturn(TEST_URL);

        mocksControl.replay();
        String result = oauthService.getAuthorizationUrl();
        mocksControl.verify();
        assertEquals("Urls should be the same", TEST_URL, result);
    }

    @Test
    public void getAuthorizationUrlWithState(){
        String csrfState = "teststate";
        expect(mockApi.getAuthorizationUrl(defaultConfig, csrfState)).andReturn(TEST_URL);

        mocksControl.replay();
        String result = oauthService.getAuthorizationUrl(csrfState);
        mocksControl.verify();
        assertEquals("Urls should be the same", TEST_URL, result);
    }

    @Test
    public void signRequest(){
        OAuthRequest request = new OAuthRequest(Verb.GET, TEST_URL);
        Token token = new Token("key", "secret");

        oauthService.signRequest(token, request);
        assertTrue("Does not contain the parameter", request.getQueryStringParams().contains(
                new Parameter(OAuthConstants.ACCESS_TOKEN.getParamName(), token.getToken())));
    }

    @Test
    public void getAccessToken(){
        Verifier verifier = new Verifier("code");
        Response resp = requestFactory.getMockResponse();

        expect(mockApi.getAccessTokenVerb()).andReturn(Verb.GET);
        expect(mockApi.getAccessTokenEndpoint()).andReturn(TEST_URL);
        expect(mockApi.getAccessTokenEncoding()).andReturn(Encoding.QUERY);
        expect(mockApi.getAccessTokenExtractor()).andReturn(new TokenExtractor20Impl());
        expect(resp.getBody()).andReturn("access_token=token");

        mocksControl.replay();
        Token result = oauthService.getAccessToken(verifier);
        mocksControl.verify();
        assertEquals("Result did not contain the access token", "token", result.getToken());
    }

    @Test
    public void getAccessTokenBodyEncoded(){
        Verifier verifier = new Verifier("code");
        Response resp = requestFactory.getMockResponse();

        expect(mockApi.getAccessTokenVerb()).andReturn(Verb.GET);
        expect(mockApi.getAccessTokenEndpoint()).andReturn(TEST_URL);
        expect(mockApi.getAccessTokenEncoding()).andReturn(Encoding.FORM);
        expect(mockApi.getGrantType()).andReturn("authorization_code");
        expect(mockApi.hasGrantType()).andReturn(true);
        expect(mockApi.getAccessTokenExtractor()).andReturn(new TokenExtractor20Impl());
        expect(resp.getBody()).andReturn("access_token=token");

        mocksControl.replay();
        Token result = oauthService.getAccessToken(verifier);
        mocksControl.verify();
        assertEquals("Result did not contain the access token", "token", result.getToken());
    }

    @Test
    public void getAccessTokenWithScope(){

        oauthService = new OAuth20ServiceImpl(mockApi, new OAuthConfig("key", "secret", null, null, "scope", null), requestFactory);
        Verifier verifier = new Verifier("code");
        Response resp = requestFactory.getMockResponse();

        expect(mockApi.getAccessTokenVerb()).andReturn(Verb.GET);
        expect(mockApi.getAccessTokenEndpoint()).andReturn(TEST_URL);
        expect(mockApi.getAccessTokenEncoding()).andReturn(Encoding.QUERY);
        expect(mockApi.getAccessTokenExtractor()).andReturn(new TokenExtractor20Impl());
        expect(resp.getBody()).andReturn("access_token=token");

        mocksControl.replay();
        Token result = oauthService.getAccessToken(verifier);
        mocksControl.verify();
        assertEquals("Result did not contain the access token", "token", result.getToken());
    }

    @Test
    public void getAccessTokenBodyEncodedWithScope(){

        oauthService = new OAuth20ServiceImpl(mockApi, new OAuthConfig("key", "secret", null, null, "scope", null), requestFactory);
        Verifier verifier = new Verifier("code");
        Response resp = requestFactory.getMockResponse();

        expect(mockApi.getAccessTokenVerb()).andReturn(Verb.GET);
        expect(mockApi.getAccessTokenEndpoint()).andReturn(TEST_URL);
        expect(mockApi.getAccessTokenEncoding()).andReturn(Encoding.FORM);
        expect(mockApi.getGrantType()).andReturn("authorization_code");
        expect(mockApi.hasGrantType()).andReturn(true);
        expect(mockApi.getAccessTokenExtractor()).andReturn(new TokenExtractor20Impl());
        expect(resp.getBody()).andReturn("access_token=token");

        mocksControl.replay();
        Token result = oauthService.getAccessToken(verifier);
        mocksControl.verify();
        assertEquals("Result did not contain the access token", "token", result.getToken());
    }
}
