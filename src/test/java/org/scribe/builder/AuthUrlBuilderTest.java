package org.scribe.builder;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Test;

import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;

/**
 *
 */
public class AuthUrlBuilderTest {
    private static final String TEST_ENDPOINT = "http://www.example.com";
    private static final String TEST_CLIENT_ID = "testId";
    private static final String TEST_REDIRECT_URL = "http://www.example.com";
    private static final String TEST_SCOPE = "testScope";
    private static final String CSRFSTATE = "csrfstate";
    private static final String RESPONSE_TYPE = "code";
    AuthUrlBuilder builder;

    @Before
    public void init(){
        builder = new DefaultAuthUrlBuilder();
    }

    @Test
    public void createMinimalURL() throws UnsupportedEncodingException {
        String expectedResult = TEST_ENDPOINT
                + "?client_id=" + TEST_CLIENT_ID
                + "&redirect_uri=" + URLEncoder.encode(TEST_REDIRECT_URL, "utf-8");
        builder.setClientId(TEST_CLIENT_ID);
        builder.setEndpoint(TEST_ENDPOINT);
        builder.setRedirectUrl(TEST_REDIRECT_URL);
        String result = builder.build();

        assertEquals("Result did not match expected URL", expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfEndpointNotSet(){
        builder.setClientId(TEST_CLIENT_ID);
        builder.setRedirectUrl(TEST_REDIRECT_URL);
        builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfClientIdNotSet(){
        builder.setEndpoint(TEST_ENDPOINT);
        builder.setRedirectUrl(TEST_REDIRECT_URL);
        builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfRedirectURLNotSet(){
        builder.setClientId(TEST_CLIENT_ID);
        builder.setEndpoint(TEST_ENDPOINT);
        builder.build();
    }

    @Test
    public void createWithOptionsURL() throws UnsupportedEncodingException {
        String expectedResult = TEST_ENDPOINT
                + "?client_id=" + TEST_CLIENT_ID
                + "&redirect_uri=" + URLEncoder.encode(TEST_REDIRECT_URL, "utf-8")
                + "&scope=" + TEST_SCOPE
                + "&state=" + CSRFSTATE
                + "&response_type=" + RESPONSE_TYPE;
        builder.setClientId(TEST_CLIENT_ID);
        builder.setEndpoint(TEST_ENDPOINT);
        builder.setRedirectUrl(TEST_REDIRECT_URL);
        builder.setScope(TEST_SCOPE);
        builder.setState(CSRFSTATE);
        builder.setResponseType(RESPONSE_TYPE);
        String result = builder.build();

        assertEquals("Result did not match expected URL", expectedResult, result);
    }
}
